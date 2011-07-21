package org.etk.orm.plugins.jcr.type;

import javax.jcr.nodetype.NodeType;

import org.etk.orm.plugins.jcr.NodeTypeInfo;


/**
 * <p>Meta information about a mixin type.</p>
 *
 * <p>This object does not hold a reference to an existing node type object.</p>
 *
 */
public class MixinTypeInfo extends NodeTypeInfo {

  public MixinTypeInfo(NodeType nodeType) {
    super(nodeType);

    //
    if (!nodeType.isMixin()) {
      throw new IllegalArgumentException();
    }
  }
}
