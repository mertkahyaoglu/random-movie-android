package com.mertkahyaoglu.randommovie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by xray on 21/04/15.
 */
public class Utils {

    private static Random rand = new Random();

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }

    public static JSONArray loadJSONFromAsset(Context ctx) {
        JSONArray arr;
        try {
            InputStream is = ctx.getAssets().open("ids.json");

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            arr = new JSONArray(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return arr;
    }


    public static String getRandomID(JSONArray ids) {
        int num = rand.nextInt(ids.length());
        String id = "";
        try {
            id = ids.getString(num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

}
