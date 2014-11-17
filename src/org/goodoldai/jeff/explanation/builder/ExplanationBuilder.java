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

import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationException;
 

/**
 * This abstract class represents an explanation builder. It is supposed to 
 * create an explanation step by step by gradually adding explanation 
 * chunks to it.
 *
 * @author Bojan Tomic
 */
public abstract class ExplanationBuilder {

    /**
     * Explanation instance that is supposed to be completed by the builder
     */
    protected Explanation explanation = null;

    /**
     * Explanation chunk builder factory instance to be used when
     * acquiring explanation chunk builders
     */
    protected ExplanationChunkBuilderFactory factory = null;

    /**
     * Creates the builder and provides it with the Explanation and 
     * ExplamationChunkBuilderFactory instances.
     *
     * @param e explanation that
     * contains no explanation chunks and that is supposed to be completed by 
     * adding explanation chunks to it
     * @param factory a factory for
     * returning appropriate explanation chunk builder instances
     *
     * @throws explanation.ExplanationException
     * if any of the entered arguments is null
     */
    public ExplanationBuilder (Explanation e, ExplanationChunkBuilderFactory factory) {
        if (e == null)
            throw new ExplanationException("The entered explanation must not be null");
        if (factory == null)
            throw new ExplanationException("The entered factory must not be null");

        explanation = e;
        this.factory = factory;
    }

    /**
     * This abstract method creates an explanation chunk instance (by using the 
     * appropriate explanation chunk builder), populates it with entered data 
     * and inserts it into the explanation.
     *
     * @param type explanation chunk
     * type (text, data or image) which is defined as a static constant in the 
     * ExplanationChunkBuilderFactory class
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     */
    public abstract void addExplanationChunk (int type, int context, String group, String rule, String[] tags, Object content);

    /**
     * Returns the completed explanation that contains all explanation chunks.
     *
     * @return completed explanation
     */
    public Explanation getExplanation () {
        return explanation;
    }

}

