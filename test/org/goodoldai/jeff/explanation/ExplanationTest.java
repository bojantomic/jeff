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
package org.goodoldai.jeff.explanation;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import junit.framework.TestCase;

/**
 *
 * @author Bojan Tomic
 */
public class ExplanationTest extends TestCase {

    Explanation instance = null;
    TextExplanationChunk tchunk = null;
    ImageExplanationChunk ichunk = null;

    /**
     * Creates an instance of Explanation and
     * two sample explanation chunks.
     */
    @Override
    protected void setUp() {
        instance = new Explanation();
        tchunk = new TextExplanationChunk("sample text");
        ichunk = new ImageExplanationChunk(new ImageData("sample.jpg"));
    }

    /**
     * Test of no argument constructor, of class Explanation.
     */
    public void testNoArgConstructor() {
        //Testing if the "created" attribute is set to
        //the current date/time. Accuracy is not full
        //as of the time difference between object creation
        //moments.
        GregorianCalendar expresult = new GregorianCalendar();
        GregorianCalendar result = instance.getCreated();

        //The difference (in milliseconds) between creation moments
        //must be more or equal to zero but less than 1 second.
        long milliseconds = expresult.getTimeInMillis() - result.getTimeInMillis();
        assertTrue(milliseconds >= 0 && milliseconds <= 1000);

        assertEquals(null, instance.getOwner());
        assertEquals(null, instance.getLanguage());
        assertEquals(null, instance.getCountry());
        assertEquals(null, instance.getTitle());

        assertTrue(instance.getChunks() != null);
        assertEquals(0, instance.getChunks().size());
    }

    /**
     * Test of one argument constructor, of class Explanation.
     */
    public void testOneArgConstructor() {
        String owner = "Peter";

        instance = new Explanation(owner);

        //Testing if the "created" attribute is set to
        //the current date/time. Accuracy is not full
        //as of the time difference between object creation
        //moments.
        GregorianCalendar expresult = new GregorianCalendar();
        GregorianCalendar result = instance.getCreated();

        //The difference (in milliseconds) between creation moments
        //must be more or equal to zero but less than 1 second.
        long milliseconds = expresult.getTimeInMillis() - result.getTimeInMillis();
        assertTrue(milliseconds >= 0 && milliseconds <= 1000);

        assertEquals(owner, instance.getOwner());
        assertEquals(null, instance.getLanguage());
        assertEquals(null, instance.getCountry());
        assertEquals(null, instance.getTitle());

        assertTrue(instance.getChunks() != null);
        assertEquals(0, instance.getChunks().size());
    }

    /**
     * Test of two argument constructor, of class Explanation.
     */
    public void testTwoArgConstructor() {
        String owner = "Peter";
        String title = "explanation title";

        instance = new Explanation(owner, title);

        //Testing if the "created" attribute is set to
        //the current date/time. Accuracy is not full
        //as of the time difference between object creation
        //moments.
        GregorianCalendar expresult = new GregorianCalendar();
        GregorianCalendar result = instance.getCreated();

        //The difference (in milliseconds) between creation moments
        //must be more or equal to zero but less than 1 second.
        long milliseconds = expresult.getTimeInMillis() - result.getTimeInMillis();
        assertTrue(milliseconds >= 0 && milliseconds <= 1000);

        assertEquals(owner, instance.getOwner());
        assertEquals(null, instance.getLanguage());
        assertEquals(null, instance.getCountry());
        assertEquals(title, instance.getTitle());

        assertTrue(instance.getChunks() != null);
        assertEquals(0, instance.getChunks().size());
    }

    /**
     * Test of four argument constructor, of class Explanation.
     */
    public void testFourArgConstructor() {
        String owner = "Peter";
        String language = "English";
        String locale = "EN-GB";
        String title = "explanation title";

        instance = new Explanation(owner, language, locale, title);

        //Testing if the "created" attribute is set to
        //the current date/time. Accuracy is not full
        //as of the time difference between object creation
        //moments.
        GregorianCalendar expresult = new GregorianCalendar();
        GregorianCalendar result = instance.getCreated();

        //The difference (in milliseconds) between creation moments
        //must be more or equal to zero but less than 1 second.
        long milliseconds = expresult.getTimeInMillis() - result.getTimeInMillis();
        assertTrue(milliseconds >= 0 && milliseconds <= 1000);

        assertEquals(owner, instance.getOwner());
        assertEquals(language, instance.getLanguage());
        assertEquals(locale, instance.getCountry());
        assertEquals(title, instance.getTitle());

        assertTrue(instance.getChunks() != null);
        assertEquals(0, instance.getChunks().size());
    }

    /**
     * Test of getChunks method, of class Explanation.
     * Test case - successfull execution and cloning.
     */
    public void testGetChunksSuccessfull() {
        instance.addChunk(tchunk);
        instance.addChunk(ichunk);

        ArrayList<ExplanationChunk> result = instance.getChunks();

        //Check that the list contains only two chunks
        assertEquals(2, result.size());

        //Check that the first chunk was cloned properly
        assertTrue(tchunk != result.get(0));

        //Check the first chunk type and content
        assertEquals(tchunk.getClass().getName(),
                result.get(0).getClass().getName());
        assertEquals(tchunk.getContent(), result.get(0).getContent());

        //Check that the second chunk was cloned properly
        assertTrue(ichunk != result.get(1));

        //Check the second chunk type and content
        assertEquals(ichunk.getClass().getName(),
                result.get(1).getClass().getName());
        assertTrue(ichunk.getContent() != result.get(1).getContent());
        assertEquals(((ImageData) ichunk.getContent()).getURL(),
                ((ImageData) result.get(1).getContent()).getURL());
        assertEquals(((ImageData) ichunk.getContent()).getCaption(),
                ((ImageData) result.get(1).getContent()).getCaption());

    }

    /**
     * Test of addChunk method, of class Explanation.
     * Test case - successfull execution.
     */
    public void testAddChunkSuccessfull() {
        ArrayList<ExplanationChunk> result = instance.getChunks();

        //Check that the list is empty
        assertTrue(result.size() == 0);

        //Add chunks
        instance.addChunk(ichunk);
        instance.addChunk(tchunk);
        result = instance.getChunks();

        //Check that the list contains one tchunk
        assertTrue(result.size() == 2);

        //Check that the ichunk was inserted correctly
        assertEquals(ichunk.getClass().getName(),
                result.get(0).getClass().getName());
        assertTrue(ichunk.getContent() != result.get(1).getContent());
        assertEquals(((ImageData) ichunk.getContent()).getURL(),
                ((ImageData) result.get(0).getContent()).getURL());
        assertEquals(((ImageData) ichunk.getContent()).getCaption(),
                ((ImageData) result.get(0).getContent()).getCaption());

        //Check that the tchunk was inserted correctly
        assertEquals(tchunk.getClass().getName(),
                result.get(1).getClass().getName());
        assertEquals(tchunk.getContent(), result.get(1).getContent());

    }

    /**
     * Test of addChunk method, of class Explanation.
     * Test case - unsuccessfull execution because of null argument.
     */
    public void testAddChunkNullArgument() {
        try {
            instance.addChunk(null);
            fail("Exception should have been thrown, but it wasn't");
        } catch (Exception e) {
            String result = e.getMessage();
            String expResult = "The entered chunk must not be null";
            assertTrue(e instanceof org.goodoldai.jeff.explanation.ExplanationException);
            assertEquals(expResult, result);
        }
    }

     /**
     * Test of getChunks method, of class Explanation.
     * Test case - successfull execution and cloning.
     */
    public void testGetCreated() {
        //Testing if the "created" attribute is set to
        //the current date/time. Accuracy is not full
        //as of the time difference between object creation
        //moments.
        GregorianCalendar expResult = new GregorianCalendar();
        GregorianCalendar result = instance.getCreated();

        //The difference (in milliseconds) between creation moments
        //must be more or equal to zero but less than 1 second.
        long milliseconds = expResult.getTimeInMillis() - result.getTimeInMillis();
        assertTrue(milliseconds >= 0 && milliseconds <= 1000);

        //Testing if the "getCreated" method returns a clone by trying
        //to change the returned value.
        result.setTimeInMillis(0);
        assertTrue(result.getTimeInMillis()==0);
        
        result = instance.getCreated();
        assertTrue(result.getTimeInMillis()!=0);
    }
}
