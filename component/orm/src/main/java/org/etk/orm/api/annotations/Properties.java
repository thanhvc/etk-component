package org.etk.orm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotates a Chromattic property having a return type equals to {@code java.util.Map<String,?>} or
 * {@code java.util.Map<String, List<?>>}. The generic value type of the map can be any type, it is used at runtime
 * to filter the returned properties according to their value type. It provides access to residual properties of
 * the corresponding jcr node when that type is compatible with the generic value type of the returned Map.</p>
 *
 * <p>This annotation can be combined with the {@link NamingPrefix} annotation to filter the property map with
 * a name prefix.</p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Properties {
}
