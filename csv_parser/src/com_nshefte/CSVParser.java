/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com_nshefte;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Input a csv 'text' file and return a 2-day array
 * Allow for column and row headers
 * 
 * @author Nicholas
 */
public class CSVParser {
    
    BufferedReader inCSV;
    String[][] outCSV;
    
    public CSVParser() {
        inCSV = null;
        outCSV = null;
    }
    
    public CSVParser(BufferedReader inFile){
        outCSV = parse(inFile);
    }
    
    public String[][] parse(BufferedReader inFile){
        
        int maxDelim = 0;
        String[][] output = null;
        String[] temp = null;
        ArrayList<String> tempList = null;
        String line = null;
        
        try {
            while((line = inFile.readLine()) != null){
                
                int tempDelim = 0;
                tempList.add(line);
                for (char ch: line.toCharArray()){
                    tempDelim += (ch == 44)? 1:0;
                }
                
                if(tempDelim > maxDelim){
                    maxDelim = tempDelim;
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(
                             Level.SEVERE, null, ex);
        }
        
        if ( !tempList.isEmpty() ) {
            temp = tempList.toArray(temp);
            
            //Parse Temp in private function here
            
        }
       
        return output;
        
    }
    
    private String[][] parseDelims(String[] stringFile){
        
        String[][] parsedMatrix = null;
        
        return parsedMatrix;
    }
    
}
