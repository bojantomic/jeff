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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

/**
 * A concrete ReportBuilder implementation. This class is used when the 
 * output report is supposed to be PDF.
 * 
 * In order to provide a suitable PDF transformation, the "iText" framework
 * is used (www.lowagie.com).
 *
 * @author Bojan Tomic
 */
public class PDFReportBuilder extends ReportBuilder {

    /**
     * Just calls the superclass constructor.
     *
     * @param factory chunk builder factory
     */
    public PDFReportBuilder(ReportChunkBuilderFactory factory) {
        super(factory);
    }

    /**
     * Creates a report based on the provided explanation and sends it to a PDF 
     * file as output. If the file doesn't exist, it is created. If it exists, 
     * it is overwritten.
     *
     * Basically, this method opens a binary output stream (based on the
     * provided file path) and calls the "buildReport" method that has an
     * output stream as an argument.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param filepath a string representing an URL for the file
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null or if filepath is an empty string
     */
    public void buildReport(Explanation explanation, String filepath) {
        if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }

        if (filepath == null || filepath.isEmpty()) {
            throw new ExplanationException("The entered filepath must not be null or empty string");
        }

        //Open a new FileOutputStream for the provided filepath
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(filepath);
        } catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }

        //Call the other buildReport method that inserts the header and chunks
        buildReport(explanation, stream);

        //Close the FileOutputStream
        try {
            stream.close();
        } catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }

    }

    /**
     * This method overrides the superclass method which creates a report based 
     * on the provided explanation and writes it to the provided output 
     * stream. In this case, the expected output stream is any binary output
     * stream - java.io.OutputStream or any of its subclasses.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param stream output stream to which the report is to be written -
     * java.io.OutputStream or any of its subclasses
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null
     * or if the entered stream is not java.io.OutputStream or any of its
     * subclasses
     */
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

        //Open a new PDF document for the provided stream
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, (OutputStream) (stream));

            //Open PDF document
            doc.open();

            //Insert header into the report
            insertHeader(explanation, doc);
        } catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }

        //Get all explanation chunks
        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

        //Transform explanation chunks and insert them into the report
        for (int i = 0; i < chunks.size(); i++) {
            //Get one chunk from the list
            ExplanationChunk chunk = chunks.get(i);

            //Get the appropriate chunk builder for this chunk
            ReportChunkBuilder cbuilder = factory.getReportChunkBuilder(chunk);

            //Transform chunk and insert it into the report
            cbuilder.buildReportChunk(chunk, doc, isInsertChunkHeaders());
        }

        //Close the PDF document
        doc.close();

    }

    /**
     * This method inserts the header into the PDF report. The header consists 
     * of general data collected from the explanation (date and time created, 
     * owner, title, language and country). If any of this data is missing, it is not
     * inserted into the report. Since the report format is PDF, the provided 
     * stream should be an instance of com.lowagie.text.Document.
     * 
     * @param explanation explanation from which the header data is to be
     * collected
     * @param stream output stream that the header is supposed to be inserted
     * into
     * 
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null or if the entered output stream type is not com.lowagie.text.Document
     */
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

