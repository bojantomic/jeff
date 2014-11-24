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

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Bojan Tomic
 */
public class SimpleTextExplanationChunkBuilderTest extends AbstractJeffTest {

    String content = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;
    
    SimpleTextExplanationChunkBuilder instance = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Create some sample TextExplanationChunk data
        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule3";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";

        content = "Explanation text";
    }

    /**
     * Test of buildChunk method, of class SimpleTextExplanationChunkBuilder.
     * Test case: unsuccessfull execution - null content
     */
    @Test
    public void testBuildChunkNullContent() {
        instance = new SimpleTextExplanationChunkBuilder();
        try {
            instance.buildChunk(context, group, rule, tags, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The content must be a non-null, non-empty string value";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }
    
    /**
     * Test of buildChunk method, of class SimpleTextExplanationChunkBuilder.
     * Test case: unsuccessfull execution - content empty String
     */
    @Test
    public void testBuildChunkContentEmptyString() {
        instance = new SimpleTextExplanationChunkBuilder();
        try {
            instance.buildChunk(context, group, rule, tags, "");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The content cannot be null or an empty string";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }
    
    /**
     * Test of buildChunk method, of class SimpleTextExplanationChunkBuilder.
     * Test case: unsuccessfull execution - wrong type content
     */
    @Test
    public void testBuildChunkWrongTypeContent() {
        instance = new SimpleTextExplanationChunkBuilder();
        try {
            instance.buildChunk(context, group, rule, tags, new Object());
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The content must be a non-null, non-empty string value";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class SimpleTextExplanationChunkBuilder.
     * Test case: successfull execution
     */
    @Test
    public void testBuildChunkSuccessfull1() {
        instance = new SimpleTextExplanationChunkBuilder();

        TextExplanationChunk tc =
                (TextExplanationChunk) (instance.buildChunk(context, group, rule, tags, content));

        //Assert that the chunk holds correct data
        assertEquals(context, tc.getContext());
        assertEquals(group, tc.getGroup());
        assertEquals(rule, tc.getRule());
        assertEquals(tags, tc.getTags());

        assertEquals(content,((String) tc.getContent()));
    }
    
}
