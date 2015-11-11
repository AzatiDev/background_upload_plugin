package com.azati.backgroundupload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Viktor on 09.11.2015.
 */
public class UploadService extends Service {

    public static final String ACTION_START_DOWNLOAD = "ACTION_START_DOWNLOAD";
    public static final String ACTION_STOP_DOWNLOAD = "ACTION_STOP_DOWNLOAD";


    public UploadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(ACTION_START_DOWNLOAD)) {
                    if (isOnline()) {
                        Log.d(this.getClass().getName(), "starting download, internet available"/* + "\nimage url: " + intent.getStringExtra("IMAGE_URL")*/);
                        //createNotification("starting download, internet available");
                        String filePath = intent.getStringExtra("IMAGE_URL");
                        UploadTask.getInstance(getApplicationContext()).execute(filePath);
                    } else {
                        if(UploadTask.getInstance(getApplicationContext()).getStatus() ==  AsyncTask.Status.RUNNING) {
                            Log.d(this.getClass().getName(), "starting download, internet is not available"/* + "\nimage url: " + intent.getStringExtra("IMAGE_URL")*/);
                            UploadTask.getInstance(getApplicationContext()).cancel(true);
                        } else {
                            Log.d(this.getClass().getName(), "async task is not executing"/* + "\nimage url: " + intent.getStringExtra("IMAGE_URL")*/);
                        }
                    }
                } else if (action.equals(ACTION_STOP_DOWNLOAD)) {
                    if(UploadTask.getInstance(getApplicationContext()).getStatus() ==  AsyncTask.Status.RUNNING) {
                        Log.d(this.getClass().getName(), "stopping download, internet is not available"/* + "\nimage url: " + intent.getStringExtra("IMAGE_URL")*/);
                        UploadTask.getInstance(getApplicationContext()).cancel(true);
                    } else {
                        Log.d(this.getClass().getName(), "async task is not executing"/* + "\nimage url: " + intent.getStringExtra("IMAGE_URL")*/);
                    }
                }
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
