package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A concrete builder for transforming text explanation chunks into pieces
 * of JSON report.
 * 
 * @author Marko Popovic
 *
 */
public class JSONTextChunkBuilder implements ReportChunkBuilder{

	/**
     * Initializes the builder
     */
	public JSONTextChunkBuilder(){
	}
	
	/**
     * This method transforms a text explanation chunk into an JSON report piece 
     * and writes this piece into the provided Json object which is, in this
     * case, an instance of com.google.gson.JsonObject. The method first make instance 
     * of com.google.gson.JsonArray (which is an atribute in provided Json object).After that,
     * collects all general chunk data (context, rule, group, tags) and inserts them into 
     * the new Json object, and then retrieves the chunk content. Since the content is, 
     * in this case, a string, it also gets inserted into the Json object(which is make, not 
     * provided).Json object(which is make in method,not provided) is insert into an instance of 
     * com.google.gson.JsonArray(which is make earlier in method). 
     *
     * Json object in Json array look like this:          
     * {
     *  "type": "text",
     * 	"content": "value"        
     * }
     * @param echunk text explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written in as an json object (in this case com.google.gson.JsonObject)
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
        if(!(stream instanceof JsonObject)){
        	throw new ExplanationException("The argument 'stream' must be the type of com.google.gson.JsonObject");
        }
        
        JsonArray jsonArray = (JsonArray) ( (JsonObject) stream ).get("chunks") ;
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("type", "text");
        
        if(insertHeaders){
        	jsonObj = JSONChunkUtility.insertExplanationInfo(echunk, jsonObj);
        }
        
        TextExplanationChunk chunk = (TextExplanationChunk) echunk;
        
        String content = String.valueOf(chunk.getContent());
		jsonObj.addProperty("content", content);
		
		jsonArray.add(jsonObj);
	}
}
