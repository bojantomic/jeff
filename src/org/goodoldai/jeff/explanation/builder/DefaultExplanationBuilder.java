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
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import java.util.Locale;

/**
 * This class represents a default implementation of the ExplanationBuilder 
 * abstract class with internationalization as a feature. If no 
 * internationalization is needed, SimpleExplanationBuilder should be used.
 *
 * @author Bojan Tomic
 */
public class DefaultExplanationBuilder extends ExplanationBuilder {

    /**
     * Calls the superclass constructor. Also, it initializes the 
     * InternationalizationManager instance based on the language and country
     * provided in the explanation. If no language and country are specified,
     * the default language and country are used.
     *
     * @param e explanation that contains no explanation chunks and that is
     * supposed to be completed by adding explanation chunks to it
     * @param factory a factory for returning appropriate explanation chunk builder
     * instances
     */
    public DefaultExplanationBuilder(Explanation e, ExplanationChunkBuilderFactory factory) {
        super(e, factory);

        //This is needed in order to avoid a NullPointerException
        //raised by the java.util.Locale constructor when null arguments
        //are entered.
        String language = e.getLanguage();
        String country = e.getCountry();
        if (language == null) {
            language = "";
        }
        if (country == null) {
            country = "";
        }

        //Initialize the internationalization manager to be used by
        //explanation chunk builders
        InternationalizationManager i18nManager = new InternationalizationManager(
                new Locale(language, country));

        //Translate the explanation title if it is present
        if (e.getTitle() != null && !e.getTitle().isEmpty())
            e.setTitle(i18nManager.translateImageCaption(e.getTitle()));

        //Pass the initialized i18nManager to the factory
        DefaultExplanationChunkBuilderFactory decf =
                (DefaultExplanationChunkBuilderFactory)factory;

        decf.setI18nManager(i18nManager);
    }

    /**
     * Implements the abstract "addExplanationChunk" method.It creates an
     * explanation chunk instance (by using the appropriate explanation chunk 
     * builder), populates it with entered data and inserts it into the 
     * explanation.
     *
     * @param type explanation chunk type (text, data or image)
     * which is defined as a static constant in the 
     * ExplanationChunkBuilderFactory class
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

