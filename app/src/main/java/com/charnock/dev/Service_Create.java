package com.charnock.dev;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Database;
import com.charnock.dev.model.Request_Information_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service_Create extends Activity {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String product_id = "", category_id = "", subcategory_id = "";
    String type_id = "";
    List<Request_Information_Model> feedslist;
    EditText et_description;
    String description;
    List<Database> database;

    ArrayList<String> myarray_category = new ArrayList<>();
    ArrayList<String> myarray2_category = new ArrayList<>();

    ArrayList<String> myarray_subcategory = new ArrayList<>();
    ArrayList<String> myarray2_subcategory = new ArrayList<>();

    ArrayList<String> myarray_product = new ArrayList<>();
    ArrayList<String> myarray2_product = new ArrayList<>();

    ArrayList<String> myarray_warrenty = new ArrayList<>();
    ArrayList<String> myarray2_warrenty = new ArrayList<>();

    String tag_string_req_category = "string_req_product";
    String tag_string_req_category2 = "string_req_branch";
    String tag_string_req_category3 = "string_req_warrenty";
    String tag_string_req_category4 = "string_req_serviceRequest";

    Spinner spnr_product, spnr_warrenty;
    Spinner spnr_category, spnr_subcategory;
    String role_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_create);

        try {
            getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Service Creation" + "</font>")));
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        progress = new ProgressDialog(Service_Create.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();
        dbhelp.DatabaseHelper2 datab = new dbhelp.DatabaseHelper2(Service_Create.this);
        database = datab.getdatabase();
        if (database != null) {
            for (final Database flower : database) {
                role_id = flower.getRole_id();
            }
        }
        myarray_category.add("Select");
        myarray2_category.add("Select");

        myarray_subcategory.add("Select");
        myarray2_subcategory.add("Select");

        myarray_product.add("Select");
        myarray2_product.add("Select");

        myarray_warrenty.add("Select");
        myarray2_warrenty.add("Select");

        progress.show();

        spinner_category();
        spnr_category = (Spinner) findViewById(R.id.service_category_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_category);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_category.setAdapter(adapter3);
        spnr_category.setFocusableInTouchMode(true);

        spnr_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (myarray_category.get(position).equals("Select") || myarray2_category.get(position).equals("")) {
                    category_id = "";
                    spnr_product.setSelection(0);
                    product_id = "";
                    spnr_subcategory.setSelection(0);
                    subcategory_id = "";
                } else {
                    category_id = myarray2_category.get(position);
                    spnr_product.setSelection(0);
                    product_id = "";
                    spnr_subcategory.setSelection(0);
                    subcategory_id = "";
                    spinner_subcategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category_id = "";
                subcategory_id = "";
                product_id = "";
            }
        });

        spnr_subcategory = (Spinner) findViewById(R.id.service_subcategory_spinner);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_subcategory);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_subcategory.setAdapter(adapter4);
        spnr_subcategory.setFocusableInTouchMode(true);

        spnr_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (myarray_subcategory.get(position).equals("Select") || myarray_subcategory.get(position).equals("")) {
                    subcategory_id = "";
                    product_id = "";
                    spnr_subcategory.setSelection(0);
                    spnr_product.setSelection(0);
                } else {
                    subcategory_id = myarray2_subcategory.get(position);
                    spnr_product.setSelection(0);
                    product_id = "";
                    spinner_product();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subcategory_id = "";
                product_id = "";
                spnr_subcategory.setSelection(0);
                spnr_product.setSelection(0);
            }
        });


        spnr_product = (Spinner) findViewById(R.id.service_product_spinner);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_product);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_product.setAdapter(adapter5);
        spnr_product.setFocusableInTouchMode(true);

        spnr_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (myarray_product.get(position).equals("Select") || myarray_product.get(position).equals("")) {
                    product_id = "";
                    spnr_product.setSelection(0);
                } else {
                    product_id = myarray2_product.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                product_id = "";
                spnr_product.setSelection(0);
            }
        });

        progress.show();

        spinnerfun_type();
        spnr_warrenty = (Spinner) findViewById(R.id.service_warranty_spinner);
        ArrayAdapter<String> adapter3_role = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_warrenty);
        adapter3_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_warrenty.setAdapter(adapter3_role);
        spnr_warrenty.setFocusableInTouchMode(true);

        spnr_warrenty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (myarray_warrenty.get(position).equals("Select") || myarray_warrenty.get(position).equals("")) {
                    type_id = "";
                } else {
                    type_id = myarray2_warrenty.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type_id = "";
            }
        });

        Button b = (Button) findViewById(R.id.service_submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_description = (EditText) findViewById(R.id.service_desc);
                description = et_description.getText().toString();
                if (isonline()) {

                    if (category_id.equals("")) {
                        Toast.makeText(Service_Create.this, getResources().getString(R.string.category_required), Toast.LENGTH_LONG).show();
                    } else if (subcategory_id.equals("")) {
                        Toast.makeText(Service_Create.this, getResources().getString(R.string.subcategory_required), Toast.LENGTH_LONG).show();
                    } else if (product_id.equals("")) {
                        Toast.makeText(Service_Create.this, getResources().getString(R.string.product_required), Toast.LENGTH_LONG).show();
                    } else if (type_id.equals("")) {
                        Toast.makeText(Service_Create.this, getResources().getString(R.string.warrenty_required), Toast.LENGTH_LONG).show();
                    } else if (description.trim().equals("")) {
                        Toast.makeText(Service_Create.this, getResources().getString(R.string.problem_details_required), Toast.LENGTH_LONG).show();
                    } else {
                        progress.show();
                        updateonlinedata(getResources().getString(R.string.url_reference) + "home/service_request_submit.php");
                    }
                } else {
                    Toast.makeText(Service_Create.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_service__create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void spinner_category() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/category_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        myarray_category.clear();
                        myarray2_category.clear();

                        myarray_category.add("Select");
                        myarray2_category.add("Select");

                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String product = obj.getString("name");

                                myarray_category.add(product);
                                myarray2_category.add(id);
                                Log.d("res", product);
                            }
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_category);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_category.setAdapter(adapter3);

                        } catch (JSONException e) {
                            Log.d("response", response);
                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Service_Create.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service_Create.this);
                        builder.setCancelable(false)
                                .setMessage(getResources().getString(R.string.nointernetaccess_messgae))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spinner_category();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Service_Create.this, SplashScreen.class);
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }

                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("business_id", database.get(0).getBusiness_id());
                params.put("email", database.get(0).getEmail());
                params.put("profile_id", database.get(0).getProfile_id());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }

    public void spinner_subcategory() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/subcategory_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        myarray_subcategory.clear();
                        myarray2_subcategory.clear();

                        myarray_subcategory.add("Select");
                        myarray2_subcategory.add("Select");

                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String product = obj.getString("name");

                                myarray_subcategory.add(product);
                                myarray2_subcategory.add(id);
                                Log.d("res", product);
                            }
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_subcategory);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_subcategory.setAdapter(adapter3);

                        } catch (JSONException e) {
                            Log.d("response", response);
                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Service_Create.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service_Create.this);
                        builder.setCancelable(false)
                                .setMessage(getResources().getString(R.string.nointernetaccess_messgae))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spinner_subcategory();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Service_Create.this, SplashScreen.class);
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }

                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("business_id", database.get(0).getBusiness_id());
                params.put("email", database.get(0).getEmail());
                params.put("profile_id", database.get(0).getProfile_id());
                params.put("category_id", category_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }


    public void spinner_product() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/product_data_request.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        Log.d("response", response);
                        myarray_product.clear();
                        myarray2_product.clear();

                        myarray_product.add("Select");
                        myarray2_product.add("Select");

                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String product = obj.getString("name");

                                myarray_product.add(product);
                                myarray2_product.add(id);
                                Log.d("res", product);
                            }
                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_product);
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_product.setAdapter(adapter3);

                        } catch (JSONException e) {

                            Log.d("error in json", "l " + e);

                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Service_Create.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service_Create.this);
                        builder.setCancelable(false)
                                .setMessage(getResources().getString(R.string.nointernetaccess_messgae))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spinner_product();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Service_Create.this, SplashScreen.class);
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }

                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("email", database.get(0).getEmail());
                params.put("business_id", database.get(0).getBusiness_id());
                params.put("profile_id", database.get(0).getProfile_id());
                params.put("subcategory_id", subcategory_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category);
    }

    public void spinnerfun_type() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/type_data.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        Log.d("response", response);
                        progress.hide();
                        myarray_warrenty.clear();
                        myarray2_warrenty.clear();

                        myarray_warrenty.add("Select");
                        myarray2_warrenty.add("Select");

                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                String type = obj.getString("type");
                                myarray_warrenty.add(type);
                                myarray2_warrenty.add(id);
                                Log.d("res", type);
                            }
                            ArrayAdapter<String> adapter3_role = new ArrayAdapter<>(Service_Create.this, android.R.layout.simple_spinner_dropdown_item, myarray_warrenty);
                            adapter3_role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_warrenty.setAdapter(adapter3_role);

                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);
                        } catch (Exception e) {
//                            Log.d("json connection", "No internet access" + e);
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Service_Create.this, getResources().getString(R.string.nointernetaccess), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service_Create.this);
                        builder.setCancelable(false)
                                .setMessage(getResources().getString(R.string.nointernetaccess_messgae))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spinnerfun_type();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Service_Create.this, SplashScreen.class);
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }

                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("business_id", database.get(0).getBusiness_id());
                params.put("email", database.get(0).getEmail());
                params.put("profile_id", database.get(0).getProfile_id());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category2);
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    void updateonlinedata(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        progress.hide();
                        Log.d("response", arg0);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Service_Create.this);
                        builder.setMessage("Thank you for contacting us we will get back to you soon")
                                .setCancelable(false)
                                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Service_Create.this, MainActivity.class);
                                        intent.putExtra("redirection", "Service Create");
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                },


                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
                        Toast.makeText(Service_Create.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        Toast.makeText(Service_Create.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Log.d("id", database.get(0).getId());
                Log.d("email", database.get(0).getEmail());
                Log.d("business_id", database.get(0).getBusiness_id());
                Log.d("profile_id", database.get(0).getProfile_id());
                Log.d("node_id", database.get(0).getNode_id());
                Log.d("category_id", category_id);
                Log.d("subcategory_id", subcategory_id);
                Log.d("product_id", product_id);
                Log.d("warrenty_id", type_id);
                Log.d("problem", description);

                params.put("id", database.get(0).getId());
                params.put("email", database.get(0).getEmail());
                params.put("business_id", database.get(0).getBusiness_id());
                params.put("profile_id", database.get(0).getProfile_id());
                params.put("node_id", database.get(0).getNode_id());
                params.put("category_id", category_id);
                params.put("subcategory_id", subcategory_id);
                params.put("product_id", product_id);
                params.put("warrenty_id", type_id);
                params.put("problem", description);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category4);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Service_Create.this, MainActivity.class);
                intent.putExtra("redirection", "Service Create");
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            default:
                return true;
        }
    }

    public void onBackPressed() {

        Intent intent = new Intent(Service_Create.this, MainActivity.class);
        intent.putExtra("redirection", "Service Create");
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
