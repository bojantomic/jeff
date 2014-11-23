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

/**
 * @author Boris Horvat
 */
public class XMLUtilityTest extends AbstractJeffTest {

    TextExplanationChunk textEchunk1;
    TextExplanationChunk textEchunk2;

    Document document;

    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.dom4j.Document instances that are
     * used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String[] tags = {"tag1", "tag2"};

        textEchunk1 = new TextExplanationChunk("testing");
        textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

        document = DocumentHelper.createDocument();
        document.addElement("root");
    }

    @After
    public void tearDown() {

        textEchunk1 = null;
        textEchunk2 = null;
        
        document = null;
    }

    /**
     * Test of insertExplanationInfo method, of class XMLUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    @Test
    public void testInsertExplenationInfoFirstConstructor() {

        Element root = document.getRootElement();

        XMLChunkUtility.insertExplanationInfo(textEchunk1, root);

        //checks the number of attributes and elements
        assertEquals(1, root.attributes().size());
        assertEquals(0, root.elements().size());

        //checks the value of attribute
        for (Iterator it = root.attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals("INFORMATIONAL".toLowerCase(), attribute.getText());
        }
    }

    /**
     * Test of insertExplanationInfo method, of class XMLUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
    public void testInsertExplenationInfoSecondConstructor() {

        Element root = document.getRootElement();

        String[] names = {"rule", "group", "context"};
        String[] values = {"testRule", "testGroup", "error"};
        String[] tags = {"tag1", "tag2"};

        XMLChunkUtility.insertExplanationInfo(textEchunk2, root);

        //checks the number of attributes and elements
        assertEquals(3, root.attributes().size());
        assertEquals(1, root.elements().size());

        //checks the number of elements in the element "tags"
        assertEquals(2, root.element("tags").elements().size());

        //checks the values and names of attributes
        int i = 0;
        for (Iterator it = root.attributeIterator(); it.hasNext();) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }

        //checks the names of the sub elements
        for (Iterator it = root.elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tags", element.getName());
        }

        //checks the values and names of element "tags"
        int j = 0;
        for (Iterator it = root.element("tags").elementIterator(); it.hasNext();) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[j++], element.getText());
        }
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is not predefined
     */
    @Test
    public void testTranslateContextTypeNotRecognized() {
        int context = -555;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);

        assertEquals(String.valueOf(context), result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "INFORMATIONAL"
     */
    @Test
    public void testTranslateContextKnownTypeInformational() {
        int context = 0;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "INFORMATIONAL".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "WARNING"
     */
    @Test
    public void testTranslateContextKnownTypeInformationalWarning() {
        int context = -5;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "WARNING".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "ERROR"
     */
    @Test
    public void testTranslateContextKnownTypeError() {
        int context = -10;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "ERROR".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypePositive() {
        int context = 1;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryPositive() {
        int context = 2;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeNegative() {
        int context = -1;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryNegative() {
        int context = -2;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }


    
}
