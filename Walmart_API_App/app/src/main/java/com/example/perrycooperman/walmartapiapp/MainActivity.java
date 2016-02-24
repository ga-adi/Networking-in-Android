package com.example.perrycooperman.walmartapiapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
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

    URL mUrl;
    String mSearchQuery, mTextFromInternet;
    Button mCerealButton, mChocolateButton, mTeaButton;
    ArrayList<String> mArray;
    ListView mListView;
    ArrayAdapter<String> mArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button)findViewById(R.id.cerealButton);
        mChocolateButton = (Button)findViewById(R.id.chocolateButton);
        mTeaButton = (Button)findViewById(R.id.teaButton);
        mListView = (ListView)findViewById(R.id.listView);
        mArray = new ArrayList<String>();

        mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mArray);
        mListView.setAdapter(mArrayAdapter);

        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchQuery = "http://api.walmartlabs.com/v1/search?apiKey=6xmarf8jt66bxs4qyccay98g&query=cereal";
                AttypicalProgrammer attypicalProgrammer = new AttypicalProgrammer();
                attypicalProgrammer.execute(mSearchQuery);
            }
        });

        mChocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchQuery = "http://api.walmartlabs.com/v1/search?apiKey=6xmarf8jt66bxs4qyccay98g&query=chocolate";
                AttypicalProgrammer attypicalProgrammer = new AttypicalProgrammer();
                attypicalProgrammer.execute(mSearchQuery);
            }
        });

        mTeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchQuery = "http://api.walmartlabs.com/v1/search?apiKey=6xmarf8jt66bxs4qyccay98g&query=tea";
                AttypicalProgrammer attypicalProgrammer = new AttypicalProgrammer();
                attypicalProgrammer.execute(mSearchQuery);
            }
        });


    }


    public String getInputData(InputStream streaminInmyDreamin){
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(streaminInmyDreamin));

        String data = "";
        try {
            while ((data=reader.readLine()) != null){
                builder.append(data);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public class AttypicalProgrammer extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                mUrl = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
                connection.connect();

                InputStream streaminInmyDreamin = connection.getInputStream();

                String stringVersionofJSONResult = getInputData(streaminInmyDreamin);
                JSONObject firstLevelObject = new JSONObject(stringVersionofJSONResult);
                JSONArray secondLevelArray = firstLevelObject.optJSONArray("items");

                mArray.clear();
                for (int i = 0; i <= secondLevelArray.length(); i++){
                    JSONObject object = secondLevelArray.optJSONObject(i);
                    mArray.add(object.optString("name"));
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mArrayAdapter.notifyDataSetChanged();
        }
    }

}