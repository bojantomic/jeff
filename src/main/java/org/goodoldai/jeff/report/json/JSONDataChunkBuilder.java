package org.goodoldai.jeff.report.json;

import java.util.ArrayList;
import java.util.Iterator;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A concrete builder for transforming data explanation chunks into pieces 
 * of JSON report.
 * 
 * @author Marko Popovic
 *
 */
public class JSONDataChunkBuilder implements ReportChunkBuilder{

	/**
	 * Initializes the builder
	 */
	public JSONDataChunkBuilder(){
	}

	/**
	 * This method transforms a data explanation chunk into an JSON report piece 
	 * and writes this piece into the provided Json object which is, in this
	 * case, an instance of com.google.gson.JsonObject. The method first make instance 
	 * of com.google.gson.JsonArray (which is an atribute in provided Json object).After that,
	 * collects all general chunk data (context, rule, group, tags) and inserts them into 
	 * the new Json object, and then retrieves the chunk content. Since the content can 
	 * be a SingleData, OneDimData, TwoDimData or a ThreeDimData instance,
	 * dimension details and concrete data are transformed and inserted into the 
	 * Json object(which is make in method, not provided).
	 * Json object(which is make in method,not provided) is insert into an instance of 
	 * com.google.gson.JsonArray(which is make earlier in method). 
	 *
	 * In all cases, if the dimension unit is missing it should be omitted from
	 * the report.
	 * 
	 * In the case of SingleData the Json object in Json array  look like this:          
	 * {
	 *  "type": data,
	 *  "subtype": single,
	 * 	dimensionName: temperature,
	 *  dimensiomUnit: C,
	 *  value: 17,
	 * }    
	 * 
	 * In the case of OneDimData the Json object in Json array  look like this:
	 * {
	 *  "type": data,
	 *  "subtype": one,
	 * 	dimensionName: temperature,
	 *  dimensiomUnit: C,
	 *  "values": [
	 *  	{
          		"value": "18"
        	},
        	{
          		"value": "21"
        	}
	 *  ] 
	 * }                                                                  
	 *                                                                      
	 * In the case of TwoDimData the Json object in Json array  look like this:
	 * {
	 *  "type": data,
	 *  "subtype": two,
	 * 	dimensionName: temperature,
	 *  dimensiomUnit: C,
	 *  "values": [
	 *  	{
          		"value1": "18",
          		"value2": "Moscow"
        	},
        	{
          		"value1": "21",
          		"value2": "Oslo"
        	}
	 *  ] 
	 * }                                                                 
	 *
	 *In the case of ThreeDimData the Json object in Json array  look like this:
	 *{
	 *  "type": data,
	 *  "subtype": three,
	 * 	dimensionName: temperature,
	 *  dimensiomUnit: C,
	 *  "values": [
	 *  	{
          		"value1": "18",
          		"value2": "Berlin",
          		"value3": "Germany"
        	},
        	{
          		"value1": "21",
          		"value2": "Paris,
          		"value3": "France"
        	}
	 *  ] 
	 * }      
	 *
	 * @param echunk data explanation chunk that needs to be transformed
	 * @param stream output stream to which the transformed chunk will be
	 * written(in this case com.google.gson.JsonObject)
	 * @param insertHeaders denotes if chunk headers should be inserted into the
	 * report (true) or not (false)
	 *
	 * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
	 * null, if the entered chunk is not a DataExplanationChunk instance or if 
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

		if (!(echunk instanceof DataExplanationChunk)) {
			throw new ExplanationException("The ExplanationChunk must be type of DataExplanationChunk");
		}

		if (!(stream instanceof JsonObject)) {
			throw new ExplanationException("The argument 'stream' must be the type of com.google.gson.JsonObject");
		}

		JsonArray jsonArray = (JsonArray) ( (JsonObject) stream ).get("chunks") ;
		JsonObject jsonObj = new JsonObject();

		jsonObj.addProperty("type", "data");
		jsonObj = insertSubType((DataExplanationChunk) echunk, jsonObj);

		if(insertHeaders){
			jsonObj = JSONChunkUtility.insertExplanationInfo(echunk, jsonObj);
		}

		DataExplanationChunk dataExplanationChunk = (DataExplanationChunk) echunk;
		jsonObj = insertContent(dataExplanationChunk, jsonObj);

		jsonArray.add(jsonObj);
	}

	/**
	 * This is a private method that is used to insert subtype into the Json object.
	 * Subtype depends on instance of parameter dataExplanationChunk.
	 * @param dataExplanationChunk explanation chunks whose subtype is inserted in Json object
	 * @param Json object in which atribut is inserted
	 * @return Json object with inserted subtype
	 */
	private JsonObject insertSubType(DataExplanationChunk dataExplanationChunk, JsonObject jsonObj){

		if (dataExplanationChunk.getContent() instanceof SingleData) {
			jsonObj.addProperty("subtype", "single");
			return jsonObj;

		} else if (dataExplanationChunk.getContent() instanceof OneDimData) {
			jsonObj.addProperty("subtype", "one");
			return jsonObj;

		} else if (dataExplanationChunk.getContent() instanceof TwoDimData) {
			jsonObj.addProperty("subtype", "two");
			return jsonObj;

		} else {
			jsonObj.addProperty("subtype", "three");
			return jsonObj;
		}
	}

	/**
	 * This is a private method that is used to insert content into the Json object,
	 * this method first checks to see what type of content is (SingleData, OneDimData,
	 * TwoDimData or a ThreeDimData ) and than calls the right method to insert
	 * the content into the Json object (an instance of com.google.gson.JsonObject)
	 *
	 * @param dataExplanationChunk data explanation chunk which holds the content
	 * that needs to be transformed
	 * @param jsonObj Json object in which the content of the transformed chunk will be
	 * written (in this case com.google.gson.JsonObject)
	 * @return Json object with inserted content.
	 */
	private JsonObject insertContent(DataExplanationChunk dataExplanationChunk, JsonObject jsonObj) {

		if (dataExplanationChunk.getContent() instanceof SingleData) {
			return inputSingleDataContent(dataExplanationChunk, jsonObj);

		} else if (dataExplanationChunk.getContent() instanceof OneDimData) {
			return inputOneDimDataContent(dataExplanationChunk, jsonObj);

		} else if (dataExplanationChunk.getContent() instanceof TwoDimData) {
			return inputTwoDimDataContent(dataExplanationChunk, jsonObj);

		} else {
			return inputThreeDimDataContent(dataExplanationChunk, jsonObj);
		}
	}

	/**
	 * This is a private method that is used to insert content into the Json object.
	 * The content must be the instance of explanation.data.ThreeDimData
	 *
	 * @param dataExplenationChunk data explanation chunk which holds the content
	 * that needs to be transformed
	 * @param jsonObj  Json object in which the content of the transformed chunk will be
	 * written (in this case com.google.gson.JsonObject)
	 * @return Json object with inserted content.
	 */
	private JsonObject inputThreeDimDataContent(DataExplanationChunk dataExplanationChunk, JsonObject jsonObj) {

		ThreeDimData threeDimData = (ThreeDimData) dataExplanationChunk.getContent();

		Dimension dimension1 = threeDimData.getDimension1();
		String dimensionName1 = dimension1.getName();
		String dimensionUnit1 = dimension1.getUnit();

		Dimension dimension2 = threeDimData.getDimension2();
		String dimensionName2 = dimension2.getName();
		String dimensionUnit2 = dimension2.getUnit();

		Dimension dimension3 = threeDimData.getDimension3();
		String dimensionName3 = dimension3.getName();
		String dimensionUnit3 = dimension3.getUnit();

		if(dimensionName1 != null){
			jsonObj.addProperty("dimensionName1", dimensionName1);
		}

		if(dimensionUnit1 != null){
			jsonObj.addProperty("dimensionUnit1", dimensionUnit1);
		}

		if(dimensionName2 != null){
			jsonObj.addProperty("dimensionName2", dimensionName2);
		}

		if(dimensionUnit2 != null){
			jsonObj.addProperty("dimensionUnit2", dimensionUnit2);
		}

		if(dimensionName3 != null){
			jsonObj.addProperty("dimensionName3", dimensionName3);
		}

		if(dimensionUnit3 != null){
			jsonObj.addProperty("dimensionUnit3", dimensionUnit3);
		}

		ArrayList<Triple> tripleValues = threeDimData.getValues();
		JsonArray jsonArray = new JsonArray();

		Iterator<Triple> iterator = tripleValues.iterator();
		while(iterator.hasNext()){
			Triple triple = iterator.next();
			JsonObject obj = new JsonObject();

			obj.addProperty("value1", String.valueOf(triple.getValue1()));
			obj.addProperty("value2", String.valueOf(triple.getValue2()));
			obj.addProperty("value3", String.valueOf(triple.getValue3()));

			jsonArray.add(obj);
		}
		jsonObj.add("values", jsonArray);

		return jsonObj;
	}

	/**
	 * This is a private method that is used to insert content into the Json object.
	 * The content must be the instance of explanation.data.TwoDimData
	 *
	 * @param dataExplenationChunk data explanation chunk which holds the content
	 * that needs to be transformed
	 * @param jsonObj Json object in which the content of the transformed chunk will be
	 * written (in this case com.google.gson.JsonObject)
	 * @return Json object with inserted content.
	 */
	private JsonObject inputTwoDimDataContent(DataExplanationChunk dataExplanationChunk, JsonObject jsonObj) {

		TwoDimData twoDimData = (TwoDimData) dataExplanationChunk.getContent();

		Dimension dimension1 = twoDimData.getDimension1();
		String dimensionName1 = dimension1.getName();
		String dimensionUnit1 = dimension1.getUnit();

		Dimension dimension2 = twoDimData.getDimension2();
		String dimensionName2 = dimension2.getName();
		String dimensionUnit2 = dimension2.getUnit();

		if(dimensionName1 != null){
			jsonObj.addProperty("dimensionName1", dimensionName1);
		}

		if(dimensionUnit1 != null){
			jsonObj.addProperty("dimensionUnit1", dimensionUnit1);
		}

		if(dimensionName2 != null){
			jsonObj.addProperty("dimensionName2", dimensionName2);
		}

		if(dimensionUnit2 != null){
			jsonObj.addProperty("dimensionUnit2", dimensionUnit2);
		}

		ArrayList<Tuple> tupleValues = twoDimData.getValues();
		JsonArray jsonArray = new JsonArray();

		Iterator<Tuple> iterator = tupleValues.iterator();
		while(iterator.hasNext()){
			Tuple tuple = iterator.next();
			JsonObject obj = new JsonObject();

			obj.addProperty("value1", String.valueOf(tuple.getValue1()));
			obj.addProperty("value2", String.valueOf(tuple.getValue2()));

			jsonArray.add(obj);
		}
		jsonObj.add("values", jsonArray);

		return jsonObj;
	}

	/**
	 * This is a private method that is used to insert content into the JsonObject.
	 * The content must be the instance of explanation.data.OneDimData
	 *
	 * @param dataExplenationChunk data explanation chunk which holds the content
	 * that needs to be transformed
	 * @param jsonObj Json object in which the content of the transformed chunk will be
	 * written (in this case com.google.gson.JsonObject)
	 * @return Json object with inserted content.
	 */
	private JsonObject inputOneDimDataContent(DataExplanationChunk dataExplanationChunk, JsonObject jsonObj) {

		OneDimData oneDimData = (OneDimData) dataExplanationChunk.getContent();

		Dimension dimension = oneDimData.getDimension();
		String dimensionName = dimension.getName();
		String dimensionUnit = dimension.getUnit();
		if(dimensionName != null){
			jsonObj.addProperty("dimensionName", dimensionName);
		}

		if(dimensionUnit != null){
			jsonObj.addProperty("dimensionUnit", dimensionUnit);
		}

		ArrayList<Object> objectValues = oneDimData.getValues();
		JsonArray jsonArray = new JsonArray();

		Iterator<Object> iterator = objectValues.iterator();
		while (iterator.hasNext()) {
			JsonObject obj = new JsonObject();
			obj.addProperty("value", String.valueOf(iterator.next()));

			jsonArray.add(obj);
		}
		jsonObj.add("values", jsonArray);

		return jsonObj;
	}

	/**
	 * This is a private method that is used to insert content into the json object.
	 * The content must be the instance of explanation.data.SingleData
	 *
	 * @param dataExplenationChunk data explanation chunk which holds the content
	 * that needs to be transformed
	 * @param jsonObj Json object in which the content of the transformed chunk will be
	 * written (in this case com.google.gson.JsonObject)
	 * @return Json object with inserted content.
	 */
	private JsonObject inputSingleDataContent(DataExplanationChunk dataExplanationChunk, JsonObject jsonObj) {

		SingleData singleData = (SingleData) dataExplanationChunk.getContent();

		String value = String.valueOf(singleData.getValue());

		Dimension dimension = singleData.getDimension();
		String dimensionName = dimension.getName();
		String dimensionUnit = dimension.getUnit();

		if(dimensionName != null){
			jsonObj.addProperty("dimensionName", dimensionName);
		}

		if(dimensionUnit != null){
			jsonObj.addProperty("dimensionUnit", dimensionUnit);
		}

		if(value != null){
			jsonObj.addProperty("value", value);
		}

		return jsonObj;
	}
}
