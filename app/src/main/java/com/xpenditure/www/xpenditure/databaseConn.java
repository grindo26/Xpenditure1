package com.xpenditure.www.xpenditure;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Swaraj on 21-01-2018.
 */

public class databaseConn extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
