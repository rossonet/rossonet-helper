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
package org.rossonet.ext.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.rossonet.ext.rules.annotation.Action;
import org.rossonet.ext.rules.annotation.Condition;
import org.rossonet.ext.rules.annotation.Fact;
import org.rossonet.ext.rules.annotation.Priority;
import org.rossonet.ext.rules.annotation.Rule;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.ext.rules.api.Rules;
import org.rossonet.ext.rules.api.RulesEngine;
import org.rossonet.ext.rules.api.RulesEngineListener;

public class InferenceRulesEngineTest {

	@Rule
	static class AnotherDummyRule {

		private boolean isExecuted;
		private long timestamp;

		public long getTimestamp() {
			return timestamp;
		}

		public boolean isExecuted() {
			return isExecuted;
		}

		@Priority
		public int priority() {
			return 2;
		}

		@Action
		public void then(Facts facts) {
			isExecuted = true;
			timestamp = System.currentTimeMillis();
			facts.remove("bar");
		}

		@Condition
		public boolean when(@Fact("bar") boolean bar) {
			return bar;
		}
	}

	@Rule
	static class DummyRule {

		private boolean isExecuted;
		private long timestamp;

		public long getTimestamp() {
			return timestamp;
		}

		public boolean isExecuted() {
			return isExecuted;
		}

		@Priority
		public int priority() {
			return 1;
		}

		@Action
		public void then(Facts facts) {
			isExecuted = true;
			timestamp = System.currentTimeMillis();
			facts.remove("foo");
		}

		@Condition
		public boolean when(@Fact("foo") boolean foo) {
			return foo;
		}
	}

	@Test
	public void testCandidateOrdering() {
		// Given
		final Facts facts = new Facts();
		facts.put("foo", true);
		facts.put("bar", true);
		final DummyRule dummyRule = new DummyRule();
		final AnotherDummyRule anotherDummyRule = new AnotherDummyRule();
		final Rules rules = new Rules(dummyRule, anotherDummyRule);
		final RulesEngine rulesEngine = new InferenceRulesEngine();

		// When
		rulesEngine.fire(rules, facts);

		// Then
		assertThat(dummyRule.isExecuted()).isTrue();
		assertThat(anotherDummyRule.isExecuted()).isTrue();
		assertThat(dummyRule.getTimestamp()).isLessThanOrEqualTo(anotherDummyRule.getTimestamp());
	}

	@Test
	public void testCandidateSelection() {
		// Given
		final Facts facts = new Facts();
		facts.put("foo", true);
		final DummyRule dummyRule = new DummyRule();
		final AnotherDummyRule anotherDummyRule = new AnotherDummyRule();
		final Rules rules = new Rules(dummyRule, anotherDummyRule);
		final RulesEngine rulesEngine = new InferenceRulesEngine();

		// When
		rulesEngine.fire(rules, facts);

		// Then
		assertThat(dummyRule.isExecuted()).isTrue();
		assertThat(anotherDummyRule.isExecuted()).isFalse();
	}

	@Test
	public void testRulesEngineListener() {
		// Given
		class StubRulesEngineListener implements RulesEngineListener {

			private boolean executedBeforeEvaluate;
			private boolean executedAfterExecute;

			@Override
			public void afterExecute(Rules rules, Facts facts) {
				executedAfterExecute = true;
			}

			@Override
			public void beforeEvaluate(Rules rules, Facts facts) {
				executedBeforeEvaluate = true;
			}

			private boolean isExecutedAfterExecute() {
				return executedAfterExecute;
			}

			private boolean isExecutedBeforeEvaluate() {
				return executedBeforeEvaluate;
			}
		}

		final Facts facts = new Facts();
		facts.put("foo", true);
		final DummyRule rule = new DummyRule();
		final Rules rules = new Rules(rule);
		final StubRulesEngineListener rulesEngineListener = new StubRulesEngineListener();

		// When
		final InferenceRulesEngine rulesEngine = new InferenceRulesEngine();
		rulesEngine.registerRulesEngineListener(rulesEngineListener);
		rulesEngine.fire(rules, facts);

		// Then
		// Rules engine listener should be invoked
		assertThat(rulesEngineListener.isExecutedBeforeEvaluate()).isTrue();
		assertThat(rulesEngineListener.isExecutedAfterExecute()).isTrue();
		assertThat(rule.isExecuted()).isTrue();
	}

	@Test // (expected = NullPointerException.class)
	public void whenCheckRules_thenNullFactsShouldNotBeAccepted() {
		final InferenceRulesEngine engine = new InferenceRulesEngine();
		engine.check(new Rules(), null);
	}

	@Test // (expected = NullPointerException.class)
	public void whenCheckRules_thenNullRulesShouldNotBeAccepted() {
		final InferenceRulesEngine engine = new InferenceRulesEngine();
		engine.check(null, new Facts());
	}

	@Test // (expected = NullPointerException.class)
	public void whenFireRules_thenNullFactsShouldNotBeAccepted() {
		final InferenceRulesEngine engine = new InferenceRulesEngine();
		engine.fire(new Rules(), null);
	}

	@Test // (expected = NullPointerException.class)
	public void whenFireRules_thenNullRulesShouldNotBeAccepted() {
		final InferenceRulesEngine engine = new InferenceRulesEngine();
		engine.fire(null, new Facts());
	}

}
