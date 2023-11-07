package org.rossonet.ext.utils.concurrent;

import java.util.Comparator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor implements PriorityExecutorService {
	@SuppressWarnings("rawtypes")
	private static class PriorityFutureTaskComparator<T extends PriorityFuture> implements Comparator<T> {
		@Override
		public int compare(T t1, T t2) {
			return t2.getPriority() - t1.getPriority();
		}
	}

	private static final RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();

	private final BlockingDeque<Runnable> workQueue;

	public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), defaultHandler);
	}

	public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			RejectedExecutionHandler handler) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), handler);
	}

	public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			ThreadFactory threadFactory) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, defaultHandler);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				new PriorityBlockingDeque<Runnable>(corePoolSize, new PriorityFutureTaskComparator()), threadFactory,
				handler);
		this.workQueue = (BlockingDeque<Runnable>) super.getQueue();
	}

	@Override
	public void changePriorities(int fromPriority, int toPriority) {
		if (fromPriority < Thread.MIN_PRIORITY || fromPriority > Thread.MAX_PRIORITY || toPriority < Thread.MIN_PRIORITY
				|| toPriority > Thread.MAX_PRIORITY || fromPriority == toPriority) {
			throw new IllegalArgumentException("Invalid from/to priority values");
		}

		final PriorityFuture<?>[] tasks = this.workQueue.toArray(new PriorityFuture<?>[0]);

		for (final PriorityFuture<?> task : tasks) {
			if (task.getPriority() == fromPriority) {
				if (this.workQueue.remove(task)) {
					task.setPriority(toPriority);
					this.workQueue.offer(task);
				}
			}
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public int getHighestPriority() {
		final PriorityFuture task = ((PriorityFuture) this.workQueue.peekFirst());
		return task != null ? task.getPriority() : Integer.MAX_VALUE;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public int getLeastPriority() {
		final PriorityFuture task = ((PriorityFuture) this.workQueue.peekLast());
		return task != null ? task.getPriority() : Integer.MIN_VALUE;
	}

	protected <T> RunnableFuture<T> newPriorityTaskFor(Callable<T> callable, int priority) {
		return new PriorityFuture<T>(callable, priority);
	}

	protected <T> RunnableFuture<T> newPriorityTaskFor(Runnable runnable, T value, int priority) {
		return new PriorityFuture<T>(runnable, value, priority);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return this.submit(task, Thread.NORM_PRIORITY);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task, int priority) {
		if (task == null) {
			throw new NullPointerException();
		}
		final RunnableFuture<T> ftask = newPriorityTaskFor(task, priority);
		execute(ftask);
		return ftask;
	}

	@Override
	public Future<?> submit(Runnable task) {
		return this.submit(task, Thread.NORM_PRIORITY);
	}

	@Override
	public Future<?> submit(Runnable task, int priority) {
		if (task == null) {
			throw new NullPointerException();
		}
		final RunnableFuture<Object> ftask = newPriorityTaskFor(task, null, priority);
		execute(ftask);
		return ftask;
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return this.submit(task, result, Thread.NORM_PRIORITY);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result, int priority) {
		if (task == null) {
			throw new NullPointerException();
		}
		final RunnableFuture<T> ftask = newPriorityTaskFor(task, result, priority);
		execute(ftask);
		return ftask;
	}
}