package com.boloutaredoubeni.droidnet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class WalmartAPIClient {

  private static WalmartAPIClient mInstance = null;
  private static final String BASE_URL = "http://api.walmartlabs.com/v1/search?format=json&apiKey=h5xks9jb45shrgjnp2egvstf&query=";

  private WalmartAPIClient() {
  }

  public static WalmartAPIClient getInstance() {
    if (mInstance == null) {
      mInstance = new WalmartAPIClient();
    }
    return mInstance;
  }

  public String connectToServer(String query){
    InputStream in;
    String link = BASE_URL + query;
    String jsonResponse = "";
    try {
      URL url = new URL(link);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.connect();
      in = connection.getInputStream();

      BufferedReader buffer =  new BufferedReader(new InputStreamReader(in, "UTF-8"));
      String data;
      while ((data = buffer.readLine()) != null) {
        jsonResponse += data;
      }
      in.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    if ((jsonResponse.charAt(0) != '{') && (jsonResponse.charAt(jsonResponse.length() - 1) != '}')) {
      return null;
    }

    return jsonResponse;
  }

  public ArrayList<GroceryItem> parseJSON(String response) {
    ArrayList<GroceryItem> itemsList = new ArrayList<>();
    try{
      JSONObject jsonObject = new JSONObject(response);
      JSONArray items = jsonObject.getJSONArray("items");
      for (int i = 0; i < items.length(); ++i) {
        JSONObject item = items.getJSONObject(i);
        String name = item.getString("name");
        Double price = item.getDouble("salePrice");
        itemsList.add(new GroceryItem(name, price));
      }
    } catch (JSONException ex) {
      ex.printStackTrace();
    }
    return itemsList;
  }
}
