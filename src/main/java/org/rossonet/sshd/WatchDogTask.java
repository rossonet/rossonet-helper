package org.rossonet.sshd;

public interface WatchDogTask {

	void checkSshServerState(BaseSshServer sshServer);

}
