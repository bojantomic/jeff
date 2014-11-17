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


/**
 * The class that implements this interface is supposed to provide 
 * references to the concrete explanation chunk builder instances for the 
 * appropriate explanation chunk type.
 *
 * @author Bojan Tomic
 */
public interface ExplanationChunkBuilderFactory {

    /**
     * Represents the text explanation chunk
     */
    public static final int TEXT = 0;

    /**
     * Represents the data explanation chunk
     */
    public static final int DATA = 1;

    /**
     * Represents the image explanation chunk
     */
    public static final int IMAGE = 2;

    /**
     * This method should return a concrete explanation chunk builder instance 
     * for the entered explanation chunk type.
     *
     * @param type explanation chunk type for which a builder is needed 
     * (concrete values are represented as static constants TEXT, DATA and 
     * IMAGE)
     * 
     * @return the appropriate explanation chunk builder instance
     * for the entered chunk type
     */
    public ExplanationChunkBuilder getExplanationChunkBuilder (int type);

}

