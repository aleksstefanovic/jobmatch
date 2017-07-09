package com.triosstudent.aleks.jobmatch;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Adapter;
import android.widget.StackView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.accounts.AccountManager.get;

public class mainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final boolean newUser = intent.getBooleanExtra("newUser", false);

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("currentUser", null);

        AccountManager accountManager = get(this);
        Account[] accounts = accountManager.getAccountsByType("com.triosstudent.aleks.jobmatch.ACCOUNT");
        Account currentAccount = null;
        for (int i=0; i < accounts.length; i++) {
            System.out.println("ACCOUNT: " + accounts[i].name);
            if (accounts[i].name.equals(currentUser)) {
                currentAccount = accounts[i];
            }
        }

        if (currentAccount == null) {
            Intent backwardsIntent = new Intent (mainPage.this, MainActivity.class);
            startActivity(backwardsIntent);
        }
        final String user_id = accountManager.getUserData(currentAccount, "user_id");
        final String email = currentAccount.name;
        final String type = accountManager.getUserData(currentAccount, "type");


        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        if (newUser) {
            welcomeMessage.setText("Welcome to JobMatch!");
        }
        else {
            welcomeMessage.setText("Welcome back!");
        }

        FloatingActionButton createQuestionnaire = (FloatingActionButton) findViewById(R.id.createQuestionnaire);
        if (!type.equals("Company")) {
            createQuestionnaire.hide();
        }
        else {
            createQuestionnaire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "Creating Questionnaire", Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show();
                    Intent intent = new Intent (mainPage.this, createQuestionnaire.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("email", email);
                    intent.putExtra("type", type);
                    intent.putExtra("newUser", false);
                    startActivity(intent);
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            ViewGroup parent = (ViewGroup) findViewById(R.id.header);
            int childCount = parent.getChildCount();
            for (int i=0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                parent.removeView(childView);
            }
            View C = getLayoutInflater().inflate(R.layout.profile, parent, false);
            parent.addView(C, 0);
        }
        else if (id == R.id.nav_questions) {
            ViewGroup parent = (ViewGroup) findViewById(R.id.header);
            int childCount = parent.getChildCount();
            for (int i=0; i < childCount; i++) {
                View childView = parent.getChildAt(i);
                parent.removeView(childView);
            }
            View C = getLayoutInflater().inflate(R.layout.questionnaires, parent, false);
            parent.addView(C, 0);


            String apiUrl = getString(R.string.apiurl) + "/questionnaires";
            apiUrl = apiUrl + "?user_id=15";
            System.out.println(apiUrl);
            CallWebService job = new CallWebService();
            job.execute(apiUrl);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                }
                else {
                    StackView questionnaireCards = (StackView) findViewById(R.id.questionnaireCards);
                    ArrayList questionnaireList = new ArrayList<QuestionnaireCard>();
                    JSONArray questionnaires = response.getJSONArray("questionnaires");
                    for (int i=0; i < questionnaires.length(); i++) {
                        JSONObject obj = (JSONObject) questionnaires.get(i);
                        QuestionnaireCard card = new QuestionnaireCard(obj.getString("question1"), obj.getString("question2"), obj.getString("question3"), obj.getString("question4"), obj.getString("title"));
                        questionnaireList.add(card);
                    }

                    QuestionnaireCardAdapter adapter = new QuestionnaireCardAdapter(questionnaireList, mainPage.this);
                    questionnaireCards.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
            catch (JSONException ex) {

            }
        }
    }
}
