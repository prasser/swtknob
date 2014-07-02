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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
    
    /** The message*/
    private String message;
    /** The input text*/
    private String input;
    /** The scale*/
    private KnobScale<T> scale;
    /** The value*/
    private T value;
    
    /**
     * Creates a new dialog
     * @param parent
     * @param language
     * @param scale
     * @param value
     */
    public KnobInputDialog(Shell parent, KnobLanguageProfile language, KnobScale<T> scale, T value) {
        super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        setText(language.getTitle());
        String message = language.getMessage();
        if ((scale instanceof KnobScale.Character) || (scale instanceof KnobScale.Long) ||
            (scale instanceof KnobScale.Integer)) {
            message.replace("[type]", language.getInteger());
        } else {
            message.replace("[type]", language.getDecimal());
        }
        message.replace("[range]", "[" + scale.getMinimum() + ", " + scale.getMaximum() + "] ");
        this.message = message;
        this.scale = scale;
        this.value = value;
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
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        if (input == null) return null;
        else return getValue(input);
    }

    @SuppressWarnings("unchecked")
    private T getValue(String input) {
        
        try {
            if (scale instanceof KnobScale.Long){
                
                long min = ((KnobScale.Long)scale).getMinimum();
                long max = ((KnobScale.Long)scale).getMaximum();
                long value = Long.valueOf(input);
                if (value < min || value > max) return null;
                else return (T)Long.valueOf(value);
                
            } else if (scale instanceof KnobScale.Integer){
                
                int min = ((KnobScale.Integer)scale).getMinimum();
                int max = ((KnobScale.Integer)scale).getMaximum();
                int value = Integer.valueOf(input);
                if (value < min || value > max) return null;
                else return (T)Integer.valueOf(value);
                
            } else if (scale instanceof KnobScale.Character){
                
                char min = ((KnobScale.Character)scale).getMinimum();
                char max = ((KnobScale.Character)scale).getMaximum();
                char value = (char)Integer.valueOf(input).intValue();
                if (value < min || value > max) return null;
                else return (T)Character.valueOf(value);
                
            } else if (scale instanceof KnobScale.Float){
                
                float min = ((KnobScale.Float)scale).getMinimum();
                float max = ((KnobScale.Float)scale).getMaximum();
                float value = Float.valueOf(input);
                if (value < min || value > max) return null;
                else return (T)Float.valueOf(value);
                
            } else if (scale instanceof KnobScale.Double){
                
                double min = ((KnobScale.Double)scale).getMinimum();
                double max = ((KnobScale.Double)scale).getMaximum();
                double value = Double.valueOf(input);
                if (value < min || value > max) return null;
                else return (T)Double.valueOf(value);
            } 
        } catch (Exception e){
            /* Catch silently*/
        }
        return null;
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
        text.setText(String.valueOf(value));

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
                if (getValue(text.getText()) != null) {
                    ok.setEnabled(true);
                } else {
                    ok.setEnabled(false);
                }
            }
        });

        shell.setDefaultButton(ok);
    }
}
