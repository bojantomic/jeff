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
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Bojan Tomic
 */
public class DefaultImageExplanationChunkBuilderTest extends AbstractJeffTest {

    ImageData imagedata = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;

    File unit = null;
    File dimensionNames = null;
    File imageCaptions = null;

    InternationalizationManager im = null;
    DefaultImageExplanationChunkBuilder instance = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Create some sample ImageExplanationChunk data
        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule 1";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";

        imagedata = new ImageData("URL1", "Whale photo");

        //Create a sample units file with some sample data
        unit = newFile("units_srb_RS.properties");
        PrintWriter unitpw = new PrintWriter(new FileWriter(unit));
        unitpw.println("EUR = RSD");
        unitpw.close();

        //Create a sample dimension names file with some sample data
        dimensionNames = newFile("dimension_names_srb_RS.properties");
        PrintWriter dimnamespw = new PrintWriter(new FileWriter(dimensionNames));
        dimnamespw.println("distance = razdaljina");
        dimnamespw.println("money = novac");
        dimnamespw.println("profit = dobit (profit)");
        dimnamespw.close();

        //Create a sample image captions file with some sample data
        imageCaptions = newFile("image_captions_srb_RS.properties");
        PrintWriter imagecaptpw = new PrintWriter(new FileWriter(imageCaptions));
        imagecaptpw.println("Whale\\ photo = Fotografija kita");
        imagecaptpw.println("Image\\ 1 = Slika 1");
        imagecaptpw.close();

        //The internationalization manager needs to be initialized before
        //explanation builders can use it
        im = new InternationalizationManager(new Locale("srb", "RS"));

        //Initialize the builder instance
        instance = new DefaultImageExplanationChunkBuilder(im);

    }

    @After
    public void tearDown() throws Exception {
        unit.delete();
        dimensionNames.delete();
        imageCaptions.delete();
    }

    /**
     * Test of constructor, of class DefaultImageExplanationChunkBuilder.
     * Test case: unsuccessfull execution - i18nManager is null
     */
    @Test
    public void testConstructori18nManagerNull() {
        try {
            instance= new DefaultImageExplanationChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered i18nManager instance must not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: unsuccessfull execution - content is null
     */
    @Test
    public void testBuildChunkNullContent() {
        try {
            instance.buildChunk(context, group, rule, tags, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter image data as content";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: unsuccessfull execution - wrong type content
     */
    @Test
    public void testBuildChunkWrongTypeContent() {
        try {
            instance.buildChunk(context, group, rule, tags, "content");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter image data as content";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: successfull execution - caption is null
     */
    @Test
    public void testBuildChunkSuccessfull1() {
        imagedata.setCaption(null);

        ImageExplanationChunk imc =
                (ImageExplanationChunk) (instance.buildChunk(context, group, rule, tags, imagedata));

        //Assert that the chunk holds correct data
        assertEquals(context,imc.getContext());
        assertEquals(group,imc.getGroup());
        assertEquals(rule, imc.getRule());
        assertEquals(tags,imc.getTags());
        assertEquals(imagedata,imc.getContent());

        //Assert that the caption has been left as a null String
        //and that URL remains unchanged
        assertEquals(null,((ImageData)(imc.getContent())).getCaption());
        assertEquals("URL1",((ImageData)(imc.getContent())).getURL());
    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: successfull execution - translation is performed
     */
    @Test
    public void testBuildChunkSuccessfull2() {
        ImageExplanationChunk imc =
                (ImageExplanationChunk) (instance.buildChunk(context, group, rule, tags, imagedata));

        //Assert that the chunk holds correct data
        assertEquals(context,imc.getContext());
        assertEquals(group,imc.getGroup());
        assertEquals(rule, imc.getRule());
        assertEquals(tags,imc.getTags());
        assertEquals(imagedata,imc.getContent());

        //Assert that the caption has been translated
        //and that URL remains unchanged
        assertEquals("Fotografija kita",((ImageData)(imc.getContent())).getCaption());
        assertEquals("URL1",((ImageData)(imc.getContent())).getURL());
    }

    /**
     * Test of buildChunk method, of class DefaultImageExplanationChunkBuilder.
     * Test case: successfull execution - translation does not exist
     */
    @Test
    public void testBuildChunkSuccessfull3() {
        imagedata.setCaption("Unknown picture");

        ImageExplanationChunk imc =
                (ImageExplanationChunk) (instance.buildChunk(context, group, rule, tags, imagedata));

        //Assert that the chunk holds correct data
        assertEquals(context,imc.getContext());
        assertEquals(group,imc.getGroup());
        assertEquals(rule, imc.getRule());
        assertEquals(tags,imc.getTags());
        assertEquals(imagedata,imc.getContent());

        //Assert that the caption has been left unchanged because the
        //translation doesn't exist and that URL also remains unchanged
        assertEquals("Unknown picture",((ImageData)(imc.getContent())).getCaption());
        assertEquals("URL1",((ImageData)(imc.getContent())).getURL());
    }
}
