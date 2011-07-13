package org.etk.orm.plugins.common.jcr;


public interface PathVisitor {

  public abstract void onPathSegment(String s, int start, int end, Integer number) throws PathException;

  public abstract void onPrefixPathSegment(String s, int prefixStart, int prefixEnd, int start, int end, Integer number) throws PathException;

  public abstract void onURIPathSegment(String s, int uriStart, int uriEnd, int start, int end, Integer number) throws PathException;

  public abstract void onSelf() throws PathException;

  public abstract void onParent() throws PathException;


}