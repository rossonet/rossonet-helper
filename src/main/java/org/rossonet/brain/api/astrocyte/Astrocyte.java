package org.rossonet.brain.api.astrocyte;

import org.rossonet.brain.api.UniversallyUniqueObject;

public interface Astrocyte extends UniversallyUniqueObject {

	boolean isBootstrap();

	void setBootstrap(boolean isBootstrapAstrocyte);

}
