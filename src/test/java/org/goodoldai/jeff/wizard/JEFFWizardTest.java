/*
 * Copyright 2009 Boris Horvat & Nemanja Jovanovic
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
package org.goodoldai.jeff.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import junit.framework.TestCase;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.builder.DefaultExplanationBuilder;
import org.goodoldai.jeff.explanation.builder.SimpleExplanationBuilder;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 * @author Boris Horvat & Nemanja Jovanovic
 */
public class JEFFWizardTest extends AbstractJeffTest {

    private JEFFWizard defaultWizard;
    private JEFFWizard simpleWizard;
    private JEFFWizard wizardConst;
    //explanation chunk parameters
    private String owner = "tester";
    private String rule = "rule1";
    private String group = "test group";
    private String[] tags = {"tag1", "tag2"};
    private String language = "srb";
    private String country = "RS";
    private String title = "Explanation title";
    //explanation chunks data
    private String url = "/WHALE.JPG";
    private String caption = "Image 1";
    private String name = "money";
    private String unit = "EUR";
    private String dataValue = "valueTest";
    //explanation chunks content
    private String textContent = "test textContent";
    Object[] tcontent = null;
    int tcontext = 0;
    String tgroup = null;
    String trule1 = null;
    String trule2 = null;
    String[] ttags = null;
    private Dimension dimensions;
    private SingleData dataContent;
    private ImageData imageContent;
    //propertie files
    private File fileUnit;
    private File fileDimensionNames;
    private File fileImageCaptions;
    private File fileText;
    private File fileTextGroup;
    private File fileUnitII;
    private File fileDimensionNamesII;
    private File fileImageCaptionsII;
    private File fileTextII;
    private File fileTextGroupII;
    //report files
    private String filePathTXT = "report.txt";
    private String filePathXML = "report.xml";
    private String filePathPDF = "report.pdf";
    private String filePathJSON = "report.json";

    @Before
    public void setUp() throws Exception {
        super.setUp();

        //Create some sample TextExplanationChunk data
        tcontext = ExplanationChunk.ERROR;
        tgroup = "tgroup 1";
        trule1 = "rule1";
        trule2 = "rule3";
        ttags = new String[2];
        ttags[0] = "tagt1";
        ttags[1] = "tagt2";

        tcontent = new Object[2];
        tcontent[0] = "poslednjem trenutku";
        tcontent[1] = new Integer(3);

        simpleWizard = new JEFFWizard();
        defaultWizard = new JEFFWizard(owner, language, country, title, true);

        //explanation chunks content
        dimensions = new Dimension(name, unit);
        imageContent = new ImageData(url, caption);
        dataContent = new SingleData(dimensions, dataValue);

        //creates propertie files for srb RS locale
        fileUnit = newFile("units_srb_RS.properties");
        PrintWriter unitpw = new PrintWriter(new FileWriter(fileUnit));
        unitpw.println("EUR = RSD");
        unitpw.close();

        fileDimensionNames = newFile("dimension_names_srb_RS.properties");
        PrintWriter dimnamespw = new PrintWriter(new FileWriter(fileDimensionNames));
        dimnamespw.println("distance = razdaljina");
        dimnamespw.println("money = novac");
        dimnamespw.println("profit = dobit (profit)");
        dimnamespw.println("gender = pol");
        dimnamespw.close();

        fileImageCaptions = newFile("image_captions_srb_RS.properties");
        PrintWriter imagecaptpw = new PrintWriter(new FileWriter(fileImageCaptions));
        imagecaptpw.println("Explanation\\ title = Naslov objasnjenja");
        imagecaptpw.println("Whale\\ photo = Fotografija kita");
        imagecaptpw.println("Image\\ 1 = Slika 1");
        imagecaptpw.close();

        fileText = newFile("text_srb_RS.properties");
        PrintWriter textpw = new PrintWriter(new FileWriter(fileText));
        textpw.println("rule1 = donet je zakljucak 1");
        textpw.println("rule\\ 2 = donet je zakljucak 2");
        textpw.close();

        fileTextGroup = newFile("tgroup_1_text_srb_RS.properties");
        PrintWriter textgrouppw = new PrintWriter(new FileWriter(fileTextGroup));
        textgrouppw.println("rule3 = donet je zakljucak {1} u {0}");
        textgrouppw.println("trule2\\ 4 = donet je zakljucak 4");
        textgrouppw.close();

        //creates propertie files for en US locale
        fileUnitII = newFile("units_en_US.properties");
        unitpw = new PrintWriter(new FileWriter(fileUnitII));
        unitpw.println("EUR = USD");
        unitpw.close();

        fileDimensionNamesII = newFile("dimension_names_en_US.properties");
        dimnamespw = new PrintWriter(new FileWriter(fileDimensionNamesII));
        dimnamespw.println("distance = distance");
        dimnamespw.println("money = money");
        dimnamespw.println("profit = profit");
        dimnamespw.println("gender = gender");
        dimnamespw.close();

        fileImageCaptionsII = newFile("image_captions_en_US.properties");
        imagecaptpw = new PrintWriter(new FileWriter(fileImageCaptionsII));
        imagecaptpw.println("Explanation\\ title = Explanation title");
        imagecaptpw.println("Whale\\ photo = Whale photo");
        imagecaptpw.println("Image\\ 1 = Image 1");
        imagecaptpw.close();

        fileTextII = newFile("text_en_US.properties");
        textpw = new PrintWriter(new FileWriter(fileTextII));
        textpw.println("rule1 = inference 1 has been made");
        textpw.println("rule\\ 2 = inference 2 has been made");
        textpw.close();

        fileTextGroupII = newFile("tgroup_1_text_en_US.properties");
        textgrouppw = new PrintWriter(new FileWriter(fileTextGroupII));
        textgrouppw.println("rule3 = inference {1} in {0} has been made");
        textgrouppw.println("trule2\\ 4 = inference 4 has been made");
        textgrouppw.close();

    }

    @After
    public void tearDown() {

        //deleteing propertie
        fileUnit.delete();
        fileDimensionNames.delete();
        fileImageCaptions.delete();
        fileText.delete();
        fileTextGroup.delete();

        fileUnitII.delete();
        fileDimensionNamesII.delete();
        fileImageCaptionsII.delete();
        fileTextII.delete();
        fileTextGroupII.delete();

        //deleteing report files
        new File(filePathTXT).delete();
        new File(filePathXML).delete();
        new File(filePathPDF).delete();
        new File(filePathJSON).delete();
    }

    /**
     * Test of no argument constructor, of class JEFFWizard.
     * Test case: successfull initialization.
     */
    @Test
    public void testConstructorNoArguments() {

        //creates object using default constructor
        wizardConst = new JEFFWizard();

        //checks the default values
        assertEquals(null, wizardConst.getOwner());
        assertEquals(null, wizardConst.getCountry());
        assertEquals(null, wizardConst.getLanguage());
        assertEquals(null, wizardConst.getTitle());

        assertEquals(false, wizardConst.isInternationalization());
    }

    /**
     * Test of one argument constructor, of class JEFFWizard.
     * Test case: successfull initialization.
     */
    @Test
    public void testConstructorOneArgument() {

        //creates object using constructor
        wizardConst = new JEFFWizard(owner);

        //checks the default values as well as inserted owner value
        assertEquals(owner, wizardConst.getOwner());
        assertEquals(null, wizardConst.getCountry());
        assertEquals(null, wizardConst.getLanguage());
        assertEquals(null, wizardConst.getTitle());

        assertEquals(false, wizardConst.isInternationalization());
    }

    /**
     * Test of two argument constructor, of class JEFFWizard.
     * Test case: successfull initialization.
     */
    @Test
    public void testConstructorTwoArguments() {

       //creates the objcet using constructor with two arguments
        wizardConst = new JEFFWizard(owner, title);

        //checks the default values
        //checks the inserted owner and internationalization values
        assertEquals(owner, wizardConst.getOwner());
        assertEquals(null, wizardConst.getCountry());
        assertEquals(null, wizardConst.getLanguage());
        assertEquals(title, wizardConst.getTitle());

        assertEquals(false, wizardConst.isInternationalization());
    }

    /**
     * Test of three argument constructor, of class JEFFWizard.
     * Test case: successfull initialization.
     */
    @Test
    public void testConstructorThreeArguments() {

        boolean internationalization = true;

        //creates the objcet using constructor with three arguments
        wizardConst = new JEFFWizard(owner, title, internationalization);

        //checks the default values
        //checks the inserted owne, title and internationalization values
        assertEquals(owner, wizardConst.getOwner());
        assertEquals(null, wizardConst.getCountry());
        assertEquals(null, wizardConst.getLanguage());
        assertEquals(title, wizardConst.getTitle());

        assertEquals(internationalization, wizardConst.isInternationalization());
    }

    /**
     * Test of all argument constructor, of class JEFFWizard.
     * Test case: successfull initialization.
     */
    @Test
    public void testConstructorAllArguments() {

        boolean internationalization = true;

        //creates the object using the constructor with all values
        wizardConst = new JEFFWizard(owner, language, country, title, internationalization);

        // tests the inserted values for all parametars
        assertEquals(owner, wizardConst.getOwner());
        assertEquals(country, wizardConst.getCountry());
        assertEquals(language, wizardConst.getLanguage());
        assertEquals(title, wizardConst.getTitle());

        assertEquals(internationalization, wizardConst.isInternationalization());
    }

    /**
     * Test of setOwner method, of class JEFFWizard.
     * Test case:  successfull change of owner name.
     */
    @Test
    public void testsetOwnerSuccess() {

        simpleWizard.setOwner(owner);

        assertEquals(owner, simpleWizard.getOwner());
    }

    /**
     * Test of setOwner method, of class JEFFWizard.
     * Test case: successfull change of owner name with null value.
     */
    @Test
    public void testsetOwnerSuccessNull() {

        String ownerNull = null;
        simpleWizard.setOwner(ownerNull);

        assertEquals(ownerNull, simpleWizard.getOwner());
    }

    /**
     * Test of setOwner method, of class JEFFWizard.
     * Test case: unsuccessful change of owner name - the building process has begun.
     */
    @Test
    public void testSetOwnerBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setOwner("Tester");
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
    
    /**
     * Test of setOwner method, of class JEFFWizard.
     * Test case: unsuccessful change of owner name - the building process has begun.
     */
    @Test
    public void testSetOwnerNullBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setOwner(null);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setTitle method, of class JEFFWizard.
     * Test case:  successfull change of explanation title.
     */
    @Test
    public void testsetTitleSuccess() {

        simpleWizard.setTitle(title);

        assertEquals(title, simpleWizard.getTitle());
    }

    /**
     * Test of setTitle method, of class JEFFWizard.
     * Test case:  successfull change of explanation title with null value.
     */
    @Test
    public void testsetTitleSuccessNull() {

        simpleWizard.setTitle(null);

        assertEquals(null, simpleWizard.getTitle());
    }

    /**
     * Test of setTitle method, of class JEFFWizard.
     * Test case: unsuccessful change of explanation title - the building
     * process has begun.
     */
    @Test
    public void testSetTitleBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setTitle(title);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setTitle method, of class JEFFWizard.
     * Test case: unsuccessful change of explanation title - the building
     * process has begun.
     */
    @Test
    public void testSetTitleNullBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setTitle(null);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setLanguage method, of class JEFFWizard.
     * Test case: successfull change of language.
     */
    @Test
    public void testSetLanguageSuccess() {

        simpleWizard.setLanguage(language);

        assertEquals(language, simpleWizard.getLanguage());
    }

    /**
     * Test of setLanguage method, of class JEFFWizard.
     * Test case: successfull change of language with null value.
     */
    @Test
    public void testSetLanguageSuccessNull() {

        String languageNull = null;
        simpleWizard.setLanguage(languageNull);

        assertEquals(languageNull, simpleWizard.getLanguage());
    }

    /**
     * Test of setLanguage method, of class JEFFWizard.
     * Test case: unsuccessful change of language - the building process has begun.
     */
    @Test
    public void testSetLanguageBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setLanguage("eng");
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setLanguage method, of class JEFFWizard.
     * Test case: unsuccessful change of language - the building process has begun.
     */
    @Test
    public void testSetLanguageNullBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setLanguage(null);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setCountry method, of class JEFFWizard.
     * Test case: successfull change of country.
     */
    @Test
    public void testSetCountrySuccess() {

        String countryUS = "US";
        simpleWizard.setCountry(countryUS);

        assertEquals(countryUS, simpleWizard.getCountry());
    }

    /**
     * Test of setCountry method, of class JEFFWizard.
     * Test case: successfull change of country with null value.
     */
    @Test
    public void testSetCountrySuccessNull() {

        String countryNull = null;
        simpleWizard.setCountry(countryNull);

        assertEquals(countryNull, simpleWizard.getCountry());
    }

    /**
     * Test of setCountry method, of class JEFFWizard.
     * Test case: unsuccessful change of country - the building process has begun.
     */
    @Test
    public void testSetCountryBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setCountry("US");
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setCountry method, of class JEFFWizard.
     * Test case: unsuccessful change of country - the building process has begun.
     */
    @Test
    public void testSetCountryNullBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setCountry(null);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of setInternationalization method, of class JEFFWizard.
     * Test case: successfull change when paramether of internationalization is true.
     */
    @Test
    public void testSetInternationalizationTrue() {

        boolean internationalization = true;
        simpleWizard.setInternationalization(internationalization);

        assertEquals(internationalization, simpleWizard.isInternationalization());
    }

    /**
     * Test of setInternationalization method, of class JEFFWizard.
     * Test case: successfull change when paramether of internationalization is false.
     */
    @Test
    public void testSetInternationalizationFalse() {

        boolean internationalization = false;
        simpleWizard.setInternationalization(internationalization);

        assertEquals(internationalization, simpleWizard.isInternationalization());
    }

    /**
     * Test of setInternationalization method, of class JEFFWizard.
     * Test case: unsuccessful change of internationalization - the building process has begun.
     */
    @Test
    public void testSetInternationalizationBuildingProcessStarted() {
        try {
            simpleWizard.createExplanation();

            simpleWizard.setInternationalization(true);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The argument can not be set beacuse the explanation building process has begun";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of createExplanation method, of class JEFFWizard.
     * Test case: successful creation of an explanation using null values.
     */
    @Test
    public void testCreateExplanationSuccesful_NullValues() {

        //creates object using default constructor
        simpleWizard = new JEFFWizard();

        //starting the building process
        simpleWizard.createExplanation();

        //testing the builders
        assertTrue(simpleWizard.builder != null);
        assertTrue(simpleWizard.builder instanceof SimpleExplanationBuilder);

        //testing the explanation parameters
        assertEquals(null, simpleWizard.getExplanation().getOwner());
        assertEquals(null, simpleWizard.getExplanation().getCountry());
        assertEquals(null, simpleWizard.getExplanation().getLanguage());
        assertEquals(null, simpleWizard.getExplanation().getTitle());
    }

    /**
     * Test of createExplanation method, of class JEFFWizard.
     * Test case: successful creation of an explanation with an owner value.
     */
    @Test
    public void testCreateExplanationSuccesful_OnlyOwner() {


        //creates object using default constructor
        simpleWizard = new JEFFWizard(owner);

        //starting the building process
        simpleWizard.createExplanation();

        //testing the builders
        assertTrue(simpleWizard.builder != null);
        assertTrue(simpleWizard.builder instanceof SimpleExplanationBuilder);

        //testing the explanation parameters
        assertEquals(owner, simpleWizard.getExplanation().getOwner());
        assertEquals(null, simpleWizard.getExplanation().getCountry());
        assertEquals(null, simpleWizard.getExplanation().getLanguage());
        assertEquals(null, simpleWizard.getExplanation().getTitle());
    }

    /**
     * Test of createExplanation method, of class JEFFWizard.
     * Test case: successful creation of an explanation with only owner, and
     * internationalization is true.
     */
    @Test
    public void testCreateExplanationSuccesful_OwnerAndInternationalizationTrue() throws IOException {
        //creates object using default constructor
        wizardConst = new JEFFWizard(owner, title, true);

        wizardConst.setCountry("US");
        wizardConst.setLanguage("en");

        //starting the building process
        wizardConst.createExplanation();

        //testing the builders
        assertTrue(wizardConst.builder != null);
        assertTrue(wizardConst.builder instanceof DefaultExplanationBuilder);

        //testing the explanation parameters
        assertEquals(owner, wizardConst.getExplanation().getOwner());
        assertEquals("US", wizardConst.getExplanation().getCountry());
        assertEquals("en", wizardConst.getExplanation().getLanguage());
        assertEquals(title, wizardConst.getExplanation().getTitle());
        
    }

    /**
     * Test of createExplanation method, of class JEFFWizard.
     * Test case: successful creation of an explanation with only owner, and
     * internationalization is false.
     */
    @Test
    public void testCreateExplanationSuccesful_OwnerAndInternationalizationFalse() throws IOException {

        //creates object using default constructor
        wizardConst = new JEFFWizard(owner, title, false);

        //starting the building process
        wizardConst.createExplanation();

        //testing the builders
        assertTrue(wizardConst.builder != null);
        assertTrue(wizardConst.builder instanceof SimpleExplanationBuilder);

        //testing the explanation parameters
        assertEquals(owner, wizardConst.getExplanation().getOwner());
        assertEquals(null, wizardConst.getExplanation().getCountry());
        assertEquals(null, wizardConst.getExplanation().getLanguage());
        assertEquals(title, wizardConst.getExplanation().getTitle());
    }

    /**
     * Test of createExplanation method, of class JEFFWizard.
     * Test case: successful creation of an explanation with all arguments, and
     * internationalization is true.
     */
    @Test
    public void testCreateExplanationSuccesful_AllArgumentsInterntaionalizationTrue() throws IOException {

        //creates object using default constructor
        wizardConst = new JEFFWizard(owner, language, country, title, true);

        //starting the building process
        wizardConst.createExplanation();

        //testing the builders
        assertTrue(wizardConst.builder != null);
        assertTrue(wizardConst.builder instanceof DefaultExplanationBuilder);

        //testing the explanation parameters
        assertEquals(owner, wizardConst.getExplanation().getOwner());
        assertEquals(country, wizardConst.getExplanation().getCountry());
        assertEquals(language, wizardConst.getExplanation().getLanguage());
        assertEquals("Naslov objasnjenja",wizardConst.getExplanation().getTitle());
    }

    /**
     * Test of createExplanation method, of class JEFFWizard.
     * Test case: successful creation of an explanation with all arguments, and
     * internationalization is false.
     */
    @Test
    public void testCreateExplanationSuccesful_AllArgumentsInterntaionalizationFalse() {

        //creates object using default constructor
        wizardConst = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        wizardConst.createExplanation();

        //testing the explanation parameters
        assertEquals(owner, wizardConst.getExplanation().getOwner());
        assertEquals(country, wizardConst.getExplanation().getCountry());
        assertEquals(language, wizardConst.getExplanation().getLanguage());
        assertEquals(title,wizardConst.getExplanation().getTitle());
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddText_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addText(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddText_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addText(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddText_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addText(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddText_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addText(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddTextError_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addTextError(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddTextError_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addTextError(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddTextError_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addTextError(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddTextError_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addTextError(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddTextStrategic_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addTextStrategic(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddTextStrategic_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addTextStrategic(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddTextStrategic_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addTextStrategic(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddTextStrategic_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addTextStrategic(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddTextWarning_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addTextWarning(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddTextWarning_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addTextWarning(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddTextWarning_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addTextWarning(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddTextWarning_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addTextWarning(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddTextPositive_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addTextPositive(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddTextPositive_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addTextPositive(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddTextPositive_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addTextPositive(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddTextPositive_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addTextPositive(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddTextNegative_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addTextNegative(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddTextNegative_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addTextNegative(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddTextNegative_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addTextNegative(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddTextNegative_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addTextNegative(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddTextVeryPositive_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryPositive(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddTextVeryPositive_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryPositive(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddTextVeryPositive_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryPositive(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddTextVeryPositive_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryPositive(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddTextVeryNegative_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryPositive(textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddTextVeryNegative_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryNegative(rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddTextVeryNegative_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryNegative(group, rule, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the text explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddTextVeryNegative_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addTextVeryNegative(group, rule, tags, textContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addText(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.INFORMATIONAL, tc.getContext());
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addText(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.INFORMATIONAL, tc.getContext());
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addText(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.INFORMATIONAL, tc.getContext());
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addText(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.INFORMATIONAL, tc.getContext());
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.ERROR, tc.getContext());
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.ERROR, tc.getContext());
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.ERROR, tc.getContext());
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.ERROR, tc.getContext());
    }

/**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.STRATEGIC
     */
    @Test
    public void testAddTextStrategic_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextStrategic(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.STRATEGIC, tc.getContext());
    }

    /**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.STRATEGIC
     */
    @Test
    public void testAddTextStrategic_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextStrategic(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.STRATEGIC, tc.getContext());
    }

    /**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.STRATEGIC
     */
    @Test
    public void testAddTextStrategic_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextStrategic(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.STRATEGIC, tc.getContext());
    }

    /**
     * Test of addTextStrategic method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.STRATEGIC
     */
    @Test
    public void testAddTextStrategic_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextStrategic(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.STRATEGIC, tc.getContext());
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextWarning(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.WARNING, tc.getContext());
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextWarning(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.WARNING, tc.getContext());
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextWarning(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.WARNING, tc.getContext());
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextWarning(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.WARNING, tc.getContext());
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextPositive(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextPositive(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextPositive(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextPositive(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextNegative(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextNegative(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextNegative(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextNegative(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testaddTextVeryPositive_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryPositive(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testAddTextVeryPositive_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryPositive(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testAddTextVeryPositive_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryPositive(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testAddTextVeryPositive_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryPositive(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testAddTextVeryNegative_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryNegative(textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testAddTextVeryNegative_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryNegative(rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testAddTextVeryNegative_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryNegative(group, rule, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testAddTextVeryNegative_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextVeryNegative(group, rule, tags, textContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, tc.getRule());
        assertEquals(group, tc.getGroup());
        assertEquals(tags[0], tc.getTags()[0]);
        assertEquals(tags[1], tc.getTags()[1]);
        assertEquals(textContent, tc.getContent());
        assertEquals(ExplanationChunk.VERY_NEGATIVE, tc.getContext());
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_Content_DefaultBuilder() {
        try {
            //starting the building process
            defaultWizard.createExplanation();

            //adding explanation chunk
            defaultWizard.addText(tcontent);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization feature can not be used without providing a rule";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and trule2 using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addText(trule1, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule1, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 1", tc.getContent());
        assertEquals(ExplanationChunk.INFORMATIONAL, tc.getContext());
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2 and tgroup using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addText(tgroup, trule2, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.INFORMATIONAL, tc.getContext());
    }

    /**
     * Test of addText method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2, tgroup and ttags using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddText_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addText(tgroup, trule2, ttags, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.INFORMATIONAL, tc.getContext());
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_Content_DefaultBuilder() {
        try {
            //starting the building process
            defaultWizard.createExplanation();

            //adding explanation chunk
            defaultWizard.addTextError(tcontent);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization feature can not be used without providing a rule";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and trule2 using default builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextError(trule1, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule1, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 1", tc.getContent());
        assertEquals(ExplanationChunk.ERROR, tc.getContext());
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2 and tgroup using default builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextError(tgroup, trule2, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.ERROR, tc.getContext());
    }

    /**
     * Test of addTextError method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2, tgroup and ttags using default builder.
     * The context is ExplanationChunk.ERROR
     */
    @Test
    public void testAddTextError_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextError(tgroup, trule2, ttags, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.ERROR, tc.getContext());
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_Content_DefaultBuilder() {
        try {
            //starting the building process
            defaultWizard.createExplanation();

            //adding explanation chunk
            defaultWizard.addTextWarning(tcontent);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization feature can not be used without providing a rule";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and trule2 using default builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextWarning(trule1, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule1, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 1", tc.getContent());
        assertEquals(ExplanationChunk.WARNING, tc.getContext());
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2 and tgroup using default builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextWarning(tgroup, trule2, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.WARNING, tc.getContext());
    }

    /**
     * Test of addTextWarning method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2, tgroup and ttags using default builder.
     * The context is ExplanationChunk.WARNING
     */
    @Test
    public void testAddTextWarning_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextWarning(tgroup, trule2, ttags, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.WARNING, tc.getContext());
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_Content_DefaultBuilder() {
        try {
            //starting the building process
            defaultWizard.createExplanation();

            //adding explanation chunk
            defaultWizard.addTextPositive(tcontent);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization feature can not be used without providing a rule";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and trule2 using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextPositive(trule1, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule1, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 1", tc.getContent());
        assertEquals(ExplanationChunk.POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2 and tgroup using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextPositive(tgroup, trule2, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2, tgroup and ttags using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddTextPositive_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextPositive(tgroup, trule2, ttags, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_Content_DefaultBuilder() {
        try {
            //starting the building process
            defaultWizard.createExplanation();

            //adding explanation chunk
            defaultWizard.addTextNegative(tcontent);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization feature can not be used without providing a rule";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and trule2 using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextNegative(trule1, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule1, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 1", tc.getContent());
        assertEquals(ExplanationChunk.NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2 and tgroup using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextNegative(tgroup, trule2, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2, tgroup and ttags using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddTextNegative_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextNegative(tgroup, trule2, ttags, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testaddTextVeryPositive_Content_DefaultBuilder() {
        try {
            //starting the building process
            defaultWizard.createExplanation();

            //adding explanation chunk
            defaultWizard.addTextVeryPositive(tcontent);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization feature can not be used without providing a rule";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and trule2 using default builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testAddTextVeryPositive_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextVeryPositive(trule1, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule1, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 1", tc.getContent());
        assertEquals(ExplanationChunk.VERY_POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2 and tgroup using default builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testAddTextVeryPositive_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextVeryPositive(tgroup, trule2, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.VERY_POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryPositive method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2, tgroup and ttags using default builder.
     * The context is ExplanationChunk.VERY_POSITIVE
     */
    @Test
    public void testAddTextVeryPositive_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextVeryPositive(tgroup, trule2, ttags, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.VERY_POSITIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testaddTextVeryNegative_Content_DefaultBuilder() {
        try {
            //starting the building process
            defaultWizard.createExplanation();

            //adding explanation chunk
            defaultWizard.addTextVeryNegative(tcontent);
            fail("Exception should have been thrown, but it wasn't");

        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The internationalization feature can not be used without providing a rule";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content and trule2 using default builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testAddTextVeryNegative_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextVeryNegative(trule1, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule1, tc.getRule());
        assertEquals(null, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 1", tc.getContent());
        assertEquals(ExplanationChunk.VERY_NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2 and tgroup using default builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testAddTextVeryNegative_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextVeryNegative(tgroup, trule2, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(null, tc.getTags());
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.VERY_NEGATIVE, tc.getContext());
    }

    /**
     * Test of addTextVeryNegative method, of class JEFFWizard.
     * Test case: successful adding of the text explanation chunk
     * with provided content, trule2, tgroup and ttags using default builder.
     * The context is ExplanationChunk.VERY_NEGATIVE
     */
    @Test
    public void testAddTextVeryNegative_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addTextVeryNegative(tgroup, trule2, ttags, tcontent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof TextExplanationChunk);

        TextExplanationChunk tc =
                             (TextExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(trule2, tc.getRule());
        assertEquals(tgroup, tc.getGroup());
        assertEquals(ttags[0], tc.getTags()[0]);
        assertEquals(ttags[1], tc.getTags()[1]);
        assertEquals("donet je zakljucak 3 u poslednjem trenutku", tc.getContent());
        assertEquals(ExplanationChunk.VERY_NEGATIVE, tc.getContext());
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: unsuccessful adding of the image explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddImage_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addImage(imageContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: unsuccessful adding of the image explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddImage_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addImage(rule, imageContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: unsuccessful adding of the image explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddImage_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addImage(group, rule, imageContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: unsuccessful adding of the image explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddImage_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addImage(group, rule, tags, imageContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addImage(imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, ic.getRule());
        assertEquals(null, ic.getGroup());
        assertEquals(null, ic.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //test content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals(caption, resultCaption);
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addImage(rule, imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, ic.getRule());
        assertEquals(null, ic.getGroup());
        assertEquals(null, ic.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //testing content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals(caption, resultCaption);
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addImage(group, rule, imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, ic.getRule());
        assertEquals(group, ic.getGroup());
        assertEquals(null, ic.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //testing content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals(caption, resultCaption);
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addImage(group, rule, tags, imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, ic.getRule());
        assertEquals(group, ic.getGroup());
        assertEquals(tags[0], ic.getTags()[0]);
        assertEquals(tags[1], ic.getTags()[1]);
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //testing content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals(caption, resultCaption);
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_Content_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addImage(imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, ic.getRule());
        assertEquals(null, ic.getGroup());
        assertEquals(null, ic.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //test content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals("Slika 1", resultCaption);
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content and rule using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addImage(rule, imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, ic.getRule());
        assertEquals(null, ic.getGroup());
        assertEquals(null, ic.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //testing content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals("Slika 1", resultCaption);
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content, rule and group using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addImage(group, rule, imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, ic.getRule());
        assertEquals(group, ic.getGroup());
        assertEquals(null, ic.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //testing content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals("Slika 1", resultCaption);
    }

    /**
     * Test of addImage method, of class JEFFWizard.
     * Test case: successful adding of the image explanation chunk
     * with provided content, rule, group and tags using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddImage_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addImage(group, rule, tags, imageContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof ImageExplanationChunk);

        ImageExplanationChunk ic =
                              (ImageExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, ic.getRule());
        assertEquals(group, ic.getGroup());
        assertEquals(tags[0], ic.getTags()[0]);
        assertEquals(tags[1], ic.getTags()[1]);
        assertEquals(ExplanationChunk.INFORMATIONAL, ic.getContext());

        //testing content

        ImageData data = (ImageData) ic.getContent();
        String resultUrl = data.getURL();
        String resultCaption = data.getCaption();

        assertEquals(url, resultUrl);
        assertEquals("Slika 1", resultCaption);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddData_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addData(dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddData_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addData(rule, dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddData_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addData(group, rule, dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddData_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addData(group, rule, tags, dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddDataPositive_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addDataPositive(dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddDataPositive_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addDataPositive(rule, dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddDataPositive_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addDataPositive(group, rule, unit);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddDataPositive_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addDataPositive(group, rule, tags, dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content
     */
    @Test
    public void testFailAddDataNegative_Content() {
        try {
            //adding explanation chunk
            simpleWizard.addDataNegative(dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content and rule
     */
    @Test
    public void testFailAddDataNegative_ContentAndRule() {
        try {
            //adding explanation chunk
            simpleWizard.addDataNegative(rule, dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content, rule and group
     */
    @Test
    public void testFailAddDataNegative_ContentRuleAndGroup() {
        try {
            //adding explanation chunk
            simpleWizard.addDataNegative(group, rule, unit);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: unsuccessful adding of the data explanation chunk
     * with provided content, rule, group and tags
     */
    @Test
    public void testFailAddDataNegative_ContentRuleGroupAndTags() {
        try {
            //adding explanation chunk
            simpleWizard.addDataNegative(group, rule, tags, dataContent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The chunk can not be added if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addData(dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addData(rule, dataContent);

//assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addData(group, rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addData(group, rule, tags, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(tags[0], dc.getTags()[0]);
        assertEquals(tags[1], dc.getTags()[1]);
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataPositive(dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataPositive(rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataPositive(group, rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(tags[0], dc.getTags()[0]);
        assertEquals(tags[1], dc.getTags()[1]);
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_Content_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataNegative(dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content and rule using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_ContentAndRule_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataNegative(rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule and group using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_ContentRuleAndGroup_SimpleBuilder() {
        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataNegative(group, rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule, group and tags using simple builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_ContentRuleGroupAndTags_SimpleBuilder() {

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addDataNegative(group, rule, tags, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, simpleWizard.getExplanation().getChunks().size());
        assertTrue(simpleWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (simpleWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(tags[0], dc.getTags()[0]);
        assertEquals(tags[1], dc.getTags()[1]);
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals(name, resultDimensionsName);
        assertEquals(unit, resultDimensionsUnit);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_Content_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addData(dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content and rule using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addData(rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule and group using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addData(group, rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addData method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule, group and tags using default builder.
     * The context is ExplanationChunk.INFORMATIONAL
     */
    @Test
    public void testAddData_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addData(group, rule, tags, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(tags[0], dc.getTags()[0]);
        assertEquals(tags[1], dc.getTags()[1]);
        assertEquals(ExplanationChunk.INFORMATIONAL, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_Content_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataPositive(dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content and rule using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataPositive(rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule and group using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataPositive(group, rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataPositive method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule, group and tags using default builder.
     * The context is ExplanationChunk.POSITIVE
     */
    @Test
    public void testAddDataPositive_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataPositive(group, rule, tags, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(tags[0], dc.getTags()[0]);
        assertEquals(tags[1], dc.getTags()[1]);
        assertEquals(ExplanationChunk.POSITIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_Content_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataNegative(dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(null, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content and rule using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_ContentAndRule_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataNegative(rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(null, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule and group using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_ContentRuleAndGroup_DefaultBuilder() {
        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataNegative(group, rule, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(null, dc.getTags());
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of addDataNegative method, of class JEFFWizard.
     * Test case: successful adding of the data explanation chunk
     * with provided content, rule, group and tags using default builder.
     * The context is ExplanationChunk.NEGATIVE
     */
    @Test
    public void testAddDataNegative_ContentRuleGroupAndTags_DefaultBuilder() {

        //starting the building process
        defaultWizard.createExplanation();

        //adding explanation chunk
        defaultWizard.addDataNegative(group, rule, tags, dataContent);

        //assert that one chunk was added and that it is a TextExplanationChunk
        assertEquals(1, defaultWizard.getExplanation().getChunks().size());
        assertTrue(defaultWizard.getExplanation().getChunks().get(0) instanceof DataExplanationChunk);

        DataExplanationChunk dc =
                             (DataExplanationChunk) (defaultWizard.getExplanation().getChunks().get(0));

        //testing inserted data of the chunk
        assertEquals(rule, dc.getRule());
        assertEquals(group, dc.getGroup());
        assertEquals(tags[0], dc.getTags()[0]);
        assertEquals(tags[1], dc.getTags()[1]);
        assertEquals(ExplanationChunk.NEGATIVE, dc.getContext());

        //test content

        SingleData data = (SingleData) dc.getContent();
        String resultValue = (String) data.getValue();
        Dimension resultDimensions = data.getDimension();

        String resultDimensionsName = resultDimensions.getName();
        String resultDimensionsUnit = resultDimensions.getUnit();

        assertEquals(dataValue, resultValue);
        assertEquals("novac", resultDimensionsName);
        assertEquals("RSD", resultDimensionsUnit);
    }

    /**
     * Test of generateTXTReport method, of class JEFFWizard.
     * Test case: unsuccessful creation of the report
     * because the building process has never started
     */
    @Test
    public void testFailGenerateTXTReport() {
        try {
            simpleWizard.generateTXTReport(filePathTXT, true);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The the report can not be generated if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of generateTXTReport method, of class JEFFWizard.
     * Test case: successful creation of the report
     */
    @Test
    public void testGenerateTXTReport_FilePath() throws FileNotFoundException, IOException {

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateTXTReport(filePathTXT, true);

        //checks if the file is created
        assertTrue(new File(filePathTXT).exists());

        BufferedReader br = new BufferedReader(new FileReader(filePathTXT));

        //checks the heading of the report
        assertEquals("Creation date: " + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: " + owner, br.readLine());
        assertEquals("The language used: " + language, br.readLine());
        assertEquals("The country is: " + country, br.readLine());
        assertEquals("", br.readLine());
        assertEquals(title, br.readLine());
        assertEquals("", br.readLine());

        //checks the first explanation chunk
        assertEquals("The context is: error", br.readLine());
        assertEquals(textContent, br.readLine());
        assertEquals("", br.readLine());

        //checks the second explanation chunk
        assertEquals("The context is: informational", br.readLine());
        assertEquals("The rule that initiated the creation of this chunk: " + rule, br.readLine());
        assertEquals("Caption is: " + caption, br.readLine());
        assertEquals("The path to this image is: " + url, br.readLine());
        assertEquals("", br.readLine());

        //checks the third explanation chunk
        assertEquals("The context is: positive", br.readLine());
        assertEquals("The rule that initiated the creation of this chunk: " + rule, br.readLine());
        assertEquals("The group to which the executed rule belongs: " + group, br.readLine());
        assertEquals("The tags are: " + tags[0] + " " + tags[1] + " ", br.readLine());
        assertEquals(name + " [" + unit + "]", br.readLine());
        assertEquals("-------------------", br.readLine());
        assertEquals(dataValue, br.readLine());
        assertEquals("", br.readLine());

        assertEquals(null, br.readLine());

        br.close();
    }

    /**
     * Test of generateTXTReport method, of class JEFFWizard.
     * Test case: successful creation of the report but with no chunk headers
     */
    @Test
    public void testGenerateTXTReport_FilePathNoHeaders() throws FileNotFoundException, IOException {

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateTXTReport(filePathTXT, false);

        //checks if the file is created
        assertTrue(new File(filePathTXT).exists());

        BufferedReader br = new BufferedReader(new FileReader(filePathTXT));

        //checks the heading of the report
        assertEquals("Creation date: " + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: " + owner, br.readLine());
        assertEquals("The language used: " + language, br.readLine());
        assertEquals("The country is: " + country, br.readLine());
        assertEquals("", br.readLine());
        assertEquals(title, br.readLine());
        assertEquals("", br.readLine());

        //checks the first explanation chunk
        assertEquals(textContent, br.readLine());
        assertEquals("", br.readLine());

        //checks the second explanation chunk
        assertEquals("Caption is: " + caption, br.readLine());
        assertEquals("The path to this image is: " + url, br.readLine());
        assertEquals("", br.readLine());

        //checks the third explanation chunk
        assertEquals(name + " [" + unit + "]", br.readLine());
        assertEquals("-------------------", br.readLine());
        assertEquals(dataValue, br.readLine());
        assertEquals("", br.readLine());

        assertEquals(null, br.readLine());

        br.close();
    }

    /**
     * Test of generateTXTReport method, of class JEFFWizard.
     * Test case: successful creation of the report
     */
    @Test
    public void testGenerateTXTReport_Stream() throws FileNotFoundException, IOException {

        //creates the stream where the report will be sent
        PrintWriter stream = new PrintWriter(new File(filePathTXT));

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateTXTReport(stream, true);

        //close the stream
        stream.close();

        //checks if the file is created
        assertTrue(new File(filePathTXT).exists());

        BufferedReader br = new BufferedReader(new FileReader(filePathTXT));

        //checks the heading of the report
        assertEquals("Creation date: " + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: " + owner, br.readLine());
        assertEquals("The language used: " + language, br.readLine());
        assertEquals("The country is: " + country, br.readLine());
        assertEquals("", br.readLine());
        assertEquals(title, br.readLine());
        assertEquals("", br.readLine());

        //checks the first explanation chunk
        assertEquals("The context is: error", br.readLine());
        assertEquals(textContent, br.readLine());
        assertEquals("", br.readLine());

        //checks the second explanation chunk
        assertEquals("The context is: informational", br.readLine());
        assertEquals("The rule that initiated the creation of this chunk: " + rule, br.readLine());
        assertEquals("Caption is: " + caption, br.readLine());
        assertEquals("The path to this image is: " + url, br.readLine());
        assertEquals("", br.readLine());

        //checks the third explanation chunk
        assertEquals("The context is: positive", br.readLine());
        assertEquals("The rule that initiated the creation of this chunk: " + rule, br.readLine());
        assertEquals("The group to which the executed rule belongs: " + group, br.readLine());
        assertEquals("The tags are: " + tags[0] + " " + tags[1] + " ", br.readLine());
        assertEquals(name + " [" + unit + "]", br.readLine());
        assertEquals("-------------------", br.readLine());
        assertEquals(dataValue, br.readLine());
        assertEquals("", br.readLine());

        assertEquals(null, br.readLine());

        br.close();
    }

    /**
     * Test of generateTXTReport method, of class JEFFWizard.
     * Test case: successful creation of the report but with no chunk headers
     */
    @Test
    public void testGenerateTXTReport_StreamNoChunkHeaders() throws FileNotFoundException, IOException {

        //creates the stream where the report will be sent
        PrintWriter stream = new PrintWriter(new File(filePathTXT));

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateTXTReport(stream, false);

        //close the stream
        stream.close();

        //checks if the file is created
        assertTrue(new File(filePathTXT).exists());

        BufferedReader br = new BufferedReader(new FileReader(filePathTXT));

        //checks the heading of the report
        assertEquals("Creation date: " + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), br.readLine());
        assertEquals("Report owner is: " + owner, br.readLine());
        assertEquals("The language used: " + language, br.readLine());
        assertEquals("The country is: " + country, br.readLine());
        assertEquals("", br.readLine());
        assertEquals(title, br.readLine());
        assertEquals("", br.readLine());

        //checks the first explanation chunk
        assertEquals(textContent, br.readLine());
        assertEquals("", br.readLine());

        //checks the second explanation chunk
        assertEquals("Caption is: " + caption, br.readLine());
        assertEquals("The path to this image is: " + url, br.readLine());
        assertEquals("", br.readLine());

        //checks the third explanation chunk
        assertEquals(name + " [" + unit + "]", br.readLine());
        assertEquals("-------------------", br.readLine());
        assertEquals(dataValue, br.readLine());
        assertEquals("", br.readLine());

        assertEquals(null, br.readLine());

        br.close();
    }

    /**
     * Test of generateXMLReport method, of class JEFFWizard.
     * Test case: unsuccessful creation of the report
     * because the building process has never started
     */
    @Test
    public void testFailGenerateXMLReport() {
        try {
            simpleWizard.generateXMLReport(filePathXML, true);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The the report can not be generated if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of generateXMLReport method, of class JEFFWizard.
     * Test case: successful creation of the report
     */
    @Test
    public void testGenerateXMLReport_FilePath() throws FileNotFoundException, IOException, DocumentException {

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateXMLReport(filePathXML, true);

        //checks if the file is created
        assertTrue(new File(filePathXML).exists());

        SAXReader reader = new SAXReader();
        Document document = reader.read(filePathXML);

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(5, root.attributes().size());
        assertEquals(3, root.elements().size());

        //checks the name of root element
        assertEquals("explanation", root.getName());

        //checks the attributes of the root - heading of the report
        assertEquals(owner, root.attribute("owner").getText());
        assertEquals(language, root.attribute("language").getText());
        assertEquals(country, root.attribute("country").getText());
        assertEquals(DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), root.attribute("date").getText());
        assertEquals(title, root.attribute("title").getText());

        //checks the names of elements which hold the explanation chunks 
        assertEquals("textualExplanation", root.element("textualExplanation").getName());
        assertEquals("imageExplanation", root.element("imageExplanation").getName());
        assertEquals("dataExplanation", root.element("dataExplanation").getName());

        //checks the first explanation chunk
        assertEquals(1, root.element("textualExplanation").attributes().size());
        assertEquals("error", root.element("textualExplanation").attribute("context").getText());
        assertEquals(1, root.element("textualExplanation").elements().size());
        assertEquals(textContent, root.element("textualExplanation").element("content").getText());


        //checks the second explanation chunk
        assertEquals(2, root.element("imageExplanation").attributeCount());
        assertEquals("informational", root.element("imageExplanation").attribute("context").getText());
        assertEquals(rule, root.element("imageExplanation").attribute("rule").getText());
        assertEquals(1, root.element("imageExplanation").elements().size());
        assertEquals(0, root.element("imageExplanation").element("content").attributeCount());
        assertEquals(1, root.element("imageExplanation").element("content").elements().size());
        assertEquals(1, root.element("imageExplanation").element("content").element("imageUrl").attributeCount());
        assertEquals(caption, root.element("imageExplanation").element("content").element("imageUrl").attribute("caption").getText());
        assertEquals(url, root.element("imageExplanation").element("content").element("imageUrl").getText());

        //checks the third explanation chunk
        assertEquals(3, root.element("dataExplanation").attributeCount());
        assertEquals("positive", root.element("dataExplanation").attribute("context").getText());
        assertEquals(rule, root.element("dataExplanation").attribute("rule").getText());
        assertEquals(group, root.element("dataExplanation").attribute("group").getText());
        assertEquals(2, root.element("dataExplanation").elements().size());
        assertEquals(0, root.element("dataExplanation").element("tags").attributeCount());
        assertEquals(2, root.element("dataExplanation").element("tags").elements().size());

        for (int i = 0; i <
                root.element("dataExplanation").element("tags").elements().size(); i++) {
            Element element = (Element) root.element("dataExplanation").element("tags").elements().get(i);
            assertEquals(0, element.attributeCount());
            assertEquals(tags[i], element.getText());
        }

        assertEquals(2, root.element("dataExplanation").element("content").attributeCount());
        assertEquals(name, root.element("dataExplanation").element("content").attribute("dimensionName").getText());
        assertEquals(unit, root.element("dataExplanation").element("content").attribute("dimensionUnit").getText());
        assertEquals(dataValue, root.element("dataExplanation").element("content").getText());
    }

    /**
     * Test of generateXMLReport method, of class JEFFWizard.
     * Test case: successful creation of the report but with no chunk headers
     */
    @Test
    public void testGenerateXMLReport_FilePathNoChunkHeaders() throws FileNotFoundException, IOException, DocumentException {

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateXMLReport(filePathXML, false);

        //checks if the file is created
        assertTrue(new File(filePathXML).exists());

        SAXReader reader = new SAXReader();
        Document document = reader.read(filePathXML);

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(5, root.attributes().size());
        assertEquals(3, root.elements().size());

        //checks the name of root element
        assertEquals("explanation", root.getName());

        //checks the attributes of the root - heading of the report
        assertEquals(owner, root.attribute("owner").getText());
        assertEquals(language, root.attribute("language").getText());
        assertEquals(country, root.attribute("country").getText());
        assertEquals(DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), root.attribute("date").getText());
        assertEquals(title, root.attribute("title").getText());

        //checks the names of elements which hold the explanation chunks
        assertEquals("textualExplanation", root.element("textualExplanation").getName());
        assertEquals("imageExplanation", root.element("imageExplanation").getName());
        assertEquals("dataExplanation", root.element("dataExplanation").getName());

        //checks the first explanation chunk
        assertEquals(0, root.element("textualExplanation").attributes().size());
        assertEquals(1, root.element("textualExplanation").elements().size());
        assertEquals(textContent, root.element("textualExplanation").element("content").getText());


        //checks the second explanation chunk
        assertEquals(0, root.element("imageExplanation").attributeCount());
        assertEquals(1, root.element("imageExplanation").elements().size());
        assertEquals(0, root.element("imageExplanation").element("content").attributeCount());
        assertEquals(1, root.element("imageExplanation").element("content").elements().size());
        assertEquals(1, root.element("imageExplanation").element("content").element("imageUrl").attributeCount());
        assertEquals(caption, root.element("imageExplanation").element("content").element("imageUrl").attribute("caption").getText());
        assertEquals(url, root.element("imageExplanation").element("content").element("imageUrl").getText());

        //checks the third explanation chunk
        assertEquals(0, root.element("dataExplanation").attributeCount());
        assertEquals(1, root.element("dataExplanation").elements().size());

        assertEquals(2, root.element("dataExplanation").element("content").attributeCount());
        assertEquals(name, root.element("dataExplanation").element("content").attribute("dimensionName").getText());
        assertEquals(unit, root.element("dataExplanation").element("content").attribute("dimensionUnit").getText());
        assertEquals(dataValue, root.element("dataExplanation").element("content").getText());
    }

    /**
     * Test of generateXMLReport method, of class JEFFWizard.
     * Test case: successful creation of the report
     */
    @Test
    public void testGenerateXMLReport_Stream() throws FileNotFoundException, IOException, DocumentException {

        //creates the stream where the report will be sent
        PrintWriter stream = new PrintWriter(new File(filePathXML));

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateXMLReport(stream, true);

        //close the stream
        stream.close();

        //checks if the file is created
        assertTrue(new File(filePathXML).exists());

        SAXReader reader = new SAXReader();
        Document document = reader.read(filePathXML);

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(5, root.attributes().size());
        assertEquals(3, root.elements().size());

        //checks the name of root element
        assertEquals("explanation", root.getName());

        //checks the attributes of the root - heading of the report
        assertEquals(owner, root.attribute("owner").getText());
        assertEquals(language, root.attribute("language").getText());
        assertEquals(country, root.attribute("country").getText());
        assertEquals(DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), root.attribute("date").getText());
        assertEquals(title, root.attribute("title").getText());

        //checks the names of elements which hold the explanation chunks
        assertEquals("textualExplanation", root.element("textualExplanation").getName());
        assertEquals("imageExplanation", root.element("imageExplanation").getName());
        assertEquals("dataExplanation", root.element("dataExplanation").getName());

        //checks the first explanation chunk
        assertEquals(1, root.element("textualExplanation").attributes().size());
        assertEquals("error", root.element("textualExplanation").attribute("context").getText());
        assertEquals(1, root.element("textualExplanation").elements().size());
        assertEquals(textContent, root.element("textualExplanation").element("content").getText());


        //checks the second explanation chunk
        assertEquals(2, root.element("imageExplanation").attributeCount());
        assertEquals("informational", root.element("imageExplanation").attribute("context").getText());
        assertEquals(rule, root.element("imageExplanation").attribute("rule").getText());
        assertEquals(1, root.element("imageExplanation").elements().size());
        assertEquals(0, root.element("imageExplanation").element("content").attributeCount());
        assertEquals(1, root.element("imageExplanation").element("content").elements().size());
        assertEquals(1, root.element("imageExplanation").element("content").element("imageUrl").attributeCount());
        assertEquals(caption, root.element("imageExplanation").element("content").element("imageUrl").attribute("caption").getText());
        assertEquals(url, root.element("imageExplanation").element("content").element("imageUrl").getText());

        //checks the third explanation chunk
        assertEquals(3, root.element("dataExplanation").attributeCount());
        assertEquals("positive", root.element("dataExplanation").attribute("context").getText());
        assertEquals(rule, root.element("dataExplanation").attribute("rule").getText());
        assertEquals(group, root.element("dataExplanation").attribute("group").getText());
        assertEquals(2, root.element("dataExplanation").elements().size());
        assertEquals(0, root.element("dataExplanation").element("tags").attributeCount());
        assertEquals(2, root.element("dataExplanation").element("tags").elements().size());

        for (int i = 0; i <
                root.element("dataExplanation").element("tags").elements().size(); i++) {
            Element element = (Element) root.element("dataExplanation").element("tags").elements().get(i);
            assertEquals(0, element.attributeCount());
            assertEquals(tags[i], element.getText());
        }

        assertEquals(2, root.element("dataExplanation").element("content").attributeCount());
        assertEquals(name, root.element("dataExplanation").element("content").attribute("dimensionName").getText());
        assertEquals(unit, root.element("dataExplanation").element("content").attribute("dimensionUnit").getText());
        assertEquals(dataValue, root.element("dataExplanation").element("content").getText());
    }

    /**
     * Test of generateXMLReport method, of class JEFFWizard.
     * Test case: successful creation of the report but with no chunk headers
     */
    @Test
    public void testGenerateXMLReport_StreamNoChunkHeaders() throws FileNotFoundException, IOException, DocumentException {

        //creates the stream where the report will be sent
        PrintWriter stream = new PrintWriter(new File(filePathXML));

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generateXMLReport(stream, false);

        //close the stream
        stream.close();

        //checks if the file is created
        assertTrue(new File(filePathXML).exists());

        SAXReader reader = new SAXReader();
        Document document = reader.read(filePathXML);

        Element root = document.getRootElement();

        //checks the number of attributes and elements
        assertEquals(5, root.attributes().size());
        assertEquals(3, root.elements().size());

        //checks the name of root element
        assertEquals("explanation", root.getName());

        //checks the attributes of the root - heading of the report
        assertEquals(owner, root.attribute("owner").getText());
        assertEquals(language, root.attribute("language").getText());
        assertEquals(country, root.attribute("country").getText());
        assertEquals(DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()), root.attribute("date").getText());
        assertEquals(title, root.attribute("title").getText());

        //checks the names of elements which hold the explanation chunks
        assertEquals("textualExplanation", root.element("textualExplanation").getName());
        assertEquals("imageExplanation", root.element("imageExplanation").getName());
        assertEquals("dataExplanation", root.element("dataExplanation").getName());

        //checks the first explanation chunk
        assertEquals(0, root.element("textualExplanation").attributes().size());
        assertEquals(1, root.element("textualExplanation").elements().size());
        assertEquals(textContent, root.element("textualExplanation").element("content").getText());


        //checks the second explanation chunk
        assertEquals(0, root.element("imageExplanation").attributeCount());
        assertEquals(1, root.element("imageExplanation").elements().size());
        assertEquals(0, root.element("imageExplanation").element("content").attributeCount());
        assertEquals(1, root.element("imageExplanation").element("content").elements().size());
        assertEquals(1, root.element("imageExplanation").element("content").element("imageUrl").attributeCount());
        assertEquals(caption, root.element("imageExplanation").element("content").element("imageUrl").attribute("caption").getText());
        assertEquals(url, root.element("imageExplanation").element("content").element("imageUrl").getText());

        //checks the third explanation chunk
        assertEquals(0, root.element("dataExplanation").attributeCount());
        assertEquals(1, root.element("dataExplanation").elements().size());
        assertEquals(2, root.element("dataExplanation").element("content").attributeCount());
        assertEquals(name, root.element("dataExplanation").element("content").attribute("dimensionName").getText());
        assertEquals(unit, root.element("dataExplanation").element("content").attribute("dimensionUnit").getText());
        assertEquals(dataValue, root.element("dataExplanation").element("content").getText());
    }

    /**
     * Test of generatePDFReport method, of class JEFFWizard.
     * Test case: unsuccessful creation of the report
     * because the building process has never started
     */
    @Test
    public void testFailGeneratePDFReport() {
        try {
            simpleWizard.generatePDFReport(filePathPDF, true);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The the report can not be generated if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of generatePDFReport method, of class JEFFWizard.
     * Test case: successful creation of the report
     */
    @Test
    public void testGeneratePDFReport_FilePath() {

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generatePDFReport(filePathPDF, true);

        //checks if the file is created
        assertTrue(new File(filePathPDF).exists());
        assertTrue(new File(filePathPDF).length() > 0);
    }

    /**
     * Test of generatePDFReport method, of class JEFFWizard.
     * Test case: successful creation of the report
     */
    @Test
    public void testGeneratePDFReport_Stream() throws FileNotFoundException, IOException {

        //creates the stream where the report will be sent
        FileOutputStream stream = new FileOutputStream(new File(filePathPDF));

        //creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);

        //generates the report
        simpleWizard.generatePDFReport(stream, true);

        stream.close();

        //checks if the file is created
        assertTrue(new File(filePathPDF).exists());
        assertTrue(new File(filePathPDF).length() > 0);
    }
    
    /**
     * Test of generateJSONReport method, of class JEFFWizard.
     * Test case: unsuccessful creation of the report
     * because the building process has never started.
     */
    @Test
    public void testFailGenerateJSONReport(){
    	try {
            simpleWizard.generateJSONReport(filePathJSON, true);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The the report can not be generated if explanation does not exist";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }
    
    /**
     * Test of generateJSONReport method, of class JEFFWizard.
     * Test case: successful creation of the report.
     */
    @Test
    public void testGenerateJSONReport_FilePath() throws FileNotFoundException, IOException{
    	//creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);
        
        //generates the report
        simpleWizard.generateJSONReport(filePathJSON, true);

        //checks if the file is created
        assertTrue(new File(filePathJSON).exists());
        
        //create parser 
        JsonParser parser = new JsonParser();
        
        FileReader reader = new FileReader(filePathJSON);
        Object obj = parser.parse(reader);
        JsonObject jsonObject = (JsonObject) obj;
        
        //checks the heading of the report
        assertEquals("" + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()) + "", jsonObject.get("date").getAsString());
        assertEquals(owner, jsonObject.get("owner").getAsString());
        assertEquals(language, jsonObject.get("language").getAsString());
        assertEquals(country, jsonObject.get("country").getAsString());
        assertEquals(title, jsonObject.get("title").getAsString());

        JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

        //checks the first explanation chunk
        assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
        assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
        assertEquals(textContent, ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());

        //checks the second explanation chunk
        assertEquals("image", ( (JsonObject) jsonArray.get(1) ).get("type").getAsString());
        assertEquals("informational", ( (JsonObject) jsonArray.get(1) ).get("context").getAsString());
        assertEquals(rule, ( (JsonObject) jsonArray.get(1) ).get("rule").getAsString());
        assertEquals(url, ( (JsonObject) jsonArray.get(1) ).get("url").getAsString());
        assertEquals(caption, ( (JsonObject) jsonArray.get(1) ).get("caption").getAsString());
        
        //checks the third explanation chunk
        assertEquals("data", ( (JsonObject) jsonArray.get(2) ).get("type").getAsString());
        assertEquals("single", ( (JsonObject) jsonArray.get(2) ).get("subtype").getAsString());
        assertEquals("positive", ( (JsonObject) jsonArray.get(2) ).get("context").getAsString());
        assertEquals(rule, ( (JsonObject) jsonArray.get(2) ).get("rule").getAsString());
        assertEquals(group, ( (JsonObject) jsonArray.get(2) ).get("group").getAsString());
        JsonArray array = ( (JsonObject) jsonArray.get(2) ).get("tags").getAsJsonArray();
        assertEquals(tags[0], ( (JsonObject) array.get(0) ).get("value").getAsString());
        assertEquals(tags[1], ( (JsonObject) array.get(1) ).get("value").getAsString());
        assertEquals(name, ( (JsonObject) jsonArray.get(2) ).get("dimensionName").getAsString());
        assertEquals(unit, ( (JsonObject) jsonArray.get(2) ).get("dimensionUnit").getAsString());
        assertEquals(dataValue, ( (JsonObject) jsonArray.get(2) ).get("value").getAsString());
        
        reader.close();
    }
    
    /**
     * Test of generateJSONReport method, of class JEFFWizard.
     * Test case: successful creation of the report but with no chunk headers.
     */
    @Test
    public void testGenerateJSONReport_FilePathNoHeaders() throws FileNotFoundException, IOException{
    	//creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);
        
        //generates the report
        simpleWizard.generateJSONReport(filePathJSON, false);

        //checks if the file is created
        assertTrue(new File(filePathJSON).exists());

        //create parser 
        JsonParser parser = new JsonParser();

        FileReader reader = new FileReader(filePathJSON);
        Object obj = parser.parse(reader);
        JsonObject jsonObject = (JsonObject) obj;

        //checks the heading of the report
        assertEquals("" + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()) + "", jsonObject.get("date").getAsString());
        assertEquals(owner, jsonObject.get("owner").getAsString());
        assertEquals(language, jsonObject.get("language").getAsString());
        assertEquals(country, jsonObject.get("country").getAsString());
        assertEquals(title, jsonObject.get("title").getAsString());
        
        JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();
        
        //checks the first explanation chunk
        assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
        assertEquals(textContent, ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());

        //checks the second explanation chunk
        assertEquals("image", ( (JsonObject) jsonArray.get(1) ).get("type").getAsString());
        assertEquals(url, ( (JsonObject) jsonArray.get(1) ).get("url").getAsString());
        assertEquals(caption, ( (JsonObject) jsonArray.get(1) ).get("caption").getAsString());
        
        //checks the third explanation chunk
        assertEquals("data", ( (JsonObject) jsonArray.get(2) ).get("type").getAsString());
        assertEquals("single", ( (JsonObject) jsonArray.get(2) ).get("subtype").getAsString());
        assertEquals(name, ( (JsonObject) jsonArray.get(2) ).get("dimensionName").getAsString());
        assertEquals(unit, ( (JsonObject) jsonArray.get(2) ).get("dimensionUnit").getAsString());
        assertEquals(dataValue, ( (JsonObject) jsonArray.get(2) ).get("value").getAsString());
        
        reader.close();
    }
    
    /**
     * Test of generateJSONReport method, of class JEFFWizard.
     * Test case: successful creation of the report.
     */
    @Test
    public void testGenerateJSONReport_Stream() throws FileNotFoundException, IOException{
    	//creates the stream where the report will be sent
    	FileWriter stream = new FileWriter(new File(filePathJSON));
    	
    	//creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);
        
        //generates the report
        simpleWizard.generateJSONReport(stream, true);

        stream.close();
        
        //checks if the file is created
        assertTrue(new File(filePathJSON).exists());
        
        //create parser 
        JsonParser parser = new JsonParser();
        
        FileReader reader = new FileReader(filePathJSON);
        Object obj = parser.parse(reader);
        JsonObject jsonObject = (JsonObject) obj;
        
        //checks the heading of the report
        assertEquals("" + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()) + "", jsonObject.get("date").getAsString());
        assertEquals(owner, jsonObject.get("owner").getAsString());
        assertEquals(language, jsonObject.get("language").getAsString());
        assertEquals(country, jsonObject.get("country").getAsString());
        assertEquals(title, jsonObject.get("title").getAsString());

        JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();

        //checks the first explanation chunk
        assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
        assertEquals("error", ( (JsonObject) jsonArray.get(0) ).get("context").getAsString());
        assertEquals(textContent, ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());

        //checks the second explanation chunk
        assertEquals("image", ( (JsonObject) jsonArray.get(1) ).get("type").getAsString());
        assertEquals("informational", ( (JsonObject) jsonArray.get(1) ).get("context").getAsString());
        assertEquals(rule, ( (JsonObject) jsonArray.get(1) ).get("rule").getAsString());
        assertEquals(url, ( (JsonObject) jsonArray.get(1) ).get("url").getAsString());
        assertEquals(caption, ( (JsonObject) jsonArray.get(1) ).get("caption").getAsString());
        
        //checks the third explanation chunk
        assertEquals("data", ( (JsonObject) jsonArray.get(2) ).get("type").getAsString());
        assertEquals("single", ( (JsonObject) jsonArray.get(2) ).get("subtype").getAsString());
        assertEquals("positive", ( (JsonObject) jsonArray.get(2) ).get("context").getAsString());
        assertEquals(rule, ( (JsonObject) jsonArray.get(2) ).get("rule").getAsString());
        assertEquals(group, ( (JsonObject) jsonArray.get(2) ).get("group").getAsString());
        JsonArray array = ( (JsonObject) jsonArray.get(2) ).get("tags").getAsJsonArray();
        assertEquals(tags[0], ( (JsonObject) array.get(0) ).get("value").getAsString());
        assertEquals(tags[1], ( (JsonObject) array.get(1) ).get("value").getAsString());
        assertEquals(name, ( (JsonObject) jsonArray.get(2) ).get("dimensionName").getAsString());
        assertEquals(unit, ( (JsonObject) jsonArray.get(2) ).get("dimensionUnit").getAsString());
        assertEquals(dataValue, ( (JsonObject) jsonArray.get(2) ).get("value").getAsString());
        
        reader.close();
    }
    
    /**
     * Test of generateJSONReport method, of class JEFFWizard.
     * Test case: successful creation of the report but with no chunk headers.
     */
    @Test
    public void testGenerateJSONReport_StreamNoChunkHeaders() throws FileNotFoundException, IOException {
    	//creates the stream where the report will be sent
    	FileWriter stream = new FileWriter(new File(filePathJSON));
    	
    	//creates the object using the constructor with all values
        simpleWizard = new JEFFWizard(owner, language, country, title, false);

        //starting the building process
        simpleWizard.createExplanation();

        //adding explanation chunk
        simpleWizard.addTextError(textContent);
        simpleWizard.addImage(rule, imageContent);
        simpleWizard.addDataPositive(group, rule, tags, dataContent);
        
        //generates the report
        simpleWizard.generateJSONReport(stream, false);

        stream.close();
        
        //checks if the file is created
        assertTrue(new File(filePathJSON).exists());

        //create parser 
        JsonParser parser = new JsonParser();

        FileReader reader = new FileReader(filePathJSON);
        Object obj = parser.parse(reader);
        JsonObject jsonObject = (JsonObject) obj;

        //checks the heading of the report
        assertEquals("" + DateFormat.getInstance().format(simpleWizard.getExplanation().getCreated().getTime()) + "", jsonObject.get("date").getAsString());
        assertEquals(owner, jsonObject.get("owner").getAsString());
        assertEquals(language, jsonObject.get("language").getAsString());
        assertEquals(country, jsonObject.get("country").getAsString());
        assertEquals(title, jsonObject.get("title").getAsString());
        
        JsonArray jsonArray = jsonObject.get("chunks").getAsJsonArray();
        
        //checks the first explanation chunk
        assertEquals("text", ( (JsonObject) jsonArray.get(0) ).get("type").getAsString());
        assertEquals(textContent, ( (JsonObject) jsonArray.get(0) ).get("content").getAsString());

        //checks the second explanation chunk
        assertEquals("image", ( (JsonObject) jsonArray.get(1) ).get("type").getAsString());
        assertEquals(url, ( (JsonObject) jsonArray.get(1) ).get("url").getAsString());
        assertEquals(caption, ( (JsonObject) jsonArray.get(1) ).get("caption").getAsString());
        
        //checks the third explanation chunk
        assertEquals("data", ( (JsonObject) jsonArray.get(2) ).get("type").getAsString());
        assertEquals("single", ( (JsonObject) jsonArray.get(2) ).get("subtype").getAsString());
        assertEquals(name, ( (JsonObject) jsonArray.get(2) ).get("dimensionName").getAsString());
        assertEquals(unit, ( (JsonObject) jsonArray.get(2) ).get("dimensionUnit").getAsString());
        assertEquals(dataValue, ( (JsonObject) jsonArray.get(2) ).get("value").getAsString());
        
        reader.close();
    }
}
