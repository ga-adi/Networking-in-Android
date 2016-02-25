package com.example.asagir.myapplication;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button mCerealButton;
    Button mChocolateButton;
    Button mTeaButton;

    private ListView mListView;
    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mAdapter;

    String cerealUrl = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=9xhfjf2aj78axy7u43f3rv2e";
    String chocolateUrl = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=9xhfjf2aj78axy7u43f3rv2e";
    String teaUrl = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=9xhfjf2aj78axy7u43f3rv2e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button) findViewById(R.id.ceralButton);
        mChocolateButton = (Button) findViewById(R.id.chocolateButton);
        mTeaButton = (Button) findViewById(R.id.teaButton);

        mStringArray = new ArrayList<String>();

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStringArray);

        mListView.setAdapter(mAdapter);

        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                downloadAsyncTask.execute(cerealUrl);
            }
        });

        mChocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                downloadAsyncTask.execute(chocolateUrl);
            }
        });

        mTeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                downloadAsyncTask.execute(teaUrl);
            }
        });
    }

    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream));

        String cerealData;

        while ((cerealData = bufferedReader.readLine()) != null) {
            stringBuilder.append(cerealData);
        }

        bufferedReader.close();

        return stringBuilder.toString();

    }

    public class DownloadAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
//                connection.setRequestMethod("GET");
//                connection.setDoInput(true);
                connection.connect();

                InputStream inStream = connection.getInputStream();

                data = getInputData(inStream);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            try {
                JSONObject dataObject = new JSONObject(data);

                JSONArray cerealObject = dataObject.getJSONArray("items");

                mStringArray.clear();

                for (int i = 0; i < cerealObject.length(); i++) {
                    JSONObject object = cerealObject.optJSONObject(i);
                    String name = object.getString("name");

                    mStringArray.add(name);
                }
            } catch (JSONException exception) {
                exception.printStackTrace();
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
