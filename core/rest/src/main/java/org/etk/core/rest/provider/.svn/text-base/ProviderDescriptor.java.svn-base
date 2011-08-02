/**
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.rest.provider;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.exoplatform.services.rest.ObjectModel;
import org.exoplatform.services.rest.resource.ResourceDescriptor;

/**
 * Descriptor of Provider. Provider is annotated with &#64;Provider and
 * implement interface defined by JAX-RS.
 * 
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface ProviderDescriptor extends ResourceDescriptor, ObjectModel {

  /**
   * Get list of {@link MediaType} which current provider consumes.
   * 
   * @return list of media types
   */
  List<MediaType> consumes();

  /**
   * Get list of {@link MediaType} which current provider produces.
   * 
   * @return list of media types
   */
  List<MediaType> produces();

}
