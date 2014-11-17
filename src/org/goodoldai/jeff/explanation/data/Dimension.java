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
 * This class represents a data dimension.
 *
 * A dimension must have a name and can have (optionally) a measuring unit,
 * e.g.: $, meter, yard etc.
 *
 * @author Nemanja Jovanovic
 */
public class Dimension implements Cloneable{

    /**
     * The name of the dimension, e.g.: profit, temperature etc.
     */
    private String name;

    /**
     * The measuring unit for this dimension: $ (dollar), m (meter), in (inch).
     * The value for this attribute is optional.
     */
    private String unit;

    /**
     * This constructor sets the "name" attribute to the entered value by
     * calling the "setName" method. The "unit" attribute is set to null.
     *
     * @param name dimension name
     */
    public Dimension (String name) {
        this.setName(name);
        this.setUnit(null);
    }

    /**
     * This constructor sets the "name" and "unit" attributes to the entered
     * values by calling the appropriate "set" methods.
     *
     * @param name dimension name
     * @param unit dimension unit
     */
    public Dimension (String name, String unit) {
        this.setName(name);
        this.setUnit(unit);
    }

    /**
     * Returns the dimension name.
     *
     * @return string containing dimension name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the dimension name.
     *
     * @param val string containing the dimension name
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered name is null or empty String
     */
    public void setName (String val) {
        if(val == null || val.equals(""))
            throw new ExplanationException("You must enter a dimension name");
        this.name = val;
    }

    /**
     * Returns the dimension measuring unit.
     * 
     * @return string containing dimension unit or null
     * if no unit is specified
     */
    public String getUnit () {
        return unit;
    }

    /**
     * Sets the dimension unit.
     * 
     * @param val String containing the dimension unit.
     * Since this value is optional, null and empty strings are allowed.
     */
    public void setUnit (String val) {
        this.unit = val;
    }

   /**
    * Performs a "deep" cloning operation for this class.
    *
    * @return object clone
    */
    @Override
    public Dimension clone(){
        return new Dimension(name,unit);
    }

}

