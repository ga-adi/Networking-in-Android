package com.example.todo.networkinglab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
    Button mCerealButton, mChocolateButton, mTeaButton;
    String mTeaString = "http://api.walmartlabs.com/v1/search?apiKey=wg9r9j94m37sy5e7whn7yy3m&format=json&query=Tea";
    String mCerealString = "http://api.walmartlabs.com/v1/search?apiKey=wg9r9j94m37sy5e7whn7yy3m&format=json&query=Cereal";
    String mChocolateString = "http://api.walmartlabs.com/v1/search?apiKey=wg9r9j94m37sy5e7whn7yy3m&format=json&query=Chocolate";
    ListView mListView;
    ArrayAdapter<String> mAdapter;
    ArrayList<String> mArrayList;
    DownloadTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button) findViewById(R.id.xmlCerealButton);
        mChocolateButton = (Button) findViewById(R.id.xmlChocolateButton);
        mTeaButton = (Button) findViewById(R.id.xmlTeaButton);
        mListView = (ListView) findViewById(R.id.xmlListView);
        mArrayList = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,mArrayList);
        mListView.setAdapter(mAdapter);
        mTask = new DownloadTask();

        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if(mTask.getStatus()== AsyncTask.Status.RUNNING){mTask.cancel(true);}
                    mTask = new DownloadTask();
                    mTask.execute(mCerealString);
                } else {
                    Toast.makeText(MainActivity.this, "Check Network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mChocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if(mTask.getStatus()== AsyncTask.Status.RUNNING){mTask.cancel(true);}
                    mTask = new DownloadTask();
                    mTask.execute(mChocolateString);
                } else {
                    Toast.makeText(MainActivity.this, "Check Network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mTeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if(mTask.getStatus()== AsyncTask.Status.RUNNING){mTask.cancel(true);}
                    mTask = new DownloadTask();
                    mTask.execute(mTeaString);
                } else {
                    Toast.makeText(MainActivity.this, "Check Network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class DownloadTask extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... urls) {
            InputStream is;
            try {
                mArrayList.clear();
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String contentAsString;
                while((contentAsString = br.readLine()) != null) {sb.append(contentAsString);}
                contentAsString = sb.toString();
                sb.setLength(0);
                JSONObject object = new JSONObject(contentAsString);
                JSONArray array = object.optJSONArray("items");
                for (int i = 0; i < array.length(); i++){
                    JSONObject repo = array.getJSONObject(i);
                    String productName = repo.getString("name");
                    String productPrice = repo.optString("salePrice");
                    sb.append(productName + " $" + productPrice);
                    mArrayList.add(sb.toString());
                    sb.setLength(0);}
            } catch (Throwable e){
                e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            mAdapter.notifyDataSetChanged();
        }
    }
}
