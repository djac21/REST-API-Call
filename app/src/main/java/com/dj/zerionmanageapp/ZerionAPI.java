package com.dj.zerionmanageapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class ZerionAPI {

    private String url;
    private ArrayList<String> idList;
    private StringBuilder builder;

    public ZerionAPI() {
        String user_id = "3641286";
        String profile_id = "470196";
        url = "https://app.iformbuilder.com/exzact/api/profiles/" + profile_id + "/pages/" + user_id + "/records";

        idList = new ArrayList<>();
        builder = new StringBuilder();
    }

    public void generateIdURL(String token) {
        url += "?ACCESS_TOKEN=" + token + "&VERSION=5.1";
    }

    public void generateDetailURL(String token, int pos) {
        int itemID = (pos + 1) * 3;
        url = url + "/" + itemID + "/feed?ACCESS_TOKEN=" + token + "&VERSION=5.1&FORMAT=json";

        System.out.println("--- Generated URL ---" + "\n\n" + url);
    }

    public void getResponse() {
        try {
            System.out.println("--- Get Response ---");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);

            System.out.println("--- New URL ---" + "\n\n" + url);

            HttpResponse response = httpClient.execute(getRequest);

            System.out.println("--- Response ---" + "\n\n" + getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            for (String line = null; (line = br.readLine()) != null; ) {
                builder.append(line).append("\n");
            }

            httpClient.getConnectionManager().shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getIdFromResponse() {
        JSONObject jsonObj;
        try {
            System.out.println("--- Get JSON ---");

            jsonObj = new JSONObject(builder.toString());
            JSONArray recordArray = jsonObj.getJSONArray("RECORDS");
            for (int i = 0; i < recordArray.length(); i++) {
                JSONObject objects = recordArray.getJSONObject(i);
                idList.add("Id: " + Integer.toString(objects.getInt("ID")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.reverse(idList);
        return idList;
    }

    public DetailsModel getItemDetailFromResponse() {
        DetailsModel newItem = null;
        JSONArray jsonArray;

        try {
            jsonArray = new JSONArray(builder.toString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject jsonObject1 = jsonObject.getJSONObject("record");

            newItem = new DetailsModel();
            newItem.setId(jsonObject1.getString("ID"));
            newItem.setName(jsonObject1.getString("input_name"));
            newItem.setPhone(jsonObject1.getString("input_phone"));
            newItem.setDate(jsonObject1.getString("input_date"));
            newItem.setAge(Integer.toString(jsonObject1.getInt("input_age")));

            String imgStr = jsonObject1.getString("input_photo");
            newItem.setImage(imgStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newItem;
    }
}