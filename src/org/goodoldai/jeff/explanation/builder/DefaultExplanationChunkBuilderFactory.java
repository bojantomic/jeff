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

import org.goodoldai.jeff.explanation.ExplanationException;

/**
 * A default implementation of the ExplanationChunkBuilderFactory inteface 
 * for providing references to the default explanation chunk builder 
 * instances which have internationalization as a feature.
 *
 * @author Bojan Tomic
 */
public class DefaultExplanationChunkBuilderFactory implements ExplanationChunkBuilderFactory {

    /**
     * A TextExplanationChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private TextExplanationChunkBuilder textExplanationChunkBuilder;
    /**
     * An ImageExplanationChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private ImageExplanationChunkBuilder imageExplanationChunkBuilder;
    /**
     * A DataExplanationChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private DataExplanationChunkBuilder dataExplanationChunkBuilder;

    /**
     * Initializes all attributes (chunk builder references) to null.
     */
    public DefaultExplanationChunkBuilderFactory() {
        textExplanationChunkBuilder = null;
        imageExplanationChunkBuilder = null;
        dataExplanationChunkBuilder = null;
    }

    /**
     * This method returns the appropriate explanation chunk builder reference 
     * for the entered explanation chunk type. If, for example, 
     * ExplanationChunkBuilderFactory.DATA was entered as an argument, the 
     * method would return a DefaultDataExplanationChunkBuilder instance.It 
     * is necessary to state that chunk builder instances are "lazy initialized"
     * and cached (as attributes) while the factory instance exists. This
     * means that, for example, ImageExplanationChunkBuilder attribute is null
     * when the factory is created and initialized only when the first
     * ImageExplanationChunkBuilder instance is needed and not before.
     * In all subsequent calls when this method is supposed to return a
     * ImageExplanationChunkBuilder instance it returns the reference to 
     * the already initialized ImageExplanationChunkBuilder object.
     *
     * @param type explanation chunk type for which a builder is
     * needed (concrete values are represented as static constants TEXT, DATA 
     * and IMAGE of the ExplanationChunkBuilderFactory interface)
     * 
     * @return the appropriate explanation chunk builder instance for
     * the entered chunk type
     *
     * @throws explanation.ExplanationException if the type was not recognized
     */
    public ExplanationChunkBuilder getExplanationChunkBuilder(int type) {
        if (type == TEXT) {
            if (textExplanationChunkBuilder == null) {
                textExplanationChunkBuilder = new TextExplanationChunkBuilder();
            }

            return textExplanationChunkBuilder;
        }

        if (type == IMAGE) {
            if (imageExplanationChunkBuilder == null) {
                imageExplanationChunkBuilder = new ImageExplanationChunkBuilder();
            }

            return imageExplanationChunkBuilder;
        }

        if (type == DATA) {
            if (dataExplanationChunkBuilder == null) {
                dataExplanationChunkBuilder = new DataExplanationChunkBuilder();
            }

            return dataExplanationChunkBuilder;
        }

        //Explanation chunk type was not recognized
        throw new ExplanationException("Chunk type '"+type+"' was not recognized");
    }
}

