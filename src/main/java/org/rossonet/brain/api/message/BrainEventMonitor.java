package org.rossonet.brain.api.message;

import java.io.Serializable;

public interface BrainEventMonitor {

	public void brodcast(BrainMessage<? extends Serializable> event);

}
