package org.rossonet.brain.api.nucleo;

import java.util.Collection;

import org.rossonet.brain.api.UniversallyUniqueObject;
import org.rossonet.brain.api.astrocyte.Astrocyte;
import org.rossonet.brain.api.neurone.Neurone;

public interface Nucleo extends UniversallyUniqueObject {

	public void addNeurone(Neurone<?> neurone);

	public Collection<Astrocyte> listAstrocytes();

	public Collection<Neurone<?>> listNeurones();

	public void removeNeurone(Neurone<?> neurone);

	public void start();

	public void stop();

}
