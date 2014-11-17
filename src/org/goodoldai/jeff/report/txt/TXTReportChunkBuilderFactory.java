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
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete implementation of the ReportChunkBuilderFactory inteface for 
 * reports that are supposed to be regular text documents (not DOC nor RTF, 
 * just TXT). This class provides references to the concrete report chunk 
 * builder instances for the text report type.
 *
 * @author Nemanja Jovanovic
 */
public class TXTReportChunkBuilderFactory implements ReportChunkBuilderFactory {

    /**
     * A TXTTextChunkBuilder instance which is "lazy initialized" 
     * and cached for future use.
     */
    private TXTTextChunkBuilder TXTTextChunkBuilder;

    /**
     * A TXTDataChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private TXTDataChunkBuilder TXTDataChunkBuilder;

    /**
     * A TXTImageChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private TXTImageChunkBuilder TXTImageChunkBuilder;

    /**
     * Initializes all attributes (chunk builder references) to null.
     */
    public TXTReportChunkBuilderFactory () {
        this.TXTDataChunkBuilder = null;
        this.TXTImageChunkBuilder = null;
        this.TXTTextChunkBuilder = null;
    }

    /**
     * This method returns the appropriate chunk builder refernce for the 
     * entered explanation chunk.
     * 
     * If, for example, a DataExplanationChunk was entered as an argument,
     * the method would return a TXTDataChunkBuilder instance.
     *
     * It is necessary to state that chunk builder instances are
     * "lazy initialized" and cached (as attributes) while the factory instance
     * exists. This means that, for example, TXTImageChunkBuilder attribute is 
     * null when the factory is created and initialized only when the first
     * TXTImageChunkBuilder instance is needed and not before. In all
     * subsequent calls when this method is supposed to return a
     * TXTImageChunkBuilder instance it returns the reference to the already
     * initialized TXTImageChunkBuilder object.
     *
     * @param echunk explanation chunk that needs to be transformed
     * into a report piece
     *
     * @return chunk builder instance that is supposed
     * to be used in order to transform the entered chunk
     */
    public ReportChunkBuilder getReportChunkBuilder (ExplanationChunk echunk) {
        
        if(echunk instanceof TextExplanationChunk){
            if(TXTTextChunkBuilder == null)
                 TXTTextChunkBuilder = new TXTTextChunkBuilder();
            return TXTTextChunkBuilder;

        }if(echunk instanceof DataExplanationChunk){
            if(TXTDataChunkBuilder == null)
                TXTDataChunkBuilder = new TXTDataChunkBuilder();
            return TXTDataChunkBuilder;
            
        }if(echunk instanceof ImageExplanationChunk){
            if(TXTImageChunkBuilder == null)
                TXTImageChunkBuilder = new TXTImageChunkBuilder();
            return TXTImageChunkBuilder;
        }

        throw new ExplanationException(
                "The provided ExplanationChunk does not match any of the required types");
    }

}

