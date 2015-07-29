package com.charnock.dev;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Home_Model;
import com.charnock.dev.parsers.MainPage_JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeCategory extends Fragment
{
    View rootView;
    private ArrayList<String> items = new ArrayList<String>();
    LinearLayout llo;
    List<Home_Model> feedlist;
    private String tag_string_req = "string_req";
    ProgressDialog progress;
    String id,name;
    int i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_category, container, false);
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        llo = (LinearLayout) rootView.findViewById(R.id.linserid);
        progress.show();
            if (isonline()) {
                getdata(getResources().getString(R.string.url_reference) + "home/home_data.php");
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }

        getActivity().setTitle("Home");
        return rootView;
    }


    private void display_data()
    {
        progress.show();
        if(feedlist != null) {
            for (i = 0; i < feedlist.size(); i = i + 2) {
                Log.d("id1", feedlist.get(i).getId());
                Log.d("name1", feedlist.get(i).getName());
                Log.d("image1", feedlist.get(i).getImage());
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(0, 400);
                p3.weight = 1;
                p3.setMargins(0, 0, 0, 6);


                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p4.gravity = Gravity.BOTTOM;

                final LinearLayout llm = new LinearLayout(getActivity());
                llm.setLayoutParams(p3);
                llm.setId(Integer.valueOf(feedlist.get(i).getId()));
                llm.setOrientation(LinearLayout.HORIZONTAL);
                get_image(feedlist.get(i).getImage(), llm);

                TextView tv = new TextView(getActivity());
                tv.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle_22);
                tv.setText(feedlist.get(i).getName());
                tv.setBackgroundColor(Color.parseColor("#90000000"));
                tv.setLayoutParams(p4);

                llm.addView(tv);
                ll.addView(llm);

                llm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String position = String.valueOf(llm.getId());
                        String name_sub = "";
                        String business_id = "";
                        for (int j = 0; j < feedlist.size(); j++) {
                            if (position.equals(feedlist.get(j).getId())) {
                                name_sub = feedlist.get(j).getName();
                                business_id = feedlist.get(j).getUser_id();
                            }
                        }
                        Intent intent = new Intent(getActivity(), SubCategory.class);
                        intent.putExtra("id", position);
                        intent.putExtra("name", name_sub);
                        intent.putExtra("business_id",business_id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                    }
                });

                p3.setMargins(6, 0, 0, 6);
                final LinearLayout llm2 = new LinearLayout(getActivity());
                llm2.setLayoutParams(p3);
                llm2.setOrientation(LinearLayout.HORIZONTAL);


                try {
                    feedlist.get(i + 1);
                    Log.d("id", feedlist.get(i + 1).getId());
                    Log.d("name", feedlist.get(i + 1).getName());
                    Log.d("image", feedlist.get(i + 1).getImage());
                    llm2.setId(Integer.valueOf(feedlist.get(i + 1).getId()));

                    get_image(feedlist.get(i + 1).getImage(), llm2);


                    TextView tv2 = new TextView(getActivity());
                    tv2.setTextAppearance(getActivity(), R.style.SimpleTextviewStyle_22);
 //                   tv2.setTextColor(Color.parseColor("#FFFFFF"));
                    tv2.setBackgroundColor(Color.parseColor("#90000000"));
                    tv2.setLayoutParams(p4);
                    tv2.setText(feedlist.get(i + 1).getName());
                    llm2.addView(tv2);


                    llm2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String position = String.valueOf(llm2.getId());
                            String name_sub = "";
                            String business_id = "";
                            for (int j = 0; j < feedlist.size(); j++) {
                                if (position.equals(feedlist.get(j).getId())) {
                                    name_sub = feedlist.get(j).getName();
                                    business_id = feedlist.get(j).getUser_id();
                                }
                            }
                            Intent intent = new Intent(getActivity(), SubCategory.class);
                            intent.putExtra("id", position);
                            intent.putExtra("name", name_sub);
                            intent.putExtra("business_id",business_id);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
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
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }



    protected boolean isonline()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}