package org.rossonet.sshd.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.rossonet.sshd.MinaSshServer;

public class InternalThreadPoolFactory implements ThreadFactory {

	private final AtomicInteger counter;
	private final MinaSshServer sshServer;

	public InternalThreadPoolFactory(final MinaSshServer sshServer) {
		this.sshServer = sshServer;
		counter = new AtomicInteger(0);
	}

	@Override
	public Thread newThread(final Runnable arg0) {
		final Thread newThread = new Thread(arg0);
		newThread.setName(
				sshServer.getConfiguration().getChildThreadPrefix() + String.valueOf(counter.getAndIncrement()));
		newThread.setPriority(sshServer.getConfiguration().getChildThreadPriority());
		return newThread;
	}

}
