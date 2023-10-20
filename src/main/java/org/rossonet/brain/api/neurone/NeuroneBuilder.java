package org.rossonet.brain.api.neurone;

public interface NeuroneBuilder<MANAGED_OBJECT extends Object> {

	Neurone<MANAGED_OBJECT> build();

	NeuroneBuilder<MANAGED_OBJECT> setSymbiosis(SymbiosisInterface<MANAGED_OBJECT> managedObj);

}
