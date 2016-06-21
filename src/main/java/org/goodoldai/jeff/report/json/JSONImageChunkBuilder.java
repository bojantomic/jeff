package org.goodoldai.jeff.report.json;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A concrete builder for transforming image explanation chunks into pieces 
 * of JSON report.
 * 
 * @author Marko Popovic
 *
 */
public class JSONImageChunkBuilder implements ReportChunkBuilder{

	/**
	 * Initializes the builder
	 */
	public JSONImageChunkBuilder(){
	}

	/**
	 * This method transforms a image explanation chunk into an JSON report piece 
	 * and writes this piece into the provided Json object which is, in this
	 * case, an instance of com.google.gson.JsonObject. The method first make instance 
	 * of com.google.gson.JsonArray (which is an atribute in provided Json object).After that,
	 * collects all general chunk data (context, rule, group, tags) and inserts them into 
	 * the new Json object, and then retrieves the chunk content. Since the content 
	 * is, in this case, an ImageData instance, and since images cannot be 
	 * displayed in json files, only the image data (caption and URL) gets 
	 * inserted into the Json object. If the image caption is missing, it doesn't 
	 * get inserted into Json object(which is make, not provided).
	 * Json object(which is make in method,not provided) is insert into an instance of 
	 * com.google.gson.JsonArray(which is make earlier in method). 
	 *
	 *Json object in Json array look like this:          
	 * {
	 *  "type": "image",
	 *  "url": "value"        
	 * 	"caption": "value",
	 * }
	 *
	 * @param echunk image explanation chunk that needs to be transformed
	 * @param stream output stream to which the transformed chunk will be
	 * written in as an json object (in this case com.google.gson.JsonObject)
	 * @param insertHeaders denotes if chunk headers should be inserted into the
	 * report (true) or not (false)
	 *
	 * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
	 * null, if the entered chunk is not a ImageExplanationChunk instance or if 
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

		if (!(echunk instanceof ImageExplanationChunk)) {
			throw new ExplanationException("The ExplanationChunk must be type of ImageExplanationChunk");
		}

		if (!(stream instanceof JsonObject)) {
			throw new ExplanationException("The argument 'stream' must be the type of com.google.gson.JsonObject");
		}

		JsonArray jsonArray = (JsonArray) ( (JsonObject) stream ).get("chunks") ;
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("type", "image");

		if(insertHeaders){
			jsonObj = JSONChunkUtility.insertExplanationInfo(echunk, jsonObj);
		}

		ImageExplanationChunk chunk = (ImageExplanationChunk) echunk;

		jsonObj = insertContent(chunk, jsonObj);

		jsonArray.add(jsonObj);

	}

	/**
	 * This is a private method that is used to insert content into the Json object.
	 * @param chunk image explanation chunk which holds the content
	 * that needs to be transformed
	 * @param jsonObj Json object in which the content of the transformed chunk will be
	 * written in as an json object (in this case com.google.gson.JsonObject)
	 * @return Json object with inserted content
	 */
	private JsonObject insertContent(ImageExplanationChunk chunk, JsonObject jsonObj) {

		ImageData content = (ImageData) chunk.getContent();

		String url = content.getURL();
		String caption = content.getCaption();

		if(url != null){
			jsonObj.addProperty("url", url);
		}

		if(caption != null){
			jsonObj.addProperty("caption", caption);
		}

		return jsonObj;
	}
}
