package org.rossonet.rules.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rossonet.ext.rules.api.Fact;
import org.rossonet.ext.rules.api.Facts;

public class CachedMemory implements FactProvider, CommandExecuter {

	private final List<Fact<?>> factsMemory = Collections.synchronizedList(new ArrayList<>());

	public void clear() {
		factsMemory.clear();
	}

	@Override
	public Facts getFacts() {
		final Facts reply = new Facts();
		for (final Fact<?> f : factsMemory) {
			reply.add(f);
		}
		reply.add(new Fact<CachedMemory>(AbstractBaseRulesEngine.MEM, this));
		return reply;
	}

	public void remove(String factName) {
		Fact<?> toDelete = null;
		for (final Fact<?> f : factsMemory) {
			if (f.getName().equals(factName)) {
				toDelete = f;
				break;
			}
		}
		if (toDelete != null) {
			factsMemory.remove(toDelete);
		}
	}

	public <F extends Object> void save(String name, F payload) {
		factsMemory.add(new Fact<F>(name, payload));
	}

	@Override
	public String toString() {
		final int maxLen = 20;
		final StringBuilder builder = new StringBuilder();
		builder.append("CachedMemory [");
		if (factsMemory != null) {
			builder.append("factsMemory=");
			builder.append(factsMemory.subList(0, Math.min(factsMemory.size(), maxLen)));
		}
		builder.append("]");
		return builder.toString();
	}

}
