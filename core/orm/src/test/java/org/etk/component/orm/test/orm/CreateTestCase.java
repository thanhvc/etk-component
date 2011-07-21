package org.etk.component.orm.test.orm;

import org.etk.component.orm.test.AbstractTestCase;
import org.etk.orm.api.ORMSessionImpl;
import org.etk.orm.api.Status;
import org.etk.orm.core.DomainSession;
import org.etk.orm.core.EntityContext;
import org.etk.orm.core.ObjectContext;

public class CreateTestCase extends AbstractTestCase {

  @Override
  protected void createDomain() {
    addClass(AClass.class);
  }
  
  public void testCreate() throws Exception {
    DomainSession session = domainLogin();
    ObjectContext ctx = session.create(AClass.class, null);
    assertEquals(EntityContext.class, ctx.getClass());
    assertEquals(Status.TRANSIENT, ctx.getStatus());
    assertEquals("foo", ctx.getMapper().getNodeTypeName());
    assertTrue(ctx.getObject() instanceof AClass);
    assertNull(ctx.getTypeInfo());
    assertSame(session, ctx.getSession());
  }

  public void testCreateNamed() throws Exception {
    DomainSession session = domainLogin();
    ObjectContext ctx = session.create(AClass.class, "aclass");
    assertEquals(EntityContext.class, ctx.getClass());
    assertEquals(Status.TRANSIENT, ctx.getStatus());
    assertEquals("foo", ctx.getMapper().getNodeTypeName());
    assertNull(ctx.getTypeInfo());
    assertSame(session, ctx.getSession());
  }

  public void testCreateThrowsNPE() throws Exception {
    DomainSession session = domainLogin();
    try {
      session.create(null, "aclass");
      fail();
    } catch (NullPointerException ignore) {
    }
  }

  private DomainSession domainLogin() {
    ORMSessionImpl session = login();
    return session.getDomainSession();
  }

}
