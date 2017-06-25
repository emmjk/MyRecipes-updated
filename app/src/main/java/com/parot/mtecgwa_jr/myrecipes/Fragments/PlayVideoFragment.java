package com.parot.mtecgwa_jr.myrecipes.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.parot.mtecgwa_jr.myrecipes.PlayVideoActivity;
import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.RecipeSteps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayVideoFragment extends Fragment {


    public PlayVideoFragment() {
        // Required empty public constructor
    }

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ArrayList<RecipeSteps> stepsList;
    private int indexPosition;
    private final String STEP_OBJECT = "step object";
    private  final String INDEX = "index_position";
    private final String TAG = PlayVideoFragment.class.getName();
    private ImageView imageWhenNoVideo;


    private TextView stepHeader , stepProcedure;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_play_video, container, false);

        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.video);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            RecipeSteps recipeSteps = bundle.getParcelable(STEP_OBJECT);
            indexPosition = bundle.getInt(INDEX);

                playerSetup(recipeSteps.getVideoUrl() , rootView);


        }
        else
        {
            stepsList = PlayVideoActivity.getStepsList();
            indexPosition = PlayVideoActivity.getItemIndex();

            RecipeSteps recipeStep = getRecipeStep(stepsList , indexPosition);


                playerSetup(recipeStep.getVideoUrl() , rootView);


        }


        return rootView;
    }


    public void playerSetup(String videoUrl , View rootView)
    {
        Log.v(TAG , "the string url of the video is : "+videoUrl);

        if(!videoUrl.equals(""))
        {
            Uri uri = Uri.parse(videoUrl);

            // 1. Create a default TrackSelector

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();

            DefaultTrackSelector.Parameters parameters = trackSelector.getParameters();

            parameters.withoutVideoSizeConstraints();

            trackSelector.setParameters(parameters);


            // 2 . Create a load control for the ExoPlayer . Done by Mtecgwa ........

            LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(false , 3500) , 12000 , 15000 , 3500 , 2000);


            // 3. Create the player
            player =
                    ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getActivity())
                            ,trackSelector
                            , loadControl);

            simpleExoPlayerView.setPlayer(player);

            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "VideoPlayer"), defaultBandwidthMeter);

            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(uri,
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            player.prepare(videoSource);

            player.setPlayWhenReady(true);
        }
        else
        {
            imageWhenNoVideo = (ImageView) rootView.findViewById(R.id.image_when_no_video);
            imageWhenNoVideo.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.GONE);
            Picasso.with(getActivity())
                    .load(R.drawable.dinner)
                    .into(imageWhenNoVideo);

            Toast.makeText(getActivity() , "No Video." , Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onPause() {
        super.onPause();

        if(player == null)
        {
            Log.v(TAG , "pLAYER IS NULL ");
        }
        else
        {
            player.stop();
            player.release();
            player = null;
        }
    }

    public RecipeSteps getRecipeStep(ArrayList<RecipeSteps> stepsList , int indexPosition)
    {
        RecipeSteps recipeSteps = stepsList.get(indexPosition);

        return recipeSteps;
    }

}
