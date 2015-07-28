package com.charnock.dev;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Product_Description extends ActionBarActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    String tag_string_req_recieve2 = "string_req_recieve2";
    ProgressDialog progress;
    String category_id = "";
    String category_name = "";
    String subcategory_id = "";
    String subcategory_name = "";
    String product_id = "";
    String product_name = "";
    Button btn_specification;

    List<Image_Model> feedlist_image;
    List<Product_Description_Model> feedlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product__description);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
//        btn_specification = (Button) findViewById(R.id.btn_specification);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progress = new ProgressDialog(this);
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
        Product_Description.this.setTitle(product_name);


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
    }

    private void getimage_data(String uri) {
        StringRequest request = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                switch (s) {
                    case "":
                        Toast.makeText(Product_Description.this, "No image available", Toast.LENGTH_LONG).show();
                        break;
                    case "[]":
                        Toast.makeText(Product_Description.this, "No image available", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Log.d("response", s);
                        feedlist_image = Image_JSONParser.parserFeed(s);
                        updateimage();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.hide();
                Toast.makeText(Product_Description.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(Product_Description.this,getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",product_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_string_req_recieve2);
    }

//    private void displayPopupWindow(View anchorView) {
//        PopupWindow popup = new PopupWindow(Product_Description.this);
//        View layout = getLayoutInflater().inflate(R.layout.pop_up, null);
//        popup.setContentView(layout);
//        // Set content width and height
//        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
//        // Closes the popup window when touch outside of it - when looses focus
//        popup.setOutsideTouchable(true);
//        popup.setFocusable(true);
//        // Show anchored to button
//        popup.setBackgroundDrawable(new BitmapDrawable());
//        popup.showAsDropDown(anchorView);
//    }

    private void updateimage() {
        progress.show();
        if(feedlist_image != null) {
 //           HashMap<String, Drawable> file_maps = new HashMap<>();
            HashMap<String,BitmapDrawable> file_maps = new HashMap<>();
            for(Image_Model flower: feedlist_image) {

                byte[] imageByteArray = Base64.decode(flower.getImage(), Base64.DEFAULT);
                if (imageByteArray != null) {
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
                    Bitmap company_logo = BitmapFactory.decodeStream(imageStream);

                    BitmapDrawable drawable = new BitmapDrawable(getResources(),company_logo );
                    file_maps.put(flower.getId(), drawable);
                }
            }
            for(String name : file_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .description(name)
                        .image(String.valueOf(file_maps.get(name)))
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
                Toast.makeText(Product_Description.this,volleyError.getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(Product_Description.this,getResources().getString(R.string.nointernetaccess),Toast.LENGTH_LONG).show();
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
                    TextView tv2 = (TextView) findViewById(R.id.product_code);
                    tv2.setText(flower.getProduct_code());
                    TextView tv3 = (TextView) findViewById(R.id.product_specification);
                    tv3.setText(flower.getProduct_specification());
                    TextView tv5 = (TextView) findViewById(R.id.product_description);
                    tv5.setText(flower.getProduct_description());
                }
            } catch (Exception e) {
//                e.printStackTrace();
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
//
//    @Override
//    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
//    }

    protected boolean isonline() {
        ConnectivityManager cm = (ConnectivityManager) Product_Description.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        return netinfo != null && netinfo.isConnectedOrConnecting();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Product_Description.this.finish();
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }
}
