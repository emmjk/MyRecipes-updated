package com.parot.mtecgwa_jr.myrecipes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.IngredientData;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 6/1/17.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<IngredientData> ingredientList = new ArrayList<>();
    private Context context;

    public IngredientAdapter(Context context , ArrayList<IngredientData> ingredientList)
    {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName , ingredientContent;
        RelativeLayout ingredientContainer;
        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientContent = (TextView) itemView.findViewById(R.id.quantity);
            ingredientName = (TextView) itemView.findViewById(R.id.name);
            ingredientContainer = (RelativeLayout) itemView.findViewById(R.id.ingredient_container);
        }
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View ingredientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient , parent , false);

        IngredientViewHolder ingredientViewHolder = new IngredientViewHolder(ingredientView);
        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {

        IngredientData ingredient = ingredientList.get(position);

        holder.ingredientName.setText(ingredient.getIngredientName());
        String quantity = ingredient.getValue()+" "+ingredient.getMeasure();
        holder.ingredientContent.setText(quantity);
        if(position % 2 == 0)
        {
            holder.ingredientContainer.setBackgroundResource(R.color.lightgrey);
        }
        else
        {
            holder.ingredientContainer.setBackgroundResource(R.color.white);

        }

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }


}
