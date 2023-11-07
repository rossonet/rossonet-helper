package org.rossonet.brain.api.neurone.simple;

import org.rossonet.brain.api.neurone.Neurone;
import org.rossonet.brain.api.neurone.NeuroneBuilder;
import org.rossonet.brain.api.neurone.SymbiosisInterface;

public class SimpleNeuroneBuilder<MANAGED_OBJECT extends Object> implements NeuroneBuilder<MANAGED_OBJECT> {

	private final SimpleNeurone<MANAGED_OBJECT> simpleNeurone = new SimpleNeurone<>();

	@Override
	public Neurone<MANAGED_OBJECT> build() {
		return simpleNeurone;
	}

	@Override
	public NeuroneBuilder<MANAGED_OBJECT> setSymbiosis(SymbiosisInterface<MANAGED_OBJECT> managedObj) {
		simpleNeurone.setSymbiosisInterface(managedObj);
		return this;
	}

}
