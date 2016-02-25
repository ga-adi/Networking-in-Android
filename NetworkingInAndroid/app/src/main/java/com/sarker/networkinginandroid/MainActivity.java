package com.sarker.networkinginandroid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String URL = "http://api.walmartlabs.com/v1/search?query=";
    private static final String API_KEY = "84gn8d6ftf2a4wqkakd6akw6";

    private ListView mItemsListView;
    private ArrayList mItemsArrayList;
    private ArrayAdapter mItemAdapter;
    private GetDataTask mGetDataTask;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mItemsListView = (ListView) findViewById(R.id.listview);
        mItemsArrayList = new ArrayList<>();
        mItemAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, mItemsArrayList);
        mItemsListView.setAdapter(mItemAdapter);

        mGetDataTask = new GetDataTask();

        Button cerealButton = (Button) findViewById(R.id.cereal_button);
        cerealButton.setOnClickListener(this);

        Button chocolateButton = (Button) findViewById(R.id.chocolate_button);
        chocolateButton.setOnClickListener(this);

        Button teaButton = (Button) findViewById(R.id.tea_button);
        teaButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String query;

        switch (v.getId()) {
            case R.id.cereal_button:
                query = "cereal";
                break;
            case R.id.chocolate_button:
                query = "chocolate";
                break;
            case R.id.tea_button:
                query = "tea";
                break;
            default:
                query = "none";
        }

        if (mGetDataTask.getStatus() != null && mGetDataTask.getStatus() != AsyncTask.Status.FINISHED) {
            mGetDataTask.cancel(true);
        }

        mGetDataTask = new GetDataTask();
        mGetDataTask.execute(URL + query + getString(R.string.format_json) + getString(R.string.api_key) + API_KEY);
    }

    private void getData(String myURL) throws IOException, JSONException {
        InputStream inputStream = null;

        try {
            // Opening the URL connection
            URL url = new URL(myURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            inputStream = conn.getInputStream();

            // Convert InputStream into a string
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String read;

            while ((read = bufferedReader.readLine()) != null) {
                stringBuilder.append(read);
            }

            // Process JSON data to make it user friendly
            parseJson(stringBuilder.toString());

            // Make sure InputStream is closed after the app is finished using it
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private void parseJson(String contentAsString) throws JSONException {
        mItemsArrayList.clear();

        JSONObject object = new JSONObject(contentAsString);
        JSONArray array = object.optJSONArray("items");
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            String itemName = item.getString("name");
            String itemPrice = item.getString("salePrice");
            mItemsArrayList.add(itemName + "\n$" + itemPrice);
        }
    }

    private class GetDataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                getData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mItemAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
