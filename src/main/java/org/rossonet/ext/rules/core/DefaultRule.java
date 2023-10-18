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
package org.rossonet.ext.rules.core;

import java.util.List;

import org.rossonet.ext.rules.api.Action;
import org.rossonet.ext.rules.api.Condition;
import org.rossonet.ext.rules.api.Facts;

class DefaultRule extends BasicRule {

	private final Condition condition;
	private final List<Action> actions;

	DefaultRule(String name, String description, int priority, Condition condition, List<Action> actions) {
		super(name, description, priority);
		this.condition = condition;
		this.actions = actions;
	}

	@Override
	public boolean evaluate(Facts facts) {
		return condition.evaluate(facts);
	}

	@Override
	public void execute(Facts facts) throws Exception {
		for (final Action action : actions) {
			action.execute(facts);
		}
	}

}
