package com.brprog.networkinginandroidhomework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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
        mCerealArrayList.add("Rice Krispies");
        mCerealArrayList.add("Special K");

        mChocolateArrayList = new ArrayList<>();
        mChocolateArrayList.add("Snickers Bar");
        mChocolateArrayList.add("Milkyway Bar");

        mTeaArrayList = new ArrayList<>();
        mTeaArrayList.add("Chamomile");
        mTeaArrayList.add("Citrus");

        mCerealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mCerealArrayList);
                mProductsListView.setAdapter(mProductsAdapter);
            }
        });

        mChocolateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mChocolateArrayList);
                mProductsListView.setAdapter(mProductsAdapter);
            }
        });

        mTeaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mTeaArrayList);
                mProductsListView.setAdapter(mProductsAdapter);
            }
        });

    }
}
