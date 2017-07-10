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

import com.triosstudent.aleks.jobmatch.utilities.JobMatchService;

import org.json.JSONException;
import org.json.JSONObject;

import static android.accounts.AccountManager.get;

public class UserResponse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userresponse);

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        final String r1 = intent.getStringExtra("r1");
        final String r2 = intent.getStringExtra("r2");
        final String r3 = intent.getStringExtra("r3");
        final String r4 = intent.getStringExtra("r4");

        TextView r1Txt = (TextView) findViewById(R.id.q1);
        TextView r2Txt = (TextView) findViewById(R.id.q2);
        TextView r3Txt = (TextView) findViewById(R.id.q3);
        TextView r4Txt = (TextView) findViewById(R.id.q4);

        r1Txt.setText(r1);
        r2Txt.setText(r2);
        r3Txt.setText(r3);
        r4Txt.setText(r4);

        String apiUrl = getString(R.string.apiurl) + "/users";
        apiUrl = apiUrl + "?user_id=" + user_id;

        GetProfile job = new GetProfile();
        job.execute(apiUrl);
    }

    class GetProfile extends AsyncTask<String, Void, String> {

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
                    TextView userIdView = (TextView) findViewById(R.id.userName);
                    userIdView.setText("Error Displaying Profile");
                }
                else {
                    TextView fullNameView = (TextView) findViewById(R.id.fullName);
                    TextView addressView = (TextView) findViewById(R.id.address);
                    TextView phoneView = (TextView) findViewById(R.id.phone);
                    TextView emailView = (TextView) findViewById(R.id.email);
                    TextView userIdView = (TextView) findViewById(R.id.userName);

                    String fullNameStr = null, addressStr = null, phoneStr = null, emailStr = null, userNameStr = null;

                    if (!response.get("fullname").equals(null)) {
                        fullNameStr = (String) response.get("fullname");
                    }
                    if (!response.get("address").equals(null)) {
                        addressStr = (String) response.get("address");
                    }
                    if (!response.get("phone").equals(null)) {
                        phoneStr = (String) response.get("phone");
                    }
                    if (!response.get("email").equals(null)) {
                        emailStr = (String) response.get("email");
                    }
                    if (!response.get("userName").equals(null)) {
                        userNameStr = (String) response.get("userName");
                    }

                    fullNameView.setText(fullNameStr);
                    addressView.setText(addressStr);
                    phoneView.setText(phoneStr);
                    emailView.setText(emailStr);
                    userIdView.setText(userNameStr);
                }
            }
            catch (JSONException ex) {
                TextView userIdView = (TextView) findViewById(R.id.userName);
                userIdView.setText("Error Displaying Profile");
            }
        }
    }
}
