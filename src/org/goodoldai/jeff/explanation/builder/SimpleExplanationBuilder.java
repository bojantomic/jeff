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

/**
 * This class represents a simple implementation of the ExplanationBuilder 
 * abstract class without internationalization as a feature. If 
 * internationalization is needed, DefaultExplanationBuilder should be used.
 * 
 * @author Bojan Tomic
 */
public class SimpleExplanationBuilder extends ExplanationBuilder {

    /**
     * Just calls the superclass constructor. Also, it ignores all language and 
     * country data provided in the explanation.
     *
     * @param e explanation that contains no explanation chunks and that is
     * supposed to be completed by adding explanation chunks to it
     * @param factory a factory for returning appropriate explanation chunk
     * builder instances
     */
    public SimpleExplanationBuilder(Explanation e, ExplanationChunkBuilderFactory factory) {
        super(e, factory);
    }

    /**
     * Implements the abstract "addExplanationChunk" method.It creates an
     * explanation chunk instance (by using the appropriate explanation chunk 
     * builder), populates it with entered data and inserts it into the 
     * explanation.
     * 
     * @param type explanation chunk type (text, data or image) which is
     * defined as a static constant in the ExplanationChunkBuilderFactory class
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content
     */
    public void addExplanationChunk(int type, int context, String group, String rule, String[] tags, Object content) {
        ExplanationChunkBuilder builder =
                factory.getExplanationChunkBuilder(type);

        explanation.addChunk(
                builder.buildChunk(context, group, rule, tags, content));
    }
}

