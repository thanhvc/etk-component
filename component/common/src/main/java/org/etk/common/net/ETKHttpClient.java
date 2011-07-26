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
package org.etk.common.net;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 29, 2011  
 */
public class ETKHttpClient {

  private DefaultHttpClient httpClient = null;
  public ETKHttpClient() {
    
    httpClient = new DefaultHttpClient();
  }
  
  public HttpEntity execute(String targetURL) throws ClientProtocolException, IOException {
    HttpHost targetHost = new HttpHost("127.0.0.1", 8080, "http"); 
    
    
    httpClient.getCredentialsProvider().setCredentials(
            new AuthScope(targetHost.getHostName(), targetHost.getPort()), 
            new UsernamePasswordCredentials("demo", "gtn"));

    // Create AuthCache instance
    AuthCache authCache = new BasicAuthCache();
    // Generate BASIC scheme object and add it to the local auth cache
    BasicScheme basicAuth = new BasicScheme();
    authCache.put(targetHost, basicAuth);

    // Add AuthCache to the execution context
    BasicHttpContext localcontext = new BasicHttpContext();
    localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);        

    HttpGet httpget = new HttpGet(targetURL);
    Header header = new BasicHeader("Content-Type", "application/json");
    httpget.setHeader(header);
    HttpResponse response = httpClient.execute(targetHost, httpget, localcontext);
    DumpHttpResponse.dumpHeader(response);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      entity = new BufferedHttpEntity(entity);
    }
    EntityUtils.consume(entity);
    
    return entity;
  }
  
}
