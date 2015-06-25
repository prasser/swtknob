package de.linearbits.swt.test;

/*
 * Copyright (c) 2000, 2002 IBM Corp. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.linearbits.swt.examples.Example1;

public class Sleak {
    Display  display;
    Shell    shell;
    List     list;
    Canvas   canvas;
    Button   start, stop, check, analyze;
    Text     text;
    Label    label;
    
    Object[] oldObjects = new Object[0];
    Error[]  oldErrors  = new Error[0];
    Object[] objects    = new Object[0];
    Error[]  errors     = new Error[0];
    
    public void open() {
        display = Display.getCurrent();
        shell = new Shell(display);
        shell.setText("S-Leak");
        list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
        list.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                refreshObject();
            }
        });
        text = new Text(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        canvas = new Canvas(shell, SWT.BORDER);
        canvas.addListener(SWT.Paint, new Listener() {
            public void handleEvent(Event event) {
                paintCanvas(event);
            }
        });
        check = new Button(shell, SWT.CHECK);
        check.setText("Stack");
        check.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                toggleStackTrace();
            }
        });
        start = new Button(shell, SWT.PUSH);
        start.setText("Snap");
        start.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                refreshAll();
            }
        });
        stop = new Button(shell, SWT.PUSH);
        stop.setText("Diff");
        stop.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                refreshDifference();
            }
        });
        
        analyze = new Button(shell, SWT.PUSH);
        analyze.setText("Analyze");
        analyze.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                analyze();
            }
        });
        label = new Label(shell, SWT.BORDER);
        label.setText("0 object(s)");
        shell.addListener(SWT.Resize, new Listener() {
            public void handleEvent(Event e) {
                layout();
            }
        });
        check.setSelection(false);
        text.setVisible(false);
        Point size = shell.getSize();
        shell.setSize(size.x / 2, size.y / 2);
        shell.open();
    }
    
    void refreshLabel() {
        @SuppressWarnings("unused")
        int colors = 0, cursors = 0, fonts = 0, gcs = 0, images = 0, regions = 0;
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object instanceof Color) colors++;
            if (object instanceof Cursor) cursors++;
            if (object instanceof Font) fonts++;
            if (object instanceof GC) gcs++;
            if (object instanceof Image) images++;
            if (object instanceof Region) regions++;
        }
        String string = "";
        if (colors != 0) string += colors + " Color(s)\n";
        if (cursors != 0) string += cursors + " Cursor(s)\n";
        if (fonts != 0) string += fonts + " Font(s)\n";
        if (gcs != 0) string += gcs + " GC(s)\n";
        if (images != 0) string += images + " Image(s)\n";
        /* Currently regions are not counted. */
        // if (regions != 0) string += regions + " Region(s)\n";
        if (string.length() != 0) {
            string = string.substring(0, string.length() - 1);
        }
        label.setText(string);
    }
    
    void analyze() {
        refreshAll();
        
        Map<String, Object> map = new HashMap<String, Object>();
        Map<Object, Integer> mapIDX = new HashMap<Object, Integer>();
        final Map<Object, Integer> mapTimes = new HashMap<Object, Integer>();
        
        for (int i = 0; i < objects.length; i++) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PrintStream s = new PrintStream(stream);
            errors[i].printStackTrace(s);
            String stackTrace = stream.toString();
            if (!map.containsKey(stackTrace)) {
                map.put(stackTrace, objects[i]);
                mapIDX.put(objects[i], i);
                mapTimes.put(objects[i], 1);
            } else {
                Object object = map.get(stackTrace);
                mapTimes.put(object, mapTimes.get(object) + 1);
            }
        }
        
        final Object[] equalObjects = new Object[map.size()];
        final Error[] equalErrors = new Error[map.size()];
        
        int idx = 0;
        for (Entry<String, Object> entry : map.entrySet()) {
            equalObjects[idx++] = entry.getValue();
        }
        
        Arrays.sort(equalObjects, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return mapTimes.get(o2) - mapTimes.get(o1);
            }
        });
        
        for (int i = 0; i < equalErrors.length; i++) {
            equalErrors[i] = errors[mapIDX.get(equalObjects[i])];
        }
        
        check.setSelection(true);
        list.removeAll();
        text.setText("");
        canvas.redraw();
        for (int i = 0; i < equalObjects.length; i++) {
            list.add(objectName(equalObjects[i]) + "(" + mapTimes.get(equalObjects[i]) + ")");
        }
        
        objects = equalObjects;
        errors = equalErrors;
        
        refreshLabel();
        layout();
        
    }
    
    void refreshDifference() {
        DeviceData info = display.getDeviceData();
        if (!info.tracking) {
            MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
            dialog.setText(shell.getText());
            dialog.setMessage("Warning: Device is not tracking resource allocation");
            dialog.open();
        }
        Object[] newObjects = info.objects;
        Error[] newErrors = info.errors;
        Object[] diffObjects = new Object[newObjects.length];
        Error[] diffErrors = new Error[newErrors.length];
        int count = 0;
        for (int i = 0; i < newObjects.length; i++) {
            int index = 0;
            while (index < oldObjects.length) {
                if (newObjects[i] == oldObjects[index]) break;
                index++;
            }
            if (index == oldObjects.length) {
                diffObjects[count] = newObjects[i];
                diffErrors[count] = newErrors[i];
                count++;
            }
        }
        objects = new Object[count];
        errors = new Error[count];
        System.arraycopy(diffObjects, 0, objects, 0, count);
        System.arraycopy(diffErrors, 0, errors, 0, count);
        list.removeAll();
        text.setText("");
        canvas.redraw();
        for (int i = 0; i < objects.length; i++) {
            list.add(objectName(objects[i]));
        }
        refreshLabel();
        layout();
    }
    
    String objectName(Object object) {
        String string = object.toString();
        int index = string.lastIndexOf('.');
        if (index == -1) return string;
        return string.substring(index + 1, string.length());
    }
    
    void toggleStackTrace() {
        refreshObject();
        layout();
    }
    
    void paintCanvas(Event event) {
        canvas.setCursor(null);
        int index = list.getSelectionIndex();
        if (index == -1) return;
        GC gc = event.gc;
        Object object = objects[index];
        if (object instanceof Color) {
            if (((Color) object).isDisposed()) return;
            gc.setBackground((Color) object);
            gc.fillRectangle(canvas.getClientArea());
            return;
        }
        if (object instanceof Cursor) {
            if (((Cursor) object).isDisposed()) return;
            canvas.setCursor((Cursor) object);
            return;
        }
        if (object instanceof Font) {
            if (((Font) object).isDisposed()) return;
            gc.setFont((Font) object);
            FontData[] array = gc.getFont().getFontData();
            String string = "";
            String lf = text.getLineDelimiter();
            for (int i = 0; i < array.length; i++) {
                FontData data = array[i];
                String style = "NORMAL";
                int bits = data.getStyle();
                if (bits != 0) {
                    if ((bits & SWT.BOLD) != 0) style = "BOLD ";
                    if ((bits & SWT.ITALIC) != 0) style += "ITALIC";
                }
                string += data.getName() + " " + data.getHeight() + " " + style + lf;
            }
            gc.drawString(string, 0, 0);
            return;
        }
        // NOTHING TO DRAW FOR GC
        // if (object instanceof GC) {
        // return;
        // }
        if (object instanceof Image) {
            if (((Image) object).isDisposed()) return;
            gc.drawImage((Image) object, 0, 0);
            return;
        }
        if (object instanceof Region) {
            if (((Region) object).isDisposed()) return;
            String string = ((Region) object).getBounds().toString();
            gc.drawString(string, 0, 0);
            return;
        }
    }
    
    void refreshObject() {
        int index = list.getSelectionIndex();
        if (index == -1) return;
        if (check.getSelection()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PrintStream s = new PrintStream(stream);
            errors[index].printStackTrace(s);
            text.setText(stream.toString());
            text.setVisible(true);
            canvas.setVisible(false);
        } else {
            canvas.setVisible(true);
            text.setVisible(false);
            canvas.redraw();
        }
    }
    
    void refreshAll() {
        oldObjects = new Object[0];
        oldErrors = new Error[0];
        refreshDifference();
        oldObjects = objects;
        oldErrors = errors;
    }
    
    void layout() {
        Rectangle rect = shell.getClientArea();
        @SuppressWarnings("unused")
        String[] strings = new String[objects.length];
        int width = 0;
        String[] items = list.getItems();
        GC gc = new GC(list);
        for (int i = 0; i < objects.length; i++) {
            width = Math.max(width, gc.stringExtent(items[i]).x);
        }
        gc.dispose();
        Point size1 = start.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point size2 = stop.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point size5 = analyze.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point size3 = check.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point size4 = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        width = Math.max(size1.x, Math.max(size2.x, Math.max(size3.x, Math.max(size5.x, width))));
        width = Math.max(64, Math.max(size4.x, list.computeSize(width, SWT.DEFAULT).x));
        start.setBounds(0, 0, width, size1.y);
        stop.setBounds(0, size1.y, width, size2.y);
        analyze.setBounds(0, size1.y + size2.y, width, size5.y);
        
        check.setBounds(0, size1.y + size2.y + size5.y, width, size3.y);
        label.setBounds(0, rect.height - size4.y, width, size4.y);
        int height = size1.y + size2.y + size3.y + size5.y;
        list.setBounds(0, height, width, rect.height - height - size4.y);
        text.setBounds(width, 0, rect.width - width, rect.height);
        canvas.setBounds(width, 0, rect.width - width, rect.height);
    }
    
    public static void main(String[] args) {
        DeviceData data = new DeviceData();
        data.tracking = true;
        Display display = new Display(data);
        Sleak sleak = new Sleak();
        sleak.open();
        Example1.main(display, new String[0]);
        Example1.dispose();
        Example1.main(display, new String[0]);
        Example1.dispose();
        Example1.main(display, new String[0]);
        Example1.dispose();
        Example1.main(display, new String[0]);

        sleak.loop();
    }

    private void loop() {

        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        
    }
    
}