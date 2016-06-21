package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
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
public class JSONTextChunkBuilderTest extends AbstractJeffTest{

	TextExplanationChunk textEchunk1;
	TextExplanationChunk textEchunk2;
	JsonObject jsonObject;
	JSONTextChunkBuilder jsonTextChunkBuilder;

	/**
	 * Creates a explanation.TextExplanationChunk and com.google.gson.JsonObject
	 *  instance that are used for testing.
	 */ 
	@Before
	public void setUp() throws Exception {
		super.setUp();

		String[] tags = {"tag1", "tag2"};

		jsonTextChunkBuilder = new JSONTextChunkBuilder();

		textEchunk1 = new TextExplanationChunk("testing");
		textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

		jsonObject = new JsonObject();
	}

	@After
	public void tearDown() throws Exception {
		jsonTextChunkBuilder = null;

		textEchunk1 = null;
		textEchunk2 = null;

		jsonObject = null;
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the null arguments.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingAllArguments() {
		jsonTextChunkBuilder.buildReportChunk(null, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the first null argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingFirstArgumant() {
		jsonTextChunkBuilder.buildReportChunk(null, textEchunk1, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the second null argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingSecondArgumant() {
		jsonTextChunkBuilder.buildReportChunk(textEchunk1, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the wrong
	 * type of the first argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeFirsArgumant() {
		jsonObject.add("chunks", new JsonArray());
		jsonTextChunkBuilder.buildReportChunk(new ImageExplanationChunk(new ImageData("image1.jpg")), jsonObject, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the wrong
	 * type of the second argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeSecondArgumant() {
		jsonObject.add("chunks", new JsonArray());
		jsonTextChunkBuilder.buildReportChunk(textEchunk2, new TextExplanationChunk("test"), false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that only has content.
	 */
	@Test
	public void testBuildReportChunkFirstConstructor() {
		jsonObject.add("chunks", new JsonArray());
		jsonTextChunkBuilder.buildReportChunk(textEchunk1, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(text) explanation chunk
		assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("informational", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testing", ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements.
	 */
	@Test
	public void testBuildReportChunkSecondConstructor() {
		jsonObject.add("chunks", new JsonArray());
		jsonTextChunkBuilder.buildReportChunk(textEchunk2, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(text) explanation chunk
		assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testRule", ( (JsonObject) jsonArray.get(0) ).get("rule").getAsString());
		assertEquals("testGroup", ( (JsonObject) jsonArray.get(0) ).get("group").getAsString());

		JsonArray array = ( (JsonObject) jsonArray.get(0) ).get("tags").getAsJsonArray();
		assertEquals("tag1", ( (JsonObject) array.get(0) ).get("value").getAsString());
		assertEquals("tag2", ( (JsonObject) array.get(1) ).get("value").getAsString());

		assertEquals("test text", ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements - but no chunk headers are inserted.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorNocHunkHeaders() {
		jsonObject.add("chunks", new JsonArray());
		jsonTextChunkBuilder.buildReportChunk(textEchunk2, jsonObject, false);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(text) explanation chunk
		assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("test text", ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());
	}
}
