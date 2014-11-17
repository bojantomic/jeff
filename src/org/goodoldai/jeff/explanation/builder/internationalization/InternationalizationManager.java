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
package org.goodoldai.jeff.explanation.builder.internationalization;

import org.goodoldai.jeff.explanation.ExplanationException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Locale;
import java.text.MessageFormat;

/**
 * This class represents a focus point for all internationalization 
 * features.
 *
 * The idea is to enable making expert system explanations in different
 * languages. In order to do this, all image captions, units, dimension 
 * names and paragraphs (text) need to be translated and transformed before 
 * they can be inserted into the explanation. This class does this by using 
 * Java internationization classes such as java.util.Locale and 
 * java.util.ResourceBundle. Also, it can be noted that, at this point, 
 * this class is implemented as a Singleton.
 *
 * @author Bojan Tomic
 */
public class InternationalizationManager {

    /**
     * manager instance (reference to it should be acquired through the static 
     * method "getManager")
     */
    private static InternationalizationManager manager;
    /**
     * Class lock needed for synchronization
     */
    private static final Object classlock = InternationalizationManager.class;
    /**
     * Current locale for the manager instance
     */
    private Locale locale;
    private ResourceBundle units = null;
    private ResourceBundle dimensionNames = null;
    private ResourceBundle imageCaptions = null;
    private HashMap<String, ResourceBundle> text;

    /**
     * Creates a manager instance by opening appropriate property files and 
     * creating java.util.ResourceBundle instances. Since this is an 
     * implementation of the Singleton pattern, this constructor is protected 
     * in order to prevent initialization of multiple 
     * InternationalizationManager instances.
     *
     * First a ResourceBundle containing unit translations is created from a
     * properties file named "units_lang_coun.properties" where "lang" and "coun"
     * are concrete language and country from the entered java.util.Locale
     * instance. For example, "en" "US" locale (english, United States) unit
     * translations should be in a file named "units_en_US.properties",
     * "en" "GB" locale (english, Great Britain) unit translations should be in
     * a file named "units_en_GB.properties" and
     * "fr" "FR" locale (french, France) unit translations should be in a file
     * named "units_fr_FR.properties".
     *
     * Then, a ResourceBundle containing dimension name translations is created
     * from a properties file named "dimension_names_lang_coun.properties" where
     * "lang" and "coun" are concrete language and country from the entered
     * java.util.Locale instance.
     *
     * Finally, a ResourceBundle containing image caption translations is
     * created from a properties file named 
     * "image_captions_lang_coun.properties" where "lang" and "coun"
     * are concrete language and country from the entered
     * java.util.Locale instance.
     *
     * The final step is to initialize the collection that will contain various
     * java.util.ResourceBundle instances that refer to text translations. The 
     * collection is supposed to be empty at this point and will be populated 
     * when text translations are needed, i.e. when calls to "translateText"
     * method are made.
     *
     * All of the property files mentioned here need to be somewhere in the
     * classpath in order for the initialization to work.
     *
     * @param locale locale that is supposed to be used when translating text,
     * captions, units etc.
     * 
     * @throws explanation.ExplanationException
     * if the entered locale instance is null
     * or if the appropriate properties files cannot be found in the classpath
     */
    protected InternationalizationManager(Locale locale) {
        //Check the locale
        if (locale == null) {
            throw new ExplanationException("The entered locale cannot be null");
        }

        //Open the properties files and retrieve internationalization resources
        //All exceptions are wrapped into an ExplanationException instance
        try {
            units = ResourceBundle.getBundle("units", locale);
            dimensionNames = ResourceBundle.getBundle("dimension_names", locale);
            imageCaptions = ResourceBundle.getBundle("image_captions", locale);
            this.locale = locale;
        } catch (Exception e) {
            throw new ExplanationException(e.getMessage());
        }

        //Initalize the resource bundle list for text translations
        text = new HashMap<String, ResourceBundle>();
    }

    /**
     * This static method initializes one InternationalizationManager instance
     * as a static variable. This is an implementation of the Singleton 
     * pattern, so all subsequent calls to this method have no effect.
     *
     * This method is thread-safe.
     *
     * @param locale locale that is supposed to be used when translating text,
     * captions, units etc.
     */
    public static void initializeManager(Locale locale) {
        synchronized (classlock) {
            if (manager == null) {
                manager = new InternationalizationManager(locale);
            }
        }
    }

    /**
     * This static method returns the reference to the 
     * InternationalizationManager instance. This is an implementation of the 
     * Singleton pattern, so all calls to this method return the reference to 
     * the same instance.
     *
     * @return InternationalizationManager instance
     * 
     * @throws explanation.ExplanationException
     * if the manager instance has not yet been initialized
     */
    public static InternationalizationManager getManager() {
        if (manager != null) {
            return manager;
        } else {
            throw new ExplanationException("The internationalization manager has not yet been initialized");
        }
    }

    /**
     * This method returns the textual part of the explanation for the executed 
     * rule in the desired language and country.
     *
     * When a rule gets executed, the text concerning the explanation about
     * this new inference needs to be inserted into the explanation. Since this 
     * explanation piece can be lengthy (few paragraphs), the idea is not to 
     * enter this text as an input parameter (string value) but rather retrieve 
     * it from a properties file via the appropriate java.util.ResourceBundle 
     * instance (see the Java internationalization feature).
     *
     * Each rule can belong to a rule group (optionally) and has a unique
     * identifier (name). So, in order to find explanation text concerning a 
     * particular rule, the rule identifier and group to which it belongs to 
     * are needed. Property files that contain this text are organized by group 
     * name, and the keys are rule identifiers. For example, if the rule group 
     * name is "bicycle failures" and the rule identifier is "rule 1" the
     * explanation regarding this rule could be found in the 
     * "bicycle_failures_text_lang_coun.properties" file (note that the
     * blank spaces have been replaced with underscores) under the
     * "rule 1" key. Note that "lang" and "coun" are concrete
     * language and country from the java.util.Locale instance entered at
     * manager initialization. If the group name is omitted (null) or is an empty
     * string value the default "text_lang_coun.properties" file is looked
     * up and loaded. All blank spaces at the begining and at the end of the
     * group name are trimmed, so "  group 1 " would be treated the same as
     * "group 1".
     *
     * Explanation text can have some variable values inserted into certain
     * places within the text itself. These variables are entered as an array 
     * of Object. All details concerning this formatting can be found in the
     * java.text.MessageFormat class.
     *
     * This method is thread-safe.
     *
     * @param group the group to which this rule belongs to
     * @param rule rule identifier (or name)
     * @param arguments array of objects which will be
     * inserted at specified positions in the translated text
     *
     * @return translated text or null if the rule explanation text could not
     * be found
     * 
     * @throws explanation.ExplanationException
     * if the properties file for the rule group could not be found in
     * the classpath, or if the rule identifier is null or an empty string
     */
    public String translateText(String group, String rule, Object[] arguments) {
        if (rule == null || rule.equals("")) {
            throw new ExplanationException("You must enter a non-null, non-empty rule identifier");
        }

        //Synchronization is needed because new resource bundles may
        //be loaded at this point
        synchronized (classlock) {
            //First locate the appropriate resource bundle.
            ResourceBundle translationBundle = null;

            //Try to see if the resource bundle for that rule group is
            //already loaded and cached into the "text" HashMap. Group name
            //is the map key.
            if (text.containsKey(group)) {
                translationBundle = text.get(group);
            }

            //If it cannot be found in this way, create a new bundle from a
            //properties file and put it in the HashMap with the group name
            //as its key.
            if (translationBundle == null) {

                //Compound resource bundle properties file base name
                String resourceBundleFile = "text";

                //Trim group name, replace blank spaces with underscores and
                //create compound base name.
                if (!(group == null || group.equals(""))) {
                    resourceBundleFile = group.trim().replace(' ', '_') + "_text";
                }

                //Load resource bundle from a file and put into a map
                try {
                    translationBundle = ResourceBundle.getBundle(resourceBundleFile, locale);
                    text.put(group, translationBundle);
                } catch (Exception e) {
                    throw new ExplanationException(e.getMessage());
                }

            }

            //Create and return translation. If no translation can be found,
            //return null
            if (!(translationBundle.containsKey(rule))) {
                return null;
            } else {
                if (arguments == null) {
                    return translationBundle.getString(rule);
                } else {
                    //Create a MessageFormat object that uses the desired locale
                    //and not the default locale (for that machine).
                    MessageFormat mf = new MessageFormat(translationBundle.getString(rule), locale);

                    //Translate the text while inserting all arguments
                    return mf.format(arguments, new StringBuffer(), null).toString();
                }
            }
        }


    }

    /**
     * Substitutes the entered unit name with an equivalent unit name for the 
     * desired language and country.
     *
     * For example, "m" (meter) can be substituted with "ft" (foot). The
     * substitution is performed by using the entered unit name as a key for 
     * the key-value pair contained in the "units" ResourceBundle (which is
     * initialized from a properties file - see Java internationalization 
     * feature). The value represents the desired substitute. For example the 
     * key-value pair (m,ft) from the ResourceBundle is represented in the 
     * properties file in a single line: m = ft.
     *
     * Note that it is left to
     * the developer to define which unit should be substituted and which 
     * shouldn't and what should the substitution be. Also note that this is a 
     * substitution of the unit name, and no data value transformations are 
     * being performed (e.g. distance is not being converted from meters to 
     * feet).
     * 
     * @param unit string value representing the unit name
     *
     * @return substituion unit name for the desired language and country, or
     * null if no substitution could be found (the appropriate key-value pair 
     * could not be found)
     */
    public String translateUnit(String unit) {
        String substitute = null;

        try {
            substitute = units.getString(unit);
        } catch (Exception e) {
        }

        return substitute;
    }

    /**
     * Translates the entered dimension name for the desired language and 
     * country.
     *
     * For example, "distance" (english) can be translated as "distanz"
     * (german). The translation is performed by using the entered dimension 
     * name as a key for the key-value pair contained in the "dimensionNames"
     * ResourceBundle (which is initialized from a properties file - see Java 
     * internationalization feature). The value represents the desired 
     * translation. For example the key-value pair (distance,distanz) from the 
     * ResourceBundle is represented in the properties file in a single line: 
     * distance = distanz.
     *
     * Note that it is left to the developer to
     * define the translations and decide if the translations should be 
     * performed in the first place.
     * 
     * @param name string value representing the dimension name
     *
     * @return translated dimension name for the desired language and country,
     * or null if no translation could be found (the appropriate key-value pair 
     * could not be found)
     */
    public String translateDimensionName(String name) {
        String translation = null;

        try {
            translation = dimensionNames.getString(name);
        } catch (Exception e) {
        }

        return translation;
    }

    /**
     * Translates the entered image caption for the desired language and country.
     *
     * For example, "Whale photo" (english) can be translated as "Wal foto"
     * (german). The translation is performed by using the entered dimension 
     * name as a key for the key-value pair contained in the "imageCaptions"
     * ResourceBundle (which is initialized from a properties file - see Java 
     * internationalization feature). The value represents the desired 
     * translation. For example the key-value pair (Whale photo,Wal foto) from 
     * the ResourceBundle is represented in the properties file in a single 
     * line: Whale photo = Wal foto.
     * 
     * @param caption string value representing the image caption
     *
     * @return translated image caption for the desired language and country, or
     * null if no translation could be found (the appropriate key-value pair 
     * could not be found)
     */
    public String translateImageCaption(String caption) {
        String translation = null;

        try {
            translation = imageCaptions.getString(caption);
        } catch (Exception e) {
        }

        return translation;
    }
}

