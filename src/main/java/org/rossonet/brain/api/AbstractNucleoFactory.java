package org.rossonet.brain.api;

import org.rossonet.brain.api.astrocyte.AstrocyteBuilder;
import org.rossonet.brain.api.neurone.NeuroneBuilder;
import org.rossonet.brain.api.nucleo.NucleoBuilder;

public abstract class AbstractNucleoFactory {

	public abstract AstrocyteBuilder newAstrocyteBuilder();

	public abstract NeuroneBuilder newNeuroneBuilder();

	public abstract NucleoBuilder newNucleoBuilder();

}
