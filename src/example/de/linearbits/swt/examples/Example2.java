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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobRange;

/**
 * A class with examples for using SWTKnob
 * 
 * @author Fabian Prasser
 */
public class Example2 {
    
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

        // Create Knob
        final Knob<Integer> knob = new Knob<Integer>(shell, SWT.NULL, new KnobRange.Integer(20,60));
        knob.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Create Label
        final Label label = new Label(shell, SWT.NULL);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        // Attach
        knob.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                label.setText("Value: "+String.valueOf(knob.getValue()));
            }
        });
        

        // Open
        shell.open();

        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }
}
