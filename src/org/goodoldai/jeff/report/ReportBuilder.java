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
package org.goodoldai.jeff.report;

import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import java.util.ArrayList;

/**
 * The report builder is supposed to transfrom an explanation into a 
 * report. 
 *
 * The algorthytm for doing this consists of several steps. First, general
 * explanation data like owner, date when it was created etc. gets inserted 
 * into the report. Then, each explanation chunk gets transformed into a 
 * report piece and inserted into the report.
 *
 * This algorhytm has a general outline which does not depend on the report
 * and chunk type. This outline is implemented in this abstract class while 
 * leaving some concrete steps to be implemented in its subclasses. 
 *
 * @author Bojan Tomic
 */
public abstract class ReportBuilder {

    /**
     * Report chunk builder factory instance to be used when
     * acquiring report chunk builders
     */
    protected ReportChunkBuilderFactory factory;

    /**
     * Creates the report builder and provides it with a report chunk builder 
     * factory instance
     *
     * @param factory report chunk builder factory instance
     *
     * @throws explanation.ExplanationException if the entered factory is null
     */
    public ReportBuilder(ReportChunkBuilderFactory factory) {
        if (factory == null) {
            throw new ExplanationException("You must enter a non-null chunk factory reference");
        }

        this.factory = factory;
    }

    /**
     * This abstract method should create a report based on the provided 
     * explanation and send it to a file as output. If the file doesn't exist, 
     * it is supposed to be created. If it exists, it should be overwritten.
     * 
     * Basically, this method should open the apropriate output stream (based on the
     * provided file path) and call the "buildReport" method that has an output
     * stream as an argument.
     * 
     * Subclasses need to implement this method in order to open the
     * appropriate output stream type.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param filepath a string representing an URL for the file
     *
     * @throws explanation.ExplanationException if any of the arguments are null
     * or if filepath is an empty string
     */
    public abstract void buildReport(Explanation explanation, String filepath);

    /**
     * Creates a report based on the provided explanation and writes it to the 
     * provided output stream.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param stream output stream to which the report is to be written
     *
     * @throws explanation.ExplanationException if any of the arguments are null
     */
    public void buildReport(Explanation explanation, Object stream) {
        if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }
        if (stream == null) {
            throw new ExplanationException("The entered stream must not be null");
        }

        //Insert header into the report
        insertHeader(explanation, stream);

        //Get all explanation chunks
        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

        //Transform explanation chunks and insert them into the report
        for (int i = 0; i < chunks.size(); i++) {
            //Get one chunk from the list
            ExplanationChunk chunk = chunks.get(i);

            //Get the appropriate chunk builder for this chunk
            ReportChunkBuilder cbuilder = factory.getReportChunkBuilder(chunk);

            //Transform chunk and insert it into the report
            cbuilder.buildReportChunk(chunk, stream);
        }

    }

    /**
     * This abstract method should insert the header into the report. The 
     * header should consist of general data collected from the explanation 
     * (date and time created, owner, language, country)
     *
     * @param explanation explanation from which the header data is to be
     * collected
     * @param stream output stream that the header is supposed to be inserted into
     * 
     * @throws explanation.ExplanationException if any of the arguments are null
     * or if the entered output stream type is not correct (concrete output 
     * stream type depends on the subclass implementation)
     */
    protected abstract void insertHeader(Explanation explanation, Object stream);
}

