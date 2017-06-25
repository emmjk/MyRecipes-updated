package com.parot.mtecgwa_jr.myrecipes.NetworkUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

/**
 * Created by mtecgwa-jr on 6/22/17.
 */

public class CheckForInternet {
    private Context context;
    public CheckForInternet(Context context)
    {
        this.context = context;
    }

    public  boolean hasInternetAccess( ) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection url = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                url.setRequestProperty("User-Agent", "Android");
                url.setRequestProperty("Connection", "close");
                url.setConnectTimeout(1500);
                url.connect();
                return (url.getResponseCode() == 204 &&
                        url.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null)
        {
            Log.v(TAG , "Network is switched ON");
        }
        else
        {
            Log.v(TAG , "both data and wifi are off");
        }

        return activeNetworkInfo != null;
    }



}
