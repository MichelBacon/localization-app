package com.cegepba.localization_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashScreen extends Activity implements Runnable {

    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mThread = new Thread( this);
        mThread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }
}
