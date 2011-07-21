package org.etk.kernel.container.client;

public class MockClientInfo implements ClientInfo {
	public MockClientInfo() {
	}

	public String getClientType() {
		return "N/A";
	}

	public String getRemoteUser() {
		return "exo";
	}

	public String getIpAddress() {
		return "127.0.0.1";
	}

	public String getClientName() {
		return "Mock client";
	}

}
