package org.rossonet.sshd.internal;

import org.apache.sshd.server.ServerBuilder;
import org.rossonet.sshd.MinaSshServer;

public class InternalServerBuilder extends ServerBuilder {

	public InternalServerBuilder(final MinaSshServer sshServer) {
		super();
		fillWithDefaultValues();
	}

}
