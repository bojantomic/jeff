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
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Bojan Tomic
 */
public class SimpleDataExplanationChunkBuilderTest extends AbstractJeffTest {
    
    SingleData sdata = null;
    OneDimData odata = null;
    TwoDimData twdata = null;
    ThreeDimData thdata = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;

    SimpleDataExplanationChunkBuilder instance = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Create some sample DataExplanationChunk data
        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule 1";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";

        sdata = new SingleData(new Dimension("money", "EUR"), new Double(1700));

        ArrayList<Object> data = new ArrayList<Object>();
        data.add(new Double(1700));
        odata = new OneDimData(new Dimension("money", "EUR"), data);

        ArrayList<Tuple> data2 = new ArrayList<Tuple>();
        data2.add(new Tuple("1700", "1300"));
        twdata = new TwoDimData(new Dimension("money", "USD"),
                new Dimension("profit", "EUR"), data2);

        ArrayList<Triple> data3 = new ArrayList<Triple>();
        data3.add(new Triple("1700", "1300", "Male"));
        thdata = new ThreeDimData(new Dimension("money", "EUR"),
                new Dimension("profit", "USD"), new Dimension("gender"), data3);
    }

    /**
     * Test of buildChunk method, of class SimpleDataExplanationChunkBuilder.
     * Test case: unsuccessfull execution - content is null
     */
    @Test
    public void testBuildChunkNullContent() {
        instance = new SimpleDataExplanationChunkBuilder();
        try {
            instance.buildChunk(context, group, rule, tags, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The content must be a non-null object from"+"" +
            " the following classes: SingleData, OneDimData, TwoDimData, ThreeDimData";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class SimpleDataExplanationChunkBuilder.
     * Test case: unsuccessfull execution - wrong type content
     */
    @Test
    public void testBuildChunkWrongTypeContent() {
        instance = new SimpleDataExplanationChunkBuilder();
        try {
            instance.buildChunk(context, group, rule, tags, "wrong type");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The content must be a non-null object from"+"" +
            " the following classes: SingleData, OneDimData, TwoDimData, ThreeDimData";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class SimpleDataExplanationChunkBuilder.
     * Test case: successfull execution - SingleData content
     */
    @Test
    public void testBuildChunkSuccessfullSingleData1() {
        instance = new SimpleDataExplanationChunkBuilder();

        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, sdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(sdata, dc.getContent());

        assertEquals("money", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals("EUR", ((SingleData) dc.getContent()).getDimension().getUnit());

    }

    /**
     * Test of buildChunk method, of class SimpleDataExplanationChunkBuilder.
     * Test case: successfull execution - OneDimData content
     */
    @Test
    public void testBuildChunkSuccessfullOneDimData() {
        instance = new SimpleDataExplanationChunkBuilder();

        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, odata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(odata, dc.getContent());

        assertEquals("money", ((OneDimData) dc.getContent()).getDimension().getName());
        assertEquals("EUR", ((OneDimData) dc.getContent()).getDimension().getUnit());

    }

    /**
     * Test of buildChunk method, of class SimpleDataExplanationChunkBuilder.
     * Test case: successfull execution - TwoDimData content
     */
    @Test
    public void testBuildChunkSuccessfullTwoDimData() {
        instance = new SimpleDataExplanationChunkBuilder();

        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, twdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(twdata, dc.getContent());

        assertEquals("money", ((TwoDimData) dc.getContent()).getDimension1().getName());
        assertEquals("USD", ((TwoDimData) dc.getContent()).getDimension1().getUnit());
        assertEquals("profit", ((TwoDimData) dc.getContent()).getDimension2().getName());
        assertEquals("EUR", ((TwoDimData) dc.getContent()).getDimension2().getUnit());

    }

    /**
     * Test of buildChunk method, of class SimpleDataExplanationChunkBuilder.
     * Test case: successfull execution - ThreeDimData content
     */
    @Test
    public void testBuildChunkSuccessfullThreeDimData() {
        instance = new SimpleDataExplanationChunkBuilder();

        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, thdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(thdata, dc.getContent());

        assertEquals("money", ((ThreeDimData) dc.getContent()).getDimension1().getName());
        assertEquals("EUR", ((ThreeDimData) dc.getContent()).getDimension1().getUnit());
        assertEquals("profit", ((ThreeDimData) dc.getContent()).getDimension2().getName());
        assertEquals("USD", ((ThreeDimData) dc.getContent()).getDimension2().getUnit());
        assertEquals("gender", ((ThreeDimData) dc.getContent()).getDimension3().getName());
        assertEquals(null, ((ThreeDimData) dc.getContent()).getDimension3().getUnit());

    }

}
