
package com.nshefte;

import java.io.BufferedReader;
import java.util.ArrayDeque ;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Input a CSV 'text' file and return a 2-dimensional array
 * 
 * @author Nicholas
 */
public class CSVParser {
    
    private final static Logger LOGGER = Logger.getLogger(CSVParser.class.getName());
    
    private BufferedReader inCSV;
    private String[][] outCSV;
    
    public CSVParser() {
        this.inCSV = null;
        this.outCSV = null;
    }
    
    public CSVParser(BufferedReader inFile){
        this.outCSV = parse(inFile);
    }
    
    public String[][] parse(BufferedReader inFile){
        
        int maxDelim = 0;
        int maxRows = 0;
        String[][] output = null;
        ArrayDeque<String> tempList = new ArrayDeque(1);
        String line = null;
        String cell = "";
        boolean first = true;
        boolean quote = false;
        
        try {
            while((line = inFile.readLine()) != null){
                
                tempList.add(line);
                maxRows++;
                  
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(
                             Level.SEVERE, null, ex);
        }
        
        maxRows = tempList.size();
        
        output = new String[maxRows][];
        
        for(int i = 0; i < maxRows; i++){
            
                csvFSM unparsedLine = new csvFSM(tempList.pop());
                output[i] = unparsedLine.get_parsedLine();
                unparsedLine = null;
        }
        
        return output;
        
    }
    
    public String[][] getCSV(){
        return this.outCSV;
    }
        
}
