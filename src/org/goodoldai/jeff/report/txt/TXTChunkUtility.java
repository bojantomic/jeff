/*
 * Copyright 2009 Nemanja Jovanovic
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
package org.goodoldai.jeff.report.txt;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

/**
 * This class is used to do the utility jobs that are common for TXT report
 * generation purposes.
 *
 * @author Nemanja jovanovic
 */
public class TXTChunkUtility {

    /**
     * In order to prevent initialization, the constructor is private
     */
    private TXTChunkUtility() {
    }

    /**
     * This method inserts the general explanation information (context, rule,
     * group, tags) into the txt document.
     *
     * @param echunk explanation chunk that holds the information that needs to be inserted
     * @param writer A java.io.PrintWriter instance in which the content of the
     * transformed chunk will be written in as a txt document
     */
    public static void insertExplanationInfo(ExplanationChunk echunk, PrintWriter writer) {

        int cont = echunk.getContext();
        String context = translateContext(cont, echunk);

        String rule = echunk.getRule();
        String group = echunk.getGroup();
        String[] tags = echunk.getTags();

        writer.write("The context is: " + context + "\n");

        if (rule !=null){
            writer.write("The rule that initiated the creation of this chunk: " + rule + "\n" );
        }

        if (group != null){
            writer.write("The group to which the executed rule belongs: " + group + "\n");
        }

        if (tags != null){
            writer.write("The tags are: " );
            for (int i = 0; i < tags.length; i++){
                writer.write(tags[i] + " ");
            }
            writer.write("\n");
        }
    }

    /**
     * This is a method that is used to translate the context from an integer
     * into a String. This method uses reflection in order to do this.
     *
     * @param context the int representation of explanation context
     * @param echunk explanation chunk that holds the string value of the context
     *
     * @return the string value of the context
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException
     * In the case of any problems by covering raised IllegalArgumentException or
     * IllegalAccessException.
     */
    public static String translateContext(int context, ExplanationChunk echunk) {

        Class cl = echunk.getClass();
        Field fields[] = cl.getFields();

        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                if (field.getInt(field.getName()) == context) {
                    return field.getName().toLowerCase();
                }
            } catch (IllegalArgumentException ex) {
                throw new ExplanationException(ex.getMessage());
            } catch (IllegalAccessException ex) {
                throw new ExplanationException(ex.getMessage());
            }
        }
        return String.valueOf(context);
    }
}
