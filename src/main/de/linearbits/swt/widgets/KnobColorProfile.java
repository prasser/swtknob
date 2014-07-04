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
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * This class implements a color profile for the knob. Two profiles can be passed to an instance of the widget, one
 * that is used when the widget is in focus and one that is used when the widget is not in focus
 * 
 * @author Fabian Prasser
 */
public class KnobColorProfile {

    /**
     * Creates a default profile
     * @return
     */
    public static KnobColorProfile createDefaultBlueRedProfile() {
        return createDefaultBlueRedProfile(Display.getCurrent());
    }
    /**
     * Creates a default profile
     * @param display
     * @return
     */
    public static KnobColorProfile createDefaultBlueRedProfile(Display display) {
        KnobColorProfile result = new KnobColorProfile();
        result.setBackground(new Color(display, 190, 190, 190));
        result.setBorder(new Color(display, 0, 0, 0));
        result.setEdgeFrom(new Color(display, 6, 76, 160));
        result.setEdgeTo(new Color(display, 0, 0, 0));
        result.setHighlightBottom(new Color(display, 255, 255, 255));
        result.setHighlightInnerFrom(new Color(display, 64, 142, 203));
        result.setHighlightInnerTo(new Color(display, 64, 142, 203));
        result.setHighlightSpecular(new Color(display, 255, 255, 255));
        result.setIndicatorInner(new Color(display, 255, 50, 0));
        result.setIndicatorOuter(new Color(display, 255, 50, 0));
        result.setPlateauInner(new Color(display, 0, 0, 0));
        result.setPlateauOuter(new Color(display, 0, 0, 0));
        result.setShadow(new Color(display, 0, 0, 0));
        result.setTick(new Color(display, 0, 0, 0));
        return result;
    }
    /**
     * Creates a default system profile
     * @return
     */
    public static KnobColorProfile createDefaultSystemProfile() {
        return createDefaultSystemProfile(Display.getCurrent());
    }
    /**
     * Creates a default system profile
     * @param display
     * @return
     */
    public static KnobColorProfile createDefaultSystemProfile(Display display) {
        KnobColorProfile result = new KnobColorProfile();

        result.setBackground(getSystemColor(display, SWT.COLOR_WIDGET_BACKGROUND));
        result.setEdgeFrom(getSystemColor(display, SWT.COLOR_WIDGET_LIGHT_SHADOW));
        result.setEdgeTo(getSystemColor(display, SWT.COLOR_WIDGET_DARK_SHADOW));
        result.setHighlightBottom(getSystemColor(display, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
        result.setHighlightInnerFrom(getSystemColor(display, SWT.COLOR_WIDGET_LIGHT_SHADOW));
        result.setHighlightInnerTo(getSystemColor(display, SWT.COLOR_WIDGET_LIGHT_SHADOW));
        result.setHighlightSpecular(getSystemColor(display, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
        
        result.setBorder(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setIndicatorInner(getSystemColor(display, SWT.COLOR_LIST_SELECTION_TEXT));
        result.setIndicatorOuter(getSystemColor(display, SWT.COLOR_LIST_SELECTION_TEXT));
        result.setPlateauInner(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setPlateauOuter(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setShadow(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setTick(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        return result;
    }
    /**
     * Creates a focused profile
     * @return
     */
    public static KnobColorProfile createFocusedBlueRedProfile() {
        return createFocusedBlueRedProfile(Display.getCurrent());
    }
    /**
     * Creates a focused profile
     * @param display
     * @return
     */
    public static KnobColorProfile createFocusedBlueRedProfile(Display display) {
        KnobColorProfile result = new KnobColorProfile();
        result.setBackground(new Color(display, 190, 190, 190));
        result.setBorder(new Color(display, 0, 0, 0));
        result.setEdgeFrom(new Color(display, 6, 76, 160));
        result.setEdgeTo(new Color(display, 0, 0, 0));
        result.setHighlightBottom(new Color(display, 255, 255, 255));
        result.setHighlightInnerFrom(new Color(display, 64, 142, 203));
        result.setHighlightInnerTo(new Color(display, 64, 142, 203));
        result.setHighlightSpecular(new Color(display, 255, 255, 255));
        result.setIndicatorInner(new Color(display, 255, 50, 0));
        result.setIndicatorOuter(new Color(display, 0, 0, 0));
        result.setPlateauInner(new Color(display, 255, 50, 0));
        result.setPlateauOuter(new Color(display, 0, 0, 0));
        result.setShadow(new Color(display, 0, 0, 0));
        result.setTick(new Color(display, 0, 0, 0));
        return result;
    }
    /**
     * Creates a focused system profile
     * @return
     */
    public static KnobColorProfile createFocusedSystemProfile() {
        return createFocusedSystemProfile(Display.getCurrent());
    }
    /**
     * Creates a focused system profile
     * @param display
     * @return
     */
    public static KnobColorProfile createFocusedSystemProfile(Display display) {
        KnobColorProfile result = new KnobColorProfile();

        result.setBackground(getSystemColor(display, SWT.COLOR_WIDGET_BACKGROUND));
        result.setEdgeFrom(getSystemColor(display, SWT.COLOR_WIDGET_LIGHT_SHADOW));
        result.setEdgeTo(getSystemColor(display, SWT.COLOR_WIDGET_DARK_SHADOW));
        result.setHighlightBottom(getSystemColor(display, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
        result.setHighlightInnerFrom(getSystemColor(display, SWT.COLOR_WIDGET_LIGHT_SHADOW));
        result.setHighlightInnerTo(getSystemColor(display, SWT.COLOR_WIDGET_LIGHT_SHADOW));
        result.setHighlightSpecular(getSystemColor(display, SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
        
        result.setBorder(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setIndicatorInner(getSystemColor(display, SWT.COLOR_LIST_SELECTION_TEXT));
        result.setIndicatorOuter(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setPlateauInner(getSystemColor(display, SWT.COLOR_LIST_SELECTION_TEXT));
        result.setPlateauOuter(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setShadow(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        result.setTick(getSystemColor(display, SWT.COLOR_WIDGET_BORDER));
        return result;
    }
    /**
     * Returns a new instance of the given system color
     * @param display
     * @param color
     * @return
     */
    private static final Color getSystemColor(Display display, int color){
        return new Color(display, display.getSystemColor(color).getRGB());
    }
    /** Design */
    private Color background;
    /** Design */
    private Color border;
    /** Design */
    private Color edgeFrom;
    /** Design */
    private Color edgeTo;
    /** Design */
    private Color highlightBottom;

    /** Design */
    private Color highlightInnerFrom;
    
    /** Design */
    private Color highlightInnerTo;

    /** Design */
    private Color highlightSpecular;

    /** Design */
    private Color indicatorInner;

    /** Design */
    private Color indicatorOuter;

    /** Design */
    private Color plateauInner;

    /** Design */
    private Color plateauOuter;

    /** Design */
    private Color shadow;

    /** Design */
    private Color tick;

    /** Is the profile disposed*/
    private boolean disposed = false;

    /**
     * Checks all colors
     */
    public void check(){
        checkDisposed();
        if (background == null) throw new NullPointerException("Color for 'background' is null"); 
        if (border == null) throw new NullPointerException("Color for 'border' is null");   
        if (edgeFrom == null) throw new NullPointerException("Color for 'edgeFrom' is null");    
        if (edgeTo == null) throw new NullPointerException("Color for 'edgeTo' is null");     
        if (highlightBottom == null) throw new NullPointerException("Color for 'highlightBottom' is null"); 
        if (highlightInnerFrom == null) throw new NullPointerException("Color for 'highlightInnerFrom' is null"); 
        if (highlightInnerTo == null) throw new NullPointerException("Color for 'highlightInnerTo' is null"); 
        if (highlightSpecular == null) throw new NullPointerException("Color for 'highlightSpecular' is null");
        if (indicatorInner == null) throw new NullPointerException("Color for 'indicatorInner' is null"); 
        if (indicatorOuter == null) throw new NullPointerException("Color for 'indicatorOuter' is null"); 
        if (plateauInner == null) throw new NullPointerException("Color for 'plateauInner' is null"); 
        if (plateauOuter == null) throw new NullPointerException("Color for 'plateauOuter' is null"); 
        if (shadow == null) throw new NullPointerException("Color for 'shadow' is null");
        if (tick == null) throw new NullPointerException("Color for 'tick' is null");
    }

    /**
     * Disposes all colors
     */
    public void dispose(){

         checkDisposed();
         if (background != null && !background.isDisposed()) background.dispose();
         if (border != null && !border.isDisposed()) border.dispose();
         if (edgeFrom != null && !edgeFrom.isDisposed()) edgeFrom.dispose();
         if (edgeTo != null && !edgeTo.isDisposed()) edgeTo.dispose();
         if (highlightBottom != null && !highlightBottom.isDisposed()) highlightBottom.dispose();
         if (highlightInnerFrom != null && !highlightInnerFrom.isDisposed()) highlightInnerFrom.dispose();
         if (highlightInnerTo != null && !highlightInnerTo.isDisposed()) highlightInnerTo.dispose();
         if (highlightSpecular != null && !highlightSpecular.isDisposed()) highlightSpecular.dispose();
         if (indicatorInner != null && !indicatorInner.isDisposed()) indicatorInner.dispose();
         if (indicatorOuter != null && !indicatorOuter.isDisposed()) indicatorOuter.dispose();
         if (plateauInner != null && !plateauInner.isDisposed()) plateauInner.dispose();
         if (plateauOuter != null && !plateauOuter.isDisposed()) plateauOuter.dispose();
         if (shadow != null && !shadow.isDisposed()) shadow.dispose();
         if (tick != null && !tick.isDisposed()) tick.dispose();
         disposed = true;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getBackground() {
        checkDisposed();
        return background;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getBorder() {
        checkDisposed();
        return border;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getEdgeFrom() {
        checkDisposed();
        return edgeFrom;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getEdgeTo() {
        checkDisposed();
        return edgeTo;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getHighlightBottom() {
        checkDisposed();
        return highlightBottom;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getHighlightInnerFrom() {
        checkDisposed();
        return highlightInnerFrom;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getHighlightInnerTo() {
        checkDisposed();
        return highlightInnerTo;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getHighlightSpecular() {
        checkDisposed();
        return highlightSpecular;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getIndicatorInner() {
        checkDisposed();
        return indicatorInner;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getIndicatorOuter() {
        checkDisposed();
        return indicatorOuter;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getPlateauInner() {
        checkDisposed();
        return plateauInner;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getPlateauOuter() {
        checkDisposed();
        return plateauOuter;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getShadow() {
        checkDisposed();
        return shadow;
    }

    /**
     * Returns a color
     * @return
     */
    public Color getTick() {
        checkDisposed();
        return tick;
    }

    /**
     * Is this object disposed
     * @return
     */
    public boolean isDisposed() {
        return disposed;
    }

    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setBackground(Color color) {
        checkDisposed();
        checkNull(color);
        this.background = color;
        return this;
    }
    
    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setBorder(Color color) {
        checkDisposed();
        checkNull(color);
        this.border = color;
        return this;
    }

    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setEdgeFrom(Color color) {
        checkDisposed();
        checkNull(color);
        this.edgeFrom = color;
        return this;
    }
    
    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setEdgeTo(Color color) {
        checkDisposed();
        checkNull(color);
        this.edgeTo = color;
        return this;
    }
    
    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setHighlightBottom(Color color) {
        checkDisposed();
        checkNull(color);
        this.highlightBottom = color;
        return this;
    }
    
    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setHighlightInnerFrom(Color color) {
        checkDisposed();
        checkNull(color);
        this.highlightInnerFrom = color;
        return this;
    }

    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setHighlightInnerTo(Color color) {
        checkDisposed();
        checkNull(color);
        this.highlightInnerTo = color;
        return this;
    }

    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setHighlightSpecular(Color color) {
        checkDisposed();
        checkNull(color);
        this.highlightSpecular = color;
        return this;
    }
    
    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setIndicatorInner(Color color) {
        checkDisposed();
        checkNull(color);
        this.indicatorInner = color;
        return this;
    }

    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setIndicatorOuter(Color color) {
        checkDisposed();
        checkNull(color);
        this.indicatorOuter = color;
        return this;
    }
    
    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setPlateauInner(Color color) {
        checkDisposed();
        checkNull(color);
        this.plateauInner = color;
        return this;
    }

    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setPlateauOuter(Color color) {
        checkDisposed();
        checkNull(color);
        this.plateauOuter = color;
        return this;
    }
    
    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setShadow(Color color) {
        checkDisposed();
        checkNull(color);
        this.shadow = color;
        return this;
    }

    /**
     * Sets a color
     * @param color
     */
    public KnobColorProfile setTick(Color color) {
        checkDisposed();
        checkNull(color);
        this.tick = color;
        return this;
    }
    
    /**
     * Checks if the profile is disposed
     */
    private void checkDisposed() {
        if (this.disposed) throw new SWTException("Profile disposed");
    }
    
    /**
     * Checks if the object is null
     * @param value
     */
    private void checkNull(Object value) {
        if (value == null) throw new NullPointerException("Argument is null");
    }

    /**
     * Returns a byte b, such that (b,b,b) is not part of the profile
     * @return
     */
    protected int getTransparentByte(){
        checkDisposed();
        int i;
        for (i=0; i<256; i++) {
            if (background.getRed() == i || background.getGreen() == i || background.getBlue() == i) continue;
            if (border.getRed() == i || border.getGreen() == i || border.getBlue() == i) continue;
            if (edgeFrom.getRed() == i || edgeFrom.getGreen() == i || edgeFrom.getBlue() == i) continue;
            if (edgeTo.getRed() == i || edgeTo.getGreen() == i || edgeTo.getBlue() == i) continue;
            if (highlightBottom.getRed() == i || highlightBottom.getGreen() == i || highlightBottom.getBlue() == i) continue;
            if (highlightInnerFrom.getRed() == i || highlightInnerFrom.getGreen() == i || highlightInnerFrom.getBlue() == i) continue;
            if (highlightInnerTo.getRed() == i || highlightInnerTo.getGreen() == i || highlightInnerTo.getBlue() == i) continue;
            if (highlightSpecular.getRed() == i || highlightSpecular.getGreen() == i || highlightSpecular.getBlue() == i) continue;
            if (indicatorInner.getRed() == i || indicatorInner.getGreen() == i || indicatorInner.getBlue() == i) continue;
            if (indicatorOuter.getRed() == i || indicatorOuter.getGreen() == i || indicatorOuter.getBlue() == i) continue;
            if (plateauInner.getRed() == i || plateauInner.getGreen() == i || plateauInner.getBlue() == i) continue;
            if (plateauOuter.getRed() == i || plateauOuter.getGreen() == i || plateauOuter.getBlue() == i) continue;
            if (shadow.getRed() == i || shadow.getGreen() == i || shadow.getBlue() == i) continue;
            if (tick.getRed() == i || tick.getGreen() == i || tick.getBlue() == i) continue;
            break;
        }
        return i;
    }
}
