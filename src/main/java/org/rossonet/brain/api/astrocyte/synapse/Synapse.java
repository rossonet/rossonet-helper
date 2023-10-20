package org.rossonet.brain.api.astrocyte.synapse;

import java.util.Collection;

import org.rossonet.brain.api.UniversallyUniqueObject;
import org.rossonet.brain.api.astrocyte.synapse.rules.BrainFact;
import org.rossonet.brain.api.neurone.axion.Axion;
import org.rossonet.brain.api.neurone.dendrite.Dendrite;

public interface Synapse extends UniversallyUniqueObject {

	void clearCachedFacts();

	Axion getAxion();

	Dendrite getDendrite();

	Collection<BrainFact<?>> peekCachedFacts();

	Collection<BrainFact<?>> pollCachedFacts();

	void refuelling(BrainThread brainThread);

	void registerCompleteCallback(SynapseCallback callback);

}
