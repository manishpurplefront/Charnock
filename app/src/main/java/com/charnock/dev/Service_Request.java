package com.charnock.dev;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Database;
import com.charnock.dev.model.Request_Information_Model;
import com.charnock.dev.parsers.Request_Information_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Service_Request extends Fragment {

    ImageButton create_service;
    ProgressDialog progress;
    List<Request_Information_Model> feedlist;
    int i;
    String id;
    List<Database> database;
    RVAdapter mAdapter;
    View rootView;
    private String tag_string_req = "string_req";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.service_request, container, false);
        setHasOptionsMenu(true);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        create_service = (ImageButton) rootView.findViewById(R.id.btn_create_service);
        create_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Service_Create2.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        try {
            getActivity().getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Service Request" + "</font>")));
            getActivity().getActionBar().setHomeButtonEnabled(true);
//            getActivity().getActionBar().setIcon(R.drawable.pf);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(getActivity());
        database = db.getdatabase();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setClickable(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //progress.show();
        if (isonline()) {
            getdata(getResources().getString(R.string.url_reference) + "home/service_data_request.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    private void getdata(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response", s);
                feedlist = Request_Information_JSONParser.parserFeed(s);
                display_data();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("business_id", database.get(0).getBusiness_id() + "");
                params.put("email", database.get(0).getEmail());
                params.put("profile_id", database.get(0).getProfile_id() + "");
                params.put("role_id", database.get(0).getRole_id() + "");
                params.put("node_id", database.get(0).getNode_id() + "");
                params.put("get_data", "0");
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    private void display_data() {
        if (feedlist != null) {

            mAdapter = new RVAdapter(feedlist, database.get(0).getName());
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.SetOnItemClickListener(new RVAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
            });

            for (Request_Information_Model flower : feedlist) {
                Log.d("id", flower.getId());
                Log.d("site_name", flower.getSite_Name());
                Log.d("date", flower.getDate());
            }
        } else {
            Toast.makeText(getActivity(), "No service request asked", Toast.LENGTH_LONG).show();
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }
    }
}

