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
import java.util.ArrayDeque;

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
    ArrayDeque<String[][]> test1 = new ArrayDeque(0);
    ArrayDeque<float[][]> converttest = new ArrayDeque(0);

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
                        
                        test1.add(outFile);
                        
                        ConvertToFloat(test1, converttest);
                        
                              
                        for(String[] outString: outFile){
                            for(String line: outString){
                                System.out.print(line+" | ");
                            }
                            System.out.println();
                        }
                        
                        System.out.println();
                        System.out.println("Float test");
                        while(!converttest.isEmpty()){
                            float[][] temp = converttest.pop();
                            
                            for(float[] flt1: temp){
                                for(float flt: flt1){
                                    System.out.println(flt+" ");
                                }
                            }
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
    
        public static void ConvertToFloat(ArrayDeque inputCSVs, ArrayDeque floatCSVs){
            
            String[][] temp_in;
            float[][] temp_out;
            
            while(!inputCSVs.isEmpty()){
                temp_in = (String[][]) inputCSVs.pop();
                temp_out = new float[temp_in.length][temp_in[0].length];
                
                for(int i = 0; i < temp_in.length; i++){
                    for(int j = 0; j < temp_in[0].length; j++){
                        temp_out[i][j] = Float.parseFloat(temp_in[i][j]);
                    }
                }
                
                floatCSVs.add(temp_out);
                
            }
            
        }      
    
}
