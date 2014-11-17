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
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import java.util.ArrayList;
import junit.framework.TestCase;

/**
 * @author Bojan Tomic
 */
public class PDFTextChunkBuilderTest extends TestCase {

    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;
    String content = null;
    TextExplanationChunk tchunk = null;
    Document doc = null;
    DummyDocListener docListener = null;
    PDFTextChunkBuilder instance = null;

    @Override
    protected void setUp() throws Exception {
        //Create some sample TextExplanationChunk data
        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule3";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";

        content = "Explanation text";

        //Create a sample TextExplanationChunk
        tchunk = new TextExplanationChunk(context, group, rule, tags, content);

        //Create an empty document instance, open it and attach a dummy document
        //event listener to it.
        doc = new Document();
        doc.open();
        docListener = new DummyDocListener();
        doc.addDocListener(docListener);

        //Initialize the builder
        instance = new PDFTextChunkBuilder();
    }

    @Override
    protected void tearDown() throws Exception {
        doc.close();
    }

    /**
     * Test of buildReportChunk method, of class PDFTextChunkBuilder.
     * Test case: unsuccessfull execution - null chunk
     */
    public void testBuildReportChunkNullChunk() {

        try {
            instance.buildReportChunk(null, doc, false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered chunk must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFTextChunkBuilder.
     * Test case: unsuccessfull execution - null stream
     */
    public void testBuildReportChunkNullStream() {

        try {
            instance.buildReportChunk(tchunk, null, false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFTextChunkBuilder.
     * Test case: unsuccessfull execution - wrong type chunk
     */
    public void testBuildReportChunkWrongTypeChunk() {
        ImageExplanationChunk ichunk =
                new ImageExplanationChunk(new ImageData("pic.jpg"));

        try {
            instance.buildReportChunk(ichunk, doc, false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered chunk must be a TextExplanationChunk instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFTextChunkBuilder.
     * Test case: unsuccessfull execution - wrong type stream
     */
    public void testBuildReportChunkWrongTypeStream() {

        try {
            instance.buildReportChunk(tchunk, new Object(), false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must be a com.lowagie.text.Document instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFTextChunkBuilder.
     * Test case: successfull execution
     */
    public void testBuildReportChunkSuccessfull() {
        instance.buildReportChunk(tchunk, doc, true);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 5 paragraphs added - 5 events
        assertTrue(events.size() == 5);

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

        //Check the fifth event - chunk content added as paragraph
        event = events.get(4);
        confirmParagraphAdded(event, content);

    }

    /**
     * Test of buildReportChunk method, of class PDFTextChunkBuilder.
     * Test case: successfull execution but no chunk headers inserted
     */
    public void testBuildReportChunkSuccessfull2() {
        instance.buildReportChunk(tchunk, doc, false);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there has been 1 paragraph added - 1 event
        assertTrue(events.size() == 1);

        //Check the first event - chunk content added as paragraph
        Object[] event = events.get(0);
        confirmParagraphAdded(event, content);

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
