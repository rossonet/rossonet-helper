package org.rossonet.brain.api.neurone;

import java.util.Collection;

import org.rossonet.brain.api.UniversallyUniqueObject;
import org.rossonet.brain.api.job.BrainJob;
import org.rossonet.brain.api.neurone.axion.Axion;
import org.rossonet.brain.api.neurone.dendrite.Dendrite;

public interface Neurone<MANAGED_OBJECT extends Object> extends UniversallyUniqueObject {

	public Axion getAxion();

	public Collection<Dendrite> getDendrites();

	BrainJobExecutionStrategy getJobExecutionStrategy();

	public SymbiosisInterface<MANAGED_OBJECT> getSymbiosisInterface();

	void runJob(Dendrite dendrite, BrainJob job);

}
