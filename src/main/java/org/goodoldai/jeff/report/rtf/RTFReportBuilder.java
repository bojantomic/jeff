package org.goodoldai.jeff.report.rtf;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.rtf.RtfWriter2;

public class RTFReportBuilder extends ReportBuilder {
	
	 public RTFReportBuilder(ReportChunkBuilderFactory factory) {
	        super(factory);
	    }
	 
	 public void buildReport(Explanation explanation, String filepath) {
	        if (explanation == null) {
	            throw new ExplanationException("The entered explanation must not be null");
	        }

	        if (filepath == null || filepath.isEmpty()) {
	            throw new ExplanationException("The entered filepath must not be null or empty string");
	        }

	        FileOutputStream stream;
	        try {
	            stream = new FileOutputStream(filepath);
	        } catch (Exception e) {
	            throw new ExplanationException(e.getMessage());
	        }

	        buildReport(explanation, stream);

	        try {
	            stream.close();
	        } catch (Exception e) {
	            throw new ExplanationException(e.getMessage());
	        }

	    }
	 
	 @Override
	public void buildReport(Explanation explanation, Object stream) {
		 if (explanation == null) {
	            throw new ExplanationException("The entered explanation must not be null");
	        }

	        if (stream == null) {
	            throw new ExplanationException("The entered stream must not be null");
	        }

	        if (!(stream instanceof OutputStream)) {
	            throw new ExplanationException("The entered stream must be an OutputStream instance");
	        }

	        Document doc = new Document();
	        try {
	            RtfWriter2.getInstance(doc, (OutputStream)(stream));
	            doc.open();
	            insertHeader(explanation, doc);
	        } catch (Exception e) {
	            throw new ExplanationException(e.getMessage());
	        }

	        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

	        for (int i = 0; i < chunks.size(); i++) {
	            ExplanationChunk chunk = chunks.get(i);
	            ReportChunkBuilder cbuilder = factory.getReportChunkBuilder(chunk);
	            cbuilder.buildReportChunk(chunk, doc, isInsertChunkHeaders());
	        }

	        doc.close();
	}
	 
	 protected void insertHeader(Explanation explanation, Object stream) {
	        if (explanation == null) {
	            throw new ExplanationException("The argument 'explanation' is mandatory, so it can not be null");
	        }

	        if (stream == null) {
	            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
	        }

	        if (!(stream instanceof Document)) {
	            throw new ExplanationException("The entered stream must be a com.lowagie.text.Document instance");
	        }

	        Document doc = (Document) stream;

	        String owner = explanation.getOwner();
	        String title = explanation.getTitle();

	        doc.addCreator("JEFF (Java Explanation Facility Framework)");
	        doc.addAuthor(owner + " [OWNER]");
	        doc.addCreationDate();
	        
	        if (title != null){
	            try {
	                Phrase p = new Phrase(title, new Font(Font.HELVETICA, 16));
	                Paragraph pa = new Paragraph(p);
	                pa.setSpacingBefore(10);
	                pa.setSpacingAfter(30);
	                pa.setAlignment(Element.ALIGN_CENTER);
	                doc.add(pa);
	            } catch (DocumentException ex) {
	                throw new ExplanationException(ex.getMessage());
	            }
	        }
	        
	    }
}
