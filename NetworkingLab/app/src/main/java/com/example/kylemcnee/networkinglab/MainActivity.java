package com.example.kylemcnee.networkinglab;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button mCerealButton;
    Button mChocolateButton;
    Button mTeaButton;
    ListView mListView;

    ArrayList<String> mProductDisplayArray = new ArrayList<String>();
    ArrayAdapter<String> mAdapter;

    String mCerealQuery = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=xx2c73n6acnbcdhjqwk3gp8n";
    String mChocolateQuery = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=xx2c73n6acnbcdhjqwk3gp8n";
    String mTeaQuery = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=xx2c73n6acnbcdhjqwk3gp8n";

    SearchAsyncTask mCerealTask;
    SearchAsyncTask mChocolatetask;
    SearchAsyncTask mTeaTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button) findViewById(R.id.cerealButton);
        mChocolateButton = (Button) findViewById(R.id.chocolateButton);
        mTeaButton = (Button) findViewById(R.id.teaButton);
        mListView = (ListView)findViewById(R.id.listView);

        mAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mProductDisplayArray);


        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCerealTask != null && mCerealTask.getStatus() != SearchAsyncTask.Status.FINISHED) {
                    mCerealTask.cancel(true);
                }
                mCerealTask = new SearchAsyncTask();
                mCerealTask.execute(mCerealQuery);
            }
        });

        mChocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChocolatetask != null && mChocolatetask.getStatus() != SearchAsyncTask.Status.FINISHED){
                    mChocolatetask.cancel(true);
                }
                mChocolatetask = new SearchAsyncTask();
                mChocolatetask.execute(mChocolateQuery);
            }
        });

        mTeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTeaTask != null && mTeaTask.getStatus() != SearchAsyncTask.Status.FINISHED){
                    mTeaTask.cancel(true);
                }
                mTeaTask = new SearchAsyncTask();
                mTeaTask.execute(mTeaQuery);
            }
        });

        mListView.setAdapter(mAdapter);

    }

    public class SearchAsyncTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            String product = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");

                connection.connect();
                InputStream inputStream = connection.getInputStream();
                product = getInputData(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject object = new JSONObject(product);
//                String status = object.getString("stat");

                JSONArray productArray = object.getJSONArray("items");
                mProductDisplayArray.clear();
                for (int i = 0; i < productArray.length(); i++){
                    JSONObject secondObject = productArray.optJSONObject(i);
                    String name = secondObject.optString("name");

                    mProductDisplayArray.add(name);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
        }
    }
    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String product;

        while ((product = reader.readLine()) != null){
            builder.append(product);
        }

        return builder.toString();
    }
}
