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

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected()) {
       //         Toast.makeText(SplashScreen.this, "Network Available", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.finish();
                startActivity(intent);
            } else {

            try {

                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);

                builder.setTitle("Information");
                builder.setMessage("Internet not available, check your internet connectivity and try again");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashScreen.this.finish();
                    }

                });
                AlertDialog alert = builder.create();
                alert.show();

            } catch (Exception e) {
                System.out.println("alert=" + e);
            }
        }
            finish();
        }
        }, SPLASH_TIME_OUT);

    }


}
