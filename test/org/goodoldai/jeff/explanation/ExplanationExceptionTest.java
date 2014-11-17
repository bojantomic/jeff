/*
 * Copyright 2009 Bojan Tomic
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
package org.goodoldai.jeff.explanation;

import junit.framework.TestCase;

/**
 *
 * @author Bojan Tomic
 */
public class ExplanationExceptionTest extends TestCase {
    
    /**
     * Test of the constructor, of class ExplanationException
     */
    public void testConstructorSuccessfull(){
        try{
            throw new ExplanationException("Test message");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Test message";
            assertTrue(e instanceof ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of the constructor, of class ExplanationException
     */
    public void testConstructorSuccessfull2(){
        try{
            throw new ExplanationException(null);
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = null;
            assertTrue(e instanceof ExplanationException);
            assertEquals(expResult, result);
        }
    }

}
