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
package org.rossonet.ext.rules.mvel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mvel2.ParserContext;
import org.rossonet.ext.rules.api.Condition;
import org.rossonet.ext.rules.api.Facts;

public class MVELConditionTest {

	@Test
	public void testMVELConditionWithExpressionAndParserContext() {
		// given
		final ParserContext context = new ParserContext();
		context.addPackageImport("java.util");
		final Condition condition = new MVELCondition("return new java.util.Random(123).nextBoolean();", context);
		final Facts facts = new Facts();
		// when
		final boolean evaluationResult = condition.evaluate(facts);

		// then
		assertThat(evaluationResult).isTrue();
	}

	@Test
	public void testMVELExpressionEvaluation() {
		// given
		final Condition isAdult = new MVELCondition("person.age > 18");
		final Facts facts = new Facts();
		facts.put("person", new Person("foo", 20));

		// when
		final boolean evaluationResult = isAdult.evaluate(facts);

		// then
		assertThat(evaluationResult).isTrue();
	}

	// Note this behaviour is different in SpEL, where a missing fact is silently
	// ignored and returns false
	@Test // (expected = RuntimeException.class)
	public void whenDeclaredFactIsNotPresent_thenShouldThrowRuntimeException() {
		// given
		final Condition isHot = new MVELCondition("temperature > 30");
		final Facts facts = new Facts();

		// when
		final boolean evaluationResult = isHot.evaluate(facts);

		// then
		// expected exception
	}
}
