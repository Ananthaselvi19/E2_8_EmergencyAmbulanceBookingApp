package com.example.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static String getNetworkState(Context context){
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                status = "Wifi Connected";
                return status;
            }
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                status = "Mobile data Connected";
                return status;
            }
        }
        else {
            status = "No Internet Connection";
            return status;
        }

        return status;
    }
}
