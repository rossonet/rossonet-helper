package org.rossonet.brain.api.neurone.simple;

import java.util.Collection;
import java.util.UUID;

import org.rossonet.brain.api.job.BrainJob;
import org.rossonet.brain.api.neurone.BrainJobExecutionStrategy;
import org.rossonet.brain.api.neurone.Neurone;
import org.rossonet.brain.api.neurone.SymbiosisInterface;
import org.rossonet.brain.api.neurone.axion.Axion;
import org.rossonet.brain.api.neurone.dendrite.Dendrite;

public class SimpleNeurone<O extends Object> implements Neurone<O> {

	private final UUID uuid = UUID.randomUUID();
	private SymbiosisInterface<O> symbiosisInterface;

	@Override
	public Axion getAxion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Dendrite> getDendrites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BrainJobExecutionStrategy getJobExecutionStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SymbiosisInterface<O> getSymbiosisInterface() {
		return symbiosisInterface;
	}

	@Override
	public UUID getUniversallyUniqueIdentifier() {
		return uuid;
	}

	@Override
	public void runJob(Dendrite dendrite, BrainJob job) {
		// TODO Auto-generated method stub

	}

	public void setSymbiosisInterface(SymbiosisInterface<O> symbiosisInterface) {
		this.symbiosisInterface = symbiosisInterface;
	}

}
