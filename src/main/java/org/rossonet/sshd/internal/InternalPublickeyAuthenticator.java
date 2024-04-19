package org.rossonet.sshd.internal;

import java.security.PublicKey;
import java.util.Map;

import org.apache.sshd.common.AttributeRepository.AttributeKey;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.rossonet.rpc.PasswordAuthenticationProvider;
import org.rossonet.sshd.MinaSshServer;

public class InternalPublickeyAuthenticator implements PublickeyAuthenticator {

	private final MinaSshServer sshServer;

	public InternalPublickeyAuthenticator(final MinaSshServer sshServer) {
		this.sshServer = sshServer;
	}

	@Override
	public boolean authenticate(final String username, final PublicKey key, final ServerSession session)
			throws AsyncAuthException {
		for (final PasswordAuthenticationProvider passwordAuthenticationProvider : sshServer
				.getPasswordAuthenticationProviders()) {
			if (passwordAuthenticationProvider.authenticate(username, key)) {
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
