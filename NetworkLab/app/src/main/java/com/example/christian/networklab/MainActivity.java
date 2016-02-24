package com.example.christian.networklab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
​
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
​
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
​
public class MainActivity extends AppCompatActivity {
    ​
    private String urlStringCereal = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=2m7c5ftbbrvxnahp6kjp23md";
    private String urlStringChocoloate = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=2m7c5ftbbrvxnahp6kjp23md";
    private String urlStringTea = "http://api.walmartlabs.com/v1/search?query=teal&format=json&apiKey=2m7c5ftbbrvxnahp6kjp23md";
    private ListView mListView;
    private Button mCereal;
    private Button mChocolate;
    private Button mTea
    ​
    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mAdapter;
    ​
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ​
        mStringArray = new ArrayList<String>();
        ​
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStringArray);
        ​
        mListView.setAdapter(mAdapter);
        ​
        mCereal = (Button) findViewById(R.id.cereal);
        mChocolate = (Button) findViewById(R.id.chocolate);
        mTea = (Button) findViewById(R.id.tea);

        mCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ​
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                ​
                downloadAsyncTask.execute(urlStringCereal);

            }
        });

        mChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ​
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                ​
                downloadAsyncTask.execute(urlStringChocoloate);

            }
        });

        mTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ​
                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask();
                ​
                downloadAsyncTask.execute(urlStringTea);

            }
        });
    }
    ​
    private String getInputData(InputStream inStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader( new InputStreamReader(inStream));
        ​
        String data;
        ​
        while ((data = reader.readLine()) != null){
            builder.append(data);
        }
        ​
        reader.close();
        ​
        return builder.toString();
    }
    ​
    public class DownloadAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                ​
                InputStream inStream = connection.getInputStream();
                ​
                data = getInputData(inStream);
                ​
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
            ​
            ​
            try {
                JSONObject dataObject = new JSONObject(data);
                ​
//                String status = dataObject.getString("stat");
                JSONArray itemsJsonArray = dataObject.getJSONArray("items");
                ​
                mStringArray.clear();
                ​
                for (int i = 0; i < itemsJsonArray.length(); i++){
                    JSONObject object = itemsJsonArray.optJSONObject(i);
                    String name = object.optString("name");
                    String description = object.optString("description");
                    ​
                    mStringArray.add(name + "\n\r" + description);
                }
                ​
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ​
            ​
            return data;
        }
        ​
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            ​
            mAdapter.notifyDataSetChanged();
        }
    }​
}
