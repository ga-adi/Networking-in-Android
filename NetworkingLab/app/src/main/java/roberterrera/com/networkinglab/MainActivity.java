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
//    public ItemAsyncTask chocolateAsyncTask,teaAsyncTask, cerealAsyncTask;
    public String cerealURL = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    public String chocolateURL = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";
    public String teaURL = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=hkx9q4hya6s2mq4ts79jmtd5";

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

        mArrayList = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mArrayList);
        mListView.setAdapter(mArrayAdapter);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        mCereal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (networkInfo != null && networkInfo.isConnected()) {

                    // TODO: Fix crash on cancel. Check if this is only with no connection.

                    ItemAsyncTask chocolateAsyncTask = new ItemAsyncTask();
                    chocolateAsyncTask.cancel(true);

                    ItemAsyncTask teaAsyncTask = new ItemAsyncTask();
                    teaAsyncTask.cancel(true);

                    ItemAsyncTask cerealAsyncTask = new ItemAsyncTask();
                    cerealAsyncTask.execute(cerealURL);

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

                    // TODO: Fix crash on cancel.

                    ItemAsyncTask teaAsyncTask = new ItemAsyncTask();
                    teaAsyncTask.cancel(true);

                    ItemAsyncTask cerealAsyncTask = new ItemAsyncTask();
                    cerealAsyncTask.cancel(true);

                    ItemAsyncTask chocolateAsyncTask = new ItemAsyncTask();
                    chocolateAsyncTask.execute(chocolateURL);

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
                    // TODO: Fix crash on cancel.

                    ItemAsyncTask cerealAsyncTask = new ItemAsyncTask();
                    cerealAsyncTask.cancel(true);

                    ItemAsyncTask chocolateAsyncTask = new ItemAsyncTask();
                    chocolateAsyncTask.cancel(true);

                    ItemAsyncTask teaAsyncTask = new ItemAsyncTask();
                    teaAsyncTask.execute(teaURL);

                } else {
                    // the connection is not available
                    Toast.makeText(MainActivity.this, "No connection :(", Toast.LENGTH_SHORT).show();
                }
            }
        });


/*
            mCereal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                        if (networkInfo != null && networkInfo.isConnected()) {

                            // TODO: Fix crash on cancel. Check if this is only with no connection.

                            chocolateAsyncTask.cancel(true);
                            teaAsyncTask.cancel(true);
                            if (chocolateAsyncTask.isCancelled() || teaAsyncTask.isCancelled()) {
                                cerealAsyncTask.execute(cerealURL);
                            } else {
                                Toast.makeText(MainActivity.this, "Previous task still running", Toast.LENGTH_SHORT).show();
                            }
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

                            // TODO: Fix crash on cancel.

                            teaAsyncTask.cancel(true);
                            cerealAsyncTask.cancel(true);

                            if (teaAsyncTask.isCancelled() || cerealAsyncTask.isCancelled()) {
                                chocolateAsyncTask.execute(chocolateURL);
                            } else {
                                Toast.makeText(MainActivity.this, "Previous task still running", Toast.LENGTH_SHORT).show();
                            }
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
                        // TODO: Fix crash on cancel.
                        try {
                            cerealAsyncTask.cancel(true);
                        }  catch (Throwable thr) {
                             thr.printStackTrace();
                        }

                        try {
                            chocolateAsyncTask.cancel(true);
                        }  catch (Throwable thr) {
                            thr.printStackTrace();
                        }

                        if (chocolateAsyncTask.isCancelled() || cerealAsyncTask.isCancelled()) {
                            teaAsyncTask.execute(teaURL);
                        } else {
                            Toast.makeText(MainActivity.this, "Previous task still running", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    // the connection is not available
                    Toast.makeText(MainActivity.this, "No connection :(", Toast.LENGTH_SHORT).show();
                    }
                }
            });


/*
           mCereal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if (networkInfo != null && networkInfo.isConnected()) {
                            // TODO: Fix crash on cancel. Error: task has already been executed
                            itemAsyncTask.cancel(true);
                            if (itemAsyncTask.isCancelled()) {
                                itemAsyncTask.execute(cerealURL);
                            }
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
                            // TODO: Fix crash on cancel.
                            itemAsyncTask.cancel(true);
                            if (itemAsyncTask.isCancelled()) {
                                itemAsyncTask.execute(chocolateURL);
                            }
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
                        // TODO: Fix crash on cancel.
                        itemAsyncTask.cancel(true);
                        if (itemAsyncTask.isCancelled()) {
                            itemAsyncTask.execute(teaURL);
                        }
                    } else {
                    // the connection is not available
                    Toast.makeText(MainActivity.this, "No connection :(", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
 */



    }

    private class ItemAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... myUrl) {
            String contentAsString = "";
            InputStream is = null;
            try {
                URL url = new URL(myUrl[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                // Starts the query
                conn.connect();
                is = conn.getInputStream();

                // Converts the InputStream into a string
                contentAsString = readIt(is);
                return contentAsString;

            } catch (Throwable thr) {
                thr.printStackTrace();
            }

            try {
                // Get the data inside the JSON object, and the data inside the object's array.
                JSONObject dataObject = new JSONObject(contentAsString); // Could take no params, or could take the string you want to use.
                JSONObject cerealObject = dataObject.getJSONObject("query");
                JSONArray cerealJsonArray = cerealObject.getJSONArray("items"); // Getting the array gets the stuff inside the object.

                mArrayList.clear();

                // For every object in the item array, add the name to the ArrayList.
                for (int i = 0; i < cerealJsonArray.length(); i++) {
                    JSONObject object = cerealJsonArray.optJSONObject(i);
                    String name = object.optString("name");
                    mArrayList.add(name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
            if (is != null) {
                try {
                    is.close();
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
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    public String readIt(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String read;

        while((read = br.readLine()) != null) {
            sb.append(read);
        }
        return sb.toString();
    }

}
