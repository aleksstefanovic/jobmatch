package com.triosstudent.aleks.jobmatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Questionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire);

        Intent intent = getIntent();
        final int qId = intent.getIntExtra("qId", 0);
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

    public void viewResponses (View view) {
        Intent intent = getIntent();
        final String qId = Integer.toString(intent.getIntExtra("qId", 0));
        final String q1 = intent.getStringExtra("q1");
        final String q2 = intent.getStringExtra("q2");
        final String q3 = intent.getStringExtra("q3");
        final String q4 = intent.getStringExtra("q4");
        final String qTitle = intent.getStringExtra("qTitle");

        Intent responsesIntent = new Intent (Questionnaire.this, Responses.class);
        responsesIntent.putExtra("questionnaire_id", qId);
        responsesIntent.putExtra("q1", q1);
        responsesIntent.putExtra("q2", q2);
        responsesIntent.putExtra("q3", q3);
        responsesIntent.putExtra("q4", q4);
        responsesIntent.putExtra("qTitle", qTitle);
        startActivity(responsesIntent);
    }
}
