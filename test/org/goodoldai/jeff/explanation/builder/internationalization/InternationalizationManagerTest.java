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
package org.goodoldai.jeff.explanation.builder.internationalization;

import java.util.Locale;
import junit.framework.TestCase;
import java.io.*;

/**
 *
 * @author Bojan Tomic
 */
public class InternationalizationManagerTest extends TestCase {

    File unit = null;
    File dimensionNames = null;
    File imageCaptions = null;
    File text = null;
    File textgroup = null;

    @Override
    protected void setUp() throws Exception {
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
        dimnamespw.close();

        //Create a sample image captions file with some sample data
        imageCaptions = new File("test" + File.separator + "image_captions_srb_RS.properties");
        PrintWriter imagecaptpw = new PrintWriter(new FileWriter(imageCaptions));
        imagecaptpw.println("Whale\\ photo = Fotografija kita");
        imagecaptpw.println("Image\\ 1 = Slika 1");
        imagecaptpw.close();

        //Create two sample text translation files with some sample data
        text = new File("test" + File.separator + "text_srb_RS.properties");
        PrintWriter textpw = new PrintWriter(new FileWriter(text));
        textpw.println("rule1 = donet je zakljucak 1");
        textpw.println("rule\\ 2 = donet je zakljucak 2");
        textpw.close();

        textgroup = new File("test" + File.separator + "group_1_text_srb_RS.properties");
        PrintWriter textgrouppw = new PrintWriter(new FileWriter(textgroup));
        textgrouppw.println("rule3 = donet je zakljucak {1} u {0}");
        textgrouppw.println("rule\\ 4 = donet je zakljucak 4");
        textgrouppw.close();
    }

    @Override
    protected void tearDown() throws Exception {
        unit.delete();
        dimensionNames.delete();
        imageCaptions.delete();
        text.delete();
        textgroup.delete();
    }

    /**
     * Test of initializeManager method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - null Locale
     */
    public void testInitializeManagerNullLocale() {
        Locale locale = null;
        try {
            InternationalizationManager.initializeManager(locale);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered locale cannot be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of initializeManager method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - properties files do not exist
     */
    public void testInitializeManagerNoPropertyFiles() {
        Locale locale = new Locale("eng", "GB");
        try {
            InternationalizationManager.initializeManager(locale);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Can't find bundle for base name units, locale eng_GB";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of initializeManager method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - properties files do not exist
     */
    public void testInitializeManagerNoPropertyFiles2() {
        Locale locale = new Locale("eng", "US");
        File unit2 = new File("test" + File.separator + "units_eng_US.properties");
        try {
            PrintWriter unitpw = new PrintWriter(new FileWriter(unit2));
            unitpw.println("EUR = USD");
            unitpw.close();

            InternationalizationManager.initializeManager(locale);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            unit2.delete();
            String result = e.getMessage();
            String expResult = "Can't find bundle for base name dimension_names, locale eng_US";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of getManager method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - manager not initialized
     * NOTE:IT IS IMPORTANT TO RUN THIS TEST CASE BECAUSE OF SIDE EFFECTS.
     * ONCE INTIALIZED, NO NEW MANAGER INSTANCES CAN BE CREATED.
     */
    public void testGetManagerNotInitialized() {
        try {
            InternationalizationManager.getManager();
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization manager has not yet been initialized";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of initializeManager method, of class InternationalizationManager.
     * Test case: successfull execution
     */
    public void testInitializeManagerSuccessFull() {
        Locale locale = new Locale("srb", "RS");

        InternationalizationManager.initializeManager(locale);

        InternationalizationManager im = InternationalizationManager.getManager();

        //Check that manager was initialized
        assertTrue(im != null);

        //Try a unit translation
        assertEquals("RSD", im.translateUnit("EUR"));

        //Try dimension name translations
        assertEquals("razdaljina", im.translateDimensionName("distance"));
        assertEquals("novac", im.translateDimensionName("money"));
        assertEquals("dobit (profit)", im.translateDimensionName("profit"));

        //Try image caption translations
        assertEquals("Fotografija kita", im.translateImageCaption("Whale photo"));
        assertEquals("Slika 1", im.translateImageCaption("Image 1"));

    }

    /**
     * Test of initializeManager method, of class InternationalizationManager.
     * Test case: repeated initialization - should have no effect
     */
    public void testInitializeManagerRepeatedInitialization() {
        //Try to initialize the manager that is already initialized.
        //Since initialization is performed only once (Singleton),
        //it should have no effect.
        Locale locale = new Locale("eng", "US");
        InternationalizationManager.initializeManager(locale);
        InternationalizationManager im = InternationalizationManager.getManager();

        //Check that manager was initialized
        assertTrue(im != null);

        //Try a unit translation but with the old locale
        assertEquals("RSD", im.translateUnit("EUR"));

        //Try dimension name translations but with the old locale
        assertEquals("razdaljina", im.translateDimensionName("distance"));
        assertEquals("novac", im.translateDimensionName("money"));
        assertEquals("dobit (profit)", im.translateDimensionName("profit"));

        //Try image caption translations but with the old locale
        assertEquals("Fotografija kita", im.translateImageCaption("Whale photo"));
        assertEquals("Slika 1", im.translateImageCaption("Image 1"));

    }

    /**
     * Test of getManager method, of class InternationalizationManager.
     * Test case: successfull execution - always returns same manager instance
     */
    public void testGetManagerSuccessfull() {
        InternationalizationManager im = InternationalizationManager.getManager();

        InternationalizationManager im2 = InternationalizationManager.getManager();

        //Assert that the same instance is always returned
        assertEquals(im, im2);
    }

    /**
     * Test of translateUnit method, of class InternationalizationManager.
     * Test case: successfull execution
     */
    public void testTranslateUnit() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateUnit("EUR");
        String expResult = "RSD";

        //Try a unit translation
        assertEquals(expResult, result);
    }

    /**
     * Test of translateUnit method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - no translation could be found
     */
    public void testTranslateUnitNoTranslation() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateUnit("DOLLAR");
        String expResult = null;

        //Try a unit translation
        assertEquals(expResult, result);
    }

    /**
     * Test of translateDimensionName method, of class InternationalizationManager.
     * Test case: successfull execution
     */
    public void testTranslateDimensionName() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateDimensionName("money");
        String expResult = "novac";

        //Try a dimension name translation
        assertEquals(expResult, result);
    }

    /**
     * Test of translateDimensionName method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - no translation could be found
     */
    public void testTranslateDimensionNameNoTranslation() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateDimensionName("weight");
        String expResult = null;

        //Try a dimension name translation
        assertEquals(expResult, result);
    }

    /**
     * Test of translateImageCaption method, of class InternationalizationManager.
     * Test case: successfull execution
     */
    public void testTranslateImageCaption() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateImageCaption("Whale photo");
        String expResult = "Fotografija kita";

        //Try an image caption translation
        assertEquals(expResult, result);
    }

    /**
     * Test of translateImageCaption method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - no translation could be found
     */
    public void testTranslateImageCaptionNotranslation() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateImageCaption("Flower photo");
        String expResult = null;

        //Try an image caption translation
        assertEquals(expResult, result);
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - rule identifier is null
     */
    public void testTranslateTextRuleIdentifierNull() {
        try {
            InternationalizationManager im = InternationalizationManager.getManager();
            im.translateText(null, null, null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a non-null, non-empty rule identifier";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - rule identifier is an empty string
     */
    public void testTranslateTextRuleIdentifierEmptyString() {
        try {
            InternationalizationManager im = InternationalizationManager.getManager();
            im.translateText(null, "", null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "You must enter a non-null, non-empty rule identifier";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - rule group properties file doesn't exist
     */
    public void testTranslateTextGroupPropertiesFileDoesNotExist() {
        try {
            InternationalizationManager im = InternationalizationManager.getManager();
            im.translateText("Group234", "rule 1", null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Can't find bundle for base name Group234_text, locale srb_RS";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: unsuccessfull execution - rule identifier doesn't exist
     */
    public void testTranslateTextRuleIdentifierDoesNotExist() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateText(null, "rule 1234", null);
        String expResult = null;

        //Assert that the return value is null
        assertEquals(expResult, result);
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: successfull execution - translation when the group name is null
     */
    public void testTranslateTextSuccessfull() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateText(null, "rule 2", null);
        String expResult = "donet je zakljucak 2";

        assertEquals(expResult, result);
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: successfull execution - translation when the group name is
     * an empty string
     */
    public void testTranslateTextSuccessfull2() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateText("", "rule1", null);
        String expResult = "donet je zakljucak 1";

        assertEquals(expResult, result);
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: successfull execution - translation when the group name is present
     */
    public void testTranslateTextSuccessfull3() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateText("group 1", "rule 4", null);
        String expResult = "donet je zakljucak 4";

        assertEquals(expResult, result);
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: successfull execution - translation when the group name is
     * present but has white spaces
     */
    public void testTranslateTextSuccessfull4() {
        InternationalizationManager im = InternationalizationManager.getManager();

        String result = im.translateText("  group 1  ", "rule 4", null);
        String expResult = "donet je zakljucak 4";

        assertEquals(expResult, result);
    }

    /**
     * Test of translateText method, of class InternationalizationManager.
     * Test case: successfull execution - translation when the group name is
     * present, and arguments need to be inserted into the translation.
     */
    public void testTranslateTextSuccessfull5() {
        InternationalizationManager im = InternationalizationManager.getManager();

        Object[] arguments = {"poslednjem trenutku", new Integer(3)};
        String result = im.translateText("group 1", "rule3", arguments);
        String expResult = "donet je zakljucak 3 u poslednjem trenutku";

        assertEquals(expResult, result);
    }
}
