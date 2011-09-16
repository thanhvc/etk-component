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
package org.etk.sandbox.entity.plugins.model.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.etk.common.logging.Logger;
import org.etk.sandbox.entity.plugins.model.xml.Configuration;
import org.etk.sandbox.entity.plugins.model.xml.Deserializer;
import org.etk.sandbox.entity.plugins.model.xml.Entity;
import org.etk.sandbox.entity.plugins.model.xml.FieldTypeModel;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Aug
 * 29, 2011
 */
public class ConfigurationManagerImpl implements ConfigurationManager {
  protected Configuration               configurations;

  private ClassLoader                   scontextClassLoader;

  private String                        contextPath       = null;

  private boolean                       validateSchema    = true;

  private final Set<String>             profiles;

  private final static Logger           log               = Logger.getLogger(ConfigurationManagerImpl.class);

  /**
   * The URL the current document being unmarshaller.
   */
  private static final ThreadLocal<URL> currentURL        = new ThreadLocal<URL>();

  public static URL getCurrentURL() {
    return currentURL.get();
  }

  public ConfigurationManagerImpl() {
    this.profiles = Collections.emptySet();
  }

  public ConfigurationManagerImpl(Set<String> profiles) {
    this.profiles = profiles;
  }

  public ConfigurationManagerImpl(ClassLoader loader, Set<String> profiles) {
    this.scontextClassLoader = loader;
    this.profiles = profiles;
  }

  public Configuration getConfiuration() {
    return this.configurations;
  }

  public void addConfiguration(String url) throws Exception {
    if (url == null) return;
    addConfiguration(getURL(url));
  }

  public void addConfiguration(Collection urls) throws Exception {
    Iterator i = urls.iterator();
    while (i.hasNext()) {
      URL url = (URL) i.next();
      addConfiguration(url);

    }

  }

  /**
   * @param context
   * @param url
   * @throws Exception
   */
  public void addConfiguration(URL url) throws Exception {
    if (url == null)
      return;

    try {
      contextPath = (new File(url.toString())).getParent() + "/";
      contextPath = contextPath.replace("\\\\", "/");

    } catch (Exception e) {
      contextPath = null;
    }

    if (currentURL.get() != null) {
      throw new IllegalStateException("Would not expect that.");
    } else {
      currentURL.set(url);
    }

    //
    try {
      ConfigurationUnmarshaller unmarshaller = new ConfigurationUnmarshaller(profiles);
      Configuration conf = unmarshaller.unmarshall(url);

      if (configurations == null) {
        configurations = conf;
      } else {
        configurations.mergeConfiguration(conf);
      }

      List urls = conf.getImports();
      if (urls != null) {
        for (int i = 0; i < urls.size(); i++) {
          String uri = (String) urls.get(i);
          URL urlObject = getURL(uri);
          if (urlObject != null) {
            if (LOG_DEBUG)
              log.info("\timport " + urlObject);
            // Set the URL of imported file
            currentURL.set(urlObject);
            conf = unmarshaller.unmarshall(urlObject);
            configurations.mergeConfiguration(conf);
          } else {
            log.warn("Couldn't process the URL for " + uri + " configuration file ignored ");
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      log.error("Cannot process the configuration " + url, ex);
    } finally {
      currentURL.set(null);
    }

  }

  /**
   * Recursively import the configuration files
   * 
   * @param unmarshaller the unmarshaller used to unmarshall the configuration
   *          file to import
   * @param conf the configuration in which we get the list of files to import
   * @throws Exception if an exception occurs while loading the files to import
   */
  private void importConf(ConfigurationUnmarshaller unmarshaller, Configuration conf) throws Exception {
    importConf(unmarshaller, conf, 1);
  }

  /**
   * Recursively import the configuration files
   * 
   * @param unmarshaller the unmarshaller used to unmarshall the configuration
   *          file to import
   * @param conf the configuration in which we get the list of files to import
   * @param depth used to log properly the URL of the file to import
   * @throws Exception if an exception occurs while loading the files to import
   */
  private void importConf(ConfigurationUnmarshaller unmarshaller, Configuration conf, int depth) throws Exception {
    List urls = conf.getImports();
    if (urls != null) {
      StringBuilder prefix = new StringBuilder(depth);
      for (int i = 0; i < depth; i++) {
        prefix.append('\t');
      }
      for (int i = 0; i < urls.size(); i++) {
        String uri = (String) urls.get(i);
        URL urlObject = getURL(uri);
        if (urlObject != null) {
          // Set the URL of imported file
          currentURL.set(urlObject);
          conf = unmarshaller.unmarshall(urlObject);
          configurations.mergeConfiguration(conf);
          importConf(unmarshaller, conf, depth + 1);
        } else {
          log.warn("Couldn't process the URL for " + uri + " configuration file ignored ");
        }
      }
    }
  }

 
  

  public Collection<FieldTypeModel> getFieldTypeModels() {
    if (configurations == null)
      return null;
    return configurations.getFieldTypeList();
  }
  
  public boolean isValidateSchema() {
    return validateSchema;
  }

  public void setValidateSchema(boolean validateSchema) {
    this.validateSchema = validateSchema;
  }

  public URL getResource(String url, String defaultURL) throws Exception {
    if (url == null)
      url = defaultURL;
    return getResource(url);
  }

  public URL getResource(String uri) throws Exception {
    return getURL(uri);
  }

  public InputStream getInputStream(String url, String defaultURL) throws Exception {
    if (url == null)
      url = defaultURL;
    return getInputStream(url);
  }

  public InputStream getInputStream(String uri) throws Exception {
    final URL url = getURL(uri);
    if (url == null) {
      throw new IOException("Resource (" + uri + ") could not be found or the invoker doesn't have adequate privileges to get the resource");
    }
    return url.openStream();

  }

  public URL getURL(String url) throws Exception {
    if (url == null) {
      return null;
    } else if (url.startsWith("jar:")) {
      final String path = removePrefix("jar:/", url);
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      return cl.getResource(path);
    } else if (url.startsWith("classpath:")) {
      final String path = removePrefix("classpath:/", url);
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      return cl.getResource(path);
    } else if (url.startsWith("file:")) {
      url = resolveFileURL(url);
      return new URL(url);
    } else if (url.indexOf(":") < 0 && contextPath != null) {
      return new URL(contextPath + url.replace('\\', '/'));
    }
    return null;
  }

  /**
   * This methods is used to convert the given into a valid url, it will:
   * <ol>
   * <li>Resolve variables in the path if they exist</li>
   * <li>Replace windows path separators with proper separators</li>
   * <li>Ensure that the path start with file:///</li>
   * </ol>
   * , then it will
   * 
   * @param url the url to resolve
   * @return the resolved url
   */
  private String resolveFileURL(String url) {
    url = Deserializer.resolveVariables(url);
    // we ensure that we don't have windows path separator in the url
    url = url.replace('\\', '/');
    if (!url.startsWith("file:///")) {
      // The url is invalid, so we will fix it
      // it happens when we use a path of type file://${path}, under
      // linux or mac os the path will start with a '/' so the url
      // will be correct but under windows we will have something
      // like C:\ so the first '/' is missing
      if (url.startsWith("file://")) {
        // The url is of type file://, so one '/' is missing
        url = "file:///" + url.substring(7);
      } else if (url.startsWith("file:/")) {
        // The url is of type file:/, so two '/' are missing
        url = "file:///" + url.substring(6);
      } else {
        // The url is of type file:, so three '/' are missing
        url = "file:///" + url.substring(5);
      }
    }
    return url;
  }

  public boolean isDefault(String value) {
    return value == null || value.length() == 0 || "default".equals(value);
  }

  protected String removePrefix(String prefix, String url) {
    return url.substring(prefix.length(), url.length());
  }

  @Override
  public Configuration getConfiguration() {
    return configurations;
  }
}
