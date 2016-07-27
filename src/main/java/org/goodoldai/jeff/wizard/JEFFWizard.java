/*
 * Copyright 2009 Boris Horvat & Nemanja Jovanovic
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
package org.goodoldai.jeff.wizard;

import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.builder.DefaultExplanationBuilder;
import org.goodoldai.jeff.explanation.builder.DefaultExplanationChunkBuilderFactory;
import org.goodoldai.jeff.explanation.builder.ExplanationBuilder;
import org.goodoldai.jeff.explanation.builder.ExplanationChunkBuilderFactory;
import org.goodoldai.jeff.explanation.builder.SimpleExplanationBuilder;
import org.goodoldai.jeff.explanation.builder.SimpleExplanationChunkBuilderFactory;
import org.goodoldai.jeff.report.pdf.PDFReportBuilder;
import org.goodoldai.jeff.report.pdf.RTFChunkBuilderFactory;
import org.goodoldai.jeff.report.txt.TXTReportBuilder;
import org.goodoldai.jeff.report.txt.TXTReportChunkBuilderFactory;
import org.goodoldai.jeff.report.xml.XMLReportBuilder;
import org.goodoldai.jeff.report.xml.XMLReportChunkBuilderFactory;

/**
 * This class represents a focus point for all explanation features. It is meant
 * to make usage of Java Explanation Facility Framework easier. It is by no means
 * the only way to use this framework.
 *
 * The idea is to enable making expert system explanations easier for users,
 * by conceailing the more elaborate processes, such as creating and manipulating
 * explantaion and report builders, using internationalization features, from the end user.
 * Also, it can be noted that, at this point, this class is implemented as a Facade.
 *
 * @author Boris Horvat & Nemanja Jovanovic
 */
public class JEFFWizard {

    /**
     * Specifies the owner of the explanation through an identifier string
     * (owner is the person for whom the explanation is intended for).
     */
    private String owner;
    /**
     * Language in which the explanation is written (see Java internationalization
     * feature and java.util.Locale class).
     */
    private String language;
    /**
     * Country for which the explanation is written (see Java internationalization
     * feature and java.util.Locale class).
     */
    private String country;
     /**
     * Explanation title.
     */
    private String title;
    /**
     * This parameter shows whether the internationaluzation should be used or not
     */
    private boolean internationalization;
    /**
     *  This parameter represents an (expert system) explanation.
     */
    private Explanation explanation = null;
    /**
     * This bulder is used to create the explanation
     */
    protected ExplanationBuilder builder = null;


    /**
     * This constructor sets the "internationalization" attribute to 'false'.
     * All other attributes are set to null.
     */
    public JEFFWizard() {
        this.owner = null;
        this.language = null;
        this.country = null;
        this.title = null;
        this.internationalization = false;
    }

    /**
     * This constructor sets the "internationalization" attribute to 'false'.
     * Also, the "owner" attribute is set to argument value. All other
     * attributes are set to null.
     *
     * @param owner explanation owner
     */
    public JEFFWizard(String owner) {
        this.owner = owner;
        this.language = null;
        this.country = null;
        this.title = null;
        this.internationalization = false;
    }

    /**
     * This constructor sets the "internationalization" attribute to 'false'.
     * Also, the "owner" and "title" attributes are set to argument values.
     * All other attributes are set to null.
     *
     * @param owner explanation owner
     * @param title explanation title
     */
    public JEFFWizard(String owner, String title) {
        this.owner = owner;
        this.language = null;
        this.country = null;
        this.title = title;
        this.internationalization = false;
    }

    /**
     * This constructor sets the "internationalization" attribute to argument value.
     * Also, the "owner" and "title" attributes are set to argument values. All other
     * attributes are set to null.
     *
     * @param owner explanation owner
     * @param title explanation title
     * @param internationalization whether internationalization should be used or not
     */
    public JEFFWizard(String owner, String title, boolean internationalization) {
        this.owner = owner;
        this.language = null;
        this.country = null;
        this.title = title;
        this.internationalization = internationalization;
    }

    /**
     * This constructor sets all attributes to argument values.
     *
     * @param owner explanation owner
     * @param language explanation language
     * @param country explanation country
     * @param title explanation title
     * @param internationalization whether internationalization should be used or not
     */
    public JEFFWizard(String owner, String language, String country, String title, boolean internationalization) {
        this.owner = owner;
        this.language = language;
        this.country = country;
        this.title = title;
        this.internationalization = internationalization;
    }

    /**
     * This method creates an instance of the Explantion class, which represents
     * an (expert system) explanation. Generally, explanations are formed gradually
     * during the ongoing (expert system) session.
     *
     * Each explanation has some attributes that identify it: owner, date and
     * time created, title, language and country. The last two are used for translating
     * the explanation into the appropriate language for the desired country
     * and are optional.(see org.goodoldai.jeff.explanation.Explanation for more detail)
     *
     * Once this method creates an instance of the explanation the parameters
     * owner, title, language and country can not be changed any more.
     *
     * This method is also used to initialize the builder which will be used
     * to add explantaion chunks to the explantaion. If no explantaion is created prior
     * to the call of this method, an exception will be thrown, since in that case
     * no chunks can be added.
     *
     * If the internationalization should be used then the DefaultExplanationBuilder
     * will be initialized, in the other case it will be SimpleExplanationBuilder
     * 
     */
    public void createExplanation() {
        explanation = new Explanation(owner, language, country, title);


        if (isInternationalization()) {
            builder = new DefaultExplanationBuilder(
                    explanation, new DefaultExplanationChunkBuilderFactory());
        } else {
            builder = new SimpleExplanationBuilder(
                    explanation, new SimpleExplanationChunkBuilderFactory());
        }
    }

    /**
     * Creates a text report based on the explanation which has been created
     * and sends it to a text file as output. If the file doesn't exist,
     * it is created. If it exists, it is overwritten.
     *
     * This method is used when the output report is supposed to be plain text
     * (not DOC nor RTF, just TXT).
     *
     * This method can not be called before the explanation has been created
     *
     * @param filePath the path and the name of the report
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false).
     *
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void generateTXTReport(String filePath, boolean insertHeaders) {
        if (explanation == null) {
            throw new ExplanationException("The the report can not be generated if explanation does not exist");
        }

        TXTReportBuilder txtBuilder = new TXTReportBuilder(new TXTReportChunkBuilderFactory());

        txtBuilder.setInsertChunkHeaders(insertHeaders);

        txtBuilder.buildReport(explanation, filePath);
    }

    /**
     * Creates a text report based on the explanation which has been created
     * and sends it to an output stream.
     *
     * This method is used when the output report is supposed to be plain text
     * (not DOC nor RTF, just TXT).
     *
     * This method can not be called before the explanation has been created
     *
     * @param stream a java.io.PrintWriter instance to which the report will
     * be sent
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void generateTXTReport(PrintWriter stream, boolean insertHeaders) {
        if (explanation == null) {
            throw new ExplanationException("The the report can not be generated if explanation does not exist");
        }

        TXTReportBuilder txtBuilder = new TXTReportBuilder(new TXTReportChunkBuilderFactory());

        txtBuilder.setInsertChunkHeaders(insertHeaders);

        txtBuilder.buildReport(explanation, stream);
    }

    /**
     * Creates an XML report based on the provided explanation and sends it to
     * an XML file as output.If the file doesn't exist,it is created. If it exists,
     * it is overwritten.
     *
     * This method is used when the output report is supposed to be XML.
     *
     * This method can not be called before the explanation has been created
     *
     * @param filePath the path and the name of the report
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void generateXMLReport(String filePath, boolean insertHeaders) {
        if (explanation == null) {
            throw new ExplanationException("The the report can not be generated if explanation does not exist");
        }

        XMLReportBuilder xmlBuilder = new XMLReportBuilder(new XMLReportChunkBuilderFactory());

        xmlBuilder.setInsertChunkHeaders(insertHeaders);

        xmlBuilder.buildReport(explanation, filePath);
    }

    /**
     * Creates an XML report based on the provided explanation and sends it to
     * an output stream.
     *
     * This method is used when the output report is supposed to be XML.
     *
     * This method can not be called before the explanation has been created
     *
     * @param stream a java.io.PrintWriter instance to which the report will
     * be sent
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void generateXMLReport(PrintWriter stream, boolean insertHeaders) {
        if (explanation == null) {
            throw new ExplanationException("The the report can not be generated if explanation does not exist");
        }

        XMLReportBuilder xmlBuilder = new XMLReportBuilder(new XMLReportChunkBuilderFactory());

        xmlBuilder.setInsertChunkHeaders(insertHeaders);

        xmlBuilder.buildReport(explanation, stream);
    }

    /**
     * Creates a report based on the provided explanation and sends it to a PDF
     * file as output.If the file doesn't exist,it is created. If it exists,
     * it is overwritten.
     *
     * This method is used when the output report is supposed to be PDF.
     *
     * This method can not be called before the explanation has been created
     *
     * @param filePath the path and the name of the report
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void generatePDFReport(String filePath, boolean insertHeaders) {
        if (explanation == null) {
            throw new ExplanationException("The the report can not be generated if explanation does not exist");
        }

        PDFReportBuilder pdfBuilder = new PDFReportBuilder(new RTFChunkBuilderFactory());

        pdfBuilder.setInsertChunkHeaders(insertHeaders);

        pdfBuilder.buildReport(explanation, filePath);
    }

    /**
     * Creates a report based on the provided explanation and sends it to an
     * output stream.
     *
     * This method is used when the output report is supposed to be PDF.
     *
     * This method can not be called before the explanation has been created
     *
     * @param stream a java.io.OutputStream instance to which the report will
     * be sent
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void generatePDFReport(OutputStream stream, boolean insertHeaders) {
        if (explanation == null) {
            throw new ExplanationException("The the report can not be generated if explanation does not exist");
        }

        PDFReportBuilder pdfBuilder = new PDFReportBuilder(new RTFChunkBuilderFactory());

        pdfBuilder.setInsertChunkHeaders(insertHeaders);

        pdfBuilder.buildReport(explanation, stream);
    }

    
    
    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addText(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

       builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.INFORMATIONAL,
                    group, rule, tags, content);
 
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addText(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.INFORMATIONAL,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL)),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addText(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.INFORMATIONAL,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags, rule group or rule name.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addText(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {
            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.INFORMATIONAL,
                    null, null, null, content);
        }
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses warning context (ExplanationChunk.WARNING)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextWarning(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.WARNING,
                    group, rule, tags, content);
       
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses warning context (ExplanationChunk.WARNING),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextWarning(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

       builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.WARNING,
                    group, rule, null, content);
       
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses warning context (ExplanationChunk.WARNING),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextWarning(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.WARNING,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses warning context (ExplanationChunk.WARNING),
     * and does not contain any tags, rule group or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextWarning(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {

            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.WARNING,
                    null, null, null, content);
        }
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses error context (ExplanationChunk.ERROR)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextError(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.ERROR,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses error context (ExplanationChunk.ERROR),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextError(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.ERROR,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses error context (ExplanationChunk.ERROR),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextError(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.ERROR,
                    null, rule, null, content);
       
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses error context (ExplanationChunk.ERROR),
     * and does not contain any tags, rule group or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextError(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {

            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.ERROR,
                    null, null, null, content);
        }
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses strategic context (ExplanationChunk.STRATEGIC)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextStrategic(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.STRATEGIC,
                    group, rule, tags, content);

    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses strategic context (ExplanationChunk.STRATEGIC)
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextStrategic(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.STRATEGIC,
                    group, rule, null, content);

    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses strategic context (ExplanationChunk.STRATEGIC)
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextStrategic(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.STRATEGIC,
                    null, rule, null, content);

    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses strategic context (ExplanationChunk.STRATEGIC)
     * and does not contain any tags, rule group or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextStrategic(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {

            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.STRATEGIC,
                    null, null, null, content);
        }
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextPositive(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.POSITIVE,
                    group, rule, tags, content);
       
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextPositive(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.POSITIVE,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextPositive(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.POSITIVE,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE),
     * and does not contain any tags, rule group or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextPositive(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {

            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.POSITIVE,
                    null, null, null, content);
        }
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextNegative(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.NEGATIVE,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextNegative(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.NEGATIVE,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextNegative(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.NEGATIVE,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE),
     * and does not contain any tags, rule group or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextNegative(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {

            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.NEGATIVE,
                    null, null, null, content);
        }
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very positive context (ExplanationChunk.VERY_POSITIVE)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryPositive(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_POSITIVE,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very positive context (ExplanationChunk.VERY_POSITIVE),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryPositive(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_POSITIVE,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very positive context (ExplanationChunk.VERY_POSITIVE),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryPositive(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_POSITIVE,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very positive context (ExplanationChunk.VERY_POSITIVE),
     * and does not contain any tags, rule group or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryPositive(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {

            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_POSITIVE,
                    null, null, null, content);
        }
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very negative context (ExplanationChunk.VERY_NEGATIVE)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryNegative(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_NEGATIVE,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very negative context (ExplanationChunk.VERY_NEGATIVE),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryNegative(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_NEGATIVE,
                    group, rule, null, content);
       
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very negative context (ExplanationChunk.VERY_NEGATIVE),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryNegative(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_NEGATIVE,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for text chunks. It creates a
     * text explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses very negative context (ExplanationChunk.VERY_NEGATIVE),
     * and does not contain any tags, rule group or rule identifiers.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addTextVeryNegative(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        if (isInternationalization()) {
            throw new ExplanationException("The internationalization feature can not be used without providing a rule");

        } else {

            builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.TEXT,
                    ExplanationChunk.VERY_NEGATIVE,
                    null, null, null, content);
        }
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addData(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.INFORMATIONAL,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addData(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.INFORMATIONAL,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addData(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.INFORMATIONAL,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags, rule groups or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addData(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.INFORMATIONAL,
                    null, null, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataPositive(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.POSITIVE,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataPositive(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.POSITIVE,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataPositive(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.POSITIVE,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses positive context (ExplanationChunk.POSITIVE),
     * and does not contain any tags, rule group or rule itself.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataPositive(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.POSITIVE,
                    null, null, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataNegative(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.NEGATIVE,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataNegative(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.NEGATIVE,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataNegative(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.NEGATIVE,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for data chunks. It creates a
     * data explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses negative context (ExplanationChunk.NEGATIVE),
     * and does not contain any tags, rule group or rule identifier.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addDataNegative(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.DATA,
                    ExplanationChunk.NEGATIVE,
                    null, null, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for image chunks. It creates an
     * image explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL)
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addImage(String group, String rule, String[] tags, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.IMAGE,
                    ExplanationChunk.INFORMATIONAL,
                    group, rule, tags, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for image chunks. It creates an
     * image explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags.
     *
     * This method can not be called before the explanation has been created
     *
     * @param group rule group
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addImage(String group, String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.IMAGE,
                    ExplanationChunk.INFORMATIONAL,
                    group, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for image chunks. It creates an
     * image explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags or rule group.
     *
     * This method can not be called before the explanation has been created
     *
     * @param rule rule identifier
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addImage(String rule, Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.IMAGE,
                    ExplanationChunk.INFORMATIONAL,
                    null, rule, null, content);
        
    }

    /**
     * Implements the "addExplanationChunk" method for image chunks. It creates an
     * image explanation chunk instance (by using the appropriate explanation chunk
     * builder), populates it with entered data and inserts it into the
     * explanation.
     *
     * This method uses informational context as default (ExplanationChunk.INFORMATIONAL),
     * and does not contain any tags, rule group or rule itself.
     *
     * This method can not be called before the explanation has been created
     *
     * @param content chunk content
     *
     * @throws ExplanationException if the explanation has not been created
     */
    public void addImage(Object content) {
        if (explanation == null) {
            throw new ExplanationException("The chunk can not be added if explanation does not exist");
        }

        builder.addExplanationChunk(
                    ExplanationChunkBuilderFactory.IMAGE,
                    ExplanationChunk.INFORMATIONAL,
                    null, null, null, content);
        
    }

    /**
     * Sets the country for which the explanation is supposed to be written
     * (see Java internationalization feature and java.util.Locale class)
     *
     * The parameter can be set for as long as the method for explanation building
     * has not been called (see method 'createExplanation' for more information)
     *
     * @param country The country for which the explanation is supposed to be
     * written.
     * 
     * @throws ExplanationException if the explanation building process has started
     */
    public void setCountry(String country) {
        if (explanation != null) {
            throw new ExplanationException("The argument can not be set" +
                    " beacuse the explanation building process has begun");
        }
        this.country = country;
    }

    /**
     * Returns the country for which the explanation is written (see Java
     * internationalization feature and java.util.Locale class)
     *
     * @return string representing the
     * explanation country or null if the country is not specified
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the language in which the explanation is supposed to be written
     * (see Java internationalization feature and java.util.Locale class)
     *
     * The parameter can be set for as long as the method for explanation building
     * has not been called (see method 'createExplanation' for more information)
     *
     * @param language The language in which the explanation is supposed to be
     * written.
     *
     * @throws ExplanationException if the explanation building process has started
     */
    public void setLanguage(String language) {
        if (explanation != null) {
            throw new ExplanationException("The argument can not be set" +
                    " beacuse the explanation building process has begun");
        }
        this.language = language;
    }

    /**
     * Returns the language in which the explanation is written (see Java
     * internationalization feature and java.util.Locale class)
     *
     * @return string representing the explanation language
     * or null if the language is not specified
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the explanation owner identifer. The owner is the person (or entity)
     * for whom the explanation is intended for.
     *
     * The parameter can be set for as long as the method for explanation building
     * has not been called (see method 'createExplanation' for more information)
     *
     * @param owner A String identifying the owner of the explanation.
     *
     * @throws ExplanationException if the explanation building process has started
     */
    public void setOwner(String owner) {
        if (explanation != null) {
            throw new ExplanationException("The argument can not be set" +
                    " beacuse the explanation building process has begun");
        }
        this.owner = owner;
    }

    /**
     * Returns the explanation owner identifer.
     *
     * @return string identifying the owner or
     * null if the owner is not specified.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the explanation title.
     *
     * The parameter can be set for as long as the method for explanation building
     * has not been called (see method 'createExplanation' for more information)
     *
     * @param title a String representing the explanation title
     *
     * @throws ExplanationException if the explanation building process has started
     */
    public void setTitle(String title) {
        if (explanation != null) {
            throw new ExplanationException("The argument can not be set" +
                    " beacuse the explanation building process has begun");
        }
        this.title = title;
    }

    /**
     * Returns the explanation title.
     *
     * @return string representing hte explanation title or
     * null if the title is not specified.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns a value that indicates if internationalization should be used or not.
     *
     * @return boolean value 'true' if the internationalization should be used, or
     * 'false' if internationalization should not be used
     */
    public boolean isInternationalization() {
        return internationalization;
    }

    /**
     * Sets whether internationalization sholud be used or not.
     *
     * The parameter can be set for as long as the method for explanation building
     * has not been called (see method 'createExplanation' for more information)
     *
     * @param internationalization a boolean indicator on whether internationalization
     * should be used (true) or not (false).
     *
     * @throws ExplanationException if the explanation building process has started
     */
    public void setInternationalization(boolean internationalization) {
        if (explanation != null) {
            throw new ExplanationException("The argument can not be set" +
                    " beacuse the explanation building process has begun");
        }
        this.internationalization = internationalization;
    }

    /**
     * Returns an object that represents an (expert system) explanation.
     *
     * @return Explanation instance representing an (expert system) explanation or
     * null if the building process has not started
     */
    public Explanation getExplanation() {
        return explanation;
    }
}
