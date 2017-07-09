package com.triosstudent.aleks.jobmatch;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import static android.accounts.AccountManager.get;

public class Response extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.response);

        Intent intent = getIntent();
        final String q1 = intent.getStringExtra("q1");
        final String q2 = intent.getStringExtra("q2");
        final String q3 = intent.getStringExtra("q3");
        final String q4 = intent.getStringExtra("q4");
        final String qTitle = intent.getStringExtra("qTitle");

        TextView qTitleTxt = (TextView) findViewById(R.id.title);
        TextView q1Txt = (TextView) findViewById(R.id.q1);
        TextView q2Txt = (TextView) findViewById(R.id.q2);
        TextView q3Txt = (TextView) findViewById(R.id.q3);
        TextView q4Txt = (TextView) findViewById(R.id.q4);

        qTitleTxt.setText(qTitle);
        q1Txt.setText(q1);
        q2Txt.setText(q2);
        q3Txt.setText(q3);
        q4Txt.setText(q4);
    }

    public void submitResponse (View view) {
        TextInputLayout question1Error = (TextInputLayout) findViewById(R.id.q1Error);
        question1Error.setError("");
        TextInputLayout question2Error = (TextInputLayout) findViewById(R.id.q2Error);
        question2Error.setError("");
        TextInputLayout question3Error = (TextInputLayout) findViewById(R.id.q3Error);
        question3Error.setError("");
        TextInputLayout question4Error = (TextInputLayout) findViewById(R.id.q4Error);
        question4Error.setError("");
        TextInputLayout responseError = (TextInputLayout) findViewById(R.id.buttonError);
        responseError.setError("");

        Intent intent = getIntent();
        final String qId = Integer.toString(intent.getIntExtra("qId", 0));
        System.out.println("QUESTIONNAIRE ID: " + qId);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("currentUser", null);

        AccountManager accountManager = get(this);
        Account[] accounts = accountManager.getAccountsByType("com.triosstudent.aleks.jobmatch.ACCOUNT");
        Account currentAccount = null;
        for (int i=0; i < accounts.length; i++) {
            if (accounts[i].name.equals(currentUser)) {
                currentAccount = accounts[i];
            }
        }
        if (currentAccount == null) {
            responseError.setError("Error could not submit response");
            return;
        }
        final String user_id = accountManager.getUserData(currentAccount, "user_id");

        EditText q1Input = (EditText) findViewById(R.id.q1Input);
        EditText q2Input = (EditText) findViewById(R.id.q2Input);
        EditText q3Input = (EditText) findViewById(R.id.q3Input);
        EditText q4Input = (EditText) findViewById(R.id.q4Input);

        String q1InputStr = q1Input.getText().toString();
        if (q1InputStr.isEmpty()) {
            question1Error.setError("You must enter a response for question 1");
            return;
        }
        String q2InputStr = q2Input.getText().toString();
        if (q2InputStr.isEmpty()) {
            question2Error.setError("You must enter a response for question 2");
            return;
        }
        String q3InputStr = q3Input.getText().toString();
        if (q3InputStr.isEmpty()) {
            question3Error.setError("You must enter a response for question 3");
            return;
        }
        String q4InputStr = q4Input.getText().toString();
        if (q4InputStr.isEmpty()) {
            question4Error.setError("You must enter a response for question 4");
            return;
        }

        String apiUrl = getString(R.string.apiurl) + "/responses";
        String payload = JobMatchService.buildCreateResponseCall(user_id, q1InputStr, q2InputStr, q3InputStr, q4InputStr, qId);

        CallWebService job = new CallWebService();
        job.execute(apiUrl, payload);
    }

    class CallWebService extends AsyncTask<String, Void, String> {

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
                    TextInputLayout responseError = (TextInputLayout) findViewById(R.id.buttonError);
                    responseError.setError("Error could not submit response");
                }
                else {
                    Intent intent = new Intent (Response.this, mainPage.class);
                    intent.putExtra("newUser", false);
                    startActivity(intent);
                }
            }
            catch (JSONException ex) {
                TextInputLayout responseError = (TextInputLayout) findViewById(R.id.buttonError);
                responseError.setError("Error could not submit response");
            }
        }
    }
}
