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

import junit.framework.TestCase;

/**
 *
 * @author Nemanja Jovanovic
 */
public class DimensionTest extends TestCase {

    Dimension dimension;
        
    /**
     * Creates a Dimension instance
     */
    @Override
    public void setUp() {
        dimension = new Dimension("Miles");
    }

    /**
     * Test of one argument constructor, of class Dimension.
     * Test case: successfull initialization.
     */
    public void testOneArgumentConstructorSuccess() {
            String name = "Distance";
            dimension = new Dimension(name);

            assertEquals(name, dimension.getName());
    }


    /**
     * Test of two argument constructor, of class Dimension.
     * Test case: successfull initialization.
     */
    public void testTwoArgumentConstructorSuccess() {
            String name = "Distance";
            String unit = "km";
            dimension = new Dimension(name,unit);

            assertEquals(name, dimension.getName());
            assertEquals(unit, dimension.getUnit());
    }

    /**
     * Test of setName method, of class Dimension.
     * Test case:  successfull change of dimension name.
     */
    public void testSetNameSuccess() {

            String name = "Kilometers";
            dimension.setName(name);
            
            assertEquals(name, dimension.getName());
    }

    /**
     * Test of setName method, of class Dimension.
     * Test case: un successfull change of dimension name - null argument.
     */
    public void testSetNameNullValue() {
        try {
            dimension.setName(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a dimension name";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setName method, of class Dimension.
     * Test case: un successfull change of dimension name - empty String argument.
     */
    public void testSetNameEmptyValue() {
        try {
            dimension.setName("");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a dimension name";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
    
    /**
     * Test of setUnit method, of class Dimension.
     * Test case:  successfull change of dimension unit.
     */
    public void testSetUnitSuccess() {

            String unit = "euro";
            dimension.setUnit(unit);

            assertEquals(unit, dimension.getUnit());
    }

     /**
     * Test of clone method, of class Dimension.
     * Test case:  successfull cloning.
      */
     public void testCloneSuccess() {
            Dimension clone = dimension.clone();

            assertTrue(dimension != clone);
            assertEquals(dimension.getName(), clone.getName());
            assertEquals(dimension.getUnit(), clone.getUnit());
    }
}
