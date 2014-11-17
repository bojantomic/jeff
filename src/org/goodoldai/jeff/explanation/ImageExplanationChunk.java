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
package org.goodoldai.jeff.explanation;

/**
 * A concrete ExplanationChunk subclass refering to chunks that have images
 * as content.
 *
 * @author Bojan Tomic
 */
public class ImageExplanationChunk extends ExplanationChunk implements Cloneable {

    /**
     * Just calls the one argument superclass constructor.
     * 
     * @param content chunk content (see "setContent" method)
     */
    public ImageExplanationChunk(Object content) {
        super(content);
    }

    /**
     * Just calls the five argument superclass constructor.
     * 
     * @param context chunk context
     * @param group rule group
     * @param rule rule
     * @param tags tags related to this chunk
     * @param content chunk content (see "setContent" method)
     */
    public ImageExplanationChunk(int context, String group, String rule, String[] tags, Object content) {
        super(context, group, rule, tags, content);
    }

    /**
     * Implements the "setContent" method from the ExplanationChunk abstract
     * class.
     *
     * Sets the chunk content. In this case, the content is an ImageData object
     * representing an URL and a caption for the desired image.
     *
     * @param val chunk content
     *
     * @throws explanation.ExplanationException
     * if the entered object is not an ImageData object or is null
     */
    public void setContent(Object val) {
        if (val == null || (!(val instanceof ImageData))) {
            throw new ExplanationException("The content must be a non-null ImageData object");
        }

        this.content = val;
    }

    /**
     * Performs a "deep" cloning operation for this class.
     * This means that all non-String attributes are also
     * being cloned, in this case tags and content.
     *
     * @return object clone
     */
    public ImageExplanationChunk clone() {
        String[] tags = null;
        ImageData contentclone = null;

        if (getTags() != null) {
            tags = getTags().clone();
        }

        if (content != null) {
            contentclone = ((ImageData) content).clone();
        }

        ImageExplanationChunk clone =
                new ImageExplanationChunk(getContext(),
                getGroup(),
                getRule(),
                tags,
                contentclone);

        return clone;
    }
}

