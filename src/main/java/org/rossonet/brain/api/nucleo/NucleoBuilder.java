package org.rossonet.brain.api.nucleo;

import org.rossonet.brain.api.astrocyte.Astrocyte;
import org.rossonet.brain.api.neurone.Neurone;

public interface NucleoBuilder {

	public NucleoBuilder addAstrocyte(Astrocyte astrocyte);

	public NucleoBuilder addNeurone(Neurone neurone);

	public Nucleo build();

}
