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

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Boris Horvat
 */
public class SingleDataTest {

    SingleData single;

    /**
     * Creates a SingleData instance.
     */
    @Before
    public void setUp() {
        single = new SingleData(new Dimension("money", "$"), new Double(17));
    }

    /**
     * Test of two argument constructor, of class SingleData.
     * Test case: successfull initialization.
     */
    @Test
    public void testTwoArgConstructorSuccess() {

        Dimension dim = new Dimension("Distance");
        Object data = new Double(17.3);

        single = new SingleData(dim,data);

        assertEquals(dim, single.getDimension());
        assertEquals(data, single.getValue());
    }


    /**
     * Test of setDimension method, of class SingleData.
     * Test case: successfull change of dimension.
     */
    @Test
    public void testSetDimensionSuccess() {

        Dimension dim = new Dimension("Miles");
        single.setDimension(dim);

        assertEquals(dim, single.getDimension());
    }

    /**
     * Test of setValue method, of class SingleData.
     * Test case: successfull change of data value.
     */
    @Test
    public void testSetValueSuccess() {

        String str = "17C";
        single.setValue(str);

        assertEquals(str, single.getValue());
    }

    /**
     * Test of setDimension method, of class SingleData.
     * Test case: unsuccessfull change of dimension - null argument.
     */
    @Test
    public void testSetDimensionNullValue() {
        try {
            single.setDimension(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set dimensions related to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValue method, of class SingleData.
     * Test case: unsuccessfull change of value - null argument.
     */
    @Test
    public void testSetValueNullValue() {
        try {
            single.setValue(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set the values releted to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class SingleData.
     * Test case: successfull cloning.
     */
    @Test
    public void testCloneSuccessfull() {
        Dimension dim = new Dimension("Km");
        single.setDimension(dim);

        single.setValue("A1");

        SingleData clone = single.clone();

        assertTrue(single != clone);

        assertTrue(single.getDimension() != clone.getDimension());
        assertEquals(single.getDimension().getName(), clone.getDimension().getName());
        assertEquals(single.getDimension().getUnit(), clone.getDimension().getUnit());

        assertEquals(single.getValue(), clone.getValue());
    }
}
