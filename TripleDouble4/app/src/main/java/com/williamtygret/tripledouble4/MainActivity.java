package com.williamtygret.tripledouble4;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    GameIDDatabaseHelper myDB;

    private TextView mPoints;
    private TextView mRebounds;
    private TextView mAssists;
    private TextView mAnswer;
    private TextView mTitle;
    private ImageView mWestbrookImg;
    private ImageView mStunner;

    private String urlStringGames;
    ImageView backgroundImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPoints = (TextView)findViewById(R.id.points);
        mRebounds = (TextView)findViewById(R.id.rebounds);
        mAssists = (TextView)findViewById(R.id.assists);
        mAnswer = (TextView)findViewById(R.id.answer);
        mTitle = (TextView)findViewById(R.id.title);
        mWestbrookImg = (ImageView)findViewById(R.id.imageView2);
        mStunner = (ImageView)findViewById(R.id.stunnerImage);

        ReplaceFont.replaceDefaultFont(this, "DEFAULT", "PixelFJVerdana12pt.ttf");
        Typeface myTypeface = Typeface.createFromAsset(getAssets(),"PixelFJVerdana12pt.ttf");
        mAnswer.setTypeface(myTypeface);
        mAssists.setTypeface(myTypeface);
        mRebounds.setTypeface(myTypeface);
        mPoints.setTypeface(myTypeface);
        mTitle.setTypeface(myTypeface);

        mWestbrookImg.isClickable();


        setTitle("");


         backgroundImg = (ImageView)findViewById(R.id.imageView2);
        backgroundImg.setImageResource(R.drawable.russellwestbrook);


        myDB = new GameIDDatabaseHelper(this);

        myDB.insertAllGameID();
        Log.d("myDB", "this is the database: " + myDB.getGameID(3));

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedDate = df.format(c.getTime());
        Log.d("date", "the date is: " + formattedDate);


        Cursor theDate =myDB.searchDate(formattedDate);
        Log.d("grabthedate","we got: "+theDate);

        //todo dont need to do but signifying this gets the db info
        final String gameID = myDB.getGameID();
        Log.d("ooooo","game id is: "+gameID);

        String homeAway = myDB.getHomeAway();
        Log.d("ooooo2","homeaway is: "+homeAway);

        urlStringGames = "http://api.sportradar.us/nba-t3/games/"+gameID+"/boxscore.json?api_key=q297t7wfs2knu5nqkbybeksd";


        if(gameID == ""){
            mAnswer.setText("No Game Today...");
        }else {
            mWestbrookImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gameID == null) {
                       // mAnswer.setText("No Game Today");
                    } else {
                        DownloadAsyncStats downloadAsyncStats = new DownloadAsyncStats();
                        downloadAsyncStats.execute(urlStringGames);
                    }
                }
            });

        }

    }



    public class DownloadAsyncStats extends AsyncTask<String, Void, String> {

        String data;
        int points;
        int rebounds;
        int assists;
        boolean tripdoub = false;

        String homeAway = myDB.getHomeAway();

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
                JSONObject away = dataObject.getJSONObject(homeAway);
                Log.d("away", "away team is: " + away);
                JSONObject leaders = away.getJSONObject("leaders");
                Log.d("lead", "leaders are: " + leaders);
                JSONArray assistLeaders = leaders.getJSONArray("assists");
                Log.d("assist", "assist leader is: " + assistLeaders);
                if (assistLeaders.toString().contains("Russell Westbrook")) {
                    for (int i = 0; i < assistLeaders.length(); i++) {
                        JSONObject stats = assistLeaders.getJSONObject(i);
                        JSONObject statistics = stats.getJSONObject("statistics");
                        Log.d("stats", "russ stats: " + statistics);
                        points = statistics.getInt("points");
                        Log.d("points", "Russell points; " + points);
                        assists = statistics.getInt("assists");
                        rebounds = statistics.getInt("rebounds");
                        Log.d("pra", "Points: " + points + " Rebounds: " + rebounds + " Assists: " + assists);
                     }
                }else{
                    JSONArray pointsLeaders = leaders.getJSONArray("points");
                    Log.d("points","points leader is: "+pointsLeaders);
                    for(int j =0; j<pointsLeaders.length();j++){
                        JSONObject stats = pointsLeaders.getJSONObject(j);
                        JSONObject statistics = stats.getJSONObject("statistics");
                        Log.d("stats", "russ stats: " + statistics);
                        points = statistics.getInt("points");
                        Log.d("points", "Russell points; " + points);
                        assists = statistics.getInt("assists");
                        rebounds = statistics.getInt("rebounds");
                        Log.d("pra", "Points: " + points + " Rebounds: " + rebounds + " Assists: " + assists);
                    }
                }

                //mStringArray.clear();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;

        }



        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            mPoints.setText("Points: "+points);
            mRebounds.setText("Rebounds: "+rebounds);
            mAssists.setText("Assists: " + assists);
            if(points >= 10 && rebounds>=10 &&assists>=10){
                tripdoub = true;
                if(tripdoub = true){
                    mAnswer.setText("FUCK YEAH!!!");
                    backgroundImg.setImageResource(R.drawable.westbrook8bit2);
                    mStunner.setVisibility(View.VISIBLE);
                    final Animation stunnerAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.stunner);
                    mStunner.startAnimation(stunnerAnim);
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



}
