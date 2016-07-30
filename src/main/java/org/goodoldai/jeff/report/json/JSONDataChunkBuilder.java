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
 * A concrete builder for transforming data explanation chunks into pieces of
 * JSON report
 *
 * @author Dusan Ignjatovic
 */
public class JSONDataChunkBuilder implements ReportChunkBuilder {

	/**
	 * Initializes the builder
	 */
	public JSONDataChunkBuilder() {
	}

	/**
	 * This method transforms a data explanation chunk into an JSON report piece
	 * and writes this piece into the provided JSON document which is, in this
	 * case, an instance of com.google.gson.JsonObject. The method first
	 * collects all general chunk data (context, rule, group, tags) and inserts
	 * them into the report, and then retrieves the chunk content. Since the
	 * content can be a SingleData, OneDimData, TwoDimData or a ThreeDimData
	 * instance, dimension details and concrete data are transformed into JSON
	 * and inserted.
	 *
	 * In all cases, if the dimension unit is missing it should be omitted from
	 * the report.
	 *
	 * @param echunk
	 *            data explanation chunk that needs to be transformed
	 * @param stream
	 *            output stream to which the transformed chunk will be written
	 *            in as an JSON object (in this case com.google.gson.JsonObject)
	 * @param insertHeaders
	 *            denotes if chunk headers should be inserted into the report
	 *            (true) or not (false)
	 *
	 * @throws org.goodoldai.jeff.explanation.ExplanationException
	 *             if any of the arguments are null, if the entered chunk is not
	 *             a DataExplanationChunk instance or if the entered output
	 *             stream type is not com.google.gson.JsonObject
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
			throw new ExplanationException("The stream must be the type of com.google.gson.JsonObject");
		}

		JsonObject object = (JsonObject) stream;
		// getting explanation array of chunks
		JsonArray explanation = (JsonArray) object.get("explanation");

		JsonObject jsonChunk = new JsonObject();
		// getting explanation array of chunks
		jsonChunk.addProperty("type", "data");

		if (insertHeaders) {
			JSONChunkUtility.insertExplanationInfo(echunk, jsonChunk);
		}

		DataExplanationChunk dataExplenationChunk = (DataExplanationChunk) echunk;

		insertContent(dataExplenationChunk, jsonChunk);

		explanation.add(jsonChunk);
	}

	/**
	 * This is a private method that is used to insert content into the
	 * document, this method first checks to see what type of content is
	 * (SingleData, OneDimData, TwoDimData or a ThreeDimData ), adds sub type to
	 * JSON report chunk and than calls the right method to insert the content
	 * into the document (an instance of com.google.gson.JsonObject)
	 *
	 * @param imageExplanationChunk
	 *            image explanation chunk which holds the content that needs to
	 *            be transformed
	 * @param element
	 *            element in which the content of the transformed chunk will be
	 *            written in as an JSON object (in this case
	 *            com.google.gson.JsonObject)
	 */
	private void insertContent(DataExplanationChunk dataExplenationChunk, JsonObject jsonChunk) {
		if (dataExplenationChunk.getContent() instanceof SingleData) {
			jsonChunk.addProperty("subtype", "singleData");
			inputSingleDataContent(dataExplenationChunk, jsonChunk);

		} else if (dataExplenationChunk.getContent() instanceof OneDimData) {
			jsonChunk.addProperty("subtype", "OneDimData");
			inputOneDimDataContent(dataExplenationChunk, jsonChunk);

		} else if (dataExplenationChunk.getContent() instanceof TwoDimData) {
			jsonChunk.addProperty("subtype", "TwoDimData");
			inputTwoDimDataContent(dataExplenationChunk, jsonChunk);

		} else if (dataExplenationChunk.getContent() instanceof ThreeDimData) {
			jsonChunk.addProperty("subtype", "singleData");
			inputThreeDimDataContent(dataExplenationChunk, jsonChunk);
		}
	}

	/**
	 * This is a private method that is used to insert content into the
	 * document. The content must be the instance of explanation.data.SingleData
	 *
	 * @param dataExplenationChunk
	 *            data explanation chunk which holds the content that needs to
	 *            be transformed
	 * @param element
	 *            element in which the content of the transformed chunk will be
	 *            written in as an JSON object (in this case
	 *            com.google.gson.JsonObject)
	 */
	private void inputSingleDataContent(DataExplanationChunk dataExplenationChunk, JsonObject jsonChunk) {
		SingleData singleData = (SingleData) dataExplenationChunk.getContent();

		String value = String.valueOf(singleData.getValue());

		Dimension dimension = singleData.getDimension();
		String dimensionName = dimension.getName();
		String dimensionUnit = dimension.getUnit();

		jsonChunk.addProperty("dimensionName", dimensionName);

		if (dimensionUnit != null) {
			jsonChunk.addProperty("dimensionUnit", dimensionUnit);
		}
		// add content to explanation report chunk
		jsonChunk.addProperty("content", value);

	}

	/**
	 * This is a private method that is used to insert content into the
	 * document. The content must be the instance of explanation.data.OneDimData
	 *
	 * @param dataExplenationChunk
	 *            data explanation chunk which holds the content that needs to
	 *            be transformed
	 * @param element
	 *            element in which the content of the transformed chunk will be
	 *            written in as an JSON object (in this case
	 *            com.google.gson.JsonObject)
	 */
	private void inputOneDimDataContent(DataExplanationChunk dataExplenationChunk, JsonObject jsonChunk) {
		OneDimData oneDimData = (OneDimData) dataExplenationChunk.getContent();

		ArrayList<Object> objectValues = oneDimData.getValues();
		ArrayList<String> values = new ArrayList<String>();

		for (Iterator<Object> it = objectValues.iterator(); it.hasNext();) {
			Object object = it.next();
			values.add(String.valueOf(object));
		}

		Dimension dimension = oneDimData.getDimension();
		String dimensionName = dimension.getName();
		String dimensionUnit = dimension.getUnit();

		// adding one dimension values to JSON array
		JsonArray jsonValue = new JsonArray();
		for (Iterator<String> it = values.iterator(); it.hasNext();) {
			String value = it.next();
			jsonValue.add(value);
		}

		jsonChunk.addProperty("dimensionName", dimensionName);

		if (dimensionUnit != null) {
			jsonChunk.addProperty("dimensionUnit", dimensionUnit);
		}
		// add content to explanation report chunk
		jsonChunk.add("content", jsonValue);

	}

	/**
	 * This is a private method that is used to insert content into the
	 * document. The content must be the instance of explanation.data.TwoDimData
	 *
	 * @param dataExplenationChunk
	 *            data explanation chunk which holds the content that needs to
	 *            be transformed
	 * @param element
	 *            element in which the content of the transformed chunk will be
	 *            written in as an JSON object (in this case
	 *            com.google.gson.JsonObject)
	 */
	private void inputTwoDimDataContent(DataExplanationChunk dataExplenationChunk, JsonObject jsonChunk) {
		TwoDimData twoDimData = (TwoDimData) dataExplenationChunk.getContent();

		ArrayList<Tuple> tupleValues = twoDimData.getValues();

		Dimension dimension1 = twoDimData.getDimension1();
		String dimensionName1 = dimension1.getName();
		String dimensionUnit1 = dimension1.getUnit();

		Dimension dimension2 = twoDimData.getDimension2();
		String dimensionName2 = dimension2.getName();
		String dimensionUnit2 = dimension2.getUnit();

		// add dimensions property to explanation report chunk
		jsonChunk.addProperty("dimensionName1", dimensionName1);
		if (dimensionUnit1 != null) {
			jsonChunk.addProperty("dimensionUnit1", dimensionUnit1);
		}
		jsonChunk.addProperty("dimensionName2", dimensionName2);
		if (dimensionUnit2 != null) {
			jsonChunk.addProperty("dimensionUnit2", dimensionUnit2);
		}

		JsonArray jsonTuples = new JsonArray();
		for (Iterator<Tuple> it = tupleValues.iterator(); it.hasNext();) {
			Tuple tuple = it.next();
			// getting tuple values
			String value1 = String.valueOf(tuple.getValue1());
			String value2 = String.valueOf(tuple.getValue2());
			// create tuple
			JsonObject jsonTuple = new JsonObject();
			jsonTuple.addProperty("value1", value1);
			jsonTuple.addProperty("value2", value2);
			// add tuple to array
			jsonTuples.add(jsonTuple);
		}
		// add content to explanation report chunk
		jsonChunk.add("content", jsonTuples);
	}

	/**
	 * This is a private method that is used to insert content into the
	 * document. The content must be the instance of
	 * explanation.data.ThreeDimData
	 *
	 * @param dataExplenationChunk
	 *            data explanation chunk which holds the content that needs to
	 *            be transformed
	 * @param element
	 *            element in which the content of the transformed chunk will be
	 *            written in as an JSON object (in this case
	 *            com.google.gson.JsonObject)
	 */
	private void inputThreeDimDataContent(DataExplanationChunk dataExplenationChunk, JsonObject jsonChunk) {
		ThreeDimData threeDimData = (ThreeDimData) dataExplenationChunk.getContent();

		ArrayList<Triple> tripleValues = threeDimData.getValues();

		Dimension dimension1 = threeDimData.getDimension1();
		String dimensionName1 = dimension1.getName();
		String dimensionUnit1 = dimension1.getUnit();

		Dimension dimension2 = threeDimData.getDimension2();
		String dimensionName2 = dimension2.getName();
		String dimensionUnit2 = dimension2.getUnit();

		Dimension dimension3 = threeDimData.getDimension3();
		String dimensionName3 = dimension3.getName();
		String dimensionUnit3 = dimension3.getUnit();

		// add dimensions property to explanation report chunk
		jsonChunk.addProperty("dimensionName1", dimensionName1);
		if (dimensionUnit1 != null) {
			jsonChunk.addProperty("dimensionUnit1", dimensionUnit1);
		}

		jsonChunk.addProperty("dimensionName2", dimensionName2);
		if (dimensionUnit2 != null) {
			jsonChunk.addProperty("dimensionUnit2", dimensionUnit2);
		}

		jsonChunk.addProperty("dimensionName3", dimensionName3);
		if (dimensionUnit3 != null) {
			jsonChunk.addProperty("dimensionUnit3", dimensionUnit3);
		}

		JsonArray jsonTriples = new JsonArray();
		for (Iterator<Triple> it = tripleValues.iterator(); it.hasNext();) {
			Triple triple = it.next();
			// getting triple values
			String value1 = String.valueOf(triple.getValue1());
			String value2 = String.valueOf(triple.getValue2());
			String value3 = String.valueOf(triple.getValue3());
			// create triple
			JsonObject jsonTriple = new JsonObject();
			jsonTriple.addProperty("value1", value1);
			jsonTriple.addProperty("value2", value2);
			jsonTriple.addProperty("value3", value3);
			// add triple to array
			jsonTriples.add(jsonTriple);
		}
		// add content to explanation report chunk
		jsonChunk.add("content", jsonTriples);
	}

}
