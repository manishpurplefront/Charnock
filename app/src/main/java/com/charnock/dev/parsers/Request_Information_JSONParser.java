package com.charnock.dev.parsers;

import android.util.Log;

import com.charnock.dev.model.Request_Information_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Request_Information_JSONParser {
    public static List<Request_Information_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Request_Information_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Request_Information_Model flower = new Request_Information_Model();
                flower.setId(obj.getString("id"));
                flower.setSite_Name(obj.getString("site_name"));
                flower.setDate(obj.getString("date"));
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
