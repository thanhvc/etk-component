package org.etk.kernel.container;

import java.util.List;

public interface SessionManager {

	public List<SessionContainer> getLiveSessions();

	public SessionContainer getSessionContainer(String id);

	public void removeSessionContainer(String id);

	public void addSessionContainer(SessionContainer scontainer);
}
