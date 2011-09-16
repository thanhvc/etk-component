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
package org.etk.sandbox.entity.plugins.model.xml;

import java.io.Serializable;

import org.etk.common.logging.Logger;
import org.etk.sandbox.entity.plugins.config.JdbcValueHandler;
/**
 * Generic Entity - FieldType model class
 */

@SuppressWarnings("serial")
public class FieldType implements Serializable {

  private static final Logger logger          = Logger.getLogger(FieldType.class);

  /** The type of the Field */
  protected String              type             = null;

  /** The java-type of the Field */
  protected String              javaType         = null;
  /** The JDBC value handler for this Field */
  protected JdbcValueHandler<?> jdbcValueHandler = null;

  /** The sql-type of the Field */
  protected String              sqlType          = null;

  /** The sql-type-alias of the Field, this is optional */
  protected String              sqlTypeAlias     = null;

  /** Default Constructor */
  public FieldType() {
  }

   /** The type of the Field */
  public String getType() {
    return this.type;
  }

  /** The java-type of the Field */
  public String getJavaType() {
    return this.javaType;
  }

  /** Returns the JDBC value handler for this field type */
  public JdbcValueHandler<?> getJdbcValueHandler() {
    return this.jdbcValueHandler;
  }

  /** The sql-type of the Field */
  public String getSqlType() {
    return this.sqlType;
  }

  /** The sql-type-alias of the Field */
  public String getSqlTypeAlias() {
    return this.sqlTypeAlias;
  }

  /**
   * A simple function to derive the max length of a String created from the
   * field value, based on the sql-type
   * 
   * @return max length of a String representing the Field value
   */
  public int stringLength() {
    String sqlTypeUpperCase = sqlType.toUpperCase();
    if (sqlTypeUpperCase.indexOf("VARCHAR") >= 0) {
      if (sqlTypeUpperCase.indexOf("(") > 0 && sqlTypeUpperCase.indexOf(")") > 0) {
        String length = sqlTypeUpperCase.substring(sqlTypeUpperCase.indexOf("(") + 1,
                                                   sqlTypeUpperCase.indexOf(")"));

        return Integer.parseInt(length);
      } else {
        return 255;
      }
    } else if (sqlTypeUpperCase.indexOf("CHAR") >= 0) {
      if (sqlTypeUpperCase.indexOf("(") > 0 && sqlTypeUpperCase.indexOf(")") > 0) {
        String length = sqlTypeUpperCase.substring(sqlTypeUpperCase.indexOf("(") + 1,
                                                   sqlTypeUpperCase.indexOf(")"));

        return Integer.parseInt(length);
      } else {
        return 255;
      }
    } else if (sqlTypeUpperCase.indexOf("TEXT") >= 0 || sqlTypeUpperCase.indexOf("LONG") >= 0
        || sqlTypeUpperCase.indexOf("CLOB") >= 0) {
      return 5000;
    }
    return 20;
  }
}
