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
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Sep 27, 2011  
 */
public class ActivityRest {


  /**
   * The id.
   */
  private String id;

  /**
   * The title.
   */
  private String title;

  /**
   * The priority from 0 to 1. 1 is the higher priority.
   */
  private Float priority;

  /**
   * The application id.
   */
  private String appId;

  /**
   * The activity type.
   */
  private String type;

  /**
   * The posted timestamp.
   */
  private Long postedTime;

  /**
   * The date (human format).
   */
  private String createdAt;

  /**
   * The title id.
   */
  private String titleId;

  /**
   * The template parameters.
   */
  private Map<String, String> templateParams;

  /**
   * Got at least one like.
   */
  private Boolean liked;

  /**
   * The identities who like.
   */
  private IdentityRest[] likedByIdentities;

  /**
   * The comments wrapper.
   */
  private CommentRest[] comments;

  /**
   * The number of comment.
   */
  private Integer totalNumberOfComments;

  /**
   * The poster identity id.
   */
  @OptionalValue
  private IdentityRest posterIdentity;

  /**
   * The owner identity id.
   */
  private String identityId;

  /**
   * The Activity stream details.
   */
  @OptionalValue
  private ActivityStreamRest activityStream;
  
  @OptionalValue
  private String permaLink;
  /**
   * Default constructor, used by JAX-RS.
   */
  public ActivityRest() {
  }


  /**
   * Initialize constructor.
   *
   * @param id The id.
   * @param title The title.
   * @param priority The priority.
   * @param appId The application id.
   * @param type The activity type.
   * @param postedTime The timestamp.
   * @param createdAt The human date.
   * @param titleId The title id.
   * @param templateParams The template parameters.
   * @param liked Is liked
   * @param likedByIdentities The identity ids who like.
   * @param identityId The owner identity id.
   */
  public ActivityRest(
      final String id,
      final String title,
      final Float priority,
      final String appId,
      final String type,
      final Long postedTime,
      final String createdAt,
      final String titleId,
      final HashMap<String, String> templateParams,
      final Boolean liked,
      final String identityId) {

    this.setId(id);
    this.setTitle(title);
    this.setPriority(priority);
    this.setAppId(appId);
    this.setType(type);
    this.setPostedTime(postedTime);
    this.setCreatedAt(createdAt);
    this.setTitleId(titleId);
    this.setTemplateParams(templateParams);
    this.setLiked(liked);
    
    
    
    this.setIdentityId(identityId);
  }

  
  
  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public Float getPriority() {
    return this.priority;
  }

  public void setPriority(final Float priority) {
    this.priority = priority;
  }

  public String getAppId() {
    return this.appId;
  }

  public void setAppId(final String appId) {
    this.appId = appId;
  }

  public String getType() {
    return this.type;
  }

  public void setType(final String type) {
    this.type = type;
    
  }

  public Long getPostedTime() {
    return this.postedTime;
  }

  public void setPostedTime(final Long postedTime) {
    this.postedTime = postedTime;
  }

  public String getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getTitleId() {
    return this.titleId;
  }

  public void setTitleId(String titleId) {
    this.titleId = titleId;
  }

  public Map<String, String> getTemplateParams() {
    return this.templateParams;
  }

  public void setTemplateParams(Map<String, String> map) {
    this.templateParams = map;
  }

  public Boolean getLiked() {
    return liked;
  }

  public void setLiked(Boolean liked) {
    this.liked = liked;
  }

  public IdentityRest[] getLikedByIdentities() {
    return this.likedByIdentities;
  }

  public void setLikedByIdentities(IdentityRest[] likedByIdentities) {
    this.likedByIdentities = likedByIdentities;
  }

  public CommentRest[] getComments() {
    return this.comments;
  }

  public void setComments(CommentRest[] comments) {
    this.comments = comments;
  }

  public Integer getTotalNumberOfComments() {
    return this.totalNumberOfComments;
  }

  public void setTotalNumberOfComments(Integer numberOfComments) {
    this.totalNumberOfComments = numberOfComments;
  }

  public IdentityRest getPosterIdentity() {
    return this.posterIdentity;
  }

  public void setPosterIdentity(IdentityRest posterIdentity) {
    this.posterIdentity = posterIdentity;
  }

  public String getIdentityId() {
    return identityId;
  }

  public void setIdentityId(String identityId) {
    this.identityId = identityId;
  }

  public ActivityStreamRest getActivityStream() {
    return this.activityStream;
  }

  public void setActivityStream(final ActivityStreamRest activityStream) {
    this.activityStream = activityStream;
  }
  
  
}
