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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithActionMethodHavingMoreThanOneArgumentOfTypeFacts;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithActionMethodHavingOneArgumentNotOfTypeFacts;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithActionMethodHavingOneArgumentOfTypeFacts;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithActionMethodThatReturnsNonVoidType;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithConditionMethodHavingNonBooleanReturnType;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithConditionMethodHavingOneArgumentNotOfTypeFacts;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithMetaRuleAnnotation;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithMoreThanOnePriorityMethod;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithMultipleAnnotatedParametersAndOneParameterOfSubTypeFacts;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithMultipleAnnotatedParametersAndOneParameterOfTypeFacts;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithNonPublicActionMethod;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithNonPublicConditionMethod;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithNonPublicPriorityMethod;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithOneParameterNotAnnotatedWithFactAndNotOfTypeFacts;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithPriorityMethodHavingArguments;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithPriorityMethodHavingNonIntegerReturnType;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithoutActionMethod;
import org.rossonet.ext.rules.annotation.AnnotatedRuleWithoutConditionMethod;
import org.rossonet.ext.rules.core.RuleDefinitionValidator;

public class RuleDefinitionValidatorTest {

	private RuleDefinitionValidator ruleDefinitionValidator;

	/*
	 * Action method tests
	 */
	@Test // (expected = IllegalArgumentException.class)
	public void actionMethodMustBeDefined() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithoutActionMethod());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void actionMethodMustBePublic() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithNonPublicActionMethod());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void actionMethodMustHaveAtMostOneArgumentOfTypeFacts() {
		ruleDefinitionValidator
				.validateRuleDefinition(new AnnotatedRuleWithActionMethodHavingOneArgumentNotOfTypeFacts());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void actionMethodMustHaveExactlyOneArgumentOfTypeFactsIfAny() {
		ruleDefinitionValidator
				.validateRuleDefinition(new AnnotatedRuleWithActionMethodHavingMoreThanOneArgumentOfTypeFacts());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void actionMethodMustReturnVoid() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithActionMethodThatReturnsNonVoidType());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void actionMethodParametersShouldAllBeAnnotatedWithFactUnlessExactlyOneOfThemIsOfTypeFacts() {
		ruleDefinitionValidator
				.validateRuleDefinition(new AnnotatedRuleWithOneParameterNotAnnotatedWithFactAndNotOfTypeFacts());
	}

	/*
	 * Conditions methods tests
	 */
	@Test // (expected = IllegalArgumentException.class)
	public void conditionMethodMustBeDefined() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithoutConditionMethod());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void conditionMethodMustBePublic() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithNonPublicConditionMethod());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void conditionMethodMustReturnBooleanType() {
		ruleDefinitionValidator
				.validateRuleDefinition(new AnnotatedRuleWithConditionMethodHavingNonBooleanReturnType());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void conditionMethodParametersShouldAllBeAnnotatedWithFactUnlessExactlyOneOfThemIsOfTypeFacts() {
		ruleDefinitionValidator
				.validateRuleDefinition(new AnnotatedRuleWithOneParameterNotAnnotatedWithFactAndNotOfTypeFacts());
	}

	/*
	 * Rule annotation test
	 */
	@Test // (expected = IllegalArgumentException.class)
	public void notAnnotatedRuleMustNotBeAccepted() {
		ruleDefinitionValidator.validateRuleDefinition(new Object());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void priorityMethodMustBePublic() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithNonPublicPriorityMethod());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void priorityMethodMustBeUnique() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithMoreThanOnePriorityMethod());
	}

	@Test // (expected = IllegalArgumentException.class)
	public void priorityMethodMustHaveNoArguments() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithPriorityMethodHavingArguments());
	}

	/*
	 * Priority method tests
	 */

	@Test // (expected = IllegalArgumentException.class)
	public void priorityMethodReturnTypeMustBeInteger() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithPriorityMethodHavingNonIntegerReturnType());
	}

	@BeforeEach
	public void setup() {
		ruleDefinitionValidator = new RuleDefinitionValidator();
	}

	/*
	 * Valid definition tests
	 */
	@Test
	public void validAnnotationsShouldBeAccepted() {
		try {
			ruleDefinitionValidator.validateRuleDefinition(
					new AnnotatedRuleWithMultipleAnnotatedParametersAndOneParameterOfTypeFacts());
			ruleDefinitionValidator.validateRuleDefinition(
					new AnnotatedRuleWithMultipleAnnotatedParametersAndOneParameterOfSubTypeFacts());
			ruleDefinitionValidator
					.validateRuleDefinition(new AnnotatedRuleWithActionMethodHavingOneArgumentOfTypeFacts());
		} catch (final Throwable throwable) {
			Assertions.fail("Should not throw exception for valid rule definitions");
		}
	}

	@Test // (expected = IllegalArgumentException.class)
	public void whenConditionMethodHasOneNonAnnotatedParameter_thenThisParameterMustBeOfTypeFacts() {
		ruleDefinitionValidator
				.validateRuleDefinition(new AnnotatedRuleWithConditionMethodHavingOneArgumentNotOfTypeFacts());
	}

	@Test
	public void withCustomAnnotationThatIsItselfAnnotatedWithTheRuleAnnotation() {
		ruleDefinitionValidator.validateRuleDefinition(new AnnotatedRuleWithMetaRuleAnnotation());
	}
}
