package org.etk.orm.plugins.jcr;

import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;


public interface SessionWrapper {

  Property getProperty(Node node, String relPath) throws RepositoryException;

  Iterator<Property> getProperties(Node node) throws RepositoryException;

  Node getNode(Node node, String relPath) throws RepositoryException;

  Node getNode(String path) throws RepositoryException;

  NodeType getNodeType(String nodeTypeName) throws RepositoryException;

  Node addNode(Node parentNode, String relPath, String primartyNodeTypeName, List<String> mixinNodeTypeNames) throws RepositoryException;

  void orderBefore(Node parentNode, Node srcNode, Node dstNode) throws RepositoryException;

  void move(Node srcNode, Node dstNode, String dstName) throws RepositoryException;

  Node getParent(Node childNode) throws RepositoryException;

  Iterator<Node> getChildren(Node parentNode) throws RepositoryException;

  Node getChild(Node parentNode, String name) throws RepositoryException;

  Node getNodeByUUID(String uuid) throws RepositoryException;

  void remove(Node node) throws RepositoryException;

  boolean canAddMixin(Node node, String mixinTypeName) throws RepositoryException;

  void addMixin(Node node, String mixinTypeName) throws RepositoryException;

  boolean haxMixin(Node node, String mixinTypeName) throws RepositoryException;

  void save() throws RepositoryException;

  Node setReferenced(Node referent, String propertyName, Node referenced, LinkType linkType) throws RepositoryException;

  Node getReferenced(Node referent, String propertyName, LinkType linkType) throws RepositoryException;

  Iterator<Node> getReferents(Node referenced, String propertyName, LinkType linkType) throws RepositoryException;

  Session getSession();

  void close();

  boolean isClosed();

  Query createQuery(String statement) throws RepositoryException;

  QueryResult executeQuery(Query query, Long offset, Long limit) throws RepositoryException;

  int hits(QueryResult result) throws RepositoryException;
}