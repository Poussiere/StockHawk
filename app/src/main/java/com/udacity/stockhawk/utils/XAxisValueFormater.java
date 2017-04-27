package com.udacity.stockhawk.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;


public class XAxisValueFormater implements IAxisValueFormatter {


    public XAxisValueFormater() {

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        //Value is a time stamp that get Time in millis
        long date = (long) value;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);


        return month + "/" + year;


    }
}
