package com.example.ra.networkinglab;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Button mCereal;
    Button mTea;
    Button mChocolate;
    ListView mListview;
    ArrayList<String> mArrayList;
    ArrayAdapter<String> mAdapter;
    String urlTea= "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=jcpk6chshjwn5nbq2khnrvm9";
    String urlCereal="http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=jcpk6chshjwn5nbq2khnrvm9";
    String urlChoc=" http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=jcpk6chshjwn5nbq2khnrvm9";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArrayList= new ArrayList<String>();
        mListview=(ListView)findViewById(R.id.listView);
        mAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mArrayList);
        mListview.setAdapter(mAdapter);

        mTea=(Button)findViewById(R.id.tea);
        mCereal=(Button)findViewById(R.id.cereal);
        mChocolate=(Button)findViewById(R.id.choc);

        mTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask= new WalmartAsyncTask();
                walmartAsyncTask.execute(urlTea);
            }
        });

        mCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask= new WalmartAsyncTask();
                walmartAsyncTask.execute(urlCereal);
            }
        });

        mChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalmartAsyncTask walmartAsyncTask= new WalmartAsyncTask();
                walmartAsyncTask.execute(urlChoc);
            }
        });

    }

    private String getInputData(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder= new StringBuilder();
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String data;

        while ((data=bufferedReader.readLine()) !=null){
            stringBuilder.append(data);
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }
    public class WalmartAsyncTask extends AsyncTask<String,Void,String>{
        String data= " ";

        @Override
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                data = getInputData(inputStream);


            } catch (Throwable thr) {
                thr.fillInStackTrace();


            }
            try {

                JSONObject dataObject = new JSONObject(data);

                JSONArray teaArray = dataObject.getJSONArray("items");

                mArrayList.clear();

                for (int i = 0; i < teaArray.length(); i++) {
                    JSONObject object = teaArray.optJSONObject(i);
                    String title = object.optString("name");

                    mArrayList.add(title);
                }
                int i = 0;i++;

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
