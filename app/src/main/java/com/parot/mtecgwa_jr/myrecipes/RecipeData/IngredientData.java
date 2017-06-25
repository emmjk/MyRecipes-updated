package com.parot.mtecgwa_jr.myrecipes.RecipeData;

/**
 * Created by mtecgwa-jr on 6/1/17.
 */

public class IngredientData {

    private String ingredientName;
    private String measure;
    private double value;

    public IngredientData(String ingredientName , String measure , double value)
    {
        this.ingredientName = ingredientName;
        this.measure = measure;
        this.value = value;
    }

    public String getIngredientName(){ return ingredientName; }
    public String getMeasure() { return measure; }
    public double getValue() { return value; }

}
