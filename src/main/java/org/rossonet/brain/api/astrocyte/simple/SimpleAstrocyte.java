package org.rossonet.brain.api.astrocyte.simple;

import java.util.Collection;
import java.util.UUID;

import org.rossonet.brain.api.astrocyte.Astrocyte;
import org.rossonet.brain.api.astrocyte.synapse.BrainThread;
import org.rossonet.brain.api.astrocyte.synapse.Synapse;
import org.rossonet.brain.api.message.BrainEventMonitor;
import org.rossonet.brain.api.neurone.axion.Axion;
import org.rossonet.brain.api.neurone.dendrite.Dendrite;

public class SimpleAstrocyte implements Astrocyte {

	private final UUID uuid = UUID.randomUUID();

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void deregisterAxion(Axion axion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deregisterDendrite(Dendrite axion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deregisterMonitor(BrainEventMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public AstrocyteStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getUniversallyUniqueIdentifier() {
		return uuid;
	}

	@Override
	public boolean isDebugMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Axion> listAxions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Dendrite> listDendrites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<BrainThread> listManagedThreads() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<BrainEventMonitor> listMonitors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Synapse> listSynapses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerAxion(Axion axion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerDendrite(Dendrite axion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerMonitor(BrainEventMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDebugMode(boolean isDebugMode) {
		// TODO Auto-generated method stub

	}

}
