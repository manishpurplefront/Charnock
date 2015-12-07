package com.charnock.dev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.charnock.dev.Adaptor.Sub_Category_Adaptor;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Sub_Category_Model;
import com.charnock.dev.parsers.SubCategory_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SubCategory extends Activity implements Sub_Category_Adaptor.DataFromAdapterToActivity {

    String id = "",name = "",business_id = "";
    List<Sub_Category_Model> feedlist;
    Intent intent;
    ProgressDialog progress;
    int i;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category);

        progress = new ProgressDialog(SubCategory.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        business_id = intent.getStringExtra("business_id");

        getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + name + "</font>")));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setSubtitle("Sub Category");
        getActionBar().setIcon(R.drawable.pf);

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(SubCategory.this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        progress.show();
        if (isonline()) {
            getdata(getResources().getString(R.string.url_reference) + "home/home_subcategory.php");
        } else {
            Toast.makeText(SubCategory.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }
    }

    private void display_data()
    {
        progress.show();
        if(feedlist != null) {

            mAdapter = new Sub_Category_Adaptor(feedlist, SubCategory.this);
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
                feedlist = SubCategory_JSONParser.parserFeed(s);
                display_data();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(SubCategory.this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                Toast.makeText(SubCategory.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                SubCategory.this.finish();
//                SubCategory.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                Intent intent = new Intent(SubCategory.this, MainActivity.class);
                SubCategory.this.finish();
                this.finish();
                startActivity(intent);
                SubCategory.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        SubCategory.this.finish();
//        SubCategory.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        Intent intent = new Intent(SubCategory.this, MainActivity.class);
        SubCategory.this.finish();
        this.finish();
        startActivity(intent);
        SubCategory.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) SubCategory.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public void subCategoryId(String subCategory_name, String sub_id) {
        Log.d(subCategory_name, sub_id);
        Intent intent = new Intent(SubCategory.this, Product_list.class);
        intent.putExtra("category_id", id);
        intent.putExtra("category_name", name);
        intent.putExtra("subcategory_id", sub_id);
        intent.putExtra("subcategory_name", subCategory_name);
        intent.putExtra("business_id", "1");
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
