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
        
        final String INFILE="..\\test_csv.csv";
        CSVParser testCSV = null; 
        String[][] outFile;
        
//		try {
//			File file = new File(INFILE);
//			FileReader fileReader = new FileReader(file);
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//			StringBuffer stringBuffer = new StringBuffer();
//			String line;
//			while ((line = bufferedReader.readLine()) != null) {
//				stringBuffer.append(line);
//				stringBuffer.append("\n");
//			}
//			fileReader.close();
//			System.out.println("Contents of file:");
//			System.out.println(stringBuffer.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}    
                
		try {
			File file = new File(INFILE);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

                        CSVParser test_csv = new CSVParser(bufferedReader);
                        outFile = test_csv.getCSV();
                        
                        for(int i=0; i<outFile.length;i++){
                            for(int j=0; j<outFile[0].length;j++){
                                System.out.print(outFile[i][j]+" | ");
                            }
                            System.out.println();
                        }
                        
			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}                 
                
    }
    
}
