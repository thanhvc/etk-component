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
package org.etk.common.net;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Jun
 * 29, 2011
 */
public class ExoHttpClientTest extends TestCase {
  
  private ETKHttpClient exoHttpClient = null;
  private final String targetURL = "/rest-socialdemo/private/api/social/v1-alpha1/socialdemo/activity/d51715397f0001010077b5d08ddf12fc.json?poster_identity=1&number_of_comments=10&activity_stream=t";
  
  protected void setUp() throws Exception {
    super.setUp();
    exoHttpClient = new ETKHttpClient();
  }

  
  protected void tearDown() throws Exception {
    super.tearDown();
    exoHttpClient = null;
  }
  
  public void testHttpClient() throws Exception {
    
  }
  
}
