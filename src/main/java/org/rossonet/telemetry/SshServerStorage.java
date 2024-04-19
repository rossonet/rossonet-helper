package org.rossonet.telemetry;

import java.util.Set;

import org.rossonet.rpc.ShellSession;
import org.rossonet.sshd.SshServerStatus;
import org.rossonet.sshd.internal.SshServerConfiguration;

public interface SshServerStorage {

	SshServerConfiguration getServerConfig();

	SshServerStatus getServerStatus();

	Set<ShellSession> getShellSessions();

}
