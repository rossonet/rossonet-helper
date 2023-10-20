package org.rossonet.brain.api.neurone;

import java.util.Collection;

import org.rossonet.ext.rules.api.Rule;

public interface SymbiosisInterface<MANAGED_OBJECT extends Object> {

	MANAGED_OBJECT getManagedObject();

	Neurone<MANAGED_OBJECT> getNeurone();

	default Collection<Rule> getRules() {
		return getRules(null);
	}

	Collection<Rule> getRules(String ruleContext);

	// TODO aggiungere mappa metodi gestiti durante esecuzione regole

}
