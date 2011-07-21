package org.etk.orm.plugins.mapper.property;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import org.etk.orm.api.UndeclaredRepositoryException;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ListType;
import org.etk.orm.plugins.bean.ValueKind;
import org.etk.orm.plugins.common.JCR;
import org.etk.orm.plugins.common.collection.AbstractFilterIterator;

class PropertyMap extends AbstractMap<String, Object> {

  /** . */
  private final JCRPropertyDetypedPropertyMapper mapper;

  /** . */
  private final EntityContext ctx;

  /** . */
  private SetImpl set;

  PropertyMap(JCRPropertyDetypedPropertyMapper mapper, EntityContext ctx) {
    this.ctx = ctx;
    this.mapper = mapper;
    this.set = null;
  }

  public Set<Entry<String, Object>> entrySet() {
    if (set == null) {
      set = new SetImpl();
    }
    return set;
  }

  @Override
  public Object get(Object key) {
    String s = validateKey(key);
    if (s != null) {
      try {
        if (mapper.valueKind == ValueKind.SINGLE) {
          return ctx.getPropertyValue(s, null);
        } else {
          return ctx.getPropertyValues(s, null, ListType.LIST);
        }
      }
      catch (RepositoryException e) {
        throw new UndeclaredRepositoryException(e);
      }
    } else {
      return null;
    }
  }

  @Override
  public Object remove(Object key) {
    String s = validateKey(key);
    if (s != null) {
      return put(s, null);
    } else {
      return null;
    }
  }

  @Override
  public Object put(String key, Object value) {
    String s = validateKey(key);
    if (s != null) {
      return update(key, value);
    } else {
      throw new IllegalArgumentException("Invalid key " + key + " should being with the prefix " + mapper.namePrefix);
    }
  }

  private String validateKey(Object key) {
    if (key == null) {
      throw new NullPointerException("Key cannot be null");
    }
    if (key instanceof String) {
      String s = (String)key;
      if (mapper.namePrefix != null) {
        return mapper.namePrefix + s;
      } else {
        return s;
      }
    } else {
      throw new ClassCastException("Key must be instance of String instead of " + key.getClass().getName());
    }
  }

  private Object update(String key, Object value) {
    try {
      Object previous;
      if (mapper.valueKind == ValueKind.SINGLE) {
        previous = ctx.getPropertyValue(key, null);
        ctx.setPropertyValue(key, null, value);
      } else {
        List<?> list = (List<?>)value;
        previous = ctx.getPropertyValues(key, null, ListType.LIST);
        ctx.setPropertyValues(key, null, ListType.LIST, list);
      }
      return previous;
    }
    catch (RepositoryException e) {
      throw new UndeclaredRepositoryException(e);
    }
  }

  private class SetImpl extends AbstractSet<Map.Entry<String, Object>> {

    public Iterator<Map.Entry<String, Object>> iterator() {

      try {
        Iterator<Property> i;
        if (mapper.namePattern == null) {
          i = JCR.adapt(ctx.getNode().getProperties());
        } else {
          i = JCR.adapt(ctx.getNode().getProperties(mapper.namePattern));
        }

        //
        return new AbstractFilterIterator<Entry<String, Object>, Property>(i) {

          @Override
          protected Entry<String, Object> adapt(Property internal) {

            try {

              // todo : that does not respect the encoding of property names but
              // that is not much important for now as it is an unsupported feature

              // todo : support for default values is not done because
              // we pass null as the SimpleValueInfo type parameter

              //
              final String key = internal.getName();

              //
              final String name = mapper.namePrefix != null ? key.substring(mapper.namePrefix.length()) : key;

              //
              if ("*".equals(internal.getDefinition().getName())) {
                switch (internal.getType()) {
                  case PropertyType.STRING:
                  case PropertyType.NAME:
                  case PropertyType.LONG:
                  case PropertyType.BOOLEAN:
                  {
                    return new Entry<String, Object>() {
                      public String getKey() {
                        return name;
                      }
                      public Object getValue() {
                        return get(key);
                      }
                      public Object setValue(Object value) {
                        throw new UnsupportedOperationException();
                      }
                    };
                  }
                }
              }

              //
              return null;
            }
            catch (RepositoryException e) {
              throw new UndeclaredRepositoryException(e);
            }
          }
        };
      }
      catch (RepositoryException e) {
        throw new UndeclaredRepositoryException(e);
      }
    }

    public int size() {
      int count = 0;
      Iterator<Map.Entry<String, Object>> iterator = iterator();
      while (iterator.hasNext()) {
        iterator.next();
        count++;
      }
      return count;
    }
  }
}