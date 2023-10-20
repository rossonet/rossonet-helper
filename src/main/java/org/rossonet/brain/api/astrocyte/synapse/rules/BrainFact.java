package org.rossonet.brain.api.astrocyte.synapse.rules;

import org.rossonet.brain.api.message.BrainMessage;
import org.rossonet.ext.rules.api.Fact;

public class BrainFact<BRAIN_MESSAGE extends BrainMessage<?>> extends Fact<BRAIN_MESSAGE> {

	public BrainFact(String name, BRAIN_MESSAGE value) {
		super(name, value);
	}

}
