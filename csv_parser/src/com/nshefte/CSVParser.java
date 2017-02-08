/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nshefte;

import java.io.BufferedReader;
import java.util.ArrayDeque ;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
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
    
    private BufferedReader inCSV;
    private String[][] outCSV;
    
    public CSVParser() {
        inCSV = null;
        outCSV = null;
    }
    
    public CSVParser(BufferedReader inFile){
        outCSV = parse(inFile);
    }
    
    public String[][] parse(BufferedReader inFile){
        
        int maxDelim = 0;
        int maxRows = 0;
        String[][] output = null;
//        String[] temp = null;
        ArrayDeque<String> tempList = null;
        String line = null;
        String cell = "";
        boolean first = true;
        boolean quote = false;
        //Parsing states
        boolean beginCell = true;
        boolean inCell = false;
        
        try {
            while((line = inFile.readLine()) != null){
                
                tempList.add(line);
                maxRows++;
                
                if(first){ //need to check for quotations
                    for (char ch: line.toCharArray()){
                        maxDelim += (ch == 44)? 1:0;
                    }
                }
               
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(
                             Level.SEVERE, null, ex);
        }
        
        output = new String[maxDelim][tempList.size()];
        
        return output;
        
    }
    
    private String[][] parseDelims(String[] stringFile){
        
        String[][] parsedMatrix = null;
        
        return parsedMatrix;
    }
    
}
