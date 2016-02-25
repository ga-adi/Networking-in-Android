package com.adi.ho.jackie.networkingandroid;

/**
 * Created by JHADI on 2/24/16.
 */
public class WalmartItem {
    private String itemName;
    private String itemPrice;

    public WalmartItem(String itemName, String itemPrice){
        this.itemName = itemName;
        this.itemPrice = itemPrice;

    }
    public String getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }
}
