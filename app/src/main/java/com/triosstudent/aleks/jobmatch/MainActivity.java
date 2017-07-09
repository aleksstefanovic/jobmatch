package com.triosstudent.aleks.jobmatch;

import android.accounts.AccountManager;
import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import static android.accounts.AccountManager.get;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (currentAccount != null) {
            Intent intent = new Intent (MainActivity.this, mainPage.class);
            intent.putExtra("newUser", false);
            startActivity(intent);
        }
    }

    public void createUserClick (View view) {
        Intent intent = new Intent(this, createUser.class);
        startActivity(intent);
    }

    public void signInUser (View view) {
        Intent intent = new Intent(this, signIn.class);
        startActivity(intent);
    }


}
