package com.triosstudent.aleks.jobmatch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class createUser extends Activity {

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void acceptNewUser (View view) {
        this.view = view;
        Spinner userType = (Spinner) findViewById(R.id.userType);
        EditText enterEmail = (EditText) findViewById(R.id.enterEmail);
        EditText enterPassword1 = (EditText) findViewById(R.id.enterPassword1);
        EditText enterPassword2 = (EditText) findViewById(R.id.enterPassword2);

        String accountType = String.valueOf(userType.getSelectedItem());
        String enterEmailStr = enterEmail.getText().toString();
        String enterPassword1Str = enterPassword1.getText().toString();
        String enterPassword2Str = enterPassword2.getText().toString();

        String apiUrl = getString(R.string.apiurl);
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", enterEmailStr);
            payload.put("password", enterPassword1Str);
            payload.put("type", accountType);

            System.out.println(payload.toString());
            System.out.println(apiUrl+"/users");
            callWebService job = new callWebService();
            job.execute(apiUrl+"/users", payload.toString());
        }
        catch (JSONException ex) {

        }
    }

    public static String executePost(String targetURL, String payload) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    class callWebService extends AsyncTask<String, Void, String> {

        protected String doInBackground(String[] params) {
            String response = executePost(params[0], params[1]);
            return response;
        }

        protected void onPostExecute(String message) {
            System.out.println(message);
        }
    }

}
