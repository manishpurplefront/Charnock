package com.charnock.dev;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;

import java.util.HashMap;
import java.util.Map;


public class Change_Password extends Activity {

    EditText et, et2, et3;
    String password = "", newpassword = "", confirmpassword = "";
    Button b;
    ProgressDialog progress;
    CheckBox ch;
    String user_id = "";
    String user_email = "";
    private String TAG = Change_Password.class.getSimpleName();
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Change Password" + "</font>")));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);

        progress = new ProgressDialog(Change_Password.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        et = (EditText) findViewById(R.id.changepassword_password);
        et2 = (EditText) findViewById(R.id.changepassword_newpassword);
        et3 = (EditText) findViewById(R.id.changepassword_confirmpassword);

        ch = (CheckBox) findViewById(R.id.changepassword_showpassword);
        ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et.setTransformationMethod(null);
                    et2.setTransformationMethod(null);
                    et3.setTransformationMethod(null);
                    ch.setText("Hide Password");
                } else {
                    et.setTransformationMethod(new PasswordTransformationMethod());
                    et2.setTransformationMethod(new PasswordTransformationMethod());
                    et3.setTransformationMethod(new PasswordTransformationMethod());
                    ch.setText("Show password");
                }
            }
        });

        b = (Button) findViewById(R.id.changepassword_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password = et.getText().toString();
                newpassword = et2.getText().toString();
                confirmpassword = et3.getText().toString();
                if (password.equals("") || password.isEmpty() || password.trim().isEmpty()) {
                    et.setError(getResources().getString(R.string.correct_password));
                    Toast.makeText(Change_Password.this, R.string.correct_password, Toast.LENGTH_LONG).show();
                } else if (newpassword.equals("") || newpassword.isEmpty() || newpassword.trim().isEmpty()) {
                    et2.setError(getResources().getString(R.string.password_new));
                    Toast.makeText(Change_Password.this, R.string.password_new, Toast.LENGTH_LONG).show();
                } else if (confirmpassword.equals("") || confirmpassword.isEmpty() || confirmpassword.trim().isEmpty()) {
                    et3.setError(getResources().getString(R.string.correct_confirm_password));
                    Toast.makeText(Change_Password.this, R.string.correct_confirm_password, Toast.LENGTH_LONG).show();
                } else if (password.length() < 6) {
                    et.setError(getResources().getString(R.string.password_limit_correct));
                    Toast.makeText(Change_Password.this, R.string.password_limit_correct, Toast.LENGTH_LONG).show();
                } else if (newpassword.length() < 6) {
                    et2.setError(getResources().getString(R.string.password_new_limit));
                    Toast.makeText(Change_Password.this, R.string.password_new_limit, Toast.LENGTH_LONG).show();
                } else if (confirmpassword.length() < 6) {
                    et3.setError(getResources().getString(R.string.confirm_password_limit_correct));
                    Toast.makeText(Change_Password.this, R.string.confirm_password_limit_correct, Toast.LENGTH_LONG).show();
                } else if (!password.trim().equals(password)) {
                    et.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                } else if (!newpassword.trim().equals(newpassword)) {
                    et2.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                } else if (!confirmpassword.trim().equals(confirmpassword)) {
                    et3.setError(getResources().getString(R.string.password_space));
                    Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                } else if (newpassword.equals(confirmpassword)) {
                    if (newpassword.trim().equals(newpassword)) {
                        try {
                            MCrypt mcrypt = new MCrypt();
                            progress.show();
                            newpassword = MCrypt.bytesToHex(mcrypt.encrypt(newpassword));
                            password = MCrypt.bytesToHex(mcrypt.encrypt(password));
                            Toast.makeText(Change_Password.this, R.string.password_match, Toast.LENGTH_LONG).show();
                            if (isonline()) {
                                StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/change_password.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                Log.d("password response", s);
                                                updatedislay(s);
                                                progress.hide();
                                            }
                                        },

                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
//                                                    VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                                                progress.hide();
                                                Toast.makeText(Change_Password.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                                                //          Toast.makeText(Change_Password.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Change_Password.this, MainActivity.class);
                                                Change_Password.this.finish();
                                                startActivity(intent);
                                                Change_Password.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            }
                                        }) {

                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id", user_id);
                                        params.put("email", user_email);
                                        params.put("password", password);
                                        params.put("newpassword", newpassword);
                                        return params;
                                    }

                                };
                                AppController.getInstance().addToRequestQueue(request, tag_string_req);
                            } else {
                                Toast.makeText(Change_Password.this, R.string.password_correct2, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(Change_Password.this, R.string.password_correct2, Toast.LENGTH_LONG).show();
                            //                    Log.d("Exception while password decryption : ",""+e);
                        }

                    } else {
                        Toast.makeText(Change_Password.this, R.string.password_space, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Change_Password.this, R.string.password_confirm_password_not_match, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updatedislay(String res) {
        switch (res) {
            case "password updated":
                dbhelp entry = new dbhelp(Change_Password.this);
                entry.open();
                entry.updatepassword(user_id, newpassword);
                entry.close();
                AlertDialog.Builder builder = new AlertDialog.Builder(Change_Password.this);
                builder.setMessage(getResources().getString(R.string.changepassword_success))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progress.show();
                                Intent intent = new Intent(Change_Password.this, MainActivity.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("email", user_email);
                                Change_Password.this.finish();
                                startActivity(intent);
                                Change_Password.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case "Passwords does not match":
                Toast.makeText(Change_Password.this, R.string.password_online_local_different, Toast.LENGTH_LONG).show();
                break;
            case "Email not set":
                Toast.makeText(Change_Password.this, R.string.unknownerror, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(Change_Password.this, R.string.unknownerror3, Toast.LENGTH_LONG).show();
                break;
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) Change_Password.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }


    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Change_Password.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Change_Password.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }
}

