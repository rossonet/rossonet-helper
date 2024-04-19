package org.rossonet.sshd.internal;

import java.nio.file.Path;

public class SshServerConfiguration {

	private String baseDirectory;
	private String protectedDirectory;
	private String hostKeyFile;

	public String getChildThreadPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getChildThreadPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Path getServerBasePath() {
		return Path.of(baseDirectory);
	}

	public Path getServerHostKeyPath() {
		return Path.of(baseDirectory, protectedDirectory, hostKeyFile);
	}

	public String getSshHost() {
		// TODO Auto-generated method stub
		return "0.0.0.0";
	}

	public int getSshPort() {
		// TODO Auto-generated method stub
		return 6222;
	}

	public String getSshServerThreadName() {
		// TODO Auto-generated method stub
		return "ssh-server";
	}

	public int getSshServerThreadPriority() {
		// TODO Auto-generated method stub
		return Thread.NORM_PRIORITY;
	}

	public long getSshServerThreadSleepingTimeMs() {
		// TODO Auto-generated method stub
		return 10;
	}

	public long getStopWaitTimeMs() {
		// TODO Auto-generated method stub
		return 100;
	}

	public int getThreadPoolSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isValid() {
		// TODO validare la configurazione
		return true;
	}

}
