






package com.parot.mtecgwa_jr.myrecipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parot.mtecgwa_jr.myrecipes.Adapter.RecipeAdapter;
import com.parot.mtecgwa_jr.myrecipes.NetworkUtils.CheckForInternet;
import com.parot.mtecgwa_jr.myrecipes.NetworkUtils.NetworkRequest;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.MyRecipe;
import com.parot.mtecgwa_jr.myrecipes.RecipeWidget.RecipeWidgetAconfActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MyRecipe>>  {

    private static final String TAG = MainActivity.class.getName();

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private static ArrayList<MyRecipe> recipeList;
    private ProgressBar progressBar;
    private MyRecipe myRecipe;
    private final int RECIPE_DATA_LOADER_ID = 24;
    private TextView noInternet;
    private CheckForInternet checkForInternet;

    private ArrayList<String> recipeNameList = new ArrayList<>();
    private int numberOfColumns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeRecyclerContent();
        checkForInternet = new CheckForInternet(getApplicationContext());
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Void> recipeLoader = loaderManager.getLoader(RECIPE_DATA_LOADER_ID);

        if(recipeLoader == null)
        {
            loaderManager.initLoader(RECIPE_DATA_LOADER_ID , null , this);
        }
        else {
            loaderManager.restartLoader(RECIPE_DATA_LOADER_ID , null , this);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    public static ArrayList<MyRecipe> getRecipeList()
    {
        return recipeList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == R.id.action_refresh)
        {
            initializeRecyclerContent();
            getSupportLoaderManager().restartLoader(RECIPE_DATA_LOADER_ID ,null , this);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().initLoader(RECIPE_DATA_LOADER_ID , null , this);
    }

    public void initializeRecyclerContent()
    {
        recipeList  = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        noInternet = (TextView) findViewById(R.id.no_connection);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        numberOfColumns = getResources().getInteger(R.integer.number_of_columns);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext() , numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter(getApplicationContext() , recipeList);
        recyclerView.setAdapter(recipeAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getSupportLoaderManager().restartLoader(RECIPE_DATA_LOADER_ID ,null , this);
    }

    public void whileLoading()
    {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noInternet.setVisibility(View.GONE);
    }

    public void afterLoading()
    {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        noInternet.setVisibility(View.GONE);
    }

    public void whenNoData()
    {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        noInternet.setVisibility(View.VISIBLE);

    }

    public void setupRecyclerAdapter(ArrayList<MyRecipe> recipeList)
    {

        recipeAdapter = new RecipeAdapter(getApplicationContext() , recipeList);
        recyclerView.setAdapter(recipeAdapter);
        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<ArrayList<MyRecipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<MyRecipe>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                whileLoading();

                Log.v(TAG , "recipeLis has a size of : "+recipeList.size());

                if(recipeList.size() == 0)
                {
                    forceLoad();
                }
                else
                {
                    deliverResult(recipeList);
                }

            }

            @Override
            public ArrayList<MyRecipe> loadInBackground() {

                String jsonResult = NetworkRequest.getJson();
                String recipeName;
                int recipeId ;
                int recipeImageResource = 0;

                if(checkForInternet.hasInternetAccess())
                {
                    try{
                        JSONArray recipeArrays = new JSONArray(jsonResult);
                        for(int i = 0 ; i <recipeArrays.length() ; i++)
                        {
                            JSONObject recipeObject = recipeArrays.getJSONObject(i);
                            recipeName = recipeObject.getString("name");
                            recipeId = recipeObject.getInt("id");

                            switch(recipeId)
                            {
                                case 1:
                                    recipeImageResource = R.drawable.nutalie;
                                    break;
                                case 2:
                                    recipeImageResource = R.drawable.brownies;
                                    break;
                                case 3:
                                    recipeImageResource = R.drawable.cakeyellow;
                                    break;
                                case 4:
                                    recipeImageResource = R.drawable.cheese;
                                    break;
                            }

                            recipeNameList.add(recipeName);

                            myRecipe = new MyRecipe(recipeName , recipeImageResource , recipeId);

                            Log.v(TAG , "recipe id is :"+recipeId);

                            recipeList.add(myRecipe);
                        }

                        return recipeList;
                    }
                    catch(JSONException jsonException)
                    {
                        Log.v(TAG , "json exception thrown ; EXCEPTION :"+jsonException.toString());
                    }
                }
                else
                {
                    return null;

                }

                return null;

            }

            @Override
            public void deliverResult(ArrayList<MyRecipe> data) {
                super.deliverResult(data);

                    recipeList = data;

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MyRecipe>> loader, ArrayList<MyRecipe> data) {

        if(data != null)
        {
            setupRecyclerAdapter(data);
            afterLoading();
        }
        else
        {
            whenNoData();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i<recipeNameList.size() ; i++)
        {
            stringBuilder.append(recipeNameList.get(i));
            stringBuilder.append(",");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("recipe concatenated list" , stringBuilder.toString());

        editor.commit();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MyRecipe>> loader) {

        recipeList = new ArrayList<>();
        setupRecyclerAdapter(recipeList);
        afterLoading();
    }


}
