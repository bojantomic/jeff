package org.goodoldai.jeff.report.rtf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportBuilderTest;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;
import org.goodoldai.jeff.report.pdf.RTFChunkBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
*
* @author Anisja Kijevcanin
*/
public class RTFReportBuilderTest extends ReportBuilderTest {

   private Explanation explanation;
   private String filePath;

   @Override
   public ReportBuilder getInstance(ReportChunkBuilderFactory factory) {
       return new RTFReportBuilder(factory);
   }

   @Override
   public ReportChunkBuilderFactory getFactory() {
       return new RTFChunkBuilderFactory();
   }

   @Before
   public void setUp() throws Exception {
       super.setUp();

       //Set up a sample explanation
       explanation = new Explanation("OWNER", "EN", "USA", "Explanation title");
       explanation.addChunk(new TextExplanationChunk("textTest"));
       explanation.addChunk(new ImageExplanationChunk(new ImageData("/WHALE.JPG")));

       //Set up a sample filepath
       filePath = "test.pdf";

       //Set up a RTFReportBuilder instance
       instance = getInstance(getFactory());
   }

   @After
   public void tearDown() {
       new File(filePath).delete();
   }
   /**
    * Test of buildReport method, of class RTFReportBuilder.
    * Test case: unsuccessfull execution - wrong type stream
    */
   @Test
   public void testBuildReport_Explanation_ObjectWrongTypeStream() {
       try {
           java.io.PrintWriter stream = new java.io.PrintWriter(System.out);
           instance.buildReport(explanation, stream);
           fail("An exception should have been thrown but wasn't");
       } catch (Exception e) {
           assertTrue(e instanceof ExplanationException);
           String expResult = "The entered stream must be an OutputStream instance";
           String result = e.getMessage();
           assertEquals(expResult, result);
       }
   }
}


