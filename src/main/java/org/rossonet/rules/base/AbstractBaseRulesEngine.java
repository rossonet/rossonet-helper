package org.rossonet.rules.base;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.rossonet.ext.rules.api.Fact;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.ext.rules.api.Rule;
import org.rossonet.ext.rules.api.RuleListener;
import org.rossonet.ext.rules.api.Rules;
import org.rossonet.ext.rules.api.RulesEngine;
import org.rossonet.ext.rules.api.RulesEngineListener;
import org.rossonet.ext.rules.core.DefaultRulesEngine;
import org.rossonet.ext.rules.mvel.MVELRuleFactory;
import org.rossonet.ext.rules.support.AbstractRuleFactory;
import org.rossonet.ext.rules.support.reader.JsonRuleDefinitionReader;
import org.rossonet.utils.LogHelper;
import org.rossonet.utils.TextHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBaseRulesEngine implements BaseRulesEngine {

	public static final String CTX = "ctx";
	public static final String MEM = "mem";
	private static final String MAT = "mat";
	private static final String RANDOM = "rand";
	private final Set<FactProvider> factsProviders = Collections.synchronizedSet(new HashSet<>());
	private final Set<RuleProvider> rulesProviders = Collections.synchronizedSet(new HashSet<>());
	private RulesEngine rulesEngine;
	private final Logger logger = LoggerFactory.getLogger(AbstractBaseRulesEngine.class);
	private RulesEngineStatus status = RulesEngineStatus.INIT;
	private CachedMemory cachedMemory;
	private final AbstractRuleFactory ruleFactory;
	private final RuleListener ruleListener = new RuleListener() {

		@Override
		public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
			// TODO: preparare il debug on demand
			RuleListener.super.afterEvaluate(rule, facts, evaluationResult);
		}

		@Override
		public boolean beforeEvaluate(final Rule rule, final Facts facts) {
			// TODO: preparare il debug on demand
			// se false, la regola non viene eseguita
			return true;
		}

		@Override
		public void beforeExecute(final Rule rule, final Facts facts) {
			// TODO: preparare il debug on demand
		}

		@Override
		public void onEvaluationError(final Rule rule, final Facts facts, final Exception exception) {
			// TODO: preparare il debug on demand
		}

		@Override
		public void onFailure(final Rule rule, final Facts facts, final Exception exception) {
			// TODO: preparare il debug on demand
		}

		@Override
		public void onSuccess(final Rule rule, final Facts facts) {
			// TODO: preparare il debug on demand
		}

	};
	private final RulesEngineListener rulesEngineListener = new RulesEngineListener() {

		@Override
		public void afterExecute(final Rules rules, final Facts facts) {
			// TODO: preparare il debug on demand
		}

		@Override
		public void beforeEvaluate(final Rules rules, final Facts facts) {
			// TODO: preparare il debug on demand
		}

	};

	public AbstractBaseRulesEngine() {
		this(new MVELRuleFactory(new JsonRuleDefinitionReader()));
	}

	public AbstractBaseRulesEngine(final AbstractRuleFactory ruleFactory) {
		this.ruleFactory = ruleFactory;
		resetAndInitialize();
		status = RulesEngineStatus.ACTIVE;
	}

	@Override
	public void addFactProvider(final FactProvider factProvider) {
		factsProviders.add(factProvider);
	}

	@Override
	public void addRulesProvider(final RuleProvider ruleProvider) {
		this.rulesProviders.add(ruleProvider);
	}

	@Override
	public void clearFacts() {
		factsProviders.clear();
		factsProviders.add(cachedMemory);
	}

	@Override
	public void clearRules() {
		rulesProviders.clear();
	}

	private Rules createRules(final JSONArray rules) throws Exception {
		final Rules outputRules = ruleFactory.createRules(new StringReader(rules.toString()));
		if (outputRules != null) {
			logger.debug(TextHelper.ANSI_GREEN + "found " + outputRules.size() + " rules" + TextHelper.ANSI_RESET);
		} else {
			logger.warn(TextHelper.ANSI_RED + "NO RULES found" + TextHelper.ANSI_RESET);
		}
		return outputRules;
	}

	@Override
	public CommandQueue fireRules() {
		final Facts facts = new Facts();
		for (final FactProvider fp : factsProviders) {
			if (fp != null && fp.getFacts() != null) {
				for (final Fact<?> f : fp.getFacts()) {
					facts.put(f.getName(), f.getValue());
				}
			}
		}
		final JSONArray rules = new JSONArray();
		for (final RuleProvider rp : rulesProviders) {
			logger.debug("fire with " + rp.getRules().length() + " rules and " + facts.asMap().size() + " facts");
			rules.putAll(rp.getRules());
		}
		final CommandQueue commandQueue = new CommandQueue();
		try {
			final Rules activeRules = createRules(rules);
			facts.add(new Fact<>(CTX, new RulesContext(commandQueue, facts)));
			facts.add(new Fact<>(MAT, Math.class));
			facts.add(new Fact<>(RANDOM, ThreadLocalRandom.current()));
			rulesEngine.getRuleListeners().add(ruleListener);
			rulesEngine.getRulesEngineListeners().add(rulesEngineListener);
			rulesEngine.fire(activeRules, facts);
		} catch (final Exception e) {
			logger.error(
					TextHelper.ANSI_RED + "fire fault" + TextHelper.ANSI_RESET + LogHelper.stackTraceToString(e, 5));
		}
		return commandQueue;
	}

	@Override
	public CachedMemory getCachedMemory() {
		return cachedMemory;
	}

	public RulesEngine getRulesEngine() {
		return rulesEngine;
	}

	@Override
	public RulesEngineStatus getStatus() {
		return status;
	}

	@Override
	public void resetAndInitialize() {
		resetAndInitialize(new DefaultRulesEngine());
	}

	@Override
	public void resetAndInitialize(final RulesEngine rulesEngine) {
		status = RulesEngineStatus.INIT;
		clearFacts();
		clearRules();
		this.rulesEngine = rulesEngine;
		status = RulesEngineStatus.ACTIVE;
	}

	@Override
	public void setCachedMemory(final CachedMemory cachedMemory) {
		if (factsProviders.contains(this.cachedMemory)) {
			factsProviders.remove(cachedMemory);
		}
		this.cachedMemory = cachedMemory;
		factsProviders.add(cachedMemory);
	}
}
