/*
 * Copyright 2009 Bojan Tomic
 *
 * This file is part of JEFF (Java Explanation Facility Framework).
 *
 * JEFF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JEFF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with JEFF.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goodoldai.jeff.explanation.builder;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationException;
import junit.framework.TestCase;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Since ExplanationBuilder is an abstract class the test
 * case is also abstract. When testing ExplanationBuilder
 * concrete subclasses, their test case needs to subclass
 * this test case and implement its abstract methods
 * in order to provide concrete subclass instances, and
 * the appropriate sample content.
 *
 * @author Bojan Tomic
 */
public abstract class ExplanationBuilderTest extends AbstractJeffTest {
    
    /**
     * An ExplanationBuilder instance to be used while testing
     */
    protected ExplanationBuilder instance = null;

    /**
     * An Explanation instance to be used while testing
     */
    protected Explanation explanation = null;

    /**
     * Invokes the two argument constructor and returns the
     * ExplanationBuilder subclass instance.
     *
     * @param explanation explanation isntance
     * @param factory explanation chunk builder factory
     *
     * @return explanation builder instance
     */
    public abstract ExplanationBuilder getInstance(Explanation explanation,
            ExplanationChunkBuilderFactory factory);

    /**
     * Returns a concrete explanation chunk builder factory instance needed
     * for explanation builder initialization.
     *
     * @return explanation chunk builder factory instance
     */
    public abstract ExplanationChunkBuilderFactory getFactory();


    /**
     * Test of two argument constructor, of class ExplanationBuilder.
     * Test case: unsucessfull initialization - explanation is null
     */
    @Test
    public void testExplanationChunkNullExplanation() {
        try {
            instance = getInstance(null,getFactory());
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered explanation must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of two argument constructor, of class ExplanationBuilder.
     * Test case: unsucessfull initialization - factory is null
     */
    @Test
    public void testExplanationChunkNullFactory() {
        try {
            instance = getInstance(explanation,null);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered factory must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of two argument constructor, of class ExplanationBuilder.
     * Test case: sucessfull initialization
     */
    @Test
    public void testExplanationChunkSuccessfull() {
        instance = getInstance(explanation,getFactory());

        assertEquals(explanation,instance.getExplanation());
    }

    /**
     * Test of getExplanation method, of class ExplanationBuilder.
     * Test case: sucessfull execution
     */
    @Test
    public void testGetExplanation() {
        instance = getInstance(explanation,getFactory());

        assertEquals(explanation,instance.getExplanation());
    }

}
