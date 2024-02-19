package org.rossonet.rpc;

public class ThreadShellSession extends Thread {

	private final ShellSession shellSession;
	private boolean pause = false;
	private boolean running = true;

	public ThreadShellSession(final ShellSession shellSession) {
		this.shellSession = shellSession;
	}

	public void destroy() {
		running = false;
	}

	public void pause() {
		pause = true;
	}

	public void removePause() {
		pause = false;

	}

	@Override
	public void run() {
		setName(shellSession.getThreadName());
		while (running) {
			try {
				if (!pause) {
					shellSession.periodicalCall();
				}
				Thread.sleep(10L);
			} catch (final Throwable a) {
				shellSession.getRpcEngine().fireThrowable(a);
			}
		}
	}

}
