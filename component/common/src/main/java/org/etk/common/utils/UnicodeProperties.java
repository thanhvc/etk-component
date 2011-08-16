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
package org.etk.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.etk.common.io.unsync.UnsyncBufferedReader;
import org.etk.common.io.unsync.UnsyncStringReader;
import org.etk.common.logging.Logger;



/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          thanhvucong.78@google.com
 * Aug 5, 2011  
 */
public class UnicodeProperties extends HashMap<String, String> {

  public UnicodeProperties() {
    super();
  }

  public UnicodeProperties(boolean safe) {
    super();

    _safe = safe;
  }

  public void fastLoad(String props) {
    if (Validator.isNull(props)) {
      return;
    }

    int x = props.indexOf(CharPool.NEW_LINE);
    int y = 0;

    while (x != -1) {
      put(props.substring(y, x));

      y = x;

      x = props.indexOf(CharPool.NEW_LINE, y + 1);
    }

    put(props.substring(y));
  }

  public String getProperty(String key) {
    return get(key);
  }

  public String getProperty(String key, String defaultValue) {
    if (containsKey(key)) {
      return getProperty(key);
    }
    else {
      return defaultValue;
    }
  }

  public boolean isSafe() {
    return _safe;
  }

  public void load(String props) throws IOException {
    if (Validator.isNull(props)) {
      return;
    }

    UnsyncBufferedReader unsyncBufferedReader = null;

    try {
      unsyncBufferedReader = new UnsyncBufferedReader(new UnsyncStringReader(props));

      String line = unsyncBufferedReader.readLine();

      while (line != null) {
        put(line);
        line = unsyncBufferedReader.readLine();
      }
    }
    finally {
      if (unsyncBufferedReader != null) {
        try {
          unsyncBufferedReader.close();
        }
        catch (Exception e) {
        }
      }
    }
  }

  private void put(String line) {
    line = line.trim();

    if (!_isComment(line)) {
      int pos = line.indexOf(CharPool.EQUAL);

      if (pos != -1) {
        String key = line.substring(0, pos).trim();
        String value = line.substring(pos + 1).trim();

        if (_safe) {
          value = _decode(value);
        }

        setProperty(key, value);
      }
      else {
        _log.error("Invalid property on line " + line);
      }
    }
  }

  public String put(String key, String value) {
    if (key == null) {
      return null;
    }
    else {
      if (value == null) {
        return remove(key);
      }
      else {
        _length += key.length() + value.length() + 2;

        return super.put(key, value);
      }
    }
  }

  public String remove(Object key) {
    if ((key == null) || !containsKey(key)) {
      return null;
    }
    else {
      String keyString = (String)key;

      String value = super.remove(key);

      _length -= keyString.length() + value.length() + 2;

      return value;
    }
  }

  public String setProperty(String key, String value) {
    return put(key, value);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(_length);

    for (Map.Entry<String, String> entry : entrySet()) {
      String value = entry.getValue();

      if (Validator.isNotNull(value)) {
        if (_safe) {
          value = _encode(value);
        }

        sb.append(entry.getKey());
        sb.append(StringPool.EQUAL);
        sb.append(value);
        sb.append(StringPool.NEW_LINE);
      }
    }

    return sb.toString();
  }

  protected int getToStringLength() {
    return _length;
  }

  private static String _decode(String value) {
    return StringUtil.replace(
      value, _SAFE_NEWLINE_CHARACTER, StringPool.NEW_LINE);
  }

  private static String _encode(String value) {
    return StringUtil.replace(
      value,
      new String[] {
        StringPool.RETURN_NEW_LINE, StringPool.NEW_LINE,
        StringPool.RETURN
      },
      new String[] {
        _SAFE_NEWLINE_CHARACTER, _SAFE_NEWLINE_CHARACTER,
        _SAFE_NEWLINE_CHARACTER
      });
  }

  private boolean _isComment(String line) {
    return line.length() == 0 || line.startsWith(StringPool.POUND);
  }

  private static final String _SAFE_NEWLINE_CHARACTER =
    "_SAFE_NEWLINE_CHARACTER_";

  private static Logger _log = Logger.getLogger(UnicodeProperties.class);

  private boolean _safe = false;
  private int _length;

}
