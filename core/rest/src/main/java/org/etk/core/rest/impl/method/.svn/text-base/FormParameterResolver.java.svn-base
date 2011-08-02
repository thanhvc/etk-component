/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.rest.impl.method;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import org.exoplatform.services.rest.ApplicationContext;
import org.exoplatform.services.rest.impl.MultivaluedMapImpl;
import org.exoplatform.services.rest.method.TypeProducer;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class FormParameterResolver extends ParameterResolver<FormParam> {

  /**
   * Form generic type.
   */
  private static final Type FORM_TYPE = (ParameterizedType) MultivaluedMapImpl.class.getGenericInterfaces()[0];

  /**
   * See {@link FormParam}.
   */
  private final FormParam   formParam;

  /**
   * @param formParam FormParam
   */
  FormParameterResolver(FormParam formParam) {
    this.formParam = formParam;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object resolve(org.exoplatform.services.rest.Parameter parameter,
                        ApplicationContext context) throws Exception {
    String param = this.formParam.value();
    TypeProducer typeProducer = ParameterHelper.createTypeProducer(parameter.getParameterClass(),
                                                                   parameter.getGenericType());

    MediaType conetentType = context.getHttpHeaders().getMediaType();
    MessageBodyReader reader = context.getProviders().getMessageBodyReader(MultivaluedMap.class,
                                                                           FORM_TYPE,
                                                                           null,
                                                                           conetentType);
    if (reader == null)
      throw new IllegalStateException("Can't find appropriate entity reader for entity type "
          + MultivaluedMap.class.getName() + " and content-type " + conetentType);

    MultivaluedMap<String, String> form = (MultivaluedMap<String, String>) reader.readFrom(MultivaluedMap.class,
                                                                                           FORM_TYPE,
                                                                                           null,
                                                                                           conetentType,
                                                                                           context.getHttpHeaders()
                                                                                                  .getRequestHeaders(),
                                                                                           context.getContainerRequest()
                                                                                                  .getEntityStream());
    return typeProducer.createValue(param, form, parameter.getDefaultValue());
  }

}
