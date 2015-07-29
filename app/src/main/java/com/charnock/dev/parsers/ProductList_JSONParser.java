package com.charnock.dev.parsers;

import android.util.Log;

import com.charnock.dev.model.Product_List_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductList_JSONParser {
    public static List<Product_List_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Product_List_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Product_List_Model flower = new Product_List_Model();
                flower.setProduct_id(obj.getString("id"));
                flower.setProduct_name(obj.getString("name"));
                flower.setProduct_image(obj.getString("image"));
                feedslist.add(flower);
            }
            return feedslist;
        } catch (JSONException e) {

            Log.d("error in json", "l " + e);
            return null;
        } catch (Exception e) {
            Log.d("json connection", "No internet access" + e);
            return null;
        }
    }

}
