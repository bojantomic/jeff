/*
 * Copyright 2009 Nemanja Jovanovic
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
package org.goodoldai.jeff.report.txt;

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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 * A concrete builder for transforming data explanation chunks into pieces 
 * of textual report
 *
 * @author Nemanja Jovanovic
 */
public class TXTDataChunkBuilder implements ReportChunkBuilder {

    /**
     * Initializes the builder
     */
    public TXTDataChunkBuilder() {
    }

    /**
     * This method transforms a data explanation chunk into a text report piece 
     * and writes this piece into the provided output stream which is, in this 
     * case, an instance of java.io.PrintWriter. The method first collects all 
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content can 
     * be a SingleData, OneDImData, TwoDimData or a ThreeDimData instance, 
     * dimension details and concrete data are transformed into text and 
     * inserted.
     *                                                                      <br>
     * In the case of SingleData the output should look like this:          <br>
     *                                                                      <br>
     * dimension name [unit]                                                <br>
     * -------------------                                                  <br>
     * value                                                                <br>
     *                                                                      <br>
     * i.e.                                                                 <br>
     *                                                                      <br>
     * temperature [C]                                                      <br>
     * -------------------                                                  <br>
     * 17                                                                   <br>
     *                                                                      <br>
     * In the case of OneDimData the output should look like this:          <br>
     *                                                                      <br>
     * dimension name [unit]                                                <br>
     * -------------------                                                  <br>
     * value1                                                               <br>
     * value2                                                               <br>
     * value3                                                               <br>
     *                                                                      <br>
     * i.e.                                                                 <br>
     *                                                                      <br>
     * temperature [C]                                                      <br>
     * -------------------                                                  <br>
     * 17                                                                   <br>
     * 21                                                                   <br>
     * 34                                                                   <br>
     *                                                                      <br>
     * In the case of TwoDimData the output should look like this:          <br>
     *                                                                      <br>
     * dimension1 [unit1] dimension2 [unit2]                                <br>
     * ---------------------------------                                    <br>
     * value11 value21                                                      <br>
     * value12 value22                                                      <br>
     * value13 value23                                                      <br>
     *                                                                      <br>
     * i.e. (note that the second dimension "City" has no unit)             <br>
     *                                                                      <br>
     * temperature [C]          City                                        <br>
     * ---------------------------------                                    <br>
     * 17                       Oslo                                        <br>
     * 21                       Moscow                                      <br>
     * 34                       London                                      <br>
     *                                                                      <br>
     * In the case of ThreeDimData the output should look similar to TwoDimData
     * just with the addition of one column for the third member values. In all 
     * cases, if the dimension unit is missing it should be omitted from the 
     * report.
     * 
     * @param echunk data explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be written as
     * output (in this case java.io.PrintWriter)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a DataExplanationChunk instance or if 
     * the entered output stream type is not java.io.PrintWriter
     */
    public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {

        if (echunk == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }

        if (echunk == null) {
            throw new ExplanationException("The argument 'echunk' is mandatory, so it can not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }

        if (!(echunk instanceof DataExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be type of DataExplanationChunk");
        }

        if (!(stream instanceof PrintWriter)) {
            throw new ExplanationException("The argument 'stream' must be the type of java.io.PrintWriter");
        }

        DataExplanationChunk dataExplanationChunk = (DataExplanationChunk) echunk;
        PrintWriter writer = (PrintWriter) stream;

        if (insertHeaders)
            TXTChunkUtility.insertExplanationInfo(echunk, writer);

        insertContent(dataExplanationChunk, writer);
    }

    /**
     * This is a private method that is used to insert content into the document,
     * this method first checks to see what type of content is (SingleData, OneDimData,
     * TwoDimData or a ThreeDimData ) and than calls the right method to insert
     * the content into the document (an instance of java.io.PrintWriter)
     *
     * @param imageExplanationChunk image explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case java.io.PrintWriter)
     */
    private void insertContent(DataExplanationChunk dataExplenationChunk, PrintWriter writer) {

        if (dataExplenationChunk.getContent() instanceof SingleData) {
            inputSingleDataContent(dataExplenationChunk, writer);

        } else if (dataExplenationChunk.getContent() instanceof OneDimData) {
            inputOneDimDataContent(dataExplenationChunk, writer);

        } else if (dataExplenationChunk.getContent() instanceof TwoDimData) {
            inputTwoDimDataContent(dataExplenationChunk, writer);

        } else {
            inputThreeDimDataContent(dataExplenationChunk, writer);
        }
    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.SingleData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case java.io.PrintWriter)
     */
    private void inputSingleDataContent(DataExplanationChunk dataExplenationChunk, PrintWriter writer) {
        SingleData singleData = (SingleData) dataExplenationChunk.getContent();

        String value = String.valueOf(singleData.getValue());

        Dimension dimension = singleData.getDimension();
        String dimensionName = dimension.getName();
        String dimensionUnit = dimension.getUnit();

        writer.write(dimensionName);

        if (dimensionUnit != null) {
            writer.write(" [" + dimensionUnit + "]");
        }
        writer.write("\n-------------------\n");
        writer.write(value+ "\n"+ "\n");


    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.OneDimData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case java.io.PrintWriter)
     */
    private void inputOneDimDataContent(DataExplanationChunk dataExplenationChunk, PrintWriter writer) {
        OneDimData oneDimData = (OneDimData) dataExplenationChunk.getContent();

        ArrayList<Object> objectValues = oneDimData.getValues();
        ArrayList<String> values = new ArrayList<String>();

        for (Iterator<Object> it = objectValues.iterator(); it.hasNext();) {
            Object object = it.next();
            values.add(String.valueOf(object));
        }

        Dimension dimension = oneDimData.getDimension();
        String dimensionName = dimension.getName();
        String dimensionUnit = dimension.getUnit();

        writer.write(dimensionName);
        if (dimensionUnit != null) {
            writer.write(" [" + dimensionUnit + "]");
        }
        writer.write("\n-------------------\n");

        for (Iterator<String> it = values.iterator(); it.hasNext();) {
            String value = it.next();
            writer.write(value + "\n");
        }

        writer.write("\n");
    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.TwoDimData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case java.io.PrintWriter)
     */
    private void inputTwoDimDataContent(DataExplanationChunk dataExplenationChunk, PrintWriter writer) {

        TwoDimData twoDimData = (TwoDimData) dataExplenationChunk.getContent();

        ArrayList<Tuple> tupleValues = twoDimData.getValues();

        Dimension dimension1 = twoDimData.getDimension1();
        String dimensionName1 = dimension1.getName();
        String dimensionUnit1 = dimension1.getUnit();

        Dimension dimension2 = twoDimData.getDimension2();
        String dimensionName2 = dimension2.getName();
        String dimensionUnit2 = dimension2.getUnit();

        writer.write(dimensionName1);
        if (dimensionUnit1 != null) {
            writer.write(" [" + dimensionUnit1 + "]");
        }

        writer.write("       ");
        writer.write(dimensionName2);
        if (dimensionUnit2 != null) {
            writer.write(" [" + dimensionUnit2 + "]");
        }
        writer.write("\n-------------------\n");
        for (Iterator<Tuple> it = tupleValues.iterator(); it.hasNext();) {
            Tuple tuple = it.next();

            String value1 = String.valueOf(tuple.getValue1());
            String value2 = String.valueOf(tuple.getValue2());

            writer.write(value1 + "       " + value2 + "\n");
        }

        writer.write("\n");
    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.ThreeDimData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case java.io.PrintWriter)
     */
    private void inputThreeDimDataContent(DataExplanationChunk dataExplenationChunk, PrintWriter writer) {
        ThreeDimData threeDimData = (ThreeDimData) dataExplenationChunk.getContent();

        ArrayList<Triple> tripleValues = threeDimData.getValues();

        Dimension dimension1 = threeDimData.getDimension1();
        String dimensionName1 = dimension1.getName();
        String dimensionUnit1 = dimension1.getUnit();

        Dimension dimension2 = threeDimData.getDimension2();
        String dimensionName2 = dimension2.getName();
        String dimensionUnit2 = dimension2.getUnit();

        Dimension dimension3 = threeDimData.getDimension3();
        String dimensionName3 = dimension3.getName();
        String dimensionUnit3 = dimension3.getUnit();

        writer.write(dimensionName1);

        if (dimensionUnit1 != null) {
            writer.write(" [" + dimensionUnit1 + "]");
        }
        writer.write("       ");
        writer.write(dimensionName2);

        if (dimensionUnit2 != null) {
            writer.write(" [" + dimensionUnit2 + "]");
        }
        writer.write("       ");
        writer.write(dimensionName3);

        if (dimensionUnit2 != null) {
            writer.write(" [" + dimensionUnit3 + "]");
        }
        writer.write("\n-------------------\n");

        for (Iterator<Triple> it = tripleValues.iterator(); it.hasNext();) {
            Triple triple = it.next();

            String value1 = String.valueOf(triple.getValue1());
            String value2 = String.valueOf(triple.getValue2());
            String value3 = String.valueOf(triple.getValue3());

            writer.write(value1 + "       " + value2 + "       " + value3 + "\n");
        }

        writer.write("\n");
    }
}

