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

import org.etk.sandbox.ws.model.FieldMetadata.FieldName;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 28, 2011  
 */
public class ModelContextProvider {
  
  //Defines the HashMap for Activity Space by IdentityId Rest Service
  public final static Context<ActivityStreamRest> ACTIVITY_SPACE_BY_IDENTITY_ID_CONTEXT = new Context<ActivityStreamRest>(FieldName.AS_PRETTY_ID,
                                                                                                                          FieldName.AS_TITLE) {

    @Override
    public void build(ActivityStreamRest model) {
      ModelRestBuilder.resolve(ActivityStreamRest.class, model, getFieldNames());
    }
  };
  
  
  public abstract static class Context<M> {
    

    private FieldName[] fieldNames = null;

    private Context(FieldName... fieldNames) {
      this.fieldNames = fieldNames;
    }

    public FieldName[] getFieldNames() {
      return this.fieldNames;
    }

    public abstract void build(M model);
  }
}
