package com.charnock.dev;

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Home_Model;
import com.charnock.dev.parsers.MainPage_JSONParser;

import java.io.ByteArrayInputStream;
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
                Log.d("id1",feedlist.get(i).getId());
                Log.d("name1",feedlist.get(i).getName());
                Log.d("image1", feedlist.get(i).getImage());
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 400);
                p3.weight = 1;
                try {
                    feedlist.get(i + 1);
                    p3.setMargins(0, 0, 0, 6);
                } catch(Exception e) {
                    p3.setMargins(0, 0, 0, 0);
                }
                LinearLayout.LayoutParams p3_down = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 400);
                p3_down.weight = 1;
                p3_down.setMargins(6,0,0,6);

                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                p4.gravity = Gravity.BOTTOM;

                final LinearLayout llm = new LinearLayout(getActivity());
                llm.setLayoutParams(p3);
                llm.setId(Integer.valueOf(feedlist.get(i).getId()));
                llm.setOrientation(LinearLayout.HORIZONTAL);
                llm.setBackgroundResource(R.drawable.pro);

                TextView tv = new TextView(getActivity());
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setText(feedlist.get(i).getName());
                tv.setBackgroundColor(Color.parseColor("#90000000"));
                tv.setLayoutParams(p4);

                byte[] imageByteArray = Base64.decode(feedlist.get(i).getImage(),Base64.DEFAULT);

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
//

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
                        Intent intent = new Intent(getActivity(), SubCategory.class);
                        intent.putExtra("id", position);
                        intent.putExtra("name", name_sub);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                });

                try {
                    feedlist.get(i + 1);
                    Log.d("id",feedlist.get(i + 1).getId());
                    Log.d("name",feedlist.get(i + 1).getName());
                    Log.d("image",feedlist.get(i + 1).getImage());
                    final LinearLayout llm2 = new LinearLayout(getActivity());
                    llm2.setLayoutParams(p3_down);
                    llm2.setOrientation(LinearLayout.HORIZONTAL);
                    llm2.setId(Integer.valueOf(feedlist.get(i+1).getId()));


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


                    TextView tv2 = new TextView(getActivity());
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
                            Intent intent = new Intent(getActivity(), SubCategory.class);
                            intent.putExtra("id", position);
                            intent.putExtra("name", name_sub);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                Toast.makeText(getActivity(), "Menu", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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