package org.etk.orm.plugins.mapper.onetomany.hierarchical;

import org.etk.orm.core.EntityContext;
import org.etk.orm.plugins.bean.ValueKind;

public abstract class AnyChildMultiValueMapper<K extends ValueKind.Multi> {

  public abstract <E> Object createValue(EntityContext parentCtx, String prefix, Class<E> relatedClass);

  public static class Map extends AnyChildMultiValueMapper<ValueKind.Map> {
    public <E> Object createValue(EntityContext parentCtx, String prefix, Class<E> relatedClass) {
      return new AnyChildMap<E>(parentCtx, prefix, relatedClass);
    }
  }

  public static class Collection extends AnyChildMultiValueMapper<ValueKind.Collection> {
    public <E> Object createValue(EntityContext parentCtx, String prefix, Class<E> relatedClass) {
      return new AnyChildCollection<E>(parentCtx, prefix, relatedClass);
    }
  }

  public static class List extends AnyChildMultiValueMapper<ValueKind.Array> {
    public <E> Object createValue(EntityContext parentCtx, String prefix, Class<E> relatedClass) {
      return new AnyChildList<E>(parentCtx, prefix, relatedClass);
    }
  }
}
