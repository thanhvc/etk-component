/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
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
package org.etk.model.api;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;

import org.etk.common.logging.Logger;
import org.etk.orm.api.BuilderException;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 14, 2011  
 */
public abstract class EntityBuilder {

  /** . */
  private static final Logger log = Logger.getLogger(EntityBuilder.class.getName());
  
  
  /** The domain classes. */
  private final Set<Class<?>> classes;

  /** . */
  private boolean initialized;

  /** For stuff that need to happen under synchronization. */
  private final Object lock = new Object();

  public EntityBuilder() {
    this.initialized = false;
    this.classes = new HashSet<Class<?>>();
  }
  
  /**
   * Adds a class definition.
   *
   * @param clazz the class to add
   * @throws NullPointerException if the provided class is null
   * @throws IllegalStateException if the builder is already initialized
   */
  public void add(Class<?> clazz) throws NullPointerException, IllegalStateException {
    add(clazz, new Class<?>[0]);
  }

  
  /**
   * Adds a class definition.
   *
   * @param first the first class to add
   * @param other the other classes to add
   * @throws NullPointerException if the provided class is null
   * @throws IllegalStateException if the builder is already initialized
   */
  public void add(Class<?> first, Class<?>... other) throws NullPointerException, IllegalStateException {
    if (first == null) {
      throw new NullPointerException();
    }
    if (other == null) {
      throw new NullPointerException();
    }
    Set<Class<?>> toAdd = new HashSet<Class<?>>(1 + other.length);
    toAdd.add(first);
    for (Class<?> clazz : other) {
      if (clazz == null) {
        throw new IllegalArgumentException("No array containing a null class accepted");
      }
      toAdd.add(clazz);
    }
    synchronized (lock) {
      if (initialized) {
        throw new IllegalStateException("Cannot add a class to an initialized builder");
      }
      classes.addAll(toAdd);
    }
  }
  
  /**
   * Create and return an instance of the builder.
   *
   * @return the EntityBuilder instance
   */
  public static EntityBuilder create() {
    ServiceLoader<EntityBuilder> loader = ServiceLoader.load(EntityBuilder.class);
    Iterator<EntityBuilder> i =  loader.iterator();
    Throwable throwable = null;
    while (i.hasNext()) {
      try {
        EntityBuilder builder = i.next();
        log.debug("Found EntityBuilder implementation " + builder.getClass().getName());
        return builder;
      }
      catch (ServiceConfigurationError error) {
        if (throwable == null) {
          throwable = error;
        }
        log.debug("Could not load EntityBuilder implementation, will use next provider", error);
      }
    }
    throw new BuilderException("Could not instanciate builder", throwable);
  }
  
  /**
   * Builds the runtime and return a configured {@link org.etk.sample.model.EntityManager} instance.
   *
   * @param config the configuration to use
   * @return the EntityManager instance
   * @throws BuilderException any builder exception
   */
  public final EntityManager build() throws BuilderException {

    // Init if needed
    init();
    //
    return boot();
  }

  /**
   * Initialize the builder, this operation should be called once per builder, unlike the {@link #build(Configuration)}
   * operation that can be called several times with different configurations. This operation is used to perform the
   * initialization that is common to any configuration such as building the meta model from the classes.
   *
   * @return whether or not initialization occured
   * @throws BuilderException any exception that would prevent the initialization to happen correctly
   */
  public final boolean init() throws BuilderException {
    // Init if needed
    synchronized (lock) {
      if (!initialized) {
        init(classes);
        initialized = true;
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * Resolver these classes to prepare for mapping
   * @param classes
   * @throws BuilderException
   */
  protected abstract void init(Set<Class<?>> classes) throws BuilderException;

  
  protected abstract EntityManager boot() throws BuilderException;
  
}
