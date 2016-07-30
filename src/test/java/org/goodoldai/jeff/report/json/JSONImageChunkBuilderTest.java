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
 * @author Dusan Ignjatovic
 */
public class JSONImageChunkBuilderTest extends AbstractJeffTest {

	ImageExplanationChunk imageEchunk1;
	ImageExplanationChunk imageEchunk2;
	JSONImageChunkBuilder jsonImageChunkBuilder;
	JsonObject object;

	/**
	 * Creates a explanation.ImageExplanationChunk, and
	 * com.google.gson.JsonObject instances that are used for testing
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		String[] tags = { "tag1", "tag2" };

		jsonImageChunkBuilder = new JSONImageChunkBuilder();

		imageEchunk1 = new ImageExplanationChunk(new ImageData("picture.jpg"));
		imageEchunk2 = new ImageExplanationChunk(-10, "testGroup", "testRule", tags,
				new ImageData("picture.jpg", "picture"));

		object = new JsonObject();
		object.add("explanation", new JsonArray());
	}

	@After
	public void tearDown() {

		jsonImageChunkBuilder = null;

		imageEchunk1 = null;
		imageEchunk2 = null;

		object = null;

	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that only has content.
	 */
	@Test
	public void testBuildReportChunkFirstConstructor() {
		jsonImageChunkBuilder.buildReportChunk(imageEchunk1, object, true);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String content = chunk.get("content").getAsString();
		String context = chunk.get("context").getAsString();
		String expResult = "INFORMATIONAL".toLowerCase();
		// checks context
		assertEquals(expResult, context);
		// checks type
		assertEquals("image", type);
		// checks value of the content
		assertEquals("picture.jpg", content);
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements.
	 */
	@Test
	public void testBuildReportChunkSecondConstructor() {
		jsonImageChunkBuilder.buildReportChunk(imageEchunk2, object, true);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String context = chunk.get("context").getAsString();
		String content = chunk.get("content").getAsString();
		String caption = chunk.get("caption").getAsString();
		String rule = chunk.get("rule").getAsString();
		String group = chunk.get("group").getAsString();

		// checks type
		assertEquals("image", type);
		// checks the optional values
		assertEquals("error", context);
		assertEquals("testRule", rule);
		assertEquals("testGroup", group);

		// checks value of the content and caption
		assertEquals("picture.jpg", content);
		assertEquals("picture", caption);

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
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements - but with no chunk headers inserted.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorWithNoChunkHeaders() {
		jsonImageChunkBuilder.buildReportChunk(imageEchunk2, object, false);
		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String content = chunk.get("content").getAsString();
		String caption = chunk.get("caption").getAsString();
		// checks type
		assertEquals("image", type);
		// checks value of the content
		assertEquals("picture.jpg", content);
		// checks value of the caption
		assertEquals("picture", caption);
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the null arguments
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingAllArguments() {
		jsonImageChunkBuilder.buildReportChunk(null, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the first null argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingFirstArgumant() {
		jsonImageChunkBuilder.buildReportChunk(null, object, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the second null
	 * argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingSecondArgumant() {
		jsonImageChunkBuilder.buildReportChunk(imageEchunk1, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the wrong type of the
	 * first argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeFirsArgumant() {
		jsonImageChunkBuilder.buildReportChunk(new TextExplanationChunk("testing.jpg"), object, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONImageChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the wrong type of the
	 * second argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeSecondArgumant() {
		jsonImageChunkBuilder.buildReportChunk(imageEchunk1, "test", false);
		fail("Exception should have been thrown, but it wasn't");
	}

}
