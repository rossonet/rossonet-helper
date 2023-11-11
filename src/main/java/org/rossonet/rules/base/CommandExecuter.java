package org.rossonet.rules.base;

public interface CommandExecuter {

	default void executeAndWait(Command command) {
		if (command.isRunnableOnExecutor(this)) {
			command.run(this);
		}
	}

}
