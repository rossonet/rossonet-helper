package org.rossonet.ext.utils.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PriorityFuture<T> extends FutureTask<T> {
	private int priority;

	public PriorityFuture(Callable<T> callable, int priority) {
		super(callable);
		this.validatePriority(priority);
		this.priority = priority;
	}

	public PriorityFuture(Runnable runnable, T result, int priority) {
		super(runnable, result);
		this.validatePriority(priority);
		this.priority = priority;
	}

	public int getPriority() {
		return this.priority;
	}

	@Override
	public void run() {
		final int originalPriority = Thread.currentThread().getPriority();
		Thread.currentThread().setPriority(priority);
		super.run();
		Thread.currentThread().setPriority(originalPriority);
	}

	public void setPriority(int priority) {
		this.validatePriority(priority);
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Priority: " + this.priority;
	}

	private void validatePriority(int priority) {
		if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
			throw new IllegalArgumentException("Priority must be between Thread.MIN_PRIORITY and Thread.MAX_PRIORITY");
		}
	}
}