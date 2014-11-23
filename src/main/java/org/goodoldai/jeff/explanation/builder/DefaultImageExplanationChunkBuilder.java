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
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;

/**
 * A concrete builder for creating image explanation chunks
 *
 * @author Bojan Tomic
 */
public class DefaultImageExplanationChunkBuilder implements ExplanationChunkBuilder {

    /**
     * This is an instance of the InternationalizationManager which
     * will be used for translating content.
     */
    private InternationalizationManager im;

    /**
     * Initializes the builder and sets the i18nManager.
     *
     * @param i18nManager An instance of the InternationalizationManager.
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the i18nManager instance
     * is null
     */
    public DefaultImageExplanationChunkBuilder(InternationalizationManager i18nManager) {
        if (i18nManager == null)
            throw new ExplanationException("The entered i18nManager instance must not be null");

        im = i18nManager;

    }

    /**
     * This method creates an ImageExplanationChunk instance based on the 
     * arguments provided and returns it as a return value. The chunk content 
     * is, in this case, an ImageData instance. This method uses the 
     * InternationalizationManager in order to translate the image caption if 
     * it is present, and if the translation is provided. If the translation
     * doesn't exist, the caption is left unchanged.
     *
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content - in this case an ImageData object
     *
     * @return created ImageExplanationChunk instance
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered content is null
     * or is not an ImageData instance
     */
    public ExplanationChunk buildChunk(int context, String group, String rule, String[] tags, Object content) {
        if ((content == null) || (!(content instanceof ImageData))) {
            throw new ExplanationException("You must enter image data as content");
        }

        ImageData chunkcontent = (ImageData) (content);

        //Translate image caption if it is present and if the
        //translation exists
        if (chunkcontent.getCaption() != null) {

            //Translate caption
            String translatedcaption =
                    im.translateImageCaption(chunkcontent.getCaption());

            //If translation exists, change the caption
            if (translatedcaption != null)
                chunkcontent.setCaption(translatedcaption);
        }

        return new ImageExplanationChunk(context,group,rule,tags,chunkcontent);
    }
}

