/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.rossonet.ext.states.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rossonet.ext.states.api.AbstractEvent;
import org.rossonet.ext.states.api.State;
import org.rossonet.ext.states.api.Transition;
import org.rossonet.ext.states.core.TransitionBuilder;

public class TransitionImplTest {

	private static class AnotherDummyEvent extends AbstractEvent {
	}

	private static class DummyEvent extends AbstractEvent {
	}

	private final State s1 = new State("s1");

	private final State s2 = new State("s2");

	@Test
	public void whenTwoTransitionsHaveDifferentTriggeringEventsAndDifferentSourceStates_thenTheyShouldNotBeEqual() {
		// Given
		final Transition t1 = new TransitionBuilder().sourceState(s1).eventType(DummyEvent.class).build();
		final Transition t2 = new TransitionBuilder().sourceState(s2).eventType(AnotherDummyEvent.class).build();

		// When
		final boolean equals = t1.equals(t2);

		// Then
		Assertions.assertThat(equals).isFalse();
	}

	@Test
	public void whenTwoTransitionsHaveTheSameSourceStateAndTheSameTriggeringEvent_thenTheyShouldBeEqual() {
		// Given
		final Transition t1 = new TransitionBuilder().sourceState(s1).eventType(DummyEvent.class).build();
		final Transition t2 = new TransitionBuilder().sourceState(s1).eventType(DummyEvent.class).build();

		// When
		final boolean equals = t1.equals(t2);

		// Then
		Assertions.assertThat(equals).isTrue();
	}

	@Test
	public void whenTwoTransitionsHaveTheSameSourceStateButDifferentTriggeringEvent_thenTheyShouldNotBeEqual() {
		// Given
		final Transition t1 = new TransitionBuilder().sourceState(s1).eventType(DummyEvent.class).build();
		final Transition t2 = new TransitionBuilder().sourceState(s1).eventType(AnotherDummyEvent.class).build();

		// When
		final boolean equals = t1.equals(t2);

		// Then
		Assertions.assertThat(equals).isFalse();
	}

	@Test
	public void whenTwoTransitionsHaveTheSameTriggeringEventButDifferentSourceState_thenTheyShouldNotBeEqual() {
		// Given
		final Transition t1 = new TransitionBuilder().sourceState(s1).eventType(DummyEvent.class).build();
		final Transition t2 = new TransitionBuilder().sourceState(s2).eventType(DummyEvent.class).build();

		// When
		final boolean equals = t1.equals(t2);

		// Then
		Assertions.assertThat(equals).isFalse();
	}

}
