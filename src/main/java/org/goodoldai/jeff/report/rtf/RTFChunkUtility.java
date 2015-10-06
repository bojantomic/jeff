package org.goodoldai.jeff.report.rtf;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/**
 * This class represents an utility class for the chunk-to-RTF
 * transformations. All of its methods are static and are supposed to be used
 * only by the classes from the "report.pdf" package.
 *
 * @author Anisja Kijevcanin
 */
class RTFChunkUtility {
	
	/**
     * In order to prevent initialization, the constructor is private
     */
	private RTFChunkUtility() {
    }
	
	
	/**
     * This method inserts general explanation chunk data (context, rule,
     * group, tags) into the report. If any of the data is missing it is not
     * being inserted.
     *
     * @param echunk explanation chunk that holds the information that needs to be inserted
     * @param doc report document to which chunks are inserted
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are null
     */
	static void insertChunkHeader(ExplanationChunk echunk, Document doc) {
        if (echunk == null) {
            throw new ExplanationException("The entered chunk must not be null");
        }

        if (doc == null) {
            throw new ExplanationException("The entered document must not be null");
        }

        String context = translateContext(echunk);

        try {
            if (context != null) {
                doc.add(new Paragraph("CONTEXT: " + context));
            }
            else
                doc.add(new Paragraph("CONTEXT: " + echunk.getContext()));

            if (echunk.getGroup() != null) {
                doc.add(new Paragraph("GROUP: " + echunk.getGroup()));
            }

            if (echunk.getRule() != null) {
                doc.add(new Paragraph("RULE: " + echunk.getRule()));
            }

            if (echunk.getTags() != null) {
                String[] tags = echunk.getTags();

                Phrase ph = new Phrase("TAGS:");

                for (int i = 0; i < tags.length; i++) {
                    ph.add(new Chunk(" '"+tags[i]+"'"));
                }
                doc.add(new Paragraph(ph));
            }
        } catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }
    }
	
	/**
     * This method translates the explanation chunk context code (integer value)
     * into a String value. For example, value 0 (zero) is translated to
     * "INFORMATIONAL" as declared in explanation chunk specification. If no
     * translation can be found, the method returns null.
     *
     * @param echunk explanation chunk for which context translation is needed
     *
     * @return context translation
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered chunk is null
     */
	static String translateContext(ExplanationChunk echunk) {
        if (echunk == null) {
            throw new ExplanationException("The entered chunk must not be null");
        }

        Class<ExplanationChunk> cl = ExplanationChunk.class;
        Field fields[] = cl.getDeclaredFields();

        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (Modifier.isPublic(field.getModifiers()) &&
                        (field.getInt(field.getName()) == echunk.getContext())) {
                    return field.getName();
                }
            }
            return null;
        } catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }
    }
}
