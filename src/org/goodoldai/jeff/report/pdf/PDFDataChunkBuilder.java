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
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
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
import java.awt.Color;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 * A concrete builder for transforming data explanation chunks into pieces
 * of PDF report
 *
 * @author Bojan Tomic
 *
 * @version 1.0 This version does not have any formatting options regarding
 * data presentation in PDF and data is presented in the form of tables
 */
public class PDFDataChunkBuilder implements ReportChunkBuilder {

    /**
     * Initializes the builder
     */
    public PDFDataChunkBuilder() {
    }

    /**
     * This method transforms a data explanation chunk into a PDF report piece 
     * and writes this piece into the provided output stream which is, in this 
     * case, an instance of com.lowagie.text.Document. The method first collects all
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content can 
     * be a SingleData, OneDImData, TwoDimData or a ThreeDimData instance, 
     * dimension details and concrete data are transformed into PDF and 
     * inserted.
     *
     * @param echunk data explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written as output (in this case com.lowagie.text.Document)
     *
     * @throws explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a DataExplanationChunk instance or if 
     * the entered output stream type is not com.lowagie.text.Document
     */
    public void buildReportChunk(ExplanationChunk echunk, Object stream) {
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

        //Insert general chunk data
        PDFChunkUtility.insertChunkHeader(echunk, doc);

        //Insert chunk content
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
     * This is a private method that is used to insert single data content into
     * the PDF document.
     *
     * @param data single data which needs to be transformed
     * @param document PDF document in which the transformed data will be
     * written
     */
    private void inputSingleDataContent(SingleData data, Document document) {
        String value = String.valueOf(data.getValue());

        //Create table with only one column and set some formatting options
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(30);
        table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setSpacingBefore(15.0F);
        table.setSpacingAfter(15.0F);

        //Create the header cell
        PdfPCell cell1 =
                new PdfPCell(new Paragraph(turnDimensionIntoHeader(data.getDimension())));
        cell1.setBackgroundColor(Color.lightGray);

        //Create a single data cell
        PdfPCell cell2 = new PdfPCell(new Paragraph(value));

        //Insert cells into table
        table.addCell(cell1);
        table.addCell(cell2);

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }

    /**
     * This is a private method that is used to insert one dimensional data 
     * content into the PDF document.
     *
     * @param data one dimensional data which needs to be transformed
     * @param document PDF document in which the transformed data will be
     * written
     */
    private void inputOneDimDataContent(OneDimData data, Document document) {

        //Create table with only one column and set some formatting options
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(30);
        table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setSpacingBefore(15.0F);
        table.setSpacingAfter(15.0F);

        //Create the header cell
        PdfPCell cell1 =
                new PdfPCell(new Paragraph(turnDimensionIntoHeader(data.getDimension())));
        cell1.setBackgroundColor(Color.lightGray);
        table.addCell(cell1);

        //Create and insert data cells
        ArrayList<Object> values = data.getValues();

        for (int i = 0; i < values.size(); i++) {
            table.addCell(new Paragraph(transformValue(values.get(i))));
        }

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }

    /**
     * This is a private method that is used to insert two dimensional data
     * content into the PDF document.
     *
     * @param data two dimensional data which needs to be transformed
     * @param document PDF document in which the transformed data will be
     * written
     */
    private void inputTwoDimDataContent(TwoDimData data, Document document) {

        //Create table with two columns and set some formatting options
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setSpacingBefore(15.0F);
        table.setSpacingAfter(15.0F);

        //Create the header cells
        PdfPCell cell1 =
                new PdfPCell(new Paragraph(turnDimensionIntoHeader(data.getDimension1())));
        cell1.setBackgroundColor(Color.lightGray);
        table.addCell(cell1);

        PdfPCell cell2 =
                new PdfPCell(new Paragraph(turnDimensionIntoHeader(data.getDimension2())));
        cell2.setBackgroundColor(Color.lightGray);
        table.addCell(cell2);

        //Create and insert data cells
        ArrayList<Tuple> values = data.getValues();

        for (int i = 0; i < values.size(); i++) {
            Tuple tuple = (Tuple)(values.get(i));
            table.addCell(new Paragraph(transformValue(tuple.getValue1())));
            table.addCell(new Paragraph(transformValue(tuple.getValue2())));
        }

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }

    /**
     * This is a private method that is used to insert three dimensional data
     * content into the PDF document.
     *
     * @param data three dimensional data which needs to be transformed
     * @param document PDF document in which the transformed data will be
     * written
     */
    private void inputThreeDimDataContent(ThreeDimData data, Document document) {

        //Create table with three columns and set some formatting options
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(80);
        table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
        table.setSpacingBefore(15.0F);
        table.setSpacingAfter(15.0F);

        //Create the header cells
        PdfPCell cell1 =
                new PdfPCell(new Paragraph(turnDimensionIntoHeader(data.getDimension1())));
        cell1.setBackgroundColor(Color.lightGray);
        table.addCell(cell1);

        PdfPCell cell2 =
                new PdfPCell(new Paragraph(turnDimensionIntoHeader(data.getDimension2())));
        cell2.setBackgroundColor(Color.lightGray);
        table.addCell(cell2);

        PdfPCell cell3 =
                new PdfPCell(new Paragraph(turnDimensionIntoHeader(data.getDimension3())));
        cell3.setBackgroundColor(Color.lightGray);
        table.addCell(cell3);

        //Create and insert data cells
        ArrayList<Triple> values = data.getValues();

        for (int i = 0; i < values.size(); i++) {
            Triple triple = (Triple)(values.get(i));
            table.addCell(new Paragraph(transformValue(triple.getValue1())));
            table.addCell(new Paragraph(transformValue(triple.getValue2())));
            table.addCell(new Paragraph(transformValue(triple.getValue3())));
        }

        try {
            document.add(table);
        } catch (DocumentException ex) {
            throw new ExplanationException(ex.getMessage());
        }

    }

    /**
     * This method transforms a data value into a String which can be inserted
     * into a table cell
     *
     * @param value data value that needs to be transformed
     * 
     * @return string representing table cell content
     */
    private String transformValue(Object value) {
        //If the value is a date format it accordingly
        if (value instanceof Calendar) {
            Date date = ((Calendar) (value)).getTime();
            return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        }
        if (value instanceof Date) {
            Date date = (Date) (value);
            return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        }

        //In all other cases return a "toString" value
        return value.toString();
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
}