package org.rossonet.rpc;

import java.io.InputStream;
import java.io.OutputStream;

import org.rossonet.rpc.exception.RpcException;

public abstract class ShellSession {

	private OutputStream errorStream;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int minThreadPriority = Thread.MIN_PRIORITY + 1;
	private int maxThreadPriority = Thread.MAX_PRIORITY - 2;
	private final ThreadShellSession watchdog = new ThreadShellSession(this);
	private final RpcEngine rpcEngine;

	public ShellSession(final RpcEngine rpcEngine) {
		this.rpcEngine = rpcEngine;
	}

	protected void destroyRpcSession() {
		watchdog.destroy();
	}

	public OutputStream getErrorStream() throws RpcException {
		return errorStream;
	}

	public InputStream getInputStream() throws RpcException {
		return inputStream;
	}

	public int getMaxThreadPriority() {
		return maxThreadPriority;
	}

	public int getMinThreadPriority() {
		return minThreadPriority;
	}

	public OutputStream getOutputStream() throws RpcException {
		return outputStream;
	}

	public RpcEngine getRpcEngine() {
		return rpcEngine;
	}

	public abstract String getSessionDescription();

	protected abstract String getThreadName();

	public int getThreadPriority() {
		return watchdog.getPriority();
	}

	public abstract boolean isByteSession();

	public abstract boolean isCharacterSession();

	public abstract boolean isLineSession();

	protected void pauseRpcSession() {
		watchdog.pause();
	}

	protected abstract void periodicalCall();

	protected void setErrorStream(final OutputStream errorStream) {
		this.errorStream = errorStream;
	}

	protected void setInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
	}

	protected void setMaxThreadPriority(final int maxThreadPriority) {
		this.maxThreadPriority = maxThreadPriority;
	}

	protected void setMinThreadPriority(final int minThreadPriority) {
		this.minThreadPriority = minThreadPriority;
	}

	protected void setOutputStream(final OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setThreadPriority(final int priority) {
		if (priority > maxThreadPriority) {
			watchdog.setPriority(maxThreadPriority);
		} else if (priority < minThreadPriority) {
			watchdog.setPriority(minThreadPriority);
		} else {
			watchdog.setPriority(priority);
		}
	}

	protected void startRpcSession() {
		if (!watchdog.isAlive()) {
			watchdog.start();
		} else {
			watchdog.removePause();
		}
	}

}
