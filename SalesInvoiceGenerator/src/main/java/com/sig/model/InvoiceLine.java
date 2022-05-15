//EGY FWD - Front End Testing Nano Degree Program - Project 1 May Cohort - 2022
//Program Name : InvoiceLine.java
//Last Modification Date: 11/05/2022
//Author: Hossam Ahmed Fouad
//Version: 1.0
//Purpose: Serves The Model Part In The Model View Control (MVC) Design for SIG - Invoice Line Model

package com.sig.model;

public class InvoiceLine {
    /**
     * Class Instances
     */
    private String itemName;
    private String itemPrice,count;

    /**
     * Getters and setters
     */
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
