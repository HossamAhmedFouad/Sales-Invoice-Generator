//EGY FWD Front End Testing Nano Degree Program - Project 1 - 2022
//Program Name : SIG.java
//Last Modification Date: 21/06/2022
//Author: Hossam Ahmed Fouad
//Version: 5.0
//Purpose: Generates Invoices For Sales

package com.sig;
import com.sig.controller.ActionsHandler;
import com.sig.model.FileOperations;
import com.sig.view.GUI;

/**
 * Main Running File
 */
public class SIG
{
    public static void main( String[] args ) {
        //Model
        FileOperations fileOperations = new FileOperations();
        //View
        GUI gui = new GUI();
        gui.setVisible(true);
        //Control
        ActionsHandler handler = new ActionsHandler(fileOperations, gui);

    }
}
