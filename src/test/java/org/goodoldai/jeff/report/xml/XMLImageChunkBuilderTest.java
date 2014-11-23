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
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import java.util.Iterator;
import junit.framework.TestCase;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Boris Horvat
 */
public class XMLImageChunkBuilderTest extends AbstractJeffTest {

    ImageExplanationChunk imageEchunk1;
    ImageExplanationChunk imageEchunk2;
    XMLImageChunkBuilder xmlImageChunkBuilder;
    Document document;
    
    /**
     * Creates a explanation.ImageExplanationChunk, and org.dom4j.Document instances
     * that are used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String[] tags = {"tag1", "tag2"};

        xmlImageChunkBuilder = new XMLImageChunkBuilder();

        imageEchunk1 = new ImageExplanationChunk(new ImageData("picture.jpg"));        
        imageEchunk2 = new ImageExplanationChunk(-10, "testGroup", "testRule", tags, new ImageData("picture.jpg", "picture"));

        document = DocumentHelper.createDocument();
        document.addElement("root");
    }

    @After
    public void tearDown() {

        xmlImageChunkBuilder = null;

        imageEchunk1 = null;
        imageEchunk2 = null;

        document = null;
       
    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testBuildReportChunkFirstConstructor() {
        xmlImageChunkBuilder.buildReportChunk(imageEchunk1, document, true);

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(0, root.attributes().size());
        assertEquals(1, root.elements().size());

        //checks the name of element
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("imageExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "imageExplanation"
        assertEquals(1, root.element("imageExplanation").attributes().size());
        assertEquals(1, root.element("imageExplanation").elements().size());

        //checks the value of attribute of the element "imageExplanation"
        for (Iterator it = root.element("imageExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals("INFORMATIONAL".toLowerCase(), attribute.getText());
        }

        //checks the values and names of elements of the element "imageExplanation"
        for (Iterator it = root.element("imageExplanation").element("content").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(0, element.attributes().size());
            assertEquals("imageUrl", element.getName());
            assertEquals("picture.jpg", element.getText());
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
    public void testBuildReportChunkSecondConstructor() {
        xmlImageChunkBuilder.buildReportChunk(imageEchunk2, document, true);

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
            assertEquals("imageExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "imageExplanation"
        assertEquals(3, root.element("imageExplanation").attributes().size());
        assertEquals(2, root.element("imageExplanation").elements().size());
        assertEquals(2, root.element("imageExplanation").element("tags").elements().size());

        //checks the values of attributes of the element "imageExplanation"
        int i = 0;
        for (Iterator it = root.element("imageExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }

        //checks the values and names of elements of the element "imageExplanation"
        int j = 0;
        for (Iterator it = root.element("imageExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "tags"
        int k = 0;
        for (Iterator it = root.element("imageExplanation").element("tags").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[k++], element.getText());
        }

        //checks the values and names of elements of the element "imageExplanation" (the content)
        assertEquals(1, root.element("imageExplanation").element("content").element("imageUrl").attributes().size());
        assertEquals("picture", root.element("imageExplanation").element("content").element("imageUrl").attribute("caption").getText());
        assertEquals("picture.jpg", root.element("imageExplanation").element("content").element("imageUrl").getText());

    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements - but with no chunk headers inserted.
     */
    @Test
    public void testBuildReportChunkSecondConstructorWithNoChunkHeaders() {
        xmlImageChunkBuilder.buildReportChunk(imageEchunk2, document, false);

        Element root = document.getRootElement();

        //the expected values
        String[] elements = {"content"};

        //checks the number of attributes and elements
        assertEquals(0, root.attributes().size());
        assertEquals(1, root.elements().size());

        //checks the name of element
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("imageExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "imageExplanation"
        assertEquals(0, root.element("imageExplanation").attributes().size());
        assertEquals(1, root.element("imageExplanation").elements().size());

        //checks the values and names of elements of the element "imageExplanation"
        int j = 0;
        for (Iterator it = root.element("imageExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "imageExplanation" (the content)
        assertEquals(1, root.element("imageExplanation").element("content").element("imageUrl").attributes().size());
        assertEquals("picture", root.element("imageExplanation").element("content").element("imageUrl").attribute("caption").getText());
        assertEquals("picture.jpg", root.element("imageExplanation").element("content").element("imageUrl").getText());

    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testBuildReportChunkMissingAllArguments() {
        try {
            xmlImageChunkBuilder.buildReportChunk(null, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument
     */
    @Test
    public void testBuildReportChunkMissingFirstArgumant() {
        try {
            xmlImageChunkBuilder.buildReportChunk(null, document, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'echunk' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    @Test
    public void testBuildReportChunkMissingSecondArgumant() {
        try {
            xmlImageChunkBuilder.buildReportChunk(imageEchunk1, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the first argument
     */
    @Test
    public void testBuildReportChunkWrongTypeFirsArgumant() {
        try {
            xmlImageChunkBuilder.buildReportChunk(new TextExplanationChunk("testing.jpg"), document, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The ExplanationChunk must be type of ImageExplanationChunk";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLImageChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testBuildReportChunkWrongTypeSecondArgumant() {
        try {
            xmlImageChunkBuilder.buildReportChunk(imageEchunk1, "test", false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The stream must be the type of org.dom4j.Document";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
}
