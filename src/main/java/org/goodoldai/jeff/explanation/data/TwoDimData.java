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
 * This class represents a two-dimensional array of data like
 * [[2008,17$], [2009,20$], [2010,31$]]. This data consists of an
 * array of tuples and their related dimensions.
 *
 * @author Boris Horvat
 */
public class TwoDimData implements Cloneable {

    private Dimension dimension1;
    private Dimension dimension2;
    private ArrayList<Tuple> values;

    /**
     * This constructor sets all attributes
     * to entered values by calling the appropriate set methods.
     *
     * @param dimension1 first data dimension
     * @param dimension2 second data dimension
     * @param values data values
     */
    public TwoDimData(Dimension dimension1, Dimension dimension2, ArrayList<Tuple> values) {
        setDimension1(dimension1);
        setDimension2(dimension2);
        setValues(values);
    }

    /**
     * Returns the first dimension related to this data.
     *
     * @return Dimension object representing the first dimension of this data.
     */
    public Dimension getDimension1() {
        return dimension1;
    }

    /**
     * Sets the first dimension related to this data.
     * 
     * @param val Dimension object related to this data
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered object is null
     */
    public void setDimension1(Dimension val) {
        if (val == null) {
            throw new ExplanationException("You have to set dimensions related to this data");
        }
        this.dimension1 = val;
    }

    /**
     * Returns the second dimension related to this data.
     *
     * @return Dimension object representing the second dimension of this data.
     */
    public Dimension getDimension2() {
        return dimension2;
    }

    /**
     * Sets the second dimension related to this data.
     * 
     * @param val Dimension object related to this data
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered object is null
     */
    public void setDimension2(Dimension val) {
        if (val == null) {
            throw new ExplanationException("You have to set dimensions related to this data");
        }
        this.dimension2 = val;
    }

    /**
     * Returns the data values.
     * 
     * @return returns the array of tuples containing data values.
     */
    public ArrayList<Tuple> getValues() {
        return values;
    }

    /**
     * Sets the data values.
     *
     * @param val array of tuples containing data values
     * 
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the
     * entered array is empty, null, or if Tuple members are not from
     * the same type. This means that all Tuples in the array must have
     * first members from the same type, and also second members from
     * the same type, i.e.:
     * { [17, true] , [19, false] } OK;
     * { [17, 155] , [19, false] } NOT OK - second member
     * not from the same type - integer vs. boolean;
     * { ["car", true] , [19, false] } NOT OK - first member
     * not from the same type - String vs.integer;


     */
    public void setValues(ArrayList<Tuple> val) {
        if (val == null || val.isEmpty()) {
            throw new ExplanationException("You have to set an array of objects containing data values");
        }

        //Checking if the first Tuple members are from the same type, and also
        //if the second Tuple members are from the same type.
        for (int i = 1; i < val.size(); i++) {
            if ((!val.get(0).getValue1().getClass().getName().equals(val.get(i).getValue1().getClass().getName())) ||
                    (!val.get(0).getValue2().getClass().getName().equals(val.get(i).getValue2().getClass().getName()))) {
                throw new ExplanationException("The entered Tuple members  must be from the same type");
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
    public TwoDimData clone() {
        ArrayList<Tuple> clonevalues = new ArrayList<Tuple>();

        for (int i = 0; i < values.size(); i++) {
            clonevalues.add(values.get(i).clone());
        }

        return new TwoDimData(dimension1.clone(), dimension2.clone(), clonevalues);
    }
}

