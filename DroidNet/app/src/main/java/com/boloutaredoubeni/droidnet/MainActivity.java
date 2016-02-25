package com.boloutaredoubeni.droidnet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private Button mTeaButton;
  private Button mChocolateButton;
  private Button mCerealButton;
  private ListView mListView;

  private ArrayAdapter<GroceryItem>  mAdapter;
  private WalmartAPITask mTask;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (!networkIsAvailable()) {
      // TODO: create dialog
      System.exit(-1);
    }

    mTeaButton = (Button) findViewById(R.id.tea_btn);
    mTeaButton.setOnClickListener(new ProductButtonListener(mTeaButton.getText().toString().toLowerCase()));
    mCerealButton = (Button) findViewById(R.id.cereal_btn);
    mCerealButton.setOnClickListener(new ProductButtonListener(mCerealButton.getText().toString().toLowerCase()));
    mChocolateButton = (Button) findViewById(R.id.chocolate_btn);
    mChocolateButton.setOnClickListener(new ProductButtonListener(mChocolateButton.getText().toString().toLowerCase()));
    mListView = (ListView) findViewById(R.id.product_list);

    if (!networkIsAvailable()) {
      // TODO: create dialog
      System.exit(-1);
    }

    mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<GroceryItem>());
    mListView.setAdapter(mAdapter);

    mTask = new WalmartAPITask();
  }

  private boolean networkIsAvailable() {
    ConnectivityManager manager = (ConnectivityManager) getSystemService(/* Context */ CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = manager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }

  class WalmartAPITask extends AsyncTask<String, Void, ArrayList<GroceryItem>> {

    @Override
    protected ArrayList<GroceryItem> doInBackground(String... params) {
      // TODO: do the thing
      String query = params[0];
      String response = WalmartAPIClient.getInstance().connectToServer(query);
      if (response == null) {
        return null;
      }
      return  WalmartAPIClient.getInstance().parseJSON(response);
    }

    @Override
    protected void onPostExecute(ArrayList<GroceryItem> result) {
      // TODO: add the data here
      // Assign the data to data list
      mAdapter.clear();
      mAdapter.addAll(result);
      mAdapter.notifyDataSetChanged();
    }
  }

  class ProductButtonListener implements View.OnClickListener {

    private String mType;

    ProductButtonListener(String buttonText) {
      mType = buttonText;
    }

    @Override
    public void onClick(View v) {
      if (mTask.getStatus() != AsyncTask.Status.FINISHED){
        mTask.cancel(true);
      }
      mTask = new WalmartAPITask();
      mTask.execute(mType);
    }
  }
}
