package com.triosstudent.aleks.jobmatch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
