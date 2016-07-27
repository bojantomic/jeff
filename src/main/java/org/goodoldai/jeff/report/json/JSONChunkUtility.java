package org.goodoldai.jeff.report.json;

import java.lang.reflect.Field;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This class is used to do the utility jobs that are common for JSON report
 * generation purposes.
 *
 * @author Dusan Ignjatovic
 */
public class JSONChunkUtility {

	/**
     * In order to prevent initialization, the constructor is private
     */
    private JSONChunkUtility() {
    }

    /**
     * This method inserts the general explanation information (context, rule, group, tags)
     * into the JSON document
     *
     * @param echunk explanation chunk that holds the information that needs to be inserted
     * @param jsonChunk in which the content of the transformed chunk will be
     * written in as an JSON object (in this case com.google.gson.JsonObject)
     */
    static void insertExplanationInfo(ExplanationChunk echunk, JsonObject jsonChunk) {

        int cont = echunk.getContext();
        String context = translateContext(cont, echunk);

        String rule = echunk.getRule();
        String group = echunk.getGroup();
        String[] tags = echunk.getTags();

        jsonChunk.addProperty("context", context);
        
        if (rule != null) {
        	jsonChunk.addProperty("rule", rule);
        }
        
        if (group != null) {
        	jsonChunk.addProperty("group", group);
        }

        if (tags != null) {
        	JsonArray jsonTags = new JsonArray();
        	for (int i = 0; i < tags.length; i++) {
				String tag = tags[i];
				jsonTags.add(tag);
			}       	
        	jsonChunk.add("tags", jsonTags);        	
        }
    }

    /**
     *  This is a method that is used to translate the context from an integer
     * into a String. This method uses reflection in order to do this.
     *
     * @param context the int representation of explanation context
     * @param echunk explanation chunk that holds the string value of the context
     *
     * @return the string value of the context
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * In the case of any problems by covering raised IllegalArgumentException or
     * IllegalAccessException.
     */
    static String translateContext(int context, ExplanationChunk echunk) {

        Class cl = echunk.getClass();
        Field fields[] = cl.getFields();

        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                if (field.getInt(field.getName()) == context) {
                    return field.getName().toLowerCase();
                }
            } catch (IllegalArgumentException ex) {
                throw new ExplanationException(ex.getMessage());
            } catch (IllegalAccessException ex) {
                throw new ExplanationException(ex.getMessage());
            }
        }
        return String.valueOf(context);
    }
}
