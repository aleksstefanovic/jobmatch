package com.triosstudent.aleks.jobmatch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by triosStudent on 6/30/2017.
 */

class CallWebService extends AsyncTask<String, Void, String> {
    private Activity mainActivity;
    private int errorId;
    private Activity successActivity;
    private OnTaskCompleted listener;

    public CallWebService (Activity activity, OnTaskComplete listener) {
        mainActivity = activity;
    }

    protected String doInBackground(String[] params) {
        String response = JobMatchService.executePost(params[0], params[1]);
        return response;
    }

    protected void onPostExecute(String message) {
        System.out.println(message);
        try {
            JSONObject response = new JSONObject(message);
            String code = (String) response.get("code");
            if (!code.equals("200")) {
                TextInputLayout buttonError = (TextInputLayout) mainActivity.findViewById(R.id.buttonError);
                buttonError.setError("Error creating user");
            }
            else {
                Intent intent = new Intent(mainActivity, createUser.class);
                mainActivity.startActivity(intent);
            }
        }
        catch (JSONException ex) {

        }
    }
}
