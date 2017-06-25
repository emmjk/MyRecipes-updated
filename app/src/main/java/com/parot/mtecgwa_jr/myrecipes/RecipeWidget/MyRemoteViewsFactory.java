package com.parot.mtecgwa_jr.myrecipes.RecipeWidget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.parot.mtecgwa_jr.myrecipes.Fragments.RecipeIngridientFragment;
import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.IngredientData;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 6/25/17.
 */

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    private ArrayList<String> ingredientList = new ArrayList<>();


    public MyRemoteViewsFactory(Context context , Intent intent)
    {
        this.context = context;
    }

    @Override
    public void onCreate() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String ingredients = sharedPreferences.getString("key" , "");

        String[] testArray = ingredients.split("/");

        for(int i = 0 ; i < testArray.length ; i++)
        {
            ingredientList.add(testArray[i]);
        }

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list_item);
        rv.setTextViewText(R.id.name, ingredientList.get(position));

        return rv;
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
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

