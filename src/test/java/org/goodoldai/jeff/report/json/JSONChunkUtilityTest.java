package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import org.goodoldai.jeff.AbstractJeffTest;
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
public class JSONChunkUtilityTest extends AbstractJeffTest{

	TextExplanationChunk textEchunk1;
	TextExplanationChunk textEchunk2;
	JsonObject jsonObject;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		String[] tags = {"tag1", "tag2"};

		textEchunk1 = new TextExplanationChunk("testing");
		textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

		jsonObject = new JsonObject();
	}

	@After
	public void tearDown() throws Exception {
		textEchunk1 = null;
		textEchunk2 = null;

		jsonObject = null;
	}

	/**
	 * Test of insertExplanationInfo method, of class JSONChunkUtility.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that only has content.
	 */
	@Test
	public void testInsertExplenationInfoFirstConstructor() {
		JSONChunkUtility.insertExplanationInfo(textEchunk1, jsonObject);

		//checks the value of attribute
		assertEquals("informational", jsonObject.get("context").getAsString());
	}

	/**
	 * Test of insertExplanationInfo method, of class JSONChunkUtility.
	 * Test case: successful insertion of data using the ExplanationChunk constructor
	 * that has all elements.
	 */
	@Test
	public void testInsertExplenationInfoSecondConstructor() {
		JSONChunkUtility.insertExplanationInfo(textEchunk2, jsonObject);

		//checks the value
		assertEquals("error", jsonObject.get("context").getAsString());
		assertEquals("testRule", jsonObject.get("rule").getAsString());
		assertEquals("testGroup", jsonObject.get("group").getAsString());

		JsonArray array = jsonObject.get("tags").getAsJsonArray();
		assertEquals("tag1", ( (JsonObject) array.get(0) ).get("value").getAsString());
		assertEquals("tag2", ( (JsonObject) array.get(1) ).get("value").getAsString());
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is not predefined.
	 */
	@Test
	public void testTranslateContextTypeNotRecognized() {
		int context = 100;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);

		assertEquals(String.valueOf(context), result);
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is predefined ant it is "INFORMATIONAL".
	 */
	@Test
	public void testTranslateContextKnownTypeInformational() {
		int context = 0;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		String inf = "informational";
		assertEquals(inf, result);
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is predefined ant it is "WARNING".
	 */
	@Test
	public void testTranslateContextKnownTypeWarning() {
		int context = -5;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		String war = "warning";
		assertEquals(war, result);
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is predefined ant it is "ERROR".
	 */
	@Test
	public void testTranslateContextKnownTypeError() {
		int context = -10;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		String err = "error";
		assertEquals(err, result);
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is predefined ant it is "POSITIVE".
	 */
	@Test
	public void testTranslateContextKnownTypePossitive() {
		int context = 1;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		String pos = "positive";
		assertEquals(pos, result);
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is predefined ant it is "VERY_POSITIVE".
	 */
	@Test
	public void testTranslateContextKnownTypeVeryPossitive() {
		int context = 2;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		String vPos = "very_positive";
		assertEquals(vPos, result);
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is predefined ant it is "NEGATIVE".
	 */
	@Test
	public void testTranslateContextKnownTypeNegative() {
		int context = -1;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		String neg = "negative";
		assertEquals(neg, result);
	}

	/**
	 * Test of translateContext method, of class JSONChunkUtility.
	 * Test case: successful transformation of context from the TextExplanationChunk
	 * when the context is predefined ant it is "VERY_NEGATIVE".
	 */
	@Test
	public void testTranslateContextKnownTypeVeryNegative() {
		int context = -2;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		String vNeg = "very_negative";
		assertEquals(vNeg, result);
	}
}
