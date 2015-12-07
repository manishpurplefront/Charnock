package com.charnock.dev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.charnock.dev.controller.AppController;
import com.charnock.dev.model.Image_Model;
import com.charnock.dev.model.Product_Description_Model;
import com.charnock.dev.parsers.Image_JSONParser;
import com.charnock.dev.parsers.ProductDescription_JSONParser;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_Description extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String category_id = "";
    String category_name = "";
    String subcategory_id = "";
    String subcategory_name = "";
    String product_id = "";
    String product_name = "";
    String business_id = "";
    Button btn_request, btn_demo_video;
    String video_link = "";
    View layout;
    List<Image_Model> feedlist_image;
    List<Product_Description_Model> feedlist;
    private SliderLayout mDemoSlider;
    private GestureDetector gesturedetector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product__description);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        btn_request = (Button) findViewById(R.id.btn_request);
        btn_demo_video = (Button) findViewById(R.id.btn_demo_video);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progress = new ProgressDialog(Product_Description.this);
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
        product_id = getIntent().getExtras().getString("product_id");
        product_name = getIntent().getExtras().getString("product_name");
        business_id = getIntent().getExtras().getString("business_id");

        try {
            getActionBar().setTitle((Html.fromHtml("<font color=\"" + getResources().getString(R.string.actionbar_text_color) + "\">" + product_name + "</font>")));
            getActionBar().setSubtitle("Product");
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.pf);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isonline()) {
            progress.show();
            getdata(getResources().getString(R.string.url_reference) + "home/product_data.php");
        } else {
            Toast.makeText(Product_Description.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_LONG).show();
        }

        if(isonline()) {
            progress.show();
            getimage_data(getResources().getString(R.string.url_reference) + "home/image_data.php");
        }

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_Description.this, Request_Information.class);
                intent.putExtra("category_id", category_id);
                intent.putExtra("category_name", category_name);
                intent.putExtra("subcategory_id", subcategory_id);
                intent.putExtra("subcategory_name", subcategory_name);
                intent.putExtra("product_name", product_name);
                intent.putExtra("product_id", product_id);
                intent.putExtra("business_id", business_id);
                intent.putExtra("video_link", video_link);
                Log.d("videourl", video_link);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        btn_demo_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product_Description.this, Video_Player.class);
                intent.putExtra("category_id", category_id);
                intent.putExtra("category_name", category_name);
                intent.putExtra("subcategory_id", subcategory_id);
                intent.putExtra("subcategory_name", subcategory_name);
                intent.putExtra("product_name", product_name);
                intent.putExtra("product_id", product_id);
                intent.putExtra("business_id", business_id);
                intent.putExtra("video_link", video_link);
                Log.d("videourl", video_link);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        layout = (LinearLayout) findViewById(R.id.linear_descp);
//        gesturedetector = new GestureDetector(new MyGestureListener());
//        layout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                gesturedetector.onTouchEvent(event);
//                return true;
//            }
//        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setBackgroundTintList(ColorStateList.valueOf(@));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText()
//
//            }
//        });

    }

//    public boolean dispatchTouchEvent(MotionEvent ev){
//        super.dispatchTouchEvent(ev);
//        return gesturedetector.onTouchEvent(ev);
//    }

    private void getimage_data(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("response", s);
                switch (s) {
                    case "":
                        Toast.makeText(Product_Description.this, "No image available", Toast.LENGTH_LONG).show();
                        break;
                    case "[]":
                        Toast.makeText(Product_Description.this, "No image available", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        feedlist_image = Image_JSONParser.parserFeed(s);
                        updateimage();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Product_Description.this,getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
                Toast.makeText(Product_Description.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",product_id);
                params.put("business_id",business_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    private void updateimage() {
        progress.show();
        if(feedlist_image != null) {
            HashMap<String,String> file_maps = new HashMap<>();
            for(Image_Model flower: feedlist_image) {
                if (flower.getName().equals("null")) {
                    file_maps.put(flower.getName(), flower.getImage());
                } else {
                    file_maps.put(product_name, flower.getImage());
                }

            }
            for(String name : file_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                textSliderView.bundle(new Bundle());
                mDemoSlider.addSlider(textSliderView);
            }

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);
        }
        else {
            Toast.makeText(Product_Description.this, "No image available", Toast.LENGTH_LONG).show();
        }
        progress.hide();
    }

    private void getdata(String url) {
        StringRequest request = new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progress.hide();
                Log.d("response", s);
                feedlist = ProductDescription_JSONParser.parserFeed(s);
                display_data();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Product_Description.this,getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
                Toast.makeText(Product_Description.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",product_id);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

    void display_data() {
        progress.show();
        if (feedlist != null) {
            try {
                for (final Product_Description_Model flower : feedlist) {
                    TextView tv = (TextView) findViewById(R.id.product_name);
                    tv.setText(flower.getProduct_name());
                    TextView tv5 = (TextView) findViewById(R.id.product_description);
                    tv5.setText(flower.getProduct_description());
                    video_link = flower.getVideo_url();
                    TableLayout tv1 = (TableLayout) findViewById(R.id.table1);
                    tv1.removeAllViewsInLayout();

                    String specficition = flower.getProduct_specification();
                    ArrayList<String> myarray = new ArrayList<>();
                    ArrayList<String> myarray2 = new ArrayList<>();

                    try {
                        JSONArray ar = new JSONArray(specficition);
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            myarray.add(obj.getString("label"));
                            myarray2.add(obj.getString("value"));
                            Log.d("value", myarray.get(i));
                            Log.d("label", myarray2.get(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.d("value", "" + myarray);
                    Log.d("label", "" + myarray2);

                    int i = 0;
                    for (int jj = 0; jj < myarray.size(); jj++) {
                        final TableRow tr = new TableRow(Product_Description.this);
                        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                        tr.setId(i);

                        final TextView b1 = new TextView(Product_Description.this);
                        b1.setTextSize(15);

                        String formatedString = myarray.get(jj).toString()
                                .replace("\n", "")
                                .trim();

                        b1.setText(formatedString);
                        b1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                        b1.setPadding(5, 0, 5, 10);
                        tr.addView(b1);

                        final TextView b3 = new TextView(Product_Description.this);
                        b3.setTextSize(15);
                        b3.setText("  :  ");
                        b3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                        b3.setPadding(5, 0, 5, 10);
                        tr.addView(b3);

                        final TextView b2 = new TextView(Product_Description.this);
                        b2.setTextSize(15);
                        b2.setText(myarray2.get(jj));
                        b2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                        b2.setPadding(5, 0, 5, 10);
                        tr.addView(b2);

                        tv1.addView(tr);

                        final View vline1 = new View(Product_Description.this);
                        vline1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
                        vline1.setBackgroundColor(Color.DKGRAY);
                        tv1.addView(vline1);
                    }

                    if (video_link.equals("null")) {
                        video_link = "";
                    }
                    if (video_link.equals("")) {
                        btn_demo_video.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        progress.hide();
    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) Product_Description.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Product_Description.this.finish();
                Product_Description.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Product_Description.this.finish();
        Product_Description.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
    }

//    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
//        private static final int SWIPE_MIN_DISTANCE = 150;
//        private static final int SWIPE_MAX_OFF_PATH = 100;
//        private static final int SWIPE_THRESHOLD_VELOCITY = 100;
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                               float velocityY) {
//            float dX = e2.getX()-e1.getX();
//            float dY = e1.getY()-e2.getY();
//            if (Math.abs(dY)<SWIPE_MAX_OFF_PATH &&
//                    Math.abs(velocityX)>=SWIPE_THRESHOLD_VELOCITY &&
//                    Math.abs(dX)>=SWIPE_MIN_DISTANCE ) {
//                if (dX>0) {
//                    Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
//                    Product_Description.this.finish();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
////            else if (Math.abs(dX)<SWIPE_MAX_OFF_PATH &&
////                    Math.abs(velocityY)>=SWIPE_THRESHOLD_VELOCITY &&
////                    Math.abs(dY)>=SWIPE_MIN_DISTANCE ) {
////                if (dY>0) {
////                    Toast.makeText(getApplicationContext(), "Up Swipe", Toast.LENGTH_SHORT).show();
////                } else {
////                    Toast.makeText(getApplicationContext(), "Down Swipe", Toast.LENGTH_SHORT).show();
////                }
////                return true;
////            }
//            return false;
//        }
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent touchevent) {
//        switch (touchevent.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                x1 = touchevent.getX();
//                y1 = touchevent.getY();
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                x2 = touchevent.getX();
//                y2 = touchevent.getY();
//
//                if (x1 < x2) {
//                    Intent intent = new Intent(Product_Description.this, Product_list.class);
//                    intent.putExtra("category_id",category_id);
//                    intent.putExtra("category_name",category_name);
//                    intent.putExtra("subcategory_id", subcategory_id);
//                    intent.putExtra("subcategory_name", subcategory_name);
//                    intent.putExtra("product_name",product_name );
//                    intent.putExtra("product_id",product_id );
//                    intent.putExtra("business_id",business_id);
//                    startActivity(intent);
////                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
//                }
//                break;
//            }
//        }
//        return false;
//    }
}
