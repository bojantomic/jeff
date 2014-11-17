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

import org.goodoldai.jeff.explanation.ExplanationChunk;

/**
 * When creating a report based on an explanation, each explanation chunk 
 * is supposed to translate into a report piece (element).
 *
 * The idea is to have a family of builders (which inherit this interface) for each
 * chunk and report type combination. For example PDFImageChunkBuilder, 
 * PDFDataChunkBuilder, and PDFTextChunkBuilder could be used to translate 
 * ImageExplanationChunk, DataExplanationChunk and TextExplanationChunk 
 * into pieces of a PDF report.
 *
 * @author Bojan Tomic
 */
public interface ReportChunkBuilder {

    /**
     * This method should transform an explanation chunk into a report piece 
     * and write this piece into the provided output stream.
     *
     * Since this is an abstract method, subclasses should provide a concrete 
     * implementation.
     *
     * @param echunk explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be written as
     * output
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null
     * or if the entered output stream type is not correct (concrete output 
     * stream type depends on the class that implements this interface)
     */
    public void buildReportChunk (ExplanationChunk echunk, Object stream, boolean insertHeaders);

}

