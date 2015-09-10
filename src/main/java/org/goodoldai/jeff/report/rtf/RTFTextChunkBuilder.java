package org.goodoldai.jeff.report.rtf;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;

/**
 * A concrete builder for transforming text explanation chunks into pieces 
 * of PDF report
 *
 * @author Anisja Kijevcanin
 * @version 0.1 This version has no formatting options regarding text (content)
 * but just inserts the text into the RTF document
 */
public class RTFTextChunkBuilder implements ReportChunkBuilder{
	
	/**
     * Initializes the builder
     */
	public RTFTextChunkBuilder() {	
	}
	
	/**
     * This method transforms a text explanation chunk into a RTF report piece 
     * and writes this piece into the provided output stream which is, in this 
     * case, an instance of com.lowagie.text.Document. The method first collects all
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content is, 
     * in this case, a string, it also gets inserted into the report.
     *
     * @param echunk text explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written as output (in this case com.lowagie.text.Document)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a TextExplanationChunk instance or if 
     * the entered output stream type is not com.lowagie.text.Document
     */
	public void buildReportChunk(ExplanationChunk echunk, Object stream,
			boolean insertHeaders) {
		 if (echunk == null)
	            throw new ExplanationException ("The entered chunk must not be null");

	        if (stream == null)
	            throw new ExplanationException ("The entered stream must not be null");

	        if (!(echunk instanceof TextExplanationChunk))
	            throw new ExplanationException ("The entered chunk must be a TextExplanationChunk instance");

	        if (!(stream instanceof Document))
	            throw new ExplanationException ("The entered stream must be a com.lowagie.text.Document instance");

	        Document doc = (Document) stream;
	        
	        if (insertHeaders)
	           RTFChunkUtility.insertChunkHeader(echunk, doc);

	        try {
	            doc.add(new Paragraph((String)(echunk.getContent())));
	        } catch (Exception e) {
	            throw new ExplanationException(e.getMessage());
	        }
		
	}

}
