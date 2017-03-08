package com.williamtygret.tripledouble2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button mButtonUpdate;
    private ListView mListView;
    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mAdapter;

    private String urlStringGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.listView);
        mStringArray = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mStringArray);
        mListView.setAdapter(mAdapter);

        urlStringGames = "http://api.sportradar.us/nba-t3/games/2016/12/27/schedule.json?api_key=q297t7wfs2knu5nqkbybeksd";

        mButtonUpdate = (Button)findViewById(R.id.button);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncWalmart downloadAsyncWalmart = new DownloadAsyncWalmart();
                downloadAsyncWalmart.execute(urlStringGames);
            }
        });

    }


    public class DownloadAsyncWalmart extends AsyncTask<String, Void, String> {

        String data;

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inStream = connection.getInputStream();

                data = getInputData(inStream);
                Log.d("StupidGuy", "we got the data " + data);

            } catch (Throwable thro) {
                thro.printStackTrace();
            }


            try {
                JSONObject dataObject = new JSONObject(data);
                JSONArray itemsArray = dataObject.getJSONArray("games");
                mStringArray.clear();

                for(int counter=0;counter<itemsArray.length();counter++){
                    JSONObject jsonGames = itemsArray.getJSONObject(counter);
                    mStringArray.add(jsonGames.getString("id"));
                    JSONObject away = jsonGames.getJSONObject("away");
                    Object alias = away.get("alias");
                    ArrayList<Object> aliasArray = new ArrayList();
                    aliasArray.add(alias);
                    Log.d("alias", "we got: " + aliasArray);
                       int okcPosition =  aliasArray.indexOf("OKC");
                    Log.d("position","the position is: "+okcPosition);



                    //implement if position = 0 get

                }
                //research iterators


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            mAdapter.notifyDataSetChanged();

        }

        private String getInputData(InputStream inStream) throws IOException {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

            String data;

            while((data = reader.readLine()) != null){
                builder.append(data);
            }

            reader.close();

            return builder.toString();
        }
    }

}
