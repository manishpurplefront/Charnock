package com.charnock.dev;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Product_List_Model;
import com.charnock.dev.model.Sub_Category_Model;
import com.charnock.dev.parsers.ProductList_JSONParser;
import com.charnock.dev.parsers.SubCategory_JSONParser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_list extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String category_id = "";
    String category_name = "";
    String subcategory_id = "";
    String subcategory_name = "";
    ListView listview;
    DisplayAdapter disadpt;
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

        if (isonline()) {
            progress.show();
            getdata(getResources().getString(R.string.url_reference) + "home/product_list.php");
        } else {
            Toast.makeText(Product_list.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

        listview = (ListView) findViewById(R.id.listview);


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                   String position = feedlist.get(pos).getProduct_id();
                    String name_product = feedlist.get(pos).getProduct_name();

                    Intent intent = new Intent(Product_list.this, Product_Description.class);
                    intent.putExtra("category_id",category_id);
                    intent.putExtra("category_name",category_name);
                    intent.putExtra("subcategory_id", subcategory_id);
                    intent.putExtra("subcategory_name", subcategory_name);;
                    intent.putExtra("product_name",name_product );
                    intent.putExtra("product_id",position );
                    startActivity(intent);
                    Product_list.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });
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
                Toast.makeText(Product_list.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("id",subcategory_id);
                params.put("id",subcategory_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    private void display_data() {
        disadpt = new DisplayAdapter(Product_list.this,feedlist);
        listview.setAdapter(disadpt);
    }

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) Product_list.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Product_list.this.finish();
    }

}