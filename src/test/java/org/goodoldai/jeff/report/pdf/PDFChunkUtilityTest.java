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

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Bojan Tomic
 */
public class PDFChunkUtilityTest extends AbstractJeffTest {

    ImageExplanationChunk ichunk = null;
    ImageData imagedata = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;
    Document doc = null;
    DummyDocListener docListener = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Create some sample ImageExplanationChunk data
        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule 1";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";

        imagedata = new ImageData("URL1", "Whale photo");

        //Create a sample ImageExplanationChunk
        ichunk = new ImageExplanationChunk(context, group, rule, tags, imagedata);

        //Create an empty document instance, open it and attach a dummy document
        //event listener to it.
        doc = new Document();
        doc.open();
        docListener = new DummyDocListener();
        doc.addDocListener(docListener);
    }

    @After
    public void tearDown() throws Exception {
        doc.close();
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: successfull execution - ERROR context
     */
    @Test
    public void testTranslateContextSuccessfull1() {
        String expResult = "ERROR";
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: successfull execution - INFORMATIONAL context
     */
    @Test
    public void testTranslateContextSuccessfull2() {
        ichunk.setContext(ExplanationChunk.INFORMATIONAL);
        String expResult = "INFORMATIONAL";
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: successfull execution - POSITIVE context
     */
    @Test
    public void testTranslateContextSuccessfull3() {
        ichunk.setContext(ExplanationChunk.POSITIVE);
        String expResult = "POSITIVE";
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: successfull execution - VERY POSITIVE context
     */
    @Test
    public void testTranslateContextSuccessfull4() {
        ichunk.setContext(ExplanationChunk.VERY_POSITIVE);
        String expResult = "VERY_POSITIVE";
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: successfull execution - NEGATIVE context
     */
    @Test
    public void testTranslateContextSuccessfull5() {
        ichunk.setContext(ExplanationChunk.NEGATIVE);
        String expResult = "NEGATIVE";
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: successfull execution - ALERT VERY NEGATIVE context
     */
    @Test
    public void testTranslateContextSuccessfull6() {
        ichunk.setContext(ExplanationChunk.VERY_NEGATIVE);
        String expResult = "VERY_NEGATIVE";
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: successfull execution - WARNING context
     */
    @Test
    public void testTranslateContextSuccessfull7() {
        ichunk.setContext(ExplanationChunk.WARNING);
        String expResult = "WARNING";
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class PDFChunkUtility.
     * Test case: unsuccessfull execution - no translation could be performed
     */
    @Test
    public void testTranslateContextUnsuccessfull() {
        ichunk.setContext(-2324);
        String expResult = null;
        String result = RTFChunkUtility.translateContext(ichunk);
        assertEquals(expResult, result);
    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: unsucessfull execution - null chunk
     */
    @Test
    public void testInsertChunkHeaderNullChunk() {

        try {
            RTFChunkUtility.insertChunkHeader(null, doc);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered chunk must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: unsucessfull execution - null document
     */
    @Test
    public void testInsertChunkHeaderNullDocument() {

        try {
            RTFChunkUtility.insertChunkHeader(ichunk, null);
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered document must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: sucessfull execution - all chunk elements present
     */
    @Test
    public void testInsertChunkHeaderSuccessfull1() {
        RTFChunkUtility.insertChunkHeader(ichunk, doc);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 4 paragraphs added - 4 events
        assertTrue(events.size() == 4);

        //Check the first event - chunk context added as paragraph
        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        //Check the second event - chunk group added as paragraph
        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        //Check the third event - chunk rule added as paragraph
        event = events.get(2);
        confirmParagraphAdded(event, "RULE: " + rule);

        //Check the fourth event - chunk tags added as paragraph
        event = events.get(3);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");
    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: sucessfull execution - context not translatable
     */
    @Test
    public void testInsertChunkHeaderSuccessfull2() {
        ichunk.setContext(-355);

        RTFChunkUtility.insertChunkHeader(ichunk, doc);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 4 paragraphs added - 4 events
        assertTrue(events.size() == 4);

        //Check the first event - chunk context added as paragraph
        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: -355");

        //Check the second event - chunk group added as paragraph
        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        //Check the third event - chunk rule added as paragraph
        event = events.get(2);
        confirmParagraphAdded(event, "RULE: " + rule);

        //Check the fourth event - chunk tags added as paragraph
        event = events.get(3);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");
    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: sucessfull execution - no group
     */
    @Test
    public void testInsertChunkHeaderSuccessfull3() {
        ichunk.setGroup(null);

        RTFChunkUtility.insertChunkHeader(ichunk, doc);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 3 paragraphs added - 3 events
        assertTrue(events.size() == 3);

        //Check the first event - chunk context added as paragraph
        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        //Check the second event - chunk rule added as paragraph
        event = events.get(1);
        confirmParagraphAdded(event, "RULE: " + rule);

        //Check the third event - chunk tags added as paragraph
        event = events.get(2);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");
    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: sucessfull execution - no rule
     */
    @Test
    public void testInsertChunkHeaderSuccessfull4() {
        ichunk.setRule(null);

        RTFChunkUtility.insertChunkHeader(ichunk, doc);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 3 paragraphs added - 3 events
        assertTrue(events.size() == 3);

        //Check the first event - chunk context added as paragraph
        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        //Check the second event - chunk group added as paragraph
        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        //Check the third event - chunk tags added as paragraph
        event = events.get(2);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");

    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: sucessfull execution - no tags
     */
    @Test
    public void testInsertChunkHeaderSuccessfull5() {
        ichunk.setTags(null);

        RTFChunkUtility.insertChunkHeader(ichunk, doc);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 3 paragraphs added - 3 events
        assertTrue(events.size() == 3);

        //Check the first event - chunk context added as paragraph
        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        //Check the second event - chunk group added as paragraph
        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        //Check the third event - chunk rule added as paragraph
        event = events.get(2);
        confirmParagraphAdded(event, "RULE: " + rule);
    }

    /**
     * Test of insertChunkHeader method, of class PDFChunkUtility.
     * Test case: sucessfull execution - no chunk elements besides context
     */
    @Test
    public void testInsertChunkHeaderSuccessfull6() {
        ichunk.setGroup(null);
        ichunk.setRule(null);
        ichunk.setTags(null);

        RTFChunkUtility.insertChunkHeader(ichunk, doc);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there has been 1 paragraph added - 1 event
        assertTrue(events.size() == 1);

        //Check the first event - chunk context added as paragraph
        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");
    }

    /**
     * This method checks that the dummy event really represents a
     * Paragraph instance added to the Document
     *
     * @param event triggered event
     * @param content expected textual content of the paragraph
     */
    private void confirmParagraphAdded(Object[] event, String content) {
        //Two parameters for this event
        assertTrue(event.length == 2);
        //First event parameter should be the event name
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
        //Second event parameter should be a Paragraph object
        Paragraph eventArgument = (Paragraph) (event[1]);
        assertEquals(content, eventArgument.getContent());
    }
}
