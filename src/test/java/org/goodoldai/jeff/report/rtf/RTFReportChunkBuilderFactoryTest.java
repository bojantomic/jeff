package org.goodoldai.jeff.report.rtf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.pdf.RTFDataChunkBuilder;
import org.goodoldai.jeff.report.pdf.RTFImageChunkBuilder;
import org.goodoldai.jeff.report.pdf.RTFChunkBuilderFactory;
import org.goodoldai.jeff.report.pdf.RTFTextChunkBuilder;
import org.junit.Test;

/**
 * @author Anisja Kijevcanin
 */
public class RTFReportChunkBuilderFactoryTest extends AbstractJeffTest {

    private class UnknownExplanationChunk extends ExplanationChunk {

        private UnknownExplanationChunk(Object content) {
            super(content);
        }

        @Override
        public void setContent(Object val) {
        }

        @Override
        public ExplanationChunk clone() {
            return null;
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: unsuccessfull execution - chunk type not recognized
     */
    @Test
    public void testGetReportChunkBuilderChunkTypeNotRecognized() {
        //This is an instance of an unsupported chunk type
        UnknownExplanationChunk echunk = new UnknownExplanationChunk("");

        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();

        try {
            instance.getReportChunkBuilder(echunk);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Chunk type '" + echunk.getClass().getName() +
                    "' was not recognized";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: unsuccessfull execution - null chunk
     */
    @Test
    public void testGetReportChunkBuilderNullChunk() {
        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();

        try {
            instance.getReportChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a non-null chunk instance";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type
     */
    @Test
    public void testGetExplanationChunkBuilderText1() {
        TextExplanationChunk tec = new TextExplanationChunk("sample text");
        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();
        ReportChunkBuilder result = instance.getReportChunkBuilder(tec);

        assertTrue(result instanceof RTFTextChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetExplanationChunkBuilderText2() {
        TextExplanationChunk tec = new TextExplanationChunk("sample text");
        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();
        ReportChunkBuilder result1 = instance.getReportChunkBuilder(tec);
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(tec);

        assertTrue(result1 instanceof RTFTextChunkBuilder);
        assertTrue(result2 instanceof RTFTextChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type
     */
    @Test
    public void testGetExplanationChunkBuilderImage1() {
        ImageExplanationChunk iec =
                new ImageExplanationChunk(new ImageData("image.jpeg"));
        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();
        ReportChunkBuilder result = instance.getReportChunkBuilder(iec);

        assertTrue(result instanceof RTFImageChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetExplanationChunkBuilderImage2() {
        ImageExplanationChunk iec =
                new ImageExplanationChunk(new ImageData("image.jpeg"));
        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();

        ReportChunkBuilder result1 = instance.getReportChunkBuilder(iec);
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(iec);

        assertTrue(result1 instanceof RTFImageChunkBuilder);
        assertTrue(result2 instanceof RTFImageChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type
     */
    @Test
    public void testGetExplanationChunkBuilderData1() {
        DataExplanationChunk dec =
                new DataExplanationChunk(
                new SingleData(new Dimension("money"), new Double(123)));
        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();
        ReportChunkBuilder result = instance.getReportChunkBuilder(dec);

        assertTrue(result instanceof RTFDataChunkBuilder);
    }

    /**
     * Test of getReportChunkBuilder method, of class RTFReportChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetExplanationChunkBuilderData2() {
        DataExplanationChunk dec =
                new DataExplanationChunk(
                new SingleData(new Dimension("money"), new Double(123)));
        RTFChunkBuilderFactory instance = new RTFChunkBuilderFactory();
        ReportChunkBuilder result1 = instance.getReportChunkBuilder(dec);
        ReportChunkBuilder result2 = instance.getReportChunkBuilder(dec);

        assertTrue(result1 instanceof RTFDataChunkBuilder);
        assertTrue(result2 instanceof RTFDataChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }
}

