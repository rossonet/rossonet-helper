package org.rossonet.sshd.internal;

import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.server.forward.ForwardingFilter;
import org.rossonet.sshd.MinaSshServer;

public class InternalForwardingFilter implements ForwardingFilter {

	private final MinaSshServer sshServer;

	public InternalForwardingFilter(final MinaSshServer sshServer) {
		this.sshServer = sshServer;
	}

	@Override
	public boolean canConnect(final Type type, final SshdSocketAddress address, final Session session) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canForwardAgent(final Session session, final String requestType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canForwardX11(final Session session, final String requestType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canListen(final SshdSocketAddress address, final Session session) {
		// TODO Auto-generated method stub
		return true;
	}

}
