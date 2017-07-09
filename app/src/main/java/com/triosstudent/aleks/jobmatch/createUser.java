package com.triosstudent.aleks.jobmatch;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static android.accounts.AccountManager.get;
import static java.security.spec.MGF1ParameterSpec.SHA1;

public class createUser extends Activity  {

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void acceptNewUser (View view) {
        TextInputLayout usernameError = (TextInputLayout) findViewById(R.id.usernameError);
        usernameError.setError("");
        TextInputLayout password1Error = (TextInputLayout) findViewById(R.id.password1Error);
        password1Error.setError("");
        TextInputLayout password2Error = (TextInputLayout) findViewById(R.id.password2Error);
        password2Error.setError("");
        TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
        buttonError.setError("");

        Spinner userType = (Spinner) findViewById(R.id.userType);
        EditText enterusername = (EditText) findViewById(R.id.enterusername);
        EditText enterPassword1 = (EditText) findViewById(R.id.enterPassword1);
        EditText enterPassword2 = (EditText) findViewById(R.id.enterPassword2);

        String accountType = String.valueOf(userType.getSelectedItem());
        String enterusernameStr = enterusername.getText().toString();
        if (enterusernameStr.isEmpty()) {
            usernameError.setError("You need to enter an username");
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

        try {
            String hashedPassword = Hasher.SHA1(enterPassword1Str);

            String apiUrl = getString(R.string.apiurl) + "/users";
            String payload = JobMatchService.buildCreateUserCall(enterusernameStr, hashedPassword, accountType);

            CallWebService job = new CallWebService();
            job.execute(apiUrl, payload);
        }
        catch (Exception ex) {
            buttonError.setError("Error creating user");
            return;
        }

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
                    if ((boolean) response.get("duplicateUser")) {
                        TextInputLayout usernameError = (TextInputLayout) findViewById(R.id.usernameError);
                        usernameError.setError("username already exists");
                    }
                    else {
                        TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
                        buttonError.setError("Error creating user");
                    }
                }
                else {
                    String userName = (String) response.get("username");
                    AccountManager accountManager = AccountManager.get(getApplicationContext());
                    Account account = new Account(userName, "com.triosstudent.aleks.jobmatch.ACCOUNT");
                    Bundle userData = new Bundle ();
                    userData.putString("user_id", (String) response.get("user_id"));
                    userData.putString("type", (String) response.get("type"));
                    accountManager.addAccountExplicitly(account, "password", userData);

                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currentUser", userName);
                    editor.commit();

                    Intent intent = new Intent (createUser.this, mainPage.class);
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
