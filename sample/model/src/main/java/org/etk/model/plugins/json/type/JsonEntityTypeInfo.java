package org.etk.model.plugins.json.type;

import java.util.HashMap;
import java.util.Map;

import org.etk.model.core.entity.EntityType;
import org.etk.model.core.entity.EntityTypeInfo;

public class JsonEntityTypeInfo extends EntityTypeInfo {

  Map<String, Object> entityData = new HashMap<String, Object>();
  
  public JsonEntityTypeInfo(EntityType entityType) {
    super(entityType);
    
  }
  @Override
  public Object getValue(String propertyName) {
    return entityData.get(propertyName);
  }
  
  @Override
  public void setProperty(String propertyName, Object value) {
    entityData.put(propertyName, value);
    
  }

}
