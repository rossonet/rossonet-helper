package org.rossonet.sshd;

import org.rossonet.sshd.internal.PortForwarderManager;
import org.rossonet.sshd.internal.SshServerConfiguration;

public interface BaseSshServer extends AutoCloseable {

	public SshServerConfiguration getConfiguration();

	public PortForwarderManager getPortForwarderManager();

	public SshServerStatus getStatus();

	public void start();

	public void stop();

}
