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

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;
import junit.framework.TestCase;
import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Bojan Tomic
 */
public class DefaultExplanationChunkBuilderFactoryTest extends AbstractJeffTest {

    File unit = null;
    File dimensionNames = null;
    File imageCaptions = null;

    InternationalizationManager im = null;
    DefaultExplanationChunkBuilderFactory instance = null;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();

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

        //Initialize the factory instance
        instance = new DefaultExplanationChunkBuilderFactory();

        instance.setI18nManager(im);

    }

    @After
    public void tearDown() throws Exception {
        unit.delete();
        dimensionNames.delete();
        imageCaptions.delete();
    }

    /**
     * Test of setI18nManager method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: unsuccessfull execution - i18nManager is set to null
     */
    @Test
    public void testSetI18nManagerNull() {
        
        try {
            instance.setI18nManager(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The i18nManager cannot be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: unsuccessfull execution - chunk type not recognized
     */
    @Test
    public void testGetExplanationChunkBuilderChunkTypeNotRecognized() {
        int type = -555;
        
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
     * Test case: unsuccessfull execution - i18nManager not initialized
     */
    @Test
    public void testGetExplanationChunkBuilderi18nManagerNotInitialized() {
        instance = new DefaultExplanationChunkBuilderFactory();
        int type = ExplanationChunkBuilderFactory.TEXT;

        try {
            instance.getExplanationChunkBuilder(type);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The i18nManager has not been set";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type
     */
    @Test
    public void testGetExplanationChunkBuilderText1() {
        int type = ExplanationChunkBuilderFactory.TEXT;
        ExplanationChunkBuilder result = instance.getExplanationChunkBuilder(type);

        assertTrue(result instanceof DefaultTextExplanationChunkBuilder);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution TEXT chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetExplanationChunkBuilderText2() {
        int type = ExplanationChunkBuilderFactory.TEXT;

        ExplanationChunkBuilder result1 = instance.getExplanationChunkBuilder(type);
        ExplanationChunkBuilder result2 = instance.getExplanationChunkBuilder(type);

        assertTrue(result1 instanceof DefaultTextExplanationChunkBuilder);
        assertTrue(result2 instanceof DefaultTextExplanationChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type
     */
    @Test
    public void testGetExplanationChunkBuilderImage1() {
        int type = ExplanationChunkBuilderFactory.IMAGE;

        ExplanationChunkBuilder result = instance.getExplanationChunkBuilder(type);

        assertTrue(result instanceof DefaultImageExplanationChunkBuilder);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution IMAGE chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetExplanationChunkBuilderImage2() {
        int type = ExplanationChunkBuilderFactory.IMAGE;

        ExplanationChunkBuilder result1 = instance.getExplanationChunkBuilder(type);
        ExplanationChunkBuilder result2 = instance.getExplanationChunkBuilder(type);

        assertTrue(result1 instanceof DefaultImageExplanationChunkBuilder);
        assertTrue(result2 instanceof DefaultImageExplanationChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type
     */
    @Test
    public void testGetExplanationChunkBuilderData1() {
        int type = ExplanationChunkBuilderFactory.DATA;

        ExplanationChunkBuilder result = instance.getExplanationChunkBuilder(type);

        assertTrue(result instanceof DefaultDataExplanationChunkBuilder);
    }

    /**
     * Test of getExplanationChunkBuilder method, of class DefaultExplanationChunkBuilderFactory.
     * Test case: successfull execution DATA chunk type, assert returning
     * of same instance every time
     */
    @Test
    public void testGetExplanationChunkBuilderData2() {
        int type = ExplanationChunkBuilderFactory.DATA;

        ExplanationChunkBuilder result1 = instance.getExplanationChunkBuilder(type);
        ExplanationChunkBuilder result2 = instance.getExplanationChunkBuilder(type);

        assertTrue(result1 instanceof DefaultDataExplanationChunkBuilder);
        assertTrue(result2 instanceof DefaultDataExplanationChunkBuilder);

        //Assert that the same builder instance is returned every time
        assertEquals(result1, result2);
    }

}
