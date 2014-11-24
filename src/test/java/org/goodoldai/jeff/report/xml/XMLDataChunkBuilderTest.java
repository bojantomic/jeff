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
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import java.util.ArrayList;
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
public class XMLDataChunkBuilderTest extends AbstractJeffTest {

    DataExplanationChunk singleDataChunk1;
    DataExplanationChunk singleDataChunk2;
    DataExplanationChunk oneDimDataChunk;
    DataExplanationChunk twoDimDataChunk;
    DataExplanationChunk threeDimDataChunk;
    XMLDataChunkBuilder xmlDataChunkBuilder;
    Document document;

    /**
     * Creates a explanation.DataExplanationChunk, and org.dom4j.Document instances
     * that are used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        xmlDataChunkBuilder = new XMLDataChunkBuilder();

        String[] tags = {"tag1", "tag2"};
        singleDataChunk1 = new DataExplanationChunk(new SingleData(new Dimension("testName"), "value"));
        singleDataChunk2 = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new SingleData(new Dimension("testName", "testUnit"), "value"));

        ArrayList<Object> values = new ArrayList<Object>();
        values.add("value1");
        values.add("value2");
        oneDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new OneDimData(new Dimension("testName", "testUnit"), values));


        ArrayList<Tuple> tupleValues = new ArrayList<Tuple>();
        tupleValues.add(new Tuple("value1", "value2"));
        twoDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new TwoDimData(new Dimension("testName1", "testUnit1"), new Dimension("testName2", "testUnit2"), tupleValues));

        ArrayList<Triple> tripleValues = new ArrayList<Triple>();
        tripleValues.add(new Triple("value1", "value2", "value3"));
        threeDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags, new ThreeDimData(
                new Dimension("testName1", "testUnit1"),
                new Dimension("testName2", "testUnit2"),
                new Dimension("testName3", "testUnit3"),
                tripleValues));

        document = DocumentHelper.createDocument();
        document.addElement("root");
    }

    @After
    public void tearDown() {

        xmlDataChunkBuilder = null;

        singleDataChunk1 = null;
        singleDataChunk2 = null;
        oneDimDataChunk = null;
        twoDimDataChunk = null;
        threeDimDataChunk = null;

        document = null;

    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testBuildReportChunkFirsttConstructorSingleData() {
        xmlDataChunkBuilder.buildReportChunk(singleDataChunk1, document, true);

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(0, root.attributes().size());
        assertEquals(1, root.elements().size());

        //checkschecks  the name of element
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("dataExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "dataExplanation"
        assertEquals(1, root.element("dataExplanation").attributes().size());
        assertEquals(1, root.element("dataExplanation").elements().size());

        //checks the value of attribute of the element "dataExplanation"
        for (Iterator it = root.element("dataExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals("INFORMATIONAL".toLowerCase(), attribute.getText());
        }

        //checks the values and names of elements of the element "dataExplanation"
        for (Iterator it = root.element("dataExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(1, element.attributes().size());
            assertEquals("testName", element.attribute("dimensionName").getText());
            assertEquals("content", element.getName());
            assertEquals("value", element.getText());
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is SingleDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorSingleData() {
        xmlDataChunkBuilder.buildReportChunk(singleDataChunk2, document, true);

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
            assertEquals("dataExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "dataExplanation"
        assertEquals(3, root.element("dataExplanation").attributes().size());
        assertEquals(2, root.element("dataExplanation").elements().size());
        assertEquals(2, root.element("dataExplanation").element("tags").elements().size());

        //checks the values of attributes of the element "dataExplanation"
        int i = 0;
        for (Iterator it = root.element("dataExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }

        //checks the values and names of elements of the element "dataExplanation"
        int j = 0;
        for (Iterator it = root.element("dataExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "tags"
        int k = 0;
        for (Iterator it = root.element("dataExplanation").element("tags").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[k++], element.getText());
        }

        //checks the values and names of elements of the element "dataExplanation" (the content)
        assertEquals(2, root.element("dataExplanation").element("content").attributes().size());
        assertEquals(0, root.element("dataExplanation").element("content").elements().size());

        //checks the values and names of elements of the element "value" (the content)
        assertEquals("testName", root.element("dataExplanation").element("content").attribute("dimensionName").getText());
        assertEquals("testUnit", root.element("dataExplanation").element("content").attribute("dimensionUnit").getText());
        assertEquals("content", root.element("dataExplanation").element("content").getName());
        assertEquals("value", root.element("dataExplanation").element("content").getText());


    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is SingleDimDataChunk - but no chunk
     * headers are inserted.
     */
    @Test
    public void testBuildReportChunkSecondConstructorSingleDataNoChunkHeaders() {
        xmlDataChunkBuilder.buildReportChunk(singleDataChunk2, document, false);

        Element root = document.getRootElement();

        //the expected values
        String[] elements = {"content"};

        //checks the number of attributes and elements
        assertEquals(0, root.attributes().size());
        assertEquals(1, root.elements().size());

        //checks the name of element
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("dataExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "dataExplanation"
        assertEquals(0, root.element("dataExplanation").attributes().size());
        assertEquals(1, root.element("dataExplanation").elements().size());
      
        //checks the values and names of elements of the element "dataExplanation"
        int j = 0;
        for (Iterator it = root.element("dataExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "dataExplanation" (the content)
        assertEquals(2, root.element("dataExplanation").element("content").attributes().size());
        assertEquals(0, root.element("dataExplanation").element("content").elements().size());

        //checks the values and names of elements of the element "value" (the content)
        assertEquals("testName", root.element("dataExplanation").element("content").attribute("dimensionName").getText());
        assertEquals("testUnit", root.element("dataExplanation").element("content").attribute("dimensionUnit").getText());
        assertEquals("content", root.element("dataExplanation").element("content").getName());
        assertEquals("value", root.element("dataExplanation").element("content").getText());


    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is OneDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorOneDimData() {
        xmlDataChunkBuilder.buildReportChunk(oneDimDataChunk, document, true);

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
            assertEquals("dataExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "dataExplanation"
        assertEquals(3, root.element("dataExplanation").attributes().size());
        assertEquals(2, root.element("dataExplanation").elements().size());
        assertEquals(2, root.element("dataExplanation").element("tags").elements().size());

        //checks the values of attributes of the element "dataExplanation"
        int i = 0;
        for (Iterator it = root.element("dataExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }

        //checks the values and names of elements of the element "dataExplanation"
        int j = 0;
        for (Iterator it = root.element("dataExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "tags"
        int k = 0;
        for (Iterator it = root.element("dataExplanation").element("tags").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[k++], element.getText());
        }

        //checks the number of elements and attributes of the element "values" (the content)
        assertEquals(2, root.element("dataExplanation").element("content").elements().size());
        assertEquals(2, root.element("dataExplanation").element("content").attributes().size());
        assertEquals("testName", root.element("dataExplanation").element("content").attribute("dimensionName").getText());
        assertEquals("testUnit", root.element("dataExplanation").element("content").attribute("dimensionUnit").getText());

        //the expected values of the content
        String[] oneDimValues = {"value1", "value2"};

        //checks the values and names of elements of the element "tupleValue" (the content)
        int l = 0;
        for (Iterator it = root.element("dataExplanation").element("content").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("value", element.getName());
            assertEquals(oneDimValues[l++], element.getText());
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is TwoDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorTwoDimData() {
        xmlDataChunkBuilder.buildReportChunk(twoDimDataChunk, document, true);

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
            assertEquals("dataExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "dataExplanation"
        assertEquals(3, root.element("dataExplanation").attributes().size());
        assertEquals(2, root.element("dataExplanation").elements().size());
        assertEquals(2, root.element("dataExplanation").element("tags").elements().size());

        //checks the values of attributes of the element "dataExplanation"
        int i = 0;
        for (Iterator it = root.element("dataExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }

        //checks the values and names of elements of the element "dataExplanation"
        int j = 0;
        for (Iterator it = root.element("dataExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "tags"
        int k = 0;
        for (Iterator it = root.element("dataExplanation").element("tags").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[k++], element.getText());
        }

        //checks the number of elements and attributes of the element "values" (the content)
        assertEquals(1, root.element("dataExplanation").element("content").elements().size());
        assertEquals(0, root.element("dataExplanation").element("content").attributes().size());

        assertEquals(2, root.element("dataExplanation").element("content").element("tupleValue").elements().size());

        //the expected values of the content
        String[] oneDimValues = {"value1", "value2"};
        String[] oneDimEllName = {"value1", "value2"};
        String[] oneDimAttName = {"testName1", "testName2"};
        String[] oneDimAttUnit = {"testUnit1", "testUnit2"};

        //checks the values and names of elements of the element "tupleValue" (the content)
        int l = 0;
        for (Iterator it = root.element("dataExplanation").element("content").element("tupleValue").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(2, element.attributes().size());

            assertEquals(oneDimEllName[l], element.getName());
            assertEquals(oneDimValues[l], element.getText());

            assertEquals(oneDimAttName[l], element.attribute("dimensionName").getText());
            assertEquals(oneDimAttUnit[l++], element.attribute("dimensionUnit").getText());
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: successfull insertion of data using the ExplanationChunk constructor
     * that has all elements and the type is ThreeDimDataChunk.
     */
    @Test
    public void testBuildReportChunkSecondConstructorThreeDimData() {
        xmlDataChunkBuilder.buildReportChunk(threeDimDataChunk, document, true);

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
            assertEquals("dataExplanation", element.getName());
        }

        //checks the number of attributes and elements of the element "dataExplanation"
        assertEquals(3, root.element("dataExplanation").attributes().size());
        assertEquals(2, root.element("dataExplanation").elements().size());
        assertEquals(2, root.element("dataExplanation").element("tags").elements().size());

        //checks the values of attributes of the element "dataExplanation"
        int i = 0;
        for (Iterator it = root.element("dataExplanation").attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }

        //checks the values and names of elements of the element "dataExplanation"
        int j = 0;
        for (Iterator it = root.element("dataExplanation").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(elements[j++], element.getName());
        }

        //checks the values and names of elements of the element "tags"
        int k = 0;
        for (Iterator it = root.element("dataExplanation").element("tags").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[k++], element.getText());
        }

        //checks the number of elements and attributes of the element "values" (the content)
        assertEquals(1, root.element("dataExplanation").element("content").elements().size());
        assertEquals(0, root.element("dataExplanation").element("content").attributes().size());

        assertEquals(3, root.element("dataExplanation").element("content").element("tripleValue").elements().size());

        //the expected values of the content
        String[] oneDimValues = {"value1", "value2", "value3"};
        String[] oneDimEllName = {"value1", "value2", "value3"};
        String[] oneDimAttName = {"testName1", "testName2", "testName3"};
        String[] oneDimAttUnit = {"testUnit1", "testUnit2", "testUnit3"};

        //checks the values and names of elements of the element "tripleValue" (the content)
        int l = 0;
        for (Iterator it = root.element("dataExplanation").element("content").element("tripleValue").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals(2, element.attributes().size());

            assertEquals(oneDimEllName[l], element.getName());
            assertEquals(oneDimValues[l], element.getText());

            assertEquals(oneDimAttName[l], element.attribute("dimensionName").getText());
            assertEquals(oneDimAttUnit[l++], element.attribute("dimensionUnit").getText());
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments
     */
    @Test
    public void testBuildReportChunkMissingAllArguments() {
        try {
            xmlDataChunkBuilder.buildReportChunk(null, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "All of the arguments are mandatory, so they can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the First null argument
     */
    @Test
    public void testBuildReportChunkMissingFirsttArgument() {
        try {
            xmlDataChunkBuilder.buildReportChunk(null, document, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'echunk' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument
     */
    @Test
    public void testBuildReportChunkMissingSecondArgument() {
        try {
            xmlDataChunkBuilder.buildReportChunk(singleDataChunk1, null, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument 'stream' is mandatory, so it can not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the Firstt argument
     */
    @Test
    public void testBuildReportChunkWrongTypeFirstArgument() {
        try {
            xmlDataChunkBuilder.buildReportChunk(new TextExplanationChunk("testing.jpg"), document, false);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The ExplanationChunk must be type of DataExplanationChunk";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class XMLDataChunkBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test
    public void testBuildReportChunkWrongTypeSecondArgument() {
        try {
            xmlDataChunkBuilder.buildReportChunk( singleDataChunk2, "test", false );
            fail( "Exception should have been thrown, but it wasn't" );
        } catch ( Exception e ) {
            String result = e.getMessage();
            String expResult = "The stream must be the type of org.dom4j.Document";
            assertTrue( e instanceof org.goodoldai.jeff.explanation.ExplanationException );
            assertEquals( expResult, result );
        }
    }
}
