package org.etk.orm.plugins.bean.mapping;

import org.etk.orm.api.annotations.Create;
import org.etk.orm.api.annotations.Destroy;
import org.etk.orm.api.annotations.FindById;
import org.etk.orm.api.annotations.MixinType;
import org.etk.orm.api.annotations.NamingPolicy;
import org.etk.orm.api.annotations.PrimaryType;
import org.etk.reflect.core.AnnotationType;

class Constants {

  /** . */
  static final AnnotationType<NamingPolicy, ?> NAMING_POLICY = AnnotationType.get(NamingPolicy.class);

  /** . */
  static final AnnotationType<PrimaryType, ?> PRIMARY_TYPE = AnnotationType.get(PrimaryType.class);

  /** . */
  static final AnnotationType<MixinType, ?> MIXIN_TYPE = AnnotationType.get(MixinType.class);

  /** . */
  static final AnnotationType<Create, ?> CREATE = AnnotationType.get(Create.class);

  /** . */
  static final AnnotationType<Destroy, ?> DESTROY = AnnotationType.get(Destroy.class);

  /** . */
  static final AnnotationType<FindById, ?> FIND_BY_ID = AnnotationType.get(FindById.class);

}