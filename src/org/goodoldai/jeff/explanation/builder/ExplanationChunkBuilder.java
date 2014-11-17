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

/**
 * When creating an explanation, each piece of the explanation is supposed 
 * to translate into an explanation chunk.The idea is to have a family 
 * of builders (which inherit this interface) for each explanation chunk 
 * type. For example ImageExplanationChunkBuilder, DataExplanationChunkBuilder, 
 * and TextExplanationChunkBuilder could be used to create 
 * ImageExplanationChunk, DataExplanationChunk and TextExplanationChunk 
 * instances.
 *
 * @author Bojan Tomic
 */
public interface ExplanationChunkBuilder {

    /**
     * This method should create an explanation chunk based on the arguments 
     * provided and return it as a return value. Since this is an abstract 
     * method, subclasses should provide a concrete implementation.
     * 
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     *
     * @return created explanation chunk instance
     */
    public ExplanationChunk buildChunk (int context, String group, String rule, String[] tags, Object content);

}

