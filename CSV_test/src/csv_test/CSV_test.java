/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv_test;

import com.nshefte.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Nicholas
 */
public class CSV_test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
    String eFileName=null;
    String sFileName=null;

    if(args.length==0){
        //STDOUT Help
    }else{

        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-e") && i+1 < args.length){
                eFileName = args[i+1];
            }
            if(args[i].equals("-s") && i+1 < args.length){
                sFileName = args[i+1];
            }            
        }
        
        if(eFileName.equals(null)||sFileName.equals(null)){
            //STDOUT Help
        }
  
    }        
        
//        final String INFILE="..\\test_csv.csv";
        CSVParser testCSV = null; 
        String[][] outFile;  
                
		try {
			File file = new File(sFileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

                        CSVParser parsed_csv = new CSVParser(bufferedReader);
                        outFile = parsed_csv.getCSV();
                        
                              
                        for(String[] outString: outFile){
                            for(String line: outString){
                                System.out.print(line+" | ");
                            }
                            System.out.println();
                        }
                                                                      
			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
                        System.out.println("Can not locate file: "+sFileName);
		}       
                
		try {
			File file = new File(eFileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

                        CSVParser test_csv = new CSVParser(bufferedReader);
                        outFile = test_csv.getCSV();
                        
                              
                        for(String[] outString: outFile){
                            for(String line: outString){
                                System.out.print(line+" | ");
                            }
                            System.out.println();
                        }
                                                                      
			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
                        System.out.println("Can not locate file: "+eFileName);
		}                  
                
    }
    
}
