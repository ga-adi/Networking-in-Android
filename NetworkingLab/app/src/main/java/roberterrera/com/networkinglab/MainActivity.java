package roberterrera.com.networkinglab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private ArrayList<String> mArrayList;
    private ArrayAdapter<String> mArrayAdapter;
    public String cerealURL = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    public String chocolateURL = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    public String teaURL = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    public ItemAsyncTask chocolateAsyncTask;
    public ItemAsyncTask teaAsyncTask;
    public ItemAsyncTask cerealAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cerealAsyncTask = new ItemAsyncTask();
        chocolateAsyncTask = new ItemAsyncTask();
        teaAsyncTask = new ItemAsyncTask();


        Button mCereal = (Button) findViewById(R.id.button_cereal);
        Button mChocolate = (Button) findViewById(R.id.button_chocolate);
        Button mTea = (Button) findViewById(R.id.button_tea);
        ListView mListView = (ListView) findViewById(R.id.listview);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);

        mArrayList = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mArrayList);
        mListView.setAdapter(mArrayAdapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // the connection is available
            mCereal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chocolateAsyncTask.cancel(true);
                    teaAsyncTask.cancel(true);
                    cerealAsyncTask.execute(cerealURL);
                }
            });

            mChocolate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teaAsyncTask.cancel(true);
                    cerealAsyncTask.cancel(true);
                    chocolateAsyncTask.execute(chocolateURL);
                }
            });

            mTea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerealAsyncTask.cancel(true);
                    chocolateAsyncTask.cancel(true);
                    teaAsyncTask.execute(teaURL);
                }
            });

        } else {
            // the connection is not available
            Toast.makeText(MainActivity.this, "No connection :(", Toast.LENGTH_SHORT).show();
        }
    }




    private String getInputData(InputStream inStream) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader( new InputStreamReader(inStream));
        String data;

        try {
            while ( (data = reader.readLine()) != null ){
                builder.append(data); // Pieces the downloaded data together.
            }
            reader.close();
        } catch (IOException e) {}

        return builder.toString();

    }


    private class ItemAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            String data = "";
            try {
                URL url = new URL(urls[0]); // Make a new URL class
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Open a connection to the internet
                connection.connect(); // You're connected to the internet!

                InputStream inStream = connection.getInputStream();

                data = getInputData(inStream);

            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try {
                // Get the data inside the JSON object, and the data inside the object's array.
                JSONObject dataObject = new JSONObject(data); // Could take no params, or could take the string you want to use.
                JSONObject cerealObject = dataObject.getJSONObject("query");
                JSONArray cerealJsonArray = cerealObject.getJSONArray("items"); // Getting the array gets the stuff inside the object.

                mArrayList.clear();

                // For every object in the item array, add the name to the ArrayList.
                for (int i = 0; i < cerealJsonArray.length(); i++ ){
                    JSONObject object = cerealJsonArray.optJSONObject(i);
                    String name = object.optString("name");
                    mArrayList.add(name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mArrayAdapter.clear();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            mArrayAdapter.notifyDataSetChanged();
        }
    }

}
