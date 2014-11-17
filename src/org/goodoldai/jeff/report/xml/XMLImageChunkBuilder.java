/*
 * Copyright 2009 Boris Horvat
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
package org.goodoldai.jeff.report.xml;


import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.dom4j.Document;
import org.dom4j.Element;
import org.goodoldai.jeff.report.ReportChunkBuilder;


/**
 * A concrete builder for transforming image explanation chunks into pieces 
 * of XML report
 *
 * @author Boris Horvat
 */
public class XMLImageChunkBuilder implements ReportChunkBuilder {

    /**
     * Initializes the builder
     */
    public XMLImageChunkBuilder() {
    }

    /**
     * This method transforms an image explanation chunk into an XML report piece
     * and writes this piece into the provided xml document which is, in this
     * case, an instance of org.dom4j.Document. The method first collects
     * all general chunk data (context, rule, group, tags) and inserts them 
     * into the report, and then retrieves the chunk content. Since the content 
     * is, in this case, an ImageData instance, and since images cannot be 
     * displayed in XML files, only the image data (caption and URL) gets 
     * inserted into the report. If the image caption is missing, it doesn't 
     * get inserted into the report.
     *
     * @param echunk image explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     *
     * @throws explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not an ImageExplanationChunk instance or 
     * if the entered output stream type is not org.dom4j.Document
     */
    public void buildReportChunk(ExplanationChunk echunk, Object stream) {

        if (echunk == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }

        if (echunk == null) {
            throw new ExplanationException("The argument 'echunk' is mandatory, so it can not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }

        if (!(echunk instanceof ImageExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be type of ImageExplanationChunk");
        }

        if (!(stream instanceof Document)) {
            throw new ExplanationException("The stream must be the type of org.dom4j.Document");
        }

        Document document = (Document) stream;
        Element element = document.getRootElement().addElement("imageExplanation");

        XMLChunkUtility.insertExplanationInfo(echunk, element);

        ImageExplanationChunk imageExplanationChunk = (ImageExplanationChunk) echunk;

        insertContent((ImageData) imageExplanationChunk.getContent(), element);
    }

    /**
     * This is a private method that is used to insert content into the document
     *
     * @param imageExplanationChunk image explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     */
    private void insertContent(ImageData imageData, Element element) {

        String url = imageData.getURL();
        String caption = imageData.getCaption();

        Element imageDataElement = element.addElement("content").addElement("imageUrl").addText(url);

        if (caption != null) {
            imageDataElement.addAttribute("caption", caption);
        }
    }
}

