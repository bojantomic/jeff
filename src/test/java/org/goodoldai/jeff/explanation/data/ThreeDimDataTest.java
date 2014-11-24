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
public class ThreeDimDataTest {

    ThreeDimData three;
    ArrayList<Triple> list;

    /**
     * Creates a ThreeDimData instance
     */
    @Before
    public void setUp() {
        ArrayList<Triple> data = new ArrayList<Triple>();
        data.add(new Triple("1700", "1300", "F"));

        three = new ThreeDimData(new Dimension("mon", "1000$"),
                new Dimension("mon", "1000$"), new Dimension("Fax"), data);

        list = new ArrayList<Triple>();

    }

    /**
     * Test of four argument constructor, of class ThreeDimData.
     * Test case:  successfull initialization.
     */
    @Test
    public void testFourArgumentConstructorSuccess() {
        Dimension dim1 = new Dimension("money","$");
        Dimension dim2 = new Dimension("time","year");
        Dimension dim3 = new Dimension("product");
        ArrayList<Triple> data = new ArrayList<Triple>();
        data.add(new Triple(1200, "2008", "socks"));

        three = new ThreeDimData(dim1,dim2,dim3,data);

        assertEquals(dim1, three.getDimension1());
        assertEquals(dim2, three.getDimension2());
        assertEquals(dim3, three.getDimension3());
        assertEquals(data, three.getValues());
    }

    /**
     * Test of setDimension1 method, of class ThreeDimData.
     * Test case:  successfull change of dimension.
     */
    @Test
    public void testSetDimension1Success() {
        Dimension dim = new Dimension("Miles");
        three.setDimension1(dim);

        assertEquals(dim, three.getDimension1());
    }

    /**
     * Test of setDimension2 method, of class ThreeDimData.
     * Test case:  successfull change of dimension.
     */
    @Test
    public void testSetDimension2Success() {
        Dimension dim = new Dimension("Miles");
        three.setDimension2(dim);

        assertEquals(dim, three.getDimension2());
    }

    /**
     * Test of setDimension3 method, of class ThreeDimData.
     * Test case:  successfull change of dimension.
     */
    @Test
    public void testSetDimension3Success() {
        Dimension dim = new Dimension("Miles");
        three.setDimension3(dim);

        assertEquals(dim, three.getDimension3());
    }

    /**
     * Test of setValues method, of class ThreeDimData.
     * Test case:  successfull change of data values.
     */
    @Test
    public void testSetValuesSuccess() {
        list.add(new Triple("Male", 200, true));
        list.add(new Triple("Female", 400, false));
        list.add(new Triple("Male", 0, true));
        three.setValues(list);

        assertEquals(list, three.getValues());
    }

    /**
     * Test of setDimension1 method, of class ThreeDimData.
     * Test case: un successfull change of dimension - null argument.
     */
    @Test
    public void testSetDimension1NullValue() {
        try {
            three.setDimension1(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set dimensions related to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setDimension2 method, of class ThreeDimData.
     * Test case: un successfull change of dimension - null argument.
     */
    @Test
    public void testSetDimension2NullValue() {
        try {
            three.setDimension2(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set dimensions related to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setDimension3 method, of class ThreeDimData.
     * Test case: un successfull change of dimension - null argument.
     */
    @Test
    public void testSetDimension3NullValue() {
        try {
            three.setDimension3(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set dimensions related to this data";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class ThreeDimData.
     * Test case: un successfull change of data values - null argument.
     */
    @Test
    public void testSetValuesNullValue() {
        try {
            three.setValues(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set an array of objects containing data values";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class ThreeDimData.
     * Test case: un successfull change of data values - empty list argument.
     */
    @Test
    public void testSetValuesEmptyValue() {
        try {
            three.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You have to set an array of objects containing data values";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class ThreeDimData.
     * Test case: unsuccessfull change of data values - first Triple members
     * are not from the same type.
     */
    @Test
    public void testSetValuesFirstTripleMembersWrongType() {
        try {
            list.add(new Triple("String",12, 'a'));
            list.add(new Triple(true,122, 'b'));
            three.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered Triple members  must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class ThreeDimData.
     * Test case: unsuccessfull change of data values - second Triple members
     * are not from the same type.
     */
    @Test
    public void testSetValuesSecondTripleMembersWrongType() {
        try {
            list.add(new Triple(false,12.067, 'a'));
            list.add(new Triple(true,122, 'b'));
            three.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered Triple members  must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class ThreeDimData.
     * Test case: unsuccessfull change of data values - third Triple members
     * are not from the same type.
     */
    @Test
    public void testSetValuesThirdTripleMembersWrongType() {
        try {
            list.add(new Triple(false,12, 155));
            list.add(new Triple(true,122, 'b'));
            three.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered Triple members  must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValues method, of class TwoDimData.
     * Test case: unsuccessfull change of data values - all Tuple members
     * are not from the same type.
     */
    @Test
    public void testSetValuesBothTupleMembersWrongType() {
        try {
            list.add(new Triple("Car",12, true));
            list.add(new Triple(true,12.442, 'b'));
            three.setValues(list);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered Triple members  must be from the same type";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class ThreeDimData.
     * Test case: successfull cloning.
     */
    @Test
    public void testCloneSuccessfull() {
        Dimension dim1 = new Dimension("Km");
        Dimension dim2 = new Dimension("$");
        Dimension dim3 = new Dimension("s");
        three.setDimension1(dim1);
        three.setDimension2(dim2);
        three.setDimension3(dim3);

        Triple data1 = new Triple ("data11","data12","data13");
        Triple data2 = new Triple ("data21","data22","data23");

        list.add(data1);
        list.add(data2);
        three.setValues(list);

        ThreeDimData clone = three.clone();

        assertTrue(three != clone);

        assertTrue(three.getDimension1() != clone.getDimension1());
        assertEquals(three.getDimension1().getName(), clone.getDimension1().getName());
        assertEquals(three.getDimension1().getUnit(), clone.getDimension1().getUnit());

        assertTrue(three.getDimension2() != clone.getDimension2());
        assertEquals(three.getDimension2().getName(), clone.getDimension2().getName());
        assertEquals(three.getDimension2().getUnit(), clone.getDimension2().getUnit());

        assertTrue(three.getDimension3() != clone.getDimension3());
        assertEquals(three.getDimension3().getName(), clone.getDimension3().getName());
        assertEquals(three.getDimension3().getUnit(), clone.getDimension3().getUnit());

        assertTrue(three.getValues() != clone.getValues());

        for (int i = 0; i < three.getValues().size(); i++) {

            assertTrue(three.getValues().get(i) != clone.getValues().get(i));

            assertEquals(three.getValues().get(i).getValue1(), clone.getValues().get(i).getValue1());
            assertEquals(three.getValues().get(i).getValue2(), clone.getValues().get(i).getValue2());
            assertEquals(three.getValues().get(i).getValue3(), clone.getValues().get(i).getValue3());

        }
    }
}
