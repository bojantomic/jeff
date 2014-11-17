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

import com.lowagie.text.pdf.PdfReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportBuilderTest;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 *
 * @author Bojan Tomic
 */
public class PDFReportBuilderTest extends ReportBuilderTest {

    private Explanation explanation;
    private String filePath;

    @Override
    public ReportBuilder getInstance(ReportChunkBuilderFactory factory) {
        return new PDFReportBuilder(factory);
    }

    @Override
    public ReportChunkBuilderFactory getFactory() {
        return new PDFReportChunkBuilderFactory();
    }

    @Override
    protected void setUp() {
        //Set up a sample explanation
        explanation = new Explanation("OWNER", "EN", "USA");
        explanation.addChunk(new TextExplanationChunk("textTest"));
        explanation.addChunk(new ImageExplanationChunk(new ImageData("WHALE.JPG")));

        //Set up a sample filepath
        filePath = "test.pdf";

        //Set up a PDFReportBuilder instance
        instance = getInstance(getFactory());
    }

    @Override
    protected void tearDown() {
        new File(filePath).delete();
    }

    /**
     * Test of buildReport method, of class PDFReportBuilder.
     * Test case: unsuccessfull execution - wrong type stream
     */
    public void testBuildReport_Explanation_ObjectWrongTypeStream() {
        try {
            java.io.PrintWriter stream = new java.io.PrintWriter(System.out);
            instance.buildReport(explanation, stream);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must be an OutputStream instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class PDFReportBuilder.
     * Test case: successfull execution
     * NOTE: At this point the test only verifies if the appropriate
     * file is created. The content of the file is not tested, only
     * the metadata inserted by the "insertHeader" method.
     */
    public void testBuildReport_Explanation_String() throws IOException {
        instance.buildReport(explanation, filePath);

        //Assert that the appropriate file was created
        assertTrue((new File(filePath)).exists());

        //Assert that the metadata is correct
        PdfReader reader = new PdfReader(filePath);
        Map metadata = reader.getInfo();
        assertEquals("JEFF (Java Explanation Facility Framework)", (String) (metadata.get("Creator")));
        assertEquals("OWNER [OWNER]", (String) (metadata.get("Author")));
    }
}
