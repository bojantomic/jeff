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
package org.goodoldai.jeff.explanation.builder;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;

/**
 * A concrete builder for creating text explanation chunks
 *
 * @author Bojan Tomic
 */
public class TextExplanationChunkBuilder implements ExplanationChunkBuilder {

    /**
     * Initializes the builder
     */
    public TextExplanationChunkBuilder() {
    }

    /**
     * This method creates a TextExplanationChunk instance based on the 
     * arguments provided and returns it as a return value. In general, the 
     * chunk content should be text, but since this text can be lengthy (few 
     * paragraphs), it is retrieved from a properties file based on the rule 
     * and group names (see InternationalizationManager). Because of this, the 
     * expected content is an array of Object instances that will be inserted 
     * in the retireved text at the appropriate places. This method uses the 
     * InternationalizationManager in order to retrieve the text in the 
     * appropriate language.
     *
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content - in this case an array of Object
     * instances where each one represent an argument that will be inserted 
     * into the text.If there are no arguments to be inserted, this parameter
     * is expected to be null.
     *
     * @return created TextExplanationChunk instance
     *
     * @throws explanation.ExplanationException if the entered content
     * is not an array of Object or if the translation could not be found
     */
    public ExplanationChunk buildChunk(int context, String group, String rule, String[] tags, Object content) {
        if ((content != null) && (!(content instanceof Object[]))) {
            throw new ExplanationException("The entered content must be an array of Object instances");
        }

        //Get internationalization manager instance
        //The manager must be initialized for this to work
        InternationalizationManager im = InternationalizationManager.getManager();

        //Translate text and insert entered arguments
        Object[] arguments = (Object[]) (content);

        String translatedcontent = im.translateText(group, rule, arguments);

        if (translatedcontent == null)
            throw new ExplanationException("The translation for '"+
                    group+"' group '"+rule+"' rule could not be found");

        return new TextExplanationChunk(context, group, rule, tags, translatedcontent);
    }
}

