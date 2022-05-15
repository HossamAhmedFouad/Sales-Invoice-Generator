//EGY FWD - Front End Testing Nano Degree Program - Project 1 May Cohort - 2022
//Program Name : GUI.java
//Last Modification Date: 11/05/2022
//Author: Hossam Ahmed Fouad
//Version: 1.0
//Purpose: Serves The View Part In The Model View Control (MVC) Design for SIG - Graphical User Interface

package com.sig.view;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;


public class GUI extends JFrame {
    /**
     * Class Instances
     */
    private JPanel leftPanel,rightPanel,invoicesTableContainer,buttonsContainer,labelPanel;
    private JLabel invoiceTotalLbl2;

    public JLabel getInvoiceTotalLbl2() {
        return invoiceTotalLbl2;
    }

    public JLabel getInvoiceNumberlbl2() {
        return invoiceNumberlbl2;
    }

    public JTextField getCustomerName() {
        return customerName;
    }

    public JTextField getInvoiceDate() {
        return invoiceDate;
    }
    private JLabel invoiceNumberlbl2;
    private JTextField customerName,invoiceDate;
    private DefaultTableModel invoicesTableModel;
    private DefaultTableModel invoiceItemsModel;
    private JTable invoicesHeadersTable, invoiceItemsTable;
    private  JButton createNewInvoiceBtn,deleteInvoiceBtn,saveBtn,cancelBtn,addItemBtn;
    private  JMenuItem loadFile,saveFile;

    /**
     * Getters And Setters
     */
    public void setInvoiceNumberlbl2(String invoiceNumberlbl2) {
        this.invoiceNumberlbl2.setText(invoiceNumberlbl2);
    }
    public JTable getInvoicesHeadersTable() {
        return invoicesHeadersTable;
    }

    public void setInvoicesHeadersTable(JTable invoicesHeadersTable) {
        this.invoicesHeadersTable = invoicesHeadersTable;
    }
    public JTable getInvoiceItemsTable() {
        return invoiceItemsTable;
    }

    public void setInvoiceItemsTable(JTable invoiceItemsTable) {
        this.invoiceItemsTable = invoiceItemsTable;
    }

    public DefaultTableModel getInvoicesTableModel() {
        return invoicesTableModel;
    }

    public void setInvoicesTableModel(DefaultTableModel invoicesTableModel) { this.invoicesTableModel = invoicesTableModel; }
    public void setInvoiceTotalLbl2(String invoiceTotalLbl2) { this.invoiceTotalLbl2.setText(invoiceTotalLbl2);}
    public void setInvoiceItemsModel(DefaultTableModel invoiceItemsModel) { this.invoiceItemsModel = invoiceItemsModel;}
    public DefaultTableModel getInvoiceItemsModel() {
        return invoiceItemsModel;
    }
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate.setText(invoiceDate);
    }
    public void setCustomerName(String customerName) {
        this.customerName.setText(customerName);
    }

    /*
     * Class Constructor
     */
    public GUI(){
        /*
         * Main Frame Properties
         */
        super("Sales Invoice Generator");
        setVisible(true);
        setLayout(new GridLayout(1,4));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950,520);
        setLocation(300,200);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        loadFile = new JMenuItem("Load File");
        saveFile = new JMenuItem("Save File");
        fileMenu.add(loadFile);
        fileMenu.add(new JSeparator());
        fileMenu.add(saveFile);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);


        /*
         * Base Panels
         */

        leftPanel = new JPanel();
        rightPanel = new JPanel();

        //Base Panels Layout
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        /*
         * Inner Panels - Left Panel
         */

        //Table Label
        JLabel lbl1 = new JLabel("Invoices Table",SwingConstants.LEFT);
        InvoicesTable invoicesTable = new InvoicesTable();

        labelPanel= new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(lbl1,"Left");
        leftPanel.add(labelPanel,"Left");

        //Invoices Table
        invoicesTableModel = new DefaultTableModel(invoicesTable.getData(),invoicesTable.getCols()){
            public boolean isCellEditable(int row,int column){
                if(column==0){
                    return false;
                }else {
                    return true;
                }
            }
        };
        invoicesHeadersTable = new JTable(invoicesTableModel);


        //Table Designing
        invoicesHeadersTable.getTableHeader().setBackground(Color.WHITE);
        ((DefaultTableCellRenderer)invoicesHeadersTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        JScrollPane scrollPane = new JScrollPane(invoicesHeadersTable);
        scrollPane.setPreferredSize(new Dimension(430,360));
        leftPanel.add(scrollPane);

        //Left Panel Buttons
        buttonsContainer = new JPanel();
        buttonsContainer.setLayout(new FlowLayout(FlowLayout.CENTER,35,10));
        createNewInvoiceBtn = new JButton("Create New Invoice");
        deleteInvoiceBtn = new JButton("Delete Invoice");

        buttonsContainer.add(createNewInvoiceBtn);
        buttonsContainer.add(deleteInvoiceBtn);
        leftPanel.add(buttonsContainer);

        add(leftPanel);

        /*
         * Inner Panels - Right Panel
         */

        //Invoice Details
        JPanel invoiceDetails = new JPanel();
        //Inner Invoice Details Panel For Non Field
        JPanel invoiceDetailsNonField = new JPanel();

        invoiceDetails.setLayout(new BoxLayout(invoiceDetails,BoxLayout.Y_AXIS));
        invoiceDetailsNonField.setLayout(new FlowLayout(FlowLayout.LEFT,20,5));

        //Labels
        JLabel invoiceNumberlbl1 = new JLabel("Invoice Number");
        invoiceNumberlbl2 = new JLabel("");
        invoiceDetailsNonField.add(invoiceNumberlbl1);
        invoiceDetailsNonField.add(invoiceNumberlbl2);
        invoiceDetails.add(invoiceDetailsNonField);

        //Invoice Date Field
        JPanel invoiceDatePanel = new JPanel();
        invoiceDatePanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,5));
        invoiceDate = new JTextField();
        JLabel invoiceDatelbl = new JLabel("Invoice Date      ");
        invoiceDatePanel.add(invoiceDatelbl);
        invoiceDatePanel.add(invoiceDate);
        invoiceDate.setPreferredSize(new Dimension(300,20));
        invoiceDetails.add(invoiceDatePanel);

        //Invoice Customer Name Field
        JPanel customerNamePanel = new JPanel();
        customerNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,5));
        customerName = new JTextField();
        customerName.setPreferredSize(new Dimension(300,20));
        JLabel customerNamelbl = new JLabel("Customer Name");
        customerNamePanel.add(customerNamelbl);
        customerNamePanel.add(customerName);
        invoiceDetails.add(customerNamePanel);

        //Invoice Total
        JPanel invoiceTotalPanel = new JPanel();
        invoiceTotalPanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,10));
        JLabel invoiceTotalLbl1 = new JLabel("Invoice Total     ");
        invoiceTotalLbl2 = new JLabel("");
        invoiceTotalPanel.add(invoiceTotalLbl1);
        invoiceTotalPanel.add(invoiceTotalLbl2);
        invoiceDetails.add(invoiceTotalPanel);
        rightPanel.add(invoiceDetails);

        //Invoice Items

        //Invoice Items Panel
        JPanel invoiceItemsPanel = new JPanel();
        invoiceItemsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));

        //Invoice Items Table Structure
        InvoiceItems invoiceItems = new InvoiceItems();
        invoiceItemsModel = new DefaultTableModel(invoiceItems.getData(),invoiceItems.getCols()){
            @Override
            public boolean isCellEditable(int row,int column){
                if(column==0){
                    return false;
                }else {
                    return true;
                }
            }

        };

        invoiceItemsTable = new JTable(invoiceItemsModel);

        //Invoice Items Table Design
        invoiceItemsTable.getTableHeader().setBackground(Color.WHITE);
        ((DefaultTableCellRenderer)invoiceItemsTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        JScrollPane invoiceItemsScrollPane = new JScrollPane(invoiceItemsTable);
        invoiceItemsScrollPane.setPreferredSize(new Dimension(430,250));
        Border loweredEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border title = BorderFactory.createTitledBorder(loweredEtched, "Invoice Items");
        invoiceItemsPanel.setBorder(title);
        invoiceItemsPanel.add(invoiceItemsScrollPane);
        rightPanel.add(invoiceItemsPanel);

        //Right Panel Buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER,60,0));
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        addItemBtn = new JButton("Add New Item");
        buttonsPanel.add(addItemBtn);
        buttonsPanel.add(saveBtn);
        buttonsPanel.add(cancelBtn);
        rightPanel.add(buttonsPanel);
        add(rightPanel);


        //Action Commands For Left And Right Panels' Buttons
        createNewInvoiceBtn.setActionCommand("create");
        deleteInvoiceBtn.setActionCommand("delete");
        saveBtn.setActionCommand("save");
        cancelBtn.setActionCommand("cancel");
        addItemBtn.setActionCommand("add");
        loadFile.setActionCommand("loadFile");
        saveFile.setActionCommand("saveFile");


    }

    /*
     * Actions And Events
     */

    public void addCreateNewInvoiceBtnActionListener(ActionListener listener){
        createNewInvoiceBtn.addActionListener(listener);
    }
    public void addDeleteInvoiceBtnActionListener(ActionListener listener){
        deleteInvoiceBtn.addActionListener(listener);
    }
    public void addSaveBtnActionListener(ActionListener listener){
        saveBtn.addActionListener(listener);
    }
    public void addCancelBtnActionListener(ActionListener listener){
        cancelBtn.addActionListener(listener);
    }
    public void addInvoicesHeaderTableSelectionListener(ListSelectionListener listener){invoicesHeadersTable.getSelectionModel().addListSelectionListener(listener);}
    public void setAddItemBtnActionListener(ActionListener listener){addItemBtn.addActionListener(listener);}

    public void setLoadFileMenuItemActionListener(ActionListener listener){loadFile.addActionListener(listener);}
    public void setSaveFileMenuItemActionListener(ActionListener listener){saveFile.addActionListener(listener);}
}
