package com.parot.mtecgwa_jr.myrecipes.RecipeData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mtecgwa-jr on 6/2/17.
 */

public class RecipeSteps implements Parcelable {

    private String stepTitle;
    private String stepDescription;
    private String videoUrl;

    public RecipeSteps()
    {
        super();
    }

    protected RecipeSteps(Parcel parcel) {
        stepTitle = parcel.readString();
        stepDescription = parcel.readString();
        videoUrl = parcel.readString();
    }

    public static final Creator<RecipeSteps> CREATOR = new Creator<RecipeSteps>() {
        @Override
        public RecipeSteps createFromParcel(Parcel parcel) {
            return new RecipeSteps(parcel);
        }

        @Override
        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stepTitle);
        dest.writeString(stepDescription);
        dest.writeString(videoUrl);
    }

    public void setStepTitle(String stepTitle)
    {
        this.stepTitle = stepTitle;
    }
    public void setStepDescription(String stepDescription)
    {
        this.stepDescription = stepDescription;
    }
    public void setVideoUrl(String videoUrl)
    {
        this.videoUrl = videoUrl;
    }

    public String getStepTitle() { return stepTitle;}

    public String getStepDescription() { return stepDescription; }

    public String getVideoUrl() { return videoUrl; }
}
