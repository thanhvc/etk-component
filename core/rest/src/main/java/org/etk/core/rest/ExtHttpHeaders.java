package org.etk.core.rest;

import javax.ws.rs.core.HttpHeaders;

public interface ExtHttpHeaders extends HttpHeaders {

  /**
   * WebDav "Depth" header. See <a href='http://www.ietf.org/rfc/rfc2518.txt'>
   * HTTP Headers for Distributed Authoring</a> section 9 for more information.
   */
  public static final String DEPTH                  = "depth";

  /**
   * HTTP 1.1 "Accept-Ranges" header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP/1.1
   * section 14 "Header Field Definitions"</a> for more information.
   */
  public static final String ACCEPT_RANGES          = "Accept-Ranges";

  /**
   * HTTP 1.1 "Allow" header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP/1.1
   * section 14 "Header Field Definitions"</a> for more information.
   */
  public static final String ALLOW                  = "Allow";

  /**
   * HTTP 1.1 "Authorization" header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP/1.1
   * section 14 "Header Field Definitions"</a> for more information.
   */
  public static final String AUTHORIZATION          = "Authorization";

  /**
   * HTTP 1.1 "Content-Length" header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP/1.1
   * section 14 "Header Field Definitions"</a> for more information.
   */
  public static final String CONTENTLENGTH          = "Content-Length";

  /**
   * HTTP 1.1 "Content-Range" header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP/1.1
   * section 14 "Header Field Definitions"</a> for more information.
   */
  public static final String CONTENTRANGE           = "Content-Range";

  /**
   * HTTP 1.1 "Content-type" header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP/1.1
   * section 14 "Header Field Definitions"</a> for more information.
   */
  public static final String CONTENTTYPE            = "Content-type";

  /**
   * WebDav "DAV" header. See <a href='http://www.ietf.org/rfc/rfc2518.txt'>
   * HTTP Headers for Distributed Authoring</a> section 9 for more information.
   */
  public static final String DAV                    = "DAV";

  /**
   * HTTP 1.1 "Allow" header. See <a
   * href='http://msdn.microsoft.com/en-us/library/ms965954.aspx'> WebDAV/DASL
   * Request and Response Syntax</a> for more information.
   */
  public static final String DASL                   = "DASL";

  /**
   * MS-Author-Via Response Header. See <a
   * href='http://msdn.microsoft.com/en-us/library/cc250217.aspx'> MS-Author-Via
   * Response Header</a> for more information.
   */
  public static final String MSAUTHORVIA            = "MS-Author-Via";

  /**
   * HTTP 1.1 "Range" header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP/1.1
   * section 14 "Header Field Definitions"</a> for more information.
   */
  public static final String RANGE                  = "Range";

  /**
   * WebDav "Destination" header. See <a
   * href='http://www.ietf.org/rfc/rfc2518.txt'> HTTP Headers for Distributed
   * Authoring</a> section 9 for more information.
   */
  public static final String DESTINATION            = "Destination";

  /**
   * WebDav "DAV" header. See <a href='http://www.ietf.org/rfc/rfc2518.txt'>
   * HTTP Headers for Distributed Authoring</a> section 9 for more information.
   */
  public static final String LOCKTOKEN              = "lock-token";

  /**
   * WebDav "If" header. See <a href='http://www.ietf.org/rfc/rfc2518.txt'> HTTP
   * Headers for Distributed Authoring</a> section 9 for more information.
   */
  public static final String IF                     = "If";

  /**
   * WebDav "Timeout" header. See <a href='http://www.ietf.org/rfc/rfc2518.txt'>
   * HTTP Headers for Distributed Authoring</a> section 9 for more information.
   */
  public static final String TIMEOUT                = "Timeout";

  /**
   * WebDav multipart/byteranges header.
   */
  public static final String MULTIPART_BYTERANGES   = "multipart/byteranges; boundary=";

  /**
   * WebDav "Overwrite" header. See <a
   * href='http://www.ietf.org/rfc/rfc2518.txt'> HTTP Headers for Distributed
   * Authoring</a> section 9 for more information.
   */
  public static final String OVERWRITE              = "Overwrite";

  /**
   * JCR-specific header to add an opportunity to create nodes of the specific
   * types via WebDAV.
   */
  public static final String FILE_NODETYPE          = "File-NodeType";

  /**
   * JCR-specific header to add an opportunity to create nodes of the specific
   * types via WebDAV.
   */
  public static final String CONTENT_NODETYPE       = "Content-NodeType";

  /**
   * JCR-specific header to add an opportunity to set node mixins via WebDAV.
   */
  public static final String CONTENT_MIXINTYPES     = "Content-MixinTypes";

  /**
   * X-HTTP-Method-Override header. See <a
   * href='http://code.google.com/apis/gdata/docs/2.0/basics.html'>here</a>.
   */
  public static final String X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";

  /**
   * User-Agent header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP Header
   * Field Definitions sec. 14.43 Transfer-Encoding</a>.
   */
  public static final String USERAGENT              = "User-Agent";

  /**
   * Transfer-Encoding header. See <a
   * href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html'> HTTP Header
   * Field Definitions sec. 14.41 Transfer-Encoding</a>.
   */
  public static final String TRANSFER_ENCODING      = "Transfer-Encoding";

}

