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
package org.rossonet.ext.states.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rossonet.ext.states.api.State;

public class StateTest {

	@Test
	public void whenTwoStatesHaveDifferentNames_thenTheyShouldNotBeEqual() {
		// Given
		final State s1 = new State("s1");
		final State s2 = new State("s2");

		// When
		final boolean equals = s1.equals(s2);

		// Then
		Assertions.assertThat(equals).isFalse();
	}

	@Test
	public void whenTwoStatesHaveTheSameName_thenTheyShouldBeEqual() {
		// Given
		final State s1 = new State("s1");
		final State s2 = new State("s1");

		// When
		final boolean equals = s1.equals(s2);

		// Then
		Assertions.assertThat(equals).isTrue();
	}
}
