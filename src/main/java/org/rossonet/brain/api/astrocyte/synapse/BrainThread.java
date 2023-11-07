package org.rossonet.brain.api.astrocyte.synapse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.rossonet.brain.api.UniversallyUniqueObject;

public class BrainThread extends Thread implements UniversallyUniqueObject {

	private final Synapse synapse;

	private final List<BrainThreadLogLine> debugLines = new ArrayList<>();

	private final UUID uuid = UUID.randomUUID();

	public BrainThread(Synapse synapse) {
		this.synapse = synapse;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BrainThread other = (BrainThread) obj;
		return Objects.equals(uuid, other.uuid);
	}

	public List<BrainThreadLogLine> getDebugLines() {
		return debugLines;
	}

	public Synapse getSynapse() {
		return synapse;
	}

	@Override
	public UUID getUniversallyUniqueIdentifier() {
		return uuid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	public void markStep(String message) {
		final BrainThreadLogLine logLine = new BrainThreadLogLine();
		logLine.setMessage(message);
		logLine.setSynapse(synapse.getUniversallyUniqueIdentifier());
		logLine.setInstant(Instant.now());
		logLine.setThreadId(this.getId());
		logLine.setThreadName(this.getName());
		logLine.setThreadPriority(this.getPriority());
		logLine.setThreadStackTrace(this.getStackTrace());
		logLine.setThreadState(this.getState());
	}

	@Override
	public void run() {
		synapse.refuelling(this);
	}

}
