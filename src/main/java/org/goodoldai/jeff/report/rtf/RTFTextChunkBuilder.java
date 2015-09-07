package org.goodoldai.jeff.report.rtf;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;

public class RTFTextChunkBuilder implements ReportChunkBuilder{

	public RTFTextChunkBuilder(){
		
	}
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
