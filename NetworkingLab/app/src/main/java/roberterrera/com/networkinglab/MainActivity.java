package roberterrera.com.networkinglab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private ArrayList<String> mArrayList;
    private ArrayAdapter<String> mArrayAdapter;
    private String cerealURL = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    private String chocolateURL = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    private String teaURL = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    private ItemAsyncTask mItemAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mCereal = (Button) findViewById(R.id.button_cereal);
        final Button mChocolate = (Button) findViewById(R.id.button_chocolate);
        final Button mTea = (Button) findViewById(R.id.button_tea);
        ListView mListView = (ListView) findViewById(R.id.listview);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        mItemAsyncTask = new ItemAsyncTask();

        mArrayList = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mArrayList);
        mListView.setAdapter(mArrayAdapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        mCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkInfo != null && networkInfo.isConnected()) {

                    if(mItemAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
                        mItemAsyncTask.cancel(true);
                    }

                    mItemAsyncTask = new ItemAsyncTask();
                    mItemAsyncTask.execute(cerealURL);

                } else {
                    // the connection is not available
                    Toast.makeText(MainActivity.this, "No connection :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mChocolate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkInfo != null && networkInfo.isConnected()) {

                    if(mItemAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
                        mItemAsyncTask.cancel(true);
                    }

                    mItemAsyncTask = new ItemAsyncTask();
                    mItemAsyncTask.execute(chocolateURL);

                } else {
                    // the connection is not available
                    Toast.makeText(MainActivity.this, "No connection :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkInfo != null && networkInfo.isConnected()) {

                    if(mItemAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
                        mItemAsyncTask.cancel(true);
                    }

                    mItemAsyncTask = new ItemAsyncTask();
                    mItemAsyncTask.execute(teaURL);

                } else {
                    // the connection is not available
                    Toast.makeText(MainActivity.this, "No connection :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ItemAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... myUrl) {
            String contentAsString = "";
            InputStream inputStream = null;
            try {
                URL url = new URL(myUrl[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Starts the query
                conn.connect();
                inputStream = conn.getInputStream();

                // Converts the InputStream into a string
                contentAsString = getInputStream(inputStream);
                return contentAsString;

            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try {
                mArrayList.clear();

                // Get the data inside the JSON object, and the data inside the object's array.
                JSONObject dataObject = new JSONObject(contentAsString); // Could take no params, or could take the string you want to use.
                JSONArray itemJsonArray = dataObject.getJSONArray("items"); // Getting the array gets the stuff inside the object.


                // For every object in the item array, add the name to the ArrayList.
                for (int i = 0; i < itemJsonArray.length(); i++) {
                    JSONObject object = itemJsonArray.optJSONObject(i);
                    String name = object.optString("name");
                    mArrayList.add(name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            return contentAsString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.GONE);
            Log.d("ONPOSTEXECUTE", "Array = " + mArrayList.toString());
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    public String getInputStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String read;

        while((read = br.readLine()) != null) {
            sb.append(read);
        }

        br.close();
        return sb.toString();
    }

}
