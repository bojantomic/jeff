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

import com.lowagie.text.Chunk;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This class represents an utility class for the chunk-to-PDF
 * transformations. All of its methods are static and are supposed to be used
 * only by the classes from the "report.pdf" package.
 *
 * @author Bojan Tomic
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
     * @param echunk explanation chunk
     * @param doc report document
     *
     * @throws explanation.ExplanationException if any of the arguments are null
     */
    static void insertChunkHeader(ExplanationChunk echunk, com.lowagie.text.Document doc) {
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

        //Get declared fields from ExplanationChunk class
        Class cl = ExplanationChunk.class;
        Field fields[] = cl.getDeclaredFields();

        //Match the public field values (if present)
        //in order to retrieve the filed name i.e. to
        //translate the value to an appropriate string
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
