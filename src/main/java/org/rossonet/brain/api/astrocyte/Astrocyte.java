package org.rossonet.brain.api.astrocyte;

import java.util.Collection;

import org.rossonet.brain.api.UniversallyUniqueObject;
import org.rossonet.brain.api.astrocyte.synapse.BrainThread;
import org.rossonet.brain.api.astrocyte.synapse.Synapse;
import org.rossonet.brain.api.message.BrainEventMonitor;
import org.rossonet.brain.api.neurone.axion.Axion;
import org.rossonet.brain.api.neurone.dendrite.Dendrite;

public interface Astrocyte extends UniversallyUniqueObject, AutoCloseable {

	public static enum AstrocyteStatus {
		INIT, BOOTING, BOOTING_DEBUG, EMPTY, RUNNING, RUNNING_DEBUG, FAULTED, TERMINATED
	}

	public void deregisterAxion(Axion axion);

	public void deregisterDendrite(Dendrite axion);

	public void deregisterMonitor(BrainEventMonitor monitor);

	public AstrocyteStatus getStatus();

	public boolean isDebugMode();

	public Collection<Axion> listAxions();

	public Collection<Dendrite> listDendrites();

	public Collection<BrainThread> listManagedThreads();

	public Collection<BrainEventMonitor> listMonitors();

	public Collection<Synapse> listSynapses();

	public void registerAxion(Axion axion);

	public void registerDendrite(Dendrite axion);

	public void registerMonitor(BrainEventMonitor monitor);

	public void setDebugMode(boolean isDebugMode);

}
