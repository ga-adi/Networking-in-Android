package roberterrera.com.networkinglab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button mCereal, mChocolate, mTea;
    ListView mListView;
    ArrayList<String> mArrayList;
    ArrayAdapter<String> mArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCereal = (Button) findViewById(R.id.button_cereal);
        mChocolate = (Button) findViewById(R.id.button_chocolate);
        mTea = (Button) findViewById(R.id.button_tea);
        mListView = (ListView) findViewById(R.id.listview);
        mArrayList = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, mArrayList);
        mListView.setAdapter(mArrayAdapter);

        

    }
}
