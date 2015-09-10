package org.goodoldai.jeff.report.rtf;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;

/**
 * A concrete builder for transforming image explanation chunks into pieces 
 * of PDF report
 *
 * @author Anisja Kijevcanin
 *
 * @version 1.0 No formatting options regarding image position or scaling
 * are available
 */
public class RTFImageChunkBuilder implements ReportChunkBuilder {
	
	/**
     * Initializes the builder
     */
	public RTFImageChunkBuilder() {
	}
	
	/**
     * This method transforms an image explanation chunk into a RTF report
     * piece and writes this piece into the provided output stream which is, in 
     * this case, an instance of com.lowagie.text.Document. The method first
     * collects all general chunk data (context, rule, group, tags) and inserts 
     * them into the report, and then retrieves the chunk content. Since the 
     * content is, in this case, an ImageData instance, the image it relates to 
     * (caption and URL) gets inserted into the report. If the image caption is 
     * missing, it doesn't get inserted into the report.
     *
     * @param echunk image explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written as output (in this case com.lowagie.text.Document)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not an ImageExplanationChunk instance or 
     * if the entered output stream type is not com.lowagie.text.Document
     */
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
