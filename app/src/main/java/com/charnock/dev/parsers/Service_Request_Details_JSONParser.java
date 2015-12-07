package com.charnock.dev.parsers;

import android.util.Log;

import com.charnock.dev.model.Service_Request_Deatils_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Service_Request_Details_JSONParser {
    public static List<Service_Request_Deatils_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Service_Request_Deatils_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Service_Request_Deatils_Model flower = new Service_Request_Deatils_Model();

                flower.setUser_name(obj.getString("user_name"));
                flower.setUser_email(obj.getString("user_email"));
                flower.setUser_contact(obj.getString("user_contact"));
                flower.setNode_id(obj.getString("node_id"));
                flower.setNode_name(obj.getString("node_name"));
                flower.setNode_address(obj.getString("node_address"));
                flower.setNode_email(obj.getString("node_email"));
                flower.setNode_city(obj.getString("node_city"));
                flower.setNode_contact(obj.getString("node_contact"));
                flower.setProduct_status(obj.getString("status"));
                flower.setProducts(obj.getString("products"));
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
