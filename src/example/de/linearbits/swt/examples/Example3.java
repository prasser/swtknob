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

package de.linearbits.swt.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobRange;

/**
 * A class with examples for using SWTKnob
 * 
 * @author Fabian Prasser
 */
public class Example3 {
    
    /**
     * Main entry point
     * @param args
     */
    public static void main(String[] args) {
        
        // Create display and shell
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT");
        shell.setSize(70, 150);
        shell.setLayout(new GridLayout(1, false));

        // Create color profiles
        KnobColorProfile defaultProfile = KnobColorProfile.createFocusedSystemProfile(display);
        KnobColorProfile focusedProfile = KnobColorProfile.createFocusedBlueRedProfile(display);

        // Create Knob
        final Knob<Long> knob1 = new Knob<Long>(shell, SWT.NULL, new KnobRange.Long(1l, 20l));
        GridData data1 = new GridData();
        data1.heightHint = 50;
        data1.widthHint = 50;
        knob1.setLayoutData(data1);
        knob1.setDefaultColorProfile(defaultProfile);
        knob1.setFocusedColorProfile(focusedProfile);
        
        // Create Knob
        Knob<Long> knob2 = new Knob<Long>(shell, SWT.NULL, new KnobRange.Long(1l, 20l));
        GridData data2 = new GridData();
        data2.heightHint = 50;
        data2.widthHint = 50;
        knob2.setLayoutData(data2);
        knob2.setDefaultColorProfile(defaultProfile);
        knob2.setFocusedColorProfile(focusedProfile);

        // Focus list
        shell.setTabList(new Control[]{knob1, knob2});

        // Open
        shell.open();

        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        
        if (!defaultProfile.isDisposed()) defaultProfile.dispose();
        if (!focusedProfile.isDisposed()) focusedProfile.dispose();
    }
}
