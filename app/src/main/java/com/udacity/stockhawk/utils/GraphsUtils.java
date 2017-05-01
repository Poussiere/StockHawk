package com.udacity.stockhawk.utils;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphsUtils {


    //This method accepts a String and transform it into a list of Entry objects.
    // This is necessary to pass the datas to the graph

    public static List<Entry> fromStringToEntryList(String s) {
        List<Entry> graphValList = new ArrayList<Entry>();

        Entry tempEntry;

        //We split the file in order to make a tab which each case is a line
        String[] lines = s.split("\n");
        float x;
        float y;
        String[] lineSplit;

        for (int i = 0; i < lines.length; i++)

        {
            //We split each line in order to retrieve x and y values


            lineSplit = lines[i].split(", ");

            if (null!=lineSplit) {

                x = Float.valueOf(lineSplit[0]);
                y = Float.valueOf(lineSplit[1]);
                tempEntry = new Entry(x, y);
                graphValList.add(tempEntry);
            }
        }

        Collections.reverse(graphValList);
        return graphValList;


    }
}
