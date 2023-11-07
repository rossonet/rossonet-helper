package org.rossonet.utils.concurrent;

import org.junit.jupiter.api.Test;
import org.rossonet.ext.utils.concurrent.PriorityExecutorService;
import org.rossonet.ext.utils.concurrent.PriorityExecutors;

public class PriorityExecutorTests {

	private static class TestThread implements Runnable {
		int priority;

		TestThread(int priority) {
			this.priority = priority;
		}

		@Override
		public void run() {
			System.out.println("Thread Id: " + Thread.currentThread().getId() + "| Original Task Priority: " + priority
					+ "| Current Task priority: " + Thread.currentThread().getPriority());
			try {
				Thread.sleep(2000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testExecutor() throws InterruptedException {
		final PriorityExecutorService s = PriorityExecutors.newPriorityFixedThreadPool(2);
		for (int i = 0; i < 10; i++) {
			s.submit(new TestThread(3), 3);
		}
		for (int i = 0; i < 10; i++) {
			s.submit(new TestThread(5), 5);
		}
		for (int i = 0; i < 10; i++) {
			s.submit(new TestThread(8), 8);
		}
		s.changePriorities(5, 10);
		Thread.sleep(60000);
	}
}
