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
package org.etk.sandbox.security.jaas.ch02;

import java.io.File;
import java.io.IOException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 7, 2011  
 */
public class Ch02aMain {

  /**
   * @param args
   */
  public static void main(String[] args) throws IOException {
    File file = new File("conf/cheese.txt");
    System.out.println(file.getPath());
    try {
      file.canWrite();
      System.out.println("We can write to cheese.txt");
    } catch (SecurityException e) {
      System.out.println("We can NOT write to cheese.txt");
    }
  }

}
