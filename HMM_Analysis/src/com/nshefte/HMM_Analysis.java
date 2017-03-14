/*
    Analyze a provided hidden Markov model using the Viterbi and Forward
    algorithms.
*/
package com.nshefte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 *
 * @author Nicholas
 */
public class HMM_Analysis {
      
    public static void main(String[] args){
        
        ArrayDeque<String[][]> inputCSVs = new ArrayDeque(0);
        float[][][] e_v = new float[2][][];
        int[][] o_s = new int[2][];
//        float[][] emission;
//        float[][] vector;
//        int[] obs;
//        int[] state;
        String[] fileNames = new String[4];

        if(args.length==0){
            //STDOUT Help
        }else{

            for(int i = 0; i < args.length; i++){
                if(args[i].equals("-e") && i+1 < args.length){
                    fileNames[0]=args[i+1];
                }
                if(args[i].equals("-s") && i+1 < args.length){
                    fileNames[1]=args[i+1];
                }   
                if(args[i].equals("-t") && i+1 < args.length){
                    fileNames[2]=args[i+1];
                }   
                if(args[i].equals("-o") && i+1 < args.length){
                    fileNames[3]=args[i+1];
                }      
                if(args[i].equals("-h")||args[i].equals("-help")){
                    //Goto STDOUT Help
                }
            }
                        
            for(String fn: fileNames){

                try {
                        File file = new File(fn);
                        FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = 
                            new BufferedReader(fileReader);

                        CSVParser parsed_csv = new CSVParser(bufferedReader);

                        inputCSVs.add(parsed_csv.getCSV());

                        fileReader.close();

                } catch (IOException e) {
                        System.out.println("Cannot locate file: "+fn);
                }                  
            }
        
            if(inputCSVs.size()!=4){
                //Goto STDOUT Help
            }

            FormatMatrices(inputCSVs, e_v, o_s);
            Analysis(e_v[0], e_v[1], o_s[0], o_s[1]);

        }
    }
    
    private static void Analysis(float[][] emission, float[][] vector,
                                 int[] obs, int[] state){
        
//        int[] obs = {};  //Observed emissions;
//        int[] state = {}; //All possible states, must begin with 'Start' state

        //TODO: Convert csvs from String to float
        
//        float[][] emission; //emission probability matrix
//        float[][] vector; //vector transition probability matrix
        int[] mostLikely; //most likely model states
        float prob; //probability of most likely state
     
        mostLikely = Viterbi(obs, state, emission, vector);
        prob = Forward(obs, state, emission, vector);

        Results(mostLikely, prob);
        
    }
        
    //UPDATE TO RETURN AN OBJECT HOLDING THE SERIES AND DYTABLE GRID FOR SHOW
        private static int[] Viterbi(int[] obs, int[] state, 
                                    float[][] emit, float[][] vect){
            
            int[] series; //solution
            float[][] dyTable = new float[state.length][obs.length];
            int[][] dirTable = new int[state.length][obs.length]; //traceback
            
            //Initialize
            dyTable[0][0] = 1;
            for(int i = 1; i < obs.length; i++){
                dyTable[0][i] = 0;
            } 
            for(int j = 1; j < state.length; j++){
                dyTable[j][0] = 0;
            }
            
            for(int i = 0; i < obs.length; i++){
                dirTable[0][i] = 0;
                dirTable[0][i] = 0;
            }
            for(int j = 1; j < state.length; j++){
                dirTable[j][0] = 0;
                dirTable[j][0] = 0;
            }
            
            //Viterbi Algorithm
            for(int i = 1; i < obs.length; i++){
                for(int j = 1; j < state.length; j++){
                    dyTable[j][i] = emit[j][obs[i]] 
                                    * max(vect, obs, dyTable, dirTable, i-1, j);
                }
            }
            
            int finalJ = 0;
            for(int j = 1; j < dyTable.length; j++){
                if(dyTable[j][dyTable[0].length-1] > finalJ){
                    finalJ = j;
                }
            }
            
            series = trace(state, dirTable, finalJ);
            
            return series;  //MAKE THIS AN OBJECT TO HOLD THE ARRAY AND THE DYTABLE GRID
            
        }
        
        static float max(float[][] vect, int[] obs, float[][] dyTable, 
                         int[][] dirTable, int emit, int state){
            
            float tempVal;
            float maxVal = 0f;
            
            for(int w = 0; w < dyTable.length; w++){
                tempVal = dyTable[w][emit] * vect[w][state];
                
                if(tempVal > maxVal){
                    maxVal = tempVal;
                    dirTable[state][emit+1] = w;
                }
            }
            
            return maxVal;
            
        }
        
        /**
         * Returns the path of the most likely state changes
         * 
         * @param state
         * @param dirTable
         * @param j
         * @return 
         */
        static int[] trace(int[] state, int[][] dirTable, int j){
            
            int[] series = new int[dirTable[0].length];
            int pointer = dirTable[0].length-1;
            int tempJ = j;
            
            while(pointer >= 0){
                series[pointer] = state[tempJ];
                pointer--;
            }
            
            return series;
            
        }
        
        /**
         * Calculates the probability of the most likely set of emissions
         * @param obs
         * @param state
         * @param emit
         * @param vect
         * @return 
         */
        static float Forward(int[] obs, int[] state, float[][] emit,
                             float[][] vect){
            
            //UPDATE TO RETURN OBJECT HOLDING PROB AND SOLUTION MATRIX
            
            float[][] dyTable = new float[state.length][obs.length];
            int[][] dirTable = new int[state.length][obs.length];
            
            //Initialize
            dyTable[0][0] = 1f;
            for(int i = 1; i < obs.length; i++){
                dyTable[0][i] = 0f;
            }
            for(int j = 1; j < state.length; j++){
                dyTable[j][0] = 0f;
            }
            
            for(int i = 0; i < obs.length; i++){
                dirTable[0][i] = 0;
            }
            for(int j = 1; j < state.length; j++){
                dirTable[j][0] = 0;
            }
            
            //Algorithm
            for(int i = 1; i < obs.length; i++){
                for(int j = 1; j < state.length; j++){
                    dyTable[j][i] = emit[j][obs[i]] 
                                    * sum(vect, obs, dyTable, i-1, j);
                }
            }
            
            //Solution
            float prob = 0f;
            
            for(int j = 0; j < dyTable.length; j++){
                if(dyTable[j][dyTable[0].length-1] > prob){
                    prob = dyTable[j][dyTable[0].length-1];
                }
            }
            
            //UPDATE TO RETURN OBJECT HOLDING PROB AND SOLUTION MATRIX
            return prob;
            
        }
        
        /**
         * Sums the array of emission * vector probabilities
         * @param vect
         * @param obs
         * @param dyTable
         * @param i
         * @param j
         * @return 
         */
        static float sum(float[][] vect, int[] obs, float[][] dyTable,
                         int emit, int state){
            
            float sum = 0f;
            
            for(int w = 0; w < dyTable.length; w++){
                sum += dyTable[w][emit] * vect[w][state];
            }
            
            return sum;
        }
        
        /**
         * Outputs results from algorithmic analysis
         * @param mostLikely
         * @param prob 
         */
        static void Results(int[] mostLikely, float prob){
            //NOTE ADAPT TO USE OBJECTS HOLDING MOSTLIKELY, PROB AND
            //BOTH VITERBI AND FORWARD MATRICES
        }
        
        /**
         * Pops the Deque and transcribes String values in to floats
         * @param inputCSVs 
         * @param e_v 
         * @param o_s 
         */               
        public static void FormatMatrices(ArrayDeque inputCSVs
                                           ,float[][][] e_v, int[][] o_s){
            
            String[][] temp_in;
            float[][] temp_fout;
            int[] temp_iout;
            
            while(!inputCSVs.isEmpty()){
                
                //Convert emission and vector matrices from String to float
                for(int f_count = 0; f_count < 3; f_count++){
                    temp_in = (String[][]) inputCSVs.pop();
                    
                    //float matrices declared one size larger than necessary
                    //to simplify pointers used in the algorithm
                    temp_fout = new float[temp_in.length+1][temp_in[0].length];

                    for(int i = 1; i < temp_in.length+1; i++){
                        for(int j = 0; j < temp_in[0].length; j++){
                            try{
                                temp_fout[i][j] = Float.parseFloat(temp_in[i][j]);
                            }
                            catch(NumberFormatException nfe){
                                System.out.println("Could not convert "
                                                    +temp_in[i][j]
                                                    +" to float");
                            }
                        }
                    }
                    e_v[f_count] = temp_fout;
                }
                
                //Convert obs and state matrices from String to int
                for(int f_count = 0; f_count < 3; f_count++){
                    temp_in = (String[][]) inputCSVs.pop();

                    temp_iout = new int[temp_in.length];

                    for(int i = 0; i < temp_in.length; i++){
                        try{
                            temp_iout[i] = Integer.parseInt(temp_in[i][0]);
                        }
                        catch(NumberFormatException nfe){
                            System.out.println("Could not convert "
                                                +temp_in[i][0]
                                                +" to int");
                        }
                    }
                    o_s[f_count] = temp_iout;
                }                
                  
            }
        }        
    
}  