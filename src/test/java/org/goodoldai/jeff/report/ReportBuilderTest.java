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
package org.goodoldai.jeff.report;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Since ReportBuilder is an abstract class the test
 * case is also abstract. When testing ReportBuilder
 * concrete subclasses, their test case needs to subclass
 * this test case and implement its abstract methods
 * in order to provide concrete subclass instances, and
 * the appropriate sample content.
 *
 * @author Bojan Tomic
 */
public abstract class ReportBuilderTest extends AbstractJeffTest {

    /**
     * A ReportBuilder instance to be used while testing
     */
    protected ReportBuilder instance = null;
    
    /**
     * Initializes the ReportBuilder instance
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        instance = getInstance(getFactory());
    }

    /**
     * Invokes the one argument constructor and returns the
     * ReportBuilder subclass instance.
     *
     * @param factory report chunk builder factory
     *
     * @return report builder instance
     */
    public abstract ReportBuilder getInstance(ReportChunkBuilderFactory factory);

    /**
     * Returns a concrete report chunk builder factory instance needed
     * for report builder initialization.
     *
     * @return report chunk builder factory instance
     */
    public abstract ReportChunkBuilderFactory getFactory();

    /**
     * Test of constructor with one argument, of class ExplanationChunk.
     * Test case: unsucessfull initialization - factory is null
     */
    @Test
    public void testConstructorOneArgumentNullFactory() {
        try {
            instance = getInstance(null);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "You must enter a non-null chunk factory reference";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildReport method, of class ReportBuilder.
     * Test case: unsuccessfull execution - explanation is null
     */
    @Test
    public void testBuildReport_Explanation_ObjectNullExplanationAndStream() {
        try {
            Object o = null;
            instance.buildReport(null, o);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered explanation must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class ReportBuilder.
     * Test case: unsuccessfull execution - explanation is null
     */
    @Test
    public void testBuildReport_Explanation_ObjectNullExplanation() {
        try {
            instance.buildReport(null, new Object());
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered explanation must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class ReportBuilder.
     * Test case: unsuccessfull execution - stream is null
     */
    @Test
    public void testBuildReport_Explanation_ObjectNullStream() {
        try {
            Object stream = null;
            instance.buildReport(new Explanation(), stream);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }
    
    /**
     * Test of buildReport method, of class ReportBuilder.
     * Test case: unsuccessfull execution - explanation is null
     */
    @Test
    public void testBuildReport_Explanation_StringNullExplanationAndFilepath() {
        try {
            Explanation e = null;
            String filepath = null;
            instance.buildReport(e, filepath);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered explanation must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class ReportBuilder.
     * Test case: unsuccessfull execution - explanation is null
     */
    @Test
    public void testBuildReport_Explanation_StringNullExplanation() {
        try {
            Explanation e = null;
            String filepath = "file.txt";
            instance.buildReport(e, filepath);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered explanation must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class ReportBuilder.
     * Test case: unsuccessfull execution - filepath is an empty String
     */
    @Test
    public void testBuildReport_Explanation_StringFilepathEmptyString() {
        try {
            Explanation e = new Explanation();
            String filepath = "";
            instance.buildReport(e, filepath);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered filepath must not be null or empty string";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class ReportBuilder.
     * Test case: unsuccessfull execution - filepath is null
     */
    @Test
    public void testBuildReport_Explanation_StringNullFilepath() {
        try {
            Explanation e = new Explanation();
            String filepath = null;
            instance.buildReport(e, filepath);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered filepath must not be null or empty string";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of isInsertChunkHeaders method, of class ReportBuilder.
     * Test case: successfull execution - check if false is the default value
     */
    @Test
    public void testIsInsertChunkHeadersDefaultFalse() {
           assertEquals(false, instance.isInsertChunkHeaders());
        }

    /**
     * Test of setInsertChunkHeaders method, of class ReportBuilder.
     * Test case: successfull execution
     */
    @Test
    public void testSetInsertChunkHeadersDefaultFalse() {
           instance.setInsertChunkHeaders(true);
           assertEquals(true, instance.isInsertChunkHeaders());

           instance.setInsertChunkHeaders(false);
           assertEquals(false, instance.isInsertChunkHeaders());
        }
}
