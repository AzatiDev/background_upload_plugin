package com.azati.backgroundupload;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            callbackContext.success("array: " + array + "\n" + data);
        } else {
            callbackContext.error("Expected one non-empty array argument.");
        }
    }
}
