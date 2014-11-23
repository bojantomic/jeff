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
public class OneDimDataTest {

    OneDimData one;
    ArrayList<Object> list;
    ArrayList<Object> list1;
    ArrayList<Object> list2;

    /**
     * Creates a OneDimData instance
     */
    @Before
    public void setUp() {
        Dimension dim = new Dimension("Km");
        list = new ArrayList<Object>();
        list.add("data");
        one = new OneDimData(dim,list);
        
        list1 = new ArrayList<Object>();
        list2 = new ArrayList<Object>();
        list2.add(145);
        list2.add("test");
    }

    /**
     * Test of two argument constructor, of class OneDimData.
     * Test case: successfull initialization.
     */
    @Test
    public void testTwoArgumentConstructorSuccess() {

        Dimension dim = new Dimension("Distance", "miles");
        list = new ArrayList<Object>();
        list.add("data");
        one = new OneDimData(dim,list);

        assertEquals(dim, one.getDimension());
    }

    /**
     * Test of setDimension method, of class OneDimData.
     * Test case: successfull change of dimension.
     */
    @Test
    public void testSetDimensionSuccess() {

        Dimension dim = new Dimension("Miles");
        one.setDimension(dim);

        assertEquals(dim, one.getDimension());
        assertEquals(list, one.getValues());
    }

    /**
     * Test of setValues method, of class OneDimData.
     * Test case: successfull change of data values.
     */
    @Test
    public void testSetValuesSuccess() {

        ArrayList<Object> list3 = new ArrayList<Object>();
        list3.add("element1");
        one.setValues(list3);

        assertEquals(list3, one.getValues());
    }

    /**
     * Test of setDimension method, of class OneDimData.
     * Test case: unsuccessfull change of dimension - null argument.
     */
    @Test
    public void testSetDimensionNullValue() {
        try {
            one.setDimension(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set dimensions related to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class OneDimData.
     * Test case: unsuccessfull change of values - null argument.
     */
    @Test
    public void testSetValuesNullValue() {
        try {
            one.setValues(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set an array of objects containing data values";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class OneDimData.
     * Test case: unsuccessfull change of values - empty list argument.
     */
    @Test
    public void testSetValuesEmptyValue() {
        try {
            one.setValues(list1);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set an array of objects containing data values";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class OneDimData.
     * Test case: unsuccessfull change of values - list holds different types of values.
     */
    @Test
    public void testSetValuesDifferentTypeValue() {
        try {
            one.setValues(list2);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered array elements must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class OneDimData.
     * Test case: successfull cloning.
     */
    @Test
    public void testCloneSuccessfull() {
        Dimension dim = new Dimension("Km");
        one.setDimension(dim);
        list1.add("1");
        list1.add("2");
        one.setValues(list1);

        OneDimData clone = one.clone();

        assertTrue(one != clone);

        assertTrue(one.getDimension() != clone.getDimension());
        assertEquals(one.getDimension().getName(), clone.getDimension().getName());
        assertEquals(one.getDimension().getUnit(), clone.getDimension().getUnit());

        assertTrue(one.getValues() != clone.getValues());
        for (int i = 0; i < one.getValues().size(); i++) {
            assertEquals(one.getValues().get(i), clone.getValues().get(i));
        }
    }
}
