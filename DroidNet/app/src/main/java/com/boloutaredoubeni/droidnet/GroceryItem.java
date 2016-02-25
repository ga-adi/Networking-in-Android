package com.boloutaredoubeni.droidnet;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class GroceryItem {

  private String mName;
  private double mPrice;

  public GroceryItem(String name, double price) {
    mName = name;
    mPrice = price;
  }

  public String getName() {
    return mName;
  }

  public double getPrice() {
    return mPrice;
  }

  @Override
  public String toString() {
    return String.format("%s: $%.2f", mName, mPrice);
  }
}
