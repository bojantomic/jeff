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
package org.goodoldai.jeff.report.pdf;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import junit.framework.TestCase;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 * @author Bojan Tomic
 */
public class PDFReportChunkBuilderFactoryTest extends TestCase {

    //This inner class is introduced only for testing purposes
    //(type testing) and has no other function.
    private class UnknownExplanationChunk extends ExplanationChunk {

        private UnknownExplanationChunk(Object content) {
            super(content);
        }

        @Override
        public void setContent(Object val) {
        }

        @Override
        public ExplanationChunk clone() {
            return null;
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: unsuccessfull execution - chunk type not recognized
     */
    public void testGetReportChunkBuilderChunkTypeNotRecognized() {
        //This is an instance of an unsupported chunk type
        UnknownExplanationChunk echunk = new UnknownExplanationChunk("");

        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();

        try {
            instance.getReportChunkBuilder(echunk);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Chunk type '" + echunk.getClass().getName() +
                    "' was not recognized";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: unsuccessfull execution - null chunk
     */
    public void testGetReportChunkBuilderNullChunk() {
        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();

        try {
            instance.getReportChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a non-null chunk instance";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type
     */
    public void testGetExplanationChunkBuilderText1() {
        TextExplanationChunk tec = new TextExplanationChunk("sample text");
        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();
        ReportChunkBuilder result = instance.getReportChunkBuilder(tec);

        assertTrue(result instanceof PDFTextChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type, assert returning
     * of same instance every time
     */
    public void testGetExplanationChunkBuilderText2() {
        TextExplanationChunk tec = new TextExplanationChunk("sample text");
        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();
        ReportChunkBuilder result1 = instance.getReportChunkBuilder(tec);
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(tec);

        assertTrue(result1 instanceof PDFTextChunkBuilder);
        assertTrue(result2 instanceof PDFTextChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type
     */
    public void testGetExplanationChunkBuilderImage1() {
        ImageExplanationChunk iec =
                new ImageExplanationChunk(new ImageData("image.jpeg"));
        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();
        ReportChunkBuilder result = instance.getReportChunkBuilder(iec);

        assertTrue(result instanceof PDFImageChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type, assert returning
     * of same instance every time
     */
    public void testGetExplanationChunkBuilderImage2() {
        ImageExplanationChunk iec =
                new ImageExplanationChunk(new ImageData("image.jpeg"));
        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(iec);
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(iec);

        assertTrue(result1 instanceof PDFImageChunkBuilder);
        assertTrue(result2 instanceof PDFImageChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type
     */
    public void testGetExplanationChunkBuilderData1() {
        DataExplanationChunk dec =
                new DataExplanationChunk(
                new SingleData(new Dimension("money"), new Double(123)));
        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();
        ReportChunkBuilder result = instance.getReportChunkBuilder(dec);

        assertTrue(result instanceof PDFDataChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class PDFReportChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type, assert returning
     * of same instance every time
     */
    public void testGetExplanationChunkBuilderData2() {
        DataExplanationChunk dec =
                new DataExplanationChunk(
                new SingleData(new Dimension("money"), new Double(123)));
        PDFReportChunkBuilderFactory instance = new PDFReportChunkBuilderFactory();
        ReportChunkBuilder result1 = instance.getReportChunkBuilder(dec);
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(dec);

        assertTrue(result1 instanceof PDFDataChunkBuilder);
        assertTrue(result2 instanceof PDFDataChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }
}
