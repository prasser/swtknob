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

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.widgets.Listener;

/**
 * This class implements a knob widget for SWT
 * 
 * @author Fabian Prasser
 * 
 * @param <T>
 */
public class Knob<T> extends Canvas {

    /** Design */
    private static final int CUT_OFF    = 90;
    /** Design */
    private static final int SCALE_DOWN = 20;

    /**
     * Checks the style
     * 
     * @param style
     */
    private static int checkStyle(int style) {
        return style;
    }

    /** Design */
    private final Cursor            defaultCursor     = getDefaultCursor();
    /** Design */
    private final Cursor            hiddenCursor      = getHiddenCursor();

    /** Language profile*/
    private KnobDialogProfile       dialogProfile     = KnobDialogProfile.createEnglishProfile();

    /** Default color profile*/
    private final KnobColorProfile  standardDefaultProfile;
    /** Focused color profile*/
    private final KnobColorProfile  standardFocusedProfile;
    /** Default color profile*/
    private KnobColorProfile        defaultProfile;
    /** Focused color profile*/
    private KnobColorProfile        focusedProfile;
    /** Pre-rendered default background */
    private Image                   defaultBackground = null;
    /** Pre-rendered focused background */
    private Image                   focusedBackground = null;
    /** Retina factor (OSX fix) */
    private int                     retinaFactor      = isRetina() ? 2 : 1;
    
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
    private double                  sensitivity       = 200d;

    /** Value handling */
    private double                  value             = 0d;
    /** Value handling */
    private KnobRange<T>            range             = null;

    /** Listeners */
    private List<SelectionListener> listeners         = new ArrayList<SelectionListener>();

    /**
     * Creates a new instance
     * 
     * @param parent
     * @param style
     * @param scale
     */
    public Knob(Composite parent, int style, KnobRange<T> scale) {

        super(parent, checkStyle(style) | SWT.DOUBLE_BUFFERED);

        // Init
        this.range = scale;
        this.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        this.standardDefaultProfile = KnobColorProfile.createDefaultSystemProfile(parent.getDisplay());
        this.standardFocusedProfile = KnobColorProfile.createFocusedSystemProfile(parent.getDisplay());
        this.defaultProfile = standardDefaultProfile;
        this.focusedProfile = standardFocusedProfile;

        // Add listeners
        this.addDisposeListener(createDiposeHandler());
        this.addControlListener(createResizeHandler());
        this.addPaintListener(createPaintHandler());
        this.addMouseListener(createMouseButtonHandler());
        this.addMouseMoveListener(createMouseMoveHandler());
        this.addKeyListener(createKeyHandler());
        this.addFocusListener(createFocusHandler());
        this.addListener(SWT.Traverse, createTraverseHandler());
    }

    /**
     * Adds the given selection listener
     * 
     * @param listener
     */
    public void addSelectionListener(SelectionListener listener) {
        checkWidget();
        this.listeners.add(listener);
    }

    /**
     * Returns the scale
     * 
     * @return
     */
    public KnobRange<T> getRange() {
        checkWidget();
        return this.range;
    }

    /**
     * Returns the value
     * 
     * @return
     */
    public T getValue() {
        checkWidget();
        return this.range.toExternal(value);
    }

    /**
     * Removes the listener
     * 
     * @param listener
     */
    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void setBackground(Color arg0) {
        super.setBackground(arg0);
        checkWidget();
        if (defaultBackground != null) defaultBackground.dispose();
        if (focusedBackground != null) focusedBackground.dispose();
        defaultBackground = null;
        focusedBackground = null;
        redraw();
    }


    /**
     * Sets the default color profile
     */
    public void setDefaultColorProfile(KnobColorProfile profile) {
        checkWidget();
        profile.check();
        this.defaultProfile = profile;
        if (defaultBackground != null) defaultBackground.dispose();
        defaultBackground = null;
        redraw();
    }
    
    /**
     * Sets the dialog language profile
     * @param profile
     */
    public void setDialogProfile(KnobDialogProfile profile){
        checkWidget();
        profile.check();
        this.dialogProfile = profile;
    }

    /**
     * Sets the default focused color profile
     */
    public void setFocusedColorProfile(KnobColorProfile profile) {
        checkWidget();
        profile.check();
        this.focusedProfile = profile;
        if (focusedBackground != null) focusedBackground.dispose();
        focusedBackground = null;
        redraw();
    }

    /**
     * Sets the range. This resets the knob.
     * 
     * @param range
     */
    public void setRange(KnobRange<T> range) {
        checkWidget();
        this.range = range;
        this.value = 0d;
        if (defaultBackground != null) defaultBackground.dispose();
        if (focusedBackground != null) focusedBackground.dispose();
        defaultBackground = null;
        focusedBackground = null;
        this.fireSelectionEvent();
        this.redraw();
    }

    /**
     * Sets the sensitivity
     * 
     * @param sensitivity
     */
    public void setSensitivity(double sensitivity) {
        checkWidget();
        if (sensitivity <= 0d) { throw new IllegalArgumentException("Sensitivity must be > 0"); }
        this.sensitivity = sensitivity;
    }

    /**
     * Sets the value
     * 
     * @param value
     */
    public void setValue(T value) {
        checkWidget();
        double val = this.range.toInternal(value);
        if (val != this.value) {
            this.value = val;
            this.redraw();
            this.fireSelectionEvent();
        }
    }

    /**
     * Handle dispose events
     * 
     * @return
     */
    private DisposeListener createDiposeHandler() {
        return new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent arg0) {
                if (defaultBackground != null && !defaultBackground.isDisposed()) defaultBackground.dispose();
                if (focusedBackground != null && !focusedBackground.isDisposed()) focusedBackground.dispose();
                if (!standardDefaultProfile.isDisposed()) standardDefaultProfile.dispose();
                if (!standardFocusedProfile.isDisposed()) standardFocusedProfile.dispose();
            }
        };
    }

    /**
     * Handle focus events
     * 
     * @return
     */
    private FocusListener createFocusHandler() {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {
                redraw();
            }

            @Override
            public void focusLost(FocusEvent arg0) {
                redraw();
            }
        };
    }

    /**
     * Handle key events
     * 
     * @return
     */
    private KeyAdapter createKeyHandler() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {

                double newValue = value;

                // React on key press
                if (arg0.character == '0') newValue = 0.0d;
                else if (arg0.character == '1') newValue = 0.1d;
                else if (arg0.character == '2') newValue = 0.2d;
                else if (arg0.character == '3') newValue = 0.3d;
                else if (arg0.character == '4') newValue = 0.4d;
                else if (arg0.character == '5') newValue = 0.5d;
                else if (arg0.character == '6') newValue = 0.6d;
                else if (arg0.character == '7') newValue = 0.7d;
                else if (arg0.character == '8') newValue = 0.8d;
                else if (arg0.character == '9') newValue = 0.9d;
                else if (arg0.character == '-') newValue -= 0.1d;
                else if (arg0.character == '+') newValue += 0.1d;
                else if (arg0.keyCode == SWT.ARROW_UP) newValue += 1d / sensitivity;
                else if (arg0.keyCode == SWT.ARROW_DOWN) newValue -= 1d / sensitivity;

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
        };
    }

    /**
     * Handle mouse buttons
     * 
     * @return
     */
    private MouseAdapter createMouseButtonHandler() {
        return new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                
                if (drag) {
                    drag = false;
                    getDisplay().setCursorLocation(screenX, screenY);
                    Knob.this.setCursor(defaultCursor);
                }
                
                KnobInputDialog<T> dialog = new KnobInputDialog<T>(getShell(), dialogProfile, range, range.toExternal(value));
                T result = dialog.open();
                if (result != null) {
                    value = range.toInternal(result);
                    redraw();
                }
            }

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
        };
    }

    /**
     * Handle mouse moves
     * 
     * @return
     */
    private MouseMoveListener createMouseMoveHandler() {
        return new MouseMoveListener() {
            @Override
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
        };
    }

    /**
     * Handle paint events
     * 
     * @return
     */
    private PaintListener createPaintHandler() {
        return new PaintListener() {
            @Override
            public void paintControl(PaintEvent arg0) {
                paint(arg0.gc);
            }
        };
    }

    /**
     * Handle resizes
     * 
     * @return
     */
    private ControlAdapter createResizeHandler() {
        return new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent arg0) {
                if (defaultBackground != null) defaultBackground.dispose();
                if (focusedBackground != null) focusedBackground.dispose();
                defaultBackground = null;
                focusedBackground = null;
                redraw();
            }
        };
    }

    /**
     * Handle traverse events
     * 
     * @return
     */
    private Listener createTraverseHandler() {
        return new Listener() {
            public void handleEvent(Event e) {
                switch (e.detail) {
                case SWT.TRAVERSE_ESCAPE:
                case SWT.TRAVERSE_RETURN:
                case SWT.TRAVERSE_TAB_NEXT:
                case SWT.TRAVERSE_TAB_PREVIOUS:
                case SWT.TRAVERSE_PAGE_NEXT:
                case SWT.TRAVERSE_PAGE_PREVIOUS:
                    e.doit = true;
                    break;
                }
            }
        };
    }

    /**
     * Fires a selection event
     */
    private void fireSelectionEvent() {
        Event event = new Event();
        event.widget = this;
        SelectionEvent sevent = new SelectionEvent(event);
        sevent.widget = this;
        sevent.data = range.toExternal(this.value);
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

        value *= 1 - CUT_OFF / 360d;
        value += CUT_OFF / 720d;

        double r = (double) size;
        double x = r * Math.sin(-value * 2d * Math.PI);
        double y = r * Math.cos(-value * 2d * Math.PI);
        return new Point((int) Math.round(x + centerX), (int) Math.round(y + centerY));
    }
    
    /**
     * Returns whether this is a retina device. 
     * http://lubosplavucha.com/java/2013/09/02/retina-support-in-java-for-awt-swing/
     * @return
     */
    private static boolean isRetina() {
    	 
        boolean isRetina = false;
        GraphicsDevice graphicsDevice = GraphicsEnvironment.
        								getLocalGraphicsEnvironment().
        								getDefaultScreenDevice();
        try {
            Field field = graphicsDevice.getClass().getDeclaredField("scale");
            if (field != null) {
                field.setAccessible(true);
                Object scale = field.get(graphicsDevice);
                if(scale instanceof Integer && ((Integer) scale).intValue() == 2) {
                    isRetina = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRetina;
    }

    /**
     * Paint routine
     * 
     * @param gc
     */
    private void paint(GC gc) {

        Point gcsize = this.getSize();

        // Directly paint to the canvas
        if (gcsize.x >= SCALE_DOWN && gcsize.y >= SCALE_DOWN) {
            paint(gc, gcsize);

            // Paint to an image and scale down for better results
        } else {
            Image image = new Image(getDisplay(), SCALE_DOWN * retinaFactor, SCALE_DOWN * retinaFactor);
            GC gc2 = new GC(image);
            paint(gc2, new Point(SCALE_DOWN * retinaFactor, SCALE_DOWN * retinaFactor));
            gc2.dispose();

            int size = Math.min(gcsize.x, gcsize.y);
            gc.setAdvanced(true);
            gc.setAntialias(SWT.ON);
            gc.drawImage(image, 0, 0, SCALE_DOWN * retinaFactor, SCALE_DOWN * retinaFactor, 0, 0, size, size);
        }
    }

    /**
     * Actually paints the know to the given canvas
     * 
     * @param gc
     * @param gcsize
     */
    private void paint(GC gc, Point gcsize) {
        
        KnobColorProfile profile = this.defaultProfile;
        if (this.isFocusControl()) profile = this.focusedProfile;

        // Determine size
        double min = (double) Math.min(gcsize.x, gcsize.y);
        int imageSize = (int) Math.round(min);
        if (defaultBackground == null || defaultBackground.isDisposed()) {
            defaultBackground = paintBackground(imageSize * retinaFactor, imageSize * retinaFactor, this.defaultProfile);
        }
        if (focusedBackground == null || focusedBackground.isDisposed()) {
            focusedBackground = paintBackground(imageSize * retinaFactor, imageSize * retinaFactor, this.focusedProfile);
        }

        // Activate anti-aliasing
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);

        // Draw background
        Image background = this.isFocusControl() ? focusedBackground : defaultBackground;
        gc.drawImage(background, 0, 0, imageSize * retinaFactor, imageSize * retinaFactor, 0, 0, imageSize, imageSize);

        // Compute parameters
        double tick = min * 0.3d;
        double inner = min * 0.4d;
        double outer = min * 0.1d;
        double plateau = min * 0.075d;
        double indicatorWidth = min / 20d;
        double focusWidth = indicatorWidth / 1.5d;
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
        int iFocusWidth = (int) Math.round(focusWidth);
        iIndicatorWidth -= 1 - iIndicatorWidth % 2;
        if (iIndicatorWidth < 1) iIndicatorWidth = 1;
        iFocusWidth -= 1 - iFocusWidth % 2;
        if (iFocusWidth < 1) iFocusWidth = 1;
        int iPlateau = (int) Math.round(plateau);

        // Draw plateau
        gc.setForeground(profile.getPlateauInner());
        gc.setBackground(profile.getPlateauInner());
        gc.fillOval(iCenterX - iPlateau, iCenterY - iPlateau, iPlateau * 2, iPlateau * 2);

        // Draw the value indicator
        Point line = getLineCoordinates(iCenterX, iCenterY, iTick, range.toNearestInternal(value));
        gc.setForeground(profile.getIndicatorOuter());
        gc.setForeground(profile.getIndicatorOuter());
        gc.setLineCap(SWT.CAP_ROUND);
        gc.setLineWidth(iIndicatorWidth);
        gc.drawLine(line.x, line.y, iCenterX, iCenterY);

        gc.setForeground(profile.getIndicatorInner());
        gc.setForeground(profile.getIndicatorInner());
        gc.setLineCap(SWT.CAP_ROUND);
        gc.setLineWidth(iFocusWidth);
        gc.drawLine(line.x, line.y, iCenterX, iCenterY);
    }

    /**
     * Paint the background image
     * 
     * @param width
     * @param height
     * @param profile 
     */
    private Image paintBackground(int width, int height, KnobColorProfile profile) {

        Display display = getDisplay();
        Image background = new Image(display, width, height);
        GC gc = new GC(background);

        gc.setBackground(getBackground());
        gc.fillRectangle(0, 0, width, height);

        // Compute parameters
        double min = width;
        double inner = min * 0.4d;
        double outer = min * 0.1d;
        double plateau = min * 0.1d;
        double indicatorWidth = min / 20d;
        double stepping = range.getStepping();
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
        if (1d / stepping <= 72 && (dX > 5 || dY > 5)) {

            // Ticks matching scale
            for (double v = 0d; v <= 1d - stepping; v += stepping) {
                double tick = range.toNearestInternal(v);
                ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, tick));
            }
            double tick = range.toNearestInternal(1d);
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, tick));

        } else {
            // Default
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.125d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.25d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.375d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.5d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.625d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.75d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 0.875d));
            ticks11.add(getLineCoordinates(iCenterX, iCenterY, iInner + iOuter / 2 + 1, 1.0d));
        }

        // Activate anti-aliasing
        gc.setAdvanced(true);
        gc.setAntialias(SWT.ON);

        // Draw the ticks
        gc.setForeground(profile.getTick());
        gc.setBackground(profile.getTick());
        gc.setLineCap(SWT.CAP_FLAT);
        gc.setLineWidth(iTickWidth);
        for (int i = 0; i < ticks11.size(); i++) {
            Point p1 = ticks11.get(i);
            gc.drawLine(p1.x, p1.y, iCenterX, iCenterY);
        }

        // Draw the background
        KnobRenderer renderer = new KnobRenderer();
        int transparent = profile.getTransparentByte();
        Color transparentColor = new Color(getDisplay(), transparent, transparent, transparent); 
        Image image = renderer.render(getDisplay(), transparentColor, profile, iInner * 2, iInner * 2);
        gc.drawImage(image, 0, 0, iInner * 2, iInner * 2, iOuter, iOuter, iInner * 2, iInner * 2);
        transparentColor.dispose();
        image.dispose();

        // Draw circle
        gc.setForeground(profile.getBorder());
        gc.setBackground(profile.getBorder());
        gc.setLineWidth(1);
        gc.drawOval(iOuter, iOuter, iInner * 2, iInner * 2);

        // Draw plateau
        gc.setForeground(profile.getPlateauOuter());
        gc.setBackground(profile.getPlateauOuter());
        gc.fillOval(iCenterX - iPlateau, iCenterY - iPlateau, iPlateau * 2, iPlateau * 2);
        gc.dispose();
        
        // Return
        return background;
    }
}
