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

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete implementation of the ReportChunkBuilderFactory interface for
 * reports that are supposed to be XML documents. This class provides 
 * references to the concrete report chunk builder instances for the XML 
 * report type.
 *
 * @author Boris Horvat
 */
public class XMLReportChunkBuilderFactory implements ReportChunkBuilderFactory {

    /**
     * An XMLDataChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private XMLDataChunkBuilder XMLDataChunkBuilder;
    /**
     * An XMLImageChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private XMLImageChunkBuilder XMLImageChunkBuilder;
    /**
     * An XMLTextChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private XMLTextChunkBuilder XMLTextChunkBuilder;

    /**
     * Initializes all attributes (chunk builder references) to null.
     */
    public XMLReportChunkBuilderFactory() {
        this.XMLDataChunkBuilder = null;
        this.XMLImageChunkBuilder = null;
        this.XMLTextChunkBuilder = null;
    }

    /**
     * This method returns the appropriate chunk builder instances for the
     * entered explanation chunk. If, for example, a DataExplanationChunk was 
     * entered as an argument, the method would return an XMLDataChunkBuilder 
     * instance.
     *
     * It is necessary to state that chunk builder instances are
     * "lazy initialized" and cached (as attributes) while the factory instance
     * exists. This means that, for example, XMLImageChunkBuilder attribute is 
     * null when the factory is created and initialized only when the first
     * XMLImageChunkBuilder instance is needed and not before. In all
     * subsequent calls when this method is supposed to return a
     * XMLImageChunkBuilder instance it returns the reference to the already
     * initialized XMLImageChunkBuilder object.
     *
     * @param echunk explanation chunk that needs to be transformed
     * into a report piece
     * 
     * @return chunk builder instance that is supposed to be used in order
     * to transform the entered chunk
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the type was not recognized
     */
    public ReportChunkBuilder getReportChunkBuilder(ExplanationChunk echunk) {

        if (echunk instanceof TextExplanationChunk) {
            if (XMLTextChunkBuilder == null) {
                XMLTextChunkBuilder = new XMLTextChunkBuilder();
            }
            return XMLTextChunkBuilder;
        }

        if (echunk instanceof ImageExplanationChunk) {
            if (XMLImageChunkBuilder == null) {
                XMLImageChunkBuilder = new XMLImageChunkBuilder();
            }
            return XMLImageChunkBuilder;
        }

        if (echunk instanceof DataExplanationChunk){
            if (XMLDataChunkBuilder == null) {
                XMLDataChunkBuilder = new XMLDataChunkBuilder();
            }
            return XMLDataChunkBuilder;
        }
        
        throw new ExplanationException(
                "The provided ExplanationChunk does not match any of the required types");
    }
}

