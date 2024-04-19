package org.rossonet.sshd;

import java.time.Instant;

public class BaseSshServerStatus implements SshServerStatus {

	private State state;
	private State lastState;
	private Instant lastStateChangeAt;

	@Override
	public State getLastState() {
		return lastState;
	}

	@Override
	public Instant getLastStateChangeAt() {
		return lastStateChangeAt;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	// metodo usato solo in package, per cambiare stato usare i metodi di SshServer
	public synchronized void setState(final State newState) {
		lastState = state;
		lastStateChangeAt = Instant.now();
		state = newState;
	}

}
