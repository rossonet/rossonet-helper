package org.rossonet.brain.api.neurone.simple;

import java.util.UUID;

import org.rossonet.brain.api.neurone.Neurone;
import org.rossonet.brain.api.neurone.SymbiosisInterface;

public class SimpleNeurone implements Neurone {

	private final UUID uuid = UUID.randomUUID();
	private SymbiosisInterface symbiosisInterface;

	@Override
	public SymbiosisInterface getSymbiosisInterface() {
		return symbiosisInterface;
	}

	@Override
	public UUID getUniversallyUniqueIdentifier() {
		return uuid;
	}

	public SymbiosisInterface setSymbiosisInterface(SymbiosisInterface symbiosisInterface) {
		return symbiosisInterface;
	}

}
