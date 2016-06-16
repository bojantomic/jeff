package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 
 * @author Marko Popovic
 *
 */
public class JSONDataChunkBuilderTest extends AbstractJeffTest{

	DataExplanationChunk singleDataChunk1;
	DataExplanationChunk singleDataChunk2;
	DataExplanationChunk oneDimDataChunk;
	DataExplanationChunk twoDimDataChunk;
	DataExplanationChunk threeDimDataChunk;
	JsonObject jsonObject;
	JSONDataChunkBuilder jsonDataChunkBuilder;

	/**
	 * Creates a explanation.DataExplanationChunk and com.google.gson.JsonObject
	 *  instance that are used for testing.
	 */ 
	@Before
	public void setUp() throws Exception {
		super.setUp();

		jsonDataChunkBuilder = new JSONDataChunkBuilder();

		String[] tags = {"tag1", "tag2"};
		singleDataChunk1 = new DataExplanationChunk(new SingleData(new Dimension("testName"), "value"));
		singleDataChunk2 = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
				new SingleData(new Dimension("testName", "testUnit"), "value"));

		ArrayList<Object> values = new ArrayList<Object>();
		values.add("value1");
		values.add("value2");
		oneDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
				new OneDimData(new Dimension("testName", "testUnit"), values));


		ArrayList<Tuple> tupleValues = new ArrayList<Tuple>();
		tupleValues.add(new Tuple("value1", "value2"));
		twoDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
				new TwoDimData(new Dimension("testName1", "testUnit1"), new Dimension("testName2", "testUnit2"), tupleValues));

		ArrayList<Triple> tripleValues = new ArrayList<Triple>();
		tripleValues.add(new Triple("value1", "value2", "value3"));
		threeDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags, new ThreeDimData(
				new Dimension("testName1", "testUnit1"),
				new Dimension("testName2", "testUnit2"),
				new Dimension("testName3", "testUnit3"),
				tripleValues));

		jsonObject = new JsonObject();
	}

	@After
	public void tearDown() throws Exception {
		jsonDataChunkBuilder = null;

		singleDataChunk1 = null;
		singleDataChunk2 = null;
		oneDimDataChunk = null;
		twoDimDataChunk = null;
		threeDimDataChunk = null;

		jsonObject = null;
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the null arguments.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingAllArguments() {
		jsonDataChunkBuilder.buildReportChunk(null, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the first null argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingFirstArgumant() {
		jsonDataChunkBuilder.buildReportChunk(null, oneDimDataChunk, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the second null argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingSecondArgumant() {
		jsonDataChunkBuilder.buildReportChunk(oneDimDataChunk, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the wrong
	 * type of the first argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeFirsArgumant() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(new ImageExplanationChunk(new ImageData("image1.jpg")), jsonObject, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the wrong
	 * type of the second argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeSecondArgumant() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(oneDimDataChunk, new TextExplanationChunk("test"), false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that only has content.
	 */
	@Test
	public void testBuildReportChunkFirstConstructorSingleData() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk1, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(data) explanation chunk
		assertEquals("data", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("single", ( (JsonObject) jsonArray.get(0) ).get("subtype").getAsString());
		assertEquals("informational", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testName", ( (JsonObject) jsonArray.get(0) ).get("dimensionName").getAsString());
		assertEquals("value", ( (JsonObject) jsonArray.get(0) ).get("value").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is SingleDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorSingleData() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk2, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(data) explanation chunk
		assertEquals("data", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("single", ( (JsonObject) jsonArray.get(0) ).get("subtype").getAsString());
		assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testRule", ( (JsonObject) jsonArray.get(0) ).get("rule").getAsString());
		assertEquals("testGroup", ( (JsonObject) jsonArray.get(0) ).get("group").getAsString());

		JsonArray array = ( (JsonObject) jsonArray.get(0) ).get("tags").getAsJsonArray();
		assertEquals("tag1", ( (JsonObject) array.get(0) ).get("value").getAsString());
		assertEquals("tag2", ( (JsonObject) array.get(1) ).get("value").getAsString());

		assertEquals("testName", ( (JsonObject) jsonArray.get(0) ).get("dimensionName").getAsString());
		assertEquals("testUnit", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit").getAsString());
		assertEquals("value", ( (JsonObject) jsonArray.get(0) ).get("value").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is SingleDimDataChunk - but no chunk
	 * headers are inserted.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorSingleDataNoChunkHeaders() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk2, jsonObject, false);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(data) explanation chunk
		assertEquals("data", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("single", ( (JsonObject) jsonArray.get(0) ).get("subtype").getAsString());
		assertEquals("testName", ( (JsonObject) jsonArray.get(0) ).get("dimensionName").getAsString());
		assertEquals("testUnit", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit").getAsString());
		assertEquals("value", ( (JsonObject) jsonArray.get(0) ).get("value").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is OneDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorOneDimData() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(oneDimDataChunk, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(data) explanation chunk
		assertEquals("data", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("one", ( (JsonObject) jsonArray.get(0) ).get("subtype").getAsString());
		assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testRule", ( (JsonObject) jsonArray.get(0) ).get("rule").getAsString());
		assertEquals("testGroup", ( (JsonObject) jsonArray.get(0) ).get("group").getAsString());

		JsonArray array = ( (JsonObject) jsonArray.get(0) ).get("tags").getAsJsonArray();
		assertEquals("tag1", ( (JsonObject) array.get(0) ).get("value").getAsString());
		assertEquals("tag2", ( (JsonObject) array.get(1) ).get("value").getAsString());

		assertEquals("testName", ( (JsonObject) jsonArray.get(0) ).get("dimensionName").getAsString());
		assertEquals("testUnit", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit").getAsString());

		JsonArray valuesArray = ( (JsonObject) jsonArray.get(0) ).get("values").getAsJsonArray();
		assertEquals("value1", ( (JsonObject) valuesArray.get(0) ).get("value").getAsString());
		assertEquals("value2", ( (JsonObject) valuesArray.get(1) ).get("value").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is TwoDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorTwoDimData() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(twoDimDataChunk, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(data) explanation chunk
		assertEquals("data", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("two", ( (JsonObject) jsonArray.get(0) ).get("subtype").getAsString());
		assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testRule", ( (JsonObject) jsonArray.get(0) ).get("rule").getAsString());
		assertEquals("testGroup", ( (JsonObject) jsonArray.get(0) ).get("group").getAsString());

		JsonArray array = ( (JsonObject) jsonArray.get(0) ).get("tags").getAsJsonArray();
		assertEquals("tag1", ( (JsonObject) array.get(0) ).get("value").getAsString());
		assertEquals("tag2", ( (JsonObject) array.get(1) ).get("value").getAsString());

		assertEquals("testName1", ( (JsonObject) jsonArray.get(0) ).get("dimensionName1").getAsString());
		assertEquals("testUnit1", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit1").getAsString());
		assertEquals("testName2", ( (JsonObject) jsonArray.get(0) ).get("dimensionName2").getAsString());
		assertEquals("testUnit2", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit2").getAsString());

		JsonArray valuesArray = ( (JsonObject) jsonArray.get(0) ).get("values").getAsJsonArray();
		assertEquals("value1", ( (JsonObject) valuesArray.get(0) ).get("value1").getAsString());
		assertEquals("value2", ( (JsonObject) valuesArray.get(0) ).get("value2").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder.
	 * Test case: successfull insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is ThreeDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorThreeDimData() {
		jsonObject.add("chunks", new JsonArray());
		jsonDataChunkBuilder.buildReportChunk(threeDimDataChunk, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(data) explanation chunk
		assertEquals("data", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("three", ( (JsonObject) jsonArray.get(0) ).get("subtype").getAsString());
		assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testRule", ( (JsonObject) jsonArray.get(0) ).get("rule").getAsString());
		assertEquals("testGroup", ( (JsonObject) jsonArray.get(0) ).get("group").getAsString());

		JsonArray array = ( (JsonObject) jsonArray.get(0) ).get("tags").getAsJsonArray();
		assertEquals("tag1", ( (JsonObject) array.get(0) ).get("value").getAsString());
		assertEquals("tag2", ( (JsonObject) array.get(1) ).get("value").getAsString());

		assertEquals("testName1", ( (JsonObject) jsonArray.get(0) ).get("dimensionName1").getAsString());
		assertEquals("testUnit1", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit1").getAsString());
		assertEquals("testName2", ( (JsonObject) jsonArray.get(0) ).get("dimensionName2").getAsString());
		assertEquals("testUnit2", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit2").getAsString());
		assertEquals("testName3", ( (JsonObject) jsonArray.get(0) ).get("dimensionName3").getAsString());
		assertEquals("testUnit3", ( (JsonObject) jsonArray.get(0) ).get("dimensionUnit3").getAsString());

		JsonArray valuesArray = ( (JsonObject) jsonArray.get(0) ).get("values").getAsJsonArray();
		assertEquals("value1", ( (JsonObject) valuesArray.get(0) ).get("value1").getAsString());
		assertEquals("value2", ( (JsonObject) valuesArray.get(0) ).get("value2").getAsString());
		assertEquals("value3", ( (JsonObject) valuesArray.get(0) ).get("value3").getAsString());
	}
}
