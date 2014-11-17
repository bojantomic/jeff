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

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.builder.internationalization.InternationalizationManager;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;
import junit.framework.TestCase;

/**
 * @author Bojan Tomic
 */
public class DefaultTextExplanationChunkBuilderTest extends TestCase {

    Object[] content = null;
    int context = 0;
    String group = null;
    String rule = null;
    String[] tags = null;
    File unit = null;
    File dimensionNames = null;
    File imageCaptions = null;
    File text = null;
    File textgroup = null;
    DefaultTextExplanationChunkBuilder instance = null;
    InternationalizationManager im = null;

    @Override
    protected void setUp() throws Exception {
        //Create some sample TextExplanationChunk data
        context = ExplanationChunk.ERROR;
        group = "group 1";
        rule = "rule3";
        tags = new String[2];
        tags[0] = "tag1";
        tags[1] = "tag2";

        content = new Object[2];
        content[0] = "poslednjem trenutku";
        content[1] = new Integer(3);


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

        //The internationalization manager needs to be initialized before
        //explanation builders can use it
        im = new InternationalizationManager(new Locale("srb", "RS"));

        //Initialize the text explanation chunk builder
        instance = new DefaultTextExplanationChunkBuilder(im);
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
     * Test of constructor, of class DefaultTextExplanationChunkBuilder.
     * Test case: unsuccessfull execution - i18nManager is null
     */
    public void testConstructori18nManagerNull() {
        try {
            instance= new DefaultTextExplanationChunkBuilder(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered i18nManager instance must not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultTextExplanationChunkBuilder.
     * Test case: unsuccessfull execution - wrong type content
     */
    public void testBuildChunkWrongTypeContent() {
        try {
            instance.buildChunk(context, group, rule, tags, "wrong type");
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered content must be an array of Object instances";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }

    }

    /**
     * Test of buildChunk method, of class DefaultTextExplanationChunkBuilder.
     * Test case: unsuccessfull execution - translation doesn't exist
     */
    public void testBuildChunkNoTranslation() {
        try {
            rule = "rule 65";
            content = null;
            group = null;
            instance.buildChunk(context, group, rule, tags, content);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The translation for 'null' group 'rule 65' rule could not be found";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of buildChunk method, of class DefaultTextExplanationChunkBuilder.
     * Test case: successfull execution
     */
    public void testBuildChunkSuccessfull1() {
        TextExplanationChunk tc =
                (TextExplanationChunk) (instance.buildChunk(context, group, rule, tags, content));

        //Assert that the chunk holds correct data
        assertEquals(context, tc.getContext());
        assertEquals(group, tc.getGroup());
        assertEquals(rule, tc.getRule());
        assertEquals(tags, tc.getTags());

        //Assert that the translation is correct
        assertEquals("donet je zakljucak 3 u poslednjem trenutku",
                ((String) tc.getContent()));
    }

    /**
     * Test of buildChunk method, of class DefaultTextExplanationChunkBuilder.
     * Test case: successfull execution - translation with no arguments
     */
    public void testBuildChunkSuccessfull2() {
        rule = "rule 4";
        content = null;
        TextExplanationChunk tc =
                (TextExplanationChunk) (instance.buildChunk(context, group, rule, tags, content));

        //Assert that the chunk holds correct data
        assertEquals(context, tc.getContext());
        assertEquals(group, tc.getGroup());
        assertEquals(rule, tc.getRule());
        assertEquals(tags, tc.getTags());

        //Assert that the translation is correct
        assertEquals("donet je zakljucak 4", ((String) tc.getContent()));
    }

    /**
     * Test of buildChunk method, of class DefaultTextExplanationChunkBuilder.
     * Test case: successfull execution - translation with no arguments
     * and with the rule group name omitted
     */
    public void testBuildChunkSuccessfull3() {
        rule = "rule 2";
        content = null;
        group = null;
        TextExplanationChunk tc =
                (TextExplanationChunk) (instance.buildChunk(context, group, rule, tags, content));

        //Assert that the chunk holds correct data
        assertEquals(context, tc.getContext());
        assertEquals(group, tc.getGroup());
        assertEquals(rule, tc.getRule());
        assertEquals(tags, tc.getTags());

        //Assert that the translation is correct
        assertEquals("donet je zakljucak 2", ((String) tc.getContent()));
    }
}
