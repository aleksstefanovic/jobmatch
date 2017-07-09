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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.StackView;
import android.widget.TextView;

import com.triosstudent.aleks.jobmatch.questionnaires.QuestionnaireCard;
import com.triosstudent.aleks.jobmatch.questionnaires.QuestionnaireCardAdapter;
import com.triosstudent.aleks.jobmatch.responses.ResponseCard;
import com.triosstudent.aleks.jobmatch.responses.ResponseCardAdapter;
import com.triosstudent.aleks.jobmatch.utilities.JobMatchService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.accounts.AccountManager.get;

public class Responses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.responses);

        Intent intent = getIntent();
        final String qId = intent.getStringExtra("questionnaire_id");
        final String q1 = intent.getStringExtra("q1");
        final String q2 = intent.getStringExtra("q2");
        final String q3 = intent.getStringExtra("q3");
        final String q4 = intent.getStringExtra("q4");
        final String qTitle = intent.getStringExtra("qTitle");

        TextView cardTxtView = (TextView) findViewById(R.id.cardTxt);
        cardTxtView.setText("Responses for " + qTitle);

        String apiUrl = getString(R.string.apiurl) + "/responses";
        apiUrl = apiUrl + "?questionnaire_id=" + qId;
        System.out.println(apiUrl);
        GetResponses job = new GetResponses();
        job.execute(apiUrl);
    }

    class GetResponses extends AsyncTask<String, Void, String> {

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
                    TextView cardTxtView = (TextView) findViewById(R.id.cardTxt);
                    cardTxtView.setText("Error Displaying Responses");
                }
                else {
                    final StackView responseCards = (StackView) findViewById(R.id.responseCards);
                    ArrayList responseList = new ArrayList<ResponseCard>();
                    JSONArray responses = response.getJSONArray("responses");
                    for (int i=0; i < responses.length(); i++) {
                        JSONObject obj = (JSONObject) responses.get(i);
                        ResponseCard card = new ResponseCard(obj.getString("response1"), obj.getString("response2"), obj.getString("response3"), obj.getString("response4"), obj.getInt("user_id"), obj.getInt("id"));
                        responseList.add(card);
                    }

                    ResponseCardAdapter adapter = new ResponseCardAdapter(responseList, Responses.this);
                    responseCards.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    /*responseCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                            ResponseCard item = (ResponseCard) parent.getItemAtPosition(position);

                            Intent intent = new Intent(mainPage.this, Questionnaire.class);
                            intent.putExtra("qId", item.getId());
                            intent.putExtra("q1", item.getQuestion1());
                            intent.putExtra("q2", item.getQuestion2());
                            intent.putExtra("q3", item.getQuestion3());
                            intent.putExtra("q4", item.getQuestion4());
                            intent.putExtra("qTitle", item.getTitle());
                            startActivity(intent);
                        }
                    });*/
                }
            }
            catch (JSONException ex) {
                TextView cardTxtView = (TextView) findViewById(R.id.cardTxt);
                cardTxtView.setText("Error Displaying Responses");
            }
        }
    }
}
