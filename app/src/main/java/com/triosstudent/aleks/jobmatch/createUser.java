package com.triosstudent.aleks.jobmatch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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
    }

    public void ConnectToDatabase() {
        try {

            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String username = "XXXXXXXXX";
            String password = "XXXXXX";
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.188.4.83:1433/DATABASE;user=" + username + ";password=" + password);

            Log.w("Connection","open");
            Statement stmt = DbConn.createStatement();
            ResultSet reset = stmt.executeQuery(" select * from users ");


            EditText num = (EditText) findViewById(R.id.displaymessage);
            num.setText(reset.getString(1));

            DbConn.close();

        }
        catch (Exception e)
        {
            Log.w("Error connection","" + e.getMessage());
        }
    }
}
