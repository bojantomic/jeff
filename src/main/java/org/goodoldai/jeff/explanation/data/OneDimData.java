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

import java.util.ArrayList;
import org.goodoldai.jeff.explanation.ExplanationException;

/**
 * This class represents an array of data (from the same dimension) like 
 * [17$, 20$, 31$]. This data consists of a value array and its
 * related dimension.
 *
 * @author Boris Horvat
 */
public class OneDimData implements Cloneable{

    /**
     * The data values.
     */
    private ArrayList<Object> values;
    private Dimension dimension;

    /**
     * This constructor sets all attributes
     * to entered values by calling the appropriate set methods.
     *
     * @param dimension data dimension
     * @param values data values
     */
    public OneDimData(Dimension dimension, ArrayList<Object> values) {
        setDimension(dimension);
        setValues(values);
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
     * Returns the data values.
     *
     * @return returns the array containing data values.
     */
    public ArrayList<Object> getValues() {
        return values;
    }

    /**
     * Sets the data value.
     *
     * @param val array of objects containing data values
     * (all objects must be instances of the same class)
     * 
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered array is empty, null, or
     * its elements are not from the same type.
     */
    public void setValues(ArrayList<Object> val) {
        if (val == null || val.isEmpty()) {
            throw new ExplanationException("You have to set an array of objects containing data values");
        }

        //Checking if the array elements are from the same type.
        for (int i = 1; i < val.size(); i++) {
            if (!val.get(0).getClass().getName().equals(val.get(i).getClass().getName())) {
                throw new ExplanationException("The entered array elements must be from the same type");
            }
        }
        this.values = val;
    }

   /**
    * Performs a "deep" cloning operation for this class.
    *
    * @return object clone
    */
    @Override
    public OneDimData clone(){
        ArrayList<Object> clonevalues = new ArrayList<Object>(values);
        
        return new OneDimData(dimension.clone(),clonevalues);
    }

}

