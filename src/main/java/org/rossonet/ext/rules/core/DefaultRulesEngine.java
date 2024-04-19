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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.rossonet.ext.rules.api.Fact;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.ext.rules.api.Rule;
import org.rossonet.ext.rules.api.Rules;
import org.rossonet.ext.rules.api.RulesEngine;
import org.rossonet.ext.rules.api.RulesEngineParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link RulesEngine} implementation.
 *
 * Rules are fired according to their natural order which is priority by
 * default. This implementation iterates over the sorted set of rules, evaluates
 * the condition of each rule and executes its actions if the condition
 * evaluates to true.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public final class DefaultRulesEngine extends AbstractRulesEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRulesEngine.class);

	/**
	 * Create a new {@link DefaultRulesEngine} with default parameters.
	 */
	public DefaultRulesEngine() {
		super();
	}

	/**
	 * Create a new {@link DefaultRulesEngine}.
	 *
	 * @param parameters of the engine
	 */
	public DefaultRulesEngine(final RulesEngineParameters parameters) {
		super(parameters);
	}

	@Override
	public Map<Rule, Boolean> check(final Rules rules, final Facts facts) {
		Objects.requireNonNull(rules, "Rules must not be null");
		Objects.requireNonNull(facts, "Facts must not be null");
		triggerListenersBeforeRules(rules, facts);
		final Map<Rule, Boolean> result = doCheck(rules, facts);
		triggerListenersAfterRules(rules, facts);
		return result;
	}

	private Map<Rule, Boolean> doCheck(final Rules rules, final Facts facts) {
		LOGGER.debug("Checking rules");
		final Map<Rule, Boolean> result = new HashMap<>();
		for (final Rule rule : rules) {
			if (shouldBeEvaluated(rule, facts)) {
				result.put(rule, rule.evaluate(facts));
			}
		}
		return result;
	}

	void doFire(final Rules rules, final Facts facts) {
		if (rules.isEmpty()) {
			LOGGER.debug("No rules registered! Nothing to apply");
			return;
		}
		logEngineParameters();
		log(rules);
		log(facts);
		LOGGER.debug("Rules evaluation started");
		for (final Rule rule : rules) {
			final String name = rule.getName();
			final int priority = rule.getPriority();
			if (priority > parameters.getPriorityThreshold()) {
				LOGGER.debug(
						"Rule priority threshold ({}) exceeded at rule '{}' with priority={}, next rules will be skipped",
						parameters.getPriorityThreshold(), name, priority);
				break;
			}
			if (!shouldBeEvaluated(rule, facts)) {
				LOGGER.debug("Rule '{}' has been skipped before being evaluated", name);
				continue;
			}
			boolean evaluationResult = false;
			try {
				evaluationResult = rule.evaluate(facts);
			} catch (final RuntimeException exception) {
				LOGGER.error("Rule '" + name + "' evaluated with error", exception);
				triggerListenersOnEvaluationError(rule, facts, exception);
				// give the option to either skip next rules on evaluation error or continue by
				// considering the evaluation error as false
				if (parameters.isSkipOnFirstNonTriggeredRule()) {
					LOGGER.debug("Next rules will be skipped since parameter skipOnFirstNonTriggeredRule is set");
					break;
				}
			}
			if (evaluationResult) {
				LOGGER.debug("Rule '{}' triggered", name);
				triggerListenersAfterEvaluate(rule, facts, true);
				try {
					triggerListenersBeforeExecute(rule, facts);
					rule.execute(facts);
					LOGGER.debug("Rule '{}' performed successfully", name);
					triggerListenersOnSuccess(rule, facts);
					if (parameters.isSkipOnFirstAppliedRule()) {
						LOGGER.debug("Next rules will be skipped since parameter skipOnFirstAppliedRule is set");
						break;
					}
				} catch (final Exception exception) {
					LOGGER.error("Rule '" + name + "' performed with error", exception);
					triggerListenersOnFailure(rule, exception, facts);
					if (parameters.isSkipOnFirstFailedRule()) {
						LOGGER.debug("Next rules will be skipped since parameter skipOnFirstFailedRule is set");
						break;
					}
				}
			} else {
				LOGGER.debug("Rule '{}' has been evaluated to false, it has not been executed", name);
				triggerListenersAfterEvaluate(rule, facts, false);
				if (parameters.isSkipOnFirstNonTriggeredRule()) {
					LOGGER.debug("Next rules will be skipped since parameter skipOnFirstNonTriggeredRule is set");
					break;
				}
			}
		}
	}

	@Override
	public void fire(final Rules rules, final Facts facts) {
		Objects.requireNonNull(rules, "Rules must not be null");
		Objects.requireNonNull(facts, "Facts must not be null");
		triggerListenersBeforeRules(rules, facts);
		doFire(rules, facts);
		triggerListenersAfterRules(rules, facts);
	}

	private void log(final Facts facts) {
		LOGGER.debug("Known facts:");
		for (final Fact<?> fact : facts) {
			LOGGER.debug("{}", fact);
		}
	}

	private void log(final Rules rules) {
		LOGGER.debug("Registered rules:");
		for (final Rule rule : rules) {
			LOGGER.debug("Rule { name = '{}', description = '{}', priority = '{}'}", rule.getName(),
					rule.getDescription(), rule.getPriority());
		}
	}

	private void logEngineParameters() {
		LOGGER.debug("{}", parameters);
	}

	private boolean shouldBeEvaluated(final Rule rule, final Facts facts) {
		return triggerListenersBeforeEvaluate(rule, facts);
	}

	private void triggerListenersAfterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
		ruleListeners.forEach(ruleListener -> ruleListener.afterEvaluate(rule, facts, evaluationResult));
	}

	private void triggerListenersAfterRules(final Rules rule, final Facts facts) {
		rulesEngineListeners.forEach(rulesEngineListener -> rulesEngineListener.afterExecute(rule, facts));
	}

	private boolean triggerListenersBeforeEvaluate(final Rule rule, final Facts facts) {
		return ruleListeners.stream().allMatch(ruleListener -> ruleListener.beforeEvaluate(rule, facts));
	}

	private void triggerListenersBeforeExecute(final Rule rule, final Facts facts) {
		ruleListeners.forEach(ruleListener -> ruleListener.beforeExecute(rule, facts));
	}

	private void triggerListenersBeforeRules(final Rules rule, final Facts facts) {
		rulesEngineListeners.forEach(rulesEngineListener -> rulesEngineListener.beforeEvaluate(rule, facts));
	}

	private void triggerListenersOnEvaluationError(final Rule rule, final Facts facts, final Exception exception) {
		ruleListeners.forEach(ruleListener -> ruleListener.onEvaluationError(rule, facts, exception));
	}

	private void triggerListenersOnFailure(final Rule rule, final Exception exception, final Facts facts) {
		ruleListeners.forEach(ruleListener -> ruleListener.onFailure(rule, facts, exception));
	}

	private void triggerListenersOnSuccess(final Rule rule, final Facts facts) {
		ruleListeners.forEach(ruleListener -> ruleListener.onSuccess(rule, facts));
	}

}
