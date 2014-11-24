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
 * This class represents all data necessary to acquire and display an image.
 *
 * @author Bojan Tomic
 */
public class ImageData implements Cloneable {

    /**
     * Image caption. The value for this attribute is optional.
     */
    private String caption;
    /**
     * URL to the image location.
     */
    private String URL;

    /**
     * This constructor sets the URL to the entered value by calling the 
     * "setURL" method. The "caption" attribute is set to null.
     * 
     * @param URL URL for locating the image
     */
    public ImageData(String URL) {
        setURL(URL);
        caption = null;
    }

    /**
     * This constructor sets the URL and caption to the entered values by 
     * calling the appropriate "set" methods.
     * 
     * @param URL URL for locating the image
     * @param caption image caption
     */
    public ImageData(String URL, String caption) {
        setURL(URL);
        setCaption(caption);
    }

    /**
     * Returns the image URL
     * 
     * @return string containing image URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * Sets the image URL.
     *
     * @param val String representing the image URL.
     * Since this value is mandatory, null and empty strings are not allowed.
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * if the entered argument is null or an empty string
     */
    public void setURL(String val) {
        if (val == null || val.length() == 0) {
            throw new ExplanationException("The URL cannot be null or an empty string value");
        }
        this.URL = val;
    }

    /**
     * Returns the image caption or null if no caption is specified.
     * 
     * @return string containing image caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the image caption.
     *
     * @param val String representing the image
     * caption. Since this value is optional, null and empty strings are 
     * allowed.
     */
    public void setCaption(String val) {
        this.caption = val;
    }

    /**
     * Performs a "deep" cloning operation for this class.
     *
     * @return object clone
     */
    @Override
    public ImageData clone() {
        return new ImageData(URL, caption);
    }
}

