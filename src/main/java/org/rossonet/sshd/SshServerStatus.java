package org.rossonet.sshd;

import java.time.Instant;

public interface SshServerStatus {

	public enum State {
		INIT, CONFIGURED, STARTED, STOPPED, FAULT, CONFIGURATION_ERROR
	}

	State getLastState();

	Instant getLastStateChangeAt();

	State getState();

	void setState(State newState);

}
