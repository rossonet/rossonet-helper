/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.rossonet.ext.rules.jexl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.junit.jupiter.api.Test;
import org.rossonet.ext.rules.api.Condition;
import org.rossonet.ext.rules.api.Facts;
import org.rossonet.ext.rules.jexl.JexlCondition;

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
public class JexlConditionTest {

	@Test
	public void testJexlConditionWithNamespace() {
		// given
		final Map<String, Object> namespaces = new HashMap<>();
		namespaces.put("rnd", new Random(123));
		final JexlEngine jexlEngine = new JexlBuilder().namespaces(namespaces).create();
		final Condition condition = new JexlCondition("return rnd:nextBoolean();", jexlEngine);
		final Facts facts = new Facts();

		// when
		final boolean evaluationResult = condition.evaluate(facts);

		// then
		assertThat(evaluationResult).isTrue();
	}

	@Test
	public void testJexlExpressionEvaluation() {
		// given
		final Condition isAdult = new JexlCondition("person.age > 18");
		final Facts facts = new Facts();
		facts.put("person", new Person("foo", 20));

		// when
		final boolean evaluationResult = isAdult.evaluate(facts);

		// then
		assertThat(evaluationResult).isTrue();
	}

	// Note this behaviour is different in SpEL, where a missing fact is silently
	// ignored and returns false
	// This behaviour is similar to MVEL though, where a missing fact results in an
	// exception
	@Test // (expected = RuntimeException.class)
	public void whenDeclaredFactIsNotPresent_thenShouldThrowRuntimeException() {
		// given
		final Condition isHot = new JexlCondition("temperature > 30");
		final Facts facts = new Facts();

		// when
		final boolean evaluationResult = isHot.evaluate(facts);

		// then
		// expected exception
	}
}
