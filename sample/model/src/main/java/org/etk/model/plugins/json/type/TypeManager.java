package org.etk.model.plugins.json.type;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.etk.model.core.entity.EntityType;
import org.etk.model.core.entity.EntityTypeInfo;
/**
 * Factory to make the JSON, JDBC, JSR source data.
 * @author thanh_vucong
 *
 */
public class TypeManager {

  /** . */
  private final Object entityTypeInfosLock = new Object();

  /** . */
  private volatile Map<String, EntityTypeInfo> entityTypeInfos = new HashMap<String, EntityTypeInfo>();
  
  public JsonEntityTypeInfo getEntityTypeInfo(EntityType entityType) {
    return (JsonEntityTypeInfo)getTypeInfo(entityType);
  }

  
  private EntityTypeInfo getTypeInfo(EntityType entityType) {
    EntityTypeInfo entityTypeInfo = null;
    if (entityType.getEntityKind() == EntityType.Kind.JSON) {
      entityTypeInfo = new JsonEntityTypeInfo(entityType);

      // Add
      synchronized (entityTypeInfosLock) {
        Map<String, EntityTypeInfo> copy = new HashMap<String, EntityTypeInfo>(entityTypeInfos);
        copy.put(entityType.getName(), entityTypeInfo);
        entityTypeInfos = copy;
      }
    }
    return entityTypeInfo;
  }
}
