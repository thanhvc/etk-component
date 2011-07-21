package org.etk.orm.api;

/**
 * The type of a relationship. It defines the semantics about how the relationship is maintained between
 * nodes.
 *
 * @see org.chromattic.api.annotations.OneToMany
 * @see org.chromattic.api.annotations.ManyToOne
 */
public enum RelationshipType {

  /**
   * A hierarchic relationship uses the natural parent child relationship to associate nodes. It is valid either
   * for one to one or one to many relationship.
   */
  HIERARCHIC,

  /**
   * A reference relationship uses reference a typed property to associate nodes. It is only valid for one to many
   * relationships.
   */
  REFERENCE,

  /**
   * A path relationship uses path a typed property to associate nodes. It is only valid for one to many
   * relationships.
   */
  PATH,

  /**
   * <p>An embedded relationship defines a relationship between two types that are have a relationship between their
   * node types.</p>
   *
   * <p>The owner side of the relationship must be a primary node type, the owned side of the relationship can be any
   * node type. Embedded relationship only exist for {@link org.chromattic.api.annotations.OneToOne} relationships</p>
   *
   * <p>When the owned side is a primary node type, the owned node type must be super node type of the owner node type. The
   * relationship is thereby statically defined.</p>
   *
   * <p>When the owned side is a mixin type, the relationship exists when the owner side has the mixin of the owned side.
   * The relationship is static when the owner node type defines the owned mixin type has a super node type directly or
   * indirectly. The relationship is dynamic when the owner node type does not define the owned mixin type has a super node
   * type directly and indirectly, therefore the relationship can be created or destroyed at runtime.</p>
   */
  EMBEDDED

}
