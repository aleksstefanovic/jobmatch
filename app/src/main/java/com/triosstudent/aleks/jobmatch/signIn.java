package com.triosstudent.aleks.jobmatch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

public class signIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void signInUser (View view) {
        TextInputLayout emailError = (TextInputLayout) findViewById(R.id.emailError);
        emailError.setError("");
        TextInputLayout passwordError = (TextInputLayout) findViewById(R.id.passwordError);
        passwordError.setError("");
        TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
        buttonError.setError("");

        Spinner userType = (Spinner) findViewById(R.id.userType);
        EditText enterEmail = (EditText) findViewById(R.id.enterEmail);
        EditText enterPassword = (EditText) findViewById(R.id.enterPassword);

        String accountType = String.valueOf(userType.getSelectedItem());
        String enterEmailStr = enterEmail.getText().toString();
        if (enterEmailStr.isEmpty()) {
            emailError.setError("You need to enter an email");
            return;
        }
        String enterPasswordStr = enterPassword.getText().toString();
        if (enterPasswordStr.isEmpty()) {
            passwordError.setError("You need to enter a password");
            return;
        }

        String apiUrl = getString(R.string.apiurl) + "/users";
        apiUrl = apiUrl + "?email=" + enterEmailStr + "&password=" + enterPasswordStr + "&type=" + accountType;
        System.out.println(apiUrl);

        CallWebService job = new CallWebService();
        job.execute(apiUrl);
    }

    class CallWebService extends AsyncTask<String, Void, String> {

        protected String doInBackground(String[] params) {
            String response = JobMatchService.executeGet(params[0]);
            return response;
        }

        protected void onPostExecute(String message) {
            System.out.println(message);
            try {
                JSONObject response = new JSONObject(message);
                String code = (String) response.get("code");
                if (!code.equals("200")) {
                    TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
                    buttonError.setError("Could not find user");
                }
                else {
                    Intent intent = new Intent (signIn.this, mainPage.class);
                    startActivity(intent);
                }
            }
            catch (JSONException ex) {
                TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
                buttonError.setError("Error could not sign in user");
            }
        }
    }
}
