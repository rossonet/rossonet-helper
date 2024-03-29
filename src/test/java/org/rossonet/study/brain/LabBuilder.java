package org.rossonet.study.brain;

import org.junit.jupiter.api.Test;
import org.rossonet.brain.api.AbstractNucleoFactory;
import org.rossonet.brain.api.neurone.SymbiosisInterface;
import org.rossonet.brain.api.nucleo.Nucleo;
import org.rossonet.brain.api.nucleo.base.BaseNucleoFactory;

public class LabBuilder {

	private final Nucleo nucleo;

	private final AbstractNucleoFactory bf = new BaseNucleoFactory();

	@SuppressWarnings("unchecked")
	public LabBuilder() {
		final SymbiosisInterface<?> managedObject1 = new ManagedObject();
		final SymbiosisInterface<?> managedObject2 = new ManagedObject();
		nucleo = bf.newNucleoBuilder().addNeurone(bf.newNeuroneBuilder().setSymbiosis(managedObject1).build())
				.addNeurone(bf.newNeuroneBuilder().setSymbiosis(managedObject2).build()).build();
		nucleo.start();
	}

	@Test
	public void testThread() {
	}

}
