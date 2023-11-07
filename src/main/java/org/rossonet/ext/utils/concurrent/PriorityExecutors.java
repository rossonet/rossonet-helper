package org.rossonet.ext.utils.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class PriorityExecutors {
	public static PriorityExecutorService newPriorityCachedThreadPool() {
		return new PriorityThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS);
	}

	public static PriorityExecutorService newPriorityCachedThreadPool(ThreadFactory threadFactory) {
		return new PriorityThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, threadFactory);
	}

	public static PriorityExecutorService newPriorityFixedThreadPool(int nThreads) {
		return new PriorityThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS);
	}

	public static PriorityExecutorService newPriorityFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
		return new PriorityThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, threadFactory);
	}

	public static PriorityExecutorService newPrioritySingleThreadPool() {
		return new PriorityThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS);
	}

	public static PriorityExecutorService newPrioritySingleThreadPool(ThreadFactory threadFactory) {
		return new PriorityThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, threadFactory);
	}
}
