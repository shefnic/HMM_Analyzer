/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nshefte;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * Uses FSM principles to parse a String array in to 
 * comma-separated String array 'cells'
 * 
 * accessing delim count or outputChar and the processing of input
 * TODO: Use " to escape " to determine if quotes should be part of
 * stored string. i.e. """Test String""" will be stored as "Test String",
 * "This ""is"" a test string will be stored as This "is" a test string.
 * 
 * @author Nicholas
 */
public class csvFSM {
    
    private final static Logger LOGGER = Logger.getLogger(csvFSM.class.getName());
    
    private char[] input;
    private char[][] outputChar;
    private String[] output;
    private int delimCount;
    private int inPos;
    private int cell;
    private int cellPos;
    private int toState;
    
    public csvFSM(String in){

        outputChar = new char[in.length()][in.length()];
        
        input = in.toCharArray();
                    
        state_i();
        
        //Remove recursion with while-loop case select here
        while(toState<=5){
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
    
    public csvFSM(char[] in){

        input = new char[in.length];
        outputChar = new char[in.length][in.length];
        
        System.arraycopy(in, 0, input, 0, in.length);
        
        state_i();
        
        //Remove recursion with while-loop case select here
        while(toState<=5){
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
        
//        if(input!=null){
//            switch (input[0]) {
//                case ',':
//                    state_1();
//                    break;
//                case '"':
//                    state_3();
//                    break;
//                default:
//                    state_2();
//                    break;
//            }
//        }else{ state_f(); }
        
        if(input!=null){
            switch (input[0]) {
                case ',':
                    toState=1;
                    break;
                case '"':
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
        
//        while(",".equals(input[inPos]) && inPos != input.length){
        while(','==input[inPos] && inPos != input.length){    
            delimCount++;
            inPos++;
            cell++;
        }
        
        
//        if(inPos == input.length){
//            state_f();
//        }        
//        else if('"'==(input[inPos])){
//            inPos++;
//            state_3();
//        }
//        else {
//            //Do not advance
//            state_2();
//        }

        if(inPos == input.length){
            toState=999;
        }        
        else if('"'==(input[inPos])){
            inPos++;
            toState=3;
        }
        else {
            //Do not advance
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
        
        cellPos = 0;
        
        while( inPos != input.length && ','!=input[inPos] ){
            outputChar[cell][cellPos]=input[inPos];
            inPos++;
            cellPos++;
        }
        
        
//        if(inPos == input.length){
//            state_f();
//        }
//        else{ state_1(); }

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
     * TODO: If end of array met before a closing '"' is found, insert the
     * '"' to the beginning of the outputChar array 'cell' and move to final state
     * 
     */
    private void state_3(){
        
        cellPos = 0;
        
        while(!('"'==input[inPos]) && inPos != input.length){
            outputChar[cell][cellPos]=input[inPos];
            inPos++;
            cellPos++;
        }
        
//        if(inPos == input.length){
//            state_f();
//        }
//        else{ state_4(); }

        if(inPos == input.length){
            toState=999;
        }
        else{ toState=4; }
        
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
        
//        if(inPos+1 == input.length){
//            state_f();
//        }
//        else if(','==input[inPos+1]){
//            inPos++;
//            state_1();
//        }
//        else{
//            outputChar[cell][++cellPos] = input[inPos];
//            inPos++;
//            state_3();
//        }
        
        if(inPos+1 == input.length){
            toState=999;
        }
        else if(','==input[inPos+1]){
            inPos++;
            toState=1;
        }
        else{
            outputChar[cell][++cellPos] = input[inPos];
            inPos++;
            toState=3;
        }        
        
    }
    
    /**
     * Copies the outputChar array to one of appropriate size
     */
    private void state_f(){
        
        output = new String[delimCount];
        
        for(int i = 0; i < delimCount; i++){
            output[i] = new String(outputChar[i]);
        }
        
        outputChar = null;
        inPos = 0;
        cellPos = 0;
        
    }
    
    public int get_delimCount(){
        return delimCount;
    }
    
    /**
     * Returns the parsed line
     * @return 
     */
    public String[] get_parsedLine(){
        return output;
    }
        
}
