package com.example.android.walmart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity {

    Button mCerealButton;
    Button mChocolateButton;
    Button mTeaButton;
    ListView mListResults;
    String mCerealString;
    String mChocolateString;
    String mTeaString;
    ArrayList<String> mListArray;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button) findViewById(R.id.cerealButton);
        mChocolateButton = (Button) findViewById(R.id.chocolateButton);
        mTeaButton = (Button) findViewById(R.id.teaButton);
        mListResults = (ListView) findViewById(R.id.theListView);
        mCerealString = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=rbgd6kgpdyx3xzpjjvtgn9t4";
        mChocolateString = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=rbgd6kgpdyx3xzpjjvtgn9t4";
        mTeaString = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=rbgd6kgpdyx3xzpjjvtgn9t4";
        mListArray = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mListArray);
        mListResults.setAdapter(mAdapter);

        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask = new WalmartAsyncTask();
                walmartAsyncTask.execute(mCerealString);
            }
        });

        mChocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask = new WalmartAsyncTask();
                walmartAsyncTask.execute(mChocolateString);
            }
        });

        mTeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask = new WalmartAsyncTask();
                walmartAsyncTask.execute(mTeaString);

            }
        });
    }

    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String data;

        while ((data = reader.readLine()) != null) {
            builder.append(data);
        }

        reader.close();


        return builder.toString();

    }

    public class WalmartAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            String data = "";

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inStream = connection.getInputStream();

                data = getInputData(inStream);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                ;
            }

            try {
                JSONObject dataObject = new JSONObject(data);
                JSONArray itemsArrays = dataObject.getJSONArray("items");

                mListArray.clear();
                for (int i = 0; i < itemsArrays.length(); i++) {
                    JSONObject object = itemsArrays.optJSONObject(i);
                    String name = object.optString("name");
                    String price = object.optString("salePrice");
                    String nameAndPrice = name + "\nPrice: $" + price;
                    mListArray.add(nameAndPrice);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mAdapter.notifyDataSetChanged();
        }
    }


}
