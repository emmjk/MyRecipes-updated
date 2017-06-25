package com.parot.mtecgwa_jr.myrecipes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.MyRecipe;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 6/25/17.
 */

public class GridAdapter extends BaseAdapter {

    private ArrayList<String> recipeList = new ArrayList<>();
    private Context context;
    private View view;
    private LayoutInflater layoutInflater;

    public GridAdapter(Context context , ArrayList<String> recipeList)
    {
        this.context = context ;
        this.recipeList = recipeList;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {

        String myRecipe = recipeList.get(position);

        return myRecipe;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        String myRecipe = recipeList.get(position);

        if(convertView == null)
        {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.grid_item , null);

            TextView recipeName = (TextView) view.findViewById(R.id.grid_text);
            recipeName.setText(myRecipe);
        }

        return view;
    }
}
