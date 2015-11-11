package com.azati.backgroundupload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Viktor on 09.11.2015.
 */
public class AppReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent i = new Intent(context, UploadService.class);
            i.setAction(UploadService.ACTION_START_DOWNLOAD);
            context.startService(i);
        } else if("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(isOnline(context)) {
                Intent i = new Intent(context, UploadService.class);
                i.setAction(UploadService.ACTION_START_DOWNLOAD);
                context.startService(i);
            } else {
                Intent i = new Intent(context, UploadService.class);
                i.setAction(UploadService.ACTION_STOP_DOWNLOAD);
                context.startService(i);
            }
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}