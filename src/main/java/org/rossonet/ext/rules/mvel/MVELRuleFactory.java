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

import java.io.Reader;
import java.util.List;

import org.mvel2.ParserContext;
import org.rossonet.ext.rules.api.Rule;
import org.rossonet.ext.rules.api.Rules;
import org.rossonet.ext.rules.support.AbstractRuleFactory;
import org.rossonet.ext.rules.support.RuleDefinition;
import org.rossonet.ext.rules.support.reader.JsonRuleDefinitionReader;
import org.rossonet.ext.rules.support.reader.RuleDefinitionReader;
import org.rossonet.ext.rules.support.reader.YamlRuleDefinitionReader;

/**
 * Factory to create {@link MVELRule} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MVELRuleFactory extends AbstractRuleFactory {

	private final RuleDefinitionReader reader;
	private final ParserContext parserContext;

	/**
	 * Create a new {@link MVELRuleFactory} with a given reader.
	 *
	 * @param reader used to read rule definitions
	 * @see YamlRuleDefinitionReader
	 * @see JsonRuleDefinitionReader
	 */
	public MVELRuleFactory(RuleDefinitionReader reader) {
		this(reader, new ParserContext());
	}

	/**
	 * Create a new {@link MVELRuleFactory} with a given reader.
	 *
	 * @param reader        used to read rule definitions
	 * @param parserContext used to parse condition/action expressions
	 * @see YamlRuleDefinitionReader
	 * @see JsonRuleDefinitionReader
	 */
	public MVELRuleFactory(RuleDefinitionReader reader, ParserContext parserContext) {
		this.reader = reader;
		this.parserContext = parserContext;
	}

	/**
	 * Create a new {@link MVELRule} from a Reader.
	 *
	 * The rule descriptor should contain a single rule definition. If no rule
	 * definitions are found, a {@link IllegalArgumentException} will be thrown. If
	 * more than a rule is defined in the descriptor, the first rule will be
	 * returned.
	 *
	 * @param ruleDescriptor descriptor of rule definition
	 * @return a new rule
	 * @throws Exception if unable to create the rule from the descriptor
	 */
	public Rule createRule(Reader ruleDescriptor) throws Exception {
		final List<RuleDefinition> ruleDefinitions = reader.read(ruleDescriptor);
		if (ruleDefinitions.isEmpty()) {
			throw new IllegalArgumentException("rule descriptor is empty");
		}
		return createRule(ruleDefinitions.get(0));
	}

	/**
	 * Create a set of {@link MVELRule} from a rule descriptor.
	 *
	 * @param rulesDescriptor descriptor of rule definitions
	 * @return a set of rules
	 * @throws Exception if unable to create rules from the descriptor
	 */
	@Override
	public Rules createRules(Reader rulesDescriptor) throws Exception {
		final Rules rules = new Rules();
		final List<RuleDefinition> ruleDefinitions = reader.read(rulesDescriptor);
		for (final RuleDefinition ruleDefinition : ruleDefinitions) {
			rules.register(createRule(ruleDefinition));
		}
		return rules;
	}

	@Override
	protected Rule createSimpleRule(RuleDefinition ruleDefinition) {
		final MVELRule mvelRule = new MVELRule(parserContext).name(ruleDefinition.getName())
				.description(ruleDefinition.getDescription()).priority(ruleDefinition.getPriority())
				.when(ruleDefinition.getCondition());
		for (final String action : ruleDefinition.getActions()) {
			mvelRule.then(action);
		}
		return mvelRule;
	}

	@Override
	protected RuleDefinitionReader getRuleDefinitionReader() {
		return reader;
	}

}
