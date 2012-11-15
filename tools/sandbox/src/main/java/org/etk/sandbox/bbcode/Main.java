/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
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
package org.etk.sandbox.bbcode;

/**
 * Created by The eXo Platform SAS
 * Author : thanh_vucong
 *          thanh_vucong@exoplatform.com
 * Sep 27, 2012  
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.lang.StringBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Main {

  public static class BBCode {
    public final boolean closing;

    public final String  tag;

    public final String  param;

    private BBCode(String tag, String param, boolean closing) {
      this.tag = tag;
      this.closing = closing;
      this.param = param;
    }

    public String toString() {
      if (this.closing)
        return "[/" + this.tag + "]";
      if (this.param != null)
        return "[" + this.tag + "=" + this.param + "]";
      return "[" + this.tag + "]";
    }

    private static Pattern openingPattern          = Pattern.compile("([a-z]+)");

    private static Pattern openingPatternWithParam = Pattern.compile("([a-z]+)=(.*?)");

    private static Pattern closingPattern          = Pattern.compile("/([a-z]+)");

    public static BBCode valueOf(String s) {
      Matcher m = openingPattern.matcher(s);
      if (m.matches())
        return new BBCode(m.group(1), null, false);

      m = openingPatternWithParam.matcher(s);
      if (m.matches())
        return new BBCode(m.group(1), m.group(2), false);

      m = closingPattern.matcher(s);
      if (m.matches())
        return new BBCode(m.group(1), null, true);

      return null;
    }
  }

  public static class Node {

    public void render(StringBuilder buf) {
    }

  }

  public static class TextNode extends Node {

    private String text;

    TextNode(String text) {
      this.text = text;
    }

    public void render(StringBuilder buf) {
      buf.append(this.text);
    }

  }

  public static class ContainerNode extends Node {

    protected ArrayList<Node> children = new ArrayList<Node>();

    public void add(Node node) {
      this.children.add(node);
    }

    public void remove(Node node) {
      children.remove(node);
    }

    public void render(StringBuilder buf) {
      renderChildren(buf);
    }

    protected void renderChildren(StringBuilder buf) {
      for (Node node : this.children)
        node.render(buf);
    }

  }

  public static class RootNode extends ContainerNode {
  }

  public static class BBCodeNode extends ContainerNode {

    protected BBCode bbcode;

    public BBCodeNode(BBCode bbcode) {
      this.bbcode = bbcode;
    }

    public boolean isSameTag(BBCode bbcode) {
      return this.bbcode.tag.equals(bbcode.tag);
    }

    public void flushTo(ContainerNode target) {
      target.add(new TextNode(this.bbcode.toString()));
      for (Node node : this.children)
        target.add(node);
    }

    public void render(StringBuilder buf) {
      buf.append("<");
      buf.append(this.bbcode.tag);
      buf.append(">");
      renderChildren(buf);
      buf.append("</");
      buf.append(this.bbcode.tag);
      buf.append(">");
    }

  }

  public static class ColorNode extends BBCodeNode {

    public ColorNode(BBCode t) {
      super(t);
    }

    public void render(StringBuilder buf) {
      buf.append("<span style='color:");
      buf.append(bbcode.param);
      buf.append("'>");
      renderChildren(buf);
      buf.append("</span>");
    }

  }

  public static class BBCodeNodeFactory {

    private static HashSet<String> simpleCodes = new HashSet<String>();
    static {
      simpleCodes.add("b");
      simpleCodes.add("i");
      simpleCodes.add("s");
      simpleCodes.add("u");
    }

    public static BBCodeNode createNode(BBCode bbcode) {
      if (simpleCodes.contains(bbcode.tag))
        return new BBCodeNode(bbcode);
      if ("color".equals(bbcode.tag))
        return new ColorNode(bbcode);
      return null;
    }

  }

  public static class BBCodeProcessor {

    public static String process(String input) {
      BBCodeProcessor processor = new BBCodeProcessor();
      processor.parse(input);
      StringBuilder buf = new StringBuilder();
      processor.render(buf);
      return buf.toString();
    }

    private Stack<ContainerNode> stack = new Stack<ContainerNode>();

    private ContainerNode        top   = new RootNode();

    private void parse(String s) {
      int len = s.length(), start = 0, i = 0;

      while (i < len) {

        int b = s.indexOf("[", i);
        if (b == -1)
          break;

        int e = s.indexOf("]", b + 1);
        if (e == -1)
          break;

        BBCode bbcode = BBCode.valueOf(s.substring(b + 1, e));
        if (bbcode != null) {
          if (start < b)
            handleText(s.substring(start, b));
          handleBBCode(bbcode);
          i = start = e + 1;
        } else {
          i = b + 1;
        }
      }

      if (start < len)
        handleText(s.substring(start));

      handleEnd();
    }

    private void handleText(String text) {
      top.add(new TextNode(text));
    }

    private void handleBBCode(BBCode bbcode) {
      if (bbcode.closing)
        handleClosing(bbcode);
      else
        handleOpening(bbcode);
    }

    private void handleOpening(BBCode bbcode) {
      BBCodeNode node = BBCodeNodeFactory.createNode(bbcode);
      if (node == null) {
        handleText(bbcode.toString());
      } else {
        stack.push(top);
        top = node;
      }
    }

    private void handleClosing(BBCode bbcode) {
      while (!stack.isEmpty()) {
        BBCodeNode top = (BBCodeNode) this.top;
        ContainerNode node = stack.pop();
        this.top = node;
        if (top.isSameTag(bbcode)) {
          node.add(top);
          this.top = node;
          return;
        }
        top.flushTo(node);
      }
      top.add(new TextNode(bbcode.toString()));
    }

    private void handleEnd() {
      while (!stack.isEmpty()) {
        ContainerNode node = stack.pop();
        ((BBCodeNode) top).flushTo(node);
        top = node;
      }
    }

    private void render(StringBuilder buf) {
      top.render(buf);
    }

  }

  public static void main(String args[]) {
    java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
    String s;
    try {
      while ((s = r.readLine()) != null) {
        System.out.println(s + " -> " + BBCodeProcessor.process(s));
      }
    } catch (Exception e) {
    }
  }

}
