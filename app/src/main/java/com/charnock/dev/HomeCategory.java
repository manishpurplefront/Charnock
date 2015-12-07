package com.charnock.dev;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Database;
import com.charnock.dev.model.Home_Model;
import com.charnock.dev.parsers.MainPage_JSONParser;

import java.util.List;

public class HomeCategory extends Fragment
{
    View rootView;
    List<Home_Model> feedlist;
    String tag_string_req = "string_req";
    ProgressDialog progress;
    String id,name;
    int i;
    Intent intent;
    List<Database> database;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_category, container, false);
        setHasOptionsMenu(true);

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            getActivity().getActionBar().removeAllTabs();
            getActivity().invalidateOptionsMenu();
            getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            getActivity().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + getString(R.string.app_name_home) + "</font>")));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(getActivity());
            database = db.getdatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

            if (isonline()) {
                progress.show();
                getdata(getResources().getString(R.string.url_reference) + "home/home_data.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        return rootView;
    }

    private void display_data()
    {
        progress.show();
        if (feedlist != null) {

            mAdapter = new GridAdapter(feedlist, getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
        progress.hide();
    }

    private void getdata(String url) {
        StringRequest request = new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response", s);
                feedlist = MainPage_JSONParser.parserFeed(s);
                display_data();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }
}