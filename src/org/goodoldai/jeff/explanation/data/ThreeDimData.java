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
 * This class represents a three-dimensional array of data like
 * [[2008,product1,7$], [2009,product2,20$], [2010,product1,31$]].This
 * data consists of an array of triples and their related dimensions.
 *
 * @author Boris Horvat
 */
public class ThreeDimData {

    private Dimension dimension1;
    private Dimension dimension2;
    private Dimension dimension3;
    private ArrayList<Triple> values;

    /**
     * This constructor sets all attributes
     * to entered values by calling the appropriate set methods.
     *
     * @param dimension1 first data dimension
     * @param dimension2 second data dimension
     * @param dimension3 third data dimension
     * @param values data values
     */
    public ThreeDimData(Dimension dimension1, Dimension dimension2,
            Dimension dimension3, ArrayList<Triple> values) {
        setDimension1(dimension1);
        setDimension2(dimension2);
        setDimension3(dimension3);
        setValues(values);
    }

    /**
     * Returns the first dimension related to this data.
     *
     * @return Dimension object representing the first dimension of this data
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
     * Returns the third dimension related to this data.
     *
     * @return Dimension object representing the third dimension of this data.
     */
    public Dimension getDimension3() {
        return dimension3;
    }

    /**
     * Sets the third dimension related to this data.
     * 
     * @param val Dimension object related to this data
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered object is null
     */
    public void setDimension3(Dimension val) {
        if (val == null) {
            throw new ExplanationException("You have to set dimensions related to this data");
        }
        this.dimension3 = val;
    }

    /**
     * Returns the data values.
     *
     * @return returns the array of triples containing data values.
     */
    public ArrayList<Triple> getValues() {
        return values;
    }

    /**
     * Sets the data values.
     *
     * @param val array of triples containing data values
     * 
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the
     * entered array is empty, null, or if Triple members are not
     * from the same type. This means that all Triples in the array must
     * have first members from the same type, second members from the
     * same type, and third members from the same type, i.e.:
     * { [17, true] , [19, false] , [21, false] } OK;
     * { [17, 155] , [19, false] , [21, false] } NOT OK - second member
     * not from the same type - integer vs. boolean;
     * { ["car", true] , [19, false], [21, false] } NOT OK - first member
     * not from the same type - String vs.integer;
     */
    public void setValues(ArrayList<Triple> val) {
        if (val == null || val.isEmpty()) {
            throw new ExplanationException("You have to set an array of objects containing data values");
        }

        //Checking if the first Triple members are from the same type,
        //if the second Triple members are from the same type, and also
        //if the third Triple members are from the same type.
        for (int i = 1; i < val.size(); i++) {
            if ((!val.get(0).getValue1().getClass().getName().equals(val.get(i).getValue1().getClass().getName())) ||
                    (!val.get(0).getValue2().getClass().getName().equals(val.get(i).getValue2().getClass().getName()))||
                    (!val.get(0).getValue3().getClass().getName().equals(val.get(i).getValue3().getClass().getName()))) {
                throw new ExplanationException("The entered Triple members  must be from the same type");
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
    public ThreeDimData clone(){
        ArrayList<Triple> clonevalues = new ArrayList<Triple>();

        for(int i=0;i<values.size();i++)
            clonevalues.add(values.get(i).clone());

        return new ThreeDimData(dimension1.clone(),dimension2.clone(),
                dimension3.clone(),clonevalues);
    }
}

