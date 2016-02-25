package com.example.jafoole.walmartnetworkapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mAdapter;

    DownloadAsyncTask mDownloaderAsyncTask;

    Button mCereal;
    Button mChocolate;
    Button mTea;


    String cerealUrl = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=z4f3z9fqqsf4w8j44482v4wp";
    String chocolateUrl = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=z4f3z9fqqsf4w8j44482v4wp";
    String teaUrl = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=z4f3z9fqqsf4w8j44482v4wp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCereal = (Button)findViewById(R.id.cereal);
        mChocolate = (Button)findViewById(R.id.chocolate);
        mTea = (Button)findViewById(R.id.tea);


        mListView = (ListView)findViewById(R.id.listView);

        mStringArray = new ArrayList<String>();

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStringArray);
        mListView.setAdapter(mAdapter);


        mCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownloaderAsyncTask != null && mDownloaderAsyncTask.getStatus() != AsyncTask.Status.FINISHED){
                    mDownloaderAsyncTask.cancel(true);
                }
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                downloadAsyncTask.execute(cerealUrl);
            }
        });

        mChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownloaderAsyncTask != null && mDownloaderAsyncTask.getStatus() != AsyncTask.Status.FINISHED){
                    mDownloaderAsyncTask.cancel(true);
                }
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                downloadAsyncTask.execute(chocolateUrl);
            }
        });

        mTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownloaderAsyncTask != null && mDownloaderAsyncTask.getStatus() != AsyncTask.Status.FINISHED){
                    mDownloaderAsyncTask.cancel(true);
                }
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                downloadAsyncTask.execute(teaUrl);
            }
        });
    }
    private String getInputData(InputStream inputStream)throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String cerealData;

        while ((cerealData = reader.readLine())!= null){
            stringBuilder.append(cerealData);
        }

        reader.close();

        return  stringBuilder.toString();
    }

    public class DownloadAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                data = getInputData(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject dataObject = new JSONObject(data);

                JSONArray cerealObject = dataObject.getJSONArray("items");

                mStringArray.clear();

                for (int i=0; i < cerealObject.length(); i++){
                    JSONObject object = cerealObject.optJSONObject(i);

                    String name = object.getString("name");

                    mStringArray.add(name);
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
}


























