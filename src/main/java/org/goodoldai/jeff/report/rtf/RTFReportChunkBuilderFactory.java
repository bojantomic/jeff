package org.goodoldai.jeff.report.rtf;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete implementation of the ReportChunkBuilderFactory interface for 
 * reports that are supposed to be regular RTF documents. This class 
 * provides references to the concrete report chunk builder instances for 
 * the RTF report type.
 *
 * @author Anisja Kijevcanin
 */
public class RTFReportChunkBuilderFactory implements ReportChunkBuilderFactory {

    private RTFImageChunkBuilder RTFImageChunkBuilder;
    private RTFTextChunkBuilder RTFTextChunkBuilder;
    private RTFDataChunkBuilder RTFDataChunkBuilder;
    
    /**
     * Initializes all attributes (chunk builder references) to null.
     */
    public RTFReportChunkBuilderFactory() {
        RTFImageChunkBuilder = null;
        RTFTextChunkBuilder = null;
        RTFDataChunkBuilder = null;
    }
    
    /**
     * This method returns the appropriate chunk builder reference for the 
     * entered explanation chunk. If, for example, a DataExplanationChunk was 
     * entered as an argument, the method would return a RTFDataChunkBuilder 
     * instance.
     *
     * It is necessary to state that chunk builder instances are
     * "lazy initialized" and cached (as attributes) while the factory instance
     * exists. This means that, for example, RTFImageChunkBuilder attribute is 
     * null when the factory is created and initialized only when the first
     * RTFImageChunkBuilder instance is needed and not before. In all subsequent
     * calls when this method is supposed to return a RTFImageChunkBuilder
     * instance it returns the reference to the already initialized
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
            if (RTFTextChunkBuilder == null) {
                RTFTextChunkBuilder = new RTFTextChunkBuilder();
            }

            return RTFTextChunkBuilder;
        }

        if (echunk instanceof ImageExplanationChunk) {
            if (RTFImageChunkBuilder == null) {
                RTFImageChunkBuilder = new RTFImageChunkBuilder();
            }

            return RTFImageChunkBuilder;
        }

        if (echunk instanceof DataExplanationChunk) {
            if (RTFDataChunkBuilder == null) {
                RTFDataChunkBuilder = new RTFDataChunkBuilder();
            }

            return RTFDataChunkBuilder;
        }

        throw new ExplanationException("Chunk type '" + echunk.getClass().getName() + "' was not recognized");
    }



}
