package org.rossonet.brain.api.astrocyte;

public interface AstrocyteBuilder {

	AstrocyteBuilder bootstrap(boolean isBootstrapAstrocyte);

	Astrocyte build();

}
