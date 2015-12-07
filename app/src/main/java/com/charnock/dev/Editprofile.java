package com.charnock.dev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Editprofile extends Activity {

    String name = "", email = "", address = "", city = "", pincode = "";
    String id;
    EditText nameet, emailet, addresset, cityet, pincodeet;
    Button b;
    ProgressDialog progress;
    private String TAG = Editprofile.class.getSimpleName();
    private String tag_string_req_send = "string_req_send";

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

        nameet = (EditText) findViewById(R.id.viewedit_name_edit);
        emailet = (EditText) findViewById(R.id.viewedit_email_edit);
        addresset = (EditText) findViewById(R.id.viewedit_address_edit);
        cityet = (EditText) findViewById(R.id.viewedit_city_edit);
        pincodeet = (EditText) findViewById(R.id.viewedit_pincode_edit);

        b = (Button) findViewById(R.id.viewedit_update);

        try {
            SQLiteDatabase db2 = openOrCreateDatabase("Charnock", MODE_PRIVATE, null);
            Cursor c = db2.rawQuery("SELECT * FROM business_master", null);
            c.moveToFirst();
            id = c.getString(c.getColumnIndex("id"));
            name = c.getString(c.getColumnIndex("name"));
            email = c.getString(c.getColumnIndex("email"));
            address = c.getString(c.getColumnIndex("address"));
            city = c.getString(c.getColumnIndex("city"));
            pincode = c.getString(c.getColumnIndex("pincode"));

            db2.close();
        } catch (Exception e) {
            //           Log.d("Exception : ", "" + e);
            //          Log.d("exception", "user does not exist");
        }

        nameet.setText(name);
        emailet.setText(email);
        addresset.setText(address);
        cityet.setText(city);
        pincodeet.setText(pincode);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(Editprofile.this);
//                builder.setTitle(R.string.dialog_password_title);
//                builder.setMessage(R.string.dialog_password_message);
//                LinearLayout ll=new LinearLayout(Editprofile.this);
//                ll.setOrientation(LinearLayout.VERTICAL);
//                final EditText et=new EditText(Editprofile.this);
//                et.setHint(R.string.dialog_password_hint);
//                et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                et.setTransformationMethod(new PasswordTransformationMethod());
//                ll.addView(et);
//                builder.setView(ll);
//                builder.setPositiveButton(R.string.dialog_password_button, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        String pass = et.getText().toString();
//                        String word2 ="";
//                        try {
//                            MCrypt mcrypt = new MCrypt();
//                            word2  = new String( mcrypt.decrypt(password) );
//                        } catch (Exception e) {
//
//                            e.printStackTrace();
//                        }
//             //           Log.d("pass", pass+pass.length());
//                        if(pass.equals(word2))
//                        {
                name = nameet.getText().toString();
                email = emailet.getText().toString();
                address = addresset.getText().toString();
                city = cityet.getText().toString();
                pincode = pincodeet.getText().toString();
                validationfunction();
//                        }
//                        else
//                        {
//                            et.setError(getResources().getString(R.string.dialog_password_correct));
//                            Toast.makeText(Editprofile.this, R.string.dialog_password_correct, Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//                builder.setNegativeButton(R.string.dialog_password_button2, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Editprofile.this,MainActivity.class);
//                        intent.putExtra("redirection","Editprofile");
//                        Editprofile.this.finish();
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();

            }
        });


        nameet.setVisibility(View.VISIBLE);
        emailet.setVisibility(View.VISIBLE);
        addresset.setVisibility(View.VISIBLE);
        cityet.setVisibility(View.VISIBLE);
        pincodeet.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void updatedisplay() {
        progress.show();
        dbhelp entry = new dbhelp(Editprofile.this);
        entry.open();
        entry.updateeuser(id, name, email, address, city, pincode);
        entry.close();
        progress.hide();
        finish();
        Intent intent = new Intent(Editprofile.this, ViewProfile.class);
        intent.putExtra("redirection", "Editprofile");
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void validationfunction() {
        if (name.equals("") || name.isEmpty() || name.trim().isEmpty()) {
            nameet.setError(getResources().getString(R.string.correct_name));
            Toast.makeText(Editprofile.this, R.string.correct_name, Toast.LENGTH_LONG).show();
        } else if (email.equals("") || email.isEmpty() || email.trim().isEmpty()) {
            emailet.setError(getResources().getString(R.string.correct_email_error));
            Toast.makeText(Editprofile.this, R.string.correct_email_error, Toast.LENGTH_LONG).show();
        } else if (address.equals("") || address.isEmpty() || address.trim().isEmpty()) {
            addresset.setError(getResources().getString(R.string.correct_address_error));
            Toast.makeText(Editprofile.this, R.string.correct_address_error, Toast.LENGTH_LONG).show();
        } else if (city.equals("") || city.isEmpty() || city.trim().isEmpty()) {
            cityet.setError(getResources().getString(R.string.correct_city_error));
            Toast.makeText(Editprofile.this, R.string.correct_city_error, Toast.LENGTH_LONG).show();
        }
//        else if(phone.length() != 10)
//        {
//            phoneet.setError(getResources().getString(R.string.correct_limit_contact));
//            Toast.makeText(Editprofile.this, R.string.correct_limit_contact, Toast.LENGTH_LONG).show();
//        }
        else {
//            if(isValidPhoneNumber(phone))
//            {
//                if(isonline())
//                {
//                    progress.show();
//                    StringRequest request = new StringRequest(Request.Method.POST,getResources().getString(R.string.url_reference)+"edit_profile.php",
//
//                            new Response.Listener<String>() {
//
//                                @Override
//                                public void onResponse(String arg0) {
// //                                   Log.d(TAG, arg0);
// //                                   Log.d("here in sucess","sucess");
//                                    JSONObject parentObject;
//                                    try {
//                                        parentObject = new JSONObject(arg0);
//                                        //						String id = parentObject.getString("id");
//                                        String sucess = parentObject.getString("sucess");
//                                        if(sucess.equals("Profile updated"))
//                                        {
//                                            progress.hide();
//                                            updatedisplay();
//                                        }
//                                        else
//                                        {
//                                            progress.hide();
//                                            Toast.makeText(Editprofile.this, R.string.unknownerror5,Toast.LENGTH_LONG).show();
//                                        }
//                                    } catch (JSONException e) {
//
//  //                                      e.printStackTrace();
//                                    }
//                                    catch(Exception e)
//                                    {
//  //                                      e.printStackTrace();
//                                    }
//
//
//                                }
//                            },
//
//
//                            new Response.ErrorListener() {
//
//                                @Override
//                                public void onErrorResponse(VolleyError arg0) {
//                                    progress.hide();
// //                                   VolleyLog.d(TAG, "Error: " + arg0.getMessage());
//                                    Toast.makeText(Editprofile.this, R.string.nointernetaccess, Toast.LENGTH_LONG).show();
// //                                   Toast.makeText(Editprofile.this, arg0.getMessage(), Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(Editprofile.this, MainActivity.class);
//                                    intent.putExtra("redirection","Editprofile");
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    Editprofile.this.finish();
//                                    finish();
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
//                                }
//                            }){
//
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("email", email);
//                            params.put("name", name);
//                            params.put("bsname", bsname);
//                            params.put("bscategory", category);
//                            params.put("phone", phone);
//                            params.put("fbpage", fbpage);
//                            return params;
//                        };
//                    };
//
//
//                    AppController.getInstance().addToRequestQueue(request, tag_string_req_send);
//                }
//                else
//                {
//                    Toast.makeText(Editprofile.this,R.string.nointernetconnection,Toast.LENGTH_LONG).show();
//                }
//            }
//            else
//            {
//                Toast.makeText(Editprofile.this, R.string.correct_mobile, Toast.LENGTH_LONG).show();
//            }
            updatedisplay();
        }
    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
//            try {
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                picturePathicon = cursor.getString(columnIndex);
//                cursor.close();
////                Log.d("lee", "icon");
//                Bitmap bm = BitmapFactory.decodeFile(picturePathicon);
//                ByteArrayOutputStream stream=new ByteArrayOutputStream();
////                bm = Bitmap.createScaledBitmap(bm, 96, 96, true);
//                float maxImageSize = (float) 300;
//                float ratio = Math.min(
//                        (float) maxImageSize / bm.getWidth(),
//                        (float) maxImageSize / bm.getHeight());
//                int width = Math.round((float) ratio * bm.getWidth());
//                int height = Math.round((float) ratio * bm.getHeight());
//                Log.d("width",""+width);
//                Log.d("height",""+height);
//                int testheight = height * 5;
//                int testheight2 = height * 3;
//                if(testheight<width) {
//                    bm = Bitmap.createScaledBitmap(bm, width, height, true);
//                }
//                else if(testheight2<width)
//                {
//                    maxImageSize = (float) 200;
//                    ratio = Math.min(
//                            (float) maxImageSize / bm.getWidth(),
//                            (float) maxImageSize / bm.getHeight());
//                    width = Math.round((float) ratio * bm.getWidth());
//                    height = Math.round((float) ratio * bm.getHeight());
//                    bm = Bitmap.createScaledBitmap(bm, width, height, true);
//                }
//                else
//                {
//                    maxImageSize = (float) 100;
//                    ratio = Math.min(
//                            (float) maxImageSize / bm.getWidth(),
//                            (float) maxImageSize / bm.getHeight());
//                    width = Math.round((float) ratio * bm.getWidth());
//                    height = Math.round((float) ratio * bm.getHeight());
//                    bm = Bitmap.createScaledBitmap(bm, width, height, true);
//                }
//
//                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                logo = stream.toByteArray();
//                imgString = Base64.encodeToString(logo, Base64.NO_WRAP);
//                ig.setImageBitmap(bm);
//                Toast.makeText(Editprofile.this, R.string.correct_icon, Toast.LENGTH_LONG).show();
//            }
//            catch (Exception e) {
//
//                e.printStackTrace();
//                Toast.makeText(Editprofile.this, R.string.correct_icone_size, Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Editprofile.this, ViewProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

}