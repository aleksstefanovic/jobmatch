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

public class createQuestionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_questionnaire);
    }

    public void submitQuestionnaire (View view) {
        TextInputLayout question1Error = (TextInputLayout) findViewById(R.id.question1Error);
        question1Error.setError("");
        TextInputLayout question2Error = (TextInputLayout) findViewById(R.id.question2Error);
        question2Error.setError("");
        TextInputLayout question3Error = (TextInputLayout) findViewById(R.id.question3Error);
        question3Error.setError("");
        TextInputLayout question4Error = (TextInputLayout) findViewById(R.id.question4Error);
        question4Error.setError("");
        TextInputLayout titleError = (TextInputLayout) findViewById(R.id.titleError);
        titleError.setError("");

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
        final String email = intent.getStringExtra("email");
        final String type = intent.getStringExtra("type");
        final boolean newUser = intent.getBooleanExtra("newUser", false);

        EditText question1 = (EditText) findViewById(R.id.question1);
        EditText question2 = (EditText) findViewById(R.id.question2);
        EditText question3 = (EditText) findViewById(R.id.question3);
        EditText question4 = (EditText) findViewById(R.id.question4);
        EditText title = (EditText) findViewById(R.id.title);

        String question1Str = question1.getText().toString();
        if (question1Str.isEmpty()) {
            question1Error.setError("You must enter a question");
            return;
        }
        String question2Str = question2.getText().toString();
        if (question2Str.isEmpty()) {
            question2Error.setError("You must enter a question");
            return;
        }
        String question3Str = question3.getText().toString();
        if (question3Str.isEmpty()) {
            question3Error.setError("You must enter a question");
            return;
        }
        String question4Str = question4.getText().toString();
        if (question4Str.isEmpty()) {
            question4Error.setError("You must enter a question");
            return;
        }
        String titleStr = title.getText().toString();
        if (titleStr.isEmpty()) {
            titleError.setError("You must enter a title");
            return;
        }

        String apiUrl = getString(R.string.apiurl) + "/questionnaires";
        String payload = JobMatchService.buildCreateQuestionnaireCall(user_id, question1Str, question2Str, question3Str, question4Str, titleStr);

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
                    buttonError.setError("Error creating questionnaire");
                }
                else {
                    Intent intent = new Intent (createQuestionnaire.this, mainPage.class);
                    intent.putExtra("newUser", false);
                    startActivity(intent);
                }
            }
            catch (JSONException ex) {
                TextInputLayout buttonError = (TextInputLayout) findViewById(R.id.buttonError);
                buttonError.setError("Error creating questionnaire");
            }
        }
    }
}
