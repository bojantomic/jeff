package org.goodoldai.jeff.report.rtf;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

class RTFChunkUtility {
	
	private RTFChunkUtility() {
    }
	
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
