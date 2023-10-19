package org.rossonet.brain.api.neurone;

public interface NeuroneBuilder {

	Neurone build();

	NeuroneBuilder setSymbiosis(SymbiosisInterface managedObj);

}
