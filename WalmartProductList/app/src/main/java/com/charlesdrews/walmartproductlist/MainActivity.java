package com.charlesdrews.walmartproductlist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String WALMART_API_KEY = "6z2n6d5hb74x4eyxxr98km39";
    private static final String WALMART_API_ENDPOINT = "http://api.walmartlabs.com/v1/search";
    private static final String REQUEST_TYPE = "GET";
    private static final String JSON_KEY_ITEMS = "items";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_PRICE = "salePrice";

    // http://api.walmartlabs.com/v1/search?query=tea&format=json&categoryId=976759_976782_1001320&apiKey=6z2n6d5hb74x4eyxxr98km39
    private static final String QUERY_KEY = "query";
    private static final String FORMAT_KEY_AND_VALUE = "format=json";
    private static final String API_KEY_KEY = "apiKey";
    private static final String CATEGORY_ID_KEY = "categoryId";

    private static final String TEA_QUERY = "tea";
    private static final String CHOCOLATE_QUERY = "chocolate";
    private static final String CEREAL_QUERY = "cereal";

    // http://api.walmartlabs.com/v1/taxonomy?format=json&apiKey=6z2n6d5hb74x4eyxxr98km39
    private static final String TEA_CLASS_ID = "976759_976782_1001320";
    private static final String CHOCOLATE_CLASS_ID = "976759_1096070_1224976";
    private static final String CEREAL_CLASS_ID = "976759_976783_1001339";

    private ArrayList<WalmartItem> mItems;
    private ArrayAdapter<WalmartItem> mAdapter;
    private GetWalmartItemsAsyncTask mTask;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mItems = new ArrayList<>();
        mAdapter = new WalmartArrayAdapter();
        ((ListView) findViewById(R.id.list_view)).setAdapter(mAdapter);

        findViewById(R.id.cereal_button).setOnClickListener(MainActivity.this);
        findViewById(R.id.chocolate_button).setOnClickListener(MainActivity.this);
        findViewById(R.id.tea_button).setOnClickListener(MainActivity.this);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cereal_button:
                startWalmartAsyncTask(CEREAL_QUERY, CEREAL_CLASS_ID);
                break;
            case R.id.chocolate_button:
                startWalmartAsyncTask(CHOCOLATE_QUERY, CHOCOLATE_CLASS_ID);
                break;
            case R.id.tea_button:
                startWalmartAsyncTask(TEA_QUERY, TEA_CLASS_ID);
                break;
            default:
                break;
        }
    }

    private void startWalmartAsyncTask(String query, String categoryId) {
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
            mTask.cancel(true);
        }
        mTask = new GetWalmartItemsAsyncTask();
        mTask.execute(query, categoryId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetWalmartItemsAsyncTask extends AsyncTask<String, Void, Void> {
        private String mErrorMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            mProgressBar.setAlpha(0f);
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params.length < 2) {
                mErrorMessage = "Too few arguments provided to perform API call";
                return null;
            }

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // the connection is available

                try {
                    mItems.clear();

                    // http://api.walmartlabs.com/v1/search?query=tea&format=json&categoryId=976759_976782_1001320&apiKey=6z2n6d5hb74x4eyxxr98km39
                    String urlString = WALMART_API_ENDPOINT + "?"
                            + QUERY_KEY + "=" + params[0] // query value
                            + "&" + FORMAT_KEY_AND_VALUE
                            + "&" + CATEGORY_ID_KEY + "=" + params[1] // categoryId value
                            + "&" + API_KEY_KEY + "=" + WALMART_API_KEY;

                    URL url = new URL(urlString);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(REQUEST_TYPE);
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();
                    String dataString = getStringFromInputStream(inputStream);
                    inputStream.close();

                    JSONObject parentJsonObject = new JSONObject(dataString);
                    JSONArray jsonArray = parentJsonObject.optJSONArray(JSON_KEY_ITEMS);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.optJSONObject(i);
                        mItems.add(new WalmartItem(item.optString(JSON_KEY_NAME), item.optDouble(JSON_KEY_PRICE)));
                    }

                    return null;

                } catch(MalformedURLException e) {
                    e.printStackTrace();
                    mErrorMessage = "MalformedURLException: " + e.getMessage();
                } catch(IOException e) {
                    e.printStackTrace();
                    mErrorMessage = "IOException: " + e.getMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mErrorMessage = "JSONException: " + e.getMessage();
                } catch(Exception e) {
                    e.printStackTrace();
                    mErrorMessage = "Other error: " + e.getMessage();
                }
            } else {
                // the connection is not available
                mErrorMessage = "Internet connection not available";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mErrorMessage != null) {
                Log.e(MainActivity.class.getName(), mErrorMessage);
                Toast.makeText(
                        MainActivity.this,
                        "Sorry, there was a problem gathering items",
                        Toast.LENGTH_SHORT
                ).show();
            }

            mAdapter.notifyDataSetChanged();

            mProgressBar.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                         @Override
                         public void onAnimationEnd(Animator animation) {
                             super.onAnimationEnd(animation);
                             mProgressBar.setVisibility(View.GONE);
                         }
                     });
        }

        private String getStringFromInputStream(InputStream stream) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private class WalmartItem {
        private String mName;
        private Double mPrice;

        public WalmartItem(String name, Double price) {
            mName = name;
            mPrice = price;
        }

        public String getName() { return mName; }

        public Double getPrice() { return mPrice; }
    }

    private class WalmartArrayAdapter extends ArrayAdapter<WalmartItem> {

        public WalmartArrayAdapter() {
            super(MainActivity.this, R.layout.list_item, mItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            WalmartItem item = mItems.get(position);
            ((TextView) convertView.findViewById(R.id.item_name)).setText(item.getName());
            ((TextView) convertView.findViewById(R.id.item_price))
                    .setText(String.format("$%,.2f", item.getPrice()));

            return convertView;
        }
    }
}
