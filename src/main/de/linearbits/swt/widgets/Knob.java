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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

/**
 * This class implements a knob widget for SWT
 * @author Fabian Prasser
 *
 * @param <T>
 */
public class Knob<T> extends Canvas {

    /** Design */
    private static final int        SCALE_LOWER_BOUND = 20;
    /** Design */
    private final Color             black;
    /** Design */
    private final Color             darkGray;
    /** Design */
    private final Color             lightGray;
    /** Design */
    private final Color             white;
    /** Design */
    private final Color             red;

    /** Dragging */
    private boolean                 drag              = false;
    /** Dragging */
    private int                     dragY             = 0;
    /** Dragging */
    private int                     dragOffset        = 0;
    /** Dragging */
    private int                     screenX           = 0;
    /** Dragging */
    private int                     screenY           = 0;
    /** Dragging */
    private double                  dragValue         = 0;
    /** Dragging */
    private final Cursor            defaultCursor     = getDefaultCursor();
    /** Dragging */
    private final Cursor            hiddenCursor      = getHiddenCursor();
    /** Dragging */
    private double                  sensitivity       = 200d;

    /** Value in [0, 1] */
    private double                  value             = 0d;

    /** Scale */
    private KnobScale<T>            scale             = null;

    /** Pre-rendered background image */
    private Image                   background        = null;

    /** Listeners */
    private List<SelectionListener> listeners         = new ArrayList<SelectionListener>();

    /**
     * Creates a new instance
     * @param parent
     * @param style
     * @param scale
     */
    public Knob(Composite parent, int style, KnobScale<T> scale) {

        super(parent, style | SWT.DOUBLE_BUFFERED);
        this.scale = scale;
        this.red = new Color(getDisplay(), 255, 0, 0);
        this.black = new Color(getDisplay(), 0, 0, 0);
        this.darkGray = new Color(getDisplay(), 169, 169, 169);
        this.lightGray = new Color(getDisplay(), 211, 211, 211);
        this.white = new Color(getDisplay(), 255, 255, 255);

        // Dispose
        this.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent arg0) {
                if (!black.isDisposed()) black.dispose();
                if (!darkGray.isDisposed()) darkGray.dispose();
                if (!lightGray.isDisposed()) lightGray.dispose();
                if (!white.isDisposed()) white.dispose();
                if (!red.isDisposed()) red.dispose();
                if (background != null && !background.isDisposed()) background.dispose();
            }
        });

        // Resize
        this.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent arg0) {
                if (background != null) background.dispose();
                background = null;
                redraw();
            }
        });

        // Paint
        this.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent arg0) {
                paint(arg0.gc);
            }
        });

        // Mouse buttons
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent arg0) {
                dragY = arg0.y;
                Point point = Knob.this.toDisplay(arg0.x, arg0.y);
                screenX = point.x;
                screenY = point.y;
                dragValue = value;
                dragOffset = 0;
                drag = true;
                Knob.this.setCursor(hiddenCursor);
                Knob.this.setFocus();
            }

            @Override
            public void mouseUp(MouseEvent arg0) {
                if (drag) {
                    drag = false;
                    getDisplay().setCursorLocation(screenX, screenY);
                    Knob.this.setCursor(defaultCursor);
                }
            }
        });

        // Mouse moves
        this.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent me) {
                if (drag) {
                    dragOffset += me.y - dragY;
                    
                    double newValue = dragValue - dragOffset / sensitivity;
                    if (newValue < 0d) {
                        dragOffset = (int) (dragValue * sensitivity);
                        newValue = 0d;
                    } else if (newValue > 1d) {
                        dragOffset = (int) (dragValue * sensitivity - sensitivity);
                        newValue = 1d;
                    }
                    
                    if (value != newValue) {
                        value = newValue;
                        fireSelectionEvent();
                        redraw();
                    }

                    getDisplay().setCursorLocation(screenX, screenY);
                }
            }
        });
        
        // Keyboard
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent arg0) {
                
                double newValue = value;
                
                // React on key press
                if (arg0.character=='0') newValue = 0.0d;
                else if (arg0.character=='1') newValue = 0.1d;
                else if (arg0.character=='2') newValue = 0.2d;
                else if (arg0.character=='3') newValue = 0.3d;
                else if (arg0.character=='4') newValue = 0.4d;
                else if (arg0.character=='5') newValue = 0.5d;
                else if (arg0.character=='6') newValue = 0.6d;
                else if (arg0.character=='7') newValue = 0.7d;
                else if (arg0.character=='8') newValue = 0.8d;
                else if (arg0.character=='9') newValue = 0.9d;
                else if (arg0.character=='-') newValue -= 0.1d;
                else if (arg0.character=='+') newValue += 0.1d;
                
                // Adjust
                if (newValue < 0d) newValue = 0d;
                if (newValue > 1d) newValue = 1d;

                // Change
                if (value != newValue) {
                    value = newValue;
                    fireSelectionEvent();
                    redraw();
                }
            }
        });
    }

    /**
     * Adds the listener
     * @param listener
     */
    public void addSelectionListener(SelectionListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Returns the scale
     * 
     * @return
     */
    public KnobScale<T> getScale() {
        checkThreadAccess();
        return this.scale;
    }

    /**
     * Returns the value
     * 
     * @return
     */
    public T getValue() {
        checkThreadAccess();
        return this.scale.toExternal(value);
    }

    /**
     * Removes the listener
     * @param listener
     */
    public void removeSelectionListener(SelectionListener listener){
        this.listeners.remove(listener);
    }

    @Override
    public void setBackground(Color arg0) {
        super.setBackground(arg0);
        if (background != null) background.dispose();
        background = null;
        redraw();
    }

    /**
     * Sets the scale. This resets the knob.
     * 
     * @param scale
     */
    public void setScale(KnobScale<T> scale) {
        checkThreadAccess();
        this.scale = scale;
        this.value = 0d;
        if (background != null) background.dispose();
        background = null;
        this.fireSelectionEvent();
        this.redraw();
    }
    
    /**
     * Sets the sensitivity
     * 
     * @param sensitivity
     */
    public void setSensitivity(double sensitivity) {
        checkThreadAccess();
        if (sensitivity <= 0d) { throw new IllegalArgumentException("Sensitivity must be > 0"); }
        this.sensitivity = sensitivity;
    }
    
    /**
     * Sets the value
     * 
     * @param value
     */
    public void setValue(T value) {
        checkThreadAccess();
        double val = this.scale.toInternal(value);
        if (val != this.value) {
            this.value = val;
            this.redraw();
            this.fireSelectionEvent();
        }
    }
    
    /**
     * Checks the thread access
     */
    private void checkThreadAccess() {
        if (Thread.currentThread() != getDisplay().getThread()) { throw new SWTException("Invalid thread access"); }
    }
    
    /**
     * Fires a selection event
     */
    private void fireSelectionEvent(){
        Event event = new Event();
        event.widget = this;
        SelectionEvent sevent = new SelectionEvent(event);
        sevent.widget = this;
        sevent.data = scale.toExternal(this.value);
        for (SelectionListener listener : listeners) {
            listener.widgetSelected(sevent);
            listener.widgetDefaultSelected(sevent);
        }
    }

    /**
     * Returns the current default cursor of the widget
     * 
     * @return
     */
    private Cursor getDefaultCursor() {
        return this.getCursor();
    }

    /**
     * Returns an invisible cursor
     * 
     * @return
     */
    private Cursor getHiddenCursor() {
        Display display = getDisplay();
        Color white = display.getSystemColor(SWT.COLOR_WHITE);
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        PaletteData palette = new PaletteData(new RGB[] { white.getRGB(), black.getRGB() });
        ImageData sourceData = new ImageData(16, 16, 1, palette);
        sourceData.transparentPixel = 0;
        return new Cursor(display, sourceData, 0, 0);

    }

    /**
     * Calculate the x, y coordinates of end of a line from the center to the
     * edge of the knob for the given value
     * 
     * @return
     */
    private Point getLineCoordinates(int centerX, int centerY, int size, double value) {
        double r = (double) size;
        double x = r * Math.sin(-value * 2d * Math.PI);
        double y = r * Math.cos(-value * 2d * Math.PI);
        return new Point((int) Math.round(x + centerX), (int) Math.round(y + centerY));
    }

    /**
     * Paint routine
     * 
     * @param gc
     */
    private void paint(GC gc) {

        Point gcsize = this.getSize();

        // Directly paint to the canvas
        if (gcsize.x >= SCALE_LOWER_BOUND && gcsize.y >= SCALE_LOWER_BOUND) {
            paint(gc, gcsize);

        // Paint to an image and scale down for better results
        } else {
            Image image = new Image(getDisplay(), SCALE_LOWER_BOUND, SCALE_LOWER_BOUND);
            GC gc2 = new GC(image);
            paint(gc2, new Point(SCALE_LOWER_BOUND, SCALE_LOWER_BOUND));
            gc2.dispose();

            int size = Math.min(gcsize.x, gcsize.y);
            gc.setAdvanced(true);
            gc.setAntialias(SWT.ON);
            gc.drawImage(image, 0, 0, SCALE_LOWER_BOUND, SCALE_LOWER_BOUND, 0, 0, size, size);
        }
    }

    /**
     * Actually paints the know to the given canvas
     * 
     * @param gc
     * @param gcsize
     */
    private void paint(GC gc, Point gcsize) {
        
        // Determine size
        double min = (double) Math.min(gcsize.x, gcsize.y);
        int imageSize = (int)Math.round(min);
        if (background == null || background.isDisposed()) {
            paintBackground(imageSize, imageSize);
        }

        // Activate anti-aliasing
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);

        // Draw background
        gc.drawImage(this.background, 0, 0, imageSize, imageSize, 0, 0, imageSize, imageSize);

        // Compute parameters
        double tick = min * 0.3d;
        double inner = min * 0.4d;
        double outer = min * 0.1d;
        double indicatorWidth = min / 20d;
        if (indicatorWidth < 1d) indicatorWidth = 1d;
        double tickWidth = indicatorWidth / 3d;
        if (tickWidth < 1d) tickWidth = 1d;

        // Convert to ints
        int iTick = (int) Math.round(tick);
        int iInner = (int) Math.round(inner) - (int) Math.round(inner) % 2;
        int iOuter = (int) Math.round(outer);
        int iCenterX = iOuter + iInner;
        int iCenterY = iOuter + iInner;
        int iIndicatorWidth = (int) Math.round(indicatorWidth);
        iIndicatorWidth -= 1 - iIndicatorWidth % 2;
        if (iIndicatorWidth < 1d) iIndicatorWidth = 1;

        // Draw the value indicator
        Point line = getLineCoordinates(iCenterX, iCenterY, iTick, scale.toNearestInternal(value));
        gc.setForeground(red);
        gc.setBackground(red);
        gc.setLineCap(SWT.CAP_ROUND);
        gc.setLineWidth(iIndicatorWidth);
        gc.drawLine(line.x, line.y, iCenterX, iCenterY);
    }

    /**
     * Paint the background image
     * @param width
     * @param height
     */
    private void paintBackground(int width, int height) {
        
        this.background = new Image(getDisplay(), width, height);
        GC gc = new GC(this.background);
        gc.setBackground(getBackground());
        gc.fillRectangle(0, 0, width, height);

        // Compute parameters
        double min = width;
        double inner = min * 0.4d;
        double outer = min * 0.1d;
        double plateau = min * 0.1d;
        double indicatorWidth = min / 20d;
        double stepping = scale.getStepping();
        if (indicatorWidth < 1d) indicatorWidth = 1d;
        double tickWidth = indicatorWidth / 3d;
        if (tickWidth < 1d) tickWidth = 1d;

        // Convert to ints
        int iInner = (int) Math.round(inner) - (int) Math.round(inner) % 2;
        int iOuter = (int) Math.round(outer);
        int iPlateau = (int) Math.round(plateau);
        int iCenterX = iOuter + iInner;
        int iCenterY = iOuter + iInner;
        int iIndicatorWidth = (int) Math.round(indicatorWidth);
        int iTickWidth = (int) Math.round(tickWidth);
        iIndicatorWidth -= 1 - iIndicatorWidth % 2;
        iTickWidth -= 1 - iTickWidth % 2;
        if (iTickWidth < 1d) iTickWidth = 1;
        if (iIndicatorWidth < 1d) iIndicatorWidth = 1;
        if ((iCenterX + iPlateau) % 2 == 0) iPlateau++;

        // Compute lines for ticks
        List<Point> ticks11 = new ArrayList<Point>();
        
        // Adapt
        Point ap1 = getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0d);
        Point ap2 = getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, stepping);
        int dX = Math.abs(ap1.x - ap2.x);
        int dY = Math.abs(ap1.y - ap2.y);
        if (1d / stepping <= 72 && (dX>5 || dY>5)) {
            
            // Ticks matching scale
            for (double v = 0d; v<=1d; v+=stepping) {
                double value = scale.toNearestInternal(v);
                ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, value));
            }
            
        } else {
            // Default
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.25d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.5d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.75d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.125d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.375d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.625d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.875d));
        }

        // Activate anti-aliasing
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);

        // Draw the ticks
        gc.setForeground(black);
        gc.setBackground(black);
        gc.setLineWidth(iTickWidth);
        for (int i = 0; i < ticks11.size(); i++) {
            Point p1 = ticks11.get(i);
            gc.drawLine(p1.x, p1.y, iCenterX, iCenterY);
        }

        // Draw the background
        KnobRenderer renderer = new KnobRenderer();
        Image image = renderer.render(getDisplay(), red, iInner * 2, iInner * 2);
        gc.drawImage(image, 0, 0, iInner * 2, iInner * 2, iOuter, iOuter, iInner * 2, iInner * 2);
        image.dispose();

        // Draw circle
        gc.setForeground(black);
        gc.setBackground(black);
        gc.setLineWidth(1);
        gc.drawOval(iOuter, iOuter, iInner * 2, iInner * 2);

        // Draw plateau
        gc.setForeground(black);
        gc.setBackground(black);
        gc.fillOval(iCenterX - iPlateau, iCenterY - iPlateau, iPlateau * 2, iPlateau * 2);
        gc.dispose();
    }
}
