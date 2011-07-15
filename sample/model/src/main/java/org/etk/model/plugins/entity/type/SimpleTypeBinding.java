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
package org.etk.model.plugins.entity.type;

import org.etk.orm.plugins.bean.mapping.jcr.PropertyMetaType;
import org.etk.orm.plugins.bean.type.SimpleTypeProvider;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 15, 2011  
 */
/**
 * The mapping between a Table.Field property type and a simple type. A simple type is any class
 * that can be converted back and forth to a particular JCR type.
 *
 */
public interface SimpleTypeBinding {

  /**
   * Returns the property meta type.
   *
   * @return the property meta type
   */
  PropertyMetaType<?> getPropertyMetaType();

  /**
   * Create a simple type provider for this mapping. Note that this operation is only guaranted during the runtime
   * phase as it requires to load the {@code org.chromattic.spi.type.SimpleTypeProvider} class.
   *
   * @return the simple type provider
   */
  SimpleTypeProvider<?, ?> create();

}
