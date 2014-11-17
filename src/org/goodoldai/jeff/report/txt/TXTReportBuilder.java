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

import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete ReportBuilder implementation. This class is used when the 
 * output report is supposed to be plain text (not DOC nor RTF, just TXT).
 *
 * @author Nemanja Jovanovic
 */
public class TXTReportBuilder extends ReportBuilder {

    /**
     * Just calls the superclass constructor.
     * 
     * @param factory chunk builder factory
     */
    public TXTReportBuilder(ReportChunkBuilderFactory factory) {
        super(factory);
    }

    /**
     * Creates a text report based on the provided explanation and sends it to
     * a text file as output. If the file doesn't exist, it is created. If it 
     * exists, it is overwritten.
     * 
     * Basically, this method opens a java.io.PrintWriter output stream
     * (based on the provided file path) and calls the "buildReport"
     * method that has an output stream as an argument.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param filepath a string representing an URL for the file
     *
     * @throws explanation.ExplanationException if any of the arguments are null
     * or if filepath is an empty string
     */
    public void buildReport(Explanation explanation, String filepath) {

        if (explanation == null && filepath == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }

        if (explanation == null) {
            throw new ExplanationException("The argument 'explanation' is mandatory, so it can not be null");
        }

        if (filepath == null || filepath.isEmpty()) {
            throw new ExplanationException("The argument 'filepath' must not be null or empty string");
        }

        try {
            PrintWriter writer = new PrintWriter(new File(filepath));
            writer.write("Report\n");
            
            buildReport(explanation, writer);
            
            writer.close();

        } catch (FileNotFoundException ex) {
            throw new ExplanationException(ex.getMessage());
        }
    }

    /**
     * This method inserts the header into the text report. The header consists 
     * of general data collected from the explanation (date and time created, 
     * owner, language and locale). If any of this data is missing, it is not 
     * inserted into the report. Since the report format is text, the provided 
     * output stream should be an instance of java.io.PrintWriter.
     * 
     * @param explanation explanation from which the header data
     * is to be collected
     * @param stream output stream that the header is supposed to
     * be inserted into
     * 
     * @throws explanation.ExplanationException if any of the arguments
     * are null or if the entered output stream type is not java.io.PrintWriter
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

        if (!(stream instanceof PrintWriter)) {
            throw new ExplanationException("The argument 'stream' must be the type of java.io.PrintWriter");
        }

        PrintWriter writer = (PrintWriter) stream;

        String date = Long.toString(explanation.getCreated().getTimeInMillis());
        String owner = explanation.getOwner();
        String language = explanation.getLanguage();
        String country = explanation.getCountry();

        if (date != null) {
            writer.write("Creation date: " + date + "\n");
        }

        if (owner != null) {
            writer.write("Report owner is: " + owner + "\n");
        }

        if (language != null) {
            writer.write("The language on wich report is created in is: " + language + "\n");
        }

        if (country != null) {
            writer.write("The country is: " + country + "\n");
        }
    }
}

