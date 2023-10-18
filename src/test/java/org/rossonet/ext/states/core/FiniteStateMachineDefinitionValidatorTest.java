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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.rossonet.ext.states.api.AbstractEvent;
import org.rossonet.ext.states.api.FiniteStateMachine;
import org.rossonet.ext.states.api.State;
import org.rossonet.ext.states.api.Transition;
import org.rossonet.ext.states.core.FiniteStateMachineBuilder;
import org.rossonet.ext.states.core.FiniteStateMachineDefinitionValidator;
import org.rossonet.ext.states.core.FiniteStateMachineImpl;
import org.rossonet.ext.states.core.TransitionBuilder;

public class FiniteStateMachineDefinitionValidatorTest {

	private static class DummyEvent extends AbstractEvent {
	}

	private final FiniteStateMachineDefinitionValidator validator = new FiniteStateMachineDefinitionValidator();

	@Test // (expected = IllegalStateException.class)
	public void whenFinalStateDoesNotBelongToMachineStates_thenShouldThrowIllegalStateException() {
		// given
		final State s1 = new State("s1");
		final State s2 = new State("s2");
		final State s3 = new State("s3");
		final Set<State> states = new HashSet<>(Arrays.asList(s1, s2));
		final FiniteStateMachine finiteStateMachine = new FiniteStateMachineBuilder(states, s1).registerFinalState(s3)
				.build();

		// when
		validator.validateFiniteStateMachineDefinition(finiteStateMachine);

		// then
		// expected exception
	}

	@Test // (expected = IllegalStateException.class)
	public void whenInitialStateDoesNotBelongToMachineStates_thenShouldThrowIllegalStateException() {
		// given
		final State s1 = new State("s1");
		final State s2 = new State("s2");
		final State s3 = new State("s3");
		final Set<State> states = new HashSet<>(Arrays.asList(s1, s2));
		final FiniteStateMachine finiteStateMachine = new FiniteStateMachineImpl(states, s3);

		// when
		validator.validateFiniteStateMachineDefinition(finiteStateMachine);

		// then
		// expected exception
	}

	@Test
	public void whenRegisterTwoTransitionsWithSameSourceStateAndEventType_thenOnlyTheLatestOneShouldBeRegistered() {
		// given
		final State s1 = new State("s1");
		final State s2 = new State("s2");
		final State s3 = new State("s3");
		final Set<State> states = new HashSet<>(Arrays.asList(s1, s2, s3));

		final Transition t1 = new TransitionBuilder().sourceState(s1).targetState(s2).eventType(DummyEvent.class)
				.build();
		final Transition t2 = new TransitionBuilder().sourceState(s1).targetState(s3).eventType(DummyEvent.class)
				.build();

		// when
		final FiniteStateMachine finiteStateMachine = new FiniteStateMachineBuilder(states, s1).registerFinalState(s2)
				.registerTransition(t1).registerTransition(t2).build();

		// then
		assertThat(finiteStateMachine.getTransitions()).containsOnly(t2); // transitions are unique according to source
																			// state and event type
	}

}
