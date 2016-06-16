package org.goodoldai.jeff.report.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
public class JSONImageChunkBuilderTest extends AbstractJeffTest{

	ImageExplanationChunk imageEchunk1;
	ImageExplanationChunk imageEchunk2;
	JsonObject jsonObject;
	JSONImageChunkBuilder jsonImageChunkBuilder;

	/**
	 * Creates a explanation.ImageExplanationChunk and com.google.gson.JsonObject
	 *  instance that are used for testing.
	 */ 
	@Before
	public void setUp() throws Exception {
		super.setUp();

		String[] tags = {"tag1", "tag2"};

		jsonImageChunkBuilder = new JSONImageChunkBuilder();

		imageEchunk1 = new ImageExplanationChunk(new ImageData("picture.jpg"));        
		imageEchunk2 = new ImageExplanationChunk(-10, "testGroup", "testRule", tags, new ImageData("picture.jpg", "picture"));

		jsonObject = new JsonObject();
	}

	@After
	public void tearDown() throws Exception {
		jsonImageChunkBuilder = null;

		imageEchunk1 = null;
		imageEchunk2 = null;

		jsonObject = null;
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the null arguments.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingAllArguments() {
		jsonImageChunkBuilder.buildReportChunk(null, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the first null argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingFirstArgumant() {
		jsonImageChunkBuilder.buildReportChunk(null, imageEchunk1, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the second null argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingSecondArgumant() {
		jsonImageChunkBuilder.buildReportChunk(imageEchunk2, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the wrong
	 * type of the first argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeFirsArgumant() {
		jsonObject.add("chunks", new JsonArray());
		jsonImageChunkBuilder.buildReportChunk(new TextExplanationChunk("image.jpg"), jsonObject, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: unsuccessful building of a chunk because of the wrong
	 * type of the second argument.
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeSecondArgumant() {
		jsonObject.add("chunks", new JsonArray());
		jsonImageChunkBuilder.buildReportChunk(imageEchunk1, new TextExplanationChunk("test"), false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that only has content.
	 */
	@Test
	public void testBuildReportChunkFirstConstructor() {
		jsonObject.add("chunks", new JsonArray());
		jsonImageChunkBuilder.buildReportChunk(imageEchunk1, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(image) explanation chunk
		assertEquals("image", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("informational", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("picture.jpg", ( (JsonObject) jsonArray.get(0) ).get("url").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements.
	 */
	@Test
	public void testBuildReportChunkSecondConstructor() {
		jsonObject.add("chunks", new JsonArray());
		jsonImageChunkBuilder.buildReportChunk(imageEchunk2, jsonObject, true);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(image) explanation chunk
		assertEquals("image", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
		assertEquals("testRule", ( (JsonObject) jsonArray.get(0) ).get("rule").getAsString());
		assertEquals("testGroup", ( (JsonObject) jsonArray.get(0) ).get("group").getAsString());

		JsonArray array = ( (JsonObject) jsonArray.get(0) ).get("tags").getAsJsonArray();
		assertEquals("tag1", ( (JsonObject) array.get(0) ).get("value").getAsString());
		assertEquals("tag2", ( (JsonObject) array.get(1) ).get("value").getAsString());

		assertEquals("picture.jpg", ( (JsonObject) jsonArray.get(0) ).get("url").getAsString());
		assertEquals("picture", ( (JsonObject) jsonArray.get(0) ).get("caption").getAsString());
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements - but no chunk headers are inserted.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorNocHunkHeaders() {
		jsonObject.add("chunks", new JsonArray());
		jsonImageChunkBuilder.buildReportChunk(imageEchunk2, jsonObject, false);

		JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

		//checks the first(image) explanation chunk
		assertEquals("image", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
		assertEquals("picture.jpg", ( (JsonObject) jsonArray.get(0) ).get("url").getAsString());
		assertEquals("picture", ( (JsonObject) jsonArray.get(0) ).get("caption").getAsString());
	}
}
