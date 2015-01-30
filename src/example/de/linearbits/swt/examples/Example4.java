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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import de.linearbits.swt.widgets.Knob;
import de.linearbits.swt.widgets.KnobRange;

/**
 * Example 4
 */
public class Example4 {

	private CTabFolder tabFolder;

	/**
	 * Runs the application
	 */
	public void run() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT");
		createContents(shell);
		shell.setSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Creates the window's contents
	 * 
	 * @param shell
	 *            the parent shell
	 */
	private void createContents(Shell shell) {
		shell.setLayout(new GridLayout(1, true));

		// Create the buttons to create tabs
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new RowLayout());

		// Create the tabs
		tabFolder = new CTabFolder(shell, SWT.TOP);
		tabFolder.setBorderVisible(true);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		CTabItem item1 = new CTabItem(tabFolder, SWT.NONE);
		item1.setText("Tab-1");
		
		Group group = new Group(tabFolder, SWT.NONE);
		group.setText("Knob-1");
		group.setLayout(new FillLayout());
		item1.setControl(group);
		
        // Create Knob
        new Knob<Integer>(group, SWT.NULL, new KnobRange.Integer(0, 10));
	}

	/**
	 * The application entry point
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		new Example4().run();
	}
}
