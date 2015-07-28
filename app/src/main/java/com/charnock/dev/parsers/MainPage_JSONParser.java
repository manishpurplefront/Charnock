package com.charnock.dev.parsers;


import android.util.Log;

import com.charnock.dev.model.Home_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainPage_JSONParser {
        public static List<Home_Model> parserFeed(String content) {
            try {
                JSONArray ar = new JSONArray(content);
                List<Home_Model> feedslist = new ArrayList<>();
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject obj = ar.getJSONObject(i);
                    Home_Model flower = new Home_Model();
                    flower.setId(obj.getString("id"));
                    flower.setName(obj.getString("name"));
                    flower.setUser_id(obj.getString("user_id"));
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
