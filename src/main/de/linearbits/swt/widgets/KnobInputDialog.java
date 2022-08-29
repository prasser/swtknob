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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * An input dialog for the knob
 * 
 * @author Fabian Prasser
 */
class KnobInputDialog<T> extends Dialog {

    /** The formatter*/
    private NumberFormat formatter;
    /** The message*/
    private String message;
    /** The input text*/
    private String input;
    /** The scale*/
    private KnobRange<T> scale;
    /** The value*/
    private T value;
    
    /**
     * Creates a new dialog
     * @param parent
     * @param language
     * @param scale
     * @param value
     */
    public KnobInputDialog(Shell parent, KnobDialogProfile language, KnobRange<T> scale, T value) {
        
        super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        this.setText(language.getTitle());
        
        this.scale = scale;
        this.value = value;
        
        String message = language.getMessage();
        if ((scale instanceof KnobRange.Character) || (scale instanceof KnobRange.Long) ||
            (scale instanceof KnobRange.Integer)) {
            message = message.replace("[type]", language.getInteger());
        } else {
            message = message.replace("[type]", language.getDecimal());
        }
        message = message.replace("[range]", "[" + toString(scale.getMinimum()) + ", " + toString(scale.getMaximum()) + "]");
        this.message = message;
    }
    
    /**
     * Opens the dialog. Returns null if canceled, the entered value otherwise
     * @return
     */
    public T open() {
        Shell shell = new Shell(getParent(), getStyle());
        shell.setText(getText());
        createContents(shell);
        shell.pack();
        shell.open();
        center(shell, getParent());
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        if (input == null) return null;
        else return toT(input);
    }

    /**
     * Centers the given shell.
     *
     * @param shell
     * @param parent
     */
    private void center(final Shell shell, final Shell parent) {

        final Rectangle bounds = parent.getBounds();
        final Point p = shell.getSize();
        final int left = (bounds.width - p.x) / 2;
        final int top = (bounds.height - p.y) / 2;
        shell.setBounds(left + bounds.x, top + bounds.y, p.x, p.y);
    }

    /**
     * Creates the dialogs contents
     * @param shell
     */
    private void createContents(final Shell shell) {
        shell.setLayout(new GridLayout(2, true));

        Label label = new Label(shell, SWT.NONE);
        label.setText(message);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        label.setLayoutData(data);

        final Text text = new Text(shell, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        text.setLayoutData(data);
        text.setText(toString(value));
        text.setSelection(text.getText().length());
        text.selectAll();

        final Button ok = new Button(shell, SWT.PUSH);
        ok.setText("OK");
        data = new GridData(GridData.FILL_HORIZONTAL);
        ok.setLayoutData(data);
        ok.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                input = text.getText();
                shell.close();
            }
        });

        Button cancel = new Button(shell, SWT.PUSH);
        cancel.setText("Cancel");
        data = new GridData(GridData.FILL_HORIZONTAL);
        cancel.setLayoutData(data);
        cancel.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                input = null;
                shell.close();
            }
        });

        text.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (toT(text.getText()) != null) {
                    ok.setEnabled(true);
                } else {
                    ok.setEnabled(false);
                }
            }
        });

        shell.setDefaultButton(ok);
    }

    /**
     * Translates a value to a string
     * @param value
     */
    private String toString(T value) {
        if (scale instanceof KnobRange.Long){
            long lvalue = (Long)value;
            if (formatter == null) return String.valueOf(lvalue);
            else return formatter.format(lvalue);
        } else if (scale instanceof KnobRange.Integer){
            int lvalue = (Integer)value;
            if (formatter == null) return String.valueOf(lvalue);
            else return formatter.format(lvalue);
        } else if (scale instanceof KnobRange.Character){
            int lvalue = (int)((Character)value).charValue();
            if (formatter == null) return String.valueOf(lvalue);
            else return formatter.format(lvalue);
        } else if (scale instanceof KnobRange.Float){
            float lvalue =(Float)value;
            if (formatter == null) return String.valueOf(lvalue);
            else return formatter.format(lvalue);
        } else if (scale instanceof KnobRange.Double){
            double lvalue =(Double)value;
            if (formatter == null) return String.valueOf(lvalue);
            else return formatter.format(lvalue);
        }  else {
            throw new IllegalStateException("Unknown type of scale: "+scale);
        }
    }

    @SuppressWarnings("unchecked")
    private T toT(String input) {
        
        try {
            if (scale instanceof KnobRange.Long){
                
                long min = ((KnobRange.Long)scale).getMinimum();
                long max = ((KnobRange.Long)scale).getMaximum();
                long value = formatter == null ? Long.valueOf(input) : formatter.parse(input).longValue();
                if (value < min || value > max) return null;
                else return (T)Long.valueOf(value);
                
            } else if (scale instanceof KnobRange.Integer){
                
                int min = ((KnobRange.Integer)scale).getMinimum();
                int max = ((KnobRange.Integer)scale).getMaximum();
                int value = formatter == null ? Integer.valueOf(input) : formatter.parse(input).intValue();
                if (value < min || value > max) return null;
                else return (T)Integer.valueOf(value);
                
            } else if (scale instanceof KnobRange.Character){
                
                char min = ((KnobRange.Character)scale).getMinimum();
                char max = ((KnobRange.Character)scale).getMaximum();
                char value = formatter == null ? (char)Integer.valueOf(input).intValue() : (char)formatter.parse(input).intValue();
                if (value < min || value > max) return null;
                else return (T)Character.valueOf(value);
                
            } else if (scale instanceof KnobRange.Float){
                
                float min = ((KnobRange.Float)scale).getMinimum();
                float max = ((KnobRange.Float)scale).getMaximum();
                float value = formatter == null ? Float.valueOf(input) : formatter.parse(input).floatValue();
                if (value < min || value > max) return null;
                else return (T)Float.valueOf(value);
                
            } else if (scale instanceof KnobRange.Double){
                
                double min = ((KnobRange.Double)scale).getMinimum();
                double max = ((KnobRange.Double)scale).getMaximum();
                double value = formatter == null ? Double.valueOf(input) : formatter.parse(input).doubleValue();
                if (value < min || value > max) return null;
                else return (T)Double.valueOf(value);
            } 
        } catch (Exception e){
            /* Catch silently*/
        }
        return null;
    }
}
