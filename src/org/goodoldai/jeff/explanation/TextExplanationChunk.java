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
 * A concrete ExplanationChunk subclass refering to chunks that have a
 * textual content.
 *
 * @author Bojan Tomic
 */
public class TextExplanationChunk extends ExplanationChunk implements Cloneable {

    /**
     * Just calls the one argument superclass constructor.
     * 
     * @param content chunk content (see "setContent" method)
     */
    public TextExplanationChunk(Object content) {
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
    public TextExplanationChunk(int context, String group, String rule, String[] tags, Object content) {
        super(context, group, rule, tags, content);
    }

    /**
     * Implements the "setContent" method from the ExplanationChunk abstract
     * class.
     *
     * Sets the chunk content. In this case, the content is a simple String
     * value. Validation is performed before setting.
     *
     * @param val chunk content
     *
     * @throws explanation.ExplanationException if the entered object is not a
     * String, is null or has no characters (empty string).
     */
    public void setContent(Object val) {
        if (!(val instanceof String)) {
            throw new ExplanationException("The content must be a non-null, non-empty string value");
        }

        String s = (String) val;
        if (s == null || s.length() == 0) {
            throw new ExplanationException("The content cannot be null or an empty string");
        }

        content = s;
    }

    /**
     * Performs a "deep" cloning operation for this class.
     * This means that all non-String attributes are also
     * being cloned, in this case tags.
     *
     * @return object clone
     */
    @Override
    public TextExplanationChunk clone() {
        String[] tags = null;

        if (getTags() != null) {
            tags = getTags().clone();
        }

        return new TextExplanationChunk(getContext(), getGroup(), getRule(), tags, getContent());
    }
}

