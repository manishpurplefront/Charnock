package com.charnock.dev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Database;
import com.charnock.dev.model.Profile_Model;
import com.charnock.dev.parsers.Profile_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewProfile extends Activity {

    ProgressDialog progress;
    TextView nametv, emailtv, phonetv, addresstv, addresstv2, addresstv3, citytv, state;
    List<Database> database;
    List<Profile_Model> feedlist;
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Profile" + "</font>")));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);

        progress = new ProgressDialog(ViewProfile.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        nametv = (TextView) findViewById(R.id.viewedit_name);
        emailtv = (TextView) findViewById(R.id.viewedit_email);
        phonetv = (TextView) findViewById(R.id.viewedit_phone);
        addresstv = (TextView) findViewById(R.id.viewedit_address);
        addresstv2 = (TextView) findViewById(R.id.viewedit_address2);
        addresstv3 = (TextView) findViewById(R.id.viewedit_address3);
        state = (TextView) findViewById(R.id.viewedit_state);
        citytv = (TextView) findViewById(R.id.viewedit_city);

        try {
            dbhelp.DatabaseHelper2 entry = new dbhelp.DatabaseHelper2(ViewProfile.this);
            entry.close();
            database = entry.getdatabase();
        } catch (Exception e) {
            Log.d("Exception : ", "" + e);
            Log.d("exception", "user does not exist");
        }

        Internet_Access ac = new Internet_Access();
        if (ac.isonline(ViewProfile.this)) {
            get_data(getResources().getString(R.string.url_reference) + "home/view_profile.php");
        } else {
            Toast.makeText(ViewProfile.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }
    }

    private void update_display() {
        if (feedlist != null) {
            for (Profile_Model flower : feedlist) {
                nametv.setText(flower.getName());
                emailtv.setText(flower.getEmail());
                phonetv.setText(flower.getPhone());
                addresstv.setText(flower.getAddress_line1());
                addresstv2.setText(flower.getAddress_line2());
                addresstv3.setText(flower.getAddress_line3());
                state.setText(flower.getState());
                if (flower.getPincode().equals("")) {
                    citytv.setText(flower.getCity());
                } else {
                    citytv.setText(flower.getCity() + " - " + flower.getPincode());
                }
            }
        } else {
            Toast.makeText(ViewProfile.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
    }

    private void get_data(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("response", s);
                feedlist = Profile_JSONParser.parserFeed(s);
                update_display();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", database.get(0).getId());
                params.put("email", database.get(0).getEmail());
                params.put("business_id", database.get(0).getBusiness_id());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewedit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                progress.show();
                Intent intent = new Intent(ViewProfile.this, Editprofile.class);
                this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            default:
                return true;
        }
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ViewProfile.this, MainActivity.class);
        intent.putExtra("redirection", "Settings");
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

}