package com.austincjones.networkinginandroidlab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String urlStringCereal = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=8q26yg6ubjswxsg3h4sqgh3w";
    private String urlStringChocolate = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=8q26yg6ubjswxsg3h4sqgh3w";
    private String urlStringTea = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=8q26yg6ubjswxsg3h4sqgh3w";

    private ListView mListview;

    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mCerealBtn = (Button)findViewById(R.id.cereal);
        Button mChocolateBtn = (Button)findViewById(R.id.chocolate);
        Button mTeaBtn = (Button)findViewById(R.id.tea);

        mStringArray = new ArrayList<String>();
        mListview = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStringArray);

        mListview.setAdapter(mAdapter);

        mCerealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask = new WalmartAsyncTask();
                walmartAsyncTask.execute(urlStringCereal);
            }
        });

        mChocolateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask = new WalmartAsyncTask();
                walmartAsyncTask.execute(urlStringChocolate);

            }
        });

        mTeaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask = new WalmartAsyncTask();
                walmartAsyncTask.execute(urlStringTea);

            }
        });

    }

    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String data;

        while ((data = reader.readLine()) != null){
            builder.append(data);
        }

        reader.close();
        return builder.toString();
    }

//    private String downloadUrl(String myUrl) throws IOException, JSONException {
//        InputStream is = null;
//        try {
//            URL url = new URL(myUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//
//            // Starts the query
//            conn.connect();
//            is = conn.getInputStream();
//
//            // Converts the InputStream into a string
//            String contentAsString = readIt(is);
//            return contentAsString;
//
//            // Makes sure that the InputStream is closed after the app is
//            // finished using it.
//        } finally {
//            if (is != null) {
//                is.close();
//            }
//        }
//    }

    public class WalmartAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inStream = connection.getInputStream();
                data = getInputData(inStream);
                Log.d("MainActivity", data);
            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try{
                JSONObject dataObject = new JSONObject(data);

                //JSONObject itemObject = dataObject.getJSONObject("items");
                JSONArray itemJsonArray = dataObject.getJSONArray("items");

                mStringArray.clear();

                for (int i = 0; i < itemJsonArray.length(); i++){
                    JSONObject object = itemJsonArray.optJSONObject(i);
                    String title = object.optString("name");

                    mStringArray.add(title);
                }
            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            mAdapter.notifyDataSetChanged();
        }
    }
}
