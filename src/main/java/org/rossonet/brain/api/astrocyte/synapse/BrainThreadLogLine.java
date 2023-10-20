package org.rossonet.brain.api.astrocyte.synapse;

import java.lang.Thread.State;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

public class BrainThreadLogLine {

	private String state;
	private StackTraceElement[] stackTrace;
	private int threadPriority;
	private String threadName;
	private long threadId;
	private int nanos;
	private long time;
	private String synapseId;
	private String message;

	public String getMessage() {
		return message;
	}

	public int getNanos() {
		return nanos;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public String getState() {
		return state;
	}

	public String getSynapseId() {
		return synapseId;
	}

	public long getThreadId() {
		return threadId;
	}

	public String getThreadName() {
		return threadName;
	}

	public int getThreadPriority() {
		return threadPriority;
	}

	public long getTime() {
		return time;
	}

	public void setInstant(Instant time) {
		this.time = time.getEpochSecond();
		this.nanos = time.getNano();

	}

	public void setMessage(String message) {
		this.message = message;

	}

	public void setNanos(int nanos) {
		this.nanos = nanos;
	}

	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setSynapse(UUID universallyUniqueIdentifier) {
		this.synapseId = universallyUniqueIdentifier.toString();

	}

	public void setSynapseId(String synapseId) {
		this.synapseId = synapseId;
	}

	public void setThreadId(long id) {
		this.threadId = id;

	}

	public void setThreadName(String name) {
		this.threadName = name;

	}

	public void setThreadPriority(int priority) {
		this.threadPriority = priority;

	}

	public void setThreadStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;

	}

	public void setThreadState(State state) {
		this.state = state.name();

	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BrainThreadLogLine [");
		if (message != null) {
			builder.append("message=");
			builder.append(message);
			builder.append(", ");
		}
		if (state != null) {
			builder.append("state=");
			builder.append(state);
			builder.append(", ");
		}
		if (threadName != null) {
			builder.append("threadName=");
			builder.append(threadName);
			builder.append(", ");
		}
		builder.append("threadId=");
		builder.append(threadId);
		builder.append(", threadPriority=");
		builder.append(threadPriority);
		builder.append(", time=");
		builder.append(time);
		builder.append(", nanos=");
		builder.append(nanos);
		builder.append(", ");
		if (synapseId != null) {
			builder.append("synapseId=");
			builder.append(synapseId);
			builder.append(", ");
		}
		if (stackTrace != null) {
			builder.append("stackTrace=");
			builder.append(Arrays.toString(stackTrace));
		}
		builder.append("]");
		return builder.toString();
	}

}
