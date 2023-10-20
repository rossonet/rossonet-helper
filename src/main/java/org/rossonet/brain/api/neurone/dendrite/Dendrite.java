package org.rossonet.brain.api.neurone.dendrite;

import org.rossonet.brain.api.UniversallyUniqueObject;
import org.rossonet.brain.api.astrocyte.synapse.Synapse;
import org.rossonet.brain.api.job.BrainJob;
import org.rossonet.brain.api.job.BrainJobFilter;
import org.rossonet.brain.api.neurone.Neurone;

public interface Dendrite extends UniversallyUniqueObject {

	BrainJobFilter getJobFilter();

	Neurone<?> getNeurone();

	void offer(Synapse synapse, BrainJob job);

}
