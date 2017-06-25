package com.parot.mtecgwa_jr.myrecipes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parot.mtecgwa_jr.myrecipes.PlayVideoActivity;
import com.parot.mtecgwa_jr.myrecipes.R;
import com.parot.mtecgwa_jr.myrecipes.RecipeData.RecipeSteps;

import java.util.ArrayList;

import static android.R.attr.value;

/**
 * Created by mtecgwa-jr on 6/2/17.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.StepsViewHolder> {

    private Context context;
    private ArrayList<RecipeSteps> stepsList;
    private  final String INDEX = "index_position";
    private  final String STEPS_ARRAY_LIST = "steps array list";

    public RecipeStepsAdapter(Context context , ArrayList<RecipeSteps> stepsList)
    {
        this.context = context;
        this.stepsList = stepsList;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        TextView stepTitle , stepShortDescription;
        LinearLayout stepContainer;

        public StepsViewHolder(View itemView) {
            super(itemView);

            stepTitle = (TextView) itemView.findViewById(R.id.step_title);
            stepShortDescription = (TextView) itemView.findViewById(R.id.step_short_description);
            stepContainer = (LinearLayout) itemView.findViewById(R.id.step_container);
        }
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View stepView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step , parent , false);

        StepsViewHolder stepsViewHolder = new StepsViewHolder(stepView);

        return stepsViewHolder;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder,final int position) {

        RecipeSteps recipeSteps = stepsList.get(position);

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
                Intent toPlayVideoActivity = new Intent(context , PlayVideoActivity.class);

                toPlayVideoActivity.putExtra(INDEX , position);
                toPlayVideoActivity.putParcelableArrayListExtra(STEPS_ARRAY_LIST , stepsList);

                context.startActivity(toPlayVideoActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }


}
