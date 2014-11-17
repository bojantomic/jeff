/*
 * Copyright 2009 Boris Horvat
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
package org.goodoldai.jeff.explanation.data;

import org.goodoldai.jeff.explanation.ExplanationException;

/**
 * This class represents single data like [17C] or [200$]. This data
 * consists of a value and its related dimension.
 *
 * @author Boris Horvat
 */
public class SingleData implements Cloneable{

    /**
     * The data value.
     */
    private Object value;
    private Dimension dimension;

    /**
     * This constructor sets all attributes
     * to entered values by calling the appropriate set methods.
     * @param dimension data dimension
     * @param value data value
     */
    public SingleData(Dimension dimension, Object value) {
        setDimension(dimension);
        setValue(value);
    }

    /**
     * Returns the dimension related to this data.
     *
     * @return Dimension object related to this data.
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * Sets the dimension related to this data.
     *
     * @param val Dimension object related to this data
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered object is null
     */
    public void setDimension(Dimension val) {
        if (val == null) {
            throw new ExplanationException("You have to set dimensions related to this data");
        }
        this.dimension = val;
    }

    /**
     * Returns the data value.
     *
     * @return returns the data value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the data value.
     * 
     * @param val object containing the data value
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered object is null
     */
    public void setValue(Object val) {
        if (val == null) {
            throw new ExplanationException("You have to set the values releted to this data");
        }
        this.value = val;
    }

    /**
    * Performs a "deep" cloning operation for this class.
    *
    * @return object clone
    */
    @Override
    public SingleData clone(){
       return new SingleData(dimension.clone(),value);
    }

}

