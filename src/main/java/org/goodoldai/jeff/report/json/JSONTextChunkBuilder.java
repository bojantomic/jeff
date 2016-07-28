package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * A concrete builder for transforming text explanation chunks into pieces
 * of JSON report
 *
 * @author Dusan Ignjatovic
 */
public class JSONTextChunkBuilder implements ReportChunkBuilder{

	/**
     * Initializes the builder
     */
    public JSONTextChunkBuilder() {
    }
    /**
     * This method transforms a text explanation chunk into an JSON report piece 
     * and writes this piece into the provided JSON document which is, in this
     * case, an instance of com.google.gson.JsonObject. The method first collects all
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content is, 
     * in this case, a string, it also gets inserted into the report.
     *
     * @param echunk text explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written in as an JSON object (in this case com.google.gson.JsonObject)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a TextExplanationChunk instance or if 
     * the entered output stream type is not com.google.gson.JsonObject
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

	        if (!(echunk instanceof TextExplanationChunk)) {
	            throw new ExplanationException("The ExplanationChunk must be the type of TextExplanationChunk");
	        }

	        if (!(stream instanceof JsonObject)) {
	            throw new ExplanationException("The stream must be the type of com.google.gson.JsonObject");
	        }
	        
	        JsonObject object = (JsonObject) stream;
	        JsonArray explanation = (JsonArray)object.get("explanation");
	        
	        JsonObject jsonChunk = new JsonObject();
	        object.addProperty("type", "text");
	        
	        if (insertHeaders) {
				JSONChunkUtility.insertExplanationInfo(echunk, jsonChunk);
			}
	        
	        TextExplanationChunk textExplenationChunk = (TextExplanationChunk) echunk;

	        insertContent(textExplenationChunk, jsonChunk);
	        
	        explanation.add(jsonChunk);
	}
	/**
     * This is a private method that is used to insert content into the JSON chunk
     *
     * @param textExplenationChunk text explanation chunk which holds the content
     * that needs to be transformed
     * @param object element in which the content of the transformed chunk will be
     * written in as an JSON object (in this case com.google.gson.JsonObject)
     */
	private void insertContent(TextExplanationChunk textExplenationChunk, JsonObject object) {
		String content = String.valueOf(textExplenationChunk.getContent());
		
		object.addProperty("content", content);
	}
	
}
