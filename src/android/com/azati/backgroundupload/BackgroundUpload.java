package com.azati.backgroundupload;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;

public class BackgroundUpload extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject params = args.getJSONObject(0);
        if (action.equals("uploadFiles")) {

            JSONArray jsonArr = params.getJSONArray("filesArray");
            String[] arr = new String[jsonArr.length()];
            for(int i = 0; i < jsonArr.length(); i++) {
                arr[i] = jsonArr.getString(i);
            }

            String array = arr[0];/*params.getString("filesArray");*/
            String data = params.getString("data");
            this.echo(array, data, callbackContext);
            return true;
        }
        return false;
    }

    private void echo(String array, String data, CallbackContext callbackContext) {
        if (array != null && array.length() > 0) { 
            //callbackContext.success("array: " + array + "\n" + data);

            Context context = this.cordova.getActivity().getApplicationContext();

            Toast.makeText(context, "Image selected: " + array, Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, UploadService.class);
            i.setAction(UploadService.ACTION_START_DOWNLOAD);
            i.putExtra("IMAGE_URL", array);
            context.startService(i);

        } else {
            callbackContext.error("Expected one non-empty array argument.");
        }
    }
}
