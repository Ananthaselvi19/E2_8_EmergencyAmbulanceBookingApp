package com.example.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;
import android.widget.Toast;

public class CheckInternet extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String status = NetworkUtil.getNetworkState(context);
        Dialog dialog = new Dialog(context, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.custom_dialog);
        Button retry = dialog.findViewById(R.id.retry);
        retry.setOnClickListener(view -> {
            ((Activity) context).finish();
            Intent intent1 = new Intent(context, HomePage.class);
            context.startActivity(intent1);
        });

        if(status.isEmpty() || status.equals("No Internet Connection")){
            dialog.show();
        }
//        throw new UnsupportedOperationException("Not yet implemented");
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }
}