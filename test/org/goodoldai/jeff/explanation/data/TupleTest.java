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
public class TupleTest extends TestCase {

    Tuple tuple;

    /**
     * Creates a Tuple instance
     */
    @Override
    public void setUp() {
        tuple = new Tuple("1700", "1300");
    }

    /**
     * Test of two argument constructor, of class Tuple.
     * Test case: successfull initialization.
     */
    public void testTwoArgConstructorSuccess() {
        String str = "17C";
        String str2 = "17F";
        Tuple instance = new Tuple(str, str2);

        assertEquals(str, instance.getValue1());
        assertEquals(str2, instance.getValue2());
    }

    /**
     * Test of setValue1 method, of class Tuple.
     * Test case: successfull change of first data value.
     */
    public void testSetValue1Success() {
        String str = "17C";
        tuple.setValue1(str);

        assertEquals(str, tuple.getValue1());
    }

    /**
     * Test of setValue2 method, of class Tuple.
     * Test case: successfull change of second data value.
     */
    public void testSetValue2Success() {
        String str = "17C";
        tuple.setValue2(str);

        assertEquals(str, tuple.getValue2());
    }

    /**
     * Test of setValue1 method, of class Tuple.
     * Test case: unsuccessfull change of first data value - null argument.
     */
    public void testSetValue1NullValue() {
        try {
            tuple.setValue1(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a data value";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValue2 method, of class Tuple.
     * Test case: unsuccessfull change of second data value - null argument.
     */
    public void testSetValue2NullValue() {
        try {
            tuple.setValue2(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a data value";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class Tuple.
     * Test case: successfull cloning.
     */
    public void testCloneSuccessfull() {
        String str = "17C";
        String str2 = "17F";
        tuple.setValue1(str);
        tuple.setValue2(str2);

        Tuple clone = tuple.clone();

        assertTrue(tuple != clone);

        assertEquals(tuple.getValue1(), clone.getValue1());

        assertEquals(tuple.getValue2(), clone.getValue2());
    }
}
