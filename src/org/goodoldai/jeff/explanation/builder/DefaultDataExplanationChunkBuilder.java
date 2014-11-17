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

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.TwoDimData;

/**
 * A concrete builder for creating data explanation chunks
 *
 * @author Bojan Tomic
 */
public class DefaultDataExplanationChunkBuilder implements ExplanationChunkBuilder {

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
    public DefaultDataExplanationChunkBuilder(InternationalizationManager i18nManager) {
        if (i18nManager == null)
            throw new ExplanationException("The entered i18nManager instance must not be null");

        im = i18nManager;

    }

    /**
     * This method creates a DataExplanationChunk instance based on the 
     * arguments provided and returns it as a return value. The chunk content 
     * can be, in this case, an instance of: SingleData, OneDimData, TwoDimData 
     * or ThreeDimData. This method uses the InternationalizationManager in 
     * order to translate the dimension names and units (if present). If the 
     * translation doesn't exist, dimension names and units are left unchanged.
     * 
     * @param context chunk context
     * @param group rule group
     * @param rule rule identifier
     * @param tags tags associated with this explanation chunk
     * @param content chunk content - in this case an instance of:
     * SingleData, OneDimData, TwoDimData or ThreeDimData
     *
     * @return created DataExplanationChunk instance
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered content
     * is null or is not an instance of: SingleData, OneDimData,
     * TwoDimData or ThreeDimData
     */
    public ExplanationChunk buildChunk(int context, String group, String rule, String[] tags, Object content) {
        if (content == null || ((!(content instanceof SingleData) &&
                !(content instanceof OneDimData) &&
                !(content instanceof TwoDimData) &&
                !(content instanceof ThreeDimData)))) {
            throw new ExplanationException("The entered content must be a non-null" +
                    " instance of SingleData, OneDimData, TwoDimData or ThreeDimData");
        }

        //If content is an instance of SingleData, translate
        //dimension name and unit
        if (content instanceof SingleData) {
            SingleData chunkcontent = (SingleData) (content);
            translate(chunkcontent.getDimension());
        }

        //If content is an instance of OneDimData, translate
        //dimension name and unit
        if (content instanceof OneDimData) {
            OneDimData chunkcontent = (OneDimData) (content);
            translate(chunkcontent.getDimension());
        }

        //If content is an instance of TwoDimData, translate
        //both dimension names and units
        if (content instanceof TwoDimData) {
            TwoDimData chunkcontent = (TwoDimData) (content);
            translate(chunkcontent.getDimension1());
            translate(chunkcontent.getDimension2());
        }

        //If content is an instance of TwoDimData, translate
        //all three dimension names and units
        if (content instanceof ThreeDimData) {
            ThreeDimData chunkcontent = (ThreeDimData) (content);
            translate(chunkcontent.getDimension1());
            translate(chunkcontent.getDimension2());
            translate(chunkcontent.getDimension3());
        }

        return new DataExplanationChunk(context, group, rule, tags, content);
    }

    private void translate(Dimension dimension) {
        //Translate dimension name if the translation exists
        if (dimension.getName() != null) {
            //Translate name
            String translatedname =
                    im.translateDimensionName(dimension.getName());

            //If translation exists, change the dimension name
            if (translatedname != null) {
                dimension.setName(translatedname);
            }
        }

        //Translate dimension unit if it is present and if the
        //translation exists
        if (dimension.getUnit() != null) {
            //Translate unit
            String translatedunit =
                    im.translateUnit(dimension.getUnit());

            //If translation exists, change the unit
            if (translatedunit != null) {
                dimension.setUnit(translatedunit);
            }
        }
    }
}

