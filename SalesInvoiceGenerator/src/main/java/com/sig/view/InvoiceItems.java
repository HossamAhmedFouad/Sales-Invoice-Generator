//EGY FWD - Front End Testing Nano Degree Program - Project 1 May Cohort - 2022
//Program Name : InvoiceItems.java
//Last Modification Date: 30/05/2022
//Author: Hossam Ahmed Fouad
//Version: 2.0
//Purpose: Serves The View Part In The Model View Control (MVC) Design for SIG - Invoice Items Table Design


package com.sig.view;
public class InvoiceItems {
    /**
     * Class Instances
     */
    private String[] cols = {"No.","Item Name","Item Price","Count","Item Total"};
    private String[][]data = {};

    /**
     * Getters and setters
     */
    public String[] getCols() {
        return cols;
    }

    public void setCols(String[] cols) {
        this.cols = cols;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

}
