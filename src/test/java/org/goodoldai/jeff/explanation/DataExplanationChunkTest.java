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

import org.goodoldai.jeff.explanation.data.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Bojan Tomic
 */
public class DataExplanationChunkTest extends ExplanationChunkTest {

    public ExplanationChunk getInstance(Object content) {
        return new DataExplanationChunk(content);
    }

    public ExplanationChunk getInstance(int context, String group, String rule, String[] tags, Object content) {
        return new DataExplanationChunk(context, group, rule, tags, content);
    }

    public Object getSampleContent() {
        return new SingleData(new Dimension("money", "$"), new Double(17));
    }

    /**
     * Test of setContent method, of class DataExplanationChunk.
     * Test case:  successfull change of content - content is SingleData object.
     */
    @Test
    public void testSetContentSuccessfullSingleData() {
        SingleData content =
                new SingleData(new Dimension("mon", "1000$"), new Double(1700));
        instance.setContent(content);

        assertEquals(content, instance.getContent());
    }

    /**
     * Test of setContent method, of class DataExplanationChunk.
     * Test case:  successfull change of content - content is OneDimData object.
     */
    @Test
    public void testSetContentSuccessfullOneDimData() {
        ArrayList<Object> data = new ArrayList<Object>();
        data.add(new Double(1700));
        OneDimData content =
                new OneDimData(new Dimension("mon", "1000$"), data);
        instance.setContent(content);

        assertEquals(content, instance.getContent());
    }

    /**
     * Test of setContent method, of class DataExplanationChunk.
     * Test case:  successfull change of content - content is TwoDimData object.
     */
    @Test
    public void testSetContentSuccessfullTwoDimData() {
        ArrayList<Tuple> data = new ArrayList<Tuple>();
        data.add(new Tuple("1700", "1300"));
        TwoDimData content = new TwoDimData(new Dimension("mon", "1000$"),
                new Dimension("mon", "1000$"), data);
        instance.setContent(content);

        assertEquals(content, instance.getContent());
    }

    /**
     * Test of setContent method, of class DataExplanationChunk.
     * Test case:  successfull change of content - content is ThreeDimData object.
     */
    @Test
    public void testSetContentSuccessfullThreeDimData() {
        ArrayList<Triple> data = new ArrayList<Triple>();
        data.add(new Triple("1700", "1300", "F"));
        ThreeDimData content = new ThreeDimData(new Dimension("mon", "1000$"),
                new Dimension("mon", "1000$"), new Dimension("Fax"), data);
        instance.setContent(content);

        assertEquals(content, instance.getContent());
    }

    /**
     * Test of setContent method, of class DataExplanationChunk.
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
            String expResult = "The content must be a non-null object from the following classes: SingleData, OneDimData, TwoDimData, ThreeDimData";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setContent method, of class DataExplanationChunk.
     * Test case: unsuccessfull change of content - null content.
     */
    @Test
    public void testSetContentNullContent() {

        try {
            instance.setContent(null);
            fail("An exception should have been thrown but wasn't");
        } catch (Exception e) {
            assertTrue(e instanceof ExplanationException);
            String expResult = "The content must be a non-null object from the following classes: SingleData, OneDimData, TwoDimData, ThreeDimData";
            String result = e.getMessage();
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of clone method, of class DataExplanationChunk.
     * Test case:  successfull clone operation.
     */
    @Test
    public void testCloneSuccessfull() {
        DataExplanationChunk clone = (DataExplanationChunk)(instance.clone());

        //Test if it is not the same object
        assertTrue(clone != instance);

        //Test if attribute values have been sucessfully copied
        assertEquals(instance.getContext(), clone.getContext());
        assertEquals(instance.getGroup(), clone.getGroup());
        assertEquals(instance.getRule(), clone.getRule());

        //Test if tags have been properly cloned
        if (instance.getTags() != null) {
            assertTrue(instance.getTags() != clone.getTags());

            for (int i = 0; i < instance.getTags().length; i++) {
                assertEquals(instance.getTags()[i], clone.getTags()[i]);
            }
        }

        //test if content has been successfully cloned
        if (instance.getContent() != null) {
            assertTrue(instance.getContent() != clone.getContent());
        }

        SingleData instancecontent = (SingleData) (instance.getContent());
        SingleData clonecontent = (SingleData) (clone.getContent());
        assertTrue(instancecontent.getDimension() != clonecontent.getDimension());
        assertEquals(instancecontent.getDimension().getName(), clonecontent.getDimension().getName());
        assertEquals(instancecontent.getDimension().getUnit(), clonecontent.getDimension().getUnit());
        assertEquals(instancecontent.getValue(), clonecontent.getValue());
    }
}
