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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Nemanja Jovanovic
 */
public class TripleTest {

    Triple triple;

    /**
     * Creates a Triple instance
     */
    @Before
    public void setUp() {
        triple = new Triple("1700", "1300", "F");
    }

    /**
     * Test of three argument constructor, of class Triple.
     * Test case: successfull initialization.
     */
    @Test
    public void testThreeArgConstructorSuccess() {
        String str = "17C";
        String str2 = "17F";
        String str3 = "10$";
        Triple instance = new Triple(str,str2,str3);

        assertEquals(str, instance.getValue1());
        assertEquals(str2, instance.getValue2());
        assertEquals(str3, instance.getValue3());
    }

    /**
     * Test of setValue1 method, of class Triple.
     * Test case: successfull change of first data value.
     */
    @Test
    public void testSetValue1Success() {
        String str = "17C";
        triple.setValue1(str);

        assertEquals(str, triple.getValue1());
    }

    /**
     * Test of setValue2 method, of class Triple.
     * Test case: successfull change of second data value.
     */
    @Test
    public void testSetValue2Success() {
        String str = "17C";
        triple.setValue2(str);

        assertEquals(str, triple.getValue2());
    }

    /**
     * Test of setValue3 method, of class Triple.
     * Test case: successfull change of third data value.
     */
    @Test
    public void testSetValue3Success() {
        String str = "17C";
        triple.setValue3(str);

        assertEquals(str, triple.getValue3());
    }

    /**
     * Test of setValue1 method, of class Triple.
     * Test case: unsuccessfull change of first data value - null argument.
     */
    @Test
    public void testSetValue1NullValue() {
        try {
            triple.setValue1(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a data value";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValue2 method, of class Triple.
     * Test case: unsuccessfull change of second data value - null argument.
     */
    @Test
    public void testSetValue2NullValue() {
        try {
            triple.setValue2(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a data value";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setValue3 method, of class Triple.
     * Test case: unsuccessfull change of third data value - null argument.
     */
    @Test
    public void testSetValue3NullValue() {
        try {
            triple.setValue3(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a data value";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class Triple.
     * Test case: successfull cloning.
     */
    @Test
    public void testCloneSuccessfull() {
        String str = "17C";
        String str2 = "17F";
        String str3 = "10$";
        triple.setValue1(str);
        triple.setValue2(str2);
        triple.setValue3(str3);

        Triple clone = triple.clone();

        assertTrue(triple != clone);

        assertEquals(triple.getValue1(), clone.getValue1());
        assertEquals(triple.getValue2(), clone.getValue2());
        assertEquals(triple.getValue3(), clone.getValue3());
    }
}
