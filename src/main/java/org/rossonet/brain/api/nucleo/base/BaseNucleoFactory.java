package org.rossonet.brain.api.nucleo.base;

import org.rossonet.brain.api.AbstractNucleoFactory;
import org.rossonet.brain.api.neurone.NeuroneBuilder;
import org.rossonet.brain.api.neurone.simple.SimpleNeuroneBuilder;
import org.rossonet.brain.api.nucleo.NucleoBuilder;

public class BaseNucleoFactory extends AbstractNucleoFactory {

	@Override
	public NeuroneBuilder<?> newNeuroneBuilder() {
		return new SimpleNeuroneBuilder<>();
	}

	@Override
	public NucleoBuilder newNucleoBuilder() {
		return new BaseNucleoBuilder();
	}

}
