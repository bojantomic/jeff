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
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Bojan Tomic
 */
public class SimpleExplanationBuilderTest extends ExplanationBuilderTest {

    //Text chunk sample data
    String tcontent = null;
    int tcontext = 0;
    String tgroup = null;
    String trule = null;
    String[] ttags = null;

    //Image chunk sample data
    ImageData icontent = null;
    int icontext = 0;
    String igroup = null;
    String irule = null;
    String[] itags = null;

    //Data chunk sample data
    SingleData dcontent = null;
    int dcontext = 0;
    String dgroup = null;
    String drule = null;
    String[] dtags = null;

    public ExplanationChunkBuilderFactory getFactory() {
        return new SimpleExplanationChunkBuilderFactory();
    }

    public ExplanationBuilder getInstance(Explanation explanation, ExplanationChunkBuilderFactory factory) {
        return new SimpleExplanationBuilder(explanation, factory);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Create some sample TextExplanationChunk data
        tcontext = ExplanationChunk.ERROR;
        tgroup = "group 1";
        trule = "rule3";
        ttags = new String[2];
        ttags[0] = "tagt1";
        ttags[1] = "tagt2";

        tcontent = "Explanation text";

        //Create some sample ImageExplanationChunk data
        icontext = ExplanationChunk.VERY_NEGATIVE;
        igroup = null;
        irule = "rule 1";
        itags = new String[2];
        itags[0] = "tagi1";
        itags[1] = "tagi2";

        icontent = new ImageData("URL1", "Whale photo");

        //Create some sample DataExplanationChunk data
        dcontext = ExplanationChunk.WARNING;
        dgroup = "group 1";
        drule = "rule 4";
        dtags = new String[1];
        dtags[0] = "tagd1";

        dcontent = new SingleData(new Dimension("money", "EUR"), new Double(1700));

        explanation = new Explanation("owner 1", "srb", "RS",null);
        instance = getInstance(explanation,getFactory());
    }

    /**
     * Test of addExplanationChunk method, of class SimpleExplanationBuilder.
     * Test case: succesfull execution - text chunk added
     */
    @Test
    public void testAddExplanationSuccessfullTextChunk() {

        instance = getInstance(explanation, getFactory());

        instance.addExplanationChunk(ExplanationChunkBuilderFactory.TEXT,
                tcontext, tgroup, trule, ttags, tcontent);

        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

        //Assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, chunks.size());
        assertTrue(chunks.get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                (TextExplanationChunk) (chunks.get(0));

        //Assert that the chunk holds correct data
        assertEquals(tcontext, tc.getContext());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(trule, tc.getRule());
        assertTrue(tc.getTags().length == 2);
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);

        assertEquals(tcontent, ((String) tc.getContent()));
    }

    /**
     * Test of addExplanationChunk method, of class SimpleExplanationBuilder.
     * Test case: succesfull execution - image chunk added
     */
    @Test
    public void testAddExplanationSuccessfullImageChunk() {

        instance = getInstance(explanation, getFactory());

        instance.addExplanationChunk(ExplanationChunkBuilderFactory.IMAGE,
                icontext, igroup, irule, itags, icontent);

        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

        //Assert that one chunk was added and that it is an ImageExplanationChunk
        assertEquals(1, chunks.size());
        assertTrue(chunks.get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                (ImageExplanationChunk) (chunks.get(0));

        //Assert that the chunk holds correct data
        assertEquals(icontext, ic.getContext());
        assertEquals(igroup, ic.getGroup());
        assertEquals(irule, ic.getRule());
        assertTrue(ic.getTags().length == 2);
        assertEquals(itags[0], ic.getTags()[0]);
        assertEquals(itags[1], ic.getTags()[1]);
        assertTrue(ic.getContent() instanceof ImageData);

        assertEquals("Whale photo", ((ImageData) (ic.getContent())).getCaption());
        assertEquals("URL1", ((ImageData) (ic.getContent())).getURL());
    }

    /**
     * Test of addExplanationChunk method, of class SimpleExplanationBuilder.
     * Test case: succesfull execution - data chunk added
     */
    @Test
    public void testAddExplanationSuccessfullDataChunk() {

        instance = getInstance(explanation, getFactory());

        instance.addExplanationChunk(ExplanationChunkBuilderFactory.DATA,
                dcontext, dgroup, drule, dtags, dcontent);

        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

        //Assert that one chunk was added and that it is a DataExplanationChunk
        assertEquals(1, chunks.size());
        assertTrue(chunks.get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                (DataExplanationChunk) (chunks.get(0));

        //Assert that the chunk holds correct data
        assertEquals(dcontext, dc.getContext());
        assertEquals(dgroup, dc.getGroup());
        assertEquals(drule, dc.getRule());
        assertTrue(dc.getTags().length == 1);
        assertEquals(dtags[0], dc.getTags()[0]);
        assertTrue(dc.getContent() instanceof SingleData);

        assertEquals("money", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals("EUR", ((SingleData) dc.getContent()).getDimension().getUnit());
    }

    /**
     * Test of addExplanationChunk method, of class SimpleExplanationBuilder.
     * Test case: succesfull execution - three chunks added
     */
    @Test
    public void testAddExplanationSuccessfullThreeChunks() {

        instance = getInstance(explanation, getFactory());

        //Add three chunks
        instance.addExplanationChunk(ExplanationChunkBuilderFactory.TEXT,
                tcontext, tgroup, trule, ttags, tcontent);
        instance.addExplanationChunk(ExplanationChunkBuilderFactory.IMAGE,
                icontext, igroup, irule, itags, icontent);
        instance.addExplanationChunk(ExplanationChunkBuilderFactory.DATA,
                dcontext, dgroup, drule, dtags, dcontent);


        ArrayList<ExplanationChunk> chunks = explanation.getChunks();

        //Assert that three chunks were added and that they are the
        //appropriate type
        assertEquals(3, chunks.size());
        assertTrue(chunks.get(0) instanceof TextExplanationChunk);
        assertTrue(chunks.get(1) instanceof ImageExplanationChunk);
        assertTrue(chunks.get(2) instanceof DataExplanationChunk);



        //Check first (Text) chunk
        TextExplanationChunk tc =
                (TextExplanationChunk) (chunks.get(0));

        //Assert that the chunk holds correct data
        assertEquals(tcontext, tc.getContext());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(trule, tc.getRule());
        assertTrue(tc.getTags().length == 2);
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);

        assertEquals(tcontent,((String) tc.getContent()));



        //Check second (Image) chunk
        ImageExplanationChunk ic =
                (ImageExplanationChunk) (chunks.get(1));

        //Assert that the chunk holds correct data
        assertEquals(icontext, ic.getContext());
        assertEquals(igroup, ic.getGroup());
        assertEquals(irule, ic.getRule());
        assertTrue(ic.getTags().length == 2);
        assertEquals(itags[0], ic.getTags()[0]);
        assertEquals(itags[1], ic.getTags()[1]);
        assertTrue(ic.getContent() instanceof ImageData);

        assertEquals("Whale photo", ((ImageData) (ic.getContent())).getCaption());
        assertEquals("URL1", ((ImageData) (ic.getContent())).getURL());




        //Check third (Data) chunk
        DataExplanationChunk dc =
                (DataExplanationChunk) (chunks.get(2));

        //Assert that the chunk holds correct data
        assertEquals(dcontext, dc.getContext());
        assertEquals(dgroup, dc.getGroup());
        assertEquals(drule, dc.getRule());
        assertTrue(dc.getTags().length == 1);
        assertEquals(dtags[0], dc.getTags()[0]);
        assertTrue(dc.getContent() instanceof SingleData);

        assertEquals("money", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals("EUR", ((SingleData) dc.getContent()).getDimension().getUnit());
    }
}
