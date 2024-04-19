package org.rossonet.agent;

import java.util.ArrayList;
import java.util.List;

import org.rossonet.rpc.RpcCommand;
import org.rossonet.rpc.RpcEngine;
import org.rossonet.rpc.jline3.Jline3RpcEngine;
import org.rossonet.rules.base.AbstractBaseRulesEngine;
import org.rossonet.rules.base.FactProvider;
import org.rossonet.sshd.BaseSshServer;
import org.rossonet.sshd.MinaSshServer;
import org.rossonet.sshd.SshServerStatus.State;
import org.rossonet.telemetry.TelemetryStorage;

public class AgentTest extends AbstractBaseRulesEngine {

	private final static List<RpcCommand> commandRegistry = new ArrayList<>();

	public static void addCommandToRegistry(final RpcCommand command) {
		commandRegistry.add(command);
	}

	public static void main(final String[] args) {
		Thread.currentThread().setName("agent");
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		final TelemetryStorage telemetry = new TestTelemetryStorage();
		final RpcEngine rpcEngine = new Jline3RpcEngine(telemetry.getSshServerStorage().getShellSessions());
		final BaseSshServer sshServer = new MinaSshServer(telemetry.getSshServerStorage().getServerConfig(), rpcEngine,
				telemetry.getSshServerStorage().getServerStatus());
		final AgentTest agent = new AgentTest(telemetry, rpcEngine, sshServer);
		sshServer.start();
		while ((!sshServer.getStatus().getState().equals(State.CONFIGURATION_ERROR))
				&& (!sshServer.getStatus().getState().equals(State.STOPPED))
				&& (!sshServer.getStatus().getState().equals(State.FAULT))) {
			try {
				Thread.sleep(1000L);
				agent.fireRules();
				// TODO collegare la console a RpcEngine
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			sshServer.close();
			telemetry.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final TelemetryStorage telemetry;

	private final RpcEngine rpcEngine;

	private final BaseSshServer sshServer;

	public AgentTest(final TelemetryStorage telemetry, final RpcEngine rpcEngine, final BaseSshServer sshServer) {
		this.telemetry = telemetry;
		this.rpcEngine = rpcEngine;
		this.sshServer = sshServer;
		setCachedMemory(telemetry);
		addCommandsToRpcEngine();
		addFactProvider((FactProvider) rpcEngine);
	}

	private void addCommandsToRpcEngine() {
		rpcEngine.getCommands().add(new TestRpcCommand());
		for (final RpcCommand c : commandRegistry) {
			rpcEngine.getCommands().add(c);
		}
	}

}
