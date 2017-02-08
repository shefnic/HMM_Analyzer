/*
    Analyze a provided hidden Markov model using the Viterbi and Forward
    algorithms.
*/
package com.nshefte;

/**
 *
 * @author Nicholas
 */
public class HMM_Analysis {
      
    public static void main(String[] args){
        
        int[] obs = {};  //Observed emissions;
        int[] state = {}; //All possible states, must begin with 'Start' state
        
        float[][] emission; //emission probability matrix
        float[][] vector; //vector transition probability matrix
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
         * 
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
    
}  
    
    

