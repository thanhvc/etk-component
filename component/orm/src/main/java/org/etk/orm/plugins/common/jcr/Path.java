package org.etk.orm.plugins.common.jcr;

import java.util.ArrayList;
import java.util.List;


public class Path {

  public static void parseAbsolutePath(String path, PathVisitor visitor) throws PathException {
    Parser.parseAbsolutePath(visitor, path, 0, path.length());
  }

  public static void parsePath(String path, PathVisitor visitor) throws PathException {
    Parser.parsePath(visitor, path, 0, path.length());
  }

  public static void parseRelativePath(PathVisitor visitor, String s) throws PathException {
    Parser.parseRelativePath(visitor, s, 0, s.length());
  }

  public static void parsePathSegment(String s, PathVisitor visitor) throws PathException {
    Parser.parsePathSegment(visitor, s, 0, s.length());
  }

  private static class Normalizer implements PathVisitor {

    /** . */
    private final StringBuilder builder = new StringBuilder("/");

    public void onPathSegment(String s, int start, int end, Integer number) throws PathException {
      if (builder.length() > 1) {
        builder.append('/');
      }
      builder.append(s, start, end);
      if (number != null) {
        builder.append('[');
        builder.append(number);
        builder.append(']');
      }
    }

    public void onPrefixPathSegment(String s, int prefixStart, int prefixEnd, int start, int end, Integer number) throws PathException {
      if (builder.length() > 1) {
        builder.append('/');
      }
      builder.append(s, prefixStart, prefixEnd);
      builder.append(':');
      builder.append(s, start, end);
      if (number != null) {
        builder.append('[');
        builder.append(number);
        builder.append(']');
      }
      builder.append('/');
    }

    public void onURIPathSegment(String s, int uriStart, int uriEnd, int start, int end, Integer number) throws PathException {
      if (builder.length() > 1) {
        builder.append('/');
      }
      builder.append('{');
      builder.append(s, uriStart, uriEnd);
      builder.append('}');
      builder.append(s, start, end);
      if (number != null) {
        builder.append('[');
        builder.append(number);
        builder.append(']');
      }
      builder.append('/');
    }

    public void onSelf() throws PathException {
      // Do nothing
    }

    public void onParent() throws PathException {
      if (builder.length() == 1) {
        throw new PathException("Invalid absolute path");
      }
      int pos = builder.lastIndexOf("/");
      if (pos == 0) {
        builder.setLength(1);
      } else {
        builder.setLength(pos);
      }
    }
  }

  public static String normalizeAbsolutePath(String absolutePath) throws PathException {
    Normalizer normalizer = new Normalizer();
    parseAbsolutePath(absolutePath, normalizer);
    return normalizer.builder.toString();
  }

  private static class Splitter implements PathVisitor {

    /** . */
    private final List<String> pathSegments = new ArrayList<String>();

    public void onPathSegment(String s, int start, int end, Integer number) throws PathException {
      String pathSegment = s.substring(start, end);
      if (number != null) {
        pathSegment += "[" + number + "]";
      }
      pathSegments.add(pathSegment);
    }

    public void onPrefixPathSegment(String s, int prefixStart, int prefixEnd, int start, int end, Integer number) throws PathException {
      String pathSegment = s.substring(prefixStart, prefixEnd) + ":" + s.substring(start, end);
      if (number != null) {
        pathSegment += "[" + number + "]";
      }
      pathSegments.add(pathSegment);
    }

    public void onURIPathSegment(String s, int uriStart, int uriEnd, int start, int end, Integer number) throws PathException {
      String pathSegment = "{" + s.substring(uriStart, uriEnd) + "}" + s.substring(start, end);
      if (number != null) {
        pathSegment += "[" + number + "]";
      }
      pathSegments.add(pathSegment);
    }

    public void onSelf() throws PathException {
      // Do nothing
    }

    public void onParent() throws PathException {
      if (pathSegments.isEmpty()) {
        throw new PathException("Invalid absolute path");
      }
      pathSegments.remove(pathSegments.size() - 1);
    }
  }

  public static List<String> splitAbsolutePath(String absolutePath) throws PathException {
    Splitter splitter = new Splitter();
    parseAbsolutePath(absolutePath, splitter);
    return splitter.pathSegments;
  }

  /** . */
  private static final PathVisitor LOCAL_NAME_VALIDATOR = new PathVisitor() {
    public void onPathSegment(String s, int start, int end, Integer number) {
    }
    public void onPrefixPathSegment(String s, int prefixStart, int prefixEnd, int start, int end, Integer number) throws PathException {
      throw new PathException();
    }
    public void onURIPathSegment(String s, int uriStart, int uriEnd, int start, int end, Integer number) throws PathException {
      throw new PathException();
    }
    public void onSelf() throws PathException {
      throw new PathException();
    }
    public void onParent() throws PathException {
      throw new PathException();
    }
  };

  /** . */
  private static final PathVisitor NAME_VALIDATOR = new PathVisitor() {
    public void onPathSegment(String s, int start, int end, Integer number) {
    }
    public void onPrefixPathSegment(String s, int prefixStart, int prefixEnd, int start, int end, Integer number) {
    }
    public void onURIPathSegment(String s, int uriStart, int uriEnd, int start, int end, Integer number) {
    }
    public void onSelf() throws PathException {
      throw new PathException();
    }
    public void onParent() throws PathException {
      throw new PathException();
    }
  };


  public static void validateName(String name) {
    try {
      parsePathSegment(name, NAME_VALIDATOR);
    }
    catch (PathException e) {
      IllegalArgumentException iae = new IllegalArgumentException("Invalid name " + name);
      iae.initCause(e);
      throw iae;
    }
  }

  public static void validateLocalName(String localName) {
    try {
      parsePathSegment(localName, LOCAL_NAME_VALIDATOR);
    }
    catch (PathException e) {
      IllegalArgumentException iae = new IllegalArgumentException("Invalid local name " + localName);
      iae.initCause(e);
      throw iae;
    }
  }
}
