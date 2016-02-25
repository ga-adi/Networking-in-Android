package lab.imaginenat.com.walmartlab;

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

    private ListView mTheListView;
    private ArrayList<String> mStringArray;
    private ArrayAdapter<String> mArrayAdapter;
    DownloadWalmartStuff walmartStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button cerealButton = (Button)findViewById(R.id.cerealButton);
        cerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"MainActivity",Toast.LENGTH_SHORT).show();
                if(walmartStuff!=null && walmartStuff.getStatus() != AsyncTask.Status.FINISHED){
                    walmartStuff.cancel(true);
                }
                walmartStuff = new DownloadWalmartStuff();
                String searchForThis = "cereal";
                walmartStuff.execute("http://api.walmartlabs.com/v1/search?query="+searchForThis+"&format=json&apiKey=neepzzwncp3fzee897mcxzz6");

            }
        });

        Button chocolateButton=(Button)findViewById(R.id.chocolateButton);
        chocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walmartStuff!=null && walmartStuff.getStatus() != AsyncTask.Status.FINISHED){
                    walmartStuff.cancel(true);
                }
                walmartStuff = new DownloadWalmartStuff();
                String searchForThis = "chocolate";
                walmartStuff.execute("http://api.walmartlabs.com/v1/search?query="+searchForThis+"&format=json&apiKey=neepzzwncp3fzee897mcxzz6");

            }
        });
        Button teaButton = (Button) findViewById(R.id.teaButton);
        teaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walmartStuff!=null && walmartStuff.getStatus() != AsyncTask.Status.FINISHED){
                    walmartStuff.cancel(true);
                }
                walmartStuff = new DownloadWalmartStuff();
                String searchForThis = "tea";
                walmartStuff.execute("http://api.walmartlabs.com/v1/search?query="+searchForThis+"&format=json&apiKey=neepzzwncp3fzee897mcxzz6");

            }
        });

        mTheListView = (ListView)findViewById(R.id.ListView);
        mStringArray = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStringArray);
        mTheListView.setAdapter(mArrayAdapter);


    }


    private String getInputData(InputStream inStream) throws IOException{
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        String data;
        while((data=reader.readLine())!=null){
            builder.append(data);
        }
        reader.close();
        return builder.toString();
    }


    private class DownloadWalmartStuff extends AsyncTask<String,Void,String> {
        String data=null;
        @Override
        protected String doInBackground(String... params){
                String theLink=params[0];
                Log.d("MainActivity","link: "+theLink);

            try {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // the connection is available
                } else {
                    // the connection is not available
                    return data;
                }
                URL url = new URL(theLink);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inStream = connection.getInputStream();
                data = getInputData(inStream);


                JSONObject dataObject = new JSONObject(data);
                JSONArray itemsArray = dataObject.getJSONArray("items");
                mStringArray.clear();
                for(int counter=0;counter<itemsArray.length();counter++){
                    JSONObject theObject = itemsArray.optJSONObject(counter);
                    mStringArray.add(theObject.getString("name"));
                    Log.d("MainActivity", theObject.getString("name"));
                }

//                JSONArray photoJSONArray = photoObject.getJSONArray("photo");
//
//                mStringArray.clear();
//                for(int i=0;i<photoJSONArray.length();i++){
//                    JSONObject object = photoJSONArray.optJSONObject(i);
//                    String title = object.optString("title");
//                    mStringArray.add(title);
//                }

            }catch(MalformedURLException e){
                    e.printStackTrace();
            }catch(IOException ex){
                ex.printStackTrace();
            }catch(JSONException je){
                je.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if(data==null){
                Toast.makeText(MainActivity.this,"ERROR: no connectino found ",Toast.LENGTH_SHORT).show();
            }
           mArrayAdapter.notifyDataSetChanged();
        }
    }
}
