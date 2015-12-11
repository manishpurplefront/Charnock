package com.charnock.dev;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.ExistingUser_Model;
import com.charnock.dev.model.Response_Model;
import com.charnock.dev.parsers.ExistingUser_JSONParser;
import com.charnock.dev.parsers.Response_JSONParser;
import com.charnock.dev.pushnotification.QuickstartPreferences;
import com.charnock.dev.pushnotification.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends Fragment {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LoginActivity";
    Button btnSignIn;
    TextView tv_signup;
    EditText editTextUserName;
    EditText editTextPassword;
    String password = "", email = "";
    ProgressDialog progress;
    List<ExistingUser_Model> feedslist;
    View rootView;
    List<Response_Model> response_data;
    private String tag_string_req_recieve2 = "string_req_recieve2";
    private String tag_string_req_send = "string_req_send";
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_login, container, false);
        setHasOptionsMenu(true);

        getActivity().getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Login" + "</font>")));
        getActivity().getActionBar().setHomeButtonEnabled(true);
        getActivity().getActionBar().setIcon(R.drawable.pf);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        btnSignIn = (Button) rootView.findViewById(R.id.existinguser_login);
        editTextUserName = (EditText) rootView.findViewById(R.id.existinguser_username);
        editTextPassword = (EditText) rootView.findViewById(R.id.existinguser_password);
        tv_signup = (TextView) rootView.findViewById(R.id.tv_signup);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                progress.show();
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

                email = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
                validationfunction();

//                if (sentToken) {
//                    Toast.makeText(getActivity(),getResources().getString(R.string.gcm_send_message),Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getActivity(),getResources().getString(R.string.token_error_message),Toast.LENGTH_LONG).show();
//                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (checkPlayServices()) {
                    // Start IntentService to register this application with GCM.
                    Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
                    getActivity().startService(intent);
                    progress.show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.common_google_play_services_install_text_phone), Toast.LENGTH_LONG).show();
                }
            }
        });

        tv_signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Registration.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        return rootView;
    }

    public void validationfunction() {

        progress.hide();
        if (email.equals("") || email.isEmpty() || email.trim().isEmpty()) {
            Toast.makeText(getActivity(), R.string.correct_email, Toast.LENGTH_LONG).show();
        } else if (!isEmailValid(email)) {
            Toast.makeText(getActivity(), R.string.correct_email2, Toast.LENGTH_LONG).show();
        } else if (password.equals("") || password.isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(getActivity(), R.string.dialog_password_hint, Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            Toast.makeText(getActivity(), R.string.password_limit_correct, Toast.LENGTH_LONG).show();
        } else if (password.trim().length() < 6) {
            Toast.makeText(getActivity(), R.string.password_limit_correct, Toast.LENGTH_LONG).show();
        } else {
            if (password.trim().equals(password)) {
                if (email.trim().equals(email)) {
                    Internet_Access ac = new Internet_Access();
                    if (ac.isonline(getActivity())) {
                        progress.show();
                        try {
                            MCrypt mcrypt = new MCrypt();
                            password = MCrypt.bytesToHex(mcrypt.encrypt(password));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/login.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String arg0) {
                                        Log.d("response", arg0);
                                        Log.d("here in sucess", "sucess");
                                        progress.hide();
                                        feedslist = ExistingUser_JSONParser.parserFeed(arg0);
                                        updatedisplay();
                                    }
                                },

                                new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError arg0) {
                                        progress.hide();
                                        Toast.makeText(getActivity(), R.string.nointernetaccess, Toast.LENGTH_LONG).show();
//                                            Toast.makeText(Login.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getActivity(), SplashScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                    }
                                }) {

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("email", email);
                                Log.d("email1", email);
                                params.put("password", password);
                                Log.d("password1", password);

                                return params;
                            }

                        };
                        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
                    } else {
                        Toast.makeText(getActivity(), R.string.nointernetconnection, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), R.string.correct_email2, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.password_space, Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }

    public void updatedisplay() {
        progress.hide();
        Log.d("updatedisplay_out", "updatedisplay_out");
        if (feedslist != null) {

            for (final ExistingUser_Model flower : feedslist) {
                String success = flower.getId();
                switch (success) {
                    case "Error": {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.unknownerror7)
                                .setMessage(R.string.unknownerror10)
                                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(getActivity(), R.string.email_password, Toast.LENGTH_LONG).show();
                        break;
                    }
                    case "Error Password": {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.dialog_password_wrong_title)
                                .setMessage(R.string.dialog_password_wrong_message)
                                .setPositiveButton(R.string.dialog_password_wrong_button, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), Reset_Password.class);
                                        getActivity().finish();
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                    }
                                })
                                .setNegativeButton(R.string.dialog_password_wrong_button2, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        Toast.makeText(getActivity(), R.string.email_password, Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                        if (flower.getPermission_id().equals("2") && flower.getRole_id().equals("3")) {
                            Toast.makeText(getActivity(), "This feature is currently in construction", Toast.LENGTH_LONG).show();
                        } else if (flower.getPermission_id().equals("4") && flower.getRole_id().equals("4")) {
                            Toast.makeText(getActivity(), "This feature is currently in construction", Toast.LENGTH_LONG).show();
                        } else {
                            dbhelp entry = new dbhelp(getActivity());
                            entry.open();
                            entry.createuser(flower.getId(), flower.getName(), flower.getEmail(), flower.getPassword(), flower.getPhone(), flower.getBusiness_id(), flower.getProfile_id(), flower.getNode_id(), flower.getRole_id(), flower.getPermission_id(), flower.getLevel_id());
                            Log.d("id", flower.getId());
                            Log.d("name", flower.getName());
                            Log.d("email", email);
                            Log.d("password", password);
                            Log.d("role_id", flower.getRole_id());
                            Log.d("node_id", flower.getNode_id());
                            Log.d("business_id", flower.getBusiness_id());
                            Log.d("permission_id", flower.getPermission_id());
                            entry.close();
                            send_data();
                        }
                        break;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_password_wrong_title)
                    .setMessage(R.string.dialog_password_wrong_message)
                    .setPositiveButton(R.string.dialog_password_wrong_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), Reset_Password.class);
                            getActivity().finish();
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    })
                    .setNegativeButton(R.string.dialog_password_wrong_button2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }

    protected void send_data() {

        final String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("id", feedslist.get(0).getId());
        Log.d("email", feedslist.get(0).getEmail());
        Log.d("token", StaticVariables.token);
        Log.d("android_id", android_id);

        String url = getResources().getString(R.string.url_reference) + "messaging/register.php";
        Log.d("url", url);
        StringRequest request23 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        Log.d("response", s);
                        response_data = Response_JSONParser.parserFeed(s);
                        if (response_data != null) {
                            for (Response_Model flower : response_data) {
                                if (flower.getId().equals("Error")) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror8), Toast.LENGTH_LONG).show();
                                } else if (flower.getId().equals("Success")) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("redirection", "Service Create");
                                    getActivity().finish();
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                } else if (flower.getId().equals("Failure")) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("redirection", "Service Create");
                                    getActivity().finish();
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                    Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror2), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror10), Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", feedslist.get(0).getId());
                params.put("email", feedslist.get(0).getEmail());
                params.put("token", StaticVariables.token);
                params.put("android_id", android_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request23, tag_string_req_send);
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }
}