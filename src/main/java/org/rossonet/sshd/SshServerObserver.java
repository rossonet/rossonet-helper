package org.rossonet.sshd;

public interface SshServerObserver {

	public void fireException(final Throwable error);

	public void fireMessage(String message);

}
