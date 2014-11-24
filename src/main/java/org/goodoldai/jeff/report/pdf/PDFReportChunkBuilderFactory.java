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

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete implementation of the ReportChunkBuilderFactory inteface for 
 * reports that are supposed to be regular PDF documents. This class 
 * provides references to the concrete report chunk builder instances for 
 * the PDF report type.
 *
 * @author Bojan Tomic
 */
public class PDFReportChunkBuilderFactory implements ReportChunkBuilderFactory {

    private PDFImageChunkBuilder PDFImageChunkBuilder;
    private PDFTextChunkBuilder PDFTextChunkBuilder;
    private PDFDataChunkBuilder PDFDataChunkBuilder;

    /**
     * Initializes all attributes (chunk builder references) to null.
     */
    public PDFReportChunkBuilderFactory() {
        PDFImageChunkBuilder = null;
        PDFTextChunkBuilder = null;
        PDFDataChunkBuilder = null;
    }

    /**
     * This method returns the appropriate chunk builder refernce for the 
     * entered explanation chunk. If, for example, a DataExplanationChunk was 
     * entered as an argument, the method would return a PDFDataChunkBuilder 
     * instance.
     *
     * It is necessary to state that chunk builder instances are
     * "lazy initialized" and cached (as attributes) while the factory instance
     * exists. This means that, for example, PDFImageChunkBuilder attribute is 
     * null when the factory is created and initialized only when the first
     * PDFImageChunkBuilder instance is needed and not before. In all subsequent
     * calls when this method is supposed to return a PDFImageChunkBuilder
     * instance it returns the reference to the already initialized
     * PDFImageChunkBuilder object.
     *
     * @param echunk explanation chunk that needs to be transformed
     * into a report piece
     * 
     * @return chunk builder instance that is supposed to be used in order to
     * transform the entered chunk
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered chunk is null
     * or if the appropriate chunk builder for the entered chunk could not be
     * found
     */
    public ReportChunkBuilder getReportChunkBuilder(ExplanationChunk echunk) {
        if (echunk == null) {
            throw new ExplanationException("You must enter a non-null chunk instance");
        }

        if (echunk instanceof TextExplanationChunk) {
            if (PDFTextChunkBuilder == null) {
                PDFTextChunkBuilder = new PDFTextChunkBuilder();
            }

            return PDFTextChunkBuilder;
        }

        if (echunk instanceof ImageExplanationChunk) {
            if (PDFImageChunkBuilder == null) {
                PDFImageChunkBuilder = new PDFImageChunkBuilder();
            }

            return PDFImageChunkBuilder;
        }

        if (echunk instanceof DataExplanationChunk) {
            if (PDFDataChunkBuilder == null) {
                PDFDataChunkBuilder = new PDFDataChunkBuilder();
            }

            return PDFDataChunkBuilder;
        }

        //Explanation chunk type was not recognized
        throw new ExplanationException("Chunk type '" + echunk.getClass().getName() + "' was not recognized");
    }
}

