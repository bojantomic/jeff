package org.goodoldai.jeff.report.rtf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.pdf.DummyDocListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;

/**
 * @author Anisja Kijevcanin
 */
public class RTFTextChunkBuilderTest extends AbstractJeffTest {

    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;
    String content = null;
    TextExplanationChunk tchunk = null;
    Document doc = null;
    DummyDocListener docListener = null;
    RTFTextChunkBuilder instance = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();

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
        instance = new RTFTextChunkBuilder();
    }

    @After
    public void tearDown() throws Exception {
        doc.close();
    }

    /**
     * Test of buildReportChunk method, of class RTFTextChunkBuilder.
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
     * Test of buildReportChunk method, of class RTFTextChunkBuilder.
     * Test case: unsuccessfull execution - null stream
     */
    @Test
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
     * Test of buildReportChunk method, of class RTFTextChunkBuilder.
     * Test case: unsuccessfull execution - wrong type chunk
     */
    @Test
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
     * Test of buildReportChunk method, of class RTFTextChunkBuilder.
     * Test case: unsuccessfull execution - wrong type stream
     */
    @Test
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
     * Test of buildReportChunk method, of class RTFTextChunkBuilder.
     * Test case: successfull execution
     */
    @Test
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
    @Test
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

