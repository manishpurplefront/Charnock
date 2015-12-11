package com.charnock.dev;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Database;
import com.charnock.dev.model.Service_Request_Deatils_Model;
import com.charnock.dev.parsers.Service_Request_Details_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service_Request_Deatils extends Activity {


    ProgressDialog progress;
    List<Service_Request_Deatils_Model> feedlist;
    int i;
    String id;
    List<Database> database;
    String date = "", service_id = "";
    TextView device_counts;
    String deviceCounts = "";
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_request_details);

        device_counts = (TextView) findViewById(R.id.total_devices_no);
        progress = new ProgressDialog(Service_Request_Deatils.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();
        Button jobSheet = (Button) findViewById(R.id.job_sheet);
        Button approveRequest = (Button) findViewById(R.id.approve_request);
        Button rejectRequest = (Button) findViewById(R.id.reject_request);

        jobSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Service_Request_Deatils.this, "This feature is currently in construction", Toast.LENGTH_LONG).show();

            }
        });
        approveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Service_Request_Deatils.this, "This feature is currently in construction", Toast.LENGTH_LONG).show();

            }
        });
        rejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Service_Request_Deatils.this, "This feature is currently in construction", Toast.LENGTH_LONG).show();

            }
        });

        try {
            getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Service Request Details" + "</font>")));
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        try {
            date = getIntent().getExtras().getString("date");
            service_id = getIntent().getExtras().getString("id");
        } catch (Exception e) {

        }
        dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(Service_Request_Deatils.this);
        database = db.getdatabase();

        progress.show();
        if (isonline()) {
            getdata(getResources().getString(R.string.url_reference) + "home/Service_Request_View.php");
        } else {
            Toast.makeText(Service_Request_Deatils.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

/*

        request_visit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("clicked","clicked");
                Intent intent = new Intent();
                intent.setClass(Service_Request_Deatils.this, EditRequestVisitDate.class);
                startActivity(intent);
            }
        });
        update_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("clicked","clicked");
                Intent intent = new Intent();
                intent.setClass(Service_Request_Deatils.this, UpdateStatus.class);
                startActivity(intent);
            }
        });
*/

    }

    private void getdata(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response", s);
                feedlist = Service_Request_Details_JSONParser.parserFeed(s);
                display_data();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Service_Request_Deatils.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("email", database.get(0).getEmail());
                params.put("business_id", database.get(0).getBusiness_id() + "");
                params.put("node_id", database.get(0).getNode_id() + "");
                params.put("profile_id", database.get(0).getProfile_id() + "");
                params.put("service_id", service_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    private void display_data() {
        if (feedlist != null) {

            for (Service_Request_Deatils_Model flower : feedlist) {

                TextView tv = (TextView) findViewById(R.id.customer_name);
                tv.setText(database.get(0).getName());

                tv = (TextView) findViewById(R.id.service_id);
                tv.setText("Request ID - " + service_id);

                tv = (TextView) findViewById(R.id.service_date);
                if (date.equals("null")) {
                    tv.setText("");
                } else {
                    tv.setText(date);
                }

                tv = (TextView) findViewById(R.id.customer_email);
                tv.setText(database.get(0).getEmail());

                tv = (TextView) findViewById(R.id.customer_contact);
                tv.setText(database.get(0).getPhone());

                tv = (TextView) findViewById(R.id.service_status);
                if (flower.getProduct_status().equals("null")) {
                    tv.setText("");
                } else {
                    tv.setText(flower.getProduct_status());
                }

                tv = (TextView) findViewById(R.id.service_engineer_name);
                if (flower.getUser_name().equals("null")) {
                    tv.setText("Unassigned");
                } else {
                    tv.setText(flower.getUser_name());
                }

                TextView tv2 = (TextView) findViewById(R.id.service_engineer_email);
                if (flower.getUser_email().equals("null")) {
                    tv2.setText("Unassigned");
                    tv2.setVisibility(View.GONE);
                } else {
                    tv2.setText(flower.getUser_email());
                }

                TextView tv3 = (TextView) findViewById(R.id.service_engineer_mobile);
                if (flower.getUser_contact().equals("null")) {
                    tv3.setText("Unassigned");
                    tv3.setVisibility(View.GONE);
                } else {
                    tv3.setText(flower.getUser_contact());
                }


                tv = (TextView) findViewById(R.id.service_branch_name);
                if (flower.getNode_id().equals("") || flower.getNode_id().equals("null")) {
                    tv.setText("Unassigned");
                    tv = (TextView) findViewById(R.id.service_branch_address);
                    tv.setText("Unassigned");

                    tv = (TextView) findViewById(R.id.service_branch_city);
                    tv.setVisibility(View.GONE);

                    tv = (TextView) findViewById(R.id.service_branch_email);
                    tv.setVisibility(View.GONE);

                    tv = (TextView) findViewById(R.id.service_branch_mobile);
                    tv.setVisibility(View.GONE);

                } else {
                    tv.setText(flower.getNode_name());

                    tv = (TextView) findViewById(R.id.service_branch_address);
                    if (flower.getNode_address().equals("null") || flower.getNode_address().equals("")) {
                        tv.setText("");
                    } else {
                        tv.setText(flower.getNode_address());
                    }

                    tv = (TextView) findViewById(R.id.service_branch_city);
                    if (flower.getNode_city().equals("null") || flower.getNode_city().equals("")) {
                        tv.setText("");
                    } else {
                        tv.setText(flower.getNode_city());
                    }

                    tv = (TextView) findViewById(R.id.service_branch_email);
                    if (flower.getNode_email().equals("null") || flower.getNode_email().equals("")) {
                        tv.setText("");
                    } else {
                        tv.setText(flower.getNode_email());
                    }

                    tv = (TextView) findViewById(R.id.service_branch_mobile);
                    if (flower.getNode_contact().equals("null") || flower.getNode_contact().equals("")) {
                        tv.setText("");
                    } else {
                        tv.setText(flower.getNode_contact());
                    }
                }

                String products_extratort = flower.getProducts();
                try {
                    JSONArray ar = new JSONArray(products_extratort);
                    deviceCounts = ar.length() + "";
                    device_counts.setText(deviceCounts);
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        LinearLayout ll = (LinearLayout) findViewById(R.id.service_request_product_description);
                        TextView tv4 = new TextView(Service_Request_Deatils.this);
                        tv4.setTextAppearance(Service_Request_Deatils.this, R.style.SimpleTextviewStyle_16);
                        if (obj.getString("product").equals("null") || obj.getString("product").equals("")) {
                            tv4.setText("");
                        } else {
                            tv4.setText(obj.getString("product"));
                        }
                        ll.addView(tv4);

                        TextView tv5 = new TextView(Service_Request_Deatils.this);
                        tv5.setTextAppearance(Service_Request_Deatils.this, R.style.SimpleTextviewStyle_16);
                        if (obj.getString("type").equals("null") || obj.getString("type").equals("")) {
                            tv5.setText("");
                        } else {
                            tv5.setText(obj.getString("type"));
                        }
                        ll.addView(tv5);

                        TextView tv6 = new TextView(Service_Request_Deatils.this);
                        tv6.setTextAppearance(Service_Request_Deatils.this, R.style.SimpleTextviewStyle_16);
                        if (obj.getString("description").equals("null") || obj.getString("description").equals("")) {
                            tv6.setText("");
                        } else {
                            tv6.setText(obj.getString("description"));
                        }
                        ll.addView(tv6);

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                        lp.setMargins(5, 10, 5, 10);

                        ImageView divider4 = new ImageView(Service_Request_Deatils.this);
                        divider4.setLayoutParams(lp);
                        divider4.setBackgroundColor(Color.parseColor("#b6b6b6"));
                        ll.addView(divider4);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                }
            }
        } else {
            Toast.makeText(Service_Request_Deatils.this, "No service request asked", Toast.LENGTH_LONG).show();
        }
    }


    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Service_Request_Deatils.this, MainActivity.class);
        intent.putExtra("redirection", "Service Create");
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Service_Request_Deatils.this, MainActivity.class);
                intent.putExtra("redirection", "Service Create");
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }
}
