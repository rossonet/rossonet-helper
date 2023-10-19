package org.rossonet.brain.api.astrocyte.simple;

import java.util.UUID;

import org.rossonet.brain.api.astrocyte.Astrocyte;

public class SimpleAstrocyte implements Astrocyte {

	private final UUID uuid = UUID.randomUUID();

	private boolean bootstrap;

	@Override
	public UUID getUniversallyUniqueIdentifier() {
		return uuid;
	}

	@Override
	public boolean isBootstrap() {
		return bootstrap;
	}

	@Override
	public void setBootstrap(boolean isBootstrapAstrocyte) {
		this.bootstrap = isBootstrapAstrocyte;

	}

}
