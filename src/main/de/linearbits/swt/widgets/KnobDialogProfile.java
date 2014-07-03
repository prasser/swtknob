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
public class KnobDialogProfile {
    
    /**
     * Returns an English language profile
     * @return
     */
    public static KnobDialogProfile createEnglishProfile(){
        return new KnobDialogProfile();
    }
    /**
     * Returns an German language profile
     * @return
     */
    public static KnobDialogProfile createGermanProfile(){
        KnobDialogProfile profile = new KnobDialogProfile();
        profile.setMessage("Bitte geben Sie eine [type] im Bereich [range] ein:");
        profile.setInteger("Ganzzahl");
        profile.setDecimal("Gleitkommazahl");
        profile.setTitle("Eingabemaske");
        return profile;
    }
    /** Message*/
    private String message = "Please enter [type] in range [range]:";
    /** Title*/
    private String title = "Input Dialog";
    
    /** Integer*/
    private String integer = "an integer";

    /** Decimal*/
    private String decimal = "a decimal";

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
     * Returns the text for the decimal datatype. Default is: "decimal"
     * @return
     */
    public String getDecimal() {
        return decimal;
    }

    /**
     * Returns the text for the integer datatype. Default is: "integer"
     * @return
     */
    public String getInteger() {
        return integer;
    }


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
     * Returns the title of the dialog
     * @return
     */
    public String getTitle() {
        return title;
    }


    /**
     * Sets the text for the decimal datatype. Default is: "decimal"
     * @return
     */
    public void setDecimal(String value) {
        checkNull(value);
        this.decimal = value;
    }

    /**
     * Sets the text for the integer datatype. Default is: "integer"
     * @return
     */
    public void setInteger(String value) {
        checkNull(value);
        this.integer = value;
    }

    /**
     * Sets the message. "[type]" will be replaced by a text for the datatype, defined
     * via setDecimal() and setInteger(), and "[range]" will be replaced by the range.
     * Default is:  "Please enter a [type] in range [range]:"
     * @return
     */
    public void setMessage(String value) {
        checkNull(value);
        this.message = value;
    }
    
    /**
     * Sets the title of the dialog. Default is: "Input Dialog"
     * @param title
     */
    public void setTitle(String value) {
        checkNull(value);
        this.title = value;
    }

    /**
     * Checks if the object is null
     * @param value
     */
    private void checkNull(Object value) {
        if (value == null) throw new NullPointerException("Argument is null");
    }
}
