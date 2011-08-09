/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.services.organization;

/**
 * Created by The eXo Platform SAS Author : Tuan Nguyen
 * tuan08@users.sourceforge.net Nov 15, 2005 If the other service or a third
 * party want to customize the initialization phase of the organization service.
 * they should make a customize class that implements this interface and
 * register their plugin code via xml configuration. An example of the
 * customization code is we have an organization service database intializer
 * that create the predifined the user , group and membership if the database is
 * empty. To Register the plugin code by the xml configuration: You need to
 * create a my.package.MyMembershipEventListener that implements this interface
 * and add a conf/portal/configuration.xml to the classpath. The
 * configuration.xml can be in a jar file. The file should contain the following
 * configuraiton:
 * 
 * <pre>
 * &lt;configuration&gt;
 *   [..]
 *   &lt;external-component-plugins&gt;
 *     &lt;target-component&gt;org.exoplatform.services.organization.OrganizationService&lt;/target-component&gt;
 *     &lt;component-plugin&gt;
 *        &lt;name&gt;my.customize.initializer.plugin&lt;/name&gt;
 *        &lt;set-method&gt;addListenerPlugin&lt;/set-method&gt;
 *        &lt;type&gt;my.package.MyInitializerPlugin&lt;/type&gt;
 *        &lt;description&gt;your listener description&lt;/description&gt;
 *      &lt;/component-plugin&gt;
 *  &lt;/external-component-plugins&gt;
 *  [...]
 * /configuration&gt;
 * </pre>
 */
public interface OrganizationServiceInitializer {
  public void init(OrganizationService service) throws Exception;
}
