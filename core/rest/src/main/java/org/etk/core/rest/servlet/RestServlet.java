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
package org.etk.core.rest.servlet;
/*
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.container.web.AbstractHttpServlet;
import org.exoplatform.services.rest.Connector;
import org.exoplatform.services.rest.ContainerResponseWriter;
import org.exoplatform.services.rest.GenericContainerResponse;
import org.exoplatform.services.rest.RequestHandler;
import org.exoplatform.services.rest.impl.ContainerResponse;
import org.exoplatform.services.rest.impl.EnvironmentContext;
import org.exoplatform.services.rest.impl.header.HeaderHelper;
*/


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.MessageBodyWriter;

import org.etk.common.logging.Logger;
import org.etk.core.rest.ContainerResponseWriter;
import org.etk.core.rest.GenericContainerResponse;
import org.etk.core.rest.RequestHandler;
import org.etk.core.rest.impl.ContainerResponse;
import org.etk.core.rest.impl.EnvironmentContext;
import org.etk.core.rest.impl.header.HeaderHelper;
import org.etk.kernel.container.KernelContainer;
import org.etk.kernel.container.component.RequestLifeCycle;

/**
 * This servlet is front-end for the REST engine.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class RestServlet extends AbstractHttpServlet implements Connector
{

   private static final Logger LOG = Logger.getLogger(RestServlet.class);

   /**
    * Generated by Eclipse.
    */
   private static final long serialVersionUID = 2152962763071591181L;

   /**
    * {@inheritDoc}
    */
   @Override
   protected void onService(KernelContainer container, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException
   {

      RequestLifeCycle.begin(container);

      RequestHandler requestHandler = (RequestHandler)container.getComponentInstanceOfType(RequestHandler.class);

      EnvironmentContext env = new EnvironmentContext();
      env.put(HttpServletRequest.class, httpRequest);
      env.put(HttpServletResponse.class, httpResponse);
      env.put(ServletConfig.class, config);
      env.put(ServletContext.class, getServletContext());

      try
      {
         EnvironmentContext.setCurrent(env);
         ServletContainerRequest request = new ServletContainerRequest(httpRequest);
         ContainerResponse response = new ContainerResponse(new ServletContainerResponseWriter(httpResponse));
         requestHandler.handleRequest(request, response);
      }
      catch (IOException ioe)
      {
         if (ioe.getClass().getName().equals("org.apache.catalina.connector.ClientAbortException"))
         {
            LOG.debug("Write socket error!", ioe);
         }
         else
         {
            throw ioe;
         }
      }
      catch (Exception e)
      {
         throw new ServletException(e);
      }
      finally
      {
         EnvironmentContext.setCurrent(null);
         RequestLifeCycle.end();
      }
   }

   /**
    * See {@link ContainerResponseWriter}.
    */
   class ServletContainerResponseWriter implements ContainerResponseWriter
   {

      /**
       * See {@link HttpServletResponse}.
       */
      private HttpServletResponse servletResponse;

      /**
       * @param response HttpServletResponse
       */
      ServletContainerResponseWriter(HttpServletResponse response)
      {
         this.servletResponse = response;
      }

      /**
       * {@inheritDoc}
       */
      @SuppressWarnings("unchecked")
      public void writeBody(GenericContainerResponse response, MessageBodyWriter entityWriter) throws IOException
      {
         Object entity = response.getEntity();
         if (entity != null)
         {
            OutputStream out = servletResponse.getOutputStream();
            entityWriter.writeTo(entity, entity.getClass(), response.getEntityType(), null, response.getContentType(),
               response.getHttpHeaders(), out);
            out.flush();
         }
      }

      /**
       * {@inheritDoc}
       */
      public void writeHeaders(GenericContainerResponse response) throws IOException
      {
         if (servletResponse.isCommitted())
         {
            return;
         }

         servletResponse.setStatus(response.getStatus());

         if (response.getHttpHeaders() != null)
         {
            // content-type and content-length should be preset in headers
            for (Map.Entry<String, List<Object>> e : response.getHttpHeaders().entrySet())
            {
               String name = e.getKey();
               for (Object o : e.getValue())
               {
                  String value = null;
                  if (o != null && (value = HeaderHelper.getHeaderAsString(o)) != null)
                  {
                     servletResponse.addHeader(name, value);
                  }
               }
            }
         }
      }
   }
}
