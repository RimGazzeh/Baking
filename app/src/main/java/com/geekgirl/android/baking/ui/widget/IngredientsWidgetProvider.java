package com.geekgirl.android.baking.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.geekgirl.android.baking.R;
import com.geekgirl.android.baking.data.AppDatabase;
import com.geekgirl.android.baking.model.Dessert;
import com.geekgirl.android.baking.model.Ingredient;
import com.geekgirl.android.baking.service.AppExecutors;
import com.geekgirl.android.baking.utils.Constants;
import com.geekgirl.android.baking.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    private static AppDatabase mDb;
    private static Dessert mDessert;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
        Intent widgetIntent = new Intent(context, WidgetRemoteViewsService.class);
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Dessert> dessertList = mDb.dessertDAO().getSavedDessert();
            if (dessertList != null && dessertList.size() > 0) {
                mDessert = dessertList.get(0);
                Logger.d(mDessert.getName());
                views.setTextViewText(R.id.appwidget_title, mDessert.getName());
                ArrayList<String> ingredients = new ArrayList<>();
                for (Ingredient ingredient : mDessert.getIngredients()) {
                    ingredients.add(ingredient.getFullIngredient());
                }
                Logger.d(ingredients.toString());
                Logger.d(ingredients.toString());
                widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                widgetIntent.putStringArrayListExtra(Constants.INGREDIENT_LIST_WIDGET_EXTRA, ingredients);
                views.setRemoteAdapter(R.id.widget_list_ingredients, widgetIntent);
                views.setEmptyView(R.id.widget_list_ingredients, R.id.widget_emptyView);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mDb = AppDatabase.getInstance(context);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


}



