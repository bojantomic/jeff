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

/**
 *
 * @author Boris Horvat
 */
public class TwoDimDataTest extends TestCase {

    TwoDimData two;
    ArrayList<Tuple> list;

    /**
     * Creates a TwoDimData instance
     */
    @Override
    public void setUp() {
        ArrayList<Tuple> data = new ArrayList<Tuple>();
        data.add(new Tuple("1700", "1300"));

        two = new TwoDimData(new Dimension("mon", "1000$"),
                new Dimension("mon", "1000$"), data);

        list = new ArrayList<Tuple>();

    }

    /**
     * Test of three argument constructor, of class TwoDimData.
     * Test case:  successfull initialization.
     */
    public void testThreeArgumentConstructorSuccess() {
        Dimension dim1 = new Dimension("money","$");
        Dimension dim2 = new Dimension("time","year");
        ArrayList<Tuple> data = new ArrayList<Tuple>();
        data.add(new Tuple("1200", "2008"));

        two = new TwoDimData(dim1,dim2,data);

        assertEquals(dim1, two.getDimension1());
        assertEquals(dim2, two.getDimension2());
        assertEquals(data, two.getValues());
    }

    /**
     * Test of setDimension1 method, of class TwoDimData.
     * Test case:  successfull change of dimension.
     */
    public void testSetDimension1Success() {
        Dimension dim = new Dimension("Miles");
        two.setDimension1(dim);

        assertEquals(dim, two.getDimension1());
    }

    /**
     * Test of setDimension2 method, of class TwoDimData.
     * Test case:  successfull change of dimension.
     */
    public void testSetDimension2Success() {
        Dimension dim = new Dimension("Miles");
        two.setDimension2(dim);

        assertEquals(dim, two.getDimension2());
    }

    /**
     * Test of setValues method, of class TwoDimData.
     * Test case: successfull change of data values.
     */
    public void testSetValuesSuccess() {
        list.add(new Tuple("Male", 200));
        list.add(new Tuple("Female", 400));
        list.add(new Tuple("Male", 0));
        two.setValues(list);

        assertEquals(list, two.getValues());
    }

    /**
     * Test of setDimension1 method, of class TwoDimData.
     * Test case: unsuccessfull change of dimension - null argument.
     */
    public void testSetDimension1NullValue() {
        try {
            two.setDimension1(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set dimensions related to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setDimension2 method, of class TwoDimData.
     * Test case: unsuccessfull change of dimension - null argument.
     */
    public void testSetDimension2NullValue() {
        try {
            two.setDimension2(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set dimensions related to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class TwoDimData.
     * Test case: unsuccessfull change of data values - null argument.
     */
    public void testSetValuesNullValue() {
        try {
            two.setValues(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set an array of objects containing data values";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class TwoDimData.
     * Test case: unsuccessfull change of data values - empty list argument.
     */
    public void testSetValuesEmptyValue() {
        try {
            two.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set an array of objects containing data values";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class TwoDimData.
     * Test case: unsuccessfull change of data values - first Tuple members
     * are not from the same type.
     */
    public void testSetValuesFirstTupleMembersWrongType() {
        try {
            list.add(new Tuple("String",12));
            list.add(new Tuple(true,12));
            two.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered Tuple members  must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class TwoDimData.
     * Test case: unsuccessfull change of data values - second Tuple members
     * are not from the same type.
     */
    public void testSetValuesSecondTupleMembersWrongType() {
        try {
            list.add(new Tuple(true,12.66));
            list.add(new Tuple(true,12));
            two.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered Tuple members  must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class TwoDimData.
     * Test case: unsuccessfull change of data values - both Tuple members
     * are not from the same type.
     */
    public void testSetValuesBothTupleMembersWrongType() {
        try {
            list.add(new Tuple("String",12.66));
            list.add(new Tuple(true,12));
            two.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered Tuple members  must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class TwoDimData.
     * Test case: successfull cloning.
     */
    public void testCloneSuccessfull() {
        Dimension dim1 = new Dimension("Km");
        Dimension dim2 = new Dimension("$");
        two.setDimension1(dim1);
        two.setDimension2(dim2);

        Tuple data1 = new Tuple("data11", "data12");
        Tuple data2 = new Tuple("data21", "data22");

        list.add(data1);
        list.add(data2);
        two.setValues(list);

        TwoDimData clone = two.clone();

        assertTrue(two != clone);

        assertTrue(two.getDimension1() != clone.getDimension1());
        assertEquals(two.getDimension1().getName(), clone.getDimension1().getName());
        assertEquals(two.getDimension1().getUnit(), clone.getDimension1().getUnit());

        assertTrue(two.getDimension2() != clone.getDimension2());
        assertEquals(two.getDimension2().getName(), clone.getDimension2().getName());
        assertEquals(two.getDimension2().getUnit(), clone.getDimension2().getUnit());

        assertTrue(two.getValues() != clone.getValues());

        for (int i = 0; i < two.getValues().size(); i++) {

            assertTrue(two.getValues().get(i) != clone.getValues().get(i));

            assertEquals(two.getValues().get(i).getValue1(), clone.getValues().get(i).getValue1());
            assertEquals(two.getValues().get(i).getValue2(), clone.getValues().get(i).getValue2());

        }
    }
}
