package org.rossonet.brain.api.nucleo.base;

import org.rossonet.brain.api.astrocyte.Astrocyte;
import org.rossonet.brain.api.neurone.Neurone;
import org.rossonet.brain.api.nucleo.Nucleo;
import org.rossonet.brain.api.nucleo.NucleoBuilder;

public class BaseNucleoBuilder implements NucleoBuilder {

	private final Nucleo nucleo = new BaseNucleo();

	@Override
	public NucleoBuilder addAstrocyte(Astrocyte astrocyte) {
		nucleo.addAstrocyte(astrocyte);
		return this;
	}

	@Override
	public NucleoBuilder addNeurone(Neurone neurone) {
		nucleo.addNeurone(neurone);
		return this;
	}

	@Override
	public Nucleo build() {
		return nucleo;
	}

}
