package org.rossonet.utils;

import org.junit.jupiter.api.Test;

public final class SslHelperTests {

	private static final String CERTIFICATE = null;

	@Test
	public void testConvertOneLine() {
		System.out.println(SslHelper.certificateStringFromOneLine(CERTIFICATE));
	}

}
