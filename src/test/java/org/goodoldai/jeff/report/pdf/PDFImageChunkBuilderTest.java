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
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
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
public class PDFImageChunkBuilderTest extends AbstractJeffTest {
    
    ImageExplanationChunk ichunk = null;
    ImageData imagedata = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;

    Document doc = null;
    DummyDocListener docListener = null;

    RTFImageChunkBuilder instance = null;

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

        //This image must be present somewhere in the classpath in order
        //for tests to work
        imagedata = new ImageData("/WHALE.JPG", "Whale photo");

        //Create a sample ImageExplanationChunk
        ichunk = new ImageExplanationChunk(context, group, rule, tags, imagedata);

        //Create an empty document instance, open it and attach a dummy document
        //event listener to it.
        doc = new Document();
        doc.open();
        docListener = new DummyDocListener();
        doc.addDocListener(docListener);

        //Initialize the builder
        instance = new RTFImageChunkBuilder();
    }

    @After
    public void tearDown() throws Exception {
        doc.close();
    }

    /**
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: unsuccessfull execution - null chunk
     */
    @Test
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
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: unsuccessfull execution - null stream
     */
    @Test
    public void testBuildReportChunkNullStream() {

        try {
            instance.buildReportChunk(ichunk, null, false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: unsuccessfull execution - wrong type chunk
     */
    @Test
    public void testBuildReportChunkWrongTypeChunk() {
        TextExplanationChunk tchunk =
                new TextExplanationChunk("Sample text");

        try {
            instance.buildReportChunk(tchunk, doc, false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered chunk must be an ImageExplanationChunk instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: unsuccessfull execution - wrong type stream
     */
    @Test
    public void testBuildReportChunkWrongTypeStream() {

        try {
            instance.buildReportChunk(ichunk, new Object(), false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must be a com.lowagie.text.Document instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: unsuccessfull execution - image cannot be found in the classpath
     */
    @Test
    public void testBuildReportChunkImageDoesNotExist() {
        imagedata.setURL("DOG.jpg");

        try {
            instance.buildReportChunk(ichunk, doc, false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The image 'DOG.jpg' could not be found";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: successfull execution - image caption present
     */
    @Test
    public void testBuildReportChunkSuccessfull1() {
        instance.buildReportChunk(ichunk, doc, true);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 6 elements added - 6 events
        assertTrue(events.size() == 6);

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

        //Check the fifth event - chunk content added as image object
        event = events.get(4);
        confirmImageAdded(event, imagedata.getURL());

        //Check the sixth event - image caption added as as paragraph
        event = events.get(5);
        confirmParagraphAdded(event, "IMAGE: "+imagedata.getCaption());
    }

    /**
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: successfull execution - image caption is not present
     */
    @Test
    public void testBuildReportChunkSuccessfull2() {
        imagedata.setCaption(null);

        instance.buildReportChunk(ichunk, doc, true);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there have been 5 elements added - 5 events
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

        //Check the fifth event - chunk content added as image object
        event = events.get(4);
        confirmImageAdded(event, imagedata.getURL());
    }

    /**
     * Test of buildReportChunk method, of class PDFImageChunkBuilder.
     * Test case: successfull execution - image caption is not present and no
     * chunk headers are inserted
     */
    @Test
    public void testBuildReportChunkSuccessfull3() {
        imagedata.setCaption(null);

        instance.buildReportChunk(ichunk, doc, false);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        //Check that there has been 1 element added - 1 event
        assertTrue(events.size() == 1);

        //Check the first event - chunk content added as paragraph
        Object[] event = events.get(0);
        confirmImageAdded(event, imagedata.getURL());
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

    /**
     * This method checks that the dummy event really represents an
     * Image instance added to the Document
     *
     * @param event triggered event
     * @param content expected textual content of the paragraph
     */
    private void confirmImageAdded(Object[] event, String content) {
        //Two parameters for this event
        assertTrue(event.length == 2);
        //First event parameter should be the event name
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
        //Second event parameter should be an Image object
        Image eventArgument = (Image) (event[1]);
        assertTrue(eventArgument.getUrl().getPath().endsWith(content));
    }

}
