/*
 * SWTKnob - A Knob Widget for SWT
 * Copyright (C) 2014 Fabian Prasser
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.linearbits.swt.widgets;

/**
 * A language profile for the input dialog. Will return
 * begin+decimal|integer+range+end
 * 
 * @author Fabian Prasser
 */
public class KnobLanguageProfile {

    /** Please enter*/
    private String begin = "Please enter";
    /**a decimal*/
    private String decimal = "a decimal";
    /**end of sentence*/
    private String end = "";
    /**an integer*/
    private String integer = "an integer";
    /**in range*/
    private String range = "in range";
    /** Input Dialog*/
    private String title = "Input Dialog";
    
    /**
     * In English: 'Please enter'
     * @return
     */
    public String getBegin() {
        return begin;
    }
    /**
     * In English: 'a decimal'
     * @return
     */
    public String getDecimal() {
        return decimal;
    }
    /**
     * In English: ':'
     * @return
     */
    public String getEnd() {
        return end;
    }
    /**
     * In English: 'an integer'
     * @return
     */
    public String getInteger() {
        return integer;
    }
    /**
     * In English: 'in range'
     * @return
     */
    public String getRange() {
        return range;
    }
    /**
     * In English: 'Input Dialog'
     * @return
     */
    public String getTitle() {
        return title;
    }
    

    /**
     * In English: 'Please enter'
     * @param text
     */
    public void setBegin(String text) {
        this.begin = text;
    }
    
    /**
     * In English: 'a decimal'
     * @param text
     */
    public void setDecimal(String text) {
        this.decimal = text;
    }
    
    /**
     * In English: ':'
     * @param text
     */
    public void setEnd(String text) {
        this.end = text;
    }
    
    /**
     * In English: 'an integer'
     * @param text
     */
    public void setInteger(String text) {
        this.integer = text;
    }

    /**
     * In English: 'in range'
     * @param text
     */
    public void setRange(String text) {
        this.range = text;
    }

    /**
     * In English: 'Input Dialog'
     * @param text
     */
    public void setTitle(String text) {
        this.title = text;
    }
    
    /**
     * Checks the settings
     */
    public void check(){

        if (begin==null) throw new NullPointerException("Text for 'begin' not specified");
        if (decimal==null) throw new NullPointerException("Text for 'begin' not specified");
        if (end==null) throw new NullPointerException("Text for 'begin' not specified");
        if (integer==null) throw new NullPointerException("Text for 'begin' not specified");
        if (range==null) throw new NullPointerException("Text for 'begin' not specified");
        if (title==null) throw new NullPointerException("Text for 'begin' not specified");
    }
    
    /**
     * Returns an English language profile
     * @return
     */
    public static KnobLanguageProfile createEnglishProfile(){
        return new KnobLanguageProfile();
    }

    /**
     * Returns an German language profile
     * @return
     */
    public static KnobLanguageProfile createGermanProfile(){
        KnobLanguageProfile profile = new KnobLanguageProfile();
        profile.setBegin("Bitte geben Sie");
        profile.setInteger("eine Ganzzahl");
        profile.setInteger("eine Gleitkommazahl");
        profile.setInteger("im Bereich");
        profile.setEnd("ein");
        profile.setTitle("Eingabedialog");
        return profile;
    }
}
