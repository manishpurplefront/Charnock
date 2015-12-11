package com.charnock.dev.pushnotification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.charnock.dev.MainActivity;
import com.charnock.dev.R;
import com.charnock.dev.SplashScreen;
import com.charnock.dev.dbhelp;
import com.charnock.dev.model.Database;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "MyGcmListenerService";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            /** Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.*/

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (extras.containsKey("This")) {
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification(extras.getString("This"));
                    Log.i(TAG, "Received: " + extras.getString("This"));
                    Log.i(TAG, "Received: " + extras.getString("This"));
                }

                if (extras.containsKey("customer_service_request")) {
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification2(extras.getString("customer_service_request"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_request"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_request"));
                }

                if (extras.containsKey("customer_branch_enginer")) {
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification3(extras.getString("customer_branch_enginer"));
                    Log.i(TAG, "Received: " + extras.getString("customer_branch_enginer"));
                    Log.i(TAG, "Received: " + extras.getString("customer_branch_enginer"));
                }

                if (extras.containsKey("customer_service_date")) {
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification4(extras.getString("customer_service_date"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_date"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_date"));
                }

                if (extras.containsKey("customer_service_status")) {
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification5(extras.getString("customer_service_status"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_status"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_status"));
                }

                if (extras.containsKey("customer_service_engineer_assign")) {
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    // Post notification of received message.
                    sendNotification6(extras.getString("customer_service_engineer_assign"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_engineer_assign"));
                    Log.i(TAG, "Received: " + extras.getString("customer_service_engineer_assign"));
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashScreen.class), 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(msg);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }

    private void sendNotification2(String msg) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("redirection", "Service Create");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New Service Request")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(msg);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }

    private void sendNotification3(String msg) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;
        String name = "";
        try {
            JSONObject parentObject = new JSONObject(msg);
            String service_id = parentObject.getString("service_id");
            name = parentObject.getString("name");
            String date = parentObject.getString("date");
            List<Database> database;
            dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(this);
            database = db.getdatabase();

            if (database.get(0).getPermission_id().equals("4") && database.get(0).getRole_id().equals("4")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("2") && database.get(0).getRole_id().equals("3")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("3") && database.get(0).getRole_id().equals("5")) {
                intent = new Intent(this, com.charnock.dev.service_engineer.Service_Request_Details_Engineer.class);
            } else {
                intent = new Intent(this, com.charnock.dev.Service_Request_Deatils.class);
            }
            intent.putExtra("date", service_id);
            intent.putExtra("id", date);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Branch Assigned")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(name);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }

    private void sendNotification4(String msg) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;
        String name = "";
        try {
            JSONObject parentObject = new JSONObject(msg);
            String service_id = parentObject.getString("service_id");
            name = parentObject.getString("name");
            String date = parentObject.getString("date");
            List<Database> database;
            dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(this);
            database = db.getdatabase();

            if (database.get(0).getPermission_id().equals("4") && database.get(0).getRole_id().equals("4")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("2") && database.get(0).getRole_id().equals("3")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("3") && database.get(0).getRole_id().equals("5")) {
                intent = new Intent(this, com.charnock.dev.service_engineer.Service_Request_Details_Engineer.class);
            } else {
                intent = new Intent(this, com.charnock.dev.Service_Request_Deatils.class);
            }
            intent.putExtra("date", service_id);
            intent.putExtra("id", date);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Visit date updated")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(name);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }

    private void sendNotification6(String msg) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;
        String name = "";
        try {
            JSONObject parentObject = new JSONObject(msg);
            String service_id = parentObject.getString("service_id");
            name = parentObject.getString("name");
            String date = parentObject.getString("date");
            List<Database> database;
            dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(this);
            database = db.getdatabase();

            if (database.get(0).getPermission_id().equals("4") && database.get(0).getRole_id().equals("4")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("2") && database.get(0).getRole_id().equals("3")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("3") && database.get(0).getRole_id().equals("5")) {
                intent = new Intent(this, com.charnock.dev.service_engineer.Service_Request_Details_Engineer.class);
            } else {
                intent = new Intent(this, com.charnock.dev.Service_Request_Deatils.class);
            }
            intent.putExtra("date", service_id);
            intent.putExtra("id", date);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Engineer Assigned")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(name);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }

    private void sendNotification5(String msg) {
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;
        String name = "";
        try {
            JSONObject parentObject = new JSONObject(msg);
            String service_id = parentObject.getString("service_id");
            name = parentObject.getString("name");
            String date = parentObject.getString("date");
            List<Database> database;
            dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(this);
            database = db.getdatabase();

            if (database.get(0).getPermission_id().equals("4") && database.get(0).getRole_id().equals("4")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("2") && database.get(0).getRole_id().equals("3")) {
                intent = new Intent(this, com.charnock.dev.branch_manager.Service_Request_Deatils.class);
            } else if (database.get(0).getPermission_id().equals("3") && database.get(0).getRole_id().equals("5")) {
                intent = new Intent(this, com.charnock.dev.service_engineer.Service_Request_Details_Engineer.class);
            } else {
                intent = new Intent(this, com.charnock.dev.Service_Request_Deatils.class);
            }
            intent.putExtra("date", service_id);
            intent.putExtra("id", date);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Status updated")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(alarmSound)
                .setContentText(name);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);
    }
}
