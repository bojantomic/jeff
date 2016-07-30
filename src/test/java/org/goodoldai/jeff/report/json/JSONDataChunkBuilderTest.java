package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
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
 * @author Dusan Ignjatovic
 */
public class JSONDataChunkBuilderTest extends AbstractJeffTest {

	DataExplanationChunk singleDataChunk1;
	DataExplanationChunk singleDataChunk2;
	DataExplanationChunk oneDimDataChunk;
	DataExplanationChunk twoDimDataChunk;
	DataExplanationChunk threeDimDataChunk;
	JSONDataChunkBuilder jsonDataChunkBuilder;
	JsonObject object;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		jsonDataChunkBuilder = new JSONDataChunkBuilder();

		String[] tags = { "tag1", "tag2" };

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
		twoDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags, new TwoDimData(
				new Dimension("testName1", "testUnit1"), new Dimension("testName2", "testUnit2"), tupleValues));

		ArrayList<Triple> tripleValues = new ArrayList<Triple>();
		tripleValues.add(new Triple("value1", "value2", "value3"));
		threeDimDataChunk = new DataExplanationChunk(-10, "testGroup", "testRule", tags,
				new ThreeDimData(new Dimension("testName1", "testUnit1"), new Dimension("testName2", "testUnit2"),
						new Dimension("testName3", "testUnit3"), tripleValues));

		object = new JsonObject();
		object.add("explanation", new JsonArray());
	}

	@After
	public void tearDown() throws Exception {
		jsonDataChunkBuilder = null;
		singleDataChunk1 = null;
		singleDataChunk2 = null;
		oneDimDataChunk = null;
		twoDimDataChunk = null;
		threeDimDataChunk = null;
		object = null;
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that only has content.
	 */
	@Test
	public void testBuildReportChunkFirsttConstructorSingleData() {
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk1, object, true);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String subtype = chunk.get("subtype").getAsString();
		String content = chunk.get("content").getAsString();
		String dimensionName = chunk.get("dimensionName").getAsString();
		String context = chunk.get("context").getAsString();

		// checks the type and subtype of chunk
		assertEquals("data", type);
		assertEquals("singleData", subtype);
		// checks the content
		assertEquals("value", content);
		// check dimensions
		assertEquals("testName", dimensionName);
		// checks context
		String expResult = "INFORMATIONAL".toLowerCase();
		assertEquals(expResult, context);

	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is SingleDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorSingleData() {
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk2, object, true);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String subtype = chunk.get("subtype").getAsString();
		String context = chunk.get("context").getAsString();
		String rule = chunk.get("rule").getAsString();
		String group = chunk.get("group").getAsString();
		String content = chunk.get("content").getAsString();
		String dimensionName = chunk.get("dimensionName").getAsString();
		String dimensionUnit = chunk.get("dimensionUnit").getAsString();

		// checks the type and subtype of chunk
		assertEquals("data", type);
		assertEquals("singleData", subtype);
		// checks the optional values
		assertEquals("error", context);
		assertEquals("testRule", rule);
		assertEquals("testGroup", group);
		// checks the number of tags
		JsonArray jsonTags = (JsonArray) chunk.get("tags");
		assertEquals(2, jsonTags.size());

		String[] tags = new String[jsonTags.size()];
		for (int i = 0; i < jsonTags.size(); i++) {
			tags[i] = jsonTags.get(i).getAsString();
		}
		// checks for tag names
		assertEquals("tag1", tags[0]);
		assertEquals("tag1", tags[0]);
		// checks the content
		assertEquals("value", content);
		// check dimensions
		assertEquals("testName", dimensionName);
		assertEquals("testUnit", dimensionUnit);
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is SingleDimDataChunk - but no chunk
	 * headers are inserted.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorSingleDataNoChunkHeaders() {
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk2, object, false);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);

		// get values from chunk
		String type = chunk.get("type").getAsString();
		String subtype = chunk.get("subtype").getAsString();
		String content = chunk.get("content").getAsString();
		String dimensionName = chunk.get("dimensionName").getAsString();
		String dimensionUnit = chunk.get("dimensionUnit").getAsString();

		// checks the values and names of elements of the element "value" (the
		// content)
		assertEquals("data", type);
		assertEquals("singleData", subtype);
		assertEquals("testName", dimensionName);
		assertEquals("testUnit", dimensionUnit);
		assertEquals("value", content);
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is OneDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorOneDimData() {
		jsonDataChunkBuilder.buildReportChunk(oneDimDataChunk, object, true);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);

		// dimension elements
		String dimensionName = chunk.get("dimensionName").getAsString();
		String dimensionUnit = chunk.get("dimensionUnit").getAsString();
		// the chunk header elements
		String[] names = { "rule", "group", "context" };
		String[] values = { "testRule", "testGroup", "error" };
		String[] tags = { "tag1", "tag2" };
		// the expected values of the content
		String[] oneDimValues = { "value1", "value2" };

		// checking for chunk header elements
		JsonArray jsonTags = (JsonArray) chunk.get("tags");
		for (int i = 0; i < names.length; i++) {
			assertEquals(values[i], chunk.get(names[i]).getAsString());
			if (i < 2)
				assertEquals(tags[i], jsonTags.get(i).getAsString());
		}

		// checking for dimension elements
		assertEquals("testName", dimensionName);
		assertEquals("testUnit", dimensionUnit);

		// checking for oneDim values
		JsonArray content = (JsonArray) chunk.get("content");
		for (int i = 0; i < oneDimValues.length; i++) {
			assertEquals(oneDimValues[i], content.get(i).getAsString());
		}
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is TwoDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorTwoDimData() {
		jsonDataChunkBuilder.buildReportChunk(twoDimDataChunk, object, true);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);

		// dimension elements
		String dimensionName1 = chunk.get("dimensionName1").getAsString();
		String dimensionUnit1 = chunk.get("dimensionUnit1").getAsString();
		String dimensionName2 = chunk.get("dimensionName2").getAsString();
		String dimensionUnit2 = chunk.get("dimensionUnit2").getAsString();
		// the chunk header elements
		String[] names = { "rule", "group", "context" };
		String[] values = { "testRule", "testGroup", "error" };
		String[] tags = { "tag1", "tag2" };
		// the tuple values
		String[] tupleValues = { "value1", "value2" };

		// checking for chunk header elements
		JsonArray jsonTags = (JsonArray) chunk.get("tags");
		for (int i = 0; i < names.length; i++) {
			assertEquals(values[i], chunk.get(names[i]).getAsString());
			if (i < 2)
				assertEquals(tags[i], jsonTags.get(i).getAsString());
		}
		// checking diension elements
		assertEquals("testName1", dimensionName1);
		assertEquals("testUnit1", dimensionUnit1);
		assertEquals("testName2", dimensionName2);
		assertEquals("testUnit2", dimensionUnit2);

		// checking for tuple values
		JsonArray content = (JsonArray) chunk.get("content").getAsJsonArray();
		JsonObject tuple = (JsonObject) content.get(0);
		String value1 = tuple.get("value1").getAsString();
		String value2 = tuple.get("value2").getAsString();

		assertEquals(tupleValues[0], value1);
		assertEquals(tupleValues[1], value2);
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements and the type is ThreeDimDataChunk.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorThreeDimData() {
		jsonDataChunkBuilder.buildReportChunk(threeDimDataChunk, object, true);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);

		// dimension elements
		// the expected values of the content
		String[] tripleValues = { "value1", "value2", "value3" };
		String[] tripleNames = { "value1", "value2", "value3" };
		// dimension elements
		String[] oneDimName = { "dimensionName1", "dimensionName2", "dimensionName3" };
		String[] oneDimAttName = { "testName1", "testName2", "testName3" };
		String[] oneDimUnit = { "dimensionUnit1", "dimensionUnit2", "dimensionUnit3" };
		String[] oneDimAttUnit = { "testUnit1", "testUnit2", "testUnit3" };
		// the chunk header elements
		String[] names = { "rule", "group", "context" };
		String[] values = { "testRule", "testGroup", "error" };
		String[] tags = { "tag1", "tag2" };

		// checking for chunk header elements
		JsonArray jsonTags = (JsonArray) chunk.get("tags");
		for (int i = 0; i < names.length; i++) {
			assertEquals(values[i], chunk.get(names[i]).getAsString());
			if (i < 2)
				assertEquals(tags[i], jsonTags.get(i).getAsString());
		}
		// checking values of the content and dimensions
		JsonArray content = (JsonArray) chunk.get("content").getAsJsonArray();
		JsonObject triple = (JsonObject) content.get(0);
		for (int i = 0; i < oneDimName.length; i++) {
			assertEquals(tripleValues[i], triple.get(tripleNames[i]).getAsString());
			assertEquals(oneDimAttName[i], chunk.get(oneDimName[i]).getAsString());
			assertEquals(oneDimAttUnit[i], chunk.get(oneDimUnit[i]).getAsString());
		}
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the null arguments
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingAllArguments() {
		jsonDataChunkBuilder.buildReportChunk(null, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the First null argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingFirsttArgument() {
		jsonDataChunkBuilder.buildReportChunk(null, object, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the second null
	 * argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingSecondArgument() {
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk1, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the wrong type of the
	 * first argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeFirstArgument() {
		jsonDataChunkBuilder.buildReportChunk(new TextExplanationChunk("testing.jpg"), object, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONDataChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the wrong type of the
	 * second argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeSecondArgument() {
		jsonDataChunkBuilder.buildReportChunk(singleDataChunk1, "test", false);
		fail("Exception should have been thrown, but it wasn't");
	}

}
