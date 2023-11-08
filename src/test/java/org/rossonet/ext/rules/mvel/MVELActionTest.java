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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.rossonet.ext.rules.api.Action;
import org.rossonet.ext.rules.api.Facts;

public class MVELActionTest {

	@Test
	public void testMVELActionExecution() throws Exception {
		// given
		final Action markAsAdult = new MVELAction("person.setAdult(true);");
		final Facts facts = new Facts();
		final Person foo = new Person("foo", 20);
		facts.put("person", foo);

		// when
		markAsAdult.execute(facts);

		// then
		assertThat(foo.isAdult()).isTrue();
	}

	@Test
	public void testMVELActionExecutionWithFailure() {
		// given
		final Action action = new MVELAction("person.setBlah(true);");
		final Facts facts = new Facts();
		final Person foo = new Person("foo", 20);
		facts.put("person", foo);

		// when
		assertThatThrownBy(() -> action.execute(facts))
				// then
				.isInstanceOf(Exception.class).hasMessageContaining(
						"Error: unable to resolve method: org.rossonet.ext.rules.mvel.Person.setBlah(java.lang.Boolean)");
	}

}
