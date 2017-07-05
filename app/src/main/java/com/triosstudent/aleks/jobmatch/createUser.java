package com.triosstudent.aleks.jobmatch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;

public class createUser extends Activity  {

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void acceptNewUser (View view) {
        TextInputLayout emailError = (TextInputLayout) findViewById(R.id.emailError);
        emailError.setError("");
        TextInputLayout password1Error = (TextInputLayout) findViewById(R.id.password1Error);
        password1Error.setError("");
        TextInputLayout password2Error = (TextInputLayout) findViewById(R.id.password2Error);
        password2Error.setError("");
        TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
        buttonError.setError("");

        Spinner userType = (Spinner) findViewById(R.id.userType);
        EditText enterEmail = (EditText) findViewById(R.id.enterEmail);
        EditText enterPassword1 = (EditText) findViewById(R.id.enterPassword1);
        EditText enterPassword2 = (EditText) findViewById(R.id.enterPassword2);

        String accountType = String.valueOf(userType.getSelectedItem());
        String enterEmailStr = enterEmail.getText().toString();
        if (enterEmailStr.isEmpty()) {
            emailError.setError("You need to enter an email");
            return;
        }
        String enterPassword1Str = enterPassword1.getText().toString();
        if (enterPassword1Str.isEmpty()) {
            password1Error.setError("You need to enter a password");
            return;
        }
        String enterPassword2Str = enterPassword2.getText().toString();
        if (!enterPassword1Str.equals(enterPassword2Str)) {
            password2Error.setError("Passwords must match");
            return;
        }

        String apiUrl = getString(R.string.apiurl) + "/users";
        String payload = JobMatchService.buildCreateUserCall(enterEmailStr, enterPassword1Str, accountType);

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
                    TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
                    buttonError.setError("Error creating user");
                }
                else {
                    Intent intent = new Intent (createUser.this, mainPage.class);
                    intent.putExtra("user_id", (String) response.get("user_id"));
                    intent.putExtra("email", (String) response.get("email"));
                    intent.putExtra("type", (String) response.get("type"));
                    intent.putExtra("newUser", true);
                    startActivity(intent);
                }
            }
            catch (JSONException ex) {
                TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
                buttonError.setError("Error creating user");
            }
        }
    }



}
