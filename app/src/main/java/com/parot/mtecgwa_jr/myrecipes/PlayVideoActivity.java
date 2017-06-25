package com.parot.mtecgwa_jr.myrecipes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.parot.mtecgwa_jr.myrecipes.Fragments.PlayVideoFragment;
import com.parot.mtecgwa_jr.myrecipes.Fragments.RecipeInstructionFragment;
import com.parot.mtecgwa_jr.myrecipes.Fragments.StepsListFragment;
import com.parot.mtecgwa_jr.myrecipes.NetworkUtils.CheckForInternet;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.RecipeSteps;

import java.util.ArrayList;

public class PlayVideoActivity extends AppCompatActivity implements StepsListFragment.OnStepClickedListener {

    private  final String STEPS_ARRAY_LIST = "steps array list";
    private  final String INDEX = "index_position";
    private FragmentManager fragmentManager;
    private PlayVideoFragment playVideoFragment;
    private StepsListFragment stepsListFragment;
    private boolean isScreenWide;
    private static ArrayList<RecipeSteps> recipeStepsList;
    private static int id;

    private final String STEP_OBJECT = "step object";

    private final String TAG = PlayVideoActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);


        getIntentContent();

        isScreenWide = getResources().getBoolean(R.bool.is_device_wide);

        playVideoFragment = new PlayVideoFragment();

        stepsListFragment = new StepsListFragment();

        fragmentManager= getSupportFragmentManager();

        if(isScreenWide)
        {
            fragmentManager.beginTransaction()
                    .add(R.id.master_list_container , stepsListFragment)
                    .commit();

            fragmentManager.beginTransaction()
                    .add(R.id.detail_video_container , playVideoFragment)
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .add(R.id.detail_video_container , playVideoFragment)
                    .commit();

            fragmentManager.beginTransaction()
                    .add(R.id.master_list_container , stepsListFragment)
                    .commit();


        }


//        Log.v(TAG , "the item clicked is at position "+id);

    }

    public void getIntentContent()
    {
        Intent playVideo = getIntent();

        id = playVideo.getIntExtra(INDEX , 0);

        recipeStepsList = playVideo.getParcelableArrayListExtra(STEPS_ARRAY_LIST);

    }

    public static int getItemIndex()
    {
        return id;
    }
    public static ArrayList<RecipeSteps> getStepsList()
    {
        return recipeStepsList;
    }

    @Override
    public void onStepClicked(int index) {
        RecipeSteps recipeSteps = recipeStepsList.get(index);
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP_OBJECT ,  recipeSteps);
        bundle.putInt(INDEX , index);

        PlayVideoFragment currentVideoFragment = new PlayVideoFragment();
        currentVideoFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.detail_video_container , currentVideoFragment)
                .commit();

    }


}
