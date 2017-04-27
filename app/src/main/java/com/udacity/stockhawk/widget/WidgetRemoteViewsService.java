package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.MainActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class WidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        return new RemoteViewsFactory() {
            private Cursor data = null;
            private DecimalFormat dollarFormat;
            private DecimalFormat percentageFormat;

            @Override
            public void onCreate() {


                dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
                percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
                percentageFormat.setMaximumFractionDigits(2);
                percentageFormat.setMinimumFractionDigits(2);
                percentageFormat.setPositivePrefix("+");

                // QuoteSyncJob.initialize(getApplicationContext());

            }

            @Override
            public void onDataSetChanged() {


                if (data != null) {
                    data.close();
                }


                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                        null, null, Contract.Quote.COLUMN_SYMBOL);

                Binder.restoreCallingIdentity(identityToken);


            }


            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                if (data == null) {
                    return 0;
                } else {
                    return data.getCount();
                }
            }


            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item);


                String symbol = data.getString(Contract.Quote.POSITION_SYMBOL);
                String price = dollarFormat.format(data.getFloat(Contract.Quote.POSITION_PRICE));
                float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);


                String percentage = percentageFormat.format(percentageChange / 100);


                views.setTextViewText(R.id.widget_symbol, symbol);
                views.setTextViewText(R.id.widget_price, price);
                views.setTextViewText(R.id.widget_change, percentage);


                //The intent to launch evoActivity
                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(MainActivity.EXTRA_SYMBOL_KEY, symbol);
                views.setOnClickFillInIntent(R.id.widget_item_id, fillInIntent);

                return views;
            }

            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_symbol, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {

                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(Contract.Quote.POSITION_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {

                return true;
            }
        };
    }
}
