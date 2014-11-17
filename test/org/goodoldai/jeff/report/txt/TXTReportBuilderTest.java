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
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import junit.framework.TestCase;

/**
 *
 * @author Nemanja Jovanovic
 */
public class TXTReportBuilderTest extends TestCase {

    TXTReportBuilder instance;
    Explanation explanation1;
    Explanation explanation2;
    Explanation explanation3;
    PrintWriter pw;
    BufferedReader br;

    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.dom4j.Document instances that are
     * used for testing
     */
    @Override
    protected void setUp() throws FileNotFoundException {

        instance = new TXTReportBuilder(new TXTReportChunkBuilderFactory());

        explanation3 = new Explanation();
        explanation1 = new Explanation("tester");

        explanation2 = new Explanation("tester", "EN", "USA");
        explanation2.addChunk(new TextExplanationChunk("textTest"));
        explanation2.addChunk(new ImageExplanationChunk(new ImageData("imgTest.jpg")));

        String[] tags = {"tag1", "tag2"};
        explanation2.addChunk(new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new SingleData(new Dimension("testName", "testUnit"), "value")));

        pw = new PrintWriter(new File("text.txt"));
        br = new BufferedReader(new FileReader("text.txt"));

    }

    @Override
    protected void tearDown() {
        new File("text.txt").delete();
    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    public void testInsertHeaderAllNullArguments() {
        try {
            instance.insertHeader(null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument
     */
    public void testInsertHeaderMissingFirstArgumant() {
        try {
            instance.insertHeader(null, pw);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'explanation' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    public void testInsertHeaderMissingSecondArgumant() {
        try {
            instance.insertHeader(explanation1, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    public void testInsertHeaderWrongTypeSecondArgumant() {
        try {
            instance.insertHeader(explanation1, "test");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' must be the type of java.io.PrintWriter";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has all elements.
     */
    public void testInsertHeader1() throws FileNotFoundException, IOException, IOException {
        instance.insertHeader(explanation2, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + Long.toString(explanation2.getCreated().getTimeInMillis()), br.readLine());
        assertEquals("Report owner is: tester", br.readLine());
        assertEquals("The language on wich report is created in is: EN", br.readLine());
        assertEquals("The country is: USA", br.readLine());

        //checks if anyting eles has been writen to file by mistake
        assertEquals(null, br.readLine());

    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that only one argument.
     */
    public void testInsertHeader2() throws FileNotFoundException, IOException {
        instance.insertHeader(explanation1, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + Long.toString(explanation1.getCreated().getTimeInMillis()), br.readLine());
        assertEquals("Report owner is: tester", br.readLine());

        //checks if anyting eles has been writen to file by mistake
        assertEquals(null, br.readLine());

    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has no arguments.
     */
    public void testInsertHeader3() throws FileNotFoundException, IOException {
        instance.insertHeader(explanation3, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + Long.toString(explanation3.getCreated().getTimeInMillis()), br.readLine());

        //checks if anyting eles has been writen to file by mistake
        assertEquals(null, br.readLine());
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    public void testBuildReportAllNullArguments() {
        try {
            instance.buildReport(null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument
     */
    public void testBuildReportMissingFirstArgumant() {
        try {
            instance.buildReport(null, "test.txt");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'explanation' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    public void testBuildReportMissingSecondArgumant() {
        try {
            instance.buildReport(explanation1, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'filepath' must not be null or empty string";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    public void testBuildReportEmptySecondArgumant() {
        try {
            instance.buildReport(explanation1, "");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'filepath' must not be null or empty string";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has all elements.
     */
    public void testBuildReport1() throws FileNotFoundException, IOException, IOException {
        instance.buildReport(explanation2, "text.txt");

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Report", br.readLine());

    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that only one argument.
     */
    public void testBuildReport2() throws FileNotFoundException, IOException {
        instance.buildReport(explanation1, "text.txt");

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Report", br.readLine());


    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has no arguments.
     */
    public void testBuildReport3() throws FileNotFoundException, IOException {
        instance.buildReport(explanation3, "text.txt");

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Report", br.readLine());

    }
}
