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
import java.text.DateFormat;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportBuilderTest;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Nemanja Jovanovic
 */
public class TXTReportBuilderTest extends ReportBuilderTest {

    Explanation explanation1;
    Explanation explanation2;
    Explanation explanation3;
    PrintWriter pw;
    BufferedReader br;

    @Override
    public ReportBuilder getInstance(ReportChunkBuilderFactory factory) {
        return new TXTReportBuilder(factory);
    }

    @Override
    public ReportChunkBuilderFactory getFactory() {
        return new TXTReportChunkBuilderFactory();
    }
    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.dom4j.Document instances that are
     * used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        instance = getInstance(getFactory());

        explanation3 = new Explanation();
        explanation1 = new Explanation("tester");

        explanation2 = new Explanation("tester", "EN", "USA", "Explanation title");
        explanation2.addChunk(new TextExplanationChunk("textTest"));
        explanation2.addChunk(new ImageExplanationChunk(new ImageData("imgTest.jpg")));

        String[] tags = {"tag1", "tag2"};
        explanation2.addChunk(new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new SingleData(new Dimension("testName", "testUnit"), "value")));

        try{
            pw = new PrintWriter(new File("text.txt"));
            br = new BufferedReader(new FileReader("text.txt"));
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @After
    public void tearDown() throws IOException{
        instance = null;

        pw.close();
        br.close();

        new File("text.txt").delete();
    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testInsertHeaderAllNullArguments() {
        try {
            ((TXTReportBuilder)instance).insertHeader(null, null);
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
    @Test
    public void testInsertHeaderMissingFirstArgument() {
        try {
            ((TXTReportBuilder)instance).insertHeader(null, pw);
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
    @Test
    public void testInsertHeaderMissingSecondArgument() {
        try {
            ((TXTReportBuilder)instance).insertHeader(explanation1, null);
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
    @Test
    public void testInsertHeaderWrongTypeSecondArgument() {
        try {
            ((TXTReportBuilder)instance).insertHeader(explanation1, "test");
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
    @Test
    public void testInsertHeader1() throws FileNotFoundException, IOException, IOException {
        ((TXTReportBuilder)instance).insertHeader(explanation2, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + DateFormat.getInstance().format(explanation2.getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: tester", br.readLine());
        assertEquals("The language used: EN", br.readLine());
        assertEquals("The country is: USA", br.readLine());
        assertEquals("", br.readLine());
        assertEquals("Explanation title", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());

    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that only one argument.
     */
    @Test
    public void testInsertHeader2() throws FileNotFoundException, IOException {
        ((TXTReportBuilder)instance).insertHeader(explanation1, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + DateFormat.getInstance().format(explanation2.getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: tester", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());

    }

    /**
     * Test of insertHeader method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has no arguments.
     */
    @Test
    public void testInsertHeader3() throws FileNotFoundException, IOException {
        ((TXTReportBuilder)instance).insertHeader(explanation3, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + DateFormat.getInstance().format(explanation2.getCreated().getTime()), br.readLine());
        assertEquals("", br.readLine());
        
        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has all elements - together with chunk headers.
     */
    @Test
    public void testBuildReport1() throws FileNotFoundException, IOException, IOException {
        instance.setInsertChunkHeaders(true);
        instance.buildReport(explanation2, "text.txt");

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + DateFormat.getInstance().format(explanation2.getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: tester", br.readLine());
        assertEquals("The language used: EN", br.readLine());
        assertEquals("The country is: USA", br.readLine());
        assertEquals("", br.readLine());
        assertEquals("Explanation title", br.readLine());
        assertEquals("", br.readLine());
        
        //check first chunk - TXT chunk
        //check chunk header
        //checks context
        assertEquals("The context is: informational", br.readLine());
        
        //check chunk content
        assertEquals("textTest", br.readLine());

        //Skip a line
        assertEquals("", br.readLine());

        //check second chunk - Image chunk
        //checks context
        assertEquals("The context is: informational", br.readLine());

        //check chunk content
        assertEquals("The path to this image is: imgTest.jpg", br.readLine());

        //Skip a line
        assertEquals("", br.readLine());

        //check third chunk - Data chunk
        //check chunk header
        //checks context
        assertEquals("The context is: error", br.readLine());
        //checks the rule
        assertEquals("The rule that initiated the creation of this chunk: testRule", br.readLine());
        //checks the group
        assertEquals("The group to which the executed rule belongs: testGroup", br.readLine());
        //checks the tags
        assertEquals("The tags are: tag1 tag2 ", br.readLine());

        //check chunk content
        assertEquals("testName [testUnit]", br.readLine());
        assertEquals("-------------------", br.readLine());
        assertEquals("value", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has all elements - buut with no chunk headers.
     */
    @Test
    public void testBuildReport1WithNoHeaders() throws FileNotFoundException, IOException, IOException {
        instance.setInsertChunkHeaders(false);
        instance.buildReport(explanation2, "text.txt");

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + DateFormat.getInstance().format(explanation2.getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: tester", br.readLine());
        assertEquals("The language used: EN", br.readLine());
        assertEquals("The country is: USA", br.readLine());
        assertEquals("", br.readLine());
        assertEquals("Explanation title", br.readLine());
        assertEquals("", br.readLine());

        //check first chunk - TXT chunk
        //check chunk content
        assertEquals("textTest", br.readLine());

        //Skip a line
        assertEquals("", br.readLine());

        //check second chunk - Image chunk
        //check chunk content
        assertEquals("The path to this image is: imgTest.jpg", br.readLine());

        //Skip a line
        assertEquals("", br.readLine());

        //check third chunk - Data chunk
        //check chunk content
        assertEquals("testName [testUnit]", br.readLine());
        assertEquals("-------------------", br.readLine());
        assertEquals("value", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());
    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that only one argument.
     */
    @Test
    public void testBuildReport2() throws FileNotFoundException, IOException {
        instance.buildReport(explanation1, "text.txt");

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

       //checks the data
        assertEquals("Creation date: " + DateFormat.getInstance().format(explanation1.getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: tester", br.readLine());
        assertEquals("", br.readLine());

        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());


    }

    /**
     * Test of buildReport method, of class TXTReportBuilder.
     * Test case: successful insertion of data using the Explanation constructor
     * that has no arguments.
     */
    @Test
    public void testBuildReport3() throws FileNotFoundException, IOException {
        instance.buildReport(explanation3, "text.txt");

        //checks if the file is created
        assertTrue(new File("text.txt").exists());

        //checks the data
        assertEquals("Creation date: " + DateFormat.getInstance().format(explanation3.getCreated().getTime()), br.readLine());
         assertEquals("", br.readLine());

        //checks if anyting else has been writen to file by mistake
        assertEquals(null, br.readLine());

    }
}
