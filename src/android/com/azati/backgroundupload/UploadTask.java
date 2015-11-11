package com.azati.backgroundupload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Viktor on 10.11.2015.
 */
public class UploadTask extends AsyncTask<String, Integer, String> {

    private Context mContext;

    private static UploadTask sInstance;

    public UploadTask(Context context) {
        mContext = context;
    }

    public static UploadTask getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UploadTask(context);
        }
        return sInstance;
    }

    private void recreateInstance(){
        sInstance = new UploadTask(mContext);
    }


    @Override
    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        createNotification("start upload");
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        createNotification("upload complete: " + string);
        recreateInstance();
    }

    @Override
    protected String doInBackground(String... parameter) {
        int myProgress = 0;
        publishProgress(myProgress);
        String result = "result";
        if(null != parameter && parameter.length >0 && null != parameter[0] && !parameter[0].isEmpty()) {

            //setup params
            Map<String, String> params = new HashMap<String, String>(2);
            params.put("json_object", "here will be json data");
            //String result = multipartRequest(URL_UPLOAD_VIDEO, params, pathToVideoFile, "video", "video/mp4");
            try {
                result = RequestsHelper.multipartRequest("http://192.168.92.230:47503/api/upload/test", params, parameter[0], "file");
            } catch (Exception e) {
                result = e.getMessage();
                e.printStackTrace();
            }
        }


        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        createNotification("Cancelling upload");
    }

    private void createNotification(String text) {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        CharSequence notiText = text;
        long meow = System.currentTimeMillis();

        Notification notification = new Notification();
        notification.tickerText = notiText;
        notification.when = meow;
        notification.icon = mContext.getResources().getIdentifier("R.drawable.ic_notification", "drawable", mContext.getPackageName();

        CharSequence contentTitle = "UPLOAD SERVICE";
        CharSequence contentText = text;
        Intent notificationIntent = new Intent(mContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

        notification.setLatestEventInfo(mContext, contentTitle, contentText, contentIntent);

        int SERVER_DATA_RECEIVED = 1;
        notificationManager.notify(SERVER_DATA_RECEIVED, notification);
    }
}