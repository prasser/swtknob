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
 * A language profile for the input dialog.
 * 
 * @author Fabian Prasser
 */
public class KnobLanguageProfile {
    
    /** Message*/
    private String message = "Please enter a [type] in range [range]:";
    /** Title*/
    private String title = "Input Dialog";
    /** Integer*/
    private String integer = "integer";
    /** Decimal*/
    private String decimal = "decimal";
    
    /**
     * Returns the message. "[type]" will be replaced by a text for the datatype, defined
     * via setDecimal() and setInteger(), and "[range]" will be replaced by the range.
     * Default is:  "Please enter a [type] in range [range]:"
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message. "[type]" will be replaced by a text for the datatype, defined
     * via setDecimal() and setInteger(), and "[range]" will be replaced by the range.
     * Default is:  "Please enter a [type] in range [range]:"
     * @return
     */
    public void setMessage(String value) {
        if (value == null) throw new NullPointerException("Argument is null");
        this.message = value;
    }

    /**
     * Returns the title of the dialog
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the dialog. Default is: "Input Dialog"
     * @param title
     */
    public void setTitle(String value) {
        if (value == null) throw new NullPointerException("Argument is null");
        this.title = value;
    }

    /**
     * Returns the text for the integer datatype. Default is: "integer"
     * @return
     */
    public String getInteger() {
        return integer;
    }


    /**
     * Sets the text for the integer datatype. Default is: "integer"
     * @return
     */
    public void setInteger(String value) {
        if (value == null) throw new NullPointerException("Argument is null");
        this.integer = value;
    }


    /**
     * Returns the text for the decimal datatype. Default is: "decimal"
     * @return
     */
    public String getDecimal() {
        return decimal;
    }


    /**
     * Sets the text for the decimal datatype. Default is: "decimal"
     * @return
     */
    public void setDecimal(String value) {
        if (value == null) throw new NullPointerException("Argument is null");
        this.decimal = value;
    }

    /**
     * Checks the settings
     */
    public void check(){

        if (decimal==null) throw new NullPointerException("Text for 'decimal' not specified");
        if (integer==null) throw new NullPointerException("Text for 'integer' not specified");
        if (message==null) throw new NullPointerException("Text for 'message' not specified");
        if (title==null) throw new NullPointerException("Text for 'title' not specified");
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
        profile.setMessage("Bitte geben Sie eine [type] im Bereich [range] ein:");
        profile.setInteger("Ganzzahl");
        profile.setDecimal("Gleitkommazahl");
        profile.setTitle("Eingabemaske");
        return profile;
    }
}
