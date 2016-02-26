package com.farazfazli.walmartassist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "[" + MainActivity.class.getCanonicalName() + "]";

    private static final String API_KEY = "INSERTAPIKEYHERE";
    private static final String BASE_URL = "http://api.walmartlabs.com/v1/search?query=";
    private static final String END_URL = "&format=json&apiKey=" + API_KEY;

    private GrabWalmartJSONDataAsyncTask grabWalmartJSONDataAsyncTask;

    private ArrayList<String> mItemsArrayList;
    private ArrayAdapter<String> mArrayAdapter;

    // API Call: BASE_URL + mQuery + END_URL

    private String mQuery = "";
    private String mResponse = "";

    private ListView mItemsListView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mItemsArrayList = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItemsArrayList);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mItemsListView = (ListView) findViewById(R.id.itemsListView);
        mItemsListView.setAdapter(mArrayAdapter);

        Button mCerealButton = (Button) findViewById(R.id.cereal);
        Button mChocolateButton = (Button) findViewById(R.id.chocolate);
        Button mTeaButton = (Button) findViewById(R.id.tea);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Tapped!");
                mArrayAdapter.clear();
                mQuery = ((Button) view).getText().toString();
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) { // has internet
                    if(grabWalmartJSONDataAsyncTask == null) { // first run
                        grabWalmartJSONDataAsyncTask = new GrabWalmartJSONDataAsyncTask();
                    }
                    if(grabWalmartJSONDataAsyncTask.getStatus() != AsyncTask.Status.RUNNING) {
                        grabWalmartJSONDataAsyncTask = new GrabWalmartJSONDataAsyncTask(); // must create another one
                        grabWalmartJSONDataAsyncTask.execute();
                    } else {
                        Log.i(TAG, "Already running!");
                        Toast.makeText(MainActivity.this, "Already running!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i(TAG, "No internet!");
                    Toast.makeText(MainActivity.this, "No internet!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        mCerealButton.setOnClickListener(listener);
        mChocolateButton.setOnClickListener(listener);
        mTeaButton.setOnClickListener(listener);
    }

    private String getInputData(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String body;
        while ((body = reader.readLine()) != null) {
            builder.append(body);
        }

        reader.close();

        return builder.toString();
    }

    public class GrabWalmartJSONDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mItemsListView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground started!");
            try {
                URL url = new URL(BASE_URL + mQuery + END_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                mResponse = getInputData(inputStream);
                convertResponseToJsonData(mResponse);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(TAG, "MalformedURLException!");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "IOException!");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "JSON Exception!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mItemsListView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
            Log.i(TAG, mItemsArrayList.toString());
            mArrayAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }

    private void convertResponseToJsonData(String response) throws JSONException {
        JSONObject object = new JSONObject(response); // entire object
        JSONArray array = object.getJSONArray("items"); // items array
        for (int i = 0; i < array.length(); i++) {
            JSONObject currentObject = array.getJSONObject(i);
            String currentItem = currentObject.getString("name"); // specific item name
            mItemsArrayList.add(currentItem);
        }
    }
}