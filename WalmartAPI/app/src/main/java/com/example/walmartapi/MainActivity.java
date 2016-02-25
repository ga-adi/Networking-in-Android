package com.example.walmartapi;

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
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button cerealButton;
    Button chocButton;
    Button teaButton;
    ListView listView;
    ArrayList<String> mStringArray;
    ArrayAdapter mAdapter;
    DlTask mDlTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cerealButton =  (Button) findViewById(R.id.cerealButton);
        chocButton =  (Button) findViewById(R.id.chocolateButton);
        teaButton =  (Button) findViewById(R.id.teaButton);
        listView = (ListView) findViewById(R.id.listView);
        mStringArray = new ArrayList<>();
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStringArray);
        listView.setAdapter(mAdapter);

        cerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getItems("cereal");
            }
        });

        chocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getItems("chocolate");
            }
        });

        teaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItems("tea");
            }
        });
    }


    private void getItems(String query) {
        if (mDlTask!=null && mDlTask.getStatus() != AsyncTask.Status.FINISHED){
            mDlTask.cancel(true);
        }
        mDlTask = new DlTask();
        mDlTask.execute("http://api.walmartlabs.com/v1/search?query="+query+"&format=json&apiKey=8rj3jh8j63z9haqvvf2w7bxu");
    }

    private class DlTask extends AsyncTask<String,Void,ArrayList>{
        @Override
        protected ArrayList doInBackground(String... url) {
            String data = "";
            try {
                URL urls = new URL(url[0]);
                HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
                connection.connect();
                InputStream inStream = connection.getInputStream();
                data = getInputData(inStream);
            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try {
                JSONObject dataObject = new JSONObject(data);
                JSONArray items = dataObject.getJSONArray("items");
                mStringArray.clear();
                for (int i = 0; i < items.length(); i++){
                    JSONObject object = items.optJSONObject(i);
                    String name = object.optString("name");
                    mStringArray.add(name);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

            return mStringArray;        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            mAdapter.notifyDataSetChanged();
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
}
