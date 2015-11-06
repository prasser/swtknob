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

/**
 * A range for SWTKnob
 * @author Fabian Prasser
 *
 * @param <T>
 */
public abstract class KnobRange<T> {
    
    /**
     * A char range
     * 
     * @author Fabian Prasser
     */
    public static class Character extends KnobRange<java.lang.Character> {

        /**
         * Default constructor. Range is Character.MIN_VALUE -
         * Character.MAX_VALUE
         * 
         * @param minimum
         * @param maximum
         */
        public Character() {
            this(java.lang.Character.MIN_VALUE, java.lang.Character.MAX_VALUE);
        }

        /**
         * Defines minimum and maximum values
         * 
         * @param minimum
         * @param maximum
         */
        public Character(java.lang.Character minimum, java.lang.Character maximum) {
            super(minimum, maximum);
            if (minimum >= maximum) { 
                throw new IllegalArgumentException("Minimum (" + minimum + ") must be < maximum (" + maximum + ")"); 
            }
        }

        @Override
        protected double getStepping() {
            return toInternal((char)(minimum + 1)) - toInternal(minimum);
        }

        @Override
        protected java.lang.Character toExternal(double value) {
            return (char) Math.round(((double) value * ((double)maximum - (double)minimum) + (double) minimum));
        }

        @Override
        protected double toInternal(java.lang.Character value) {
            if (value < minimum) {
                throw new IllegalArgumentException("Value (" + value + ") must be >= minimum (" + minimum + ")");
            } else if (value > maximum) { 
                throw new IllegalArgumentException("Value (" + value + ") must be <= maximum (" + maximum + ")"); 
            }
            return ((double)value - (double)minimum) / ((double)maximum - (double)minimum);
        }

        @Override
        protected double toNearestInternal(double value) {
            char extValue = toExternal(value);
            return ((double)extValue - (double)minimum) / ((double)maximum - (double)minimum);
        }
    }

    /**
     * A double range
     * 
     * @author Fabian Prasser
     */
    public static class Double extends KnobRange<java.lang.Double> {

        /**
         * Default constructor. Range is -Double.MAX_VALUE - Double.MAX_VALUE
         * 
         * @param minimum
         * @param maximum
         */
        public Double() {
            this(-java.lang.Double.MAX_VALUE, java.lang.Double.MAX_VALUE);
        }

        /**
         * Defines minimum and maximum values
         * 
         * @param minimum
         * @param maximum
         */
        public Double(java.lang.Double minimum, java.lang.Double maximum) {
            super(minimum, maximum);
            if (minimum >= maximum) { 
                throw new IllegalArgumentException("Minimum (" + minimum + ") must be < maximum (" + maximum + ")"); 
            }
        }

        @Override
        protected double getStepping() {
            return 0;
        }

        @Override
        protected java.lang.Double toExternal(double value) {
            return value * (maximum - minimum) + minimum;
        }

        @Override
        protected double toInternal(java.lang.Double value) {
            if (value < minimum) {
                throw new IllegalArgumentException("Value (" + value + ") must be >= minimum (" + minimum + ")");
            } else if (value > maximum) { 
                throw new IllegalArgumentException("Value (" + value + ") must be <= maximum (" + maximum + ")"); 
            }
            return (value - minimum) / (maximum - minimum);
        }

        @Override
        protected double toNearestInternal(double value) {
            return value;
        }
    }

    /**
     * A float range
     * 
     * @author Fabian Prasser
     */
    public static class Float extends KnobRange<java.lang.Float> {

        /**
         * Default constructor. Range is Float.MIN_VALUE - Float.MAX_VALUE
         * 
         * @param minimum
         * @param maximum
         */
        public Float() {
            this(java.lang.Float.MIN_VALUE, java.lang.Float.MAX_VALUE);
        }

        /**
         * Defines minimum and maximum values
         * 
         * @param minimum
         * @param maximum
         */
        public Float(java.lang.Float minimum, java.lang.Float maximum) {
            super(minimum, maximum);
            if (minimum >= maximum) { 
                throw new IllegalArgumentException("Minimum (" + minimum + ") must be < maximum (" + maximum + ")"); 
            }
        }

        @Override
        protected double getStepping() {
            return 0;
        }

        @Override
        protected java.lang.Float toExternal(double value) {
            return (float) ((double) value * ((double)maximum - (double)minimum) + (double) minimum);
        }

        @Override
        protected double toInternal(java.lang.Float value) {
            if (value < minimum) {
                throw new IllegalArgumentException("Value (" + value + ") must be >= minimum (" + minimum + ")");
            } else if (value > maximum) { 
                throw new IllegalArgumentException("Value (" + value + ") must be <= maximum (" + maximum + ")"); 
            }
            return ((double)value - (double)minimum) / ((double)maximum - (double)minimum);
        }

        @Override
        protected double toNearestInternal(double value) {
            return value;
        }
    }
    /**
     * An integer range
     * 
     * @author Fabian Prasser
     */
    public static class Integer extends KnobRange<java.lang.Integer> {

        /**
         * Default constructor. Range is Integer.MIN_VALUE - Integer.MAX_VALUE
         * 
         * @param minimum
         * @param maximum
         */
        public Integer() {
            this(java.lang.Integer.MIN_VALUE, java.lang.Integer.MAX_VALUE);
        }

        /**
         * Defines minimum and maximum values
         * 
         * @param minimum
         * @param maximum
         */
        public Integer(java.lang.Integer minimum, java.lang.Integer maximum) {
            super(minimum, maximum);
            if (minimum >= maximum) { 
                throw new IllegalArgumentException("Minimum (" + minimum + ") must be < maximum (" + maximum + ")"); 
            }
        }

        @Override
        protected double getStepping() {
            return toInternal(minimum + 1) - toInternal(minimum);
        }

        @Override
        protected java.lang.Integer toExternal(double value) {
            return (int) Math.round(((double) value * ((double)maximum - (double)minimum) + (double) minimum));
        }

        @Override
        protected double toInternal(java.lang.Integer value) {
            if (value < minimum) {
                throw new IllegalArgumentException("Value (" + value + ") must be >= minimum (" + minimum + ")");
            } else if (value > maximum) { 
                throw new IllegalArgumentException("Value (" + value + ") must be <= maximum (" + maximum + ")"); 
            }
            return ((double)value - (double)minimum) / ((double)maximum - (double)minimum);
        }

        @Override
        protected double toNearestInternal(double value) {
            int extValue = toExternal(value);
            return ((double)extValue - (double)minimum) / ((double)maximum - (double)minimum);
        }
    }

    /**
     * A long range
     * 
     * @author Fabian Prasser
     */
    public static class Long extends KnobRange<java.lang.Long> {

        /**
         * Default constructor. Range is Long.MIN_VALUE - Long.MAX_VALUE
         * 
         * @param minimum
         * @param maximum
         */
        public Long() {
            this(java.lang.Long.MIN_VALUE, java.lang.Long.MAX_VALUE);
        }

        /**
         * Defines minimum and maximum values
         * 
         * @param minimum
         * @param maximum
         */
        public Long(java.lang.Long minimum, java.lang.Long maximum) {
            super(minimum, maximum);
            if (minimum >= maximum) { 
                throw new IllegalArgumentException("Minimum (" + minimum + ") must be < maximum (" + maximum + ")"); 
            }
        }


        @Override
        protected double getStepping() {
            return toInternal(minimum + 1l) - toInternal(minimum);
        }

        @Override
        protected java.lang.Long toExternal(double value) {
            return (long) Math.round(((double) value * ((double)maximum - (double)minimum) + (double) minimum));
        }

        @Override
        protected double toInternal(java.lang.Long value) {
            if (value < minimum) {
                throw new IllegalArgumentException("Value (" + value + ") must be >= minimum (" + minimum + ")");
            } else if (value > maximum) { 
                throw new IllegalArgumentException("Value (" + value + ") must be <= maximum (" + maximum + ")"); 
            }
            return ((double)value - (double)minimum) / ((double)maximum - (double)minimum);
        }


        @Override
        protected double toNearestInternal(double value) {
            long extValue = toExternal(value);
            return ((double)extValue - (double)minimum) / ((double)maximum - (double)minimum);
        }
    }

    /** Minimum sensitivity*/
    private static final double MIN_SENSITIVITY = 1000d;

    /** Maximum sensitivity*/
    private static final double MAX_SENSITIVITY = 10000d;

    /** The minimum*/
    protected final T minimum;

    /** The maximum*/
    protected final T maximum;

    /** 
     * Creates a new range
     * @param minimum
     * @param maximum
     */
    protected KnobRange(T minimum, T maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Returns the maximum
     * @return
     */
    public T getMaximum() {
        return maximum;
    }

    /**
     * Returns the minimum
     * @return
     */
    public T getMinimum() {
        return minimum;
    }

    /**
     * Returns the sensitivity
     * @return
     */
    public double getSensitivity() {
        double result = (double)(toInternal(maximum) - toInternal(minimum));
        result = result < MIN_SENSITIVITY ? MIN_SENSITIVITY : result;
        result = result > MAX_SENSITIVITY ? MAX_SENSITIVITY : result;
        return result;
    }

    /**
     * Returns the stepping
     * @return
     */
    protected abstract double getStepping();

    /**
     * Converts the internal to an external value
     * @param value
     * @return
     */
    protected abstract T toExternal(double value);

    /**
     * Creates an external value to the internal value
     * @param value
     * @return
     */
    protected abstract double toInternal(T value);
    
    /**
     * Returns the nearest internal representation for the given external value
     * @param value
     * @return
     */
    protected abstract double toNearestInternal(double value);
}
