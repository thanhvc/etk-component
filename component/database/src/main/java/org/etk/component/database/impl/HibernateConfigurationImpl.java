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
package org.etk.component.database.impl;

import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gennady.azarenkov@exoplatform.com">Gennady
 *         Azarenkov</a>
 * @version $Id: HibernateConfigurationImpl.java 5332 2006-04-29 18:32:44Z geaz
 *          $ Hibernate's Configuration. One per 'properties-param' entry in
 *          container configuration
 */
public class HibernateConfigurationImpl extends AnnotationConfiguration
{

   private static final long serialVersionUID = -6929418313712034365L;

   public HibernateConfigurationImpl(HibernateSettingsFactory settingsFactory) throws HibernateException
   {
      super(settingsFactory);
   }
}
