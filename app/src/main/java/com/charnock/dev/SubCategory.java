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
    private ArrayList<String> items = new ArrayList<String>();
    String id = "",name = "";
    Intent intent;
    LinearLayout llo;
    private String tag_string_req = "string_req";
    ProgressDialog progress;
    int i;
    List<Sub_Category_Model> feedlist;

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

        llo = (LinearLayout) findViewById(R.id.linserid_sub);
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
            for (i = 0; i < feedlist.size(); i = i + 2) {
                LinearLayout ll = new LinearLayout(SubCategory.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 400);
                p3.weight = 1;

                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p4.gravity = Gravity.BOTTOM;

                final LinearLayout llm = new LinearLayout(SubCategory.this);
                llm.setLayoutParams(p3);
                llm.setId(Integer.valueOf(feedlist.get(i).getId()));
                llm.setOrientation(LinearLayout.HORIZONTAL);
                llm.setBackgroundResource(R.drawable.pro);

                TextView tv = new TextView(SubCategory.this);
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setText(feedlist.get(i).getName());
                tv.setBackgroundColor(Color.parseColor("#90000000"));
                tv.setLayoutParams(p4);

                byte[] imageByteArray = Base64.decode(feedlist.get(i).getImage(), Base64.DEFAULT);

                if (imageByteArray != null) {
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(
                            imageByteArray);
                    Bitmap company_logo = BitmapFactory
                            .decodeStream(imageStream);
                    llm.setBackgroundDrawable(null);
                    llm.setBackgroundDrawable(new BitmapDrawable(
                            getResources(), company_logo));
                }

                llm.addView(tv);
                ll.addView(llm);

//                id = feedlist.get(i).getId();
//                name = feedlist.get(i).getName();


                llm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String position = String.valueOf(llm.getId());
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

                try {
                    feedlist.get(i + 1);
                    Log.d("id",feedlist.get(i + 1).getId());
                    Log.d("name",feedlist.get(i + 1).getName());
                    Log.d("image", feedlist.get(i + 1).getImage());

                    final LinearLayout llm2 = new LinearLayout(SubCategory.this);
                    llm2.setLayoutParams(p3);
                    llm2.setOrientation(LinearLayout.HORIZONTAL);
                    llm2.setId(Integer.valueOf(feedlist.get(i + 1).getId()));

                    imageByteArray = Base64.decode(feedlist.get(i + 1).getImage(),Base64.DEFAULT);
                    llm2.setBackgroundResource(R.drawable.pro);
                    if (imageByteArray != null) {
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(
                                imageByteArray);
                        Bitmap company_logo = BitmapFactory
                                .decodeStream(imageStream);
                        llm2.setBackgroundDrawable(null);
                        llm2.setBackgroundDrawable(new BitmapDrawable(
                                getResources(), company_logo));
                    }


                    TextView tv2 = new TextView(SubCategory.this);
                    tv2.setTextColor(Color.parseColor("#FFFFFF"));
                    tv2.setBackgroundColor(Color.parseColor("#90000000"));
                    tv2.setLayoutParams(p4);
                    tv2.setText(feedlist.get(i + 1).getName());
                    llm2.addView(tv2);

                    ll.addView(llm2);
                    llm2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String position = String.valueOf(llm2.getId());
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
                llo.addView(ll);
            }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sub_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
