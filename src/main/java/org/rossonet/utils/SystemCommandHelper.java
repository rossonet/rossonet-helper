package org.rossonet.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class SystemCommandHelper {

	public static class StreamGobbler implements Runnable {
		private final InputStream inputStream;
		private final Consumer<String> consumer;
		private final InputStream errorStream;

		public StreamGobbler(InputStream inputStream, InputStream errorStream, Consumer<String> consumer) {
			this.inputStream = inputStream;
			this.errorStream = errorStream;
			this.consumer = consumer;
		}

		@Override
		public void run() {
			new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
			new BufferedReader(new InputStreamReader(errorStream)).lines().forEach(consumer);
		}
	}

	public static void executeSystemCommandAndWait(File baseDirectory, String[] command, Consumer<String> consumer,
			int timeoutMilliSeconds) throws IOException, InterruptedException, ExecutionException, TimeoutException {
		final ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.directory(baseDirectory);
		final Process process = builder.start();
		final StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), process.getErrorStream(),
				consumer);
		final Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
		if (timeoutMilliSeconds != 0) {
			future.get(timeoutMilliSeconds, TimeUnit.MILLISECONDS);
		} else {
			future.get();
		}
	}

	private SystemCommandHelper() {
		throw new UnsupportedOperationException("Just for static usage");
	}

}
