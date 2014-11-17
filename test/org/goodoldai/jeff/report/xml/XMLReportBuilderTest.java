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

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import junit.framework.TestCase;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author Boris Horvat
 */
public class XMLReportBuilderTest extends TestCase {

    XMLReportBuilder builder;
    Explanation explanation1;
    Explanation explanation2;
    Document document1;
    Document document2;

    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.dom4j.Document instances that are
     * used for testing
     */
    @Override
    protected void setUp() {

        builder = new XMLReportBuilder(new XMLReportBuilderChunkFactory());

        explanation1 = new Explanation("tester");

        explanation2 = new Explanation("tester", "EN", "USA");
        explanation2.addChunk(new TextExplanationChunk("textTest"));
        explanation2.addChunk(new ImageExplanationChunk(new ImageData("imgTest.jpg")));

        String[] tags = {"tag1", "tag2"};
        explanation2.addChunk(new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new SingleData(new Dimension("testName", "testUnit"), "value")));

        document1 = DocumentHelper.createDocument();
        document1.addElement("root");

        document2 = DocumentHelper.createDocument();
    }

    @Override
    protected void tearDown() {
        new File("test.xml").delete();
    }

    /**
     * Test of buildReport method (this method takes the name of the file and
     * calls the other buildReport method of the same class), of class XMLReportBuilder.
     * Test case: successful building of explanation
     */
    public void testBuildReportMainMethod() throws DocumentException {
        builder.buildReport(explanation2, "test.xml");

        //checks if the file is created
        assertTrue(new File("test.xml").exists());

        SAXReader reader = new SAXReader();
        Document document = reader.read("test.xml");

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(4, root.attributes().size());
        assertEquals(3, root.elements().size());

        //checks the name of root element
        assertEquals("explanation", root.getName());

        //checks the values of attributes
        assertEquals("tester", root.attribute("owner").getText());
        assertEquals("EN", root.attribute("language").getText());
        assertEquals("USA", root.attribute("country").getText());
        assertEquals(String.valueOf(explanation2.getCreated().getTimeInMillis()), root.attribute("date").getText());

        //checks the names of elements which hold the explanation chunks which were tested in other mehtods and classes
        assertEquals("textualExplanation", root.element("textualExplanation").getName());
        assertEquals("imageExplanation", root.element("imageExplanation").getName());
        assertEquals("dataExplanation", root.element("dataExplanation").getName());

    }

    /**
     * Test of buildReport method (main method), of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    public void testBuildReportMainMethodMissingAllArgument() {
        try {
            builder.buildReport(null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method (main method), of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the First null argument
     */
    public void testBuildReportMainMethodMissingFirstArgument() {
        try {
            builder.buildReport(null, document1);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'explanation' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method (main method), of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    public void testBuildReportMainMethodMissingSecondArgument() {
        try {
            builder.buildReport(explanation1, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'filepath' must not be null or empty string";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method (main method), of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    public void testBuildReportMainMethodSecondArgumentEmptyString() {
        try {
            builder.buildReport(explanation1, "");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'filepath' must not be null or empty string";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class XMLReportBuilder.
     * Test case: successful building of explanation
     */
    public void testBuildReport() throws FileNotFoundException, DocumentException {
        builder.buildReport(explanation2, new PrintWriter(new File("test.xml")));

        SAXReader reader = new SAXReader();
        Document document = reader.read("test.xml");

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(4, root.attributes().size());
        assertEquals(3, root.elements().size());

        //checks the name of root element
        assertEquals("explanation", root.getName());

        //checks the values of attributes
        assertEquals("tester", root.attribute("owner").getText());
        assertEquals("EN", root.attribute("language").getText());
        assertEquals("USA", root.attribute("country").getText());
        assertEquals(String.valueOf(explanation2.getCreated().getTimeInMillis()), root.attribute("date").getText());

        //checks the names of elements which hold the explanation chunks which were tested in other mehtods and classes
        assertEquals("textualExplanation", root.element("textualExplanation").getName());
        assertEquals("imageExplanation", root.element("imageExplanation").getName());
        assertEquals("dataExplanation", root.element("dataExplanation").getName());

    }

    /**
     * Test of buildReport method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    public void testBuildReportMissingAllArgument() {
        try {
            builder.buildReport(null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the First null argument
     */
    public void testBuildReportMissingFirstArgument() {
        try {
            builder.buildReport(null, document1);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'explanation' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReport method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    public void testBuildReportMissingSecondArgument() {

        Object object = null;
        try {
            builder.buildReport(explanation1, object);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }


    /**
     * Test of buildReport method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    public void testBuildReportWrongTypeSecondArgument() {
        try {
            builder.buildReport(explanation1, new Object());
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' must be the type of java.io.PrintWriter";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }    

    /**
     * Test of insertHeader method, of class XMLReportBuilder.
     * Test case: successful building of a chunk, inserting header information,
     * using Explanation constructor that only has "owner" attribute.
     */
    public void testInsertHeader() {
        builder.insertHeader(explanation1, document1);

        Element root = document1.getRootElement();

        //checks the number of attributes and elements
        assertEquals(2, root.attributes().size());
        assertEquals(0, root.elements().size());

        //checks the values of attributes
        assertEquals("tester", root.attribute("owner").getText());
        assertEquals(String.valueOf(explanation1.getCreated().getTimeInMillis()), root.attribute("date").getText());
    }

    /**
     * Test of insertHeader method, of class XMLReportBuilder.
     * Test case: successful building of a chunk, inserting header information
     * using Explanation constructor that only has all the attributes.
     */
    public void testInsertHeaderAllInfo() {
        builder.insertHeader(explanation2, document1);

        Element root = document1.getRootElement();

        //checks the number of attributes and elements
        assertEquals(4, root.attributes().size());
        assertEquals(0, root.elements().size());

        //checks the values of attributes
        assertEquals("tester", root.attribute("owner").getText());
        assertEquals("EN", root.attribute("language").getText());
        assertEquals("USA", root.attribute("country").getText());
        assertEquals(String.valueOf(explanation2.getCreated().getTimeInMillis()), root.attribute("date").getText());


    }

    /**
     * Test of insertHeader method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    public void testInsertHeaderMissingAllArgument() {
        try {
            builder.insertHeader(null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the First null argument
     */
    public void testInsertHeaderMissingFirstArgument() {
        try {
            builder.insertHeader(null, document1);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'explanation' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of insertHeader method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    public void testInsertHeaderMissingSecondArgument() {
        try {
            builder.insertHeader(explanation1, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }


    /**
     * Test of insertHeader method, of class XMLReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    public void testInsertHeaderWrongTypeSecondArgument() {
        try {
            builder.insertHeader(explanation1, "test");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' must be the type of org.dom4j.Document";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
}
