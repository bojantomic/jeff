package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete implementation of the ReportChunkBuilderFactory interface for
 * reports that are supposed to be JSON documents. This class provides 
 * references to the concrete report chunk builder instances for the JSON 
 * report type.
 *
 * @author Dusan Ignjatovic
 */
public class JSONReportChunkBuilderFactory implements ReportChunkBuilderFactory{

	/**
     * An JSONDataChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private JSONDataChunkBuilder JSONDataChunkBuilder;
    /**
     * An JSONImageChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private JSONImageChunkBuilder JSONImageChunkBuilder;
    /**
     * An JSONTextChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private JSONTextChunkBuilder JSONTextChunkBuilder;

    /**
     * Initializes all attributes (chunk builder references) to null.
     */
    public JSONReportChunkBuilderFactory() {
        this.JSONDataChunkBuilder = null;
        this.JSONImageChunkBuilder = null;
        this.JSONTextChunkBuilder = null;
    }
    /**
     * This method returns the appropriate chunk builder instances for the
     * entered explanation chunk. If, for example, a DataExplanationChunk was 
     * entered as an argument, the method would return an JSONDataChunkBuilder 
     * instance.
     *
     * It is necessary to state that chunk builder instances are
     * "lazy initialized" and cached (as attributes) while the factory instance
     * exists. This means that, for example, JSONImageChunkBuilder attribute is 
     * null when the factory is created and initialized only when the first
     * JSONImageChunkBuilder instance is needed and not before. In all
     * subsequent calls when this method is supposed to return a
     * JSONImageChunkBuilder instance it returns the reference to the already
     * initialized JSONImageChunkBuilder object.
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
		if (echunk instanceof DataExplanationChunk) {
			if (JSONDataChunkBuilder == null) {
				return new JSONDataChunkBuilder();
			}
		}
		
		if (echunk instanceof ImageExplanationChunk) {
			if (JSONImageChunkBuilder == null) {
				return new JSONImageChunkBuilder();
			}
		}
		
		if (echunk instanceof TextExplanationChunk) {
			if (JSONTextChunkBuilder == null) {
				return new JSONTextChunkBuilder();
			}
		}
		throw new ExplanationException(
                "The provided ExplanationChunk does not match any of the required types");
	}

}
