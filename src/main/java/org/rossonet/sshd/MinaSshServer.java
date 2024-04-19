package org.rossonet.sshd;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;
import org.rossonet.rpc.PasswordAuthenticationProvider;
import org.rossonet.rpc.RpcEngine;
import org.rossonet.sshd.internal.InternalExecutorService;
import org.rossonet.sshd.internal.InternalForwardingFilter;
import org.rossonet.sshd.internal.InternalPasswordAuthenticator;
import org.rossonet.sshd.internal.InternalPublickeyAuthenticator;
import org.rossonet.sshd.internal.InternalServerBuilder;
import org.rossonet.sshd.internal.InternalShellFactory;
import org.rossonet.sshd.internal.PortForwarderManager;
import org.rossonet.sshd.internal.SshServerConfiguration;

public class MinaSshServer implements BaseSshServer {

	private final SshServerConfiguration configuration;
	private final SshServerStatus status;
	private SshServerThread thread;
	private final PortForwarderManager portForwarderManager;
	private final Set<SshServerObserver> throwableObservers = new HashSet<>();
	private final Set<WatchDogTask> watchDogs = new HashSet<>();

	private SshServer server;
	private final Set<PasswordAuthenticationProvider> passwordAuthenticationProviders = new HashSet<>();
	private final RpcEngine rpcEngine;

	public MinaSshServer(final SshServerConfiguration configuration, final RpcEngine rpcEngine) {
		this(configuration, rpcEngine, new BaseSshServerStatus());
	}

	public MinaSshServer(final SshServerConfiguration configuration, final RpcEngine rpcEngine,
			final SshServerStatus serverStatus) {
		this.status = serverStatus;
		status.setState(BaseSshServerStatus.State.INIT);
		this.rpcEngine = rpcEngine;
		this.portForwarderManager = new PortForwarderManager(this);
		this.configuration = configuration;
		if (this.configuration.isValid()) {
			status.setState(BaseSshServerStatus.State.CONFIGURED);
		} else {
			status.setState(BaseSshServerStatus.State.CONFIGURATION_ERROR);
		}
	}

	private void cleanThreadIfNeeded() {
		if (thread != null && thread.isAlive()) {
			thread.forceStop();
		}
	}

	@Override
	public void close() throws Exception {
		cleanThreadIfNeeded();
	}

	private SshServer createSshServer() {
		final SshServer newserver = new InternalServerBuilder(this).build();
		newserver.setPort(configuration.getSshPort());
		newserver.setHost(configuration.getSshHost());
		newserver.setShellFactory(new InternalShellFactory(this, rpcEngine));
		newserver.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
		newserver.setFileSystemFactory(new VirtualFileSystemFactory(configuration.getServerBasePath()));
		newserver.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(configuration.getServerHostKeyPath()));
		newserver.setPublickeyAuthenticator(new InternalPublickeyAuthenticator(this));
		newserver.setPasswordAuthenticator(new InternalPasswordAuthenticator(this));
		newserver.setScheduledExecutorService(new InternalExecutorService(this), true);
		newserver.setForwardingFilter(new InternalForwardingFilter(this));
		return newserver;
	}

	void fireServerException(final Throwable error) {
		for (final SshServerObserver throwableObserver : throwableObservers) {
			throwableObserver.fireException(error);
		}
	}

	void fireServerMessage(final String message) {
		for (final SshServerObserver throwableObserver : throwableObservers) {
			throwableObserver.fireMessage(message);
		}
	}

	@Override
	public SshServerConfiguration getConfiguration() {
		return configuration;
	}

	public Set<PasswordAuthenticationProvider> getPasswordAuthenticationProviders() {
		return passwordAuthenticationProviders;
	}

	@Override
	public PortForwarderManager getPortForwarderManager() {
		return portForwarderManager;
	}

	public RpcEngine getRpcEngine() {
		return rpcEngine;
	}

	@Override
	public SshServerStatus getStatus() {
		return status;
	}

	private void internalFireWatchDogs() {
		for (final WatchDogTask watchDog : watchDogs) {
			watchDog.checkSshServerState(this);
		}
	}

	private synchronized void internalStartedCheckAndAction() throws IOException {
		if (server == null || !server.isStarted()) {
			server = createSshServer();
			server.start();
		}
	}

	private void internalStoppedCheckAndAction() throws IOException, InterruptedException {
		if (server != null) {
			server.stop();
			server.close();
			server = null;
		}
	}

	// task periodico lanciato dal thread che intercetta le eccezioni
	void periodicalThreadAction() throws IOException, InterruptedException {
		switch (status.getState()) {
		case CONFIGURATION_ERROR:
			// eventuali logiche qui
			internalFireWatchDogs();
			break;
		case CONFIGURED:
			// eventuali logiche qui
			internalFireWatchDogs();
			break;
		case FAULT:
			internalFireWatchDogs();
			// eventuali logiche qui
			break;
		case INIT:
			// eventuali logiche qui
			internalFireWatchDogs();
			break;
		case STARTED:
			internalStartedCheckAndAction();
			internalFireWatchDogs();
			break;
		case STOPPED:
			internalStoppedCheckAndAction();
			internalFireWatchDogs();
			break;
		default:
			fireServerException(new RuntimeException("ssh server status NOT know!"));
			break;
		}

	}

	@Override
	public synchronized void start() {
		cleanThreadIfNeeded();
		thread = new SshServerThread(this);
		thread.start();
		status.setState(BaseSshServerStatus.State.STARTED);
	}

	@Override
	public synchronized void stop() {
		cleanThreadIfNeeded();
		status.setState(BaseSshServerStatus.State.STOPPED);
	}

}
