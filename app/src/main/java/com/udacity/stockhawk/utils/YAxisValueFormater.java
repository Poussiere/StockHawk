package com.udacity.stockhawk.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by poussiere on 09/04/17.
 */

public class YAxisValueFormater implements IAxisValueFormatter {

    private DecimalFormat mFormat;

    public YAxisValueFormater()

    {
        //Prices have one decimal
        mFormat = new DecimalFormat("###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value)+" $";
    }
}
