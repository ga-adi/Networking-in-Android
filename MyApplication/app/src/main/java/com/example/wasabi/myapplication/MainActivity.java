package com.example.wasabi.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button cerealButton, chocolateButton, teaButton;
    ListView mListView;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mArrayList;
    String query, url;
    WalmartAsyncTask mTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "http://api.walmartlabs.com/v1/search?apiKey=ypbx38jaxteyq7kky7mskam5&query=";

        cerealButton = (Button)findViewById(R.id.cerealButton);
        chocolateButton = (Button)findViewById(R.id.chocolateButton);
        teaButton = (Button)findViewById(R.id.teaButton);
        mListView = (ListView)findViewById(R.id.listview);

        mArrayList = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,mArrayList);
        mListView.setAdapter(mAdapter);


        cerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (mTask != null && (mTask.getStatus() != AsyncTask.Status.FINISHED)) {
                        mTask.cancel(true);
                    }
                    mTask = new WalmartAsyncTask();
                    query = "cereal";
                    mTask.execute(url+query);
                } else {
                    Toast.makeText(MainActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (mTask != null && (mTask.getStatus() != AsyncTask.Status.FINISHED)) {
                        mTask.cancel(true);
                    }
                    mTask = new WalmartAsyncTask();
                    query = "chocolate";
                    mTask.execute(url+query);
                } else {
                    Toast.makeText(MainActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        teaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (mTask != null && (mTask.getStatus() != AsyncTask.Status.FINISHED)) {
                        mTask.cancel(true);
                    }
                    mTask = new WalmartAsyncTask();
                    query = "tea";
                    mTask.execute(url+query);
                } else {
                    Toast.makeText(MainActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class WalmartAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String data ="";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inStream = connection.getInputStream();
                data = getInputData(inStream);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                JSONObject dataObject = new JSONObject(data);
                JSONArray itemJsonArray = dataObject.getJSONArray("items");

                mArrayList.clear();

                for(int i = 0; i < itemJsonArray.length() ; i++){
                    JSONObject object = itemJsonArray.getJSONObject(i);
                    String name = object.optString("name");
                    double price = object.optDouble("salePrice");

                    mArrayList.add(name+", Price : "+price);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mAdapter.notifyDataSetChanged();
        }
    }

    private String getInputData(InputStream inStream) throws IOException{
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

        String data = null;

        while ((data = reader.readLine()) != null){
            builder.append(data);
        }

        reader.close();

        return builder.toString();
    }


}
