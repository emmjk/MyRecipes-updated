package com.parot.mtecgwa_jr.myrecipes.RecipeData;

/**
 * Created by mtecgwa-jr on 5/29/17.
 */

public class MyRecipe {

    private String recipeName;
    private int recipeImageResource;
    private int recipeId;

    public MyRecipe(String recipeName , int recipeImageResource , int  recipeId)
    {
        this.recipeName = recipeName ;
        this.recipeImageResource = recipeImageResource;
        this.recipeId = recipeId;
    }

    public String getRecipeName()
    {
        return recipeName;
    }

    public int getRecipeImageResource()
    {
        return recipeImageResource;
    }
    public int getRecipeId()
    {
        return recipeId;
    }

}
