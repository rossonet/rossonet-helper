package org.rossonet.sshd.internal;

import java.io.IOException;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import org.rossonet.rpc.RpcEngine;
import org.rossonet.sshd.MinaSshServer;
import org.rossonet.sshd.bridge.RpcShellSession;

public class InternalShellFactory implements ShellFactory {

	private final MinaSshServer sshServer;
	private RpcShellSession shell;
	private final RpcEngine rpcEngine;

	public InternalShellFactory(final MinaSshServer sshServer, final RpcEngine rpcEngine) {
		this.sshServer = sshServer;
		this.rpcEngine = rpcEngine;
	}

	@Override
	public Command createShell(final ChannelSession channel) throws IOException {
		shell = new RpcShellSession(sshServer, rpcEngine, channel);
		sshServer.getRpcEngine().registerShellSession(shell);
		return shell.getSshCommand();
	}

}
