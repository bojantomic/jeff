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
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;

/**
 * A default implementation of the ExplanationChunkBuilderFactory inteface 
 * for providing references to the default explanation chunk builder 
 * instances which have internationalization as a feature.
 *
 * @author Bojan Tomic
 */
public class DefaultExplanationChunkBuilderFactory implements ExplanationChunkBuilderFactory {

    /**
     * This is an instance of the InternationalizationManager which
     * will be used for translating content.
     */
    private InternationalizationManager i18nManager;

    /**
     * A DefaultTextExplanationChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private DefaultTextExplanationChunkBuilder textExplanationChunkBuilder;
    /**
     * A DefaultImageExplanationChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private DefaultImageExplanationChunkBuilder imageExplanationChunkBuilder;
    /**
     * A DefaultDataExplanationChunkBuilder instance which is "lazy initialized"
     * and cached for future use.
     */
    private DefaultDataExplanationChunkBuilder dataExplanationChunkBuilder;

    /**
     * Initializes all attributes to null.
     */
    public DefaultExplanationChunkBuilderFactory() {
        textExplanationChunkBuilder = null;
        imageExplanationChunkBuilder = null;
        dataExplanationChunkBuilder = null;
        i18nManager = null;
    }

    /**
     * This method returns the appropriate explanation chunk builder reference 
     * for the entered explanation chunk type. If, for example, 
     * ExplanationChunkBuilderFactory.DATA was entered as an argument, the 
     * method would return a DefaultDataExplanationChunkBuilder instance.It 
     * is necessary to state that chunk builder instances are "lazy initialized"
     * and cached (as attributes) while the factory instance exists. This
     * means that, for example, DefaultImageExplanationChunkBuilder attribute is null
     * when the factory is created and initialized only when the first
     * DefaultImageExplanationChunkBuilder instance is needed and not before.
     * In all subsequent calls when this method is supposed to return a
     * DefaultImageExplanationChunkBuilder instance it returns the reference to
     * the already initialized DefaultImageExplanationChunkBuilder object.
     *
     * @param type explanation chunk type for which a builder is
     * needed (concrete values are represented as static constants TEXT, DATA 
     * and IMAGE of the ExplanationChunkBuilderFactory interface)
     * 
     * @return the appropriate explanation chunk builder instance for
     * the entered chunk type
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the type was not recognized
     * or if the i18nManager has not been initialized
     */
    public ExplanationChunkBuilder getExplanationChunkBuilder(int type) {
        if (i18nManager == null)
            throw new ExplanationException("The i18nManager has not been set");

        if (type == TEXT) {
            if (textExplanationChunkBuilder == null) {
                textExplanationChunkBuilder = 
                        new DefaultTextExplanationChunkBuilder(i18nManager);
            }

            return textExplanationChunkBuilder;
        }

        if (type == IMAGE) {
            if (imageExplanationChunkBuilder == null) {
                imageExplanationChunkBuilder = 
                        new DefaultImageExplanationChunkBuilder(i18nManager);
            }

            return imageExplanationChunkBuilder;
        }

        if (type == DATA) {
            if (dataExplanationChunkBuilder == null) {
                dataExplanationChunkBuilder = 
                        new DefaultDataExplanationChunkBuilder(i18nManager);
            }

            return dataExplanationChunkBuilder;
        }

        //Explanation chunk type was not recognized
        throw new ExplanationException("Chunk type '"+type+"' was not recognized");
    }
    
    /**
     * Sets the i18nManager which will be used for translating content.
     *
     * @param i18nManager An instance of the InternationalizationManager.
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered i18nManager
     * instance is null.
     *
     */
    public void setI18nManager(InternationalizationManager i18nManager){
        if (i18nManager == null)
            throw new ExplanationException("The i18nManager cannot be null");

        this.i18nManager = i18nManager;
        
    }
}

