package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.utils.GraphsUtils;
import com.udacity.stockhawk.utils.XAxisValueFormater;
import com.udacity.stockhawk.utils.YAxisValueFormater;

import java.util.ArrayList;
import java.util.List;

public class EvoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STOCK_HISTORY_LOADER = 0;


    // A URI to request quote (and history of quotes) of a unique stock
    private Uri uniqueStockRequest;

    // The symbol of the stock that is requested by user
    private String symbol;

    //String [] for the projection in order to request only history column of the database
    private String[] projection = {Contract.Quote.COLUMN_HISTORY};

    //A Stirng to retrieve history from the cursor
    private String stringHistory;

    //The line chart
    private LineChart mLineChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evo);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);


        mLineChart = (LineChart) findViewById(R.id.line_chart);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setBackgroundColor(Color.WHITE);


        XAxis xAxis = mLineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        XAxisValueFormater xFormater = new XAxisValueFormater();
        xAxis.setValueFormatter(xFormater);


        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);

        //format the y values to draw a $ symbol after each price
        YAxisValueFormater yFormater = new YAxisValueFormater();
        leftAxis.setValueFormatter(yFormater);


        mLineChart.animateX(2500);


        mLineChart.getAxisRight().setEnabled(false);
        //Retrieve the stock symbol that has been requested
        Intent i = getIntent();
        symbol = i.getStringExtra(MainActivity.EXTRA_SYMBOL_KEY);

        //Make an uri with this symbol
        uniqueStockRequest = Contract.Quote.makeUriForStock(symbol);

        //iniate the loader
        getSupportLoaderManager().initLoader(STOCK_HISTORY_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uniqueStockRequest,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()) {
            stringHistory = data.getString(0);
            List<Entry> valList = GraphsUtils.fromStringToEntryList(stringHistory);
            LineDataSet line1 = new LineDataSet(valList, symbol);
            line1.setAxisDependency(YAxis.AxisDependency.LEFT);
            List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(line1);
            LineData lineData = new LineData(dataSets);
            mLineChart.setData(lineData);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Return to mainactivity when home button is clicked
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
