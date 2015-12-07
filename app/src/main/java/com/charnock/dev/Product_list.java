package com.charnock.dev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.Adaptor.Product_List_Adaptor;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Product_List_Model;
import com.charnock.dev.parsers.ProductList_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_list extends Activity implements Product_List_Adaptor.DataFromAdapterToActivity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String category_id = "";
    String category_name = "";
    String subcategory_id = "";
    String subcategory_name = "",business_id = "";

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    List<Product_List_Model> feedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progress = new ProgressDialog(Product_list.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(getResources().getString(R.string.please_wait));
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        category_id = getIntent().getExtras().getString("category_id");
        category_name = getIntent().getExtras().getString("category_name");
        subcategory_id = getIntent().getExtras().getString("subcategory_id");
        subcategory_name = getIntent().getExtras().getString("subcategory_name");
        business_id = getIntent().getExtras().getString("business_id");

        try {
            getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + subcategory_name + "</font>")));
            getActionBar().setSubtitle("Product List");
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(Product_list.this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (isonline()) {
            progress.show();
            getdata(getResources().getString(R.string.url_reference) + "home/product_list.php");
        } else {
            Toast.makeText(Product_list.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }
    }

    private void display_data() {
        progress.show();
        if (feedlist != null) {

            mAdapter = new Product_List_Adaptor(feedlist, Product_list.this);
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
                feedlist = ProductList_JSONParser.parserFeed(s);
                display_data();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Product_list.this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                Toast.makeText(Product_list.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("id",subcategory_id);
                Log.d("business_id",business_id);
                params.put("id", subcategory_id);
                params.put("business_id",business_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Product_list.this.finish();
                Product_list.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            default:
                return true;
        }
    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) Product_list.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Product_list.this.finish();
        Product_list.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void productId(String product_name, String id) {
        Intent intent = new Intent(Product_list.this, Product_Description.class);
        intent.putExtra("category_id", category_id);
        intent.putExtra("category_name", category_name);
        intent.putExtra("subcategory_id", subcategory_id);
        intent.putExtra("subcategory_name", subcategory_name);
        intent.putExtra("product_id", id);
        intent.putExtra("product_name", product_name);
        intent.putExtra("business_id", business_id);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}