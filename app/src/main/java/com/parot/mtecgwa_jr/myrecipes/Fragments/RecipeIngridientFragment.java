package com.parot.mtecgwa_jr.myrecipes.Fragments;


import android.app.LoaderManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.Adapter.IngredientAdapter;
import com.parot.mtecgwa_jr.myrecipes.NetworkUtils.CheckForInternet;
import com.parot.mtecgwa_jr.myrecipes.NetworkUtils.NetworkRequest;
import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.Recipe;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.IngredientData;
import com.parot.mtecgwa_jr.myrecipes.RecipeWidget.MyRemoteViewsFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngridientFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<IngredientData>> {


    public RecipeIngridientFragment() {
        // Required empty public constructor
    }

    private IngredientAdapter ingredientAdapter;
    private RecyclerView ingredientReycler;
    private ProgressBar progressIngredient;
    private TextView internetConnectionWarning;
    private Button refreshButton;
    private static ArrayList<IngredientData> ingredientList = new ArrayList();
    private CheckForInternet checkForInternet;
    private ArrayList<String> ingredientsOnlyList = new ArrayList<>();

    private final String TAG = RecipeIngridientFragment.class.getName();

    private final int INGREDIENT_LOADER_ID = 24;

    private final int recipeId = Recipe.getRecipeId();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewFragment =  inflater.inflate(R.layout.fragment_recipe_ingredient, container, false);
        checkForInternet = new CheckForInternet(getActivity());

        setupIngredientRecycler(viewFragment);

        Log.v(TAG , "recipe ID retrieved from the recipe list activity is : "+recipeId);

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        Loader<Void> loader = loaderManager.getLoader(INGREDIENT_LOADER_ID);

        if(loader == null)
        {
            loaderManager.initLoader(INGREDIENT_LOADER_ID , null , this);
        }
        else
        {
            loaderManager.restartLoader(INGREDIENT_LOADER_ID , null , this);
        }


        return viewFragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(INGREDIENT_LOADER_ID , null , this);
    }

    public void setupIngredientRecycler(View view)
    {

        ingredientReycler = (RecyclerView) view.findViewById(R.id.recycle_ingredients);
        ingredientReycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ingredientReycler.setLayoutManager(layoutManager);

        progressIngredient = (ProgressBar) view.findViewById(R.id.progress_ingredients);

        internetConnectionWarning = (TextView) view.findViewById(R.id.no_connection);
        refreshButton = (Button) view.findViewById(R.id.refresh_button);


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(INGREDIENT_LOADER_ID , null , RecipeIngridientFragment.this);

            }
        });

    }

    public void setupAdapter(ArrayList<IngredientData> ingredientListContent)
    {
        ingredientAdapter = new IngredientAdapter(getActivity() , ingredientListContent);

        ingredientReycler.setAdapter(ingredientAdapter);

        ingredientAdapter.notifyDataSetChanged();
    }

    public void whileLoading()
    {
        progressIngredient.setVisibility(View.VISIBLE);
        ingredientReycler.setVisibility(View.GONE);
        internetConnectionWarning.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);
    }

    public void afterLoading()
    {
        progressIngredient.setVisibility(View.GONE);
        ingredientReycler.setVisibility(View.VISIBLE);
        internetConnectionWarning.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);

    }

    public void whenNoInternet()
    {
        progressIngredient.setVisibility(View.GONE);
        ingredientReycler.setVisibility(View.GONE);
        internetConnectionWarning.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);

    }
    public static ArrayList<IngredientData> getIngredientList()
    {
        return ingredientList;
    }
    @Override
    public Loader<ArrayList<IngredientData>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<IngredientData>>(getActivity()) {


            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                whileLoading();

                if(ingredientList.size() == 0)
                {
                    forceLoad();
                }
                else
                {
                    deliverResult(ingredientList);
                }
            }


            @Override
            public ArrayList<IngredientData> loadInBackground() {

                String ingredientName;
                double quantity;
                String measure;
                IngredientData ingredientData;
                if(checkForInternet.hasInternetAccess())
                {
                    String jsonResult = NetworkRequest.getJson();

                    try{
                        JSONArray recipeArray = new JSONArray(jsonResult);
                        for(int i = 0; i < recipeArray.length() ; i++)
                        {
                            JSONObject recipeObject = recipeArray.getJSONObject(i);
                            int id = recipeObject.getInt("id");
                            if(id == recipeId)
                            {
                                Log.v(TAG , "value of i is "+i);

                                JSONArray ingredientArray = recipeObject.getJSONArray("ingredients");

                                for(int j = 0 ; j < ingredientArray.length() ; j++)
                                {
                                    JSONObject ingredientObject = ingredientArray.getJSONObject(j);

                                    ingredientName = ingredientObject.getString("ingredient");
                                    quantity = ingredientObject.getDouble("quantity");
                                    measure = ingredientObject.getString("measure");

                                    ingredientsOnlyList.add(quantity +" "+measure+" of "+ingredientName+"/");

                                    Log.v(TAG , "data retrieved is ingredientName :"+ingredientName);
                                    ingredientData =  new IngredientData(ingredientName , measure , quantity);

                                    ingredientList.add(ingredientData);

                                }

                                return ingredientList;
                            }


                        }
                    }
                    catch(JSONException jsonException)
                    {
                        Log.v(TAG , "JSON exception thrown while trying to retrieve json result , Exception : "+jsonException.toString());
                    }
                }

                return null;

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<IngredientData>> loader, ArrayList<IngredientData> data) {
        if(data != null)
        {
            setupAdapter(data);
            afterLoading();

            Log.v(TAG , "data has been received to the loader on finish loading");
        }
        else
        {
            whenNoInternet();

            Log.v(TAG , "NO INTERNET CONNECTION AVAILABLE");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0 ; i < ingredientsOnlyList.size() ; i++)
        {
            stringBuilder.append(ingredientsOnlyList.get(i));
        }



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());



            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("key" , stringBuilder.toString());

            editor.commit();



    }

    @Override
    public void onLoaderReset(Loader<ArrayList<IngredientData>> loader) {
        ingredientList = new ArrayList<>();
        setupAdapter(ingredientList);
        afterLoading();
    }


}
