package org.etk.core.rest.impl;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import org.etk.core.rest.impl.header.AcceptLanguage;
import org.etk.core.rest.impl.header.AcceptLanguageHeaderDelegate;
import org.etk.core.rest.impl.header.AcceptMediaType;
import org.etk.core.rest.impl.header.AcceptMediaTypeHeaderDelegate;
import org.etk.core.rest.impl.header.CacheControlHeaderDelegate;
import org.etk.core.rest.impl.header.CookieHeaderDelegate;
import org.etk.core.rest.impl.header.DateHeaderDelegate;
import org.etk.core.rest.impl.header.EntityTagHeaderDelegate;
import org.etk.core.rest.impl.header.LocaleHeaderDelegate;
import org.etk.core.rest.impl.header.MediaTypeHeaderDelegate;
import org.etk.core.rest.impl.header.NewCookieHeaderDelegate;
import org.etk.core.rest.impl.header.StringHeaderDelegate;
import org.etk.core.rest.impl.header.URIHeaderDelegate;

public class RuntimeDelegateImpl extends RuntimeDelegate {

  /**
   * HeaderDelegate cache.
   */
  @SuppressWarnings("unchecked")
  private final Map<Class<?>, HeaderDelegate> headerDelegates = new HashMap<Class<?>, HeaderDelegate>();


  /**
   * Should be used only once for initialize.
   * 
   * @see RuntimeDelegate#setInstance(RuntimeDelegate)
   * @see RuntimeDelegate#getInstance()
   */
  public RuntimeDelegateImpl() {
    // JSR-311
    headerDelegates.put(MediaType.class, new MediaTypeHeaderDelegate());
    headerDelegates.put(CacheControl.class, new CacheControlHeaderDelegate());
    headerDelegates.put(Cookie.class, new CookieHeaderDelegate());
    headerDelegates.put(NewCookie.class, new NewCookieHeaderDelegate());
    headerDelegates.put(EntityTag.class, new EntityTagHeaderDelegate());
    headerDelegates.put(Date.class, new DateHeaderDelegate());
    // external
    headerDelegates.put(AcceptLanguage.class, new AcceptLanguageHeaderDelegate());
    headerDelegates.put(AcceptMediaType.class, new AcceptMediaTypeHeaderDelegate());
    headerDelegates.put(String.class, new StringHeaderDelegate());
    headerDelegates.put(URI.class, new URIHeaderDelegate());
    headerDelegates.put(Locale.class, new LocaleHeaderDelegate());
  }



  /**
   * End Points is not supported. {@inheritDoc}
   */
  @Override
  public <T> T createEndpoint(Application applicationConfig, Class<T> type) {
    throw new UnsupportedOperationException("End Points is not supported");
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
    // TODO mechanism for use external HeaderDelegate
    return (HeaderDelegate<T>) headerDelegates.get(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResponseBuilder createResponseBuilder() {
    return new ResponseImpl.ResponseBuilderImpl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UriBuilder createUriBuilder() {
    return new UriBuilderImpl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VariantListBuilder createVariantListBuilder() {
    return new VariantListBuilderImpl();
  }

}
