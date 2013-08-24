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
package org.etk.sandbox.ws.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etk.sandbox.ws.model.FieldMetadata.FieldName;

/**
 * Created by The eXo Platform SAS
 * Author : Thanh_VuCong
 *          thanhvc@exoplatform.com
 * Sep 27, 2011  
 */
public class ModelRestBuilder {
    
    /**
     * Defines the Field Type which is used in the Rest Service.
     * @author thanh_vucong
     *
     * @param <D> the data type
     */
    public abstract static class FieldType<D, V> {
      
      /** Define the STRING DataType*/
      public static final FieldType<String, String> STRING_TYPE = new FieldType<String, String>(String.class, "") {};
      
      /** Define the ActivityStreamRest DataType*/
      public static final FieldType<ActivityStreamRest, List<ActivityStreamRest>> ACTIVITY_STREAM_REST = new FieldType<ActivityStreamRest, List<ActivityStreamRest>>(ActivityStreamRest.class, new ArrayList<ActivityStreamRest>()) {};
      
      
      /** Define the ActivityRest DataType*/
      public static final FieldType<ActivityRest, List<ActivityRest>> ACTIVITY_REST_TYPE = new FieldType<ActivityRest, List<ActivityRest>>(ActivityRest.class, new ArrayList<ActivityRest>()) {};
      
      //
      private final Class<D> javaType;
      private final V defaultValue;
           
      private FieldType(Class<D> javaType, V defaultValue) {
        this.javaType = javaType;
        this.defaultValue = defaultValue;
      }

      public V getDefaultValue() {
        return defaultValue;
      }
     
    }//end type class
    
   
    /**
     * Factory method which use to get the default value from provided object.
     * 
     * @param object
     * @return
     */
    public static Object getDefaultValue(Object value) {
      if (value instanceof String) {
        return FieldType.STRING_TYPE.getDefaultValue();
      } else if (value instanceof ActivityStreamRest) {
        return FieldType.ACTIVITY_STREAM_REST.getDefaultValue();
      } else if (value instanceof  ActivityRest) {
        return FieldType.ACTIVITY_REST_TYPE.getDefaultValue();
      }
      return new HashMap<String, Object>();
    }
    
    /**
     * Resolves the model object with Context.
     * @param clazz
     * @param instanceModel
     * @param fieldNames
     */
    public final static void resolve(Class<?> clazz, Map<String, Object> instanceModel, FieldName[] fieldNames) {
      for(FieldName fieldName : fieldNames) {
        Object fieldValue = instanceModel.get(fieldName.getFieldName());
        if (fieldValue == null) {
          Object defaultValue = getDefaultValue(fieldValue);
          instanceModel.put(fieldName.getFieldName(), defaultValue);
        }
      }
    }
}
