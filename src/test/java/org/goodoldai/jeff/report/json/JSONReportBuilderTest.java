package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author Marko Popovic
 *
 */
public class JSONReportBuilderTest extends ReportBuilderTest{

	Explanation explanation1;
    Explanation explanation2;
    FileWriter fw;
    FileReader fr;
    JsonObject jsonObject;
	
    @Override
    public ReportBuilder getInstance(ReportChunkBuilderFactory factory) {
    	return new JSONReportBuilder(factory);
    }
    
    @Override
    public ReportChunkBuilderFactory getFactory() {
    	return new JSONReportChunkBuilderFactory();
    }
	
    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, com.google.gson.JsonObject, java.io.FileWriter 
     * instances that are used for testing.
     */
    @Before
    public void setUp() throws Exception {
    	super.setUp();
    	
    	instance = getInstance(getFactory());
    	
    	explanation1 = new Explanation("tester");

        explanation2 = new Explanation("tester", "EN", "USA", "Explanation title");
        explanation2.addChunk(new TextExplanationChunk("textTest"));
        explanation2.addChunk(new ImageExplanationChunk(new ImageData("imgTest.jpg")));

        String[] tags = {"tag1", "tag2"};
        explanation2.addChunk(new DataExplanationChunk(-10, "testGroup", "testRule", tags,
                new SingleData(new Dimension("testName", "testUnit"), "value")));
        
        fw = new FileWriter(new File("test.json"));
        jsonObject = new JsonObject();
    }
    
    @After
    public void tearDown() throws IOException{
    	instance = null;
    	
    	fw.close();
    	jsonObject = null;
    	
    	new File("test.json").delete();
    }
    
    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the null arguments.
     */
    @Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
    public void testInsertHeaderAllNullArguments() {
    	((JSONReportBuilder)instance).insertHeader(null, null);
    	fail("Exception should have been thrown, but it wasn't");

    }
    
    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the first null argument.
     */
    @Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
    public void testInsertHeaderWithoutFirstArgument() {
    	((JSONReportBuilder)instance).insertHeader(null, fw);
    	fail("Exception should have been thrown, but it wasn't");

    }
    
    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the second null argument.
     */
    @Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
    public void testInsertHeaderWithoutSecondArgument() {
    	((JSONReportBuilder)instance).insertHeader(explanation1, null);
    	fail("Exception should have been thrown, but it wasn't");
    }
 
    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: unsuccessful building of a chunk because of the wrong
     * type of the second argument
     */
    @Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
    public void testInsertHeaderWrongTypeSecondArgument() {
    	((JSONReportBuilder)instance).insertHeader(explanation1, "test");
    	fail("Exception should have been thrown, but it wasn't");
    }
    
    /**
     * Test of buildReport method (this method takes the name of the file and
     * calls the other buildReport method of the same class), of class JSONReportBuilder.
     * Test case: successful building of explanation.
     */
    @Test
    public void testBuildReport() throws IOException{
    	instance.buildReport(explanation2, "test.json");
    	
    	//checks if the file is created
    	assertTrue(new File("test.json").exists());
    	
    	//create parser 
        JsonParser parser = new JsonParser();

        FileReader reader = new FileReader("test.json");
        Object obj = parser.parse(reader);
        jsonObject = (JsonObject) obj;
    	
        //checks the heading of the report
        assertEquals("" + DateFormat.getInstance().format(explanation2.getCreated().getTime()) + "", jsonObject.get("date").getAsString());
        assertEquals("tester", jsonObject.get("owner").getAsString());
        assertEquals("EN", jsonObject.get("language").getAsString());
        assertEquals("USA", jsonObject.get("country").getAsString());
        assertEquals("Explanation title", jsonObject.get("title").getAsString());
        
        JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();
        
        //checks the first explanation chunk
        assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
        assertEquals("textTest", ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());
        
        //checks the second explanation chunk
        assertEquals("image", ( (JsonObject) jsonArray.get(1) ).get("type").getAsString());
        assertEquals("imgTest.jpg", ( (JsonObject) jsonArray.get(1) ).get("url").getAsString());
        
      //checks the third explanation chunk
        assertEquals("data", ( (JsonObject) jsonArray.get(2) ).get("type").getAsString());
        assertEquals("single", ( (JsonObject) jsonArray.get(2) ).get("subtype").getAsString());
        assertEquals("testName", ( (JsonObject) jsonArray.get(2) ).get("dimensionName").getAsString());
        assertEquals("testUnit", ( (JsonObject) jsonArray.get(2) ).get("dimensionUnit").getAsString());
        assertEquals("value", ( (JsonObject) jsonArray.get(2) ).get("value").getAsString());
        
        reader.close();
    }
    
    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: successful building of a chunk, inserting header information,
     * using Explanation constructor that only has "owner" attribute.
     */
    @Test
    public void testInsertHeader() {
    	((JSONReportBuilder) instance).insertHeader(explanation1, jsonObject);
    	
    	//checks the values of JSONObject
    	assertEquals("tester", jsonObject.get("owner").getAsString());;
    	assertEquals("" + DateFormat.getInstance().format(explanation1.getCreated().getTime()) + "", jsonObject.get("date").getAsString());
    }
    
    /**
     * Test of insertHeader method, of class JSONReportBuilder.
     * Test case: successful building of a chunk, inserting header information
     * using Explanation constructor that only has all the attributes.
     */
    @Test
    public void testInsertHeaderAllInfo() {
    	((JSONReportBuilder) instance).insertHeader(explanation2, jsonObject);
    	
    	//checks the values of JSONObject
    	assertEquals("" + DateFormat.getInstance().format(explanation2.getCreated().getTime()) + "", jsonObject.get("date").getAsString());
        assertEquals("tester", jsonObject.get("owner").getAsString());
        assertEquals("EN", jsonObject.get("language").getAsString());
        assertEquals("USA", jsonObject.get("country").getAsString());
        assertEquals("Explanation title", jsonObject.get("title").getAsString());
    }
}
