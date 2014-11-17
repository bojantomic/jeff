/*
 * Copyright 2009 Nemanja Jovanovic
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

/**
 * This class represents a tuple - two related data values.
 *
 * @author Nemanja Jovanovic
 */
public class Tuple implements Cloneable{

    /**
     * Data value representing the first tuple memeber.
     */
    private Object value1;
    /**
     * Data value representing the second tuple memeber.
     */
    private Object value2;

    /**
     * This constructor sets all attributes to entered
     * values by calling the appropriate set methods.
     *
     * @param value1 first data value
     * @param value2 second data value
     */
    public Tuple(Object value1, Object value2) {
        setValue1(value1);
        setValue2(value2);
    }

    /**
     * Returns the first tuple member value.
     *
     * @return the first member data value
     */
    public Object getValue1() {
        return value1;
    }

    /**
     * Sets the first member data value.
     * 
     * @param val object containing the value
     *
     * @throws explanation.ExplanationException if the entered object is null
     */
    public void setValue1(Object val) {
        if (val == null) {
            throw new org.goodoldai.jeff.explanation.ExplanationException("You must enter a data value");
        }
        this.value1 = val;
    }

    /**
     * Returns the second tuple member value.
     *
     * @return the second member data value
     */
    public Object getValue2() {
        return value2;
    }

    /**
     * Sets the second member data value.
     * 
     * @param val object containing the value
     *
     * @throws explanation.ExplanationException if the entered
     * object is null
     */
    public void setValue2(Object val) {
        if (val == null) {
            throw new org.goodoldai.jeff.explanation.ExplanationException("You must enter a data value");
        }
        this.value2 = val;
    }

   /**
    * Performs a "deep" cloning operation for this class.
    *
    * @return object clone
    */
    @Override
    public Tuple clone(){
        return new Tuple(value1,value2);
    }
}

