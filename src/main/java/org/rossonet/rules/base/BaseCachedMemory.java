package org.rossonet.rules.base;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rossonet.ext.rules.api.Fact;
import org.rossonet.ext.rules.api.Facts;

public class BaseCachedMemory implements CachedMemory {

	public class TimerCachedMemory {

		private final String name;
		private final long validDelayMs;
		private final long validUntilMs;

		private final Fact<?> fact;

		private final long createdAt;

		public TimerCachedMemory(final String name, final long validDelayMs, final long validUntilMs,
				final Fact<?> fact) {
			this.name = name;
			this.validDelayMs = validDelayMs;
			this.validUntilMs = validUntilMs;
			this.fact = fact;
			this.createdAt = Instant.now().toEpochMilli();
		}

		public long getCreatedAt() {
			return createdAt;
		}

		public Fact<?> getFact() {
			return fact;
		}

		public String getName() {
			return name;
		}

		public long getValidDelayMs() {
			return validDelayMs;
		}

		public long getValidUntilMs() {
			return validUntilMs;
		}

		public boolean isExpired() {
			if (validUntilMs != -1 && createdAt + validUntilMs < Instant.now().toEpochMilli()) {
				return true;
			} else {
				return false;
			}
		}

		public boolean isValidNow() {
			if (validDelayMs != -1 && createdAt + validDelayMs > Instant.now().toEpochMilli()) {
				return false;
			}
			if (isExpired()) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("TimerCachedMemory [");
			if (name != null) {
				builder.append("name=");
				builder.append(name);
				builder.append(", ");
			}
			builder.append("createdAt=");
			builder.append(createdAt);
			builder.append(", validDelayMs=");
			builder.append(validDelayMs);
			builder.append(", validUntilMs=");
			builder.append(validUntilMs);
			builder.append(", ");
			if (fact != null) {
				builder.append("fact=");
				builder.append(fact);
			}
			builder.append("]");
			return builder.toString();
		}

	}

	private final List<TimerCachedMemory> factsMemory = Collections.synchronizedList(new ArrayList<>());

	public void clear() {
		factsMemory.clear();
	}

	@Override
	public void close() throws Exception {
		factsMemory.clear();

	}

	@Override
	public Facts getFacts() {
		final Facts reply = new Facts();
		final List<TimerCachedMemory> toDelete = new ArrayList<>();
		for (final TimerCachedMemory memoryFact : factsMemory) {
			if (memoryFact.isValidNow()) {
				reply.add(memoryFact.getFact());
			}
			if (memoryFact.isExpired()) {
				toDelete.add(memoryFact);
			}
		}
		reply.add(new Fact<BaseCachedMemory>(AbstractBaseRulesEngine.MEM, this));
		for (final TimerCachedMemory expiredFact : toDelete) {
			factsMemory.remove(expiredFact);
		}
		return reply;
	}

	public void remove(final String factName) {
		TimerCachedMemory toDelete = null;
		for (final TimerCachedMemory f : factsMemory) {
			if (f.getName().equals(factName)) {
				toDelete = f;
				break;
			}
		}
		if (toDelete != null) {
			factsMemory.remove(toDelete);
		}
	}

	public <F extends Object> void save(final String name, final F payload) {
		save(name, payload, -1, -1);
	}

	public <F extends Object> void save(final String name, final F payload, final long validDelayMs,
			final long validUntilMs) {
		factsMemory.add(new TimerCachedMemory(name, validDelayMs, validUntilMs, new Fact<F>(name, payload)));
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
