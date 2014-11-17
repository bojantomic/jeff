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

/**
 *
 * @author Bojan Tomic
 */
public class TextExplanationChunkTest extends ExplanationChunkTest {
    
    public ExplanationChunk getInstance(Object content){
        return new TextExplanationChunk(content);
    }

    public ExplanationChunk getInstance(int context, String group, String rule, String[] tags, Object content){
        return new TextExplanationChunk(context, group, rule, tags, content);
    }

    public Object getSampleContent(){
        return "sample content";
    }

    /**
     * Test of setContent method, of class TextExplanationChunk.
     * Test case:  successfull change of content.
     */
    public void testSetContentSuccessfull() {
        String val = "sample content 2";
        instance.setContent(val);

         //Test if content has been sucessfully changed
        assertEquals(val, instance.getContent());
    }

    /**
     * Test of setContent method, of class TextExplanationChunk.
     * Test case: unsuccessfull change of content - wrong type content.
     */
    public void testSetContentWrongTypeContent() {
        Object val = new Object();

        try{
            instance.setContent(val);
            fail("An exception should have been thrown but wasn't");
        }catch(Exception e){
            assertTrue(e instanceof ExplanationException);
            String expResult = "The content must be a non-null, non-empty string value";
            String result = e.getMessage();
            assertEquals(expResult,result);
        }
    }

    /**
     * Test of setContent method, of class TextExplanationChunk.
     * Test case: unsuccessfull change of content - null content.
     */
    public void testSetContentNullContent() {

        try{
            instance.setContent(null);
            fail("An exception should have been thrown but wasn't");
        }catch(Exception e){
            assertTrue(e instanceof ExplanationException);
            String expResult = "The content must be a non-null, non-empty string value";
            String result = e.getMessage();
            assertEquals(expResult,result);
        }
    }

    /**
     * Test of setContent method, of class TextExplanationChunk.
     * Test case: unsuccessfull change of content - empty string content.
     */
    public void testSetContentEmptyStringContent() {
        String s = "";

        try{
            instance.setContent(s);
            fail("An exception should have been thrown but wasn't");
        }catch(Exception e){
            assertTrue(e instanceof ExplanationException);
            String expResult = "The content cannot be null or an empty string";
            String result = e.getMessage();
            assertEquals(expResult,result);
        }
    }

    /**
     * Test of clone method, of class TextExplanationChunk.
     * Test case:  successfull clone operation.
     */
    public void testCloneSuccessfull() {
        TextExplanationChunk clone = (TextExplanationChunk)(instance.clone());
        
         //Test if it is not the same object
        assertTrue(clone != instance);

         //Test if attribute values have been sucessfully copied
        assertEquals(instance.getContext(), clone.getContext());
        assertEquals(instance.getGroup(), clone.getGroup());
        assertEquals(instance.getRule(), clone.getRule());

        //Test if tags have been properly cloned
        if (instance.getTags() != null)
            assertTrue(instance.getTags() != clone.getTags());

        for (int i=0;i<instance.getTags().length;i++)
            assertEquals(instance.getTags()[i], clone.getTags()[i]);

        //test if content has been successfully copied
         assertEquals(instance.getContent(), clone.getContent());
    }

}
