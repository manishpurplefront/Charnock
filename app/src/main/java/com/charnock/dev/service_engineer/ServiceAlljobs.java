package com.charnock.dev.service_engineer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.Adaptor.InflatDateJobs;
import com.charnock.dev.Internet_Access;
import com.charnock.dev.R;
import com.charnock.dev.StaticVariables;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.dbhelp;
import com.charnock.dev.model.Database;
import com.charnock.dev.model.Job_Sheet_Details_Model;
import com.charnock.dev.parsers.Job_Sheet_Details_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceAlljobs extends Activity {

    List<Job_Sheet_Details_Model> feedlist;
    ProgressDialog progress;
    List<Database> database;
    ListView listview;
    private String tag_string_req_recieve = "string_req_recieve";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_alljobs);

        getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + getResources().getString(R.string.title_activity_service_job_details) + "</font>")));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);

        progress = new ProgressDialog(ServiceAlljobs.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        dbhelp.DatabaseHelper2 datab = new dbhelp.DatabaseHelper2(ServiceAlljobs.this);
        database = datab.getdatabase();

        listview = (ListView) findViewById(R.id.listView2);

        Log.d("params", database.get(0).getId());
        Log.d("service_id", StaticVariables.service_id);

        Internet_Access ac = new Internet_Access();
        if (ac.isonline(ServiceAlljobs.this)) {
            progress.show();
            getDateList();
        } else {
            Toast.makeText(ServiceAlljobs.this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }


    }

    protected void display_data() {
        if (feedlist != null) {
            for (Job_Sheet_Details_Model flower : feedlist) {
                if (flower.getId().equals("No Data")) {
                    Toast.makeText(ServiceAlljobs.this, "No job sheet created yet", Toast.LENGTH_LONG).show();
                } else if (flower.getId().equals("Error")) {
                    Toast.makeText(ServiceAlljobs.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
                } else {
                    listview.setAdapter(new InflatDateJobs(ServiceAlljobs.this, feedlist));
                }
            }
        } else {
            Toast.makeText(ServiceAlljobs.this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
        }
    }

    public void getDateList() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/service_request_all_job.php",
                new Response.Listener<String>()

                {
                    @Override
                    public void onResponse(String response) {
                        progress.hide();

                        feedlist = Job_Sheet_Details_JSONParser.parserFeed(response);
                        display_data();

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {

                        progress.hide();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("business_id", database.get(0).getBusiness_id());
                params.put("email", database.get(0).getEmail());
                params.put("node_id", database.get(0).getNode_id() + "");
                params.put("role_id", database.get(0).getRole_id() + "");
                params.put("profile_id", database.get(0).getProfile_id());
                params.put("service_id", "1");

                Log.d("params", database.get(0).getId());
                Log.d("service_id", StaticVariables.service_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve);
    }
}