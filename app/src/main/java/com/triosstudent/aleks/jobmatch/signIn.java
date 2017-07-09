package com.triosstudent.aleks.jobmatch;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.accounts.AccountManager.get;

public class signIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        AccountManager accountManager = get(this);
        Account[] accounts = accountManager.getAccountsByType("com.triosstudent.aleks.jobmatch.ACCOUNT");

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i < accounts.length; i++) {
            list.add(accounts[i].name);
        }
        list.add("Sign In with Different Account");
        if (list.size() != 1) {
            final CharSequence accountNames[] = list.toArray(new CharSequence[list.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick an account");
            builder.setItems(accountNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String accountName = (String) accountNames[which];
                    if (!accountName.equals("Sign In with Different Account")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("currentUser", accountName);
                        editor.commit();

                        Intent intent = new Intent (signIn.this, mainPage.class);
                        intent.putExtra("newUser", false);
                        startActivity(intent);
                    }
                }
            });
            builder.show();
        }
    }

    public void signInUser (View view) {
        TextInputLayout usernameError = (TextInputLayout) findViewById(R.id.usernameError);
        usernameError.setError("");
        TextInputLayout passwordError = (TextInputLayout) findViewById(R.id.passwordError);
        passwordError.setError("");
        TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
        buttonError.setError("");

        Spinner userType = (Spinner) findViewById(R.id.userType);
        EditText enterusername = (EditText) findViewById(R.id.enterusername);
        EditText enterPassword = (EditText) findViewById(R.id.enterPassword);

        String accountType = String.valueOf(userType.getSelectedItem());
        String enterusernameStr = enterusername.getText().toString();
        if (enterusernameStr.isEmpty()) {
            usernameError.setError("You need to enter an username");
            return;
        }
        String enterPasswordStr = enterPassword.getText().toString();
        if (enterPasswordStr.isEmpty()) {
            passwordError.setError("You need to enter a password");
            return;
        }

        try {
            String hashedPassword = Hasher.SHA1(enterPasswordStr);

            String apiUrl = getString(R.string.apiurl) + "/users";
            apiUrl = apiUrl + "?username=" + enterusernameStr + "&password=" + hashedPassword + "&type=" + accountType;
            System.out.println(apiUrl);

            CallWebService job = new CallWebService();
            job.execute(apiUrl);
        }
        catch (Exception ex) {
            buttonError.setError("Could not find user");
            return;
        }
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

                    Intent intent = new Intent (signIn.this, mainPage.class);
                    intent.putExtra("newUser", false);
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
