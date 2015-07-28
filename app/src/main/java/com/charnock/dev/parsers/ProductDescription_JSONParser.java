package com.charnock.dev.parsers;

import android.util.Log;

import com.charnock.dev.model.Product_Description_Model;
import com.charnock.dev.model.Product_List_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BALAKUMAR on 25-Jul-15.
 */
public class ProductDescription_JSONParser {

    public static List<Product_Description_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Product_Description_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Product_Description_Model flower = new Product_Description_Model();
                flower.setProduct_code(obj.getString("code"));
                flower.setProduct_name(obj.getString("name"));
                flower.setProduct_specification(obj.getString("specification"));
                flower.setProduct_description(obj.getString("description"));
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
