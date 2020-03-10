/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.laf.modern.ui;

import com.patrickangle.commons.util.MathUtils;
import com.patrickangle.commons.util.OperatingSystems;
import com.patrickangle.commons.util.TimeUtils;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author patrickangle
 */
public class ModernSpinnerScrollAdapter implements MouseWheelListener {
    private final int MASK_ADJUST_LARGER = (OperatingSystems.current() == OperatingSystems.Macintosh) ? KeyEvent.ALT_DOWN_MASK : KeyEvent.SHIFT_DOWN_MASK;
        private final int MASK_ADJUST_SMALLER = (OperatingSystems.current() == OperatingSystems.Macintosh) ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK;

        private static final float SMALL_MULTIPLIER = .1f;
        private static final float NORMAL_MULTIPLIER = 1f;
        private static final float LARGE_MULTIPLIER = 10f;
        
        private final JSpinner spinner;
        
        public ModernSpinnerScrollAdapter(JSpinner spinner) {
            this.spinner = spinner;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            boolean larger = (e.getModifiersEx() & MASK_ADJUST_LARGER) == MASK_ADJUST_LARGER;
            boolean smaller = (e.getModifiersEx() & MASK_ADJUST_SMALLER) == MASK_ADJUST_SMALLER;

            SpinnerModel model = spinner.getModel();
            if (model instanceof SpinnerNumberModel) {
                float adjustmentMultiplier = 0;
                if (!(larger ^ smaller)) {
                    // Normal, since either neither or both keys are being held.
                    adjustmentMultiplier = NORMAL_MULTIPLIER;
                } else if (larger) {
                    adjustmentMultiplier = LARGE_MULTIPLIER;
                } else if (smaller) {
                    adjustmentMultiplier = SMALL_MULTIPLIER;
                }

                // TODO: This doesn't work as expected on a trackpad set up for natural scrolling…
                adjustmentMultiplier *= -e.getWheelRotation();

                SpinnerNumberModel numberModel = (SpinnerNumberModel) model;
                double adjustment = numberModel.getStepSize().doubleValue() * adjustmentMultiplier;
                double newValue = ((Number) numberModel.getValue()).doubleValue() + adjustment;

                // The comparables will be numbers of some kind.
                newValue = MathUtils.clamp(((Number) numberModel.getMinimum()).doubleValue(), newValue, ((Number) numberModel.getMaximum()).doubleValue());
                numberModel.setValue(newValue);
            } else if (model instanceof SpinnerDateModel) {
                SpinnerDateModel dateModel = (SpinnerDateModel) model;

                int unit = dateModel.getCalendarField();
                if (larger && !smaller) {
                    unit = largerCalendarField(dateModel.getCalendarField());
                } else if (smaller && !larger) {
                    unit = smallerCalendarField(dateModel.getCalendarField());
                }

                Date newValue = TimeUtils.roll(dateModel.getDate(), unit, e.getWheelRotation());

                dateModel.setValue(newValue);
            }
        }

        private final int smallerCalendarField(int field) {
            switch (field) {
                case Calendar.ERA:
                    return Calendar.YEAR;
                case Calendar.YEAR:
                    return Calendar.MONTH;
                case Calendar.MONTH:
                case Calendar.WEEK_OF_YEAR:
                case Calendar.WEEK_OF_MONTH:
                    return Calendar.DAY_OF_MONTH;
                case Calendar.DAY_OF_MONTH:
                case Calendar.DAY_OF_WEEK:
                case Calendar.DAY_OF_WEEK_IN_MONTH:
                case Calendar.DAY_OF_YEAR:
                case Calendar.AM_PM:
                    return Calendar.HOUR_OF_DAY;
                case Calendar.HOUR:
                case Calendar.HOUR_OF_DAY:
                    return Calendar.MINUTE;
                case Calendar.MINUTE:
                    return Calendar.SECOND;
                case Calendar.SECOND:
                case Calendar.MILLISECOND:
                    return Calendar.MILLISECOND;
                default:
                    return Calendar.DAY_OF_MONTH;
            }
        }

        private final int largerCalendarField(int field) {
            switch (field) {
                case Calendar.ERA:
                case Calendar.YEAR:
                    return Calendar.ERA;
                case Calendar.MONTH:
                    return Calendar.YEAR;
                case Calendar.WEEK_OF_YEAR:
                case Calendar.WEEK_OF_MONTH:
                case Calendar.DAY_OF_MONTH:
                case Calendar.DAY_OF_WEEK:
                case Calendar.DAY_OF_WEEK_IN_MONTH:
                case Calendar.DAY_OF_YEAR:
                    return Calendar.MONTH;
                case Calendar.AM_PM:
                case Calendar.HOUR:
                case Calendar.HOUR_OF_DAY:
                    return Calendar.DAY_OF_MONTH;
                case Calendar.MINUTE:
                    return Calendar.HOUR_OF_DAY;
                case Calendar.SECOND:
                    return Calendar.MINUTE;
                case Calendar.MILLISECOND:
                    return Calendar.SECOND;
                default:
                    return Calendar.DAY_OF_MONTH;
            }
        }
}
