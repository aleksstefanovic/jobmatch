package com.triosstudent.aleks.jobmatch.auth;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class JobMatchAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        JobMatchAuthenticator authenticator = new JobMatchAuthenticator(this);
        return authenticator.getIBinder();
    }
}
