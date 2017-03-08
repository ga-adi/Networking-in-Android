package com.williamtygret.tripledouble3;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button mButtonUpdate;

    GameIDDatabaseHelper myDB;

    private ListView mListView;
    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mAdapter;

    private String urlStringGames;
    CursorAdapter mCursorAdapter;
    GameIDDatabaseHelper mGameIDDatabaseHelper;

    private TextView mPoints;
    private TextView mRebounds;
    private TextView mAssists;
    private TextView mAnswer;

    private String mGameID;
    private String mHomeAway;

    private int mId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new GameIDDatabaseHelper(this);

        mListView = (ListView)findViewById(R.id.listView);
        mStringArray = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mStringArray);
        mListView.setAdapter(mAdapter);

        mPoints = (TextView)findViewById(R.id.points);
        mRebounds = (TextView)findViewById(R.id.rebounds);
        mAssists = (TextView)findViewById(R.id.assists);
        mAnswer = (TextView)findViewById(R.id.answer);

//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => " + c.getTime());
//
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//        String formattedDate = df.format(c.getTime());
//        Log.d("date", "the date is: "+ formattedDate);
//
//        Cursor cursor = null;
//        mCursorAdapter = new myAdapter(this, cursor);
//        mListView.setAdapter(mCursorAdapter);

       // GameIDDatabaseHelper.getInstance(MainActivity.this).insertAllGameID();

//        handleIntent(getIntent());


//        int id = getIntent().getIntExtra("id",-1);
//        mId=id;
//        if(id >= 0){
//            GameIDDatabaseHelper dbh = GameIDDatabaseHelper.getInstance(MainActivity.this);
//            String theGameID = dbh.getGameID(id);
//            mGameID.equals(theGameID);
//        }
//        if(id >= 0){
//            GameIDDatabaseHelper dbh = GameIDDatabaseHelper.getInstance(MainActivity.this);
//            String homeAway = dbh.getHomeAway(id);
//            mHomeAway.equals(homeAway);
//        }


        //here is where I need to call method probably on getting todays date, making cursor cursor adapter maybe?
        //use database helper to get gameid make a string
        //create an if statement to catch if there is no game and cancel the call and change type to no game today

        urlStringGames = "http://api.sportradar.us/nba-t3/games/"+"fb60c3d8-3feb-4ca7-8cd5-045f2e6a4c22"+"/boxscore.json?api_key=q297t7wfs2knu5nqkbybeksd";

        mButtonUpdate = (Button)findViewById(R.id.button);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadAsyncStats downloadAsyncStats = new DownloadAsyncStats();
                downloadAsyncStats.execute(urlStringGames);
            }
        });


    }

//    private class SearchDatabaseAsync extends AsyncTask<Void,Void,Void> {
//
//        Cursor curse;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            GameIDDatabaseHelper dbh = GameIDDatabaseHelper.getInstance(MainActivity.this);
//            curse = dbh.getGameID();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            mCursorAdapter.swapCursor(curse);
//            mTextView.setText("My Pokemon:");
//            mFavoritesButton.setVisibility(View.GONE);
//            super.onPostExecute(aVoid);
//
//        }
//
//    }

    public class DownloadAsyncStats extends AsyncTask<String, Void, String> {

        String data;
        int points;
        int rebounds;
        int assists;
        boolean tripdoub = false;

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
                JSONObject away = dataObject.getJSONObject("away");
                Log.d("away", "away team is: "+away);
                JSONObject leaders = away.getJSONObject("leaders");
                Log.d("lead","leaders are: "+leaders);
                JSONArray assistLeaders = leaders.getJSONArray("points");
                Log.d("assist","assist leader is: "+assistLeaders);
                for(int i=0;i<assistLeaders.length();i++){
                    JSONObject stats = assistLeaders.getJSONObject(i);

                    JSONObject statistics = stats.getJSONObject("statistics");
                    Log.d("stats", "russ stats: " + statistics);
                    points = statistics.getInt("points");
                    Log.d("points","Russell points; "+points);
                    assists = statistics.getInt("assists");
                    rebounds = statistics.getInt("rebounds");
                    Log.d("pra","Points: " +points+ " Rebounds: "+rebounds+ " Assists: "+assists);

                }

                mStringArray.clear();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;

        }



        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            mAdapter.notifyDataSetChanged();
            mPoints.setText("Points: "+points);
            mRebounds.setText("Rebounds: "+rebounds);
            mAssists.setText("Assists: " + assists);
            if(points >= 10 && rebounds>=10 &&assists>=10){
                tripdoub = true;
                if(tripdoub = true){
                    mAnswer.setText("FUCK YEAH!!!");
                }
            }

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

//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }
//
//
//    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
//            // where you do the actual database search
//
//            Calendar c = Calendar.getInstance();
//            System.out.println("Current time => " + c.getTime());
//
//            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//            String formattedDate = df.format(c.getTime());
//            Log.d("date", "the date is: "+ formattedDate);
//
//            String query = intent.getStringExtra(formattedDate);
//
//            // SELECT * FROM awesometable WHERE name = ? (or WHERE name LIKE ?)
//            Cursor cursor = mGameIDDatabaseHelper.getInstance(this).searchDate(formattedDate);
//            Log.d("cursor","cursor: "+cursor);
//
//            mCursorAdapter.swapCursor(cursor);
//        }
//    }



}
