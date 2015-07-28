package com.charnock.dev.parsers;

import android.util.Log;

import com.charnock.dev.model.Image_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Image_JSONParser {
    public static List<Image_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Image_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Image_Model flower = new Image_Model();
                flower.setId(obj.getString("id"));
                flower.setImage(obj.getString("image"));
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
