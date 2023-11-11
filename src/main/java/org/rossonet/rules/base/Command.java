package org.rossonet.rules.base;

public interface Command {

	boolean isRunnableOnExecutor(CommandExecuter commandExecuter);

	void run(CommandExecuter commandExecuter);

	void setData(Object... data);

}
