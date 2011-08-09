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
package org.exoplatform.services.database;

import org.exoplatform.services.database.annotation.TableField;

/**
 * Created by The eXo Platform SAS Author : Le Bien Thuy
 * lebienthuy@exoplatform.com Apr 4, 2006
 */
public class MSSQLServerTableManager extends StandardSQLTableManager {

  public MSSQLServerTableManager(ExoDatasource datasource) {
    super(datasource);
  }

  protected void appendDateField(TableField field, StringBuilder builder) {
    builder.append(field.name()).append(" DATETIME");
  }
}
