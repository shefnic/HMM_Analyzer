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
        String[] fileNames = new String[4];

        if(args.length==0){
            HelpScreen();
        }else{

            //Stores the name in a specific order to be used later for
            //formatting the CSVs in to their appropriate matrices
            for(int i = 0; i < args.length; i++){
                if(args[i].equals("-e") && i+1 < args.length){
                    fileNames[0]=args[i+1];
                }
                if(args[i].equals("-v") && i+1 < args.length){
                    fileNames[1]=args[i+1];
                }   
                if(args[i].equals("-o") && i+1 < args.length){
                    fileNames[2]=args[i+1];
                }   
                if(args[i].equals("-s") && i+1 < args.length){
                    fileNames[3]=args[i+1];
                }      
                if(args[i].equals("-h")||args[i].equals("-help")){
                    HelpScreen();
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
                        System.exit(1);
                }                  
            }
        
            if(inputCSVs.size()!=4){
                HelpScreen();
            }

            FormatMatrices(inputCSVs, e_v, o_s);
            Analysis(e_v[0], e_v[1], o_s[0], o_s[1]);

        }
    }
    
    /**
     * Calls on the Viterbi algorithm to find the most likely set of states
     * given the input observations, then calls the Forward algorithm
     * to determine the probability of that specific set of states
     * occurring.
     * Both results are then displayed.
     * 
     * @param emission
     * @param vector
     * @param obs
     * @param state 
     */
    private static void Analysis(float[][] emission, float[][] vector,
                                 int[] obs, int[] state){
        
        int[] mostLikely; //most likely model states
        float prob; //probability of most likely state
     
        mostLikely = Viterbi(obs, state, emission, vector);
        prob = Forward(obs, state, emission, vector);

        Results(obs, mostLikely, prob);
        
    }
        
    /**
     * Uses the Viterbi algorithm to identify the most likely set of states
     * that would conform to the given set of observations in the model.
     * 
     * @param obs
     * @param state
     * @param emit
     * @param vect
     * @return 
     */
    private static int[] Viterbi(int[] obs, int[] state, 
                                float[][] emit, float[][] vect){

        int[] series; //solution
        float[][] dyTable = new float[state.length][obs.length];
        int[][] dirTable = new int[state.length][obs.length]; //traceback

        //Initialize
        dyTable[0][0] = 1; //Dynamic table
        dirTable[0][0] = 0; //Directional table
        for(int i = 1; i < obs.length; i++){
            dyTable[0][i] = 0;
            dirTable[0][i] = 0;
        } 
        for(int j = 1; j < state.length; j++){
            dyTable[j][0] = 0;
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

        return series;  

    }

    /**
     * Max determines inserts the position of the highest probability of hte
     * emission*state to directional table, then returns the value of that
     * probability.
     * 
     * @param vect
     * @param obs
     * @param dyTable
     * @param dirTable
     * @param emit
     * @param state
     * @return 
     */
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
     * Returns the path of the most likely state changes.
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
            tempJ = dirTable[tempJ][pointer];
            pointer--;
        }

        return series;

    }

    /**
     * Calculates the probability of most likely set of states, given 
     * the input observations.
     * 
     * @param obs
     * @param state
     * @param emit
     * @param vect
     * @return 
     */
    static float Forward(int[] obs, int[] state, float[][] emit,
                         float[][] vect){


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

        //Forward Algorithm
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
    static void Results(int[] obs, int[] mostLikely, float prob){

        for(int i=0;i<100;i++){System.out.print("*");}
        System.out.println();
        System.out.print("Given the set of observations: ");
        for(int ob: obs){
            System.out.print(ob+" ");
        }
        System.out.println();
        System.out.println();
        
        System.out.print("The most likely set of states is: ");
        for(int state: mostLikely){
            System.out.print(state+" ");
        }
        System.out.println();
        
        System.out.println("The probability of this set occurring is: "+prob);
        
        for(int i=0;i<100;i++){System.out.print("*");}
        System.out.println();

        
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

            for(int f_count = 0; f_count < 2; f_count++){
                temp_in = (String[][]) inputCSVs.pop();

                temp_fout = new float[temp_in.length][temp_in[0].length];

                for(int i = 0; i < temp_in.length; i++){
                    for(int j = 0; j < temp_in[0].length; j++){
                        try{
                            temp_fout[i][j] = Float.parseFloat(temp_in[i][j]);
                        }
                        catch(NumberFormatException nfe){
                            System.out.println("Could not convert "
                                                +temp_in[i][j]
                                                +" to float. Exiting...");
                            System.exit(1);
                        }
                    }
                }
                e_v[f_count] = temp_fout;
            }


            //Convert obs and state matrices from String to int
            for(int f_count = 0; f_count < 2; f_count++){
                temp_in = (String[][]) inputCSVs.pop();

                temp_iout = new int[temp_in.length];

                for(int i = 0; i < temp_in.length; i++){
                    try{
                        temp_iout[i] = Integer.parseInt(temp_in[i][0]);
                    }
                    catch(NumberFormatException nfe){
                        System.out.println("Could not convert "
                                            +temp_in[i][0]
                                            +" to int. Exiting...");
                        System.exit(1);
                    }
                }
                o_s[f_count] = temp_iout;
            }                

        }//End while inputCSV !empty
    }        

    
    /**
     * Displays application information and help screen instructions
     * then exits the program.
     */
    public static void HelpScreen(){
        System.out.println("Hidden Markov Model Analyzer");
        System.out.println("Author: Nicholas Shefte, 2017");
        System.out.println();
        System.out.println("This application implements the Virterbi and"
                           +"Foward algorithms to analyze a given Hidden"
                           +" Markov Model represented by a set of four"
                           +" comma-separated files.");
        System.out.println();
        System.out.println("Accepted arguments:");
        System.out.println("-e\t[REQUIRED]Emission Matrix file - Comma-"
                           +"separated - A matrix of probabilities in "
                           +"decimal form for each emission that can be"
                           +" emitted from each state.");
        System.out.println();
        System.out.println("-v\t[REQUIRED]Vector Matrix file - Comma-separated"
                           +" - A matrix of probabilities in decimal form for"
                           +" the transition of each state to the next.");
        System.out.println();
        System.out.println("-s\t[REQUIRED]State list file - A list of all"
                           + " possible states in the model.");
        System.out.println();
        System.out.println("-e\t[REQUIRED]Observed emissions file - A list"
                           +" of the sequence of emissions observed from the"
                           + " active model.");
        System.out.println();
        System.out.println("-h\tDisplays this help screen.");
        System.out.println();
        System.out.println("-help\tDisplays this help screen.");
        for(int i=0;i<40;i++){System.out.print("*");}
        System.out.println();
        System.out.println("For instructions on setting up the required files"
                           +" visit https://github.com/shefnic/HMM_Analyzer");
        
        System.exit(0);
        
        
    }
    
}  