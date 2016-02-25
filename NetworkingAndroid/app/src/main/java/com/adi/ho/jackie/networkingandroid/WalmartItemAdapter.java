package com.adi.ho.jackie.networkingandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by JHADI on 2/24/16.
 */
public class WalmartItemAdapter extends ArrayAdapter<WalmartItem> {
    private ArrayList<WalmartItem> walmartList;
    private Context context;
    public WalmartItemAdapter(Context context, ArrayList<WalmartItem> walmartList) {
        super(context, -1);
        this.context = context;
        this.walmartList = walmartList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.custom_list_layout, parent, false);

        TextView itemNameText = (TextView)v.findViewById(R.id.item_name);
        TextView itemPriceText = (TextView)v.findViewById(R.id.item_price);

        itemNameText.setText(walmartList.get(position).getItemName());
        itemPriceText.setText("$"+walmartList.get(position).getItemPrice());

        return v;
    }

    @Override
    public int getCount() {
        return walmartList.size();
    }
}
