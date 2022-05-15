//EGY FWD - Front End Testing Nano Degree Program - Project 1 May Cohort - 2022
//Program Name : InvoiceHeader.java
//Last Modification Date: 11/05/2022
//Author: Hossam Ahmed Fouad
//Version: 1.0
//Purpose: Serves The Model Part In The Model View Control (MVC) Design for SIG - Invoice Header Model


package com.sig.model;

import java.util.ArrayList;

public class InvoiceHeader {

    /**
     * Invoice Header Instances
     */
    private String invoiceNum, invoiceDate, customerName;


    private ArrayList<InvoiceLine> invoiceLines;


    /**
     * Getter And Setter Methods
     */

    public String getInvoiceNumber() {
        return invoiceNum;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNum = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }
    public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        ArrayList<InvoiceLine>invoiceLinesDublicate = new ArrayList<>(0);
        invoiceLinesDublicate.addAll(invoiceLines);
        this.invoiceLines = invoiceLinesDublicate;
    }
}
