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
package org.etk.sandbox.entity.plugins.model.xml;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.etk.kernel.container.configuration.ConfigurationManagerImpl;
import org.etk.sandbox.entity.plugins.config.DatasourceConfig;
import org.jibx.runtime.IMarshallingContext;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform
 * thanhvucong.78@exoplatform.com Aug 26, 2011
 */
public class Entity implements Comparable<Entity> {

  /** The name of the time stamp field for locking/synchronization */
  public static final String STAMP_FIELD           = "lastUpdatedStamp";

  public static final String STAMP_TX_FIELD        = "lastUpdatedTxStamp";

  public static final String CREATE_STAMP_FIELD    = "createdStamp";

  public static final String CREATE_STAMP_TX_FIELD = "createdTxStamp";

  private URL                documentURL;

  private Map<String, Field> fieldMap              = FastMap.newInstance();

  private Collection<PKField> pks                  = FastList.newInstance();
  private List<Field> pkFields               = FastList.newInstance();

  private List<Field>        nopkg                 = FastList.newInstance();

  private List<Index>        indexes               = FastList.newInstance();

  private List<Relation>     relations             = FastList.newInstance();

  /** The entity-name that defined this Entity. */
  protected String           entityName            = "";

  /** The table-name of the Entity */
  protected String           tableName             = "";

  /** The package-name of the Entity */
  protected String           packageName           = "";

  /** The description of the Entity */
  protected String           description           = "";

  /** An indicator to specify if this entity requires locking for updates */
  protected boolean          doLock                = false;

  /**
   * Can be used to disable automatically creating update stamp fields and
   * populating them on inserts and updates
   */
  protected boolean          noAutoStamp           = false;

  /**
   * The entity-name of the Entity that this Entity is dependent on, If empty
   * then no dependency
   */
  protected String           dependentOn           = "";

  /** An indicator to specify if this entity is never cached.
   * If true causes the delegator to not clear caches on write and to not get
   * from cache on read showing a warning messages to that effect
   */
  protected boolean          neverCache            = false;

  protected boolean          neverCheck            = false;

  protected boolean          autoClearCache        = true;

  protected Boolean          hasFieldWithAuditLog  = null;

  protected int              priority              = 0;

  public Entity() {
    documentURL = ConfigurationManagerImpl.getCurrentURL();
  }

  public URL getDocumentURL() {
    return documentURL;
  }

  /**
   * Adds the field for the Entity.
   * 
   * @param object
   */
  public void addField(Object object) {
    Field field = (Field) object;
    fieldMap.put(field.getName(), field);
  }
  /**
   * Gets FieldIterator
   * @return
   */
  public Iterator<Field> getFieldIterator() {
    return fieldMap.values().iterator();
  }

  /**
   * Adds the field for the Entity.
   * 
   * @param object
   */
  public void addPKField(Object object) {
    PKField pkfield = (PKField) object;
    Field field = fieldMap.get(pkfield.getFieldName());
    pkfield.setField(field);
    
    pkFields.add(field);
    pks.add(pkfield);
  }

  public Iterator<PKField> getPKFieldIterator() {
    return pks.iterator();
  }

  /**
   * Adds the Index for the Entity.
   * 
   * @param object
   */
  public void addIndex(Object object) {
    Index index = (Index) object;
    indexes.add(index);
  }

  /**
   * Adds the Relation for the Entity.
   * 
   * @param object
   */
  public void addRelation(Object object) {
    Relation relation = (Relation) object;
    relations.add(relation);
  }

  public Collection<Field> getFields() {
    return fieldMap.values();
  }

  public Collection<PKField> getPkgs() {
    return pks;
  }

  public int getPksSize() {
    return pks.size();
  }

  public List<Field> getNopkg() {
    return nopkg;
  }

  public List<Index> getIndexes() {
    return indexes;
  }

  public List<Relation> getRelations() {
    return relations;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public int compareTo(Entity o) {
    return getPriority() - o.getPriority();
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public String getTableName() {
    return tableName;
  }
  
  
  /** The plain table-name of the Entity without a schema name prefix */
  public String getPlainTableName() {
      return this.tableName;
  }

  
  /** The table-name of the Entity including a Schema name if specified in the datasource config */
  public String getTableName(DatasourceConfig datasourceInfo) {
      if (datasourceInfo != null && datasourceInfo.schemaName != null) {
          return datasourceInfo.schemaName + "." + this.tableName;
      } else {
          return this.tableName;
      }
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDependentOn() {
    return dependentOn;
  }

  public void setDependentOn(String dependentOn) {
    this.dependentOn = dependentOn;
  }

  public boolean isNeverCache() {
    return neverCache;
  }

  public void setNeverCache(boolean neverCache) {
    this.neverCache = neverCache;
  }

  public boolean isNeverCheck() {
    return neverCheck;
  }

  public void setNeverCheck(boolean neverCheck) {
    this.neverCheck = neverCheck;
  }

  public boolean isAutoClearCache() {
    return autoClearCache;
  }

  public void setAutoClearCache(boolean autoClearCache) {
    this.autoClearCache = autoClearCache;
  }

  public Boolean getHasFieldWithAuditLog() {
    return hasFieldWithAuditLog;
  }

  public void setHasFieldWithAuditLog(Boolean hasFieldWithAuditLog) {
    this.hasFieldWithAuditLog = hasFieldWithAuditLog;
  }

  public void setFields(Map<String, Field> fields) {
    this.fieldMap = fields;
  }

  public void setPkgs(Collection<PKField> pkgs) {
    this.pks = pkgs;
  }

  public void setNopkg(List<Field> nopkg) {
    this.nopkg = nopkg;
  }

  public void setIndexes(List<Index> indexes) {
    this.indexes = indexes;
  }

  public void setRelations(List<Relation> relations) {
    this.relations = relations;
  }

  public Iterator<Relation> getRelationsIterator() {
    return relations.iterator();
  }

  public void preGet(IMarshallingContext ictx) {
    ConfigurationMarshallerUtil.addURLToContent(documentURL, ictx);
  }

  // TODO Check binding.xml
  public Iterator<Field> getNopksIterator() {
    return this.nopkg.iterator();
  }

  public Iterator<Index> getIndexIterator() {
    return indexes.iterator();
  }

  public Field getField(String fieldName) {
    return fieldMap.get(fieldName);
  }

  public Set<String> getAllFieldNames() {
    return fieldMap.keySet();
  }

  public Collection<String> getPkFieldNames() {
    return getFieldNamesFromFieldVector(pkFields);
  }

  public List<String> getNoPkFieldNames() {
    return getFieldNamesFromFieldVector(nopkg);
  }

  public Collection<String> getFieldNamesFromFieldVector(Field... modelFields) {
    return getFieldNamesFromFieldVector(Arrays.asList(modelFields));
  }

  public List<String> getFieldNamesFromFieldVector(List<Field> modelFields) {
    List<String> nameList = FastList.newInstance();

    if (modelFields == null || modelFields.size() <= 0)
      return nameList;
    for (Field field : modelFields) {
      nameList.add(field.getName());
    }
    return nameList;
  }

  /** An indicator to specify if this entity requires locking for updates */
  public boolean getDoLock() {
    return this.doLock;
  }

  public void setDoLock(boolean doLock) {
    this.doLock = doLock;
  }
  
  /** An indicator to specify if this entity is never cached.
   * If true causes the delegator to not clear caches on write and to not get
   * from cache on read showing a warning messages to that effect
   */
  public boolean getNeverCache() {
      return this.neverCache;
  }

 
  /**
   * An indicator to specific if this entity should ignore automatic DB checks.
   * This should be set when the entity is mapped to a database view to prevent
   * warnings and attempts to modify the schema.     
   */
  public boolean getNeverCheck() {
      return neverCheck;
  }


  public boolean lock() {
    if (doLock && isField(STAMP_FIELD)) {
      return true;
    } else {
      doLock = false;
      return false;
    }
  }

  public boolean isField(String fieldName) {
    if (fieldName == null)
      return false;
    for (Field field : fieldMap.values()) {
      if (field.getName().equals(fieldName))
        return true;
    }
    return false;
  }

  public Field getOnlyPk() {
    if (this.pks.size() == 1) {
      return this.pkFields.get(0);
    } else {
      throw new IllegalArgumentException("Error in getOnlyPk, the [" + this.getEntityName()
          + "] entity has more than one pk!");
    }
  }

  public List<Field> getPkFieldsUnmodifiable() {
    return Collections.unmodifiableList(this.pkFields);
  }

  public String fieldNameString() {
    return fieldNameString(", ", "");
  }

  public String fieldNameString(String separator, String afterLast) {
    Field[] fields = fieldMap.values().toArray(new Field[0]);
    return nameString(Arrays.asList(fields), separator, afterLast);
  }

  public String nameString(List<Field> flds, String separator, String afterLast) {
    StringBuilder returnString = new StringBuilder();

    if (flds.size() < 1) {
      return "";
    }

    int i = 0;

    for (; i < flds.size() - 1; i++) {
      returnString.append(flds.get(i).getName());
      returnString.append(separator);
    }
    returnString.append(flds.get(i).getName());
    returnString.append(afterLast);
    return returnString.toString();
  }
}
