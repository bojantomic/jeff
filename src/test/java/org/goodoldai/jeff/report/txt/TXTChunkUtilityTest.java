/*
 * Copyright 2009 Nemanja Jovanovic
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
package org.goodoldai.jeff.report.txt;

import org.goodoldai.jeff.AbstractJeffTest;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Nemanja Jovanovic
 */
public class TXTChunkUtilityTest extends AbstractJeffTest {

    TextExplanationChunk textEchunk1;
    TextExplanationChunk textEchunk2;
    PrintWriter pw;
    BufferedReader br;

    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.dom4j.Document instances that are
     * used for testing
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        String[] tags = {"tag1", "tag2"};

        textEchunk1 = new TextExplanationChunk("testing");
        textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");

        pw = new PrintWriter(new File("tekst.txt"));
        br = new BufferedReader(new FileReader("tekst.txt"));
    }

    @After
    public void tearDown() throws IOException {

        textEchunk1 = null;
        textEchunk2 = null;

        pw.close();
        br.close();

        new File("tekst.txt").delete();

    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is not predefined
     */
    @Test
    public void testTranslateContextTypeNotRecognized() {
        int context = -555;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);

        assertEquals(String.valueOf(context), result);
    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "INFORMATIONAL"
     */
    @Test
    public void testTranslateContextKnownTypeInformational() {
        int context = 0;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);
        String expResult = "INFORMATIONAL".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "WARNING"
     */
    @Test
    public void testTranslateContextKnownTypeInformationalWarning() {
        int context = -5;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);
        String expResult = "WARNING".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "ERROR"
     */
    @Test
    public void testTranslateContextKnownTypeError() {
        int context = -10;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);
        String expResult = "ERROR".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypePositive() {
        int context = 1;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);
        String expResult = "POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_POSITIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryPositive() {
        int context = 2;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeNegative() {
        int context = -1;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);
        String expResult = "NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class TXTChunkUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_NEGATIVE"
     */
    @Test
    public void testTranslateContextKnownTypeVeryNegative() {
        int context = -2;
        String result = TXTChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of insertExplanationInfo method, of class TXTChunkUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    @Test
    public void testInsertExplanationInfo1() throws IOException {
        TXTChunkUtility.insertExplanationInfo(textEchunk2, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("tekst.txt").exists());

        //checks the context
        assertEquals("The context is: error", br.readLine());

        //checks the rule
        assertEquals("The rule that initiated the creation of this chunk: testRule", br.readLine());

        //checks the group
        assertEquals("The group to which the executed rule belongs: testGroup", br.readLine());

        //checks the tags
        assertEquals("The tags are: tag1 tag2 ", br.readLine());

        //checks if anyting eles has been writen to file by mistake
        assertEquals(null, br.readLine());
    }

    /**
     * Test of insertExplanationInfo method, of class XMLUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has only context.
     */
    @Test
    public void testInsertExplanationInfo2() throws IOException {
        TXTChunkUtility.insertExplanationInfo(textEchunk1, pw);
        pw.close();

        //checks if the file is created
        assertTrue(new File("tekst.txt").exists());

        //checks the context
        assertEquals("The context is: informational", br.readLine());

        //checks if anyting eles has been writen to file by mistake
        assertEquals(null, br.readLine());
    }
}
