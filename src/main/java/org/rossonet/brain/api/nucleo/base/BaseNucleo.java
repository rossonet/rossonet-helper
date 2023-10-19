package org.rossonet.brain.api.nucleo.base;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.rossonet.brain.api.astrocyte.Astrocyte;
import org.rossonet.brain.api.neurone.Neurone;
import org.rossonet.brain.api.nucleo.Nucleo;

public class BaseNucleo implements Nucleo {

	private final Set<Astrocyte> astrocytes = ConcurrentHashMap.newKeySet();
	private final Set<Neurone> neurones = ConcurrentHashMap.newKeySet();
	private final UUID uuid = UUID.randomUUID();

	@Override
	public void addAstrocyte(Astrocyte astrocyte) {
		astrocytes.add(astrocyte);

	}

	@Override
	public void addNeurone(Neurone neurone) {
		neurones.add(neurone);

	}

	@Override
	public UUID getUniversallyUniqueIdentifier() {
		return uuid;
	}

	@Override
	public Collection<Astrocyte> listAstrocytes() {
		return astrocytes;
	}

	@Override
	public Collection<Neurone> listNeurones() {
		return neurones;
	}

	@Override
	public void removeAstrocyte(Astrocyte astrocyte) {
		astrocytes.remove(astrocyte);

	}

	@Override
	public void removeNeurone(Neurone neurone) {
		neurones.remove(neurone);

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
