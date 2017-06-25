package com.parot.mtecgwa_jr.myrecipes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.Recipe;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.MyRecipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 5/29/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<MyRecipe> recipeList = new ArrayList<>();
    private Context context;
    private final String RECIPE_IMAGE = "recipe_image";
    private final String RECIPE_NAME = "recipe_name";
    private final String RECIPE_ID = "recipe_id";

    public RecipeAdapter(Context context , ArrayList<MyRecipe> recipeList)
    {
        this.context = context;
        this.recipeList = recipeList;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageView recipeImage;
        FrameLayout recipeContainer;
        public RecipeViewHolder(View itemView) {
            super(itemView);

            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
            recipeContainer = (FrameLayout) itemView.findViewById(R.id.recipe_container);

        }
    }
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View recipeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_recipe , parent , false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(recipeView);


        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        final MyRecipe myRecipe = recipeList.get(position);

        holder.recipeName.setText(myRecipe.getRecipeName());

        Picasso.with(context)
                .load(myRecipe.getRecipeImageResource())
                .into(holder.recipeImage);

        holder.recipeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toRecipe = new Intent(context , Recipe.class);
                toRecipe.putExtra(RECIPE_IMAGE , myRecipe.getRecipeImageResource());
                toRecipe.putExtra(RECIPE_NAME , myRecipe.getRecipeName());
                toRecipe.putExtra(RECIPE_ID , myRecipe.getRecipeId());
                toRecipe.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(toRecipe);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}
