package org.rossonet.brain.api.astrocyte.simple;

import org.rossonet.brain.api.astrocyte.Astrocyte;
import org.rossonet.brain.api.astrocyte.AstrocyteBuilder;

public class SimpleAstrocyteBuilder implements AstrocyteBuilder {

	private final SimpleAstrocyte simpleAstrocyte = new SimpleAstrocyte();

	@Override
	public Astrocyte build() {
		return simpleAstrocyte;
	}

}
