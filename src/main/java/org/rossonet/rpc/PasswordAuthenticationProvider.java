package org.rossonet.rpc;

import java.security.PublicKey;
import java.util.Map;

public interface PasswordAuthenticationProvider {

	boolean authenticate(String username, PublicKey key);

	boolean authenticate(String username, String password);

	Map<String, String> getSessionData();

}
