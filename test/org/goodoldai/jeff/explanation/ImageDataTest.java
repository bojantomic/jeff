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

import junit.framework.TestCase;

/**
 *
 * @author Bojan Tomic
 */
public class ImageDataTest extends TestCase {

    private ImageData instance=null;

    /**
     * Creates a sample ImageData instance
     */
    @Override
    protected void setUp() {
        String URL = "sample.jpg";
        instance = new ImageData(URL);
    }

    /**
     * Test of one argument constructor, of class ImageData.
     * test case: successfull intialization.
     */
    public void testOneArgumentConstructor() {
        String URL = "sample2.jpg";
        instance = new ImageData(URL);

        //Test if the URL value was set
        assertEquals(URL,instance.getURL());

        //Test if the caption is se to null
        assertEquals(null,instance.getCaption());
    }
    
    /**
     * Test of two argument constructor, of class ImageData.
     * test case: successfull intialization.
     */
    public void testTwoArgumentConstructor() {
        String URL = "sample2.jpg";
        String caption = "sample2";
        instance = new ImageData(URL,caption);

        //Test if the URL value was set
        assertEquals(URL,instance.getURL());

        //Test if the caption is se to null
        assertEquals(caption,instance.getCaption());
    }

    /**
     * Test of setURL method, of class ImageData.
     * test case: successfull URL change.
     */
    public void testSetURLSuccessfull() {
        String val = "sample2.jpg";
        instance.setURL(val);

        assertEquals(val,instance.getURL());
    }


    /**
     * Test of setURL method, of class ImageData.
     * test case: unsuccessfull URL change - URL is null.
     */
    public void testSetURLNullURL() {
            try{
            instance.setURL(null);
            fail("An exception should have been thrown but wasn't");
        }catch(Exception e){
            assertTrue(e instanceof ExplanationException);
            String expResult = "The URL cannot be null or an empty string value";
            String result = e.getMessage();
            assertEquals(expResult,result);
        }
    }

    /**
     * Test of setURL method, of class ImageData.
     * test case: unsuccessfull URL change - URL is an empty string.
     */
    public void testSetURLEmptyURL() {
            try{
            instance.setURL("");
            fail("An exception should have been thrown but wasn't");
        }catch(Exception e){
            assertTrue(e instanceof ExplanationException);
            String expResult = "The URL cannot be null or an empty string value";
            String result = e.getMessage();
            assertEquals(expResult,result);
        }
    }

    /**
     * Test of setCaption method, of class ImageData.
     * test case: successfull caption change.
     */
    public void testSetCaptionSuccessfull() {
        String val = "sample caption";
        instance.setCaption(val);

        assertEquals(val,instance.getCaption());
    }
    
    /**
     * Test of clone method, of class ImageData.
     * test case: successfull cloning.
     */
    public void testCloneSuccessfull() {
        String URL = "sample URL";
        String caption = "sample caption";
        instance.setURL(URL);
        instance.setCaption(caption);

        ImageData clone = instance.clone();

        assertTrue(instance != clone);

        assertEquals(URL, clone.getURL());
        assertEquals(caption,clone.getCaption());
    }

}
