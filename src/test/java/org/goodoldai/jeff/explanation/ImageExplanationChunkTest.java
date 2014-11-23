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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Bojan Tomic
 */
public class ImageExplanationChunkTest extends ExplanationChunkTest {

    public ExplanationChunk getInstance(Object content) {
        return new ImageExplanationChunk(content);
    }

    public ExplanationChunk getInstance(int context, String group, String rule, String[] tags, Object content) {
        return new ImageExplanationChunk(context, group, rule, tags, content);
    }

    public Object getSampleContent() {
        return new ImageData("sample.jpg", "SAMPLE");
    }

    /**
     * Test of setContent method, of class ImageExplanationChunk.
     * Test case:  successfull change of content.
     */
    @Test
    public void testSetContent() {
        ImageData content = new ImageData("sample2.jpg");
        instance.setContent(content);

        assertEquals(content, instance.getContent());
    }

    /**
     * Test of setContent method, of class ImageExplanationChunk.
     * Test case: unsuccessfull change of content - wrong type content.
     */
    @Test
    public void testSetContentWrongTypeContent() {
        Object val = new Object();

        try {
            instance.setContent(val);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The content must be a non-null ImageData object";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setContent method, of class ImageExplanationChunk.
     * Test case: unsuccessfull change of content - null content.
     */
    @Test
    public void testSetContentNullContent() {

        try {
            instance.setContent(null);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The content must be a non-null ImageData object";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class ImageExplanationChunk.
     * Test case:  successfull clone operation.
     */
    @Test
    public void testCloneSuccessfull() {
        ImageExplanationChunk clone = (ImageExplanationChunk)(instance.clone());

        //Test if it is not the same object
        assertTrue(clone != instance);

        //Test if attribute values have been sucessfully copied
        assertEquals(instance.getContext(), clone.getContext());
        assertEquals(instance.getGroup(), clone.getGroup());
        assertEquals(instance.getRule(), clone.getRule());

        //Test if tags have been properly cloned
        if (instance.getTags() != null) {
            assertTrue(instance.getTags() != clone.getTags());
        }

        for (int i = 0; i < instance.getTags().length; i++) {
            assertEquals(instance.getTags()[i], clone.getTags()[i]);
        }

        //test if content has been successfully cloned
        if (instance.getContent() != null) {
            assertTrue(instance.getContent() != clone.getContent());
        }

        ImageData instancecontent = (ImageData) (instance.getContent());
        ImageData clonecontent = (ImageData) (clone.getContent());
        assertEquals(instancecontent.getCaption(), clonecontent.getCaption());
        assertEquals(instancecontent.getURL(), clonecontent.getURL());
    }
}
