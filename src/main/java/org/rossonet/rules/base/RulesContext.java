package org.rossonet.rules.base;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.rossonet.ext.rules.api.Fact;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.utils.LogHelper;
import org.rossonet.utils.TextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesContext {

	private static final String DEFAULT_CLASS_LIST_SEPARATOR = ", ";

	private final static Map<String, Class<? extends Command>> commands = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(RulesContext.class);

	public static int registerCommand(final String label, final Class<? extends Command> command) {
		commands.put(label, command);
		return commands.size();
	}

	private final CommandQueue commandQueue;

	private final Facts facts;

	public RulesContext(final CommandQueue commandQueue, final Facts facts) {
		this.facts = facts;
		this.commandQueue = commandQueue;
	}

	public <T extends Object> void addFact(final String factName, final T payload) {
		facts.add(new Fact<T>(factName, payload));
	}

	public void error(final String msg) {
		logger.error(TextHelper.ANSI_RED_BOLD + msg + TextHelper.ANSI_RESET);
	}

	public void exec(final String command, final Object... data) {
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

	public List<Object> getByClass(final String factClass) {
		final List<Object> result = new ArrayList<>();
		for (final Fact<?> f : facts) {
			final String simpleName = f.getValue().getClass().getSimpleName();
			final String name = f.getValue().getClass().getName();
			if (simpleName.equals(factClass) || name.startsWith(factClass)) {
				result.add(f.getValue());
			}
		}
		return result;
	}

	public long getEpochMs() {
		return Instant.now().toEpochMilli();
	}

	public Fact<?> getFact(final String factName) {
		for (final Fact<?> f : facts) {
			if (f.getName().equals(factName)) {
				return f;
			}
		}
		return null;
	}

	public void info(final String msg) {
		logger.info(TextHelper.ANSI_PURPLE_BOLD + msg + TextHelper.ANSI_RESET);
	}

	public boolean isCachedMemoryPresent() {
		if (facts.asMap().containsKey(AbstractBaseRulesEngine.MEM)
				&& facts.asMap().get(AbstractBaseRulesEngine.MEM).getClass().equals(CachedMemory.class)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPresent(final String factName) {
		for (final Fact<?> f : facts) {
			if (f.getName().equals(factName)) {
				return true;
			}
		}
		return false;
	}

	public boolean isPresentByClass(final String factClass) {
		for (final Fact<?> f : facts) {
			final String simpleName = f.getValue().getClass().getSimpleName();
			final String name = f.getValue().getClass().getName();
			if (simpleName.equals(factClass) || name.equals(factClass)) {
				return true;
			}
		}
		return false;
	}

	public String listAllFactsByClass() {
		return listAllFactsByClass(DEFAULT_CLASS_LIST_SEPARATOR);
	}

	public String listAllFactsByClass(final String separator) {
		final StringBuilder result = new StringBuilder();
		boolean first = true;
		for (final Fact<?> f : facts) {
			final String simpleName = f.getValue().getClass().getSimpleName();
			final String name = f.getValue().getClass().getName();
			if (!first) {
				result.append(separator);
			}
			result.append(f.getName() + " - " + simpleName + " [" + name + "]");
			first = false;
		}
		return result.toString();
	}

	public void removeFact(final String factName) {
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

	public boolean test(final boolean condition) {
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

	public void warning(final String msg) {
		logger.warn(TextHelper.ANSI_PURPLE_BOLD + msg + TextHelper.ANSI_RESET);
	}
}
