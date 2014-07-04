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

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobColorProfile;
import de.linearbits.swt.widgets.KnobDialogProfile;
import de.linearbits.swt.widgets.KnobRange;

/**
 * A class with examples for using SWTKnob
 * 
 * @author Fabian Prasser
 */
public class Example1 {
    
    /**
     * Main entry point
     * @param args
     */
    public static void main(String[] args) {

        // Create display and shell
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT");
        shell.setSize(230, 500);
        shell.setLayout(new GridLayout(1, false));
        
        // Create profiles
        KnobColorProfile defaultColorProfile = KnobColorProfile.createDefaultBlueRedProfile();
        KnobColorProfile focusedColorProfile = KnobColorProfile.createFocusedBlueRedProfile();
        KnobDialogProfile dialogProfile = KnobDialogProfile.createEnglishProfile().setFormatter(new DecimalFormat("0.00000"));

        // Create Knob
        final Knob<Double> knob = new Knob<Double>(shell, SWT.NULL, new KnobRange.Double(0d, 1d));
        GridData data = new GridData();
        data.heightHint = 200;
        data.widthHint = 200;
        knob.setLayoutData(data);
        knob.setDefaultColorProfile(defaultColorProfile);
        knob.setFocusedColorProfile(focusedColorProfile);
        knob.setDialogProfile(dialogProfile);

        // Create Knob
        final Knob<Integer> knob0 = new Knob<Integer>(shell, SWT.NULL, new KnobRange.Integer(1, 20));
        GridData data0 = new GridData();
        data0.heightHint = 100;
        data0.widthHint = 100;
        knob0.setLayoutData(data0);
        knob0.setDefaultColorProfile(defaultColorProfile);
        knob0.setFocusedColorProfile(focusedColorProfile);
        knob0.setDialogProfile(dialogProfile);

        // Create Knob
        final Knob<Long> knob1 = new Knob<Long>(shell, SWT.NULL, new KnobRange.Long(1l, 20l));
        GridData data1 = new GridData();
        data1.heightHint = 50;
        data1.widthHint = 50;
        knob1.setLayoutData(data1);
        knob1.setDefaultColorProfile(defaultColorProfile);
        knob1.setFocusedColorProfile(focusedColorProfile);
        knob1.setDialogProfile(dialogProfile);

        // Create Knob
        Knob<Character> knob2 = new Knob<Character>(shell, SWT.NULL, new KnobRange.Character());
        GridData data2 = new GridData();
        data2.heightHint = 30;
        data2.widthHint = 30;
        knob2.setLayoutData(data2);
        knob2.setDefaultColorProfile(defaultColorProfile);
        knob2.setFocusedColorProfile(focusedColorProfile);
        knob2.setDialogProfile(dialogProfile);

        // Create Knob
        Knob<Float> knob3 = new Knob<Float>(shell, SWT.NULL, new KnobRange.Float(0f, 1f));
        GridData data3 = new GridData();
        data3.heightHint = 23;
        data3.widthHint = 23;
        knob3.setLayoutData(data3);
        knob3.setDefaultColorProfile(defaultColorProfile);
        knob3.setFocusedColorProfile(focusedColorProfile);
        knob3.setDialogProfile(dialogProfile);

        // Create Knob
        Knob<Integer> knob4 = new Knob<Integer>(shell, SWT.NULL, new KnobRange.Integer(0, 9999));
        GridData data4 = new GridData();
        data4.heightHint = 10;
        data4.widthHint = 10;
        knob4.setLayoutData(data4);
        knob4.setDefaultColorProfile(defaultColorProfile);
        knob4.setFocusedColorProfile(focusedColorProfile);
        knob4.setDialogProfile(dialogProfile);

        // Bind knob0 with knob1
        knob1.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                knob0.setValue(knob1.getValue().intValue());
            }
        });
        knob0.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                knob1.setValue((long)knob0.getValue());
            }
        });

        // Focus list
        shell.setTabList(new Control[]{knob, knob0, knob1, knob2, knob3, knob4});

        // Open
        shell.open();

        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        
        // Dispose profiles
        defaultColorProfile.dispose();
        focusedColorProfile.dispose();
    }
}
