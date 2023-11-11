package org.rossonet.rules.base;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.rossonet.ext.rules.api.Fact;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.utils.LogHelper;
import org.rossonet.utils.TextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesContext {

	private final static Map<String, Class<? extends Command>> commands = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(RulesContext.class);

	public static int registerCommand(String label, Class<? extends Command> command) {
		commands.put(label, command);
		return commands.size();
	}

	private final CommandQueue commandQueue;

	private final Facts facts;

	public RulesContext(CommandQueue commandQueue, Facts facts) {
		this.facts = facts;
		this.commandQueue = commandQueue;
	}

	public <T extends Object> void addFact(String factName, T payload) {
		facts.add(new Fact<T>(factName, payload));
	}

	public void error(String msg) {
		logger.error(TextHelper.ANSI_RED_BOLD + msg + TextHelper.ANSI_RESET);
	}

	public void exec(String command, Object... data) {
		if (commands.containsKey(command)) {
			try {
				final Command commandObject = (Command) commands.get(command).getConstructors()[0].newInstance();
				if (data != null) {
					commandObject.setData(data);
				}
				commandQueue.offer(commandObject);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException e) {
				logger.error(TextHelper.ANSI_RED + "command " + command + " got error " + TextHelper.ANSI_RESET
						+ LogHelper.stackTraceToString(e, 5));
			}
		} else {
			logger.error(command + " not found [" + commands + "]");
		}
	}

	public void info(String msg) {
		logger.info(TextHelper.ANSI_PURPLE_BOLD + msg + TextHelper.ANSI_RESET);
	}

	public boolean isPresent(String factName) {
		for (final Fact<?> f : facts) {
			if (f.getName().equals(factName)) {
				return true;
			}
		}
		return false;
	}

	public void removeFact(String factName) {
		Fact<?> toDelete = null;
		for (final Fact<?> f : facts) {
			if (f.getName().equals(factName)) {
				toDelete = f;
				break;
			}
		}
		if (toDelete != null) {
			facts.remove(toDelete);
		}
	}

	public boolean test(boolean condition) {
		logger.info("called test condition " + condition);
		return condition;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("RulesContext [");
		if (commandQueue != null) {
			builder.append(commandQueue);
		}
		builder.append("]");
		return builder.toString();
	}

	public UUID uuid() {
		return UUID.randomUUID();
	}

	public void warning(String msg) {
		logger.warn(TextHelper.ANSI_PURPLE_BOLD + msg + TextHelper.ANSI_RESET);
	}
}
