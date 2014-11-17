/*
 * Copyright 2009 Nemanja Jovanovic
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
package org.goodoldai.jeff.report.txt;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import junit.framework.TestCase;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 *
 * @author Nemanja Jovanovic
 */
public class TXTReportChunkBuilderFactoryTest extends TestCase {
   TXTReportChunkBuilderFactory instance;

    @Override
    protected void setUp(){
        instance = new TXTReportChunkBuilderFactory();
    }

    @Override
    protected void tearDown(){
        instance = null;
    }

    /**
     * Test of getReportChunkBuilder method, of class TXTReportBuilderChunkFactory.
     * Test case: unsuccessful execution - chunk type not recognized
     */
    public void testGetTXTReportChunkBuilderTypeNotRecognized() {
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
     * Test of getReportChunkBuilder method, of class TXTReportBuilderChunkFactory.
     * Test case: successful execution, passing of TEXT Explanation Chunk type
     */
    public void testGetTXTReportChunkBuilderText1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(new TextExplanationChunk("testText"));

        assertTrue(result instanceof TXTTextChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class TXTReportBuilderChunkFactory.
     * Test case: successful execution, passing of TEXT Explanation chunk type, assert returning
     * of same instance every time
     */
    public void testGetTXTReportChunkBuilderText2() {

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(new TextExplanationChunk("testText1"));
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(new TextExplanationChunk("testText2"));

        assertTrue(result1 instanceof TXTTextChunkBuilder);
        assertTrue(result2 instanceof TXTTextChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class TXTReportBuilderChunkFactory.
     * Test case: successful execution, passing of IMAGE Explanation chunk type
     */
    public void testGetTXTReportChunkBuilderImage1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test.jpg")));

        assertTrue(result instanceof TXTImageChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class TXTReportBuilderChunkFactory.
     * Test case: successful execution, passing of  IMAGE Explanation chunk type, assert returning
     * of same instance every time
     */
    public void testGetTXTReportChunkBuilderImage2() {

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test1.jpg")));
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test2.jpg")));

        assertTrue(result1 instanceof TXTImageChunkBuilder);
        assertTrue(result2 instanceof TXTImageChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class TXTReportBuilderChunkFactory.
     * Test case: successful execution, passing of DATA Explanation chunk type
     */
    public void testGetTXTReportChunkBuilderData1() {

        ReportChunkBuilder result = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

        assertTrue(result instanceof TXTDataChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class TXTReportBuilderChunkFactory.
     * Test case: successful execution, passing of  DATA Explanation chunk type, assert returning
     * of same instance every time
     */
    public void testGetTXTReportChunkBuilderData2() {
        ReportChunkBuilder result1 = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

        ReportChunkBuilder result2 = instance.getReportChunkBuilder(
                new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

        assertTrue(result1 instanceof TXTDataChunkBuilder);
        assertTrue(result2 instanceof TXTDataChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

}
