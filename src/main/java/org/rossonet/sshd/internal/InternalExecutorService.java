package org.rossonet.sshd.internal;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.rossonet.sshd.MinaSshServer;

public class InternalExecutorService implements ScheduledExecutorService {

	private final ScheduledExecutorService wrappedScheduledExecutorService;

	public InternalExecutorService(final MinaSshServer sshServer) {
		wrappedScheduledExecutorService = Executors.newScheduledThreadPool(
				sshServer.getConfiguration().getThreadPoolSize(), new InternalThreadPoolFactory(sshServer));
	}

	@Override
	public boolean awaitTermination(final long arg0, final TimeUnit arg1) throws InterruptedException {
		return wrappedScheduledExecutorService.awaitTermination(arg0, arg1);
	}

	@Override
	public void execute(final Runnable command) {
		wrappedScheduledExecutorService.execute(command);
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return wrappedScheduledExecutorService.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> arg0, final long arg1,
			final TimeUnit arg2) throws InterruptedException {
		return wrappedScheduledExecutorService.invokeAll(arg0, arg1, arg2);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return wrappedScheduledExecutorService.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> arg0, final long arg1, final TimeUnit arg2)
			throws InterruptedException, ExecutionException, TimeoutException {
		return wrappedScheduledExecutorService.invokeAny(arg0, arg1, arg2);
	}

	@Override
	public boolean isShutdown() {
		return wrappedScheduledExecutorService.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return wrappedScheduledExecutorService.isTerminated();
	}

	@Override
	public <V> ScheduledFuture<V> schedule(final Callable<V> arg0, final long arg1, final TimeUnit arg2) {
		return wrappedScheduledExecutorService.schedule(arg0, arg1, arg2);
	}

	@Override
	public ScheduledFuture<?> schedule(final Runnable arg0, final long arg1, final TimeUnit arg2) {
		return wrappedScheduledExecutorService.schedule(arg0, arg1, arg2);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(final Runnable arg0, final long arg1, final long arg2,
			final TimeUnit arg3) {
		return wrappedScheduledExecutorService.scheduleAtFixedRate(arg0, arg1, arg2, arg3);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable arg0, final long arg1, final long arg2,
			final TimeUnit arg3) {
		return wrappedScheduledExecutorService.scheduleWithFixedDelay(arg0, arg1, arg2, arg3);
	}

	@Override
	public void shutdown() {
		wrappedScheduledExecutorService.shutdown();

	}

	@Override
	public List<Runnable> shutdownNow() {
		return wrappedScheduledExecutorService.shutdownNow();
	}

	@Override
	public <T> Future<T> submit(final Callable<T> task) {
		return wrappedScheduledExecutorService.submit(task);
	}

	@Override
	public Future<?> submit(final Runnable task) {
		return wrappedScheduledExecutorService.submit(task);
	}

	@Override
	public <T> Future<T> submit(final Runnable arg0, final T arg1) {
		return wrappedScheduledExecutorService.submit(arg0, arg1);
	}

}
