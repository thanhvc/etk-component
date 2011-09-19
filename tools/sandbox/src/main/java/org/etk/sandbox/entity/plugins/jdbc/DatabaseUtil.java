package org.etk.sandbox.entity.plugins.jdbc;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.etk.common.logging.Logger;

import org.etk.sandbox.entity.base.concurrent.ExecutionPool;
import org.etk.sandbox.entity.base.utils.UtilTimer;
import org.etk.sandbox.entity.base.utils.UtilValidate;
import org.etk.sandbox.entity.core.GenericEntityException;
import org.etk.sandbox.entity.plugins.config.DatasourceConfig;
import org.etk.sandbox.entity.plugins.config.FieldTypeConfig;
import org.etk.sandbox.entity.plugins.datasource.GenericHelperInfo;
import org.etk.sandbox.entity.plugins.model.xml.Entity;
import org.etk.sandbox.entity.plugins.model.xml.Field;
import org.etk.sandbox.entity.plugins.model.xml.FieldType;

public class DatabaseUtil {

  public static final Logger logger = Logger.getLogger(DatabaseUtil.class);

  protected DatasourceConfig datasourceInfo = null;
  protected GenericHelperInfo helperInfo = null;
  protected FieldTypeConfig fieldTypeConfig = null;
  
  boolean isLegacy = false;
  protected ExecutorService executor;
  
  public DatabaseUtil(DatasourceConfig datasourceInfo, FieldTypeConfig fieldTypeConfig) {
    // FIXME get datasource from the Component.
    this.datasourceInfo = datasourceInfo;
    this.isLegacy = true;
  }
  
  protected <T> Future<T> submitWork(Callable<T> callable) {
    if (this.executor == null) {
      FutureTask<T> task = new FutureTask<T>(callable);
      task.run();
      return task;
    }
    return this.executor.submit(callable);
  }

  protected <T> List<Future<T>> submitAll(Collection<? extends Callable<T>> tasks) {
    List<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());

    if (this.executor == null) {
      for (Callable<T> callable : tasks) {
        FutureTask<T> task = new FutureTask<T>(callable);
        task.run();
        futures.add(task);
      }
    }

    for (Callable<T> callable : tasks) {
      futures.add(this.executor.submit(callable));
    }

    return futures;
  }
  
  protected Connection getConnection() throws SQLException, GenericEntityException {
    //Gets java.sql.Connection from DatabaseService.getConnection();
    return ConnectionFactory.getConnection();
  }
  
  protected Connection getConnectionLogged(Collection<String> messages) {
    try {
      return getConnection();
    } catch (SQLException e) {
      String message = "Unable to establish a connection with the database ... Error was: " + e.toString();
      logger.error(message);
      if (messages != null)
        messages.add(message);
      return null;
    } catch (GenericEntityException e) {
      String message = "Unable to establish a connection with the database... Error was: " + e.toString();
      logger.error(message);
      if (messages != null)
        messages.add(message);
      return null;
    }
  }
  
  /* =============================================================== */
  /* =============================================================== */
  public void checkDb(Map<String, Entity> modelEntities, List<String> messages, boolean addMissing) {
    checkDb(modelEntities,
            null,
            messages,
            datasourceInfo.checkPrimaryKeysOnStart,
            (datasourceInfo.useFks && datasourceInfo.checkForeignKeysOnStart),
            (datasourceInfo.useFkIndices && datasourceInfo.checkFkIndicesOnStart),
            addMissing);
  }
  
  public void checkDb(Map<String, Entity> modelEntities,
                      List<String> colWrongSize,
                      List<String> messages,
                      boolean checkPks,
                      boolean checkFks,
                      boolean checkFkIdx,
                      boolean addMissing) {
  
    UtilTimer timer = new UtilTimer();
    timer.timerString("Start - Before Get Database Meta Data");

    // get All tables from this database
    TreeSet<String> tableNames = this.getTablesNames(messages);
    TreeSet<String> fkTableNames = tableNames == null ? null : new TreeSet<String>(tableNames);
    TreeSet<String> indexTableNames = tableNames == null ? null : new TreeSet<String>(tableNames);

    if (tableNames == null) {
      String message = "Could not get table name information from the database, aborting.";
      if (message != null)
        messages.add(message);
      logger.error(message);
    }

    timer.timerString("After Get All Table Names");

    // get ALL column info, put into the hashmap by table name.
    Map<String, Map<String, ColumnCheckInfo>> colInfo = this.getColumnInfo(tableNames,
                                                                           checkPks,
                                                                           messages);
    if (colInfo == null) {
      String message = "Could not get column information from database, aborting.";
      if (message != null)
        messages.add(message);
      logger.error(message);
      return;
    }

    timer.timerString("After Get ALL Column Info.");
    // - make sure all entities have a corresponding table
    // - list all tables that do not have a corresponding does not match number
    // of entity fields
    // - list all columns that do not have a corressponding field.
    // - make sure each corresponding column is od the correct type.
    // - List all fields that do not have a corresponding column.

    timer.timerString("Before Individual Table/Column Check");
    List<Entity> modelEntityList = new ArrayList<Entity>(modelEntities.values());
    Collections.sort(modelEntityList);
    int curEnt = 0;
    int totalEnt = modelEntityList.size();
    List<Entity> entitiesAdded = FastList.newInstance();
    String schemaName;
    try {
      DatabaseMetaData dbData = this.getDatabaseMetaData(null, messages);
      schemaName = getSchemaName(dbData);
    } catch (SQLException e) {
      String message = "Could not get schema name the database, aborting.";
      if (messages != null)
        messages.add(message);
      logger.error(message);
      return;
    }

    // Create the task to create table
    List<Future<CreateTableCallable>> tableFutures = FastList.newInstance();
    for (Entity entity : modelEntityList) {
      curEnt++;

      if (entity.getNeverCheck()) {
        String entMessage = "(" + timer.timeSinceLast() + "ms) NOT Checking #" + curEnt + "/"
            + totalEnt + " Entity " + entity.getEntityName();
        logger.info(entMessage);
        if (messages != null)
          messages.add(entMessage);
        continue;
      }

      String plainTableName = entity.getTableName();
      String tableName;

      if (UtilValidate.isNotEmpty(schemaName)) {
        tableName = schemaName + "." + plainTableName;
      } else {
        tableName = plainTableName;
      }
      String entMessage = "(" + timer.timeSinceLast() + "ms) Checking #" + curEnt + "/" + totalEnt
          + " Entity " + entity.getEntityName() + " with table " + tableName;

      logger.debug(entMessage);
      if (messages != null)
        messages.add(entMessage);

    }// end for
    
  }
  
  /*=======================================================================*/
  public Map<String, Map<String, ColumnCheckInfo>> getColumnInfo(Set<String> tableNames,
                                                                 boolean getPks,
                                                                 Collection<String> messages) {
    if (tableNames.size() == 0) {
      return FastMap.newInstance();
    }

    Connection connection = null;
    try {
      connection = getConnectionLogged(messages);
      if (connection == null) {
        return null;
      }

      DatabaseMetaData dbData = null;
      try {
        dbData = connection.getMetaData();
      } catch (SQLException e) {
        String message = "Unable to get database meta data... Error was:" + e.toString();
        logger.error(message);
        if (messages != null)
          messages.add(message);

        try {
          connection.close();
        } catch (SQLException e2) {
          String message2 = "Unable to close database connection, continuing anyway... Error was:"
              + e2.toString();
          logger.error(message2);
          if (messages != null)
            messages.add(message2);
        }
        return null;
      }

      // processing the columns information.
      if (logger.isDebugEnabled())
        logger.debug("Getting Column Info From Database");

      Map<String, Map<String, ColumnCheckInfo>> colInfo = FastMap.newInstance();
      try {
        String lookupSchemaName = getSchemaName(dbData);
        boolean needsUpperCase = false;

        try {
          needsUpperCase = dbData.storesLowerCaseIdentifiers()
              || dbData.storesMixedCaseIdentifiers();
        } catch (SQLException e) {
          String message = "Error getting identifier case information... Error was:" + e.toString();
          logger.error(message);
          if (messages != null)
            messages.add(message);
        }

        boolean foundCols = false;
        ResultSet rsCols = dbData.getColumns(null, lookupSchemaName, null, null);
        if (rsCols.next() == false) {
          try {
            rsCols.close();
          } catch (SQLException e) {
            String message = "Unable to close ResultSet for column list, continuing anyway... Error was:"
                + e.toString();
            logger.error(message);
            if (messages != null)
              messages.add(message);
          }
          rsCols = dbData.getColumns(null, lookupSchemaName, "%", "%");

          if (rsCols.next())
            foundCols = true;
        } else {
          foundCols = true;
        }

        if (foundCols) {
          do {
            try {
              ColumnCheckInfo ccInfo = new ColumnCheckInfo();

              ccInfo.tableName = ColumnCheckInfo.fixupTableName(rsCols.getString("TABLE_NAME"),
                                                                lookupSchemaName,
                                                                needsUpperCase);
              // ignore the column info if the table name is not in the list we
              // are concerned with
              if (!tableNames.contains(ccInfo.tableName)) {
                continue;
              }

              ccInfo.columnName = rsCols.getString("COLUMN_NAME");
              if (needsUpperCase && ccInfo.columnName != null) {
                ccInfo.columnName = ccInfo.columnName.toUpperCase();
              }
              // NOTE: this may need a toUpperCase in some cases, keep an eye on
              // it
              ccInfo.typeName = rsCols.getString("TYPE_NAME");
              ccInfo.columnSize = rsCols.getInt("COLUMN_SIZE");
              ccInfo.decimalDigits = rsCols.getInt("DECIMAL_DIGITS");
              // NOTE: this may need a toUpperCase in some cases, keep an eye on
              // it
              ccInfo.isNullable = rsCols.getString("IS_NULLABLE");

              Map<String, ColumnCheckInfo> tableColInfo = colInfo.get(ccInfo.tableName);
              if (tableColInfo == null) {
                tableColInfo = FastMap.newInstance();
                colInfo.put(ccInfo.tableName, tableColInfo);
              }
              tableColInfo.put(ccInfo.columnName, ccInfo);
            } catch (SQLException e) {
              String message = "Error getting column info for column. Error was:" + e.toString();
              logger.error(message);
              if (messages != null)
                messages.add(message);
              continue;
            }
          } while (rsCols.next());
        }

        try {
          rsCols.close();
        } catch (SQLException e) {
          String message = "Unable to close ResultSet for column list, continuing anyway... Error was:"
              + e.toString();
          logger.error(message);
          if (messages != null)
            messages.add(message);
        }
        if (getPks) {
          int pkCount = 0;

          // first try getting all at once for databases that support that and
          // can generally perform WAY better, if that fails get one at a time
          // so it will at least work
          try {
            ResultSet rsPks = dbData.getPrimaryKeys(null, lookupSchemaName, null);
            pkCount += checkPrimaryKeyInfo(rsPks,
                                           lookupSchemaName,
                                           needsUpperCase,
                                           colInfo,
                                           messages);
          } catch (Exception e1) {
            logger.warn("Error getting primary key info from database with null tableName, will try other means: " + e1.toString());
          }
          if (pkCount == 0) {
            try {
              ResultSet rsPks = dbData.getPrimaryKeys(null, lookupSchemaName, "%");
              pkCount += checkPrimaryKeyInfo(rsPks,
                                             lookupSchemaName,
                                             needsUpperCase,
                                             colInfo,
                                             messages);
            } catch (Exception e1) {
              logger.warn("Error getting primary key info from database with % tableName, will try other means: "
                  + e1.toString());
            }
          }
          if (pkCount == 0) {
            logger.info("Searching in " + tableNames.size() + " tables for primary key fields ...");
            List<Future<AbstractCountingCallable>> pkFetcherFutures = FastList.newInstance();
            for (String curTable : tableNames) {
              curTable = curTable.substring(curTable.indexOf('.') + 1); // cut
                                                                        // off
                                                                        // schema
                                                                        // name
              pkFetcherFutures.add(submitWork(createPrimaryKeyFetcher(dbData,
                                                                      lookupSchemaName,
                                                                      needsUpperCase,
                                                                      colInfo,
                                                                      messages,
                                                                      curTable)));
            }
            for (AbstractCountingCallable pkFetcherCallable : ExecutionPool.getAllFutures(pkFetcherFutures)) {
              pkCount += pkFetcherCallable.updateData(messages);
            }
          }

          logger.info("Reviewed " + pkCount + " primary key fields from database.");
        }

      } catch (SQLException e) {
        String message = "Error getting column meta data for Error was: [" + e.toString()
            + "]. Not checking columns.";
        logger.error(message, e);
        if (messages != null)
          messages.add(message);
        // we are returning an empty set in this case because databases like
        // SapDB throw an exception when there are no tables in the database
        // colInfo = null;
      }
      return colInfo;

    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          String message = "Unable to close database connection, continuing anyway... Error was:"
              + e.toString();
          logger.error(message);
          if (messages != null)
            messages.add(message);
        }
      }
    }
  }
  /*=======================================================================*/
  private AbstractCountingCallable createPrimaryKeyFetcher(final DatabaseMetaData dbData,
                                                           final String lookupSchemaName,
                                                           final boolean needsUpperCase,
                                                           final Map<String, Map<String, ColumnCheckInfo>> colInfo,
                                                           final Collection<String> messages,
                                                           final String curTable) {
    return new AbstractCountingCallable(null, null) {
      public AbstractCountingCallable call() throws Exception {
        logger.info("Fetching primary keys for " + curTable);
        ResultSet rsPks = dbData.getPrimaryKeys(null, lookupSchemaName, curTable);
        count = checkPrimaryKeyInfo(rsPks, lookupSchemaName, needsUpperCase, colInfo, messages);
        return this;
      }
    };
  }
  
  /*=======================================================================*/
  
  public int checkPrimaryKeyInfo(ResultSet rsPks,
                                 String lookupSchemaName,
                                 boolean needsUpperCase,
                                 Map<String, Map<String, ColumnCheckInfo>> colInfo,
                                 Collection<String> messages) throws SQLException {
    int pkCount = 0;
    try {
      while (rsPks.next()) {
        pkCount++;
        try {
          String tableName = ColumnCheckInfo.fixupTableName(rsPks.getString("TABLE_NAME"),
                                                            lookupSchemaName,
                                                            needsUpperCase);
          String columnName = rsPks.getString("COLUMN_NAME");
          if (needsUpperCase && columnName != null) {
            columnName = columnName.toUpperCase();
          }
          Map<String, ColumnCheckInfo> tableColInfo = colInfo.get(tableName);
          if (tableColInfo == null) {
            // not looking for info on this table
            continue;
          }
          ColumnCheckInfo ccInfo = tableColInfo.get(columnName);
          if (ccInfo == null) {
            // this isn't good, what to do?
            logger.warn("Got primary key information for a column that we didn't get column information for: tableName=["
                + tableName + "], columnName=[" + columnName + "]");
            continue;
          }

          // KEY_SEQ short => sequence number within primary key
          // PK_NAME String => primary key name (may be null)

          ccInfo.isPk = true;
          ccInfo.pkSeq = rsPks.getShort("KEY_SEQ");
          ccInfo.pkName = rsPks.getString("PK_NAME");
        } catch (SQLException e) {
          String message = "Error getting primary key info for column. Error was:" + e.toString();
          logger.error(message);
          if (messages != null)
            messages.add(message);
          continue;
        }
      }
    } finally {
      try {
        rsPks.close();
      } catch (SQLException sqle) {
        String message = "Unable to close ResultSet for primary key list, continuing anyway... Error was:"
            + sqle.toString();
        logger.error(message);
        if (messages != null)
          messages.add(message);
      }
    }
    return pkCount;
  }
  /*=======================================================================*/
  
  public TreeSet<String> getTablesNames(Collection<String> messages) {
    Connection connection = getConnectionLogged(messages);
    if (connection == null) {
      String message = "Unable to establish a connection with the database, no additional information available.";
      logger.error(message);
      if (messages != null)
        messages.add(message);
      return null;
    }

    DatabaseMetaData dbData = this.getDatabaseMetaData(connection, messages);

    if (dbData == null) {
      return null;
    }

    printDbMiscData(dbData, connection);
    if (logger.isDebugEnabled())
      logger.debug("Getting Table Info From Database");

    // get ALL tables from this database
    TreeSet<String> tableNames = new TreeSet<String>();
    ResultSet tableSet = null;
    String lookupSchemaName = null;
    try {
      String[] types = { "TABLE", "VIEW", "ALIAS", "SYNONYM" };
      lookupSchemaName = getSchemaName(dbData);
      tableSet = dbData.getTables(null, lookupSchemaName, null, types);
      if (tableSet == null) {
        logger.warn("getTables returned null set");
      }
    } catch (SQLException e) {
      String message = "Unable to get list of table information, let's try the create anyway... Error was:"
          + e.toString();
      logger.error(message);
      if (messages != null)
        messages.add(message);

      try {
        connection.close();
      } catch (SQLException e2) {
        String message2 = "Unable to close database connection, continuing anyway... Error was:"
            + e2.toString();
        logger.error(message2);
        if (messages != null)
          messages.add(message2);
      }
      // we are returning an empty set here because databases like SapDB throw
      // an exception when there are no tables in the database
      return tableNames;
    }

    try {
      boolean needsUpperCase = false;
      try {
        needsUpperCase = dbData.storesLowerCaseIdentifiers() || dbData.storesMixedCaseIdentifiers();
      } catch (SQLException e) {
        String message = "Error getting identifier case information... Error was:" + e.toString();
        logger.error(message);
        if (messages != null)
          messages.add(message);
      }
      while (tableSet.next()) {
        try {
          String tableName = tableSet.getString("TABLE_NAME");
          // for those databases which do not return the schema name with the
          // table name (pgsql 7.3)
          boolean appendSchemaName = false;
          if (tableName != null && lookupSchemaName != null
              && !tableName.startsWith(lookupSchemaName)) {
            appendSchemaName = true;
          }
          if (needsUpperCase && tableName != null) {
            tableName = tableName.toUpperCase();
          }
          if (appendSchemaName) {
            tableName = lookupSchemaName + "." + tableName;
          }

          // NOTE: this may need a toUpperCase in some cases, keep an eye on it,
          // okay for now just do a compare with equalsIgnoreCase
          String tableType = tableSet.getString("TABLE_TYPE");
          // only allow certain table types
          if (tableType != null && !"TABLE".equalsIgnoreCase(tableType)
              && !"VIEW".equalsIgnoreCase(tableType) && !"ALIAS".equalsIgnoreCase(tableType)
              && !"SYNONYM".equalsIgnoreCase(tableType)) {
            continue;
          }

          // String remarks = tableSet.getString("REMARKS");
          tableNames.add(tableName);
          // if (Debug.infoOn()) Debug.logInfo("Found table named [" + tableName
          // + "] of type [" + tableType + "] with remarks: " + remarks,
          // module);
        } catch (SQLException e) {
          String message = "Error getting table information... Error was:" + e.toString();
          logger.error(message);
          if (messages != null)
            messages.add(message);
          continue;
        }
      }
    } catch (SQLException e) {
      String message = "Error getting next table information... Error was:" + e.toString();
      logger.error(message);
      if (messages != null)
        messages.add(message);
    } finally {
      try {
        tableSet.close();
      } catch (SQLException e) {
        String message = "Unable to close ResultSet for table list, continuing anyway... Error was:"
            + e.toString();
        logger.error(message);
        if (messages != null)
          messages.add(message);
      }

      try {
        connection.close();
      } catch (SQLException e) {
        String message = "Unable to close database connection, continuing anyway... Error was:"
            + e.toString();
        logger.error(message);
        if (messages != null)
          messages.add(message);
      }
    }
    return tableNames;

  }
  
  private static final List<Detection> detections = new ArrayList<Detection>();

  private static final String goodFormatStr;

  private static final String badFormatStr;

  private static class Detection {
    protected final String name;

    protected final boolean required;

    protected final Method method;

    protected final Object[] params;

    protected Detection(String name, boolean required, String methodName, Object... params) throws NoSuchMethodException {
      this.name = name;
      this.required = required;
      Class[] paramTypes = new Class[params.length];
      for (int i = 0; i < params.length; i++) {
        Class<?> paramType = params[i].getClass();
        if (paramType == Integer.class) {
          paramType = Integer.TYPE;
        }
        paramTypes[i] = paramType;
      }
      method = DatabaseMetaData.class.getMethod(methodName, paramTypes);
      this.params = params;
    }
  }

  static {
    try {
      detections.add(new Detection("supports transactions", true, "supportsTransactions"));
      detections.add(new Detection("isolation None", false, "supportsTransactionIsolationLevel", Connection.TRANSACTION_NONE));
      detections.add(new Detection("isolation ReadCommitted", false, "supportsTransactionIsolationLevel", Connection.TRANSACTION_READ_COMMITTED));
      detections.add(new Detection("isolation ReadUncommitted", false, "supportsTransactionIsolationLevel", Connection.TRANSACTION_READ_UNCOMMITTED));
      detections.add(new Detection("isolation RepeatableRead", false, "supportsTransactionIsolationLevel", Connection.TRANSACTION_REPEATABLE_READ));
      detections.add(new Detection("isolation Serializable", false, "supportsTransactionIsolationLevel", Connection.TRANSACTION_SERIALIZABLE));
      detections.add(new Detection("forward only type", false, "supportsResultSetType", ResultSet.TYPE_FORWARD_ONLY));
      detections.add(new Detection("scroll sensitive type", false, "supportsResultSetType", ResultSet.TYPE_SCROLL_SENSITIVE));
      detections.add(new Detection("scroll insensitive type", false, "supportsResultSetType", ResultSet.TYPE_SCROLL_INSENSITIVE));
      detections.add(new Detection("is case sensitive", false, "supportsMixedCaseIdentifiers"));
      detections.add(new Detection("stores LowerCase", false, "storesLowerCaseIdentifiers"));
      detections.add(new Detection("stores MixedCase", false, "storesMixedCaseIdentifiers"));
      detections.add(new Detection("stores UpperCase", false, "storesUpperCaseIdentifiers"));
      detections.add(new Detection("max table name length", false, "getMaxTableNameLength"));
      detections.add(new Detection("max column name length", false, "getMaxColumnNameLength"));
      detections.add(new Detection("concurrent connections", false, "getMaxConnections"));
      detections.add(new Detection("concurrent statements", false, "getMaxStatements"));
      detections.add(new Detection("ANSI SQL92 Entry", false, "supportsANSI92EntryLevelSQL"));
      detections.add(new Detection("ANSI SQL92 Intermediate", false, "supportsANSI92IntermediateSQL"));
      detections.add(new Detection("ANSI SQL92 Full", false, "supportsANSI92FullSQL"));
      detections.add(new Detection("ODBC SQL Grammar Core", false, "supportsCoreSQLGrammar"));
      detections.add(new Detection("ODBC SQL Grammar Extended", false, "supportsExtendedSQLGrammar"));
      detections.add(new Detection("ODBC SQL Grammar Minimum", false, "supportsMinimumSQLGrammar"));
      detections.add(new Detection("outer joins", true, "supportsOuterJoins"));
      detections.add(new Detection("limited outer joins", false, "supportsLimitedOuterJoins"));
      detections.add(new Detection("full outer joins", false, "supportsFullOuterJoins"));
      detections.add(new Detection("group by", true, "supportsGroupBy"));
      detections.add(new Detection("group by not in select", false, "supportsGroupByUnrelated"));
      detections.add(new Detection("column aliasing", false, "supportsColumnAliasing"));
      detections.add(new Detection("order by not in select", false, "supportsOrderByUnrelated"));
      detections.add(new Detection("alter table add column", true, "supportsAlterTableWithAddColumn"));
      detections.add(new Detection("non-nullable column", true, "supportsNonNullableColumns"));
      // detections.add(new Detection("", false, "", ));
    } catch (NoSuchMethodException e) {
      throw (InternalError) new InternalError(e.getMessage()).initCause(e);
    }
    int maxWidth = 0;
    for (Detection detection : detections) {
      if (detection.name.length() > maxWidth) {
        maxWidth = detection.name.length();
      }
    }
    goodFormatStr = "- %-" + maxWidth + "s [%s]%s";
    badFormatStr = "- %-" + maxWidth + "s [ DETECTION FAILED ]%s";
  }
  
  /*===============================Inner Class =============================*/
  private class CreateTableCallable implements Callable<CreateTableCallable> {
    private final Entity entity;

    private final Map<String, Entity> modelEntities;

    private final String tableName;

    private String message;

    private boolean success;

    protected CreateTableCallable(Entity entity, Map<String, Entity> modelEntities, String tableName) {
      this.entity = entity;
      this.modelEntities = modelEntities;
      this.tableName = tableName;
    }

    public CreateTableCallable call() throws Exception {
      String errMsg = createTable(entity, modelEntities, false);
      if (UtilValidate.isNotEmpty(errMsg)) {
        this.success = false;
        this.message = "Could not create table [" + tableName + "]: " + errMsg;
        logger.error(this.message);
      } else {
        this.success = true;
        this.message = "Created table [" + tableName + "]";
        logger.warn(this.message);
      }
      return this;
    }

    protected void updateData(Collection<String> messages, List<Entity> entitiesAdded) {
      if (this.success) {
        entitiesAdded.add(entity);
        if (messages != null) {
          messages.add(this.message);
        }
      } else {
        if (messages != null) {
          messages.add(this.message);
        }
      }
    }
  }
  
  private abstract class AbstractCountingCallable implements Callable<AbstractCountingCallable> {
    protected final Entity entity;
    protected final Map<String, Entity> modelEntities;
    protected final List<String> messages = FastList.newInstance();
    protected int count;

    protected AbstractCountingCallable(Entity entity, Map<String, Entity> modelEntities) {
        this.entity = entity;
        this.modelEntities = modelEntities;
    }

    protected int updateData(Collection<String> messages) {
        if (messages != null && UtilValidate.isNotEmpty(this.messages)) {
            messages.addAll(messages);
        }
        return count;
    }
}
  /* ====================================================================== */

  public DatabaseMetaData getDatabaseMetaData(Connection connection, Collection<String> messages) {
    if (connection == null) {
      connection = getConnectionLogged(messages);
    }

    if (connection == null) {
      String message = "Unable to establish a connection with the database, no additional information available.";
      logger.error(message);
      if (messages != null)
        messages.add(message);
      return null;
    }

    DatabaseMetaData dbData = null;
    try {
      dbData = connection.getMetaData();
    } catch (SQLException e) {
      String message = "Unable to get database meta data... Error was:" + e.toString();
      logger.error(message);
      if (messages != null) {
        messages.add(message);
      }
      return null;
    }

    if (dbData == null) {
      logger.warn("Unable to get database meta data; method returned null");
    }

    return dbData;
  }
  public void printDbMiscData(DatabaseMetaData dbData, Connection con) {
    if (dbData == null) {
      return;
    }
    // Database Info
    if (logger.isInfoEnabled()) {
      try {
        logger.info("Database Product Name is " + dbData.getDatabaseProductName());
        logger.info("Database Product Version is " + dbData.getDatabaseProductVersion());
      } catch (SQLException e) {
        logger.warn("Unable to get Database name & version information");
      }
    }
    // JDBC Driver Info
    if (logger.isInfoEnabled()) {
      try {
        logger.info("Database Driver Name is " + dbData.getDriverName());
        logger.info("Database Driver Version is " + dbData.getDriverVersion());
        logger.info("Database Driver JDBC Version is " + dbData.getJDBCMajorVersion() + "."
            + dbData.getJDBCMinorVersion());
      } catch (SQLException e) {
        logger.warn("Unable to get Driver name & version information");
      } catch (AbstractMethodError ame) {
        logger.warn("Unable to get Driver JDBC Version");
      }
    }
    // Db/Driver support settings
    if (logger.isInfoEnabled()) {
      logger.info("Database Setting/Support Information (those with a * should be true):");
      for (Detection detection : detections) {
        String requiredFlag = detection.required ? "*" : "";
        try {
          Object result = detection.method.invoke(dbData, detection.params);
          logger.info(String.format(goodFormatStr, detection.name, result, requiredFlag));
        } catch (Exception e) {
          e.printStackTrace();
          logger.info(e.getMessage(), e);
          logger.warn(String.format(badFormatStr, detection.name, requiredFlag));
        }
      }
      try {
        logger.info("- default fetchsize        [" + con.createStatement().getFetchSize() + "]");
      } catch (Exception e) {
        logger.info(e.getMessage(), e);
        logger.warn("- default fetchsize        [ DETECTION FAILED ]");
      }
      try {
        // this doesn't work in HSQLDB, other databases?
        // crashed (vm-death) with MS SQL Server 2000, runs properly with MS SQL
        // Server 2005
        // Debug.logInfo("- named parameters         [" +
        // dbData.supportsNamedParameters() + "]", module);
        logger.info("- named parameters         [ SKIPPED ]");
      } catch (Exception e) {
        logger.info(e.getMessage(), e);
        logger.warn("- named parameters JDBC-3  [ DETECTION FAILED ]");
      }
    }
  }
  
  public String getSchemaName(DatabaseMetaData dbData) throws SQLException {
    if (this.datasourceInfo.useSchemas && dbData.supportsSchemasInTableDefinitions()) {
      if (UtilValidate.isNotEmpty(this.datasourceInfo.schemaName)) {
        if (dbData.storesLowerCaseIdentifiers()) {
          return this.datasourceInfo.schemaName.toLowerCase();
        } else if (dbData.storesUpperCaseIdentifiers()) {
          return this.datasourceInfo.schemaName.toUpperCase();
        } else {
          return this.datasourceInfo.schemaName;
        }
      } else {
        return dbData.getUserName();
      }
    }
    return null;
  }
  
  public String createTable(Entity entity, Map<String, Entity> modelEntities, boolean addFks) {
    if (entity == null) {
        return "ModelEntity was null and is required to create a table";
    }
    
    Connection connection = null;
    Statement stmt = null;

    try {
        connection = getConnection();
    } catch (SQLException e) {
        String errMsg = "Unable to establish a connection with the database ... Error was: " + e.toString();
        logger.error(errMsg, e);
        return errMsg;
    } catch (GenericEntityException e) {
        String errMsg = "Unable to establish a connection with the database ... Error was: " + e.toString();
        logger.error(errMsg, e);
        return errMsg;
    }

    StringBuilder sqlBuf = new StringBuilder("CREATE TABLE ");
    sqlBuf.append(entity.getTableName(this.datasourceInfo));
    sqlBuf.append(" (");
    Iterator<Field> fieldIter = entity.getFieldIterator();
    while (fieldIter.hasNext()) {
        Field field = fieldIter.next();
        FieldType type = fieldTypeConfig.getFieldType(field.getType());
        if (type == null) {
            return "Field type [" + type + "] not found for field [" + field.getName() + "] of entity [" + entity.getEntityName() + "], not creating table.";
        }

        sqlBuf.append(field.getColName());
        sqlBuf.append(" ");
        sqlBuf.append(type.getSqlType());

        if ("String".equals(type.getJavaType()) || "java.lang.String".equals(type.getJavaType())) {
            // if there is a characterSet, add the CHARACTER SET arg here
            if (UtilValidate.isNotEmpty(this.datasourceInfo.characterSet)) {
                sqlBuf.append(" CHARACTER SET ");
                sqlBuf.append(this.datasourceInfo.characterSet);
            }
            // if there is a collate, add the COLLATE arg here
            if (UtilValidate.isNotEmpty(this.datasourceInfo.collate)) {
                sqlBuf.append(" COLLATE ");
                sqlBuf.append(this.datasourceInfo.collate);
            }
        }

        if (field.getIsNotNull() || field.getIsPk()) {
            if (this.datasourceInfo.alwaysUseConstraintKeyword) {
                sqlBuf.append(" CONSTRAINT NOT NULL, ");
            } else {
                sqlBuf.append(" NOT NULL, ");
            }
        } else {
            sqlBuf.append(", ");
        }
    }

    /*
    
    String pkName = makePkConstraintName(entity, this.datasourceInfo.constraintNameClipLength);
    if (this.datasourceInfo.usePkConstraintNames) {
        sqlBuf.append("CONSTRAINT ");
        sqlBuf.append(pkName);
    }
    sqlBuf.append(" PRIMARY KEY (");
    entity.colNameString(entity.getPkFieldsUnmodifiable(), sqlBuf, "");
    sqlBuf.append(")");

    if (addFks) {
        // NOTE: This is kind of a bad idea anyway since ordering table creations is crazy, if not impossible

        // go through the relationships to see if any foreign keys need to be added
        Iterator<Relation> relationsIter = entity.getRelationsIterator();
        while (relationsIter.hasNext()) {
            Relation modelRelation = relationsIter.next();
            if ("one".equals(modelRelation.getType())) {
                Entity relModelEntity = modelEntities.get(modelRelation.getRelEntityName());
                if (relModelEntity == null) {
                    logger.logError("Error adding foreign key: ModelEntity was null for related entity name " + modelRelation.getRelEntityName());
                    continue;
                }
                if (relModelEntity instanceof ViewEntity) {
                    logger.error("Error adding foreign key: related entity is a view entity for related entity name " + modelRelation.getRelEntityName());
                    continue;
                }

                String fkConstraintClause = makeFkConstraintClause(entity, modelRelation, relModelEntity, this.datasourceInfo.constraintNameClipLength, this.datasourceInfo.fkStyle, this.datasourceInfo.useFkInitiallyDeferred);
                if (UtilValidate.isNotEmpty(fkConstraintClause)) {
                    sqlBuf.append(", ");
                    sqlBuf.append(fkConstraintClause);
                } else {
                    continue;
                }
            }
        }
    }*/
    

    sqlBuf.append(")");

    // if there is a tableType, add the TYPE arg here
    if (UtilValidate.isNotEmpty(this.datasourceInfo.tableType)) {
     // jaz:20101229 - This appears to be only used by mysql and now mysql has
        // deprecated (and in 5.5.x removed) the use of the TYPE keyword. This is
        // changed to ENGINE which is supported starting at 4.1
        sqlBuf.append(" ENGINE ");
        //sqlBuf.append(" TYPE ");
        sqlBuf.append(this.datasourceInfo.tableType);
    }

    // if there is a characterSet, add the CHARACTER SET arg here
    if (UtilValidate.isNotEmpty(this.datasourceInfo.characterSet)) {
        sqlBuf.append(" CHARACTER SET ");
        sqlBuf.append(this.datasourceInfo.characterSet);
    }

    // if there is a collate, add the COLLATE arg here
    if (UtilValidate.isNotEmpty(this.datasourceInfo.collate)) {
        sqlBuf.append(" COLLATE ");
        sqlBuf.append(this.datasourceInfo.collate);
    }

    if (logger.isDebugEnabled()) logger.debug("[createTable] sql=" + sqlBuf.toString());
    try {
        stmt = connection.createStatement();
        stmt.executeUpdate(sqlBuf.toString());
    } catch (SQLException e) {
        return "SQL Exception while executing the following:\n" + sqlBuf.toString() + "\nError was: " + e.toString();
    } finally {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
          logger.error(e.getMessage(), e);
        }
    }
    return null;
}
  
  /* ====================================================================== */
  /* ====================================================================== */
  @SuppressWarnings("serial")
  public static class ColumnCheckInfo implements Serializable {
      public String tableName;
      public String columnName;
      public String typeName;
      public int columnSize;
      public int decimalDigits;
      public String isNullable; // YES/NO or "" = ie nobody knows
      public boolean isPk = false;
      public int pkSeq;
      public String pkName;

      public static String fixupTableName(String rawTableName, String lookupSchemaName, boolean needsUpperCase) {
          String tableName = rawTableName;
          // for those databases which do not return the schema name with the table name (pgsql 7.3)
          boolean appendSchemaName = false;
          if (tableName != null && lookupSchemaName != null && !tableName.startsWith(lookupSchemaName)) {
              appendSchemaName = true;
          }
          if (needsUpperCase && tableName != null) {
              tableName = tableName.toUpperCase();
          }
          if (appendSchemaName) {
              tableName = lookupSchemaName + "." + tableName;
          }
          return tableName;
      }
  }
}
