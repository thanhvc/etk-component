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

package org.exoplatform.services.rest.impl.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.exoplatform.services.rest.ApplicationContext;
import org.exoplatform.services.rest.RequestHandler;
import org.exoplatform.services.rest.impl.ApplicationContextImpl;
import org.exoplatform.services.rest.provider.EntityProvider;

/**
 * Processing multipart data based on apache fileupload.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
@Provider
@Consumes({ "multipart/*" })
public class MultipartFormDataEntityProvider implements EntityProvider<Iterator<FileItem>> {

  /**
   * @see HttpServletRequest
   */
  @Context
  private HttpServletRequest httpRequest;

  /**
   * {@inheritDoc}
   */
  public boolean isReadable(Class<?> type,
                            Type genericType,
                            Annotation[] annotations,
                            MediaType mediaType) {
    if (type == Iterator.class) {
      try {
        ParameterizedType t = (ParameterizedType) genericType;
        Type[] ta = t.getActualTypeArguments();
        if (ta.length == 1 && ta[0] == FileItem.class) {
          return true;
        }
        return false;
      } catch (ClassCastException e) {
        return false;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Iterator<FileItem> readFrom(Class<Iterator<FileItem>> type,
                                     Type genericType,
                                     Annotation[] annotations,
                                     MediaType mediaType,
                                     MultivaluedMap<String, String> httpHeaders,
                                     InputStream entityStream) throws IOException {
    try {
      ApplicationContext context = ApplicationContextImpl.getCurrent();
      int bufferSize = (Integer) context.getAttributes().get(RequestHandler.WS_RS_BUFFER_SIZE);
      File repo = (File) context.getAttributes().get(RequestHandler.WS_RS_TMP_DIR);

      DefaultFileItemFactory factory = new DefaultFileItemFactory(bufferSize, repo);
      FileUpload upload = new FileUpload(factory);
      return upload.parseRequest(httpRequest).iterator();
    } catch (FileUploadException e) {
      throw new IOException("Can't process multipart data item " + e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public long getSize(Iterator<FileItem> t,
                      Class<?> type,
                      Type genericType,
                      Annotation[] annotations,
                      MediaType mediaType) {
    return -1;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isWriteable(Class<?> type,
                             Type genericType,
                             Annotation[] annotations,
                             MediaType mediaType) {
    // output is not supported
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public void writeTo(Iterator<FileItem> t,
                      Class<?> type,
                      Type genericType,
                      Annotation[] annotations,
                      MediaType mediaType,
                      MultivaluedMap<String, Object> httpHeaders,
                      OutputStream entityStream) throws IOException {
    throw new UnsupportedOperationException();
  }
  
}
