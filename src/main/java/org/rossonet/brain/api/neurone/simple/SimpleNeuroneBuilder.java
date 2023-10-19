package org.rossonet.brain.api.neurone.simple;

import org.rossonet.brain.api.neurone.Neurone;
import org.rossonet.brain.api.neurone.NeuroneBuilder;
import org.rossonet.brain.api.neurone.SymbiosisInterface;

public class SimpleNeuroneBuilder implements NeuroneBuilder {

	private final SimpleNeurone simpleNeurone = new SimpleNeurone();

	@Override
	public Neurone build() {
		return simpleNeurone;
	}

	@Override
	public NeuroneBuilder setSymbiosis(SymbiosisInterface managedObj) {
		simpleNeurone.setSymbiosisInterface(managedObj);
		return this;
	}

}
