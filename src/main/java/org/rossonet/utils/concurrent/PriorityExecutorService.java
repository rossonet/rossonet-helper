package org.rossonet.utils.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface PriorityExecutorService extends ExecutorService {

	public <T> void changePriorities(int fromPriority, int toPriority);

	public int getHighestPriority();

	public int getLeastPriority();

	public <T> Future<T> submit(Callable<T> task, int priority);

	public Future<?> submit(Runnable task, int priority);

	public <T> Future<T> submit(Runnable task, T result, int priority);
}
