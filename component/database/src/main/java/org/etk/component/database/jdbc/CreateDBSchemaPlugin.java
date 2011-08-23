/*
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.etk.component.database.jdbc;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.etk.common.logging.Logger;
import org.etk.kernel.container.component.BaseComponentPlugin;
import org.etk.kernel.container.configuration.ConfigurationException;
import org.etk.kernel.container.xml.InitParams;
import org.etk.kernel.container.xml.ValueParam;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady
 *         Azarenkov</a>
 * @version $Id: CreateDBSchemaPlugin.java 8017 2006-08-16 15:12:00Z peterit $
 */
public class CreateDBSchemaPlugin extends BaseComponentPlugin {

  protected static Logger log = Logger.getLogger(CreateDBSchemaPlugin.class);

  private String          dataSource;

  private String          script;

  public CreateDBSchemaPlugin(InitParams params) throws ConfigurationException {
    ValueParam dsParam = params.getValueParam("data-source");
    ValueParam scriptFileParam = params.getValueParam("script-file");
    ValueParam scriptParam = params.getValueParam("script");

    if (dsParam == null)
      return;
    dataSource = dsParam.getValue();
    if (scriptParam != null) {
      script = scriptParam.getValue();
      return;
    }
    // ClassLoader cl = this.getClass().getClassLoader();
    // //Thread.currentThread().getContextClassLoader();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    InputStream is = cl.getResourceAsStream(scriptFileParam.getValue());

    if (is == null)
      is = ClassLoader.getSystemResourceAsStream(scriptFileParam.getValue());

    if (is == null) {
      try {
        log.warn("Db script not found as system resource... Trying to search as file by path: "
            + scriptFileParam.getValue());
        is = new FileInputStream(scriptFileParam.getValue());
        log.info("Db script found as file by path: " + scriptFileParam.getValue());
      } catch (IOException e) {
        log.warn("Db script not found as file by path: " + scriptFileParam.getValue() + ". " + e);
      }
    }

    if (is == null) {
      try {
        log.warn("Db script not found as system resource... Trying to search as file by url: "
            + scriptFileParam.getValue());
        is = new URL(scriptFileParam.getValue()).openStream();
        log.info("Db script found as file by url: " + scriptFileParam.getValue());
      } catch (IOException e) {
        log.warn("Db script not found as file by url: " + scriptFileParam.getValue() + ". " + e);
      }
    }

    if (is == null) {
      throw new ConfigurationException("Could not open input stream for db script "
          + cl.getResource(scriptFileParam.getValue()));
    }

    try {
      byte[] buf = new byte[is.available()];
      is.read(buf);
      script = new String(buf);
    } catch (IOException e) {
      log.error(e.getLocalizedMessage(), e);
    } finally {
      try {
        is.close();
      } catch (IOException e) {
      }
    }

  }

  public String getDataSource() {
    return dataSource;
  }

  public String getScript() {
    return script;
  }

}
