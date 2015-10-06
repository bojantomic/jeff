/*
 * Copyright 2009 Bojan Tomic
 *
 * This file is part of JEFF (Java Explanation Facility Framework).
 *
 * JEFF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JEFF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with JEFF.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goodoldai.jeff.report.pdf;

import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 * A concrete builder for transforming image explanation chunks into pieces 
 * of PDF report
 *
 * @author Bojan Tomic
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
     * This method transforms an image explanation chunk into a PDF report
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

        //Insert general chunk data
        if (insertHeaders)
            RTFChunkUtility.insertChunkHeader(echunk, doc);

        try {
            //Insert content - in this case an image
            ImageData imdata = (ImageData) (echunk.getContent());

            //Get image data
            Image img =
                    Image.getInstance(getClass().getResource(imdata.getURL()));

            //Scale the image in order to fit the page
            img.scaleToFit(doc.getPageSize().getRight(doc.leftMargin() + doc.rightMargin()),
                    doc.getPageSize().getTop(doc.topMargin() + doc.bottomMargin()));

            //Add image
            doc.add(img);

            //If a caption is present, insert it below the image
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

