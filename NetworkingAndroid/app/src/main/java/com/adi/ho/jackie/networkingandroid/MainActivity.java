package com.adi.ho.jackie.networkingandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String walmartKey = "5hbnkvrdvq3dafvfax34meez";
    String url = "http://api.walmartlabs.com/v1/search?query=";
    private String query;
    //private ArrayList<String> jsonItemNames;
    private ArrayList<WalmartItem> jsonItemList;
    //private ArrayAdapter<String> listAdapter;
    private WalmartItemAdapter listAdapter;
    DownloadCategoryAsyncTask mDownloadCategoryAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ListView walmartList = (ListView) findViewById(R.id.list_listview);
        Button teaSelect = (Button) findViewById(R.id.tea_but);
        Button chocSelect = (Button) findViewById(R.id.choc_but);
        Button cerealSelect = (Button) findViewById(R.id.cereal_but);

        mDownloadCategoryAsyncTask = new DownloadCategoryAsyncTask();

        ArrayList<CharSequence> spinnerList = new ArrayList<>();
        jsonItemList = new ArrayList<>();
        spinnerList.add("Select an item");
        spinnerList.add("Tea");
        spinnerList.add("Cereal");
        spinnerList.add("Chocolate");

        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);
        categorySpinner.setOnItemSelectedListener(spinListener);


        // listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1 , jsonItemList);
        listAdapter = new WalmartItemAdapter(MainActivity.this, jsonItemList);
        walmartList.setAdapter(listAdapter);

        //button listeners
        teaSelect.setOnClickListener(teaListener);
        chocSelect.setOnClickListener(chocListener);
        cerealSelect.setOnClickListener(cerealListener);

    }

    AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    return;
                case 1:
                    query = "Tea";
                    break;
                case 2:
                    query = "Cereal";
                    break;
                case 3:
                    query = "Chocolate";
                    break;
                default:
            }
            DownloadCategoryAsyncTask downloadCategoryAsyncTask = new DownloadCategoryAsyncTask();

            downloadCategoryAsyncTask.execute(url + query + "&format=json&apiKey=" + walmartKey);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    View.OnClickListener teaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String query = "Tea";
            if (mDownloadCategoryAsyncTask.getStatus() != null && mDownloadCategoryAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                mDownloadCategoryAsyncTask.cancel(true);
                mDownloadCategoryAsyncTask = new DownloadCategoryAsyncTask();
                mDownloadCategoryAsyncTask.execute(url + query + "&format=json&apiKey=" + walmartKey);
            } else {
                mDownloadCategoryAsyncTask = new DownloadCategoryAsyncTask();
                mDownloadCategoryAsyncTask.execute(url + query + "&format=json&apiKey=" + walmartKey);
            }
        }
    };
    View.OnClickListener chocListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String query = "Chocolate";
            if (mDownloadCategoryAsyncTask.getStatus() != null && mDownloadCategoryAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                mDownloadCategoryAsyncTask.cancel(true);
                mDownloadCategoryAsyncTask = new DownloadCategoryAsyncTask();
                mDownloadCategoryAsyncTask.execute(url + query + "&format=json&apiKey=" + walmartKey);
            } else {
                mDownloadCategoryAsyncTask = new DownloadCategoryAsyncTask();
                mDownloadCategoryAsyncTask.execute(url + query + "&format=json&apiKey=" + walmartKey);
            }
        }
    };
    View.OnClickListener cerealListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String query = "Cereal";
            if (mDownloadCategoryAsyncTask.getStatus() != null && mDownloadCategoryAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                mDownloadCategoryAsyncTask.cancel(true);
                mDownloadCategoryAsyncTask = new DownloadCategoryAsyncTask();
                mDownloadCategoryAsyncTask.execute(url + query + "&format=json&apiKey=" + walmartKey);
            } else {
                mDownloadCategoryAsyncTask = new DownloadCategoryAsyncTask();
                mDownloadCategoryAsyncTask.execute(url + query + "&format=json&apiKey=" + walmartKey);
            }
        }
    };

    private String getInputData(InputStream inStream) throws IOException {

        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream)); //

        String data; //usually for xml or json, where you can parse it

        while ((data = bufferedReader.readLine()) != null) {
            builder.append(data);//add each line on the reader to the data
        }
        bufferedReader.close();
        return builder.toString();

    }

    private class DownloadCategoryAsyncTask extends AsyncTask<String, Void, String> {
        String data;

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect(); // talking to server at this point

                InputStream inStream = connection.getInputStream(); //Gets data from internet

                data = getInputData(inStream);


            } catch (Throwable tho) {
                tho.printStackTrace();
            }


            try {
                JSONObject parentObject = new JSONObject(data);
                JSONArray itemsArrayJsonObject = parentObject.optJSONArray("items");

                jsonItemList.clear();
                for (int i = 0; i < itemsArrayJsonObject.length(); i++) {
                    JSONObject actualyItemJsonObject = itemsArrayJsonObject.getJSONObject(i);
                    String name = actualyItemJsonObject.getString("name");
                    String price = actualyItemJsonObject.getString("salePrice");
                    jsonItemList.add(new WalmartItem(name, price));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            // listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,jsonItemNames);
            listAdapter.notifyDataSetChanged();
        }
    }
}
