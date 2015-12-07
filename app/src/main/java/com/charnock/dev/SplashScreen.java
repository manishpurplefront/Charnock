package com.charnock.dev;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            if (isonline()) {

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            } else {
                alert_dialog();
            }
        }
        }, SPLASH_TIME_OUT);
    }

    protected void alert_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setMessage(getResources().getString(R.string.no_internet_splashscreen))
                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isonline()) {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            SplashScreen.this.finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        } else {
                            alert_dialog();
                        }
                    }
                })
                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SplashScreen.this.finish();
                    }

                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }
}