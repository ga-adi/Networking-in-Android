package com.brprog.networkinginandroidhomework;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private String mApiKey = "av3vuutdbxvyfbymb22sgaqg";
    private String mAllCerealUrlString = "http://api.walmartlabs.com/v1/search?query=all+cereal&format=json&nojsoncallback=1&categoryId=976759_976783_1001339&apiKey=av3vuutdbxvyfbymb22sgaqg";


    Button mCerealButton;
    Button mChocolateButton;
    Button mTeaButton;
    ListView mProductsListView;
    ArrayList<String> mCerealArrayList;
    ArrayList<String> mChocolateArrayList;
    ArrayList<String> mTeaArrayList;
    ArrayAdapter<String> mProductsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCerealButton = (Button)findViewById(R.id.cereal_button);
        mChocolateButton = (Button)findViewById(R.id.chocolate_button);
        mTeaButton = (Button)findViewById(R.id.tea_button);
        mProductsListView = (ListView)findViewById(R.id.products_listview);
        mCerealArrayList = new ArrayList<>();
        mProductsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mCerealArrayList);
        mProductsListView.setAdapter(mProductsAdapter);


        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncTask task = new DownloadAsyncTask();
                task.execute(mAllCerealUrlString);
            }
        });

//        mChocolateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mProductsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mChocolateArrayList);
//                mProductsListView.setAdapter(mProductsAdapter);
//            }
//        });
//
//        mTeaButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mProductsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mTeaArrayList);
//                mProductsListView.setAdapter(mProductsAdapter);
//            }
//        });

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

    public class DownloadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String data = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inStream = connection.getInputStream();
                data = getInputData(inStream);

            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try {
                JSONObject dataObject = new JSONObject(data);
                JSONArray itemsJSONArray = dataObject.getJSONArray("items");
                mCerealArrayList.clear();
                for (int i =0; i < itemsJSONArray.length(); i++) {
                    JSONObject object = itemsJSONArray.optJSONObject(i);
                    String name = object.optString("name");
                    mCerealArrayList.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            mProductsAdapter.notifyDataSetChanged();

        }

    }


}
