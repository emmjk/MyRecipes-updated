package com.parot.mtecgwa_jr.myrecipes.NetworkUtils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by mtecgwa-jr on 5/29/17.
 */

public class NetworkRequest {

    private static final String URL_STRING = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String TAG = NetworkRequest.class.getName();
    public static String getJson()
    {
        Uri uri = Uri.parse(URL_STRING).buildUpon().build();
        URL url = null;
        try{
            url = new URL(uri.toString());
        }
        catch(MalformedURLException exception)
        {
            Log.v(TAG , "Malformed exception :"+exception.toString());
        }
        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);

            scanner.useDelimiter("\\A");

            if(scanner.hasNext())
            {
                return scanner.next();
            }
            else
            {
                return null;
            }

        }
        catch(IOException ioException)
        {
            Log.v(TAG , " IO exception thrown during network operation");
            connection.disconnect();
        }

        return  null;

    }
}
