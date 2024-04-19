package org.rossonet.rpc;

import java.util.Collection;

public interface RpcEngine {

	void fireThrowable(Throwable Exception);

	Collection<RpcCommand> getCommands();

	Collection<ShellSession> getSessions();

	void killSshShell(ShellSession session);

	void registerShellSession(ShellSession shellSession);

}
