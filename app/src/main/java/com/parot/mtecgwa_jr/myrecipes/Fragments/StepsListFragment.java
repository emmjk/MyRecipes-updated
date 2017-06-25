package com.parot.mtecgwa_jr.myrecipes.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.Adapter.RecipeMasterListAdapter;
import com.parot.mtecgwa_jr.myrecipes.PlayVideoActivity;
import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.RecipeSteps;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsListFragment extends Fragment {


    public StepsListFragment() {
        // Required empty public constructor
    }

    private OnStepClickedListener onStepClickListener;

    private RecipeMasterListAdapter recipeMasterListAdapter;
    private RecyclerView masterListRecyclerView;
    private ArrayList<RecipeSteps> recipeStepList = new ArrayList<>();

    private TextView stepDescription;


    public interface OnStepClickedListener {
        public void onStepClicked(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_steps_list, container, false);

        initializeListFromRecycler(rootView);

        return rootView;
    }



    public void initializeListFromRecycler(View rootView )
    {
        masterListRecyclerView = (RecyclerView) rootView.findViewById(R.id.master_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        masterListRecyclerView.setLayoutManager(layoutManager);
        masterListRecyclerView.setHasFixedSize(true);

        recipeStepList = PlayVideoActivity.getStepsList();
        recipeMasterListAdapter = new RecipeMasterListAdapter(getActivity() , recipeStepList , onStepClickListener);
        recipeMasterListAdapter.notifyDataSetChanged();

        masterListRecyclerView.setAdapter(recipeMasterListAdapter);

        int position = PlayVideoActivity.getItemIndex();

        masterListRecyclerView.scrollToPosition(position);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickedListener) {
            onStepClickListener = (OnStepClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        onStepClickListener = null;
    }

}
