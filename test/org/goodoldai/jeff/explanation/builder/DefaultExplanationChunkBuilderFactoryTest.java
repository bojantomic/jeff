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
package org.goodoldai.jeff.explanation.builder;

import junit.framework.TestCase;

/**
 * @author Bojan Tomic
 */
public class DefaultExplanationChunkBuilderFactoryTest extends TestCase {

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: unsuccessfull execution - chunk type not recognized
     */
    public void testGetExplanationChunkBuilderChunkTypeNotRecognized() {
        int type = -555;
        DefaultExplanationChunkBuilderFactory instance = new DefaultExplanationChunkBuilderFactory();

        try {
            instance.getExplanationChunkBuilder(type);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Chunk type '-555' was not recognized";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type
     */
    public void testGetExplanationChunkBuilderText1() {
        int type = ExplanationChunkBuilderFactory.TEXT;
        DefaultExplanationChunkBuilderFactory instance = new DefaultExplanationChunkBuilderFactory();
        ExplanationChunkBuilder result = instance.getExplanationChunkBuilder(type);

        assertTrue(result instanceof TextExplanationChunkBuilder);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type, assert returning
     * of same instance every time
     */
    public void testGetExplanationChunkBuilderText2() {
        int type = ExplanationChunkBuilderFactory.TEXT;
        DefaultExplanationChunkBuilderFactory instance = new DefaultExplanationChunkBuilderFactory();
        ExplanationChunkBuilder result1 = instance.getExplanationChunkBuilder(type);
        ExplanationChunkBuilder result2 = instance.getExplanationChunkBuilder(type);

        assertTrue(result1 instanceof TextExplanationChunkBuilder);
        assertTrue(result2 instanceof TextExplanationChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type
     */
    public void testGetExplanationChunkBuilderImage1() {
        int type = ExplanationChunkBuilderFactory.IMAGE;
        DefaultExplanationChunkBuilderFactory instance = new DefaultExplanationChunkBuilderFactory();
        ExplanationChunkBuilder result = instance.getExplanationChunkBuilder(type);

        assertTrue(result instanceof ImageExplanationChunkBuilder);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type, assert returning
     * of same instance every time
     */
    public void testGetExplanationChunkBuilderImage2() {
        int type = ExplanationChunkBuilderFactory.IMAGE;
        DefaultExplanationChunkBuilderFactory instance = new DefaultExplanationChunkBuilderFactory();
        ExplanationChunkBuilder result1 = instance.getExplanationChunkBuilder(type);
        ExplanationChunkBuilder result2 = instance.getExplanationChunkBuilder(type);

        assertTrue(result1 instanceof ImageExplanationChunkBuilder);
        assertTrue(result2 instanceof ImageExplanationChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type
     */
    public void testGetExplanationChunkBuilderData1() {
        int type = ExplanationChunkBuilderFactory.DATA;
        DefaultExplanationChunkBuilderFactory instance = new DefaultExplanationChunkBuilderFactory();
        ExplanationChunkBuilder result = instance.getExplanationChunkBuilder(type);

        assertTrue(result instanceof DataExplanationChunkBuilder);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type, assert returning
     * of same instance every time
     */
    public void testGetExplanationChunkBuilderData2() {
        int type = ExplanationChunkBuilderFactory.DATA;
        DefaultExplanationChunkBuilderFactory instance = new DefaultExplanationChunkBuilderFactory();
        ExplanationChunkBuilder result1 = instance.getExplanationChunkBuilder(type);
        ExplanationChunkBuilder result2 = instance.getExplanationChunkBuilder(type);

        assertTrue(result1 instanceof DataExplanationChunkBuilder);
        assertTrue(result2 instanceof DataExplanationChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

}
