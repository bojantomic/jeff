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

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * @author Bojan Tomic
 */
public class DefaultDataExplanationChunkBuilderTest extends TestCase {

    SingleData sdata = null;
    OneDimData odata = null;
    TwoDimData twdata = null;
    ThreeDimData thdata = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;
    File unit = null;
    File dimensionNames = null;
    File imageCaptions = null;
    InternationalizationManager im = null;
    DefaultDataExplanationChunkBuilder instance = null;

    @Override
    protected void setUp() throws Exception {
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

        //Create a sample units file with some sample data
        unit = new File("test" + File.separator + "units_srb_RS.properties");
        PrintWriter unitpw = new PrintWriter(new FileWriter(unit));
        unitpw.println("EUR = RSD");
        unitpw.close();

        //Create a sample dimension names file with some sample data
        dimensionNames = new File("test" + File.separator + "dimension_names_srb_RS.properties");
        PrintWriter dimnamespw = new PrintWriter(new FileWriter(dimensionNames));
        dimnamespw.println("distance = razdaljina");
        dimnamespw.println("money = novac");
        dimnamespw.println("profit = dobit (profit)");
        dimnamespw.println("gender = pol");
        dimnamespw.close();

        //Create a sample image captions file with some sample data
        imageCaptions = new File("test" + File.separator + "image_captions_srb_RS.properties");
        PrintWriter imagecaptpw = new PrintWriter(new FileWriter(imageCaptions));
        imagecaptpw.println("Whale\\ photo = Fotografija kita");
        imagecaptpw.println("Image\\ 1 = Slika 1");
        imagecaptpw.close();

        //The internationalization manager needs to be initialized before
        //explanation builders can use it
        im = new InternationalizationManager(new Locale("srb", "RS"));

        //Initialize the builder instance
        instance = new DefaultDataExplanationChunkBuilder(im);
    }

    @Override
    protected void tearDown() throws Exception {
        unit.delete();
        dimensionNames.delete();
        imageCaptions.delete();
    }

    /**
     * Test of constructor, of class DefaultDataExplanationChunkBuilder.
     * Test case: unsuccessfull execution - i18nManager is null
     */
    public void testConstructori18nManagerNull() {
        try {
            instance= new DefaultDataExplanationChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered i18nManager instance must not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: unsuccessfull execution - content is null
     */
    public void testBuildChunkNullContent() {
        try {
            instance.buildChunk(context, group, rule, tags, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered content must be a non-null" +
                    " instance of SingleData, OneDimData, TwoDimData or ThreeDimData";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: unsuccessfull execution - wrong type content
     */
    public void testBuildChunkWrongTypeContent() {
        try {
            instance.buildChunk(context, group, rule, tags, "wrong type");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered content must be a non-null" +
                    " instance of SingleData, OneDimData, TwoDimData or ThreeDimData";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: successfull execution - SingleData content
     */
    public void testBuildChunkSuccessfullSingleData1() {
        
        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, sdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(sdata, dc.getContent());

        //Assert that the dimension name and unit translations are
        //correct
        assertEquals("novac", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals("RSD", ((SingleData) dc.getContent()).getDimension().getUnit());

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: successfull execution - SingleData content, no unit
     */
    public void testBuildChunkSuccessfullSingleData2() {
        
        //Set the unit to null
        sdata.getDimension().setUnit(null);

        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, sdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(sdata, dc.getContent());

        //Assert that the dimension name is translated and that the unit is
        //left null
        assertEquals("novac", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals(null, ((SingleData) dc.getContent()).getDimension().getUnit());

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: successfull execution - SingleData content, no dimension
     * name translation, no unit name translation
     */
    public void testBuildChunkSuccessfullSingleData3() {
        
        //Set the dimension name and unit so that no translation exists
        sdata.getDimension().setName("cash");
        sdata.getDimension().setUnit("YEN");

        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, sdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(sdata, dc.getContent());

        //Assert that the dimension name and unit are unchanged
        assertEquals("cash", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals("YEN", ((SingleData) dc.getContent()).getDimension().getUnit());

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: successfull execution - OneDimData content
     */
    public void testBuildChunkSuccessfullOneDimData() {
        
        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, odata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(odata, dc.getContent());

        //Assert that the dimension name and unit translations are
        //correct
        assertEquals("novac", ((OneDimData) dc.getContent()).getDimension().getName());
        assertEquals("RSD", ((OneDimData) dc.getContent()).getDimension().getUnit());

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: successfull execution - TwoDimData content
     */
    public void testBuildChunkSuccessfullTwoDimData() {
        
        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, twdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(twdata, dc.getContent());

        //Assert that the dimension name and unit translations are
        //correct for both dimensions
        assertEquals("novac", ((TwoDimData) dc.getContent()).getDimension1().getName());
        assertEquals("USD", ((TwoDimData) dc.getContent()).getDimension1().getUnit());
        assertEquals("dobit (profit)", ((TwoDimData) dc.getContent()).getDimension2().getName());
        assertEquals("RSD", ((TwoDimData) dc.getContent()).getDimension2().getUnit());

    }

    /**
     * Test of buildChunk method, of class DefaultDataExplanationChunkBuilder.
     * Test case: successfull execution - ThreeDimData content
     */
    public void testBuildChunkSuccessfullThreeDimData() {
        
        DataExplanationChunk dc =
                (DataExplanationChunk) (instance.buildChunk(context, group, rule, tags, thdata));

        //Assert that the chunk holds correct data
        assertEquals(context, dc.getContext());
        assertEquals(group, dc.getGroup());
        assertEquals(rule, dc.getRule());
        assertEquals(tags, dc.getTags());
        assertEquals(thdata, dc.getContent());

        //Assert that the dimension name and unit translations are
        //correct for all three dimensions
        assertEquals("novac", ((ThreeDimData) dc.getContent()).getDimension1().getName());
        assertEquals("RSD", ((ThreeDimData) dc.getContent()).getDimension1().getUnit());
        assertEquals("dobit (profit)", ((ThreeDimData) dc.getContent()).getDimension2().getName());
        assertEquals("USD", ((ThreeDimData) dc.getContent()).getDimension2().getUnit());
        assertEquals("pol", ((ThreeDimData) dc.getContent()).getDimension3().getName());
        assertEquals(null, ((ThreeDimData) dc.getContent()).getDimension3().getUnit());

    }
}
