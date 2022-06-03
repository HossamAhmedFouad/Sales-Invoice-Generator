//EGY FWD - Front End Testing Nano Degree Program - Project 1 May Cohort - 2022
//Program Name : ActionsHandler.java
//Last Modification Date: 3/06/2022
//Author: Hossam Ahmed Fouad
//Version: 4.0
//Purpose: Serves The Controller Part In The Model View Control (MVC) Design for SIG

package com.sig.controller;
import com.sig.model.FileOperations;
import com.sig.model.InvoiceHeader;
import com.sig.model.InvoiceLine;
import com.sig.view.GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.*;

public class ActionsHandler {
    /*
    Class Instances
     */
    private FileOperations fileOperations;
    private GUI gui;
    private ArrayList<InvoiceHeader> invoiceHeaderArrayList, invoiceHeaderArrayListDuplicate;


    /*
    Constructor
     */
    public ActionsHandler(FileOperations fileOperations, GUI gui) {
        this.fileOperations = fileOperations;
        this.gui = gui;

        //On Creating New Controller (ActionsHandler) Object.
        invoiceHeaderArrayList = new ArrayList<>(0);
        invoiceHeaderArrayList = fileOperations.readFile();


        //Create Duplicate Array List to store original copy of headers data before saving.
        invoiceHeaderArrayListDuplicate = new ArrayList<>(0);
        //If statement checking whether the file is empty or not.
        if (invoiceHeaderArrayList != null) {
            invoiceHeaderArrayListDuplicate.addAll(invoiceHeaderArrayList);
            //Creating data structure to hold invoice header contents with number of columns of 4
            //And number of rows equals to the size of the invoice header array list which is the number of
            //invoices

            String[][] data = new String[invoiceHeaderArrayList.size()][4];
            double sum = 0;
            for (int i = 0; i < invoiceHeaderArrayList.size(); i++) {
                sum = 0;
                data[i][0] = invoiceHeaderArrayList.get(i).getInvoiceNumber();
                data[i][1] = invoiceHeaderArrayList.get(i).getInvoiceDate();
                data[i][2] = invoiceHeaderArrayList.get(i).getCustomerName();
                StringBuilder str;

                for (InvoiceLine line : invoiceHeaderArrayList.get(i).getInvoiceLines()) {
                    str = new StringBuilder(line.getCount());
                    sum += Double.parseDouble(str.toString()) * Double.parseDouble(line.getItemPrice());
                }
                data[i][3] = Double.toString(sum);
            }
            DefaultTableModel myModel = gui.getInvoicesTableModel();

            //Adding rows in invoices table to match new additional invoices
            while (data.length > myModel.getRowCount()) {
                myModel.addRow(new Object[]{"", "", "", ""});
            }
            //Updating cells values in table
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < 4; j++) {
                    myModel.setValueAt(data[i][j], i, j);
                }
            }
            //Applying new model to table
            this.gui.setInvoicesTableModel(myModel);
            this.gui.getInvoicesHeadersTable().revalidate();
        }

        /*
         * Adding Action Listener (ButtonsListener) to all buttons
         */
        this.gui.addCreateNewInvoiceBtnActionListener(new ButtonsListener());
        this.gui.addDeleteInvoiceBtnActionListener(new ButtonsListener());
        this.gui.addSaveBtnActionListener(new ButtonsListener());
        this.gui.addCancelBtnActionListener(new ButtonsListener());
        this.gui.setAddItemBtnActionListener(new ButtonsListener());
        this.gui.setLoadFileMenuItemActionListener(new ButtonsListener());
        this.gui.setSaveFileMenuItemActionListener(new ButtonsListener());

        /*
         * Adding Selection Listener for invoices table
         */
        this.gui.addInvoicesHeaderTableSelectionListener(new ListListener());

    }

    /*
     * ButtonsListener Class
     */
    class ButtonsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //Clicking On Create New Invoice Button - "create" command
            if (e.getActionCommand().equals("create")) {
                //Getting the model of current invoice header table
                DefaultTableModel myModel = gui.getInvoicesTableModel();

                //On creating a new invoice, the number of new invoice is the increment of the previous
                //invoice number by one
                int previousInvoiceNumber = 0;
                if (invoiceHeaderArrayList != null) {
                    if(invoiceHeaderArrayList.size()>0) {
                        previousInvoiceNumber = Integer.parseInt(invoiceHeaderArrayList.get(invoiceHeaderArrayList.size() - 1).getInvoiceNumber());
                    }
                }else{
                    invoiceHeaderArrayList = new ArrayList<>(0);
                }
                //Adding new row to the current invoice header table model with No. column value set with incremented invoice number
                myModel.addRow(new Object[]{previousInvoiceNumber + 1, "", "", ""});

                //Creating new invoice header object to hold data of the new added invoice
                InvoiceHeader header = new InvoiceHeader();

                //Setting the invoice number value of the currently newly created header object
                header.setInvoiceNumber(String.valueOf(previousInvoiceNumber + 1));
                invoiceHeaderArrayList.add(header);

                //Setting the modified model to the invoice header table
                gui.setInvoicesTableModel(myModel);

                //Clicking On Delete Invoice Button - "delete" command
            } else if (e.getActionCommand().equals("delete")) {
                //Boolean variable storing whether if there is a row selected in the table to be removed
                boolean rowSelected = false;

                //Looping on every row of the invoice header table to check if any is selected.
                for (int i = 0; i < gui.getInvoicesHeadersTable().getRowCount(); i++) {
                    if (gui.getInvoicesHeadersTable().isRowSelected(i)) {
                        //On finding a selected row, make rowSelected true and end the loop.
                        rowSelected = true;
                        break;
                    }
                }
                //Applying deletion on selected row
                if (rowSelected) {
                    if (invoiceHeaderArrayList.size() > 0) {
                        //Deleting the selected invoice header.
                        //Since the invoices are arranged in ascending order, the number of the selected row is the number
                        //Of the invoice to be deleted in 0 indexed array.
                        invoiceHeaderArrayList.remove(gui.getInvoicesHeadersTable().getSelectedRow());
                    }
                    //Getting invoice header table model and remove selected row from it.
                    DefaultTableModel myModel = gui.getInvoicesTableModel();
                    myModel.removeRow(gui.getInvoicesHeadersTable().getSelectedRow());
                    //Setting modified model to table.
                    gui.setInvoicesTableModel(myModel);
                }

                //Clicking On Save Button - "save" command
            } else if (e.getActionCommand().equals("save")) {
                //Setting Date And Customer Name to newly
                String date = gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(),1).toString();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                boolean correctDate;
                    try {
                        format.parse(date);
                        correctDate = true;
                    }
                    catch(ParseException parseException){
                        System.out.println("Invalid Date Format");
                        correctDate = false;
                    }
                if(correctDate) {
                    invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).setInvoiceDate(gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 1).toString());
                    invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).setCustomerName(gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 2).toString());

                    //A lines array list that will hold invoice lines for head invoice header
                    ArrayList<InvoiceLine> lines = new ArrayList<>(10);

                    //Reading every row in invoice items table to check for any modifications to overwrite.
                    for (int j = 0; j < gui.getInvoiceItemsTable().getRowCount(); j++) {
                        InvoiceLine line = new InvoiceLine();
                        line.setItemName(gui.getInvoiceItemsTable().getValueAt(j, 1).toString());
                        line.setItemPrice(gui.getInvoiceItemsTable().getValueAt(j, 2).toString());
                        line.setCount(gui.getInvoiceItemsTable().getValueAt(j, 3).toString());
                        lines.add(line);
                    }
                    //Setting the invoice header's invoice lines to newly modified lines.
                    invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).setInvoiceLines(lines);

                    //Getting invoice items model to modify rows in it
                    DefaultTableModel myModel = gui.getInvoiceItemsModel();
                    double sum = 0;
                    boolean error = false;

                    //Removing empty rows with no data on clicking the save button
                    for (int i = 0; i < invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size(); i++) {
                        if (myModel.getValueAt(i, 1).equals("") || myModel.getValueAt(i, 2).equals("") || myModel.getValueAt(i, 3).equals("") ||myModel.getValueAt(i, 0)==null||myModel.getValueAt(i, 1)==null||myModel.getValueAt(i, 2)==null) {
                            myModel.removeRow(i);
                            invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().remove(i);
                            if (i == 0 && invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size() == 0) {
                                invoiceHeaderArrayList.remove(gui.getInvoicesHeadersTable().getSelectedRow());
                                DefaultTableModel myModel2 = gui.getInvoicesTableModel();
                                myModel2.removeRow(gui.getInvoicesHeadersTable().getSelectedRow());
                                gui.setInvoicesTableModel(myModel2);
                                error = true;
                                break;
                            }
                        } else {
                            //Updating Values Of Cells In Invoice Items Table
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceNumber(), i, 0);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemName(), i, 1);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemPrice(), i, 2);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getCount(), i, 3);
                            myModel.setValueAt(Double.parseDouble(myModel.getValueAt(i, 2).toString()) * Double.parseDouble(myModel.getValueAt(i, 3).toString()), i, 4);
                            sum += Double.parseDouble(myModel.getValueAt(i, 4).toString());
                        }
                    }
                    if (!error) {
                        gui.setInvoiceItemsModel(myModel);
                    }
                    gui.getInvoicesHeadersTable().setValueAt(sum, gui.getInvoicesHeadersTable().getSelectedRow(), 3);
                    sum = 0;
                    error = false;
                    for (int i = 0; i < invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size(); i++) {
                        if (myModel.getValueAt(i, 1).equals("") || myModel.getValueAt(i, 2).equals("") || myModel.getValueAt(i, 3).equals("") ||myModel.getValueAt(i, 0)==null||myModel.getValueAt(i, 1)==null||myModel.getValueAt(i, 2)==null) {
                            myModel.removeRow(i);
                            invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().remove(i);
                            if (i == 0 && invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size() == 0) {
                                invoiceHeaderArrayList.remove(gui.getInvoicesHeadersTable().getSelectedRow());
                                DefaultTableModel myModel2 = gui.getInvoicesTableModel();
                                myModel2.removeRow(gui.getInvoicesHeadersTable().getSelectedRow());
                                gui.setInvoicesTableModel(myModel2);
                                error = true;
                                break;
                            }
                        } else {
                            //Updating Values Of Cells In Invoice Items Table
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceNumber(), i, 0);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemName(), i, 1);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemPrice(), i, 2);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getCount(), i, 3);
                            myModel.setValueAt(Double.parseDouble(myModel.getValueAt(i, 2).toString()) * Double.parseDouble(myModel.getValueAt(i, 3).toString()), i, 4);
                            sum += Double.parseDouble(myModel.getValueAt(i, 4).toString());
                        }
                    }
                    gui.setInvoiceItemsModel(myModel);

                    gui.getInvoicesHeadersTable().setValueAt(sum, gui.getInvoicesHeadersTable().getSelectedRow(), 3);
                    //Setting The Duplicate Array List To Current New One, As Saving The Status Of Project Denies Going Back.
                    invoiceHeaderArrayListDuplicate.clear();
                    invoiceHeaderArrayListDuplicate.addAll(invoiceHeaderArrayList);

                }else{
                    JOptionPane.showMessageDialog(null, "Please Enter a valid date format!", "Error", 0);
                }

                //Clicking Cancel Button - "cancel" command
            } else if (e.getActionCommand().equals("cancel")) {

                DefaultTableModel model = gui.getInvoiceItemsModel();
                model.removeRow(gui.getInvoiceItemsTable().getSelectedRow());
                invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().remove(gui.getInvoiceItemsTable().getSelectedRow()+1);
                gui.setInvoiceItemsModel(model);

                //Clicking Add New Item Button - "add" command
            } else if (e.getActionCommand().equals("add")) {

                //Add new empty row to the invoice items  model
                DefaultTableModel model = gui.getInvoiceItemsModel();
                model.addRow(new Object[]{"", "", "", "", ""});
                gui.setInvoiceItemsModel(model);

                //Clicking Load File From File Menu - "loadFile" command
            } else if (e.getActionCommand().equals("loadFile")) {
                //Loading loaded data to tables
                invoiceHeaderArrayList = fileOperations.readFile();
                if(invoiceHeaderArrayList != null) {
                    if(invoiceHeaderArrayList.size()>0) {
                        DefaultTableModel model = gui.getInvoicesTableModel();
                        model.setRowCount(invoiceHeaderArrayList.size());
                        for (int i=0; i < invoiceHeaderArrayList.size(); i++) {
                            model.setValueAt(invoiceHeaderArrayList.get(i).getInvoiceNumber(), i, 0);
                            model.setValueAt(invoiceHeaderArrayList.get(i).getInvoiceDate(), i, 1);
                            model.setValueAt(invoiceHeaderArrayList.get(i).getCustomerName(), i, 2);
                            double sum = 0;
                            for (InvoiceLine line : invoiceHeaderArrayList.get(i).getInvoiceLines() ){
                                sum += Double.parseDouble(line.getItemPrice()) * Double.parseDouble(line.getCount());
                            }
                            model.setValueAt(sum,i,3);
                        }

                        gui.setInvoicesTableModel(model);
                        gui.getInvoicesHeadersTable().revalidate();
                    }
                }

                //Clicking On Save File From File Menu - "saveFile" command
            } else if (e.getActionCommand().equals("saveFile")) {
                String date = gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(),1).toString();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                boolean correctDate;
                try {
                    format.parse(date);
                    correctDate = true;
                }
                catch(ParseException parseException){
                    System.out.println("Invalid Date Format");
                    correctDate = false;
                }
                if(correctDate) {
                    invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).setInvoiceDate(gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 1).toString());
                    invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).setCustomerName(gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 2).toString());

                    //A lines array list that will hold invoice lines for head invoice header
                    ArrayList<InvoiceLine> lines = new ArrayList<>(10);

                    //Reading every row in invoice items table to check for any modifications to overwrite.
                    for (int j = 0; j < gui.getInvoiceItemsTable().getRowCount(); j++) {
                        InvoiceLine line = new InvoiceLine();
                        line.setItemName(gui.getInvoiceItemsTable().getValueAt(j, 1).toString());
                        line.setItemPrice(gui.getInvoiceItemsTable().getValueAt(j, 2).toString());
                        line.setCount(gui.getInvoiceItemsTable().getValueAt(j, 3).toString());
                        lines.add(line);
                    }
                    //Setting the invoice header's invoice lines to newly modified lines.
                    invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).setInvoiceLines(lines);

                    //Getting invoice items model to modify rows in it
                    DefaultTableModel myModel = gui.getInvoiceItemsModel();
                    double sum = 0;
                    boolean error = false;

                    //Removing empty rows with no data on clicking the save button
                    for (int i = 0; i < invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size(); i++) {
                        if (myModel.getValueAt(i, 1).equals("") || myModel.getValueAt(i, 2).equals("") || myModel.getValueAt(i, 3).equals("") ||myModel.getValueAt(i, 0)==null||myModel.getValueAt(i, 1)==null||myModel.getValueAt(i, 2)==null) {
                            myModel.removeRow(i);
                            invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().remove(i);
                            if (i == 0) {
                                invoiceHeaderArrayList.remove(gui.getInvoicesHeadersTable().getSelectedRow());
                                DefaultTableModel myModel2 = gui.getInvoicesTableModel();
                                myModel2.removeRow(gui.getInvoicesHeadersTable().getSelectedRow());
                                gui.setInvoicesTableModel(myModel2);
                                error = true;
                                break;
                            }
                        } else {
                            //Updating Values Of Cells In Invoice Items Table
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceNumber(), i, 0);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemName(), i, 1);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemPrice(), i, 2);
                            myModel.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getCount(), i, 3);
                            myModel.setValueAt(Double.parseDouble(myModel.getValueAt(i, 2).toString()) * Double.parseDouble(myModel.getValueAt(i, 3).toString()), i, 4);
                            sum += Double.parseDouble(myModel.getValueAt(i, 4).toString());
                        }
                    }
                    if (!error) {
                        gui.getInvoicesHeadersTable().setValueAt(sum, gui.getInvoicesHeadersTable().getSelectedRow(), 3);
                        gui.setInvoiceItemsModel(myModel);
                    }



                    //Setting The Duplicate Array List To Current New One, As Saving The Status Of Project Denies Going Back.
                    invoiceHeaderArrayListDuplicate.clear();
                    invoiceHeaderArrayListDuplicate.addAll(invoiceHeaderArrayList);
                    JOptionPane.showMessageDialog(null, "Please Choose Invoices Header File To Save", "Notice", 1);
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma Separated Files", "csv");
                    fileChooser.setFileFilter(filter);
                    String path1 = "";
                    String path2 = "";

                    //Prompt user to select file
                    int choice = fileChooser.showSaveDialog(null);
                    if (choice == JFileChooser.APPROVE_OPTION) {
                        //Store invoice header file to path1
                        path1 = fileChooser.getSelectedFile().getAbsolutePath();

                        //A Message Dialog To Inform The User To Choose Invoices Lines File To Save To
                        JOptionPane.showMessageDialog(null, "Please Choose Invoices Lines File To Save", "Notice", 1);
                        fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(filter);
                        choice = fileChooser.showSaveDialog(null);
                        if (choice == JFileChooser.APPROVE_OPTION) {
                            path2 = fileChooser.getSelectedFile().getAbsolutePath();
                            if (invoiceHeaderArrayList != null) {
                                fileOperations.writeFile(invoiceHeaderArrayList, path1, path2);
                            }

                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Please Enter a valid date format!", "Error", 0);
                }



            }
        }
    }
        /*
         * ListListener Class
         */
        class ListListener implements ListSelectionListener {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                //Apply actions if row is selected, and it is not empty invoice number
                if (gui.getInvoicesHeadersTable().getSelectedRow() > -1 && !e.getValueIsAdjusting() && !gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 0).equals("")) {
                    DefaultTableModel model = gui.getInvoiceItemsModel();
                    if (gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 3).equals("")) {
                        //If selecting an empty row, show an empty invoice items table to store new data.
                        for (int i = 0; i < model.getRowCount(); i++) {
                            model.setValueAt("", i, 0);
                            model.setValueAt("", i, 1);
                            model.setValueAt("", i, 2);
                            model.setValueAt("", i, 3);
                            model.setValueAt("", i, 4);
                        }
                    } else {
                        //Adding new rows to invoice items table to match number of lines
                        while (model.getRowCount() < invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size()) {
                            model.addRow(new Object[]{"", "", "", "", ""});
                        }
                        //Removing extra unnecessary rows in invoice items table
                        while (model.getRowCount() > invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size()) {
                            model.removeRow(model.getRowCount() - 1);
                        }
                        //Setting cells' values to current selected invoice header's lines
                        for (int i = 0; i < invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().size(); i++) {
                            model.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceNumber(), i, 0);
                            model.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemName(), i, 1);
                            try {
                                model.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getItemPrice(), i, 2);
                                model.setValueAt(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceLines().get(i).getCount(), i, 3);
                                model.setValueAt(Double.parseDouble(model.getValueAt(i, 2).toString()) * Double.parseDouble(model.getValueAt(i, 3).toString()), i, 4);
                            }catch (NumberFormatException formatException){
                                System.out.println(formatException.getMessage());
                            }

                        }
                    }
                    //Setting Labels to new data selected
                    gui.setInvoiceNumberlbl2(invoiceHeaderArrayList.get(gui.getInvoicesHeadersTable().getSelectedRow()).getInvoiceNumber());
                    gui.setInvoiceTotalLbl2(gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 3).toString());
                    gui.setCustomerName(gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 2).toString());
                    gui.setInvoiceDate(gui.getInvoicesHeadersTable().getValueAt(gui.getInvoicesHeadersTable().getSelectedRow(), 1).toString());
                    gui.setInvoiceItemsModel(model);

                }
            }
        }
    }


