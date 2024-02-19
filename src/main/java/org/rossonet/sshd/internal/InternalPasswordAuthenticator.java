package org.rossonet.sshd.internal;

import java.util.Map;

import org.apache.sshd.common.AttributeRepository.AttributeKey;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import org.rossonet.rpc.PasswordAuthenticationProvider;
import org.rossonet.sshd.MinaSshServer;

public class InternalPasswordAuthenticator implements PasswordAuthenticator {

	private final MinaSshServer sshServer;

	public InternalPasswordAuthenticator(final MinaSshServer sshServer) {
		this.sshServer = sshServer;
	}

	@Override
	public boolean authenticate(final String username, final String password, final ServerSession session)
			throws PasswordChangeRequiredException, AsyncAuthException {
		for (final PasswordAuthenticationProvider passwordAuthenticationProvider : sshServer
				.getPasswordAuthenticationProviders()) {
			if (passwordAuthenticationProvider.authenticate(username, password)) {
				final Map<String, String> sessionData = passwordAuthenticationProvider.getSessionData();
				if (sessionData != null && !sessionData.isEmpty()) {
					session.setAttribute(new AttributeKey<Map<String, String>>(), sessionData);
				}
				return true;
			}
		}
		return false;
	}

}
