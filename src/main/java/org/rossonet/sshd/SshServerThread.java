package org.rossonet.sshd;

final class SshServerThread extends Thread {

	private final MinaSshServer sshServer;
	private boolean forceStopped = false;

	public SshServerThread(final MinaSshServer sshServer) {
		this.sshServer = sshServer;
	}

	public void forceStop() {
		forceStopped = true;

	}

	@Override
	public void run() {
		setName(sshServer.getConfiguration().getSshServerThreadName());
		setPriority(sshServer.getConfiguration().getSshServerThreadPriority());
		while (!forceStopped) {
			try {
				sshServer.periodicalThreadAction();
				Thread.sleep(sshServer.getConfiguration().getSshServerThreadSleepingTimeMs());
			} catch (final Throwable error) {
				sshServer.fireServerException(error);
			}
		}
	}

}
