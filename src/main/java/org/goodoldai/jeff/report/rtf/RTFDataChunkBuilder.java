package org.goodoldai.jeff.report.rtf;

import java.awt.Color;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.table.RtfCell;

/**
 * A concrete builder for transforming data explanation chunks into pieces
 * of RTF report
 *
 * @author Anisja Kijevcanin
 *
 * @version 1.0 Data is presented in the form of tables, there are no formatting options
 */
public class RTFDataChunkBuilder implements ReportChunkBuilder {
	
	/**
     * Initializes the builder
     */
	public RTFDataChunkBuilder() {
	}
	
	/**
     * This method transforms a data explanation chunk into a RTF report piece 
     * and writes this piece into the provided output stream which is, in this 
     * case, an instance of com.lowagie.text.Document. The method first collects all
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content can 
     * be a SingleData, OneDImData, TwoDimData or a ThreeDimData instance, 
     * dimension details and concrete data are transformed into RTF and 
     * inserted.
     *
     * @param echunk data explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written as output (in this case com.lowagie.text.Document)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a DataExplanationChunk instance or if 
     * the entered output stream type is not com.lowagie.text.Document
     */
	public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {
        if (echunk == null) {
            throw new ExplanationException("The entered chunk must not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The entered stream must not be null");
        }

        if (!(echunk instanceof DataExplanationChunk)) {
            throw new ExplanationException("The entered chunk must be a DataExplanationChunk instance");
        }

        if (!(stream instanceof com.lowagie.text.Document)) {
            throw new ExplanationException("The entered stream must be a com.lowagie.text.Document instance");
        }

        com.lowagie.text.Document doc = ((com.lowagie.text.Document) stream);

        if (insertHeaders)
            RTFChunkUtility.insertChunkHeader(echunk, doc);
        
        if (echunk.getContent() instanceof SingleData) {
            inputSingleDataContent((SingleData) (echunk.getContent()), doc);

        } else if (echunk.getContent() instanceof OneDimData) {
            inputOneDimDataContent((OneDimData) (echunk.getContent()), doc);

        } else if (echunk.getContent() instanceof TwoDimData) {
            inputTwoDimDataContent((TwoDimData) (echunk.getContent()), doc);

        } else if (echunk.getContent() instanceof ThreeDimData) {
            inputThreeDimDataContent((ThreeDimData) (echunk.getContent()), doc);

        }
    }
	/**
     * This is a private method that inserts single data content into
     * the RTF document.
     *
     * @param data single data which needs to be transformed
     * @param document RTF document in which the transformed data will be
     * written
     */
	private void inputSingleDataContent(SingleData data, Document document) {
        String value = String.valueOf(data.getValue());

        Table table = null;
        RtfCell cell1 = null;
        RtfCell cell2 = null;
		try {
			table = new Table(1);
			
			table.setWidth(30);
			table.setAlignment(Table.ALIGN_LEFT);
			table.setOffset(15.0F);
			
			cell1 = new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension())));

			cell1.setBackgroundColor(Color.lightGray);
			cell2 = new RtfCell(new Paragraph(value));
		} catch (BadElementException e) {
			throw new ExplanationException(e.getMessage());
		}
		table.addCell(cell1);
		table.addCell(cell2);

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }
    }
	/**
	 * This is a private method which inserts one dimensional 
	 * data content into an RTF document.
	 * @param data one dimensional data which needs to be transformed
	 * @param document RTFF document in which the transformed data will be
     * written
	 */
	private void inputOneDimDataContent(OneDimData data, Document document) {
		
        Table table = null;
        
		try {
			table = new Table(1);
			table.setWidth(30);
	        table.setAlignment(Table.ALIGN_LEFT);
	        table.setOffset(15.0F);

			RtfCell cell1 =
			        new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension())));
			cell1.setBackgroundColor(Color.lightGray);
			table.addCell(cell1);

			ArrayList<Object> values = data.getValues();

			for (int i = 0; i < values.size(); i++) {
			    table.addCell(new Paragraph(transformValue(values.get(i))));
			}
		} catch (BadElementException e) {
			throw new ExplanationException(e.getMessage());
		}

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }
	
	/**
     * This is a private method that is used to insert two dimensional data
     * content into the RTF document.
     *
     * @param data two dimensional data which needs to be transformed
     * @param document RTF document in which the transformed data will be
     * written
     */
	private void inputTwoDimDataContent(TwoDimData data, Document document) {

        Table table = null;
		try {
			table = new Table(2);
			table.setWidth(50);
			table.setAlignment(Table.ALIGN_LEFT);
			table.setOffset(15.0F);

			RtfCell cell1 =
			        new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension1())));
			cell1.setBackgroundColor(Color.lightGray);
			table.addCell(cell1);

			RtfCell cell2 =
			        new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension2())));
			cell2.setBackgroundColor(Color.lightGray);
			table.addCell(cell2);

			ArrayList<Tuple> values = data.getValues();

			for (int i = 0; i < values.size(); i++) {
			    Tuple tuple = (Tuple)(values.get(i));
			    table.addCell(new Paragraph(transformValue(tuple.getValue1())));
			    table.addCell(new Paragraph(transformValue(tuple.getValue2())));
			}
		} catch (BadElementException e) {
			throw new ExplanationException(e.getMessage());
		}

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }
	/**
     * This is a private method that is used to insert three dimensional data
     * content into the RTF document.
     *
     * @param data three dimensional data which needs to be transformed
     * @param document RTF document in which the transformed data will be
     * written
     */
	private void inputThreeDimDataContent(ThreeDimData data, Document document) {

        Table table;
		try {
			table = new Table(3);
			table.setWidth(80);
			table.setAlignment(Table.ALIGN_LEFT);
			table.setOffset(15.0F);

			RtfCell cell1 =
			        new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension1())));
			cell1.setBackgroundColor(Color.lightGray);
			table.addCell(cell1);

			RtfCell cell2 =
			        new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension2())));
			cell2.setBackgroundColor(Color.lightGray);
			table.addCell(cell2);

			RtfCell cell3 =
			        new RtfCell(new Paragraph(turnDimensionIntoHeader(data.getDimension3())));
			cell3.setBackgroundColor(Color.lightGray);
			table.addCell(cell3);

			ArrayList<Triple> values = data.getValues();

			for (int i = 0; i < values.size(); i++) {
			    Triple triple = (Triple)(values.get(i));
			    table.addCell(new Paragraph(transformValue(triple.getValue1())));
			    table.addCell(new Paragraph(transformValue(triple.getValue2())));
			    table.addCell(new Paragraph(transformValue(triple.getValue3())));
			}
		} catch (BadElementException e) {
			throw new ExplanationException(e.getMessage());
		}

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }
	
	/**
     * This method transforms the dimension name and unit into a table header
     *
     * @param dimension Dimension that needs to be transformed in order to be
     * used as a table header
     *
     * @return dimension transformed into a table header
     */
	 private String turnDimensionIntoHeader(Dimension dimension) {

	        String dimensionName = dimension.getName();
	        String dimensionUnit = dimension.getUnit();

	        if (dimensionUnit != null && !dimensionUnit.isEmpty()) {
	            return dimensionName + " [" + dimensionUnit + "]";
	        } else {
	            return dimensionName;
	        }
	 	}
	 
	 /**
	     * This method transforms a data value into a String which can be inserted
	     * into a table cell
	     * @param value data value that needs to be transformed
	     * 
	     * @return string representing table cell content
	     */
	 private String transformValue(Object value) {
	        if (value instanceof Calendar) {
	            Date date = ((Calendar) (value)).getTime();
	            return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
	        }
	        if (value instanceof Date) {
	            Date date = (Date) (value);
	            return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
	        }
	        return value.toString();
	    }
}