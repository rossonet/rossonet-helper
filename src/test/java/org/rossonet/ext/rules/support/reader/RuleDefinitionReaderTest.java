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
package org.rossonet.ext.rules.support.reader;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.rossonet.ext.rules.api.Rule;
import org.rossonet.ext.rules.support.RuleDefinition;
import org.rossonet.ext.rules.support.reader.JsonRuleDefinitionReader;
import org.rossonet.ext.rules.support.reader.RuleDefinitionReader;
import org.rossonet.ext.rules.support.reader.YamlRuleDefinitionReader;

public class RuleDefinitionReaderTest {

	// @Parameterized.Parameters
	public static Collection<Object[]> parameters() {
		return Arrays.asList(new Object[][] { { new YamlRuleDefinitionReader(), "yml" },
				{ new JsonRuleDefinitionReader(), "json" }, });
	}

	// @Parameterized.Parameter(0)
	public RuleDefinitionReader ruleDefinitionReader;

	// @Parameterized.Parameter(1)
	public String fileExtension;

	@Test
	public void testEmptyRulesDefinitionReading() throws Exception {
		// given
		final File rulesDescriptor = new File("src/test/resources/rules-empty." + fileExtension);

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new FileReader(rulesDescriptor));

		// then
		assertThat(ruleDefinitions).hasSize(0);
	}

	@Test // (expected = IllegalArgumentException.class)
	public void testInvalidRuleDefinitionReading_whenNoActions() throws Exception {
		// given
		final File adultRuleDescriptor = new File("src/test/resources/adult-rule-without-actions." + fileExtension);

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new FileReader(adultRuleDescriptor));

		// then
		// expected exception
	}

	@Test // (expected = IllegalArgumentException.class)
	public void testInvalidRuleDefinitionReading_whenNoCondition() throws Exception {
		// given
		final File adultRuleDescriptor = new File("src/test/resources/adult-rule-without-condition." + fileExtension);

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new FileReader(adultRuleDescriptor));

		// then
		// expected exception
	}

	@Test
	public void testRuleDefinitionReading_withCompositeAndBasicRules() throws Exception {
		// given
		final File compositeRuleDescriptor = new File("src/test/resources/composite-rules." + fileExtension);

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new FileReader(compositeRuleDescriptor));

		// then
		assertThat(ruleDefinitions).hasSize(2);

		// then
		RuleDefinition ruleDefinition = ruleDefinitions.get(0);
		assertThat(ruleDefinition).isNotNull();
		assertThat(ruleDefinition.getName()).isEqualTo("Movie id rule");
		assertThat(ruleDefinition.getDescription()).isEqualTo("description");
		assertThat(ruleDefinition.getPriority()).isEqualTo(1);
		assertThat(ruleDefinition.getCompositeRuleType()).isEqualTo("UnitRuleGroup");
		assertThat(ruleDefinition.getComposingRules()).isNotEmpty();

		final List<RuleDefinition> subrules = ruleDefinition.getComposingRules();
		assertThat(subrules).hasSize(2);

		RuleDefinition subrule = subrules.get(0);
		assertThat(subrule.getName()).isEqualTo("Time is evening");
		assertThat(subrule.getDescription()).isEqualTo("If it's later than 7pm");
		assertThat(subrule.getPriority()).isEqualTo(1);

		subrule = subrules.get(1);
		assertThat(subrule.getName()).isEqualTo("Movie is rated R");
		assertThat(subrule.getDescription()).isEqualTo("If the movie is rated R");
		assertThat(subrule.getPriority()).isEqualTo(1);

		ruleDefinition = ruleDefinitions.get(1);
		assertThat(ruleDefinition).isNotNull();
		assertThat(ruleDefinition.getName()).isEqualTo("weather rule");
		assertThat(ruleDefinition.getDescription()).isEqualTo("when it rains, then take an umbrella");
		assertThat(ruleDefinition.getComposingRules()).isEmpty();
		assertThat(ruleDefinition.getCondition()).isEqualTo("rain == True");
		assertThat(ruleDefinition.getActions())
				.isEqualTo(Collections.singletonList("System.out.println(\"It rains, take an umbrella!\");"));
	}

	@Test
	public void testRuleDefinitionReading_withDefaultValues() throws Exception {
		// given
		final File adultRuleDescriptor = new File("src/test/resources/adult-rule-with-default-values." + fileExtension);

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new FileReader(adultRuleDescriptor));

		// then
		assertThat(ruleDefinitions).hasSize(1);
		final RuleDefinition adultRuleDefinition = ruleDefinitions.get(0);
		assertThat(adultRuleDefinition).isNotNull();
		assertThat(adultRuleDefinition.getName()).isEqualTo(Rule.DEFAULT_NAME);
		assertThat(adultRuleDefinition.getDescription()).isEqualTo(Rule.DEFAULT_DESCRIPTION);
		assertThat(adultRuleDefinition.getPriority()).isEqualTo(Rule.DEFAULT_PRIORITY);
		assertThat(adultRuleDefinition.getCondition()).isEqualTo("person.age > 18");
		assertThat(adultRuleDefinition.getActions()).isEqualTo(Collections.singletonList("person.setAdult(true);"));
	}

	@Test
	public void testRuleDefinitionReadingFromFile() throws Exception {
		// given
		final File adultRuleDescriptor = new File("src/test/resources/adult-rule." + fileExtension);

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new FileReader(adultRuleDescriptor));

		// then
		assertThat(ruleDefinitions).hasSize(1);
		final RuleDefinition adultRuleDefinition = ruleDefinitions.get(0);
		assertThat(adultRuleDefinition).isNotNull();
		assertThat(adultRuleDefinition.getName()).isEqualTo("adult rule");
		assertThat(adultRuleDefinition.getDescription()).isEqualTo("when age is greater than 18, then mark as adult");
		assertThat(adultRuleDefinition.getPriority()).isEqualTo(1);
		assertThat(adultRuleDefinition.getCondition()).isEqualTo("person.age > 18");
		assertThat(adultRuleDefinition.getActions()).isEqualTo(Collections.singletonList("person.setAdult(true);"));
	}

	@Test
	public void testRuleDefinitionReadingFromString() throws Exception {
		// given
		final Path ruleDescriptor = Paths.get("src/test/resources/adult-rule." + fileExtension);
		final String adultRuleDescriptor = new String(Files.readAllBytes(ruleDescriptor));

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new StringReader(adultRuleDescriptor));

		// then
		assertThat(ruleDefinitions).hasSize(1);
		final RuleDefinition adultRuleDefinition = ruleDefinitions.get(0);
		assertThat(adultRuleDefinition).isNotNull();
		assertThat(adultRuleDefinition.getName()).isEqualTo("adult rule");
		assertThat(adultRuleDefinition.getDescription()).isEqualTo("when age is greater than 18, then mark as adult");
		assertThat(adultRuleDefinition.getPriority()).isEqualTo(1);
		assertThat(adultRuleDefinition.getCondition()).isEqualTo("person.age > 18");
		assertThat(adultRuleDefinition.getActions()).isEqualTo(Collections.singletonList("person.setAdult(true);"));
	}

	@Test
	public void testRulesDefinitionReading() throws Exception {
		// given
		final File rulesDescriptor = new File("src/test/resources/rules." + fileExtension);

		// when
		final List<RuleDefinition> ruleDefinitions = ruleDefinitionReader.read(new FileReader(rulesDescriptor));

		// then
		assertThat(ruleDefinitions).hasSize(2);
		RuleDefinition ruleDefinition = ruleDefinitions.get(0);
		assertThat(ruleDefinition).isNotNull();
		assertThat(ruleDefinition.getName()).isEqualTo("adult rule");
		assertThat(ruleDefinition.getDescription()).isEqualTo("when age is greater than 18, then mark as adult");
		assertThat(ruleDefinition.getPriority()).isEqualTo(1);
		assertThat(ruleDefinition.getCondition()).isEqualTo("person.age > 18");
		assertThat(ruleDefinition.getActions()).isEqualTo(Collections.singletonList("person.setAdult(true);"));

		ruleDefinition = ruleDefinitions.get(1);
		assertThat(ruleDefinition).isNotNull();
		assertThat(ruleDefinition.getName()).isEqualTo("weather rule");
		assertThat(ruleDefinition.getDescription()).isEqualTo("when it rains, then take an umbrella");
		assertThat(ruleDefinition.getPriority()).isEqualTo(2);
		assertThat(ruleDefinition.getCondition()).isEqualTo("rain == true");
		assertThat(ruleDefinition.getActions())
				.isEqualTo(Collections.singletonList("System.out.println(\"It rains, take an umbrella!\");"));
	}
}
