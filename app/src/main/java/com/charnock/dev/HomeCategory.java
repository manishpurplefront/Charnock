package com.charnock.dev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
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

public class HomeCategory extends Activity
{
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_category);

        progress = new ProgressDialog(HomeCategory.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Charnock" + "</font>")));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setSubtitle("Category");
        getActionBar().setIcon(R.drawable.pf);

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(HomeCategory.this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        try {
            dbhelp.DatabaseHelper2 db = new dbhelp.DatabaseHelper2(HomeCategory.this);
            database = db.getdatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Internet_Access ac = new Internet_Access();
        if (ac.isonline(HomeCategory.this)) {
            progress.show();
            getdata(getResources().getString(R.string.url_reference) + "home/home_data.php");
        } else {
            Toast.makeText(HomeCategory.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }
    }

    private void display_data()
    {
        progress.show();
        if (feedlist != null) {

            mAdapter = new GridAdapter(feedlist, HomeCategory.this);
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
                Toast.makeText(HomeCategory.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                SubCategory.this.finish();
//                SubCategory.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                Intent intent = new Intent(HomeCategory.this, MainActivity.class);
                HomeCategory.this.finish();
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HomeCategory.this, MainActivity.class);
        HomeCategory.this.finish();
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}