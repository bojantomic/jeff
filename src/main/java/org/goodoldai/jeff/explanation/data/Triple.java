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

import org.goodoldai.jeff.explanation.ExplanationException;

/**
 * This class represents a triple - three related data values.
 *
 * @author Nemanja Jovanovic
 */
public class Triple implements Cloneable {

    /**
     * Data value representing the first triple memeber.
     */
    private Object value1;
    /**
     * Data value representing the second triple memeber.
     */
    private Object value2;
    /**
     * Data value representing the third triple memeber.
     */
    private Object value3;

    /**
     * This constructor sets all attributes to entered
     * values by calling the appropriate set methods.
     *
     * @param value1 first data value 
     * @param value2 second data value
     * @param value3 third data value
     */
    public Triple(Object value1, Object value2, Object value3) {
        setValue1(value1);
        setValue2(value2);
        setValue3(value3);
    }

    /**
     * Returns the first triple member value.
     *
     * @return the first member data value.
     */
    public Object getValue1() {
        return value1;
    }

    /**
     * Sets the first member data value.
     * 
     * @param val object containing the value
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered object is null
     */
    public void setValue1(Object val) {
        if (val == null) {
            throw new ExplanationException("You must enter a data value");
        }
        this.value1 = val;
    }

    /**
     * Returns the second triple member value.
     *
     * @return the second member data value.
     */
    public Object getValue2() {
        return value2;
    }

    /**
     * Sets the second member data value.
     * 
     * @param val object containing the value
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if the entered
     * object is null
     */
    public void setValue2(Object val) {
        if (val == null) {
            throw new ExplanationException("You must enter a data value");
        }
        this.value2 = val;
    }

    /**
     * Returns the third triple member value.
     *
     * @return the third member data value.
     */
    public Object getValue3() {
        return value3;
    }

    /**
     * Sets the third member data value.
     * 
     * @param val object containing the value
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered object is null
     */
    public void setValue3(Object val) {
        if (val == null) {
            throw new ExplanationException("You must enter a data value");
        }
        this.value3 = val;
    }

    /**
     * Performs a "deep" cloning operation for this class.
     *
     * @return object clone
     */
    @Override
    public Triple clone() {
        return new Triple(value1, value2, value3);
    }
}

