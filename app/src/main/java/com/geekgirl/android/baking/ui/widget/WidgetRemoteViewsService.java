package com.geekgirl.android.baking.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Rim Gazzah on 23/10/18
 */
public class WidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewFactory(getApplicationContext(), intent);
    }

    class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext;
        Intent mIntent;
        ArrayList<String> mIngredients = new ArrayList<>();
        int mAppWidgetId;

        public WidgetRemoteViewFactory(Context context, Intent intent) {
            mContext = context;
            mIntent = intent;
            getData();
        }

        private void getData() {
            mAppWidgetId = mIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            mIngredients = mIntent.getStringArrayListExtra(Constants.INGREDIENT_LIST_WIDGET_EXTRA);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            getData();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return mIngredients == null ? 0 : mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                    R.layout.widget_ingredient);
            remoteView.setTextViewText(R.id.ingredient_widget_title, mIngredients.get(position));
            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
