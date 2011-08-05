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
package org.etk.storage.plugins.common;

import org.etk.common.exception.EtkException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 26, 2011  
 */
public class BarStorageException extends EtkException {

  
  private static final String MESSAGE_BUNDLE_DELIMITER = ".";

  public static enum Type {
    FAIL_TO_SAVE_BAR("Failed_To_Save_Foo"),
    FAIL_TO_UPDATE_BAR("Failed_To_Update_Foo"),
    FAIL_TO_DELETE_BAR("Failed_To_Update_Foo"),
    FAIL_TO_FIND_BAR("Failed_To_Find_Foo"),
    
    FAIL_TO_GET_BAR_BY_BAR_FILTER_COUNT("Failed_To_Get_Bar_By_Bar_Filter_Count"),
    FAIL_TO_GET_BAR_BY_BAR_FILTER("Failed_To_Get_Bar_By_Bar_Filter");
    
    
    private final String msgKey;

    private Type(String msgKey) {
      this.msgKey = msgKey;
    }

    @Override
    public String toString() {
      return this.getClass() + MESSAGE_BUNDLE_DELIMITER + msgKey;
    }
  }

  /**
   * Initializes the IdentityStorageException.
   * 
   * @param type
   */
  public BarStorageException(Type type) {
    super(type.toString());
  }
  
  /**
   * Initializes the IdentityStorageException.
   * 
   * @param type
   * @param msg
   */
  public BarStorageException(Type type, String msg) {
    super(type.toString(), msg);
  }
  
  /**
   * Initializes the IdentityStorageException.
   * 
   * @param type
   * @param msgArgs
   */
  public BarStorageException(Type type, String[] msgArgs) {
    super(type.toString(), msgArgs);
  }
  
  /**
   * Initializes the IdentityStorageException.
   * 
   * @param type
   * @param msg
   * @param cause
   */
  public BarStorageException(Type type, String msg, Throwable cause) {
    super(type.toString(), msg, cause);
  }
  
  /**
   * Initializes the IdentityStorageException.
   * 
   * @param type
   * @param msgArgs
   * @param msg
   * @param cause
   */
  public BarStorageException(Type type, String[] msgArgs, String msg, Throwable cause) {
    super(type.toString(), msgArgs, msg, cause);
  }
}
