package com.parot.mtecgwa_jr.myrecipes.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.Fragments.StepsListFragment;
import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.RecipeSteps;

import java.util.ArrayList;

/**
 * Created by mtecgwa-jr on 6/15/17.
 */

public class RecipeMasterListAdapter extends RecyclerView.Adapter<RecipeMasterListAdapter.ListViewHolder> {

    private ArrayList<RecipeSteps> recipeListSteps = new ArrayList<>();
    private Context context;
    private StepsListFragment.OnStepClickedListener onStepClickedListener;

    public RecipeMasterListAdapter(Context context , ArrayList<RecipeSteps> recipeListSteps , StepsListFragment.OnStepClickedListener onStepClickedListener)
    {
        this.context = context;
        this.recipeListSteps = recipeListSteps;
        this.onStepClickedListener = onStepClickedListener;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView stepTitle , stepShortDescription;
        LinearLayout stepContainer;

        public ListViewHolder(View itemView) {
            super(itemView);

            stepTitle = (TextView) itemView.findViewById(R.id.step_title);
            stepShortDescription = (TextView) itemView.findViewById(R.id.step_short_description);
            stepContainer = (LinearLayout) itemView.findViewById(R.id.step_container);
        }
    }

    @Override
    public RecipeMasterListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_recipe_step , parent , false);

        ListViewHolder listViewHolder = new ListViewHolder(view);


        return listViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeMasterListAdapter.ListViewHolder holder,final int position) {
        RecipeSteps recipeSteps = recipeListSteps.get(position);

        holder.stepTitle.setText(recipeSteps.getStepTitle());
        holder.stepShortDescription.setText(recipeSteps.getStepDescription());

        if(position % 2 == 0)
        {
            holder.stepContainer.setBackgroundResource(R.color.lightgrey);
        }
        else
        {
            holder.stepContainer.setBackgroundResource(R.color.white);

        }

        holder.stepContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onStepClickedListener != null)
                {
                    onStepClickedListener.onStepClicked(position);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return recipeListSteps.size();
    }


}
