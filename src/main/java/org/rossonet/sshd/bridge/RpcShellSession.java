package org.rossonet.sshd.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.rossonet.rpc.RpcEngine;
import org.rossonet.rpc.ShellSession;
import org.rossonet.sshd.BaseSshServer;

public class RpcShellSession extends ShellSession implements Command {

	private ChannelSession channel;
	private final BaseSshServer sshServer;
	private ExitCallback exitCallback;
	private Environment env;

	public RpcShellSession(final BaseSshServer sshServer, final RpcEngine rpcEngine, final ChannelSession channel) {
		super(rpcEngine);
		this.channel = channel;
		this.sshServer = sshServer;
	}

	@Override
	public void destroy(final ChannelSession channel) throws Exception {
		this.channel = channel;
		destroyRpcSession();
	}

	@Override
	public String getSessionDescription() {
		return channel.getServerSession().getUsername() + " - " + channel.toString();
	}

	public Command getSshCommand() {
		return this;
	}

	@Override
	protected String getThreadName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isByteSession() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCharacterSession() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLineSession() {
		return true;
	}

	@Override
	protected void periodicalCall() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setErrorStream(final OutputStream err) {
		setErrorStream(err);

	}

	@Override
	public void setExitCallback(final ExitCallback callback) {
		this.exitCallback = callback;
	}

	@Override
	public void setInputStream(final InputStream in) {
		setInputStream(in);
	}

	@Override
	public void setOutputStream(final OutputStream out) {
		setOutputStream(out);
	}

	@Override
	public void start(final ChannelSession channel, final Environment env) throws IOException {
		this.channel = channel;
		this.env = env;
		startRpcSession();
	}

}
