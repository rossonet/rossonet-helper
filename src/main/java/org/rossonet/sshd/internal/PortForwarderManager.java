package org.rossonet.sshd.internal;

import org.rossonet.sshd.MinaSshServer;

public class PortForwarderManager {

	private final MinaSshServer sshServer;

	public PortForwarderManager(final MinaSshServer sshServer) {
		this.sshServer = sshServer;
	}

}
