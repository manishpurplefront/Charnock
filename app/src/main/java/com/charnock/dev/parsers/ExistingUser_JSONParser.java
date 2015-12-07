package com.charnock.dev.parsers;


import android.util.Log;

import com.charnock.dev.model.ExistingUser_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExistingUser_JSONParser {
    public static List<ExistingUser_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<ExistingUser_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                ExistingUser_Model flower = new ExistingUser_Model();
                flower.setId(obj.getString("id"));
                flower.setBusiness_id(obj.getString("business_id"));
                flower.setEmail(obj.getString("email"));
                flower.setPassword(obj.getString("password"));
                flower.setName(obj.getString("name"));
                flower.setPhone(obj.getString("phone"));
                flower.setProfile_id(obj.getString("profile_id"));
                flower.setNode_id(obj.getString("node_id"));
                flower.setRole_id(obj.getString("role_id"));
                flower.setPermission_id(obj.getString("permission_id"));
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
