package com.parot.mtecgwa_jr.myrecipes;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.parot.mtecgwa_jr.myrecipes.Adapter.RecipeFragmentAdapter;
import com.parot.mtecgwa_jr.myrecipes.Fragments.RecipeIngridientFragment;
import com.parot.mtecgwa_jr.myrecipes.Fragments.RecipeInstructionFragment;
import com.parot.mtecgwa_jr.myrecipes.RecipeWidget.MyRemoteViewsFactory;
import com.squareup.picasso.Picasso;

public class Recipe extends AppCompatActivity {

    private final String RECIPE_IMAGE = "recipe_image";
    private final String RECIPE_NAME = "recipe_name";
    private final String RECIPE_ID = "recipe_id";

    private static int recipeImageResource;
    private static int recipeId;
    private static String recipeName;

    private Toolbar toolbar;
    private ImageView imageHeader;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private final String TAG = Recipe.class.getName();

    private RecipeFragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent myRecipe = getIntent();

        recipeImageResource = myRecipe.getIntExtra(RECIPE_IMAGE , 24);
        recipeId = myRecipe.getIntExtra(RECIPE_ID , 24);
        recipeName = myRecipe.getStringExtra(RECIPE_NAME);

        setupEnvironment();

        Picasso.with(this)
                .load(recipeImageResource)
                .error(R.drawable.dinner)
                .placeholder(R.drawable.dinner)
                .into(imageHeader);

        setupTabLayoutWithFragment();


    }

    public void setupEnvironment()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(recipeName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(recipeName);

                    isShow = false;
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        imageHeader = (ImageView) findViewById(R.id.image_header);
    }

    public void setupTabLayoutWithFragment()
    {


        fragmentAdapter = new RecipeFragmentAdapter(getSupportFragmentManager());

        fragmentAdapter.addFragments(new RecipeIngridientFragment() , "INGRIDIENTS");
        fragmentAdapter.addFragments(new RecipeInstructionFragment() , "INSTRUCTIONS");

        viewPager.setAdapter(fragmentAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }



    public static int getRecipeId(){ return recipeId; }
    public static int getRecipeImageResource() { return recipeImageResource; }
    public static  String getRecipeName() { return recipeName; }
}
