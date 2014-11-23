/*
 * Copyright 2009 Boris Horvat
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
package org.goodoldai.jeff.report.xml;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import junit.framework.TestCase;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Boris Horvat
 */
public class XMLReportChunkBuilderFactoryTest extends AbstractJeffTest {

    XMLReportChunkBuilderFactory instance;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        instance = new XMLReportChunkBuilderFactory();
    }

    @After
    public void tearDown(){
        instance = null;
    }
    
    /**
     * Test of getReportChunkBuilder method, of class XMLReportChunkBuilderFactory.
     * Test case: unsuccessful execution - chunk type not recognized
     */
    @Test
    public void testGetXMLReportChunkBuilderTypeNotRecognized() {
        try {
            instance.getReportChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The provided ExplanationChunk does not match any of the required types";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class XMLReportChunkBuilderFactory.
     * Test case: successful execution, passing of TEXT Explanation Chunk type
     */
    @Test
    public void testGetXMLReportChunkBuilderText1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(new TextExplanationChunk("testText"));

        assertTrue(result instanceof XMLTextChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class XMLReportChunkBuilderFactory.
     * Test case: successful execution, passing of TEXT Explanation chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetXMLReportChunkBuilderText2() {

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(new TextExplanationChunk("testText1"));
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(new TextExplanationChunk("testText2"));

        assertTrue(result1 instanceof XMLTextChunkBuilder);
        assertTrue(result2 instanceof XMLTextChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class XMLReportChunkBuilderFactory.
     * Test case: successful execution, passing of IMAGE Explanation chunk type
     */
    @Test
    public void testGetXMLReportChunkBuilderImage1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test.jpg")));

        assertTrue(result instanceof XMLImageChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class XMLReportChunkBuilderFactory.
     * Test case: successful execution, passing of  IMAGE Explanation chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetXMLReportChunkBuilderImage2() {

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test1.jpg")));
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test2.jpg")));

        assertTrue(result1 instanceof XMLImageChunkBuilder);
        assertTrue(result2 instanceof XMLImageChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class XMLReportChunkBuilderFactory.
     * Test case: successful execution, passing of DATA Explanation chunk type
     */
    @Test
    public void testGetXMLReportChunkBuilderData1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

        assertTrue(result instanceof XMLDataChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class XMLReportChunkBuilderFactory.
     * Test case: successful execution, passing of  DATA Explanation chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetXMLReportChunkBuilderData2() {
        ReportChunkBuilder result1 = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));
        
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

        assertTrue(result1 instanceof XMLDataChunkBuilder);
        assertTrue(result2 instanceof XMLDataChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }
}
