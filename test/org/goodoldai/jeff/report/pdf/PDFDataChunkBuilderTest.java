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
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;
import java.util.ArrayList;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import junit.framework.TestCase;
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

/**
 * @author Bojan Tomic
 */
public class PDFDataChunkBuilderTest extends TestCase {

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
    PDFDataChunkBuilder instance = null;

    @Override
    protected void setUp() throws Exception {
        //Create some sample DataExplanationChunk data
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

        //Initialize a DataExplanationChunk with SingleData
        dchunk = new DataExplanationChunk(context, group, rule, tags, sdata);

        //Create an empty document instance, open it and attach a dummy document
        //event listener to it.
        doc = new Document();
        doc.open();
        docListener = new DummyDocListener();
        doc.addDocListener(docListener);

        //Initialize the builder
        instance = new PDFDataChunkBuilder();
    }

    @Override
    protected void tearDown() throws Exception {
        doc.close();
    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: unsuccessfull execution - null chunk
     */
    public void testBuildReportChunkNullChunk() {

        try {
            instance.buildReportChunk(null, doc);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered chunk must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: unsuccessfull execution - null stream
     */
    public void testBuildReportChunkNullStream() {

        try {
            instance.buildReportChunk(dchunk, null);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must not be null";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: unsuccessfull execution - wrong type chunk
     */
    public void testBuildReportChunkWrongTypeChunk() {
        TextExplanationChunk tchunk =
                new TextExplanationChunk("Sample text");

        try {
            instance.buildReportChunk(tchunk, doc);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered chunk must be a DataExplanationChunk instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: unsuccessfull execution - wrong type stream
     */
    public void testBuildReportChunkWrongTypeStream() {

        try {
            instance.buildReportChunk(dchunk, new Object());
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The entered stream must be a com.lowagie.text.Document instance";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: successfull execution - single data content
     */
    public void testBuildReportChunkSuccessfull1() {
        instance.buildReportChunk(dchunk, doc);

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

        //Check the fifth event - chunk content (SingleData) added as PdfPTable
        event = events.get(4);
        confirmPdfPTableAdded(event, sdata);

    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: successfull execution - one dim data content
     */
    public void testBuildReportChunkSuccessfull2() {
        dchunk.setContent(odata);
        instance.buildReportChunk(dchunk, doc);

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

        //Check the fifth event - chunk content (OneDimData) added as PdfPTable
        event = events.get(4);
        confirmPdfPTableAdded(event, odata);

    }

    /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: successfull execution - two dim data content
     */
    public void testBuildReportChunkSuccessfull3() {
        dchunk.setContent(twdata);
        instance.buildReportChunk(dchunk, doc);

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

        //Check the fifth event - chunk content (TwoDimData) added as PdfPTable
        event = events.get(4);
        confirmPdfPTableAdded(event, twdata);

    }

        /**
     * Test of buildReportChunk method, of class PDFDataChunkBuilder.
     * Test case: successfull execution - three dim data content
     */
    public void testBuildReportChunkSuccessfull4() {
        dchunk.setContent(thdata);
        instance.buildReportChunk(dchunk, doc);

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

        //Check the fifth event - chunk content (ThreeDimData) added as PdfPTable
        event = events.get(4);
        confirmPdfPTableAdded(event, thdata);

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
     * This method checks that the dummy event really represents a
     * PdfPtable instance added to the Document with SingleData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmPdfPTableAdded(Object[] event, SingleData content) {
        //Two parameters for this event
        assertTrue(event.length == 2);
        //First event parameter should be the event name
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
        //Second event parameter should be a PDFPTable object
        PdfPTable eventArgument = (PdfPTable) (event[1]);

        //The table should have two rows
        ArrayList<PdfPRow> rows = eventArgument.getRows();
        assertEquals(2,rows.size());

        //The first row should be a single cell containing dimension and unit
        assertTrue(rows.get(0).getCells().length == 1);
        String caption = content.getDimension().getName()+" ["+content.getDimension().getUnit()+"]";
        assertEquals(caption,rows.get(0).getCells()[0].getPhrase().getContent());

        //The second row should be a single cell containing a value
        assertTrue(rows.get(1).getCells().length == 1);
        String value = content.getValue().toString();
        assertEquals(value,rows.get(1).getCells()[0].getPhrase().getContent());
    }

    /**
     * This method checks that the dummy event really represents a
     * PdfPtable instance added to the Document with OneDimData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmPdfPTableAdded(Object[] event, OneDimData content) {
        //Two parameters for this event
        assertTrue(event.length == 2);
        //First event parameter should be the event name
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
        //Second event parameter should be a PDFPTable object
        PdfPTable eventArgument = (PdfPTable) (event[1]);

        //The table should have one row for each value plus a header row
        ArrayList<PdfPRow> rows = eventArgument.getRows();
        assertEquals(content.getValues().size()+1,rows.size());

        //The first row should be a single cell containing dimension and unit
        assertTrue(rows.get(0).getCells().length == 1);
        String caption = content.getDimension().getName()+" ["+content.getDimension().getUnit()+"]";
        assertEquals(caption,rows.get(0).getCells()[0].getPhrase().getContent());

        //All remaining rows should be single cell rows containing exactly one value
        ArrayList<Object> values = content.getValues();
        
        //Assert each value
        for(int i=0;i<values.size();i++){
            assertTrue(rows.get(i+1).getCells().length == 1);
            String value = values.get(i).toString();
            assertEquals(value,rows.get(i+1).getCells()[0].getPhrase().getContent());
        }
    }

    /**
     * This method checks that the dummy event really represents a
     * PdfPtable instance added to the Document with TwoDimData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmPdfPTableAdded(Object[] event, TwoDimData content) {
        //Two parameters for this event
        assertTrue(event.length == 2);
        //First event parameter should be the event name
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
        //Second event parameter should be a PDFPTable object
        PdfPTable eventArgument = (PdfPTable) (event[1]);

        //The table should have one row for each value plus a header row
        ArrayList<PdfPRow> rows = eventArgument.getRows();
        assertEquals(content.getValues().size()+1,rows.size());

        //The first row should contain two cells containing dimension and unit
        assertTrue(rows.get(0).getCells().length == 2);
        String caption = content.getDimension1().getName()+" ["+content.getDimension1().getUnit()+"]";
        assertEquals(caption,rows.get(0).getCells()[0].getPhrase().getContent());
        String caption2 = content.getDimension2().getName()+" ["+content.getDimension2().getUnit()+"]";
        assertEquals(caption2,rows.get(0).getCells()[1].getPhrase().getContent());

        //All remaining rows should be two-cell rows containing exactly two values
        ArrayList<Tuple> values = content.getValues();

        //Assert each value
        for(int i=0;i<values.size();i++){
            assertTrue(rows.get(i+1).getCells().length == 2);
            String value1 = values.get(i).getValue1().toString();
            assertEquals(value1,rows.get(i+1).getCells()[0].getPhrase().getContent());
            String value2 = values.get(i).getValue2().toString();
            assertEquals(value2,rows.get(i+1).getCells()[1].getPhrase().getContent());
        }
    }

    /**
     * This method checks that the dummy event really represents a
     * PdfPtable instance added to the Document with ThreeDimData as content
     *
     * @param event triggered event
     * @param content expected data content of the table
     */
    private void confirmPdfPTableAdded(Object[] event, ThreeDimData content) {
        //Two parameters for this event
        assertTrue(event.length == 2);
        //First event parameter should be the event name
        String eventName = (String) (event[0]);
        assertEquals("add", eventName);
        //Second event parameter should be a PDFPTable object
        PdfPTable eventArgument = (PdfPTable) (event[1]);

        //The table should have one row for each value plus a header row
        ArrayList<PdfPRow> rows = eventArgument.getRows();
        assertEquals(content.getValues().size()+1,rows.size());

        //The first row should contain three cells containing dimension and unit
        assertTrue(rows.get(0).getCells().length == 3);
        String caption = content.getDimension1().getName()+" ["+content.getDimension1().getUnit()+"]";
        assertEquals(caption,rows.get(0).getCells()[0].getPhrase().getContent());
        String caption2 = content.getDimension2().getName()+" ["+content.getDimension2().getUnit()+"]";
        assertEquals(caption2,rows.get(0).getCells()[1].getPhrase().getContent());
        String caption3 = content.getDimension3().getName();
        assertEquals(caption3,rows.get(0).getCells()[2].getPhrase().getContent());

        //All remaining rows should be three-cell rows containing exactly three values
        ArrayList<Triple> values = content.getValues();

        //Assert each value
        for(int i=0;i<values.size();i++){
            assertTrue(rows.get(i+1).getCells().length == 3);
            String value1 = values.get(i).getValue1().toString();
            assertEquals(value1,rows.get(i+1).getCells()[0].getPhrase().getContent());
            String value2 = values.get(i).getValue2().toString();
            assertEquals(value2,rows.get(i+1).getCells()[1].getPhrase().getContent());
            String value3 = values.get(i).getValue3().toString();
            assertEquals(value3,rows.get(i+1).getCells()[2].getPhrase().getContent());
        }
    }

}
