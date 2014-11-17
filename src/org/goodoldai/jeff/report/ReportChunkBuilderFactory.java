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
package org.goodoldai.jeff.report;

import org.goodoldai.jeff.explanation.ExplanationChunk;

/**
 * The class that implements this interface is supposed to provide
 * references to the concrete report chunk builder instances for the 
 * appropriate chunk type and report type.
 *
 * @author Bojan Tomic
 */
public interface ReportChunkBuilderFactory {

    /**
     * This method should return a concrete report chunk builder instance for 
     * the entered chunk and report type.
     *
     * @param echunk a concrete ExplanationChunk for which a report chunk
     * builder is needed
     *
     * @return the appropriate chunk builder instance
     * for the entered chunk and report type
     *
     * @throws explanation.ExplanationException if the appropriate chunk builder
     * for the entered chunk could not be found
     */
    public ReportChunkBuilder getReportChunkBuilder (ExplanationChunk echunk);

}

