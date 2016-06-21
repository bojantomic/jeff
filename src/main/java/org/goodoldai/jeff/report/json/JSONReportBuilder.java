package org.goodoldai.jeff.report.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
 * @author Marko Popovic
 */
public class JSONReportBuilder extends ReportBuilder{

	/**
	 * Just calls the superclass constructor.
	 * 
	 * @param factory chunk builder factory
	 */
	public JSONReportBuilder(ReportChunkBuilderFactory factory) {
		super(factory);
	}

	/**
	 * Creates a json report based on the provided explanation and sends it to
	 * a json file as output. If the file doesn't exist, it is created. If it 
	 * exists, it is overwritten.
	 * 
	 * Basically, this method opens a java.io.FileWriter output stream
	 * (based on the provided file path) and calls the "buildReport"
	 * method that has an output stream as an argument.
	 *
	 * @param explanation the explanation that needs to be transformed into a
	 * report
	 * @param filepath a string representing an URL for the file
	 *
	 * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null
	 * or if filepath is an empty string
	 */
	@Override
	public void buildReport(Explanation explanation, String filepath) {
		if(explanation == null){
			throw new ExplanationException("The entered explanation must not be null");
		}
		if(filepath == null || filepath.isEmpty()){
			throw new ExplanationException("The entered filepath must not be null or empty string");
		}

		try {
			FileWriter out = new FileWriter(new File(filepath));

			buildReport(explanation, out);

			out.close();
		} catch (IOException e) {
			throw new ExplanationException(e.getMessage());
		}
	}

	/**
	 * Creates a report based on the provided explanation and writes it to the
	 * provided object that is type of com.google.gson.JsonObject before it is written in
	 * the file. 
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

		if (!(stream instanceof FileWriter)) {
			throw new ExplanationException("The argument 'stream' must be the type of java.io.FileWriter");
		}

		JsonObject jsonObject = new JsonObject();

		insertHeader(explanation, jsonObject);

		jsonObject.add("chunks", new JsonArray());

		ArrayList<ExplanationChunk> chunks = explanation.getChunks();

		for (int i = 0; i < chunks.size(); i++) {

			ExplanationChunk chunk = chunks.get(i);
			ReportChunkBuilder cbuilder = factory.getReportChunkBuilder(chunk);

			cbuilder.buildReportChunk(chunk, jsonObject,  isInsertChunkHeaders());
		}

		FileWriter out = (FileWriter) stream;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String jsonString = gson.toJson(jsonObject);

		try {
			out.write(jsonString);
		} catch (IOException e) {
			throw new ExplanationException("The file could not be writen due to fallowing IO error: " + e.getMessage());
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
	 * @param stream output stream that the header is supposed to be
	 * inserted into
	 * 
	 * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
	 * null or if the entered output stream type is not com.google.gson.JsonObject
	 */
	@Override
	protected void insertHeader(Explanation explanation, Object stream) {
		if(explanation == null && stream == null){
			throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
		}

		if(explanation == null){
			throw new ExplanationException("The argument 'explanation' is mandatory, so it can not be null");
		}

		if(stream == null){
			throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
		}

		if(!(stream instanceof JsonObject)){
			throw new ExplanationException("The argument 'stream' must be the type of com.google.gson.JsonObject");
		}

		JsonObject jsonObject = (JsonObject) stream;

		String date = DateFormat.getInstance().format(explanation.getCreated().getTime());
		String owner = explanation.getOwner();
		String language = explanation.getLanguage();
		String country = explanation.getCountry();
		String title = explanation.getTitle();

		if(date != null){
			jsonObject.addProperty("date", date);
		}
		if(owner != null){
			jsonObject.addProperty("owner", owner);
		}
		if(language != null){
			jsonObject.addProperty("language", language);
		}
		if(country != null){
			jsonObject.addProperty("country", country);
		}
		if(title != null){
			jsonObject.addProperty("title", title);
		}
	}
}
