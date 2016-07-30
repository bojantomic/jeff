package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 * @author Dusan Ignjatovic
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JSONTextChunkBuilderTest extends AbstractJeffTest {

	TextExplanationChunk textEchunk1;
	TextExplanationChunk textEchunk2;
	JSONTextChunkBuilder jsonTextChunkBuilder;
	JsonObject object;

	/**
	 * Creates a explanation.TextExplanationChunk, and
	 * com.google.gson.JsonObject instances that are used for testing
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		String[] tags = { "tag1", "tag2" };

		jsonTextChunkBuilder = new JSONTextChunkBuilder();

		textEchunk1 = new TextExplanationChunk("testing");
		textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

		object = new JsonObject();
		object.add("explanation", new JsonArray());
	}

	@After
	public void tearDown() {

		jsonTextChunkBuilder = null;

		textEchunk1 = null;
		textEchunk2 = null;

		object = null;
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that only has content.
	 */
	@Test
	public void testBuildReportChunkFirstConstructor() {
		jsonTextChunkBuilder.buildReportChunk(textEchunk1, object, true);

		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String content = chunk.get("content").getAsString();
		String context = chunk.get("context").getAsString();

		// checking for values
		assertEquals("text", type);
		assertEquals("testing", content);
		String expResult = "INFORMATIONAL".toLowerCase();
		assertEquals(expResult, context);
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements.
	 */
	@Test
	public void testBuildReportChunkSecondConstructor() {
		jsonTextChunkBuilder.buildReportChunk(textEchunk2, object, true);

		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String context = chunk.get("context").getAsString();
		String rule = chunk.get("rule").getAsString();
		String group = chunk.get("group").getAsString();
		String content = chunk.get("content").getAsString();

		// checking for values
		assertEquals("text", type);
		assertEquals("error", context);
		assertEquals("testGroup", group);
		assertEquals("testRule", rule);

		// checks the type of chunk
		assertEquals("text", type);
		// checks the optional values
		assertEquals("error", context);
		assertEquals("testRule", rule);
		assertEquals("testGroup", group);
		assertEquals("test text", content);

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
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements - but no chunk headers are inserted.
	 */
	@Test
	public void testBuildReportChunkSecondConstructorNocHunkHeaders() {
		jsonTextChunkBuilder.buildReportChunk(textEchunk1, object, false);

		// extract explanation chunk
		JsonArray explanation = object.get("explanation").getAsJsonArray();
		JsonObject chunk = (JsonObject) explanation.get(0);
		// get values from chunk
		String type = chunk.get("type").getAsString();
		String content = chunk.get("content").getAsString();

		// checking for values
		assertEquals("text", type);
		assertEquals("testing", content);
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the null arguments
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingAllArguments() {
		jsonTextChunkBuilder.buildReportChunk(null, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the first null argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingFirstArgumant() {
		jsonTextChunkBuilder.buildReportChunk(null, object, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the second null
	 * argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkMissingSecondArgumant() {
		jsonTextChunkBuilder.buildReportChunk(textEchunk1, null, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the wrong type of the
	 * first argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeFirsArgumant() {
		jsonTextChunkBuilder.buildReportChunk(new ImageExplanationChunk(new ImageData("testing.jpg")), object, false);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of buildReportChunk method, of class JSONTextChunkBuilder. Test
	 * case: unsuccessful building of a chunk because of the wrong type of the
	 * second argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportChunkWrongTypeSecondArgumant() {
		jsonTextChunkBuilder.buildReportChunk(textEchunk1, "test", false);
		fail("Exception should have been thrown, but it wasn't");
	}

}
