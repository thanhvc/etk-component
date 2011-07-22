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
package org.etk.storage.plugins.cache.loader;

/**
 * The loader interface is used by the future cache to retrieves the value from the key when it does not exist.
 *
 * @param <K> the key type parameter
 * @param <V> the value type parameter
 * @param <C> the context type parameter
 */
public interface Loader<K, V, C>
{

   /**
    * Retrieves the value from the key within the specified context. If the resource is not found then the value
    * null must be returned.
    *
    * @param context the context
    * @param key the key
    * @return the value
    * @throws Exception any exception that would prevent the value to be loaded
    */
   V retrieve(C context, K key) throws Exception;

}
