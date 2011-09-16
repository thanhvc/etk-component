/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.etk.sandbox.entity.plugins.config;

import java.util.Map;

import javolution.util.FastMap;

import org.etk.kernel.container.configuration.ConfigurationException;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.PropertiesParam;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvc@exoplatform.com
 * Sep 12, 2011  
 */
public class DatasourceInfo {

  private final static String CONNECTION_PROPERTIES = "db-connection";
  
  private final static String HELPER_CLASS = "helper-class";
  private final static String SCHEMA_NAME = "schema-name";
  private final static String FIELD_TYPE_NAME = "field-type-name";
  private final static String CHECK_ON_START = "check-on-start";
  private final static String ADD_MISSING_ON_START = "add-missing-on-start";
  private final static String USE_FK_INITIALLY_DEFERRED = "use-fk-initially-deferred";
  private final static String ALIAS_VIEW_COLUMNS = "alias-view-columns";
  private final static String JOIN_STYLE = "join-style";
  private final static String USE_BINARY_TYPE_FOR_BLOB = "use-binary-type-for-blob";
  private final static String USE_ORDER_BY_NULLS = "use-order-by-nulls";
  private final static String JDBC_DRIVER = "jdbc-driver";
  private final static String JDBC_URI = "jdbc-uri";
  private final static String JDBC_USERNAME = "jdbc-username";
  private final static String JDBC_PASSWORD = "jdbc-password";
  private final static String POOL_MINSIZE = "pool-minsize";
  private final static String POOL_MAXSIZE = "pool-maxsize";
  private final static String TIME_BETWEEN = "time-between-eviction-runs-millis";

  //TODO Add more for MYSQL
  
  
  public String schemaName = null;
  public boolean useSchemas = true;
  public boolean checkOnStart = true;
  public boolean addMissingOnStart = false;
  public boolean useFks = true;
  public boolean useFkIndices = true;
  public boolean checkPrimaryKeysOnStart = false;
  public boolean checkForeignKeysOnStart = false;
  public boolean checkFkIndicesOnStart = false;
  public boolean usePkConstraintNames = true;
  public int constraintNameClipLength = 30;
  public boolean useProxyCursor = false;
  public String cursorName = "p_cursor";
  public int resultFetchSize = -1;
  public String fkStyle = null;
  public boolean useFkInitiallyDeferred = true;
  public boolean useIndices = true;
  public boolean useIndicesUnique = true;
  public boolean checkIndicesOnStart = false;
  public String joinStyle = null;
  public boolean aliasViews = true;
  public boolean alwaysUseConstraintKeyword = false;
  public boolean dropFkUseForeignKeyKeyword = false;
  public boolean useBinaryTypeForBlob = false;
  public boolean useOrderByNulls = false;
  public String tableType = null;
  public String characterSet = null;
  public String collate = null;
  public int maxWorkerPoolSize = 1;
  
  public String jdbcDriver = null;
  public String jdbcUri = null;
  public String jdbcUsername = null;
  public String jdbcPassword = null;
  public String fieldTypeName = null;
  
  Map<String, String> connectionProperties = FastMap.newInstance();
  
  public DatasourceInfo(InitParams params) throws ConfigurationException {
    
    if (params == null) {
      throw new ConfigurationException("Initializations parameters expected");
    }

    PropertiesParam prop = params.getPropertiesParam(CONNECTION_PROPERTIES);

    if (prop != null) {
      
      jdbcDriver = prop.getProperty(JDBC_DRIVER);
      if (prop.getProperty(JDBC_DRIVER) == null) {
        throw new ConfigurationException("driverClassName expected in db-connection properties section");
      }

      jdbcUri = prop.getProperty(JDBC_URI);
      if (jdbcUri == null) {
        throw new ConfigurationException("url expected in db-connection properties section");
      }

      jdbcUsername = prop.getProperty(JDBC_USERNAME);
      if (jdbcUsername == null) {
        throw new ConfigurationException("username expected in db-connection properties section");
      }

      jdbcPassword = prop.getProperty(JDBC_PASSWORD);
      if (jdbcPassword == null) {
        throw new ConfigurationException("password expected in db-connection properties section");
      }
      
      this.schemaName = prop.getProperty(SCHEMA_NAME);
      this.fieldTypeName = prop.getProperty(FIELD_TYPE_NAME);
      if (fieldTypeName == null) {
        throw new ConfigurationException("field-type-name expected in db-connection properties section");
      }
      this.checkOnStart = !"false".equals(prop.getProperty(CHECK_ON_START));
      this.checkPrimaryKeysOnStart = !"false".equals(prop.getProperty("check-pks-on-start"));
      this.addMissingOnStart = "true".equals(prop.getProperty(ADD_MISSING_ON_START));
      this.useFkInitiallyDeferred = "true".equals(prop.getProperty(USE_FK_INITIALLY_DEFERRED));
      this.aliasViews = "true".equals(prop.getProperty(ALIAS_VIEW_COLUMNS));
      this.joinStyle = prop.getProperty(JOIN_STYLE);
      this.useBinaryTypeForBlob = "true".equals(prop.getProperty(USE_BINARY_TYPE_FOR_BLOB));
      this.useOrderByNulls = "true".equals(prop.getProperty(USE_ORDER_BY_NULLS));
      
    } else {
      throw new ConfigurationException("db-connection properties expected in initializations parameters");
    }

   
  }
}

