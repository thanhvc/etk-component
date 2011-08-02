package org.etk.sandbox.rest.api.model;

/**
 * Version class to expose as json response object.
 *
 */
public class Version {

  /**
   * Gets the latest social rest api version.
   *
   * @return the string the latest social rest api version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the latest social rest api version.
   *
   * @param latestVersion the string the latest social rest api version
   */
  public void setVersion(String latestVersion) {
    this.version = latestVersion;
  }

  /**
   * The latest social rest api version.
   */
  private String version;


}

