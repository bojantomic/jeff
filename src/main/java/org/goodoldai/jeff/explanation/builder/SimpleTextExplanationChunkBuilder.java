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
import org.goodoldai.jeff.explanation.TextExplanationChunk;

/**
 * A concrete builder for creating text explanation chunks. Unlike the 
 * TextExplanationChunkBuilder, this builder does not perform 
 * internationaization.
 *
 * @author Bojan Tomic
 */
public class SimpleTextExplanationChunkBuilder implements ExplanationChunkBuilder {

    /**
     * Initializes the builder
     */
    public SimpleTextExplanationChunkBuilder () {
    }

    /**
     * This method creates a TextExplanationChunk instance based on the 
     * arguments provided and returns it as a return value. In this case, the 
     * chunk content is text (String instance).
     *
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content - in this case a String containing
     * the explanation
     *
     * @return created TextExplanationChunk instance
     */
    public ExplanationChunk buildChunk (int context, String group, String rule, String[] tags, Object content) {
        return new TextExplanationChunk(context, group, rule, tags, content);
    }

}

