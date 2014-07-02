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

package de.linearbits.swt.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobScale;

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
        shell.setSize(70, 160);
        shell.setLayout(new GridLayout(1, false));

        // Create Knob
        final Knob<Long> knob1 = new Knob<Long>(shell, SWT.NULL, new KnobScale.Long(1l, 20l));
        GridData data1 = new GridData();
        data1.heightHint = 50;
        data1.widthHint = 50;
        knob1.setLayoutData(data1);
        
        // Create Knob
        Knob<Long> knob2 = new Knob<Long>(shell, SWT.NULL, new KnobScale.Long(1l, 20l));
        GridData data2 = new GridData();
        data2.heightHint = 50;
        data2.widthHint = 50;
        knob2.setLayoutData(data2);
        knob2.setDefaultColorProfile(KnobColorProfile.createDefaultProfile());
        knob2.setFocusedColorProfile(KnobColorProfile.createFocusedProfile());

        // Focus list
        shell.setTabList(new Control[]{knob1, knob2});

        // Open
        shell.open();

        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }
}
