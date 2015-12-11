package com.charnock.dev;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
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
import com.charnock.dev.model.Response_Model;
import com.charnock.dev.parsers.Response_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration extends Activity {
    EditText editTextName, editTextEmail, editTextPhone, editTextAddress, editTextAddress2, editTextAddress3, editTextCity, editPincode,
            editbusiness_name;
    Button btnCreateAccount;
    String title_id = "", state_id = "";
    String name = "", email = "", phone = "", address = "", city = "", address2 = "", address3 = "", pincode = "", business_name = "";
    String tag_string_req_registration = "string_req_registration";
    ProgressDialog progress;
    int i;
    String tag_string_req_category3 = "string_req_warrenty";

    ArrayList<String> myarray_title = new ArrayList<>();
    ArrayList<String> myarray2_title = new ArrayList<>();

    ArrayList<String> myarray_state = new ArrayList<>();
    ArrayList<String> myarray2_state = new ArrayList<>();
    Spinner spnr_title, spnr_state;
    List<Response_Model> feedlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + "Registration" + "</font>")));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);

        progress = new ProgressDialog(Registration.this);
        progress.setCancelable(false);
        progress.setMessage(getResources().getString(R.string.loading));
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        myarray_title.add("Select");
        myarray2_title.add("Select");

        myarray_state.add("Select");
        myarray2_state.add("Select");

        if (isonline()) {
            get_title();
            get_states();
        } else {
            Toast.makeText(Registration.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

        spnr_title = (Spinner) findViewById(R.id.registation_title);
        ArrayAdapter<String> adapter3_branch = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_dropdown_item, myarray_title);
        adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_title.setAdapter(adapter3_branch);
        spnr_title.setFocusableInTouchMode(true);

        spnr_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                progress.show();
                if (myarray_title.get(position).equals("Select") || myarray_title.get(position).equals("")) {
                    title_id = "";
                } else {
                    title_id = myarray2_title.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                title_id = "";
            }
        });


        spnr_state = (Spinner) findViewById(R.id.registration_state);
        ArrayAdapter<String> adapter4_branch = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_dropdown_item, myarray_state);
        adapter4_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_state.setAdapter(adapter4_branch);
        spnr_state.setFocusableInTouchMode(true);

        spnr_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                progress.show();
                if (myarray_state.get(position).equals("Select") || myarray_state.get(position).equals("")) {
                    state_id = "";
                } else {
                    state_id = myarray2_state.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                state_id = "";
            }
        });


        editTextName = (EditText) findViewById(R.id.registration_name);
        editTextEmail = (EditText) findViewById(R.id.registration_email);
        editTextPhone = (EditText) findViewById(R.id.registration_phone);
        editTextAddress = (EditText) findViewById(R.id.registration_address);
        editTextCity = (EditText) findViewById(R.id.registration_city);
        editbusiness_name = (EditText) findViewById(R.id.registration_business_name);
        editPincode = (EditText) findViewById(R.id.registration_pin_code);
        editTextAddress2 = (EditText) findViewById(R.id.registration_address_2);
        editTextAddress3 = (EditText) findViewById(R.id.registration_address_3);
        btnCreateAccount = (Button) findViewById(R.id.registation_sign_up);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                name = editTextName.getText().toString();
                email = editTextEmail.getText().toString();
                phone = editTextPhone.getText().toString();
                address = editTextAddress.getText().toString();
                city = editTextCity.getText().toString();
                address2 = editTextAddress2.getText().toString();
                address3 = editTextAddress3.getText().toString();
                pincode = editPincode.getText().toString();
                business_name = editbusiness_name.getText().toString();
                validationfunction();
            }
        });
    }

    public void validationfunction() {
//        if(name.equals("") || name.isEmpty() || name.trim().isEmpty())
//        {
//            invalid = true;
//            editTextName.setError(getResources().getString(R.string.registration_name_error1));
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_name_error1), Toast.LENGTH_LONG).show();
//        }
        if (title_id.equals("")) {
            spnr_title.setPrompt(getResources().getString(R.string.title_requered));
            Toast.makeText(Registration.this, getResources().getString(R.string.title_requered), Toast.LENGTH_LONG).show();
        } else if (address.trim().equals("")) {
            editTextAddress.setError(getResources().getString(R.string.address1_required));
            Toast.makeText(Registration.this, getResources().getString(R.string.address1_required), Toast.LENGTH_LONG).show();
        } else if (pincode.trim().equals("")) {
            editPincode.setError(getResources().getString(R.string.pincode_required));
            Toast.makeText(Registration.this, getResources().getString(R.string.pincode_required), Toast.LENGTH_LONG).show();
        } else if (state_id.equals("")) {
            spnr_state.setPrompt(getResources().getString(R.string.state_required));
            Toast.makeText(Registration.this, getResources().getString(R.string.state_required), Toast.LENGTH_LONG).show();
        } else if (phone.equals("") || phone.isEmpty() || phone.trim().isEmpty()) {
            editTextPhone.setError(getResources().getString(R.string.registration_phone_number_error));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_phone_number_error), Toast.LENGTH_LONG).show();
        } else if (phone.length() != 10) {
            editTextPhone.setError(getResources().getString(R.string.registration_phone_number_length_error));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_phone_number_length_error), Toast.LENGTH_LONG).show();
        } else if (email.equals("") || email.isEmpty() || email.trim().isEmpty()) {
            editTextEmail.setError(getResources().getString(R.string.correct_email_error));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.correct_email_error), Toast.LENGTH_LONG).show();
        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getResources().getString(R.string.correct_email2));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.correct_email2), Toast.LENGTH_LONG).show();
        } else {
            if (isonline()) {
                progress.show();
                StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/registration.php",
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String arg0) {
                                Log.d("here in sucess", arg0);

                                progress.hide();
                                feedlist = Response_JSONParser.parserFeed(arg0);

                                if (feedlist != null) {
                                    for (Response_Model flower : feedlist) {
                                        if (flower.getSuccess().equals("Error")) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                                            builder.setMessage(getResources().getString(R.string.unknownerror5))
                                                    .setCancelable(false)
                                                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                                            builder.setMessage(getResources().getString(R.string.registration_success_message))
                                                    .setCancelable(false)
                                                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Registration.this, MainActivity.class);
                                                            intent.putExtra("redirection", "Login");
                                                            finish();
                                                            startActivity(intent);
                                                            overridePendingTransition(R.anim.left_right, R.anim.right_left);
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(Registration.this, getResources().getString(R.string.unknownerror8), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError arg0) {
                                progress.hide();
                                Toast.makeText(Registration.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();

                        Log.d("name", name);
                        Log.d("email", email);
                        Log.d("phone", phone);
                        Log.d("address", address);
                        Log.d("city", city);

                        params.put("address_line1", address);
                        params.put("address_line2", address2);
                        params.put("address_line3", address3);
                        params.put("state_id", state_id);
                        params.put("city", city);
                        params.put("zip", pincode);
                        params.put("business_name", business_name);
                        params.put("email_cus", email);
                        params.put("mobile", phone);
                        params.put("salutation_id", title_id);
                        params.put("name", name);

                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(request, tag_string_req_registration);
            } else {
                Toast.makeText(Registration.this, R.string.nointernetconnection, Toast.LENGTH_LONG).show();
            }
        }
    }

    boolean isEmailValid(CharSequence cemail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(cemail).matches();
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Registration.this, MainActivity.class);
        intent.putExtra("redirection", "Login");
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Registration.this, MainActivity.class);
                intent.putExtra("redirection", "Login");
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;
            default:
                return true;
        }

    }

    public void get_title() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/satulation_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        myarray_title.clear();
                        myarray2_title.clear();

                        myarray_title.add("Select");
                        myarray2_title.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                myarray2_title.add(id);
                                String name = obj.getString("name");
                                myarray_title.add(name);
                            }

                            ArrayAdapter<String> adapter3_branch = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_dropdown_item, myarray_title);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_title.setAdapter(adapter3_branch);
                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Registration.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                        builder.setCancelable(false)
                                .setMessage(getResources().getString(R.string.nointernetaccess_messgae))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        get_title();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Registration.this, SplashScreen.class);
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }

                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }

    public void get_states() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/state_list.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progress.hide();
                        myarray_state.clear();
                        myarray2_state.clear();

                        myarray_state.add("Select");
                        myarray2_state.add("Select");
                        try {
                            JSONArray ar = new JSONArray(response);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject obj = ar.getJSONObject(i);
                                String id = obj.getString("id");
                                myarray2_state.add(id);
                                String name = obj.getString("name");
                                myarray_state.add(name);
                            }

                            ArrayAdapter<String> adapter3_branch = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_dropdown_item, myarray_state);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_state.setAdapter(adapter3_branch);
                        } catch (JSONException e) {
//                            Log.d("response",response);
//                            Log.d("error in json", "l "+ e);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        progress.hide();
//                        Log.d("error", "" + arg0.getMessage());
                        Toast.makeText(Registration.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                        builder.setCancelable(false)
                                .setMessage(getResources().getString(R.string.nointernetaccess_messgae))
                                .setNegativeButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        get_states();
                                    }
                                })
                                .setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Registration.this, SplashScreen.class);
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    }

                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
        AppController.getInstance().addToRequestQueue(request, tag_string_req_category3);
    }
}