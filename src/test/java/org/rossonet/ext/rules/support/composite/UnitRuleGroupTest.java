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
package org.rossonet.ext.rules.support.composite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.rossonet.ext.rules.annotation.Action;
import org.rossonet.ext.rules.annotation.Condition;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.ext.rules.api.Rule;
import org.rossonet.ext.rules.api.Rules;
import org.rossonet.ext.rules.core.DefaultRulesEngine;
import org.rossonet.ext.rules.support.composite.UnitRuleGroup;

public class UnitRuleGroupTest {

	@org.rossonet.ext.rules.annotation.Rule
	public static class MyAnnotatedRule {
		private boolean executed;

		@Condition
		public boolean evaluate() {
			return true;
		}

		@Action
		public void execute() {
			executed = true;
		}

		public boolean isExecuted() {
			return executed;
		}
	}

	@org.rossonet.ext.rules.annotation.Rule
	public static class MyRule {
		boolean executed;

		public boolean isExecuted() {
			return executed;
		}

		@Action
		public void then() {
			executed = true;
		}

		@Condition
		public boolean when() {
			return true;
		}
	}

	@Mock
	private Rule rule1, rule2;

	private final Facts facts = new Facts();

	private final Rules rules = new Rules();

	private final DefaultRulesEngine rulesEngine = new DefaultRulesEngine();

	private UnitRuleGroup unitRuleGroup;

	@Test
	public void compositeRuleAndComposingRulesMustBeExecuted() throws Exception {
		// Given
		unitRuleGroup = new UnitRuleGroup();
		unitRuleGroup.addRule(rule1);
		unitRuleGroup.addRule(rule2);
		rules.register(unitRuleGroup);

		// When
		rulesEngine.fire(rules, facts);

		// Then
		verify(rule1).execute(facts);
		verify(rule2).execute(facts);
	}

	@Test
    public void compositeRuleMustNotBeExecutedIfAComposingRuleEvaluatesToFalse() throws Exception {
        // Given
        when(rule2.evaluate(facts)).thenReturn(false);
        unitRuleGroup = new UnitRuleGroup();
        unitRuleGroup.addRule(rule1);
        unitRuleGroup.addRule(rule2);
        rules.register(unitRuleGroup);

        // When
        rulesEngine.fire(rules, facts);

        // Then
        /*
         * The composing rules should not be executed
         * since not all rules conditions evaluate to TRUE
         */

        //Rule 1 should not be executed
        verify(rule1, never()).execute(facts);
        //Rule 2 should not be executed
        verify(rule2, never()).execute(facts);
    }

	@BeforeEach
    public void setUp() {
        when(rule1.evaluate(facts)).thenReturn(true);
        when(rule2.evaluate(facts)).thenReturn(true);
        when(rule2.compareTo(rule1)).thenReturn(1);
    }

	@Test
	public void testCompositeRuleWithAnnotatedComposingRules() {
		// Given
		final MyRule rule = new MyRule();
		unitRuleGroup = new UnitRuleGroup();
		unitRuleGroup.addRule(rule);
		rules.register(unitRuleGroup);

		// When
		rulesEngine.fire(rules, facts);

		// Then
		assertThat(rule.isExecuted()).isTrue();
	}

	@Test
	public void whenAnnotatedRuleIsRemoved_thenItsProxyShouldBeRetrieved() {
		// Given
		final MyRule rule = new MyRule();
		final MyAnnotatedRule annotatedRule = new MyAnnotatedRule();
		unitRuleGroup = new UnitRuleGroup();
		unitRuleGroup.addRule(rule);
		unitRuleGroup.addRule(annotatedRule);
		unitRuleGroup.removeRule(annotatedRule);
		rules.register(unitRuleGroup);

		// When
		rulesEngine.fire(rules, facts);

		// Then
		assertThat(rule.isExecuted()).isTrue();
		assertThat(annotatedRule.isExecuted()).isFalse();
	}

	@Test
	public void whenARuleIsRemoved_thenItShouldNotBeEvaluated() throws Exception {
		// Given
		unitRuleGroup = new UnitRuleGroup();
		unitRuleGroup.addRule(rule1);
		unitRuleGroup.addRule(rule2);
		unitRuleGroup.removeRule(rule2);
		rules.register(unitRuleGroup);

		// When
		rulesEngine.fire(rules, facts);

		// Then
		// Rule 1 should be executed
		verify(rule1).execute(facts);

		// Rule 2 should not be evaluated nor executed
		verify(rule2, never()).evaluate(facts);
		verify(rule2, never()).execute(facts);
	}

	@Test
	public void whenNoComposingRulesAreRegistered_thenUnitRuleGroupShouldEvaluateToFalse() {
		// given
		unitRuleGroup = new UnitRuleGroup();

		// when
		final boolean evaluationResult = unitRuleGroup.evaluate(facts);

		// then
		assertThat(evaluationResult).isFalse();
	}
}
