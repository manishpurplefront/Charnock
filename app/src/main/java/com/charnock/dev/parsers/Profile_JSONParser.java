package com.charnock.dev.parsers;

import android.util.Log;

import com.charnock.dev.model.Profile_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 10/12/15.
 */
public class Profile_JSONParser {
    public static List<Profile_Model> parserFeed(String content) {
        try {
            JSONArray ar = new JSONArray(content);
            List<Profile_Model> feedslist = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Profile_Model flower = new Profile_Model();
                flower.setEmail(obj.getString("email"));
                flower.setName(obj.getString("name"));
                flower.setPhone(obj.getString("phone"));
                flower.setAddress_line1(obj.getString("address_line1"));
                flower.setAddress_line2(obj.getString("address_line2"));
                flower.setAddress_line3(obj.getString("address_line3"));
                flower.setCity(obj.getString("city"));
                flower.setPincode(obj.getString("pincode"));
                flower.setState(obj.getString("state"));
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
