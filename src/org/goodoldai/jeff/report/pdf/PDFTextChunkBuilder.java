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

import com.lowagie.text.Paragraph;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 * A concrete builder for transforming text explanation chunks into pieces 
 * of PDF report
 *
 * @author Bojan Tomic
 * @version 0.1 This version has no formatting options regarding text (content)
 * but just inserts the text into the PDF document
 */
public class PDFTextChunkBuilder implements ReportChunkBuilder {

    /**
     * Initializes the builder
     */
    public PDFTextChunkBuilder () {
    }

    /**
     * This method transforms a text explanation chunk into a PDF report piece 
     * and writes this piece into the provided output stream which is, in this 
     * case, an instance of com.lowagie.text.Document. The method first collects all
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content is, 
     * in this case, a string, it also gets inserted into the report.
     *
     * @param echunk text explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written as output (in this case com.lowagie.text.Document)
     *
     * @throws explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a TextExplanationChunk instance or if 
     * the entered output stream type is not com.lowagie.text.Document
     */
    public void buildReportChunk (ExplanationChunk echunk, Object stream) {
        if (echunk == null)
            throw new ExplanationException ("The entered chunk must not be null");

        if (stream == null)
            throw new ExplanationException ("The entered stream must not be null");

        if (!(echunk instanceof TextExplanationChunk))
            throw new ExplanationException ("The entered chunk must be a TextExplanationChunk instance");

        if (!(stream instanceof com.lowagie.text.Document))
            throw new ExplanationException ("The entered stream must be a com.lowagie.text.Document instance");

        com.lowagie.text.Document doc = ((com.lowagie.text.Document)stream);
        
        //Insert general chunk data
        PDFChunkUtility.insertChunkHeader(echunk, doc);

        try {
            //Insert content
            doc.add(new Paragraph((String)(echunk.getContent())));
        } catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }


    }

}

