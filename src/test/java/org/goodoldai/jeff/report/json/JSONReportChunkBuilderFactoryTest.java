package org.goodoldai.jeff.report.json;

import static org.junit.Assert.*;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Marko Popovic
 *
 */
public class JSONReportChunkBuilderFactoryTest extends AbstractJeffTest{

	JSONReportChunkBuilderFactory instance;
	
	/**
	 * Create report.ReportChunkBuilderfactory instance that is used for testing.
	 */
	@Before
    public void setUp() throws Exception {
        super.setUp();
        instance = new JSONReportChunkBuilderFactory();
    }

    @After
    public void tearDown(){
        instance = null;
    }
    
    /**
     * Test of getReportChunkBuilder method, of class JSONReportChunkBuilderFactory.
     * Test case: unsuccessful execution - chunk type not recognized
     */
    @Test(expected = org.goodoldai.jeff.explanation.ExplanationException.class)
    public void testGetJSONReportChunkBuilderTypeNotRecognized() {
    	instance.getReportChunkBuilder(null);
        fail("Exception should have been thrown, but it wasn't");
    }
	
    /**
     * Test of getReportChunkBuilder method, of class JSONReportChunkBuilderFactory.
     * Test case: successful execution, passing of TEXT Explanation chunk type, assert returning
     * of same instance every time.
     */
    @Test
    public void testGetJSONReportChunkBuilderText() {
    	ReportChunkBuilder result1 = instance.getReportChunkBuilder(new TextExplanationChunk("testText"));
    	ReportChunkBuilder result2 = instance.getReportChunkBuilder(new TextExplanationChunk("testText"));
    	
    	assertTrue(result1 instanceof JSONTextChunkBuilder);
    	assertTrue(result2 instanceof JSONTextChunkBuilder);
    	
    	//Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class JSONReportChunkBuilderFactory.
     * Test case: successful execution, passing of IMAGE Explanation chunk type, assert returning
     * of same instance every time.
     */
    @Test
    public void testGetJSONReportChunkBuilderImage() {
    	ReportChunkBuilder result1 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("image1.jpg")));
    	ReportChunkBuilder result2 = instance.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("image2.jpg")));
    	
    	assertTrue(result1 instanceof JSONImageChunkBuilder);
    	assertTrue(result2 instanceof JSONImageChunkBuilder);
    	
    	//Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }
    
    /**
     * Test of getReportChunkBuilder method, of class JSONReportChunkBuilderFactory.
     * Test case: successful execution, passing of DATA Explanation chunk type, assert returning
     * of same instance every time.
     */
    @Test
    public void testGetJSONReportChunkBuilderData() {
    	ReportChunkBuilder result1 = instance.getReportChunkBuilder
    			( new DataExplanationChunk(new SingleData(new Dimension("test1"), "test")));
    	ReportChunkBuilder result2 = instance.getReportChunkBuilder
    			( new DataExplanationChunk(new SingleData(new Dimension("test2"), "test")));
    	
    	assertTrue(result1 instanceof JSONDataChunkBuilder);
    	assertTrue(result2 instanceof JSONDataChunkBuilder);
    	
    	//Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }
}
