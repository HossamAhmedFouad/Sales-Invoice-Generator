//EGY FWD - Front End Testing Nano Degree Program - Project 1 May Cohort - 2022
//Program Name : FileOperations.java
//Last Modification Date: 11/05/2022
//Author: Hossam Ahmed Fouad
//Version: 1.0
//Purpose: Serves The Model Part In The Model View Control (MVC) Design for SIG - Handling File Operations

package com.sig.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperations {
    /**
     * Class Instances
     */
    private String data;
    private InvoiceHeader invoiceHeader;
    private InvoiceLine invoiceLine;

    private ArrayList<InvoiceHeader> invoiceHeaderArrayList;
    private ArrayList<String>dataFields;

    public ArrayList<InvoiceHeader> getInvoiceHeaderArrayList() {
        return invoiceHeaderArrayList;
    }
    /**
     * File Operations Methods
     */

    //Reads file from resources package to initialize the invoice table on starting the program with data.
    public ArrayList<InvoiceHeader> readFile(){
        dataFields = new ArrayList<>(0);
        invoiceHeaderArrayList = new ArrayList<>(0);

        //Reading from invoice header file
        try {
            //Get the project directory from system then start reading from resources package
            Scanner scanner = new Scanner(new File(System.getProperty("user.dir")+"\\src\\main\\java\\com\\sig\\resources\\InvoiceHeader.csv"));
            scanner.useDelimiter(",|\\n");
            ArrayList<String>line = new ArrayList<>(0);
            //Read data separated by a "," and 3 data items per line: number - date - customer name
            int counter = 0;
            while (scanner.hasNext()){
                line.add(scanner.next());
                counter+=1;
                if(counter==3){
                    invoiceHeader = new InvoiceHeader();
                    invoiceHeader.setInvoiceNumber(line.get(0));
                    invoiceHeader.setInvoiceDate(line.get(1));
                    invoiceHeader.setCustomerName(line.get(2));
                    invoiceHeaderArrayList.add(invoiceHeader);
                    line.clear();
                    counter=0;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File Not Found");
        }
        //Reading from invoice line file
        try {
            //Get the project directory from system then start reading from resources package
            Scanner scanner = new Scanner(new File(System.getProperty("user.dir")+"\\src\\main\\java\\com\\sig\\resources\\InvoiceLine.csv"));
            scanner.useDelimiter(",|\\n");
            ArrayList<InvoiceLine>lineArrayList = new ArrayList<>(0);
            ArrayList<String>lineData = new ArrayList<>(0);
            int counter = 0;
            int invNum = 0;
            invoiceLine = new InvoiceLine();
            //Start reading invoice line and separate data into 4 sections
            //And Loop on the headers and add each line to their header depending on invoice number
            for (int i=0;i<invoiceHeaderArrayList.size();i++){
                while (scanner.hasNext()){
                    lineData.add(scanner.next());
                    counter+=1;
                    if (counter==4){
                        //Start watching invoice numbers to know when to switch to a new header
                        if (invNum == 0){
                            invNum = Integer.parseInt(lineData.get(0));
                            invoiceLine = new InvoiceLine();
                            invoiceLine.setItemName(lineData.get(1));
                            invoiceLine.setItemPrice(lineData.get(2));
                            invoiceLine.setCount(lineData.get(3));
                            lineArrayList.add(invoiceLine);
                            counter = 0;
                            lineData.clear();
                        }else {
                            //If invoice number changes, increment to next invoice header and add the invoice line to it
                            if(invNum!=Integer.parseInt(lineData.get(0))){
                                invoiceHeaderArrayList.get(i).setInvoiceLines(lineArrayList);
                                i+=1;
                                invNum = Integer.parseInt(lineData.get(0));
                                lineArrayList.clear();
                                invoiceLine = new InvoiceLine();
                                invoiceLine.setItemName(lineData.get(1));
                                invoiceLine.setItemPrice(lineData.get(2));
                                invoiceLine.setCount(lineData.get(3));
                                lineArrayList.add(invoiceLine);
                                lineData.clear();
                                counter = 0;
                            }else{
                                invoiceLine = new InvoiceLine();
                                invoiceLine.setItemName(lineData.get(1));
                                invoiceLine.setItemPrice(lineData.get(2));
                                invoiceLine.setCount(lineData.get(3));
                                lineArrayList.add(invoiceLine);
                                lineData.clear();
                                counter=0;
                            }
                        }
                    }
                }
            }
            try{
                invoiceHeaderArrayList.get(invoiceHeaderArrayList.size()-1).setInvoiceLines(lineArrayList);
            }catch (Exception e){
                System.out.println("Empty file");
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File Not Found");
        }

        return invoiceHeaderArrayList;
    }
    //Overriding the readFile function to be able to take the paths from user selection
    //and not the default resources package
    public ArrayList<InvoiceHeader> readFile(String path1 , String path2){
        dataFields = new ArrayList<>(0);
        invoiceHeaderArrayList = new ArrayList<>(0);

        //Start reading from given path and do the same readFile function without parameters
        try {
            //Path 1 refers to the invoice header file selected by the user
            Scanner scanner = new Scanner(new File(path1));
            scanner.useDelimiter(",|\\n");
            ArrayList<String>line = new ArrayList<>(0);
            int counter = 0;
            while (scanner.hasNext()){
                line.add(scanner.next());
                counter+=1;
                if(counter==3){
                    invoiceHeader = new InvoiceHeader();
                    invoiceHeader.setInvoiceNumber(line.get(0));
                    invoiceHeader.setInvoiceDate(line.get(1));
                    invoiceHeader.setCustomerName(line.get(2));
                    invoiceHeaderArrayList.add(invoiceHeader);
                    line.clear();
                    counter=0;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File Not Found");
        }

        try {
            //Path 2 refers to the invoice line file selected by the user
            Scanner scanner = new Scanner(new File(path2));
            scanner.useDelimiter(",|\\n");
            ArrayList<InvoiceLine>lineArrayList = new ArrayList<>(0);
            ArrayList<String>lineData = new ArrayList<>(0);
            int counter = 0;
            int invNum = 0;
            invoiceLine = new InvoiceLine();
            for (int i=0;i<invoiceHeaderArrayList.size();i++){
                while (scanner.hasNext()){
                    lineData.add(scanner.next());
                    counter+=1;
                    if (counter==4){
                        if (invNum == 0){
                            invNum = Integer.parseInt(lineData.get(0));
                            invoiceLine = new InvoiceLine();
                            invoiceLine.setItemName(lineData.get(1));
                            invoiceLine.setItemPrice(lineData.get(2));
                            invoiceLine.setCount(lineData.get(3));
                            lineArrayList.add(invoiceLine);
                            counter = 0;
                            lineData.clear();
                        }else {
                            if(invNum!=Integer.parseInt(lineData.get(0))){
                                invoiceHeaderArrayList.get(i).setInvoiceLines(lineArrayList);
                                i+=1;
                                invNum = Integer.parseInt(lineData.get(0));
                                lineArrayList.clear();
                                invoiceLine = new InvoiceLine();
                                invoiceLine.setItemName(lineData.get(1));
                                invoiceLine.setItemPrice(lineData.get(2));
                                invoiceLine.setCount(lineData.get(3));
                                lineArrayList.add(invoiceLine);
                                lineData.clear();
                                counter = 0;
                            }else{
                                invoiceLine = new InvoiceLine();
                                invoiceLine.setItemName(lineData.get(1));
                                invoiceLine.setItemPrice(lineData.get(2));
                                invoiceLine.setCount(lineData.get(3));
                                lineArrayList.add(invoiceLine);
                                lineData.clear();
                                counter=0;
                            }
                        }
                    }
                }
            }
            invoiceHeaderArrayList.get(invoiceHeaderArrayList.size()-1).setInvoiceLines(lineArrayList);
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File Not Found");
        }

        return invoiceHeaderArrayList;
    }
    //Writing to file found in resource package which is the default location
    public void writeFile(ArrayList<InvoiceHeader>invoiceHeader){
        //Write headers to invoice header file in resources package
        try {
            FileWriter myWriter = new FileWriter(System.getProperty("user.dir")+"\\src\\main\\java\\com\\sig\\resources\\InvoiceHeader.csv");
            for(InvoiceHeader header : invoiceHeader){
                myWriter.write(header.getInvoiceNumber() + "," + header.getInvoiceDate() + "," + header.getCustomerName()+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("File Not Found");
        }
        //Write headers' invoice lines to invoice line file in resources package
        try {
            FileWriter myWriter = new FileWriter(System.getProperty("user.dir")+"\\src\\main\\java\\com\\sig\\resources\\InvoiceLine.csv");
            for(InvoiceHeader header : invoiceHeader){
                for (InvoiceLine line : header.getInvoiceLines()){
                    myWriter.write(header.getInvoiceNumber()+","+line.getItemName()+","+line.getItemPrice()+","+line.getCount()+"\n");
                }
            }
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("File Not Found");
        }
    }
    //Overriding writeFile function to write to file destinations provided by the user.
    public void writeFile(ArrayList<InvoiceHeader>invoiceHeader,String path1,String path2){
        try {
            //path 1 refers to invoice header file destination
            FileWriter myWriter = new FileWriter(path1);
            for(InvoiceHeader header : invoiceHeader){
                myWriter.write(header.getInvoiceNumber() + "," + header.getInvoiceDate() + "," + header.getCustomerName()+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("File Not Found");
        }

        try {
            //path 2 refers to invoice line file destination
            FileWriter myWriter = new FileWriter(path2);
            for(InvoiceHeader header : invoiceHeader){
                for (InvoiceLine line : header.getInvoiceLines()){
                    myWriter.write(header.getInvoiceNumber()+","+line.getItemName()+","+line.getItemPrice()+","+line.getCount()+"\n");
                }
            }
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("File Not Found");
        }
    }

    /*
    TESTING
     */
    public static void main(String[] args) {
        FileOperations fileOperations = new FileOperations();
        ArrayList<InvoiceHeader>testing = fileOperations.readFile();
        for (InvoiceHeader header : testing){
            System.out.println("Invoice Number: " + header.getInvoiceNumber());
            System.out.println("{");
            System.out.println(header.getInvoiceDate() + " , " + header.getCustomerName());
            for (InvoiceLine line : header.getInvoiceLines()){
                System.out.println(line.getItemName() + ", " + line.getItemPrice() + ", " + line.getCount());
            }
            System.out.println("}");
            System.out.println();
            System.out.println();
        }

    }

}
