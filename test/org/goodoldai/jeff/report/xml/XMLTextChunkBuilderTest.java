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

import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import java.util.Iterator;
import junit.framework.TestCase;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author Boris Horvat
 */
public class XMLTextChunkBuilderTest extends TestCase {

    TextExplanationChunk textEchunk1;
    TextExplanationChunk textEchunk2;
    XMLTextChunkBuilder xmlTextChunkBuilder;
    Document document;

    /**
     * Creates a explanation.TextExplanationChunk, and org.dom4j.Document instances
     * that are used for testing
     */
    @Override
    protected void setUp() {

        String[] tags = {"tag1", "tag2"};

        xmlTextChunkBuilder = new XMLTextChunkBuilder();

        textEchunk1 = new TextExplanationChunk("testing");
        textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

        document = DocumentHelper.createDocument();
        document.addElement("root");
    }

    @Override
    protected void tearDown() {

        xmlTextChunkBuilder = null;

        textEchunk1 = null;
        textEchunk2 = null;

        document = null;
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    public void testBuildReportChunkFirstConstructor() {
        xmlTextChunkBuilder.buildReportChunk(textEchunk1, document);

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(0, root.attributes().size());
        assertEquals(1, root.elements().size());

        //checks the name of element
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("textualExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "textualExplanation"
        assertEquals(1, root.element("textualExplanation").attributes().size());
        assertEquals(1, root.element("textualExplanation").elements().size());

        //checks the value of attribute of the element "textualExplanation"
        for (Iterator it = root.element("textualExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals("INFORMATIONAL".toLowerCase(), attribute.getText());
        }

        //checks the values and names of elements of the element "textualExplanation"
        for (Iterator it = root.element("textualExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("content", element.getName());
            assertEquals(textEchunk1.getContent(), element.getText());
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    public void testBuildReportChunkSecondConstructor() {
        xmlTextChunkBuilder.buildReportChunk(textEchunk2, document);

        Element root = document.getRootElement();

        //the expected values
        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};
        String[] elements = {"tags", "content"};

        //checks the number of attributes and elements
        assertEquals(0, root.attributes().size());
        assertEquals(1, root.elements().size());

        //checks the name of element
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("textualExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "textualExplanation"
        assertEquals(3, root.element("textualExplanation").attributes().size());
        assertEquals(2, root.element("textualExplanation").elements().size());
        assertEquals(2, root.element("textualExplanation").element("tags").elements().size());

        //checks the values of attributes of the element "textualExplanation"
        int i = 0;
        for (Iterator it = root.element("textualExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }

        //checks the values and names of elements of the element "textualExplanation"
        int j = 0;
        for (Iterator it = root.element("textualExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "tags"
        int k = 0;
        for (Iterator it = root.element("textualExplanation").element("tags").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[k++], element.getText());
        }

         //checks the values and names of elements of the element "content" (the content)
        assertEquals("test text", root.element("textualExplanation").element("content").getText());
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    public void testBuildReportChunkMissingAllArguments() {
        try {
            xmlTextChunkBuilder.buildReportChunk(null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument
     */
    public void testBuildReportChunkMissingFirstArgumant() {
        try {
            xmlTextChunkBuilder.buildReportChunk(null, document);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'echunk' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    public void testBuildReportChunkMissingSecondArgumant() {
        try {
            xmlTextChunkBuilder.buildReportChunk(textEchunk1, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the first argument
     */
    public void testBuildReportChunkWrongTypeFirsArgumant() {
        try {
            xmlTextChunkBuilder.buildReportChunk(new ImageExplanationChunk(new ImageData("testing.jpg")), document);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The ExplanationChunk must be the type of TextExplanationChunk";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLTextChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    public void testBuildReportChunkWrongTypeSecondArgumant() {
        try {
            xmlTextChunkBuilder.buildReportChunk(textEchunk1, "test");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The stream must be the type of org.dom4j.Document";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
}
