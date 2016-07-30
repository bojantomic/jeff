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
 * @author Dusan Ignjatovic
 */
public class JSONReportBuilderFactoryTest extends AbstractJeffTest {

	JSONReportChunkBuilderFactory instance;

	@Before
	public void setUp() throws Exception {
		instance = new JSONReportChunkBuilderFactory();
	}

	@After
	public void tearDown() throws Exception {
		instance = null;
	}

	/**
	 * Test of getReportChunkBuilder method, of class
	 * JSONReportChunkBuilderFactory. Test case: successful execution, passing
	 * of TEXT Explanation Chunk type
	 */
	@Test
	public void testGetJSONReportChunkBuilderText1() {

		ReportChunkBuilder result = instance.getReportChunkBuilder(new TextExplanationChunk("testText"));
		assertTrue(result instanceof JSONTextChunkBuilder);
	}

	/**
	 * Test of getReportChunkBuilder method, of class
	 * JSONReportChunkBuilderFactory. Test case: successful execution, passing
	 * of TEXT Explanation chunk type, assert returning of same instance every
	 * time
	 */
	@Test
	public void testGetJSONReportChunkBuilderText2() {

		ReportChunkBuilder result1 = instance.getReportChunkBuilder(new TextExplanationChunk("testText1"));
		ReportChunkBuilder result2 = instance.getReportChunkBuilder(new TextExplanationChunk("testText2"));

		assertTrue(result1 instanceof JSONTextChunkBuilder);
		assertTrue(result2 instanceof JSONTextChunkBuilder);

		// Assert that the same builder instance is returned every time
		assertEquals(result1.getClass(), result2.getClass());
	}

	/**
	 * Test of getReportChunkBuilder method, of class
	 * JSONReportChunkBuilderFactory. Test case: successful execution, passing
	 * of IMAGE Explanation chunk type
	 */
	@Test
	public void testGetJSONReportChunkBuilderImage1() {

		ReportChunkBuilder result = instance
				.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test.jpg")));

		assertTrue(result instanceof JSONImageChunkBuilder);
	}

	/**
	 * Test of getReportChunkBuilder method, of class
	 * JSONReportChunkBuilderFactory. Test case: successful execution, passing
	 * of IMAGE Explanation chunk type, assert returning of same instance every
	 * time
	 */
	@Test
	public void testGetJSONReportChunkBuilderImage2() {

		ReportChunkBuilder result1 = instance
				.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test1.jpg")));
		ReportChunkBuilder result2 = instance
				.getReportChunkBuilder(new ImageExplanationChunk(new ImageData("test2.jpg")));

		assertTrue(result1 instanceof JSONImageChunkBuilder);
		assertTrue(result2 instanceof JSONImageChunkBuilder);

		// Assert that the same builder instance is returned every time
		assertEquals(result1.getClass(), result2.getClass());
	}

	/**
	 * Test of getReportChunkBuilder method, of class
	 * JSONReportChunkBuilderFactory. Test case: successful execution, passing
	 * of DATA Explanation chunk type
	 */
	@Test
	public void testGetJSONReportChunkBuilderData1() {
		ReportChunkBuilder result = instance
				.getReportChunkBuilder(new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

		assertTrue(result instanceof JSONDataChunkBuilder);
	}

	/**
	 * Test of getReportChunkBuilder method, of class
	 * JSONReportChunkBuilderFactory. Test case: successful execution, passing
	 * of DATA Explanation chunk type, assert returning of same instance every
	 * time
	 */
	@Test
	public void testGetJSONReportChunkBuilderData2() {
		ReportChunkBuilder result1 = instance
				.getReportChunkBuilder(new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

		ReportChunkBuilder result2 = instance
				.getReportChunkBuilder(new DataExplanationChunk(new SingleData(new Dimension("test"), "test")));

		assertTrue(result1 instanceof JSONDataChunkBuilder);
		assertTrue(result2 instanceof JSONDataChunkBuilder);

		// Assert that the same builder instance is returned every time
		assertEquals(result1.getClass(), result2.getClass());
	}
	

}
