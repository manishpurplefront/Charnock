package com.charnock.dev;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.charnock.dev.model.Profile_Model;
import com.charnock.dev.model.Response_Model;
import com.charnock.dev.parsers.Profile_JSONParser;
import com.charnock.dev.parsers.Response_JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Editprofile extends Activity {

    String name = "", phone = "", address = "", address2 = "", address3 = "", city = "", pincode = "";
    EditText nameet, addresset, addresset2, addresset3, cityet, pincodeet, phoneet;
    Button b;
    ProgressDialog progress;
    List<Database> database;
    List<Profile_Model> feedlist;
    String tag_string_req_category3 = "string_req_warrenty";
    String state_id = "";
    ArrayList<String> myarray_state = new ArrayList<>();
    ArrayList<String> myarray2_state = new ArrayList<>();
    Spinner spnr_state;
    List<Response_Model> feedlist_response;
    private String TAG = Editprofile.class.getSimpleName();
    private String tag_string_req_send = "string_req_send";
    private String tag_string_req = "string_req";

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.pf);
        getActionBar().setTitle("Edit profile");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(Editprofile.this);
        progress.setCancelable(false);
        progress.setMessage("Loading...");
        progress.setTitle(R.string.please_wait);
        progress.getWindow().setGravity(Gravity.CENTER);
        progress.setIndeterminate(true);
        progress.hide();

        try {
            dbhelp.DatabaseHelper2 entry = new dbhelp.DatabaseHelper2(Editprofile.this);
            entry.close();
            database = entry.getdatabase();
        } catch (Exception e) {
            Log.d("Exception : ", "" + e);
            Log.d("exception", "user does not exist");
        }

        myarray_state.add("Select");
        myarray2_state.add("Select");

        Internet_Access ac = new Internet_Access();
        if (ac.isonline(Editprofile.this)) {
            get_data(getResources().getString(R.string.url_reference) + "home/view_profile.php");
        } else {
            Toast.makeText(Editprofile.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

        spnr_state = (Spinner) findViewById(R.id.registration_state);
        ArrayAdapter<String> adapter4_branch = new ArrayAdapter<>(Editprofile.this, android.R.layout.simple_spinner_dropdown_item, myarray_state);
        adapter4_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnr_state.setAdapter(adapter4_branch);
        spnr_state.setFocusableInTouchMode(true);

        spnr_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
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

        nameet = (EditText) findViewById(R.id.viewedit_name_edit);
        addresset = (EditText) findViewById(R.id.viewedit_address_edit);
        addresset2 = (EditText) findViewById(R.id.viewedit_address_edit2);
        addresset3 = (EditText) findViewById(R.id.viewedit_address_edit3);
        cityet = (EditText) findViewById(R.id.viewedit_city_edit);
        pincodeet = (EditText) findViewById(R.id.viewedit_city_pincode);
        phoneet = (EditText) findViewById(R.id.viewedit_phone_edit);
        b = (Button) findViewById(R.id.viewedit_update);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hideKeyboard(Editprofile.this);

                name = nameet.getText().toString();
                address = addresset.getText().toString();
                address2 = addresset2.getText().toString();
                address3 = addresset3.getText().toString();
                city = cityet.getText().toString();
                pincode = pincodeet.getText().toString();
                phone = phoneet.getText().toString();
                validationfunction();

            }
        });


        nameet.setVisibility(View.VISIBLE);
        addresset.setVisibility(View.VISIBLE);
        cityet.setVisibility(View.VISIBLE);
        pincodeet.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
    }

    private void update_display() {
        if (feedlist != null) {
            for (Profile_Model flower : feedlist) {
                nameet.setText(flower.getName());
                phoneet.setText(flower.getPhone());
                addresset.setText(flower.getAddress_line1());
                addresset2.setText(flower.getAddress_line2());
                addresset3.setText(flower.getAddress_line3());
                pincodeet.setText(flower.getPincode());
                cityet.setText(flower.getCity());
            }
        } else {
            Toast.makeText(Editprofile.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
    }


    private void get_data(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("response", s);
                feedlist = Profile_JSONParser.parserFeed(s);
                get_states();
                update_display();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("email", database.get(0).getEmail());
                params.put("business_id", database.get(0).getBusiness_id());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

    public void updatedisplay() {

        if (feedlist_response != null) {
            for (Response_Model flower : feedlist_response) {
                switch (flower.getSuccess()) {
                    case "Updated Profile":

                        dbhelp entry = new dbhelp(Editprofile.this);
                        entry.open();
                        entry.updateeuser(database.get(0).getId(), name, phone);
                        entry.close();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
                        builder.setMessage(getResources().getString(R.string.update_saved))
                                .setCancelable(false)
                                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(Editprofile.this, MainActivity.class);
                                        intent.putExtra("redirection", "Settings");
                                        finish();
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    case "Updated Failed":
                        Toast.makeText(Editprofile.this, getResources().getString(R.string.update_failed), Toast.LENGTH_LONG).show();
                        break;
                    case "Wrong Password":
                        Toast.makeText(Editprofile.this, getResources().getString(R.string.corrent_password), Toast.LENGTH_LONG).show();
                        break;
                    case "Error":
                        Toast.makeText(Editprofile.this, getResources().getString(R.string.unknownerror10), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(Editprofile.this, getResources().getString(R.string.unknownerror10), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        } else {
            Toast.makeText(Editprofile.this, getResources().getString(R.string.unknownerror7), Toast.LENGTH_LONG).show();
        }
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

                            ArrayAdapter<String> adapter3_branch = new ArrayAdapter<>(Editprofile.this, android.R.layout.simple_spinner_dropdown_item, myarray_state);
                            adapter3_branch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_state.setAdapter(adapter3_branch);

                            try {
                                if (feedlist != null) {
                                    spnr_state.setSelection(adapter3_branch.getPosition(feedlist.get(0).getState()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                        Toast.makeText(Editprofile.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
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
                                        Intent intent = new Intent(Editprofile.this, SplashScreen.class);
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

    public void validationfunction() {

        if (name.equals("") || name.isEmpty() || name.trim().isEmpty()) {
            nameet.setError(getResources().getString(R.string.correct_name));
            Toast.makeText(Editprofile.this, R.string.correct_name, Toast.LENGTH_LONG).show();
        } else if (phone.length() != 10) {
            phoneet.setError(getResources().getString(R.string.correct_limit_contact));
            Toast.makeText(Editprofile.this, R.string.correct_limit_contact, Toast.LENGTH_LONG).show();
        } else if (address.equals("") || address.isEmpty() || address.trim().isEmpty()) {
            addresset.setError(getResources().getString(R.string.correct_address_error));
            Toast.makeText(Editprofile.this, R.string.correct_address_error, Toast.LENGTH_LONG).show();
        } else if (pincode.equals("") || pincode.isEmpty() || pincode.trim().isEmpty()) {
            pincodeet.setError(getResources().getString(R.string.contact_city));
            Toast.makeText(Editprofile.this, R.string.contact_city, Toast.LENGTH_LONG).show();
        } else if (state_id.equals("")) {
            Toast.makeText(Editprofile.this, getResources().getString(R.string.contact_state), Toast.LENGTH_LONG).show();
        }
        else {
            Internet_Access ac = new Internet_Access();
            if (ac.isonline(Editprofile.this)) {
                progress.show();
                updata();
            } else {
                Toast.makeText(Editprofile.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updata() {
        StringRequest request = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_reference) + "home/edit_profile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response", s);
                feedlist_response = Response_JSONParser.parserFeed(s);
                updatedisplay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Editprofile.this, getResources().getString(R.string.no_internet_access), Toast.LENGTH_LONG).show();
                //Toast.makeText(Editprofile.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", database.get(0).getId());
                params.put("email", database.get(0).getEmail());
                params.put("name", name);
                params.put("password", database.get(0).getPassword());
                params.put("phone", phone);
                params.put("city", city);
                params.put("address1", address);
                params.put("address2", address2);
                params.put("address3", address3);
                params.put("pincode", pincode);
                params.put("state", state_id);
                params.put("business_id", database.get(0).getBusiness_id());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req);
    }

//    public static final boolean isValidPhoneNumber(CharSequence enumber) {
//        return android.util.Patterns.PHONE.matcher(enumber).matches();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(Editprofile.this, ViewProfile.class);
                intent.putExtra("redirection", "Editprofile");
                startActivity(intent);
                overridePendingTransition(R.anim.left_right, R.anim.right_left);
                return true;

            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Editprofile.this, ViewProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

}