package org.etk.reflect.api;


/**
 * Defines the visitor base interface.
 * 
 * @author thanh_vucong
 *
 * @param <V>
 * @param <S>
 */
public interface Visitor<V extends Visitor<V, S>, S extends VisitorStrategy<V, S>> {
}