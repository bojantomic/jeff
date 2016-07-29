package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * @author Dusan Ignjatovic
 */
public class JSONUtilityTest extends AbstractJeffTest {

	TextExplanationChunk textEchunk1;
	TextExplanationChunk textEchunk2;

	JsonObject object;

	/**
	 * Creates a explanation.TextExplanationChunk,
	 * explanation.ImageExplanationChunk, explanation.DataExplanationChunk, and
	 * com.google.gson.JsonObject instances that are used for testing
	 */
	@Before
	public void setUp() throws Exception {
		super.setUp();

		String[] tags = { "tag1", "tag2" };

		textEchunk1 = new TextExplanationChunk("testing");
		textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

		object = new JsonObject();
	}

	@After
	public void tearDown() {

		textEchunk1 = null;
		textEchunk2 = null;

		object = null;
	}

    
    /**
     * Test of insertExplanationInfo method, of class JSONChunkUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
	public void testInsertExplenationInfoConstructor() {

		String[] names = { "rule", "group", "context" };
		String[] values = { "testRule", "testGroup", "error" };

		JSONChunkUtility.insertExplanationInfo(textEchunk2, object);

		// checks the values and names of properties
		for (int i = 0; i < names.length; i++) {
			assertEquals(values[i], object.get(names[i]).getAsString());
		}

		String context = object.get("context").getAsString();
		String group = object.get("group").getAsString();
		String rule = object.get("rule").getAsString();

		JsonArray jsonTags = (JsonArray) object.get("tags");
		
		// checks the number of elements in the element "tags"
		assertEquals(2, jsonTags.size());
		//create array of strings
		String[] tags = new String[jsonTags.size()];
		for (int i = 0; i < jsonTags.size(); i++) {
			tags[i] = jsonTags.get(i).getAsString();
		}

		assertEquals("error", context);
		assertEquals("testGroup", group);
		assertEquals("testRule", rule);
		assertEquals("tag1", tags[0]);
		assertEquals("tag2", tags[1]);		
	}
    
    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is not predefined
     */
    @Test
	public void testTranslateContextTypeNotRecognized() {
		int context = -555;
		String result = JSONChunkUtility.translateContext(context, textEchunk1);
		assertEquals(String.valueOf(context), result);
	}
    
    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "INFORMATIONAL"
     */
    @Test
    public void testTranslateContextKnownTypeInformational() {
        int context = 0;
        String expResult = "INFORMATIONAL".toLowerCase();
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "WARNING"
     */
    @Test
    public void testTranslateContextKnownTypeInformationalWarning() {
        int context = -5;
        String expResult = "WARNING".toLowerCase();
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "ERROR"
     */
    @Test
    public void testTranslateContextKnownTypeError() {
        int context = -10;
        String expResult = "ERROR".toLowerCase();
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        assertEquals(expResult, result);
//        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        
//        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypePositive() {
        int context = 1;
        String expResult = "POSITIVE".toLowerCase();
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryPositive() {
        int context = 2;
        String expResult = "VERY_POSITIVE".toLowerCase();
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeNegative() {
        int context = -1;
        String expResult = "NEGATIVE".toLowerCase();
        String result = JSONChunkUtility.translateContext(context, textEchunk1);
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class JSONChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryNegative() {
       int context = -2;
       String expResult = "VERY_NEGATIVE".toLowerCase();
       String result = JSONChunkUtility.translateContext(context, textEchunk1);
       assertEquals(expResult, result);
    }
    
	

}
