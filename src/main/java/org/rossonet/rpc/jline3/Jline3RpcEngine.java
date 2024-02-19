package org.rossonet.rpc.jline3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.rossonet.rpc.RpcCommand;
import org.rossonet.rpc.RpcEngine;
import org.rossonet.rpc.ShellSession;

public class Jline3RpcEngine implements RpcEngine {

	private final Set<ShellSession> sessions;
	private final Set<RpcCommand> commands;

	public Jline3RpcEngine() {
		this(new HashSet<>());
	}

	public Jline3RpcEngine(final Set<ShellSession> sessions) {
		this.sessions = sessions;
		this.commands = new HashSet<>();
	}

	@Override
	public void fireThrowable(final Throwable Exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<RpcCommand> getCommands() {
		return commands;
	}

	@Override
	public Collection<ShellSession> getSessions() {
		return sessions;
	}

	@Override
	public void killSshShell(final ShellSession session) {
		sessions.remove(session);

	}

	@Override
	public void registerShellSession(final ShellSession shellSession) {
		sessions.add(shellSession);
	}

}
