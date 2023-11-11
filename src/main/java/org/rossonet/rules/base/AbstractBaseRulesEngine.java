package org.rossonet.rules.base;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.rossonet.ext.rules.api.Fact;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.ext.rules.api.Rules;
import org.rossonet.ext.rules.api.RulesEngine;
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
	private final Set<FactProvider> factsProviders = Collections.synchronizedSet(new HashSet<>());
	private final Set<RuleProvider> rulesProviders = Collections.synchronizedSet(new HashSet<>());
	private RulesEngine rulesEngine;
	private final Logger logger = LoggerFactory.getLogger(AbstractBaseRulesEngine.class);
	private RulesEngineStatus status = RulesEngineStatus.INIT;
	private CachedMemory cachedMemory;
	private final AbstractRuleFactory ruleFactory;

	public AbstractBaseRulesEngine() {
		this(new MVELRuleFactory(new JsonRuleDefinitionReader()));
	}

	public AbstractBaseRulesEngine(AbstractRuleFactory ruleFactory) {
		this.ruleFactory = ruleFactory;
		resetAndInitialize();
		status = RulesEngineStatus.ACTIVE;
	}

	@Override
	public void addFactProvider(FactProvider factProvider) {
		factsProviders.add(factProvider);
	}

	@Override
	public void addRulesProvider(RuleProvider ruleProvider) {
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

	private Rules createRules(JSONArray rules) throws Exception {
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
	public void resetAndInitialize(RulesEngine rulesEngine) {
		status = RulesEngineStatus.INIT;
		clearFacts();
		clearRules();
		this.rulesEngine = rulesEngine;
		status = RulesEngineStatus.ACTIVE;
	}

	@Override
	public void setCachedMemory(CachedMemory cachedMemory) {
		if (factsProviders.contains(this.cachedMemory)) {
			factsProviders.remove(cachedMemory);
		}
		this.cachedMemory = cachedMemory;
		factsProviders.add(cachedMemory);
	}
}
