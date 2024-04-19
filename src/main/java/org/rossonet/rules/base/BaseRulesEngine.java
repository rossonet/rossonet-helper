package org.rossonet.rules.base;

import org.rossonet.ext.rules.api.RulesEngine;

public interface BaseRulesEngine {

	enum RulesEngineStatus {
		INIT, ACTIVE, FAULTED
	}

	public void addFactProvider(FactProvider factProvider);

	public void addRulesProvider(RuleProvider ruleProvider);

	void clearFacts();

	void clearRules();

	public CommandQueue fireRules();

	CachedMemory getCachedMemory();

	public RulesEngineStatus getStatus();

	void resetAndInitialize();

	void resetAndInitialize(RulesEngine ruleEngine);

	void setCachedMemory(CachedMemory cachedMemory);

}
