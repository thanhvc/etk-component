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
package org.etk.sandbox.ws.model;

import java.util.HashMap;

import org.etk.sandbox.ws.model.FieldMetadata.FieldName;
import static org.etk.sandbox.ws.model.ModelContextProvider.Context;

/**
 * Created by The eXo Platform SAS
 * Author : Thanh_VuCong
 *          thanhvc@exoplatform.com
 * Sep 27, 2011  
 */
@SuppressWarnings("serial")
public class ActivityStreamRest extends HashMap<String, Object> {

  /**
   * The pretty id.
   */
  private String prettyId   = "";

  /**
   * The title.
   */
  private String title      = "";

  /**
   * The permanent link.
   */
  private String permaLink  = "";

  /**
   * The URL of favorite icon.a
   */
  private String faviconUrl = "";

  /**
   * Activities of activityStream;
   */
  @OptionalValue
  ActivityRest[] activities;

  /**
   * Constructor.
   * 
   * @param type The type.
   * @param prettyId The pretty id.
   * @param faviconUrl The favorite icon URL.
   * @param title The title.
   * @param permalink The permanent link.
   */
  public ActivityStreamRest(final String prettyId,
                            final String title,
                            final String permaLink) {

    this.setPrettyId(prettyId);
    this.setTitle(title);
    this.setPermaLink(permaLink);
  }

  public String getPrettyId() {
    return (String) get(FieldName.AS_PRETTY_ID.getFieldName());
  }

  public void setPrettyId(final String prettyId) {
    put(FieldName.AS_PRETTY_ID.getFieldName(), prettyId);
  }

  public String getTitle() {
    return (String) get(FieldName.AS_TITLE.getFieldName());
  }

  public void setTitle(final String title) {
    put(FieldName.AS_TITLE.getFieldName(), title);
  }

  public String getPermaLink() {
    return (String) get(FieldName.AS_PERMA_LINK.getFieldName());
  }

  public void setPermaLink(final String permalink) {
    put(FieldName.AS_PERMA_LINK.getFieldName(), title);
  }

  public String getFaviconUrl() {
    return (String) get(FieldName.AS_FAVICON_URL.getFieldName());
  }

  public void setFaviconUrl(String faviconUrl) {
    put(FieldName.AS_FAVICON_URL.getFieldName(), title);
  }

  public ActivityRest[] getActivities() {
    return (ActivityRest[]) get(FieldName.AS_ACTIVITIES.getFieldName());
  }

  public void setActivities(ActivityRest[] activities) {
    put(FieldName.AS_ACTIVITIES.getFieldName(), activities);
  }

  /**
   * Resolve myself to make sure that null value is return default value.
   * Default which defined from ModelRestBuilder class.
   * @param context
   */
  public void resolve(Context<ActivityStreamRest> context) {
    context.build(this);
  }
}
