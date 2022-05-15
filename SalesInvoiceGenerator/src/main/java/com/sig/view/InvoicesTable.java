//EGY FWD - Front End Testing Nano Degree Program - Project 1 May Cohort - 2022
//Program Name : GUI.java
//Last Modification Date: 11/05/2022
//Author: Hossam Ahmed Fouad
//Version: 1.0
//Purpose: Serves The View Part In The Model View Control (MVC) Design for SIG - Invoice Header Table Design

package com.sig.view;
import com.sig.model.InvoiceHeader;
import javax.swing.*;
import java.util.ArrayList;

public class InvoicesTable extends JTable{
    /**
     * Class Instances
     */

    private String[] cols = {"No.","Date","Customer","Total"};
    private String[][] data ={};
    private ArrayList<InvoiceHeader>invoiceHeaders;

    /**
     * Getters And Setters
     */
    public String[] getCols() {
        return cols;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }
}
