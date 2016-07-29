package org.goodoldai.jeff.report.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;

import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.report.ReportBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.goodoldai.jeff.report.ReportChunkBuilderFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 * A concrete ReportBuilder implementation. This class is used when the  
 * output report is supposed to be JSON.
 *
 * @author Dusan Ignjatovic
 */
public class JSONReportBuilder extends ReportBuilder {

	/**
     * Just calls the superclass constructor.
     *
     * @param factory chunk builder factory
     */
	public JSONReportBuilder(ReportChunkBuilderFactory factory) {
		super(factory);
	}

	
	/**
     * Creates an JSON report based on the provided explanation and sends it to 
     * an JSON file as output. If the file doesn't exist, it is created. If it 
     * exists, it is overwritten.
     * 
     * Basically, this method opens the java.io.FileWriter output stream and
     * it creates com.google.gson.JsonObject object (based on the provided file path)
     * which holds all of the explanation information before it is put into the stream,
     * and calls the "buildReport" method that has an com.google.gson.JsonObject an argument.
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param filepath a string representing an URL for the file
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null,
     * if filepath is an empty string, or if IOException is caught
     */
	@Override
	public void buildReport(Explanation explanation, String filepath) {
		if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }

        if (filepath == null || filepath.isEmpty()) {
            throw new ExplanationException("The entered filepath must not be null or empty string");
        }

        PrintWriter writer = null;
        try {
			writer = new PrintWriter(new File(filepath));
			buildReport(explanation, writer);
		} catch (IOException e) {
			throw new ExplanationException(
                    "The file could not be writen due to fallowing IO error: " + e.getMessage());
		} finally {
            if (writer != null) {
                writer.close();
            }
        }	
	}
	/**
     * Creates a report based on the provided explanation and writes it to the
     * provided object that is type of com.google.gson.JsonObject before it is written in
     * the file 
     *
     * @param explanation the explanation that needs to be transformed into a
     * report
     * @param stream output stream to which the report is to be written
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null
     */
	@Override
	public void buildReport(Explanation explanation, Object stream) {
		if (explanation == null) {
            throw new ExplanationException("The entered explanation must not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The entered stream must not be null");
        }

        if (!(stream instanceof PrintWriter)) {
            throw new ExplanationException("The argument 'stream' must be the type of java.io.PrintWriter");
        }
        
        JsonObject object = new JsonObject();
        insertHeader(explanation, object);
        object.add("explanation", new JsonArray());        
        //System.out.println((JsonArray) ( (JsonObject) stream ).get("explanation") );
        
        ArrayList<ExplanationChunk> chunks = explanation.getChunks();        
        for (int i = 0; i < chunks.size(); i++) {

            ExplanationChunk chunk = chunks.get(i);
            ReportChunkBuilder cbuilder = factory.getReportChunkBuilder(chunk);

            cbuilder.buildReportChunk(chunk, object, isInsertChunkHeaders());
        }
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PrintWriter writer = (PrintWriter) stream;

		writer.write(gson.toJson(object));
		writer.close();
		if (writer.checkError()) {
			throw new ExplanationException("The file could not be writen due to fallowing IO error: ");
		}
	}
	/**
     * This method inserts the header into the JSON report. The header consists 
     * of general data collected from the explanation (date and time created, 
     * owner, title, language and country). If any of this data is missing, it is not
     * inserted into the report. Since the report format is JSON, the provided 
     * output stream should be an instance of com.google.gson.JsonObject.
     * 
     * @param explanation explanation from which the header data is
     * to be collected
     * @param stream JsonObject that the header is supposed to be
     * inserted into
     * 
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null or if the entered output stream type is not com.google.gson.JsonObject
     */
	@Override
	protected void insertHeader(Explanation explanation, Object stream) {
		if (explanation == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }

        if (explanation == null) {
            throw new ExplanationException("The argument 'explanation' is mandatory, so it can not be null");
        }

        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }

        if (!(stream instanceof JsonObject)) {
            throw new ExplanationException("The argument 'stream' must be the type of com.google.gson.JsonObject");
        }

        JsonObject object = (JsonObject) stream;

        String date = DateFormat.getInstance().format(explanation.getCreated().getTime());
        String owner = explanation.getOwner();
        String language = explanation.getLanguage();
        String country = explanation.getCountry();
        String title = explanation.getTitle();

        if (date != null) {
        	object.addProperty("date", date);
        }

        if (owner != null) {
        	object.addProperty("owner", owner);
        }

        if (language != null) {
        	object.addProperty("language", language);
        }

        if (country != null) {
        	object.addProperty("country", country);
        }

        if (title != null) {
        	object.addProperty("title", title);
        }
		
	}

}
