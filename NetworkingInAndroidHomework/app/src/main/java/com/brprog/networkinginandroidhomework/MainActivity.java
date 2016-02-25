package com.brprog.networkinginandroidhomework;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
    ListView mProductsListView;
    ArrayList<String> mWalmartArrayList;
    ArrayAdapter<String> mProductsAdapter;
    DownloadAsyncTask mWalmartTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button)findViewById(R.id.cereal_button);
        mChocolateButton = (Button)findViewById(R.id.chocolate_button);
        mTeaButton = (Button)findViewById(R.id.tea_button);
        mProductsListView = (ListView)findViewById(R.id.products_listview);
        mWalmartArrayList = new ArrayList<>();
        mProductsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mWalmartArrayList);
        mProductsListView.setAdapter(mProductsAdapter);


        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetRequest(R.string.all_cereal_query, R.string.all_cereal_id);
            }
        });

        mChocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetRequest(R.string.chocolate_query, R.string.chocolate_id);
            }
        });

        mTeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetRequest(R.string.tea_query, R.string.tea_id);
            }
        });

    }

    protected void doGetRequest(int query, int taxonomyId) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (mWalmartTask != null && mWalmartTask.getStatus() != DownloadAsyncTask.Status.FINISHED) {
                mWalmartTask.cancel(true);
            } else {
                mWalmartTask = new DownloadAsyncTask();
                mWalmartTask.execute("http://api.walmartlabs.com/v1/search?query=" + getString(query)
                        + "&format=json&categoryId=" + getString(taxonomyId)
                        + "&apiKey=" + getString(R.string.api_key));
            }
        } else {
            Toast.makeText(MainActivity.this, "Unable to connect to IT services", Toast.LENGTH_SHORT).show();
        }
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

    public class DownloadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inStream = connection.getInputStream();
                data = getInputData(inStream);

            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try {
                JSONObject dataObject = new JSONObject(data);
                JSONArray itemsJSONArray = dataObject.getJSONArray("items");
                mWalmartArrayList.clear();
                for (int i =0; i < itemsJSONArray.length(); i++) {
                    JSONObject object = itemsJSONArray.optJSONObject(i);
                    String name = object.optString("name");
                    String price = object.optString("salePrice");
                    mWalmartArrayList.add(name + " \nCosts: $" + price);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            mProductsAdapter.notifyDataSetChanged();

        }

    }


}
