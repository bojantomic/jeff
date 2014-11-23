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
import org.goodoldai.jeff.AbstractJeffTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Since ExplanationChunk is an abstract class the test
 * case is also abstract. When testing ExplanationChunk
 * concrete subclasses, their test case needs to subclass
 * this test case and implement its three abstract methods
 * in order to provide concrete subclass instances, and
 * the appropriate sample content.
 * 
 * @author Bojan Tomic
 */
public abstract class ExplanationChunkTest extends AbstractJeffTest {

    /**
     * An ExplanationChunk instance to be used while testing
     */
    protected ExplanationChunk instance = null;

    /**
     * Initializes the ExplanationChunk instance
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        int context = ExplanationChunk.ERROR;
        String group = "Group 1";
        String rule = "Rule 1";
        String[] tags = {"tag 1", "tag 2", "tag 3"};
        instance = getInstance(context, group,rule,tags,getSampleContent());
    }

    /**
     * Invokes the one argument constructor and returns the
     * ExplanationChunk subclass instance.
     *
     * @param content sample explanation chunk content
     *
     * @return chunk instance
     */
    public abstract ExplanationChunk getInstance(Object content);

    /**
     * Invokes the five argument constructor and returns the
     * ExplanationChunk subclass instance.
     * 
     * @param context chunk context
     * @param content chunk content
     * @param group rule group
     * @param rule rule
     * @param tags tags related to this chunk
     * 
     * @return chunk instance
     */
    public abstract ExplanationChunk getInstance(int context, String group, String rule, String[] tags, Object content);

    /**
     * Returns the concrete ExplanationChunk subclass
     * sample content.
     *
     * @return sample chunk content
     */
    public abstract Object getSampleContent();

    /**
     * Test of constructor with one argument, of class ExplanationChunk.
     */
    @Test
    public void testConstructorOneArgument() {
        //Create an instance using the one argument constructor
        Object content = getSampleContent();
        instance = getInstance(content);
        
        //Test if content has been sucessfully inserted
        assertEquals(content, instance.getContent());

        //Test if context has been set to INFORMATIONAL
        assertEquals(ExplanationChunk.INFORMATIONAL,instance.getContext());
        
        //Test if all other attributes have been set to null
        assertEquals(null,instance.getGroup());
        assertEquals(null,instance.getRule());
        assertEquals(null,instance.getTags());
        
    }


    /**
     * Test of constructor with five arguments, of class ExplanationChunk.
     */
    @Test
    public void testConstructorFiveArguments() {
        //Create an instance using the five argument constructor
        int context = ExplanationChunk.ERROR;
        String group = "Group 1";
        String rule = "Rule 1";
        String[] tags = {"tag 1", "tag 2", "tag 3"};
        Object content = getSampleContent();
        instance = getInstance(context,group,rule,tags,content);
        
         //Test if content has been sucessfully inserted
        assertEquals(content,instance.getContent());

        //Test if context has been sucessfully inserted
        assertEquals(context,instance.getContext());

        //Test if group has been sucessfully inserted
        assertEquals(group,instance.getGroup());

        //Test if rule has been sucessfully inserted
        assertEquals(rule,instance.getRule());

        //Test if tags have been sucessfully inserted
        assertEquals(tags,instance.getTags());

    }


}
