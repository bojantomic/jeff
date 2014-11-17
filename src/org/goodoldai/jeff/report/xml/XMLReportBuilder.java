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

import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete ReportBuilder implementation. This class is used when the  
 * output report is supposed to be XML.
 *
 * @author Boris Horvat
 */
public class XMLReportBuilder extends ReportBuilder {

    /**
     * Just calls the superclass constructor.
     *
     * @param factory chunk builder factory
     */
    public XMLReportBuilder(ReportChunkBuilderFactory factory) {
        super(factory);
    }

    /**
     * Creates an XML report based on the provided explanation and sends it to 
     * an XML file as output. If the file doesn't exist, it is created. If it 
     * exists, it is overwritten.
     * 
     * Basically, this method opens the org.dom4j.io.XMLWriter output stream and
     * it creates org.dom4j.Document object (based on the provided file path)
     * which holds all of the explanation information before it is put into the stream,
     * and calls the "buildReport" method that has an org.dom4j.Documents an argument.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param filepath a string representing an URL for the file
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null,
     * if filepath is an empty string, or if IOException is caught
     */
    public void buildReport(Explanation explanation, String filepath) {

        if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }

        if (filepath == null || filepath.isEmpty()) {
            throw new ExplanationException("The entered filepath must not be null or empty string");
        }

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new File(filepath));

            buildReport(explanation, writer);

        } catch (IOException ex) {
            throw new ExplanationException(
                    "The file could not be writen due to fallowing IO error: " + ex.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Creates a report based on the provided explanation and writes it to the
     * provided object that is type of org.dom4j.Document before it is written in
     * the file 
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param stream output stream to which the report is to be written
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null
     */
    @Override
    public void buildReport(Explanation explanation, Object stream) {

        if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The entered stream must not be null");
        }

        if (!(stream instanceof PrintWriter)) {
            throw new ExplanationException("The argument 'stream' must be the type of java.io.PrintWriter");
        }

        Document document = DocumentHelper.createDocument();
        document.addElement("explanation");

        insertHeader(explanation, document);

        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

        for (int i = 0; i < chunks.size(); i++) {

            ExplanationChunk chunk = chunks.get(i);
            ReportChunkBuilder cbuilder = factory.getReportChunkBuilder(chunk);

            cbuilder.buildReportChunk(chunk, document, isInsertChunkHeaders());
        }
        
        XMLWriter writer = new XMLWriter(((PrintWriter)stream));
        try {
            writer.write(document);
            writer.close();
        } catch (IOException ex) {
            throw new ExplanationException("The file could not be writen due to fallowing IO error: " + ex.getMessage());
        }
    }

    /**
     * This method inserts the header into the XML report. The header consists 
     * of general data collected from the explanation (date and time created, 
     * owner, title, language and country). If any of this data is missing, it is not
     * inserted into the report. Since the report format is XML, the provided 
     * output stream should be an instance of org.dom4j.Documents.
     * 
     * @param explanation explanation from which the header data is
     * to be collected
     * @param stream output stream that the header is supposed to be
     * inserted into
     * 
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null or if the entered output stream type is not org.dom4j.Document
     */
    protected void insertHeader(Explanation explanation, Object stream) {

        if (explanation == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }

        if (explanation == null) {
            throw new ExplanationException("The argument 'explanation' is mandatory, so it can not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }

        if (!(stream instanceof Document)) {
            throw new ExplanationException("The argument 'stream' must be the type of org.dom4j.Document");
        }

        Document document = (Document) stream;

        String date = DateFormat.getInstance().format(explanation.getCreated().getTime());
        String owner = explanation.getOwner();
        String language = explanation.getLanguage();
        String country = explanation.getCountry();
        String title = explanation.getTitle();

        Element root = document.getRootElement();

        if (date != null) {
            root.addAttribute("date", date);
        }

        if (owner != null) {
            root.addAttribute("owner", owner);
        }

        if (language != null) {
            root.addAttribute("language", language);
        }

        if (country != null) {
            root.addAttribute("country", country);
        }

        if (title != null) {
            root.addAttribute("title", title);
        }
    }
}

