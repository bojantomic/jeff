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

import org.goodoldai.jeff.explanation.data.*;

/**
 * A concrete ExplanationChunk subclass refering to chunks that have data
 * as content - single values, value arrays, two or three dimensional data 
 * arrays.
 *
 * @author Bojan Tomic
 */
public class DataExplanationChunk extends ExplanationChunk implements Cloneable {

    /**
     * Just calls the one argument superclass constructor.
     *
     * @param content chunk content (see the "setContent" method)
     */
    public DataExplanationChunk(Object content) {
        super(content);
    }

    /**
     * Just calls the five argument superclass constructor.
     *
     * @param context chunk context
     * @param group rule group
     * @param rule rule
     * @param tags tags related to the chunk
     * @param content chunk content (see the "setContent" method)
     */
    public DataExplanationChunk(int context, String group, String rule, String[] tags, Object content) {
        super(context, group, rule, tags, content);
    }

    /**
     * Implements the "setContent" method from the ExplanationChunk abstract
     * class.
     *
     * Sets the chunk content. In this case, the content can be a SingleData, 
     * OneDimData, TwoDimData or ThreeDimData object. Validation is performed 
     * before setting.
     *
     * @param val chunk content.
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered object is null or is not from the following types:
     * SingleData, OneDimData, TwoDimData or ThreeDimData
     */
    public void setContent(Object val) {
        if (val == null ||
                ((!(val instanceof SingleData)) &&
                (!(val instanceof OneDimData)) &&
                (!(val instanceof TwoDimData)) &&
                (!(val instanceof ThreeDimData)))) {
            throw new ExplanationException(
                    "The content must be a non-null object from the following classes: SingleData, OneDimData, TwoDimData, ThreeDimData");
        }

        content = val;
    }

    /**
     * Performs a "deep" cloning operation for this class.
     * This means that all non-String attributes are also
     * being cloned, in this case tags and content.
     *
     * @return object clone
     */
    public DataExplanationChunk clone() {
        String[] tags = null;
        Object clonecontent = null;

        if (getTags() != null) {
            tags = getTags().clone();
        }

        if (content != null) {
            if (content instanceof SingleData) {
                clonecontent = ((SingleData) content).clone();
            }
            if (content instanceof OneDimData) {
                clonecontent = ((OneDimData) content).clone();
            }
            if (content instanceof TwoDimData) {
                clonecontent = ((TwoDimData) content).clone();
            }
            if (content instanceof ThreeDimData) {
                clonecontent = ((ThreeDimData) content).clone();
            }
        }

        DataExplanationChunk clone =
                new DataExplanationChunk(getContext(),
                getGroup(),
                getRule(),
                tags,
                clonecontent);

        return clone;
    }
}

