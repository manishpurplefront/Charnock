package com.charnock.dev;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Home_Model;
import com.charnock.dev.model.Product_List_Model;
import com.charnock.dev.model.Sub_Category_Model;
import com.charnock.dev.parsers.MainPage_JSONParser;
import com.charnock.dev.parsers.SubCategory_JSONParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SubCategory extends Activity {
    String id = "",name = "";
    Intent intent;
    GridView gridview_sub_category;

    List<Sub_Category_Model> feedlist;
    private String tag_string_req = "string_req";
    ProgressDialog progress;

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
        SubCategory.this.setTitle(name);

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
            gridview_sub_category=(GridView) findViewById(R.id.gridview_sub_category);
            gridview_sub_category.setAdapter(new Sub_Category_Adapter(SubCategory.this,feedlist));

//            gridview_sub_category.setId(Integer.valueOf(feedlist.get(i).getId()));

            gridview_sub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                    String position = String.valueOf(gridview_sub_category.getId());
                    String name_sub = "";
                    for (int j = 0; j < feedlist.size(); j++) {
                        if (position.equals(feedlist.get(j).getId())) {
                            name_sub = feedlist.get(j).getName();
                        }
                    }
                    Intent intent = new Intent(SubCategory.this, Product_list.class);
                    intent.putExtra("category_id",id);
                    intent.putExtra("category_name",name);
                    intent.putExtra("subcategory_id", position);
                    intent.putExtra("subcategory_name", name_sub);;
                    startActivity(intent);
                    SubCategory.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });

//            for (i = 0; i < feedlist.size(); i = i + 2) {
//                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                p4.gravity = Gravity.BOTTOM;
//                TextView tv = new TextView(SubCategory.this);
//                tv.setTextColor(Color.parseColor("#FFFFFF"));
//                tv.setText(feedlist.get(i).getName());
//                tv.setBackgroundColor(Color.parseColor("#90000000"));
//                tv.setLayoutParams(p4);
//                gridview_sub_category.addView(tv);
//                gridview_sub_category.setId(Integer.valueOf(feedlist.get(i).getId()));
//                gridview_sub_category.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String position = String.valueOf(gridview_sub_category.getId());
//                        String name_sub = "";
//                        for (int j = 0; j < feedlist.size(); j++) {
//                            if (position.equals(feedlist.get(j).getId())) {
//                                name_sub = feedlist.get(j).getName();
//                            }
//                        }
//                        Intent intent = new Intent(SubCategory.this, Product_list.class);
//                        intent.putExtra("category_id",id);
//                        intent.putExtra("category_name",name);
//                        intent.putExtra("subcategory_id", position);
//                        intent.putExtra("subcategory_name", name_sub);
//                        startActivity(intent);
//                        SubCategory.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
//                    }
//                });
//            }
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

    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) SubCategory.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    public void onBackPressed() {
       SubCategory.this.finish();
    }
}
