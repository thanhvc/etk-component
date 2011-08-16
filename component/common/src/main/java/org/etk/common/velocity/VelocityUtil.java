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
package org.etk.common.velocity;

import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.etk.common.io.unsync.UnsyncStringWriter;
import org.etk.common.utils.Validator;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 4, 2011  
 */
public class VelocityUtil {

  public static String evaluate(String input) throws Exception {
    return evaluate(input, null);
  }

  public static String evaluate(String input, Map<String, Object> variables)
    throws Exception {

    Velocity.init();

    VelocityContext velocityContext = new VelocityContext();

    if (variables != null) {
      Iterator<Map.Entry<String, Object>> itr =
        variables.entrySet().iterator();

      while (itr.hasNext()) {
        Map.Entry<String, Object> entry = itr.next();

        String key = entry.getKey();
        Object value = entry.getValue();

        if (Validator.isNotNull(key)) {
          velocityContext.put(key, value);
        }
      }
    }

    UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

    Velocity.evaluate(velocityContext, unsyncStringWriter, VelocityUtil.class.getName(), input);

    return unsyncStringWriter.toString();
  }

}