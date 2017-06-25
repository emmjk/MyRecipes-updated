package com.parot.mtecgwa_jr.myrecipes.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
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
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.Adapter.RecipeStepsAdapter;
import com.parot.mtecgwa_jr.myrecipes.NetworkUtils.CheckForInternet;
import com.parot.mtecgwa_jr.myrecipes.NetworkUtils.NetworkRequest;
import com.parot.mtecgwa_jr.myrecipes.PlayVideoActivity;
import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.Recipe;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.RecipeSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeInstructionFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<RecipeSteps>> {


    public RecipeInstructionFragment() {
        // Required empty public constructor
    }

    private String TAG = RecipeInstructionFragment.class.getName();

    private RecyclerView stepsRecycler;
    private ProgressBar progressBar;
    private RecipeStepsAdapter recipeStepsAdapter;
    private TextView noInternetWarning;
    private Button refreshButton;
    private ArrayList<RecipeSteps> recipeStepsList = new ArrayList<>();
    private CheckForInternet checkForInternet;

    private final int RECIPE_STEPS_LOADER_ID = 16;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFragment =  inflater.inflate(R.layout.fragment_recipe_instruction, container, false);
        checkForInternet = new CheckForInternet(getActivity());
        initializeContents(viewFragment);

            LoaderManager loaderManager = getLoaderManager();

            Loader<Void> loader = loaderManager.getLoader(RECIPE_STEPS_LOADER_ID);

            if(loader == null)
            {
                loaderManager.initLoader(RECIPE_STEPS_LOADER_ID , null , this);
            }
            else
            {
                loaderManager.restartLoader(RECIPE_STEPS_LOADER_ID , null , this);
            }

        return viewFragment;
    }



    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(RECIPE_STEPS_LOADER_ID , null , this);
    }

    public void initializeContents(View myStepView)
    {
        progressBar = (ProgressBar) myStepView.findViewById(R.id.steps_progress);
        stepsRecycler = (RecyclerView) myStepView.findViewById(R.id.steps_recycler);
        stepsRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        stepsRecycler.setLayoutManager(layoutManager);

        noInternetWarning = (TextView) myStepView.findViewById(R.id.no_connection);
        refreshButton = (Button) myStepView.findViewById(R.id.refresh_button);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoaderManager().restartLoader(RECIPE_STEPS_LOADER_ID , null , RecipeInstructionFragment.this);
            }
        });
    }

    public void setupRecyclerViewAdapter(ArrayList<RecipeSteps> recipeStepsListContent)
    {
        recipeStepsAdapter = new RecipeStepsAdapter(getActivity() , recipeStepsListContent);

        stepsRecycler.setAdapter(recipeStepsAdapter);

        recipeStepsAdapter.notifyDataSetChanged();
    }

    public void whileLoading()
    {
        stepsRecycler.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noInternetWarning.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);
    }

    public void afterLoading()
    {
        stepsRecycler.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        noInternetWarning.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);


    }
    public void whenNoInternet()
    {
        stepsRecycler.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        noInternetWarning.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);

    }

    @Override
    public Loader<ArrayList<RecipeSteps>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<RecipeSteps>>(getActivity()) {
            int recipeId = Recipe.getRecipeId();

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                whileLoading();
                if(recipeStepsList.size() == 0)
                {
                    forceLoad();
                }
                else
                {
                    deliverResult(recipeStepsList);
                }
            }



            @Override
            public ArrayList<RecipeSteps> loadInBackground() {

                String stepTitle;
                String stepDescription;
                String videoUrl;

                RecipeSteps recipeSteps;
                if(checkForInternet.hasInternetAccess())
                {
                    String jsonResult = NetworkRequest.getJson();

                    try{
                        JSONArray recipeArray = new JSONArray(jsonResult);
                        for(int i = 0 ; i <= recipeArray.length() ; i++)
                        {
                            JSONObject recipeStepObject = recipeArray.getJSONObject(i);
                            int id = recipeStepObject.getInt("id");

                            if(id == recipeId)
                            {
                                JSONArray instructionArray = recipeStepObject.getJSONArray("steps");
                                for(int j = 0 ; j < instructionArray.length() ; j++)
                                {
                                    JSONObject singleStep = instructionArray.getJSONObject(j);

                                    stepTitle = singleStep.getString("shortDescription");

                                    stepDescription = singleStep.getString("description");

                                    videoUrl = singleStep.getString("videoURL") + singleStep.getString("thumbnailURL");


                                    recipeSteps = new RecipeSteps();

                                    recipeSteps.setStepTitle(stepTitle);
                                    recipeSteps.setStepDescription(stepDescription);
                                    recipeSteps.setVideoUrl(videoUrl);

                                    recipeStepsList.add(recipeSteps);

                                }

                                return recipeStepsList;
                            }
                        }
                    }
                    catch(JSONException jsonException)
                    {
                        Log.v(TAG , "Exception thrown while trying to get a json , EXCEPTION :"+jsonException);
                    }
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<RecipeSteps>> loader, ArrayList<RecipeSteps> data) {
        if(data != null)
        {
            setupRecyclerViewAdapter(data);
            afterLoading();
        }
        else
        {
            whenNoInternet();
        }


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RecipeSteps>> loader) {

        recipeStepsList = new ArrayList<>();

        setupRecyclerViewAdapter(recipeStepsList);

        afterLoading();
    }
}
