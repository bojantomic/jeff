package org.goodoldai.jeff.report.rtf;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;

import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;

public class RTFImageChunkBuilder {
	
	public RTFImageChunkBuilder() {
	}
	
	public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {
        if (echunk == null) {
            throw new ExplanationException("The entered chunk must not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The entered stream must not be null");
        }

        if (!(echunk instanceof ImageExplanationChunk)) {
            throw new ExplanationException("The entered chunk must be an ImageExplanationChunk instance");
        }

        if (!(stream instanceof com.lowagie.text.Document)) {
            throw new ExplanationException("The entered stream must be a com.lowagie.text.Document instance");
        }

        com.lowagie.text.Document doc = ((com.lowagie.text.Document) stream);

        if (insertHeaders)
            RTFChunkUtility.insertChunkHeader(echunk, doc);

        try {
            ImageData imdata = (ImageData) (echunk.getContent());

            Image img =
                    Image.getInstance(getClass().getResource(imdata.getURL()));

            img.scaleToFit(doc.getPageSize().getRight(doc.leftMargin() + doc.rightMargin()),
                    doc.getPageSize().getTop(doc.topMargin() + doc.bottomMargin()));

            doc.add(img);

            if ((imdata.getCaption() != null) &&
                    (!imdata.getCaption().equals(""))) {
                doc.add(new Paragraph("IMAGE: "+imdata.getCaption()));
            }
        } catch (NullPointerException e) {
            throw new ExplanationException("The image '"+((ImageData)(echunk.getContent())).getURL()+"' could not be found");
        }catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }

    }

}
