package com.charnock.dev.parsers;


import android.util.Log;

import com.charnock.dev.model.Response_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Response_JSONParser {

    public static List<Response_Model> parserFeed(String content) {
        try {

            JSONObject parentObject = new JSONObject(content);
            List<Response_Model> respnse = new ArrayList<>();
            Response_Model flower = new Response_Model();
            flower.setId(parentObject.getString("id"));
            Log.d("id", parentObject.getString("id"));
            flower.setSuccess(parentObject.getString("success"));
            Log.d("success", parentObject.getString("success"));
            respnse.add(flower);
            return respnse;
        } catch (JSONException e) {

            Log.d("error in json", "l " + e);
            return null;
        } catch (Exception e) {
            Log.d("json connection", "No internet access" + e);
            return null;
        }

    }
}
