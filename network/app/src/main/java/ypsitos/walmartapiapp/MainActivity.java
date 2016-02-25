package ypsitos.walmartapiapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
    Button mTeaButton;
    Button mChocolateButton;
    Button mCerealButton;
    URL mTeaUrl;
    URL mChocolateUrl;
    URL mCerealUrl;
    ListView mListView;
    ArrayList<String> mStringArray;
    ArrayAdapter<String> mAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            try {
                mTeaUrl = new URL("http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=8u2tky93nyckurd5txzkqec4");
                mChocolateUrl = new URL("http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=8u2tky93nyckurd5txzkqec4");
                mCerealUrl = new URL("http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=8u2tky93nyckurd5txzkqec4");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            mTeaButton = (Button)findViewById(R.id.teaButton);
            mTeaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalmartTask walmartTask = new WalmartTask();
                    walmartTask.execute(mTeaUrl.toString());
                }
            });


            mChocolateButton = (Button)findViewById(R.id.chocolateButton);
            mChocolateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalmartTask walmartTask = new WalmartTask();
                    walmartTask.execute(mChocolateUrl.toString());
                }
            });

            mCerealButton = (Button)findViewById(R.id.cerealButton);
            mCerealButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalmartTask walmartTask = new WalmartTask();
                    walmartTask.execute(mCerealUrl.toString());
                }
            });

            ArrayList<String> mStringArray;
            ArrayAdapter<String> mAdapter;

            mStringArray = new ArrayList<String>();
            ​
            mListView = (ListView) findViewById(R.id.listView1);
            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStringArray);
            mListView.setAdapter(mAdapter);
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
        public class WalmartTask extends AsyncTask<String, Void, String>{
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

                try {
                    JSONObject dataObject = new JSONObject(data);
                    ​
                    JSONArray itemJsonArray = dataObject.getJSONArray("name");
                    ​
                    mStringArray.clear();
                    ​
                    for (int i = 0; i < itemJsonArray.length(); i++){
                        JSONObject object = itemJsonArray.optJSONObject(i);
                        String name = object.optString("name");
                        ​
                        mStringArray.add(name);
                    }
                    ​
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ​
                ​
                return data;
            }​
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                ​
                mAdapter.notifyDataSetChanged();
            }
        }
        ​}
