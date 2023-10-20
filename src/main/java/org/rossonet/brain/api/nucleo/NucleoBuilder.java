package org.rossonet.brain.api.nucleo;

import org.rossonet.brain.api.neurone.Neurone;

public interface NucleoBuilder {

	public NucleoBuilder addNeurone(Neurone<?> neurone);

	public Nucleo build();

}
