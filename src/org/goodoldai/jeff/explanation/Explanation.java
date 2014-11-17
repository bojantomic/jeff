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

/**
 * This class represents an (expert system) explanation. Generally, 
 * explanations are formed gradually during the ongoing (expert system)
 * session.
 * 
 * Each explanation has some attributes that identify it: owner, date and 
 * time created, language and country. The last two are used for translating
 * the explanation into the appropriate language for the desired country
 * and are optional.
 * 
 * Also, it consists of a series of explanation chunks (similar to paragraphs)
 * which can contain different types of content, for example: text, data,
 * images, etc.
 *
 * @author Bojan Tomic
 */
public class Explanation {

    /**
     * The date and time in which the explanation was created (when the object 
     * itself was created).
     */
    private java.util.GregorianCalendar created;
    /**
     * Specifies the owner of the explanation through an identifier string
     * (owner is the person for whom the explanation is intended for).
     */
    private String owner;
    /**
     * Language in which the explanation is written (see Java internationalization
     * feature and java.util.Locale class).
     */
    private String language;
    /**
     * Country for which the explanation is written (see Java internationalization
     * feature and java.util.Locale class).
     */
    private String country;
    /**
     * A list of chunks representing different parts of the explanation.
     */
    private ArrayList<ExplanationChunk> chunks;

    /**
     * This constructor sets the "created" attribute to the current date and
     * time. All other attributes are set to null.
     */
    public Explanation() {
        this.created = new java.util.GregorianCalendar();
        this.language = null;
        this.country = null;
        this.owner = null;
        this.chunks = new ArrayList<ExplanationChunk>();
    }

    /**
     * This constructor sets the "created" attribute to the current date and
     * time, and all other attributes to argument values.
     * 
     * @param owner explanation owner
     * @param language explanation language
     * @param country explanation country
     */
    public Explanation(String owner, String language, String country) {
        this.created = new java.util.GregorianCalendar();
        this.language = language;
        this.country = country;
        this.owner = owner;
        this.chunks = new ArrayList<ExplanationChunk>();
    }

    /**
     * This constructor sets the "created" attribute to the current date and
     * time. Also, the "owner" attribute is set to argument value. All other
     * attributes are set to null.
     *
     * @param owner explanation owner
     */
    public Explanation(String owner) {
        this.created = new java.util.GregorianCalendar();
        this.language = null;
        this.country = null;
        this.owner = owner;
        this.chunks = new ArrayList<ExplanationChunk>();
    }

    /**
     * Returns all of the chunks contained in the explanation.
     *
     * @return list of copies (clones) of the chunks contained in the
     * explanation.
     */
    public ArrayList<ExplanationChunk> getChunks() {
        ArrayList<ExplanationChunk> list =
                new ArrayList<ExplanationChunk>();

        //Creating a copy (clone) list of chunks
        for (int i = 0; i < chunks.size(); i++) {
            list.add(chunks.get(i).clone());
        }

        return list;
    }

    /**
     * Adds a chunk to the chunks list.
     *
     * @param chunk
     * the chunk to be added to the chunks list.
     * 
     * @throws explanation.ExplanationException
     * if the chunk is null.
     */
    public void addChunk(ExplanationChunk chunk) {
        if (chunk == null) {
            throw new ExplanationException("The entered chunk must not be null");
        }

        chunks.add(chunk);
    }

    /**
     * Returns the date and time when the report was created.
     * 
     * @return copy (clone) value for the "created" attribute
     */
    public java.util.GregorianCalendar getCreated() {
        java.util.GregorianCalendar createdclone =
                new java.util.GregorianCalendar();
        createdclone.setTimeInMillis(created.getTimeInMillis());

        return createdclone;
    }

    /**
     * Returns the language in which the explanation is written (see Java
     * internationalization feature and java.util.Locale class)
     * 
     * @return string representing the explanation language
     * or null if the language is not specified
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Returns the country for which the explanation is written (see Java
     * internationalization feature and java.util.Locale class)
     *
     * @return string representing the
     * explanation country or null if the country is not specified
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the explanation owner identifer
     * 
     * @return string identifying the owner or
     * null if the owner is not specified
     */
    public String getOwner() {
        return owner;
    }
}

