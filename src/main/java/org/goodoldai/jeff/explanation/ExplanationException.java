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
 * Represents a custom exception class for this package. It is an unchecked
 * exception and subclasses java.lang.RuntimeException.
 *
 * @author Bojan Tomic
 */
public class ExplanationException extends java.lang.RuntimeException {

    /**
     * This constructor sets the exception message to the input parameter.
     *
     * @param message exception message
     */
    public ExplanationException (String message) {
        super(message);
    }

}

