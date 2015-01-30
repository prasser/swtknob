/* ******************************************************************************
 * Copyright (c) 2014 - 2015 Fabian Prasser.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Fabian Prasser - initial API and implementation
 ******************************************************************************/

package de.linearbits.swt.widgets;

import java.text.NumberFormat;

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
    /** Formatter*/
    private NumberFormat formatter = null;

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
     * Returns the current formatter
     * @return
     */
    public NumberFormat getFormatter() {
        return formatter;
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
    public KnobDialogProfile setDecimal(String value) {
        checkNull(value);
        this.decimal = value;
        return this;
    }

    /**
     * Sets the formatter used for input/output
     * @return
     */
    public KnobDialogProfile setFormatter(NumberFormat formatter) {
        checkNull(formatter);
        this.formatter = formatter;
        return this;
    }

    /**
     * Sets the text for the integer datatype. Default is: "integer"
     * @return
     */
    public KnobDialogProfile setInteger(String value) {
        checkNull(value);
        this.integer = value;
        return this;
    }

    /**
     * Sets the message. "[type]" will be replaced by a text for the datatype, defined
     * via setDecimal() and setInteger(), and "[range]" will be replaced by the range.
     * Default is:  "Please enter a [type] in range [range]:"
     * @return
     */
    public KnobDialogProfile setMessage(String value) {
        checkNull(value);
        this.message = value;
        return this;
    }
    
    /**
     * Sets the title of the dialog. Default is: "Input Dialog"
     * @param title
     */
    public KnobDialogProfile setTitle(String value) {
        checkNull(value);
        this.title = value;
        return this;
    }

    /**
     * Checks if the object is null
     * @param value
     */
    private void checkNull(Object value) {
        if (value == null) throw new NullPointerException("Argument is null");
    }
}
