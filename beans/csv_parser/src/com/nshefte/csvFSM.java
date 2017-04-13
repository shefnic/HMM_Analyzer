/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nshefte;

/**
 * Uses FSM principles to parse a String array in to 
 * comma-separated String array 'cells'
 * 
 * //TODO: Make thread-safe
 * 
 * @author Nicholas
 */
public class csvFSM {
    
    private char[] input;
    private char[][] outputChar;
    private String[] output;
    private int delimCount;
    private int inPos;
    private int cell;
    private int cellPos;
    private int toState;
    
    public csvFSM(String in){

        this.outputChar = new char[in.length()][in.length()];
        
        this.input = in.toCharArray();
                    
        state_controller();       
                
    }
        
    public csvFSM(char[] in){

        this.input = new char[in.length];
        this.outputChar = new char[in.length][in.length];
        
        System.arraycopy(in, 0, input, 0, in.length);
        
        state_controller();
                
    }
    
    private void state_controller(){
        
        state_i();
        
        while(toState<5){
            switch(toState){
                case 1: state_1(); break;
                case 2: state_2(); break;
                case 3: state_3(); break;
                case 4: state_4(); break;
                default: break;
            }
        }
        
        state_f();        
    }
    
    /**
     * Initial state: Checks for empty array and evaluates first character
     * If the character is ',' move to state 1
     * If the character is '"' move to state 3
     * Otherwise, move to state 2
     * 
     */
    private void state_i(){
        delimCount=0;
        inPos=0;
        cell=0;
        cellPos=0;
        
        if(input!=null){
            switch (input[0]) {
                case ',':
                    toState=1;
                    break;
                case '"':
                    inPos++;
                    toState=3;
                    break;
                default:
                    toState=2;
                    break;
            }
        }else{ toState=999; }        
        
    }
    
    /**
     * State at ','
     * Tallies delimiter then advances on the array
     * If ',' then stay in state 1
     * If '"' then move to state 3
     * If not at the end of the array, move to state 2
     * Otherwise, if EOL move to final state
     * 
     */
    private void state_1(){
                
        while(inPos != input.length && ','==input[inPos]){    
            delimCount++;
            inPos++;
            cell++;
        }
        
        if(inPos == input.length){
            toState=999;
        }        
        else if('"'==(input[inPos])){
            inPos++;
            cellPos = 0;
            toState=3;
        }
        else {
            //Do not advance
            cellPos = 0;
            toState=2;
        }
        
    }
    
    /**
     * State when in between delimiters
     * Moves characters to outputChar array
     * If ',' move to state 1
     * If end of array, then move to final state
     * Otherwise, stay in state 2
     * 
     */
    private void state_2(){
        
        while( inPos != input.length && ','!=input[inPos] ){
            outputChar[cell][cellPos]=input[inPos];
            inPos++;
            cellPos++;
        }
        
        if(inPos == input.length){
            toState=999;
        }
        else{ toState=1; }
        
    }
    
    /**
     * State in between delimiters of a 'cell' surrounded in quotations
     * Moves characters, including ',' in to outputChar array
     * If '"' then moves to state 4
     * Otherwise, stay in state 3
     * 
     */
    private void state_3(){
        
//        cellPos = 0;
        
        while(inPos != input.length && '"'!=input[inPos]){
            outputChar[cell][cellPos]=input[inPos];
            inPos++;
            cellPos++;
        }

        if(inPos == input.length){
            toState=999;
        }
        else{ 
            toState=4; 
        }
        
    }
    
    /**
     * State that determines if the '"' from state 3 should be put in to
     * outputChar array or is the closing quotation of the 'cell'
     * If the subsequent character is a ',' then move to state 1
     * If the end of array is met, then move to final state
     * Otherwise, add '"' to outputChar array and move to state 3
     * 
     */
    private void state_4(){
               
        if(inPos+1 == input.length){
            toState=999;
        }
        else if(','==input[inPos+1]){
            inPos++;
            toState=1;
        }
        else{
            inPos++;
            outputChar[cell][cellPos] = input[inPos];
            inPos++;
            cellPos++;
            toState=3;
        }             
    }
    
    /**
     * Copies the outputChar array to one of appropriate size
     */
    private void state_f(){
        
        output = new String[delimCount+1];
        
        for(int i = 0; i < delimCount+1; i++){
            output[i] = new String(outputChar[i]);
        }
        
        outputChar = null;
        inPos = 0;
        cellPos = 0;
        
    }
    
    public int get_delimCount(){
        return this.delimCount;
    }
    
    /**
     * Returns the parsed line
     * @return 
     */
    public String[] get_parsedLine(){
        return this.output;
    }
        
}
