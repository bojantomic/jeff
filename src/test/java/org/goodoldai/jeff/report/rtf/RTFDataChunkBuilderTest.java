
package org.goodoldai.jeff.report.rtf;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;


import java.util.ArrayList;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Anisja Kijevcanin
 */

public class RTFDataChunkBuilderTest extends AbstractJeffTest {

    DataExplanationChunk dchunk = null;
    SingleData sdata = null;
    OneDimData odata = null;
    TwoDimData twdata = null;
    ThreeDimData thdata = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;
    Document doc = null;
    DummyDocListener docListener = null;
    RTFDataChunkBuilder instance = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule 1";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";

        sdata = new SingleData(new Dimension("money", "EUR"), new Double(1700));

        ArrayList<Object> data = new ArrayList<Object>();
        data.add(new Double(1700));
        odata = new OneDimData(new Dimension("money", "EUR"), data);

        ArrayList<Tuple> data2 = new ArrayList<Tuple>();
        data2.add(new Tuple("1700", "1300"));
        twdata = new TwoDimData(new Dimension("money", "USD"),
                new Dimension("profit", "EUR"), data2);

        ArrayList<Triple> data3 = new ArrayList<Triple>();
        data3.add(new Triple("1700", "1300", "Male"));
        thdata = new ThreeDimData(new Dimension("money", "EUR"),
                new Dimension("profit", "USD"), new Dimension("gender"), data3);

        dchunk = new DataExplanationChunk(context, group, rule, tags, sdata);

        doc = new Document();
        doc.open();
        docListener = new DummyDocListener();
        doc.addDocListener(docListener);

        instance = new RTFDataChunkBuilder();
    }

    @After
    public void tearDown() throws Exception {
        doc.close();
    }

    /**
     * Test of buildReportChunk method, of class RTFDataChunkBuilder.
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
     * Test of buildReportChunk method, of class RTFDataChunkBuilder.
     * Test case: unsuccessfull execution - null stream
     */
    @Test
    public void testBuildReportChunkNullStream() {

        try {
            instance.buildReportChunk(dchunk, null, false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class RTFDataChunkBuilder.
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
            String expResult = "The entered chunk must be a DataExplanationChunk instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class RTFDataChunkBuilder.
     * Test case: unsuccessfull execution - wrong type stream
     */
    @Test
    public void testBuildReportChunkWrongTypeStream() {

        try {
            instance.buildReportChunk(dchunk, new Object(), false);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must be a com.lowagie.text.Document instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class RTFDataChunkBuilder.
     * Test case: successfull execution - single data content
     */
    @Test
    public void testBuildReportChunkSuccessfull1() {
        instance.buildReportChunk(dchunk, doc, true);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        assertTrue(events.size() == 5);

        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        event = events.get(2);
        confirmParagraphAdded(event, "RULE: " + rule);

        event = events.get(3);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");

        event = events.get(4);
        confirmTableAdded(event, sdata);

    }

    /**
     * Test of buildReportChunk method, of class RTFDataChunkBuilder.
     * Test case: successfull execution - one dim data content
     */
    @Test
    public void testBuildReportChunkSuccessfull2() {
        dchunk.setContent(odata);
        instance.buildReportChunk(dchunk, doc, true);

        ArrayList<Object[]> events = docListener.getCapturedEvents();


        assertTrue(events.size() == 5);

        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        event = events.get(2);
        confirmParagraphAdded(event, "RULE: " + rule);

        event = events.get(3);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");

        event = events.get(4);
        confirmTableAdded(event, odata);

    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: successfull execution - two dim data content
     */
    @Test
    public void testBuildReportChunkSuccessfull3() {
        dchunk.setContent(twdata);
        instance.buildReportChunk(dchunk, doc, true);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        assertTrue(events.size() == 5);

        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        event = events.get(2);
        confirmParagraphAdded(event, "RULE: " + rule);

        event = events.get(3);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");

        event = events.get(4);
        confirmTableAdded(event, twdata);

    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: successfull execution - three dim data content
     */
    @Test
    public void testBuildReportChunkSuccessfull4() {
        dchunk.setContent(thdata);
        instance.buildReportChunk(dchunk, doc, true);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        assertTrue(events.size() == 5);

        Object[] event = events.get(0);
        confirmParagraphAdded(event, "CONTEXT: ERROR");

        event = events.get(1);
        confirmParagraphAdded(event, "GROUP: " + group);

        event = events.get(2);
        confirmParagraphAdded(event, "RULE: " + rule);

        event = events.get(3);
        confirmParagraphAdded(event, "TAGS: 'tag1' 'tag2'");

        event = events.get(4);
        confirmTableAdded(event, thdata);

    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: successfull execution - three dim data content but no chunk
     * headers inserted
     */
    @Test
    public void testBuildReportChunkSuccessfull5() {
        dchunk.setContent(thdata);
        instance.buildReportChunk(dchunk, doc, false);

        ArrayList<Object[]> events = docListener.getCapturedEvents();

        assertTrue(events.size() == 1);

        Object[] event = events.get(0);
        confirmTableAdded(event, thdata);

    }

    /**
     * This method checks that the dummy event really represents a
     * Paragraph instance added to the Document
     *
     * @param event triggered event
     * @param content expected textual content of the paragraph
     */
    private void confirmParagraphAdded(Object[] event, String content) {

        assertTrue(event.length == 2);
        
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
        
        Paragraph eventArgument = (Paragraph) (event[1]);
        assertEquals(content, eventArgument.getContent());
    }

    /**
     * This method checks that the dummy event really represents a
     * Table instance added to the Document with SingleData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmTableAdded(Object[] event, SingleData content) {

        assertTrue(event.length == 2);
 
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);

        Table eventArgument = (Table) (event[1]);
        assertEquals(2,eventArgument.size());
        assertTrue(eventArgument.getColumns() == 1);
        
        Cell cell1 = (Cell) eventArgument.getElement(0, 0);
        Phrase phrase1 = new Phrase();
        for (int i = 0; i < cell1.getChunks().size(); i++) {
        	phrase1.add(cell1.getChunks().get(i));
		}
        
        Cell cell2 = (Cell) eventArgument.getElement(1, 0);
        Phrase phrase2 = new Phrase();
        for (int i = 0; i < cell2.getChunks().size(); i++) {
        	phrase2.add(cell2.getChunks().get(i));
		}

        String caption = content.getDimension().getName()+" ["+content.getDimension().getUnit()+"]";
        assertEquals(caption,phrase1.getContent());
        
        
        String value = content.getValue().toString();
        assertEquals(value,phrase2.getContent());
    }

    /**
     * This method checks that the dummy event really represents a
     * Table instance added to the Document with OneDimData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmTableAdded(Object[] event, OneDimData content) {

        assertTrue(event.length == 2);

        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
 
        Table eventArgument = (Table) (event[1]);
        
        Cell cell = (Cell) eventArgument.getElement(0, 0);
        Phrase phrase = new Phrase();
        for (int i = 0; i < cell.getChunks().size(); i++) {
        	phrase.add(cell.getChunks().get(i));
		}
        
        assertEquals(content.getValues().size()+1,eventArgument.size());
        assertTrue(eventArgument.getColumns() == 1);
        String caption = content.getDimension().getName()+" ["+content.getDimension().getUnit()+"]";
        assertEquals(caption,phrase.getContent());

        ArrayList<Object> values = content.getValues();

        for(int i=0;i<values.size();i++){
        	
            String value = values.get(i).toString();
            Cell cell1 = (Cell) eventArgument.getElement(i+1, 0);
            Phrase phrase1 = new Phrase();
            for (int j = 0; j < cell1.getChunks().size(); j++) {
            	phrase1.add(cell1.getChunks().get(j));
            }
            assertEquals(value,phrase1.getContent());
        }
    }

    /**
     * This method checks that the dummy event really represents a
     * Table instance added to the Document with TwoDimData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmTableAdded(Object[] event, TwoDimData content) {

        assertTrue(event.length == 2);
 
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);

        Table eventArgument = (Table) (event[1]);
        
        Cell cell1 = (Cell) eventArgument.getElement(0, 0);
        Phrase phrase1 = new Phrase();
        for (int i = 0; i < cell1.getChunks().size(); i++) {
        	phrase1.add(cell1.getChunks().get(i));
		}
        
        Cell cell2 = (Cell) eventArgument.getElement(0, 1);
        Phrase phrase2 = new Phrase();
        for (int i = 0; i < cell2.getChunks().size(); i++) {
        	phrase2.add(cell2.getChunks().get(i));
		}
        assertEquals(content.getValues().size()+1,eventArgument.size());

        assertTrue(eventArgument.getColumns() == 2);
        String caption = content.getDimension1().getName()+" ["+content.getDimension1().getUnit()+"]";
        assertEquals(caption,phrase1.getContent());
        String caption2 = content.getDimension2().getName()+" ["+content.getDimension2().getUnit()+"]";
        assertEquals(caption2,phrase2.getContent());

        ArrayList<Tuple> values = content.getValues();

        for(int i=0;i<values.size();i++){
            String value1 = values.get(i).getValue1().toString();
            
            Cell cell1_ = (Cell) eventArgument.getElement(i+1, 0);
            Phrase phrase1_ = new Phrase();
            for (int j = 0; j < cell1_.getChunks().size(); j++) {
            	phrase1_.add(cell1_.getChunks().get(j));
    		}
            
            assertEquals(value1,phrase1_.getContent());
            String value2 = values.get(i).getValue2().toString();
            
            Cell cell2_ = (Cell) eventArgument.getElement(i+1, 1);
            Phrase phrase2_ = new Phrase();
            for (int j = 0; j < cell2_.getChunks().size(); j++) {
            	phrase2_.add(cell2_.getChunks().get(i));
    		}
            
            assertEquals(value2,phrase2_.getContent());
        }
    }

    /**
     * This method checks that the dummy event really represents a
     * Table instance added to the Document with ThreeDimData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmTableAdded(Object[] event, ThreeDimData content) {

        assertTrue(event.length == 2);

        String eventName = (String) (event[0]);
        assertEquals("add", eventName);

        Table eventArgument = (Table) (event[1]);
        
        Cell cell1 = (Cell) eventArgument.getElement(0, 0);
        Phrase phrase1 = new Phrase();
        for (int i = 0; i < cell1.getChunks().size(); i++) {
        	phrase1.add(cell1.getChunks().get(i));
		}
        
        Cell cell2 = (Cell) eventArgument.getElement(0, 1);
        Phrase phrase2 = new Phrase();
        for (int i = 0; i < cell2.getChunks().size(); i++) {
        	phrase2.add(cell2.getChunks().get(i));
		}
        
        Cell cell3 = (Cell) eventArgument.getElement(0, 2);
        Phrase phrase3 = new Phrase();
        for (int i = 0; i < cell3.getChunks().size(); i++) {
        	phrase3.add(cell3.getChunks().get(i));
		}

        assertEquals(content.getValues().size()+1, eventArgument.size());
        assertTrue(eventArgument.getColumns() == 3);
        
        String caption = content.getDimension1().getName()+" ["+content.getDimension1().getUnit()+"]";
        assertEquals(caption, phrase1.getContent());
        String caption2 = content.getDimension2().getName()+" ["+content.getDimension2().getUnit()+"]";
        assertEquals(caption2,phrase2.getContent());
        String caption3 = content.getDimension3().getName();
        assertEquals(caption3, phrase3.getContent());

        ArrayList<Triple> values = content.getValues();

        for(int i=0;i<values.size();i++){
            String value1 = values.get(i).getValue1().toString();
            
            Cell cell1_ = (Cell) eventArgument.getElement(i+1, 0);
            Phrase phrase1_ = new Phrase();
            for (int j = 0; j < cell1_.getChunks().size(); j++) {
            	phrase1_.add(cell1_.getChunks().get(j));
    		}
            assertEquals(value1,phrase1_.getContent());
            String value2 = values.get(i).getValue2().toString();

            Cell cell2_ = (Cell) eventArgument.getElement(i+1, 1);
            Phrase phrase2_ = new Phrase();
            for (int j = 0; j < cell2_.getChunks().size(); j++) {
            	phrase2_.add(cell2_.getChunks().get(j));
    		}
            assertEquals(value2,phrase2_.getContent());
            
            Cell cell3_ = (Cell) eventArgument.getElement(i+1, 2);
            Phrase phrase3_ = new Phrase();
            for (int j = 0; j < cell3_.getChunks().size(); j++) {
            	phrase3_.add(cell3_.getChunks().get(j));
    		}
            String value3 = values.get(i).getValue3().toString();
            assertEquals(value3,phrase3_.getContent());
        }
    }

}

