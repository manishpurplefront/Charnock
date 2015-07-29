package com.charnock.dev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Product_List_Model;
import com.charnock.dev.parsers.ProductList_JSONParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_list extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String category_id = "";
    String category_name = "";
    String subcategory_id = "";
    String subcategory_name = "",business_id = "";
    LinearLayout llo;
    private String tag_string_req = "string_req";
    int i;

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
        business_id = getIntent().getExtras().getString("business_id");

        getActionBar().setTitle(subcategory_name);
        getActionBar().setSubtitle("ProductList");

        llo = (LinearLayout) findViewById(R.id.linserid_list);

        if (isonline()) {
            progress.show();
            getdata(getResources().getString(R.string.url_reference) + "home/product_list.php");
        } else {
            Toast.makeText(Product_list.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }
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
                Log.d("business_id",business_id);
                params.put("id", subcategory_id);
                params.put("business_id",business_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    private void display_data() {
        progress.show();
        if(feedlist != null) {
            for (i = 0; i < feedlist.size(); i = i + 2) {
                LinearLayout ll = new LinearLayout(Product_list.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(0, 400);
                p3.weight = 1;
                p3.setMargins(0, 0, 0, 6);

                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p4.gravity = Gravity.BOTTOM;

                final LinearLayout llm = new LinearLayout(Product_list.this);
                llm.setLayoutParams(p3);
                llm.setId(Integer.valueOf(feedlist.get(i).getProduct_id()));
                llm.setOrientation(LinearLayout.HORIZONTAL);
                get_image(feedlist.get(i).getProduct_image(),llm);

                TextView tv = new TextView(Product_list.this);
                tv.setTextAppearance(Product_list.this, R.style.SimpleTextviewStyle_22);
                tv.setText(feedlist.get(i).getProduct_name());
                tv.setBackgroundColor(Color.parseColor("#90000000"));
                tv.setLayoutParams(p4);

                llm.addView(tv);
                ll.addView(llm);


                llm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String position = String.valueOf(llm.getId());
                        String name_sub = "";
                        for (int j = 0; j < feedlist.size(); j++) {
                            if (position.equals(feedlist.get(j).getProduct_id())) {
                                name_sub = feedlist.get(j).getProduct_name();
                            }
                        }

                        Intent intent = new Intent(Product_list.this, Product_Description.class);
                        intent.putExtra("category_id",category_id);
                        intent.putExtra("category_name",category_name);
                        intent.putExtra("subcategory_id", subcategory_id);
                        intent.putExtra("subcategory_name", subcategory_name);;
                        intent.putExtra("product_name",name_sub);
                        intent.putExtra("product_id",position);
                        intent.putExtra("business_id",business_id);
                        startActivity(intent);
                        Product_list.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });

                p3.setMargins(6, 0, 0, 6);
                final LinearLayout llm2 = new LinearLayout(Product_list.this);
                llm2.setLayoutParams(p3);
                llm2.setOrientation(LinearLayout.HORIZONTAL);

                try {
                    feedlist.get(i + 1);
                    Log.d("id", feedlist.get(i + 1).getProduct_id());
                    Log.d("name", feedlist.get(i + 1).getProduct_name());
                    Log.d("image", feedlist.get(i + 1).getProduct_image());
                    llm2.setId(Integer.valueOf(feedlist.get(i + 1).getProduct_id()));

                    get_image(feedlist.get(i+1).getProduct_image(),llm2);

                    TextView tv2 = new TextView(Product_list.this);
                    tv2.setTextAppearance(Product_list.this, R.style.SimpleTextviewStyle_22);
                    tv2.setBackgroundColor(Color.parseColor("#90000000"));
                    tv2.setLayoutParams(p4);
                    tv2.setText(feedlist.get(i + 1).getProduct_name());
                    llm2.addView(tv2);

                    llm2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String position = String.valueOf(llm2.getId());
                            String name_sub = "";
                            for (int j = 0; j < feedlist.size(); j++) {
                                if (position.equals(feedlist.get(j).getProduct_id())) {
                                    name_sub = feedlist.get(j).getProduct_name();
                                }
                            }
                            Intent intent = new Intent(Product_list.this, Product_Description.class);
                            intent.putExtra("category_id",category_id);
                            intent.putExtra("category_name",category_name);
                            intent.putExtra("subcategory_id", subcategory_id);
                            intent.putExtra("subcategory_name", subcategory_name);;
                            intent.putExtra("product_name",name_sub );
                            intent.putExtra("product_id",position );
                            intent.putExtra("business_id",business_id);
                            startActivity(intent);
                            Product_list.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ll.addView(llm2);
                llo.addView(ll);
            }
        }
        progress.hide();
    }

    private void get_image(String url, final LinearLayout llm) {
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                llm.setBackgroundDrawable(d);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        AppController.getInstance().addToRequestQueue(imageRequest, tag_string_req);
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