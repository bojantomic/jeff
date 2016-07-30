package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DateFormat;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportBuilderTest;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author Dusan Ignjatovic
 */
public class JSONReportBuilderTest extends ReportBuilderTest {

	Explanation explanation1;
	Explanation explanation2;
	JsonObject object1;
	JsonObject object2;

	@Override
	public ReportBuilder getInstance(ReportChunkBuilderFactory factory) {
		return new JSONReportBuilder(factory);
	}

	@Override
	public ReportChunkBuilderFactory getFactory() {
		return new JSONReportChunkBuilderFactory();
	}

	/**
	 * Creates a explanation.TextExplanationChunk,
	 * explanation.ImageExplanationChunk, explanation.DataExplanationChunk, and
	 * com.google.gson.JsonObject instances that are used for testing
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		instance = new JSONReportBuilder(new JSONReportChunkBuilderFactory());

		explanation1 = new Explanation("tester");

		explanation2 = new Explanation("tester", "EN", "USA", "explanation title");
		explanation2.addChunk(new TextExplanationChunk("textTest"));
		explanation2.addChunk(new ImageExplanationChunk(new ImageData("imgTest.jpg")));

		String[] tags = { "tag1", "tag2" };
		explanation2.addChunk(new DataExplanationChunk(-10, "testGroup", "testRule", tags,
				new SingleData(new Dimension("testName", "testUnit"), "value")));
		object1 = new JsonObject();
		object2 = new JsonObject();
	}

	@After
	public void tearDown() throws Exception {
		new File("test.json").delete();
	}

	/**
	 * Test of buildReport method (this method takes the name of the file and
	 * calls the other buildReport method of the same class), of class
	 * JSONReportBuilder. Test case: successful building of explanation
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void testBuildReportMainMethod() throws FileNotFoundException {
		instance.setInsertChunkHeaders(true);
		instance.buildReport(explanation2, "test.json");
		// checks if the file is created
		assertTrue(new File("test.json").exists());
		// extract JsonObject from JSON file
		Gson gson = new GsonBuilder().create();
		FileReader reader = new FileReader("test.json");
		JsonObject jsObject = gson.fromJson(reader, JsonObject.class);
		// geting explanation chunks
		JsonArray explanation = jsObject.getAsJsonArray("explanation").getAsJsonArray();
		JsonObject jsonTextChunk = (JsonObject) explanation.get(0);
		JsonObject jsonImageChunk = (JsonObject) explanation.get(1);
		JsonObject jsonDataChunk = (JsonObject) explanation.get(2);

		// checks the values of explanation header
		assertEquals("tester", jsObject.get("owner").getAsString());
		assertEquals("EN", jsObject.get("language").getAsString());
		assertEquals("USA", jsObject.get("country").getAsString());
		assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()),
				jsObject.get("date").getAsString());
		assertEquals("explanation title", jsObject.get("title").getAsString());

		// checks the textual explanation chunk
		assertEquals("text", jsonTextChunk.get("type").getAsString());
		assertEquals("textTest", jsonTextChunk.get("content").getAsString());

		// checks the image explanation chunk
		assertEquals("image", jsonImageChunk.get("type").getAsString());
		assertEquals("imgTest.jpg", jsonImageChunk.get("content").getAsString());

		// checks the data explanation chunk
		assertEquals("data", jsonDataChunk.get("type").getAsString());
		assertEquals("singleData", jsonDataChunk.get("subtype").getAsString());
		assertEquals("testName", jsonDataChunk.get("dimensionName").getAsString());
		assertEquals("testUnit", jsonDataChunk.get("dimensionUnit").getAsString());
		assertEquals("value", jsonDataChunk.get("content").getAsString());
		// checks the data explanation chunk header
		assertEquals("error", jsonDataChunk.get("context").getAsString());
		assertEquals("testGroup", jsonDataChunk.get("group").getAsString());
		assertEquals("testRule", jsonDataChunk.get("rule").getAsString());
		// checks the number of tags
		JsonArray jsonTags = (JsonArray) jsonDataChunk.get("tags");
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
	 * Test of buildReport method, of class JSONReportBuilder. Test case:
	 * successful building of explanation
	 */
	@Test
	public void testBuildReport() throws FileNotFoundException {
		instance.setInsertChunkHeaders(true);
		instance.buildReport(explanation2, new PrintWriter(new File("test.json")));
		// checks if the file is created
		assertTrue(new File("test.json").exists());
		// extract JsonObject from JSON file
		Gson gson = new GsonBuilder().create();
		FileReader reader = new FileReader("test.json");
		JsonObject jsObject = gson.fromJson(reader, JsonObject.class);
		// getting explanation chunks
		JsonArray explanation = jsObject.getAsJsonArray("explanation").getAsJsonArray();
		JsonObject jsonTextChunk = (JsonObject) explanation.get(0);
		JsonObject jsonImageChunk = (JsonObject) explanation.get(1);
		JsonObject jsonDataChunk = (JsonObject) explanation.get(2);

		// checks the values of explanation header
		assertEquals("tester", jsObject.get("owner").getAsString());
		assertEquals("EN", jsObject.get("language").getAsString());
		assertEquals("USA", jsObject.get("country").getAsString());
		assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()),
				jsObject.get("date").getAsString());
		assertEquals("explanation title", jsObject.get("title").getAsString());

		// checks the textual explanation chunk
		assertEquals("text", jsonTextChunk.get("type").getAsString());
		assertEquals("textTest", jsonTextChunk.get("content").getAsString());

		// checks the image explanation chunk
		assertEquals("image", jsonImageChunk.get("type").getAsString());
		assertEquals("imgTest.jpg", jsonImageChunk.get("content").getAsString());

		// checks the data explanation chunk
		assertEquals("data", jsonDataChunk.get("type").getAsString());
		assertEquals("singleData", jsonDataChunk.get("subtype").getAsString());
		assertEquals("testName", jsonDataChunk.get("dimensionName").getAsString());
		assertEquals("testUnit", jsonDataChunk.get("dimensionUnit").getAsString());
		assertEquals("value", jsonDataChunk.get("content").getAsString());
		// checks the data explanation chunk header
		assertEquals("error", jsonDataChunk.get("context").getAsString());
		assertEquals("testGroup", jsonDataChunk.get("group").getAsString());
		assertEquals("testRule", jsonDataChunk.get("rule").getAsString());
		// checks the number of tags
		JsonArray jsonTags = (JsonArray) jsonDataChunk.get("tags");
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
	 * Test of buildReport method, of class JSONReportBuilder. Test case:
	 * successful building of explanation - but with no chunk headers
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void testBuildReportNoChunkHeaders() throws FileNotFoundException {
		instance.setInsertChunkHeaders(true);
		instance.buildReport(explanation2, new PrintWriter(new File("test.json")));
		// checks if the file is created
		assertTrue(new File("test.json").exists());
		// extract JsonObject from JSON file
		Gson gson = new GsonBuilder().create();
		FileReader reader = new FileReader("test.json");
		JsonObject jsObject = gson.fromJson(reader, JsonObject.class);
		// getting explanation chunks
		JsonArray explanation = jsObject.getAsJsonArray("explanation").getAsJsonArray();
		JsonObject jsonTextChunk = (JsonObject) explanation.get(0);
		JsonObject jsonImageChunk = (JsonObject) explanation.get(1);
		JsonObject jsonDataChunk = (JsonObject) explanation.get(2);

		// checks the values of explanation header
		assertEquals("tester", jsObject.get("owner").getAsString());
		assertEquals("EN", jsObject.get("language").getAsString());
		assertEquals("USA", jsObject.get("country").getAsString());
		assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()),
				jsObject.get("date").getAsString());
		assertEquals("explanation title", jsObject.get("title").getAsString());

		// checks the textual explanation chunk
		assertEquals("text", jsonTextChunk.get("type").getAsString());
		assertEquals("textTest", jsonTextChunk.get("content").getAsString());

		// checks the image explanation chunk
		assertEquals("image", jsonImageChunk.get("type").getAsString());
		assertEquals("imgTest.jpg", jsonImageChunk.get("content").getAsString());

		// checks the data explanation chunk
		assertEquals("data", jsonDataChunk.get("type").getAsString());
		assertEquals("singleData", jsonDataChunk.get("subtype").getAsString());
		assertEquals("testName", jsonDataChunk.get("dimensionName").getAsString());
		assertEquals("testUnit", jsonDataChunk.get("dimensionUnit").getAsString());
		assertEquals("value", jsonDataChunk.get("content").getAsString());
	}

	/**
	 * Test of buildReport method, of class JSONReportBuilder. Test case:
	 * unsuccessful building of a chunk because of the wrong type of the second
	 * argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testBuildReportWrongTypeSecondArgument() {
		instance.buildReport(explanation1, new Object());
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of insertHeader method, of class JSONReportBuilder. Test case:
	 * successful building of a chunk, inserting header information using
	 * Explanation constructor that only has all the attributes.
	 */
	@Test
	public void testInsertHeaderAllInfo() {
		((JSONReportBuilder) instance).insertHeader(explanation2, object1);

		// checks the values of attributes
		assertEquals("tester", object1.get("owner").getAsString());
		assertEquals("EN", object1.get("language").getAsString());
		assertEquals("USA", object1.get("country").getAsString());
		assertEquals(DateFormat.getInstance().format(explanation2.getCreated().getTime()),
				object1.get("date").getAsString());
		assertEquals("explanation title", object1.get("title").getAsString());

	}

	/**
	 * Test of insertHeader method, of class JSONReportBuilder. Test case:
	 * unsuccessful building of a chunk because of the null arguments
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testInsertHeaderMissingAllArgument() {
		((JSONReportBuilder) instance).insertHeader(null, null);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of insertHeader method, of class JSONReportBuilder. Test case:
	 * unsuccessful building of a chunk because of the First null argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testInsertHeaderMissingFirstArgument() {
		((JSONReportBuilder) instance).insertHeader(null, object1);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of insertHeader method, of class JSONReportBuilder. Test case:
	 * unsuccessful building of a chunk because of the second null argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testInsertHeaderMissingSecondArgument() {
		((JSONReportBuilder) instance).insertHeader(explanation1, null);
		fail("Exception should have been thrown, but it wasn't");
	}

	/**
	 * Test of insertHeader method, of class JSONReportBuilder. Test case:
	 * unsuccessful building of a chunk because of the wrong type of the second
	 * argument
	 */
	@Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
	public void testInsertHeaderWrongTypeSecondArgument() {
		((JSONReportBuilder) instance).insertHeader(explanation1, "test");
		fail("Exception should have been thrown, but it wasn't");
	}

}
