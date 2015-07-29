package com.charnock.dev;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.charnock.dev.parsers.MainPage_JSONParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeCategory extends Fragment
{
    View rootView;
    List<Home_Model> feedlist;
    private String tag_string_req = "string_req";
    ProgressDialog progress;
    int i;
    GridView gv;


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

        progress.show();
        if (isonline()) {
            getdata(getResources().getString(R.string.url_reference) + "home/home_data.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

        getActivity().setTitle("Home");
        return rootView;
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

    private void display_data()
    {
        progress.show();
        if(feedlist != null) {
            gv=(GridView) rootView.findViewById(R.id.gridview);
            gv.setAdapter(new ImageAdapter(getActivity(), feedlist));
//            gv.setLayoutParams(new GridView.LayoutParams (50, 50)) ;
//            gv.setId(Integer.valueOf(feedlist.get(i).getId()));

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                    String position = String.valueOf(gv.getId());
                    String name_sub = "";
                    for (int j = 0; j < feedlist.size(); j++) {
                        if (position.equals(feedlist.get(j).getId())) {
                            name_sub = feedlist.get(j).getName();
                        }
                    }
                    Intent intent = new Intent(getActivity(), SubCategory.class);
                    intent.putExtra("id", position);
                    intent.putExtra("name",name_sub);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                }
            });

//            gv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String position = String.valueOf(gv.getId());
//                    String name_sub = "";
//                    for (int j = 0; j < feedlist.size(); j++) {
//                        if (position.equals(feedlist.get(j).getId())) {
//                            name_sub = feedlist.get(j).getName();
//                        }
//                    }
//                    Intent intent = new Intent(getActivity(), SubCategory.class);
//                    intent.putExtra("id", position);
//                    intent.putExtra("name", name_sub);
//                    startActivity(intent);
//                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                }
//            });

//            for (i = 0; i < feedlist.size(); i = i + 2) {
//
//                LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                p4.gravity = Gravity.BOTTOM;
//
//                TextView tv = new TextView(getActivity());
//                tv.setTextColor(Color.parseColor("#FFFFFF"));
//                tv.setText(feedlist.get(i).getName());
//                tv.setBackgroundColor(Color.parseColor("#90000000"));
//                tv.setLayoutParams(p4);
//                gv.addView(tv);
//                gv.setId(Integer.valueOf(feedlist.get(i).getId()));
//
//            }
        }
        progress.hide();
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
