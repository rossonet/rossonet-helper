package org.rossonet.rules.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandQueue {

	private final Queue<Command> commands = new ConcurrentLinkedQueue<>();

	public Collection<Command> asCollection() {
		final Collection<Command> list = new ArrayList<>();
		while (!commands.isEmpty()) {
			list.add(commands.poll());
		}
		return list;
	}

	public void clear() {
		commands.clear();
	}

	public void offer(Command commandObject) {
		commands.offer(commandObject);
	}

	public Command poll() {
		return commands.poll();
	}

	public int size() {
		return commands.size();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CommandQueue [");
		if (commands != null) {
			builder.append("size=");
			builder.append(commands.size());
		}
		builder.append("]");
		return builder.toString();
	}

}
