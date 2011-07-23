package org.etk.kernel.container;

import java.util.List;

public interface SessionManagerContainer {

  public List<SessionContainer> getLiveSessions();

  public void removeSessionContainer(String id);

  public SessionContainer createSessionContainer(String id, String owner);

  public SessionManager getSessionManager();

}
