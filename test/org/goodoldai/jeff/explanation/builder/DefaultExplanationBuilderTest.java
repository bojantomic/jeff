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
import org.goodoldai.jeff.explanation.Explanation;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.SingleData;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Bojan Tomic
 */
public class DefaultExplanationBuilderTest extends ExplanationBuilderTest {

    //Sample property files
    File unit = null;
    File dimensionNames = null;
    File imageCaptions = null;
    File text = null;
    File textgroup = null;

    //Text chunk sample data
    Object[] tcontent = null;
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
        return new DefaultExplanationChunkBuilderFactory();
    }

    public ExplanationBuilder getInstance(Explanation explanation, ExplanationChunkBuilderFactory factory) {
        return new DefaultExplanationBuilder(explanation, factory);
    }

    @Override
    protected void setUp() throws Exception {
        //Create some sample TextExplanationChunk data
        tcontext = ExplanationChunk.ERROR;
        tgroup = "group 1";
        trule = "rule3";
        ttags = new String[2];
        ttags[0] = "tagt1";
        ttags[1] = "tagt2";

        tcontent = new Object[2];
        tcontent[0] = "poslednjem trenutku";
        tcontent[1] = new Integer(3);

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

        explanation = new Explanation("owner 1", "srb", "RS");

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
     * Test of two argument constructor, of class DefaultExplanationBuilder.
     * Test case: unsuccesfull initialization because property files do
     * not exist
     */
    public void testDefaultExplanationBuilderNoPropertyFiles() {
        explanation = new Explanation("owner 1", "fr", "FR");

        try {
            instance = getInstance(explanation, getFactory());
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Can't find bundle for base name units, locale fr_FR";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of two argument constructor, of class DefaultExplanationBuilder.
     * Test case: unsuccesfull initialization because property files for
     * the default locale not exist
     */
    public void testDefaultExplanationBuilderNoPropertyFilesDefaultLocale() {
        explanation = new Explanation("owner 1", null, null);

        try {
            instance = getInstance(explanation, getFactory());
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Can't find bundle for base name units, locale ";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addExplanationChunk method, of class DefaultExplanationBuilder.
     * Test case: unsuccesfull execution - unknown chunk type
     */
    public void testAddExplanationChunkUnknownChunkType() {
        explanation = new Explanation("owner 1", "srb", "RS");

        instance = getInstance(explanation, getFactory());

        try {
            instance.addExplanationChunk(-2, tcontext, tgroup, trule, ttags, tcontent);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "Chunk type '-2' was not recognized";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of addExplanationChunk method, of class DefaultExplanationBuilder.
     * Test case: succesfull execution - text chunk added
     */
    public void testAddExplanationSuccessfullTextChunk() {
        explanation = new Explanation("owner 1", "srb", "RS");

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

        //Assert that the translation is correct
        assertEquals("donet je zakljucak 3 u poslednjem trenutku",
                ((String) tc.getContent()));
    }

    /**
     * Test of addExplanationChunk method, of class DefaultExplanationBuilder.
     * Test case: succesfull execution - image chunk added
     */
    public void testAddExplanationSuccessfullImageChunk() {
        explanation = new Explanation("owner 1", "srb", "RS");

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

        //Assert that the caption has been translated
        //and that URL remains unchanged
        assertEquals("Fotografija kita",((ImageData)(ic.getContent())).getCaption());
        assertEquals("URL1",((ImageData)(ic.getContent())).getURL());
    }


    /**
     * Test of addExplanationChunk method, of class DefaultExplanationBuilder.
     * Test case: succesfull execution - data chunk added
     */
    public void testAddExplanationSuccessfullDataChunk() {
        explanation = new Explanation("owner 1", "srb", "RS");

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

        //Assert that the dimension name and unit translations are
        //correct
        assertEquals("novac", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals("RSD", ((SingleData) dc.getContent()).getDimension().getUnit());
    }

        /**
     * Test of addExplanationChunk method, of class DefaultExplanationBuilder.
     * Test case: succesfull execution - three chunks added
     */
    public void testAddExplanationSuccessfullThreeChunks() {
        explanation = new Explanation("owner 1", "srb", "RS");

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

        //Assert that the translation is correct
        assertEquals("donet je zakljucak 3 u poslednjem trenutku",
                ((String) tc.getContent()));



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

        //Assert that the caption has been translated
        //and that URL remains unchanged
        assertEquals("Fotografija kita",((ImageData)(ic.getContent())).getCaption());
        assertEquals("URL1",((ImageData)(ic.getContent())).getURL());



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

        //Assert that the dimension name and unit translations are
        //correct
        assertEquals("novac", ((SingleData) dc.getContent()).getDimension().getName());
        assertEquals("RSD", ((SingleData) dc.getContent()).getDimension().getUnit());
    }
}
