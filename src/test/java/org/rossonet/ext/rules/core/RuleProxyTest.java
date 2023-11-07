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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.rossonet.ext.rules.annotation.Action;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithMetaRuleAnnotation;
import org.rossonet.ext.rules.annotation.Condition;
import org.rossonet.ext.rules.annotation.Priority;
import org.rossonet.ext.rules.api.Rule;
import org.rossonet.ext.rules.api.Rules;

public class RuleProxyTest {

	@org.rossonet.ext.rules.annotation.Rule
	static class DummyRule {
		@Action
		public void then() {
		}

		@Override
		public String toString() {
			return "I am a Dummy rule";
		}

		@Condition
		public boolean when() {
			return true;
		}

	}

	@Test
	public void asRuleForObjectThatHasProxied() {
		final Object rule = new DummyRule();
		final Rule proxy1 = RuleProxy.asRule(rule);
		final Rule proxy2 = RuleProxy.asRule(proxy1);

		assertEquals(proxy1.getDescription(), proxy2.getDescription());
		assertEquals(proxy1.getName(), proxy2.getName());
	}

	@Test
	public void asRuleForObjectThatImplementsRule() {
		final Object rule = new BasicRule();
		final Rule proxy = RuleProxy.asRule(rule);

		assertNotNull(proxy.getDescription());
		assertNotNull(proxy.getName());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void asRuleForPojo() {
		final Object rule = new Object();
		final Rule proxy = RuleProxy.asRule(rule);
	}

	@Test
	public void invokeEquals() {

		final Object rule = new DummyRule();
		final Rule proxy1 = RuleProxy.asRule(rule);
		final Rule proxy2 = RuleProxy.asRule(proxy1);
		final Rule proxy3 = RuleProxy.asRule(proxy2);
		// @see Object#equals(Object) reflexive
		assertEquals(rule, rule);
		assertEquals(proxy1, proxy1);
		assertEquals(proxy2, proxy2);
		assertEquals(proxy3, proxy3);
		// @see Object#equals(Object) symmetric
		assertNotEquals(rule, proxy1);
		assertNotEquals(proxy1, rule);
		assertEquals(proxy1, proxy2);
		assertEquals(proxy2, proxy1);
		// @see Object#equals(Object) transitive consistent
		assertEquals(proxy1, proxy2);
		assertEquals(proxy2, proxy3);
		assertEquals(proxy3, proxy1);
		// @see Object#equals(Object) non-null
		assertNotEquals(rule, null);
		assertNotEquals(proxy1, null);
		assertNotEquals(proxy2, null);
		assertNotEquals(proxy3, null);
	}

	@Test
	public void invokeHashCode() {

		final Object rule = new DummyRule();
		final Rule proxy1 = RuleProxy.asRule(rule);
		final Rule proxy2 = RuleProxy.asRule(proxy1);
		// @see Object#hashCode rule1
		assertEquals(proxy1.hashCode(), proxy1.hashCode());
		// @see Object#hashCode rule2
		assertEquals(proxy1, proxy2);
		assertEquals(proxy1.hashCode(), proxy2.hashCode());
		// @see Object#hashCode rule3
		assertNotEquals(rule, proxy1);
		assertNotEquals(rule.hashCode(), proxy1.hashCode());
	}

	@Test
	public void invokeToString() {

		final Object rule = new DummyRule();
		final Rule proxy1 = RuleProxy.asRule(rule);
		final Rule proxy2 = RuleProxy.asRule(proxy1);

		assertEquals(proxy1.toString(), proxy1.toString());

		assertEquals(proxy1.toString(), proxy2.toString());

		assertEquals(rule.toString(), proxy1.toString());
	}

	@Test
	public void proxyingHappensEvenWhenRuleIsAnnotatedWithMetaRuleAnnotation() {
		// Given
		final AnnotatedRuleWithMetaRuleAnnotation rule = new AnnotatedRuleWithMetaRuleAnnotation();

		// When
		final Rule proxy = RuleProxy.asRule(rule);

		// Then
		assertNotNull(proxy.getDescription());
		assertNotNull(proxy.getName());
	}

	@Test
	public void testCompareTo() {

		@org.rossonet.ext.rules.annotation.Rule
		class MyComparableRule implements Comparable<MyComparableRule> {

			int comparisonCriteria;

			MyComparableRule(int comparisonCriteria) {
				this.comparisonCriteria = comparisonCriteria;
			}

			@Override
			public int compareTo(MyComparableRule otherRule) {
				return Integer.compare(comparisonCriteria, otherRule.comparisonCriteria);
			}

			@Action
			public void then() {
			}

			@Condition
			public boolean when() {
				return true;
			}
		}

		final Object rule1 = new MyComparableRule(1);
		final Object rule2 = new MyComparableRule(2);
		final Object rule3 = new MyComparableRule(2);
		final Rule proxy1 = RuleProxy.asRule(rule1);
		final Rule proxy2 = RuleProxy.asRule(rule2);
		final Rule proxy3 = RuleProxy.asRule(rule3);
		assertEquals(proxy1.compareTo(proxy2), -1);
		assertEquals(proxy2.compareTo(proxy1), 1);
		assertEquals(proxy2.compareTo(proxy3), 0);

		try {
			final Rules rules = new Rules();
			rules.register(rule1, rule2);

			final Rules mixedRules = new Rules(rule3);
			mixedRules.register(proxy1, proxy2);

			final Rules yetAnotherRulesSet = new Rules(proxy1, proxy2);
			yetAnotherRulesSet.register(rule3);
		} catch (final Exception exception) {
			fail("Should not fail with " + exception.getMessage());
		}
	}

	@Test // (expected = IllegalArgumentException.class)
	public void testCompareToWithIncorrectSignature() {

		@org.rossonet.ext.rules.annotation.Rule
		class InvalidComparableRule {

			public int compareTo() {
				return 0;
			}

			@Action
			public void then() {
			}

			@Condition
			public boolean when() {
				return true;
			}
		}

		final Object rule = new InvalidComparableRule();
		final Rules rules = new Rules();
		rules.register(rule);
	}

	@Test
	public void testDefaultPriority() {

		@org.rossonet.ext.rules.annotation.Rule
		class MyRule {
			@Action
			public void then() {
			}

			@Condition
			public boolean when() {
				return true;
			}
		}

		final Object rule = new MyRule();
		final Rule proxy = RuleProxy.asRule(rule);
		assertEquals(Rule.DEFAULT_PRIORITY, proxy.getPriority());
	}

	@Test
	public void testPriorityFromAnnotation() {

		@org.rossonet.ext.rules.annotation.Rule(priority = 1)
		class MyRule {
			@Action
			public void then() {
			}

			@Condition
			public boolean when() {
				return true;
			}
		}

		final Object rule = new MyRule();
		final Rule proxy = RuleProxy.asRule(rule);
		assertEquals(1, proxy.getPriority());
	}

	@Test
	public void testPriorityFromMethod() {

		@org.rossonet.ext.rules.annotation.Rule
		class MyRule {
			@Priority
			public int getPriority() {
				return 2;
			}

			@Action
			public void then() {
			}

			@Condition
			public boolean when() {
				return true;
			}
		}

		final Object rule = new MyRule();
		final Rule proxy = RuleProxy.asRule(rule);
		assertEquals(2, proxy.getPriority());
	}

	@Test
	public void testPriorityPrecedence() {

		@org.rossonet.ext.rules.annotation.Rule(priority = 1)
		class MyRule {
			@Priority
			public int getPriority() {
				return 2;
			}

			@Action
			public void then() {
			}

			@Condition
			public boolean when() {
				return true;
			}
		}

		final Object rule = new MyRule();
		final Rule proxy = RuleProxy.asRule(rule);
		assertEquals(2, proxy.getPriority());
	}

}
