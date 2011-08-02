package org.etk.core.rest.impl.header;

/**
 * Token is any header part which contains only valid characters see
 * {@link HeaderHelper#isToken(String)} . Token is separated by ','
 * 
 */
public class Token {
  
  /**
   * Token.
   */
  private String token;

  /**
   * @param token a token
   */
  public Token(String token) {
    this.token = token.toLowerCase();
  }

  /**
   * @return the token in lower case
   */
  public String getToken() {
    return token;
  }

  /**
   * Check is to token is compatible.
   * 
   * @param other the token must be checked
   * @return true if token is compatible false otherwise
   */
  public boolean isCompatible(Token other) {
    if ("*".equals(token))
      return true;

    return token.equalsIgnoreCase(other.getToken());
  }

}