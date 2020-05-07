package com.example;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//LAST
public class CheckInternetConnection {
    private Context context;
    public  CheckInternetConnection(Context context){

        this.context=context;
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
