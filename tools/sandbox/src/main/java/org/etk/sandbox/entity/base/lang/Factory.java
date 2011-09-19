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
package org.etk.sandbox.entity.base.lang;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 6, 2011  
 */
/** Factory interface. */
public interface Factory<R, A> {

    /** Returns an instance of <code>R</code>. This is a basic factory interface
     * that is meant to be extended. Sub-interfaces declare types for
     * <code>A</code> (the <code>getInstance</code> argument), and
     * <code>R</code> (the type returned by <code>getInstance</code>).
     *
     * @param obj Optional object to be used in <code>R</code>'s construction,
     * or to be used as a selector key
     * @return An instance of <code>R</code>
     */
    public R getInstance(A obj);

}
