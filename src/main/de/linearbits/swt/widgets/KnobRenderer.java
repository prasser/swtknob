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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * This class renders an oval with 3d effects. It mostly consists of code borrowed from the TimingFramework and JFreeChart.
 * 
 * @author Fabian Prasser
 * @author Romain Guy
 * @author Henry Proudhon
 * @author Rainer Blessing
 * @author David Gilbert
 * @author Christoph Beck
 */
class KnobRenderer {

    /**
     * JFreeChart : a free chart library for the Java(tm) platform
     * 
     * 
     * (C) Copyright 2000-2007, by Object Refinery Limited and Contributors.
     * 
     * Project Info: http://www.jfree.org/jfreechart/index.html
     * 
     * This library is free software; you can redistribute it and/or modify it
     * under the terms of the GNU Lesser General Public License as published by
     * the Free Software Foundation; either version 2.1 of the License, or (at
     * your option) any later version.
     * 
     * This library is distributed in the hope that it will be useful, but
     * WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
     * General Public License for more details.
     * 
     * You should have received a copy of the GNU Lesser General Public License
     * along with this library; if not, write to the Free Software Foundation,
     * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
     * 
     * [Java is a trademark or registered trademark of Sun Microsystems, Inc. in
     * the United States and other countries.]
     * 
     * ------------- SWTUtils.java ------------- (C) Copyright 2006, 2007, by
     * Henry Proudhon and Contributors.
     * 
     * Original Author: Henry Proudhon (henry.proudhon AT ensmp.fr);
     * Contributor(s): Rainer Blessing; David Gilbert
     * (david.gilbert@object-refinery.com); Christoph Beck.
     * 
     * Changes ------- 01-Aug-2006 : New class (HP); 16-Jan-2007 : Use
     * FontData.getHeight() instead of direct field access (RB); 31-Jan-2007 :
     * Moved the dummy JPanel from SWTGraphics2D.java, added a new convert
     * method for mouse events (HP); 12-Jul-2007 : Improved the mouse event
     * conversion with buttons and modifiers handling, patch sent by Christoph
     * Beck (HP); 27-Aug-2007 : Modified toAwtMouseEvent signature (HP);
     * 27-Nov-2007 : Moved convertToSWT() method from SWTGraphics2D and added
     * convertAWTImageToSWT() (DG); 01-Jul-2008 : Simplify AWT/SWT font style
     * conversions (HP);
     * 
     */
    private ImageData convertToSWT(BufferedImage bufferedImage) {

        if (bufferedImage.getColorModel() instanceof DirectColorModel) {
            DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
            PaletteData palette = new PaletteData(colorModel.getRedMask(),
                                                  colorModel.getGreenMask(),
                                                  colorModel.getBlueMask());
            ImageData data = new ImageData(bufferedImage.getWidth(),
                                           bufferedImage.getHeight(),
                                           colorModel.getPixelSize(),
                                           palette);
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[3];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    int pixel = palette.getPixel(new RGB(pixelArray[0],
                                                         pixelArray[1],
                                                         pixelArray[2]));
                    data.setPixel(data.width - x - 1, data.height - y - 1, pixel);
                }
            }
            return data;
        } else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
            IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
            int size = colorModel.getMapSize();
            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            colorModel.getReds(reds);
            colorModel.getGreens(greens);
            colorModel.getBlues(blues);
            RGB[] rgbs = new RGB[size];
            for (int i = 0; i < rgbs.length; i++) {
                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
            }
            PaletteData palette = new PaletteData(rgbs);
            ImageData data = new ImageData(bufferedImage.getWidth(),
                                           bufferedImage.getHeight(),
                                           colorModel.getPixelSize(),
                                           palette);
            data.transparentPixel = colorModel.getTransparentPixel();
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    data.setPixel(data.width - x - 1, data.height - y - 1, pixelArray[0]);
                }
            }
            return data;
        }
        return null;
    }

    /**
     * Copyright (c) 2007, Romain Guy All rights reserved.
     * 
     * Redistribution and use in source and binary forms, with or without
     * modification, are permitted provided that the following conditions are
     * met:
     * 
     * * Redistributions of source code must retain the above copyright notice,
     * this list of conditions and the following disclaimer. * Redistributions
     * in binary form must reproduce the above copyright notice, this list of
     * conditions and the following disclaimer in the documentation and/or other
     * materials provided with the distribution. * Neither the name of the
     * TimingFramework project nor the names of its contributors may be used to
     * endorse or promote products derived from this software without specific
     * prior written permission.
     * 
     * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
     * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
     * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
     * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
     * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
     * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
     * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
     * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
     * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
     * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
     * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
     */
    private void render(Graphics g, Color background, int width, int height) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Retains the previous state
        Paint oldPaint = g2.getPaint();

        g2.setColor(background);
        g2.fillRect(0, 0, width, height);

        // Fills the circle with solid blue color
        g2.setColor(new Color(190, 190, 190));
        g2.fillOval(0, 0, width - 1, height - 1);

        // Adds shadows at the top
        Paint p;
        p = new GradientPaint(0, 0, new Color(0.0f, 0.0f, 0.0f, 0.4f), 0, height, new Color(0.0f,
                                                                                            0.0f,
                                                                                            0.0f,
                                                                                            0.0f));
        g2.setPaint(p);
        g2.fillOval(0, 0, width - 1, height - 1);

        // Adds highlights at the bottom
        p = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.0f), 0, height, new Color(1.0f,
                                                                                            1.0f,
                                                                                            1.0f,
                                                                                            0.4f));
        g2.setPaint(p);
        g2.fillOval(0, 0, width - 1, height - 1);

        // Creates dark edges for 3D effect
        p = new RadialGradientPaint(new Point2D.Double(width / 2.0, height / 2.0),
                                    width / 2.0f,
                                    new float[] { 0.0f, 1.0f },
                                    new Color[] { new Color(6, 76, 160, 127),
                                            new Color(0.0f, 0.0f, 0.0f, 0.8f) });
        g2.setPaint(p);
        g2.fillOval(0, 0, width - 1, height - 1);

        // Adds oval inner highlight at the bottom
        p = new RadialGradientPaint(new Point2D.Double(width / 2.0, height * 1.5),
                                    width / 2.3f,
                                    new Point2D.Double(width / 2.0, height * 1.75 + 6),
                                    new float[] { 0.0f, 0.8f },
                                    new Color[] { new Color(64, 142, 203, 255),
                                            new Color(64, 142, 203, 0) },
                                    RadialGradientPaint.CycleMethod.NO_CYCLE,
                                    RadialGradientPaint.ColorSpaceType.SRGB,
                                    AffineTransform.getScaleInstance(1.0, 0.5));
        g2.setPaint(p);
        g2.fillOval(0, 0, width - 1, height - 1);

        // Adds oval specular highlight at the top left
        p = new RadialGradientPaint(new Point2D.Double(width / 2.0, height / 2.0),
                                    width / 1.4f,
                                    new Point2D.Double(45.0, 25.0),
                                    new float[] { 0.0f, 0.5f },
                                    new Color[] { new Color(1.0f, 1.0f, 1.0f, 0.4f),
                                            new Color(1.0f, 1.0f, 1.0f, 0.0f) },
                                    RadialGradientPaint.CycleMethod.NO_CYCLE);
        g2.setPaint(p);
        g2.fillOval(0, 0, width - 1, height - 1);

        // Restores the previous state
        g2.setPaint(oldPaint);
    }

    /**
     * Renders a knob with the given width and height
     * 
     * @param background
     * @param width
     * @param height
     * @return
     */
    Image render(Display display, org.eclipse.swt.graphics.Color background, int width, int height) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        render(g, new Color(background.getRed(), background.getGreen(), background.getBlue()), width, height);
        g.dispose();
        ImageData data = convertToSWT(image);
        data.transparentPixel = data.palette.getPixel(background.getRGB());
        return new Image(display, data);
    }
}
