import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

public class Lexer {

    private String input;
    private Integer no_char = 0;
    private char currentChar;

    private LinkedList<String> output = new LinkedList<String>();
    private LinkedList<String> exceptions = new LinkedList<String>();

    /**
     * Reads the input from the given path, or default
     * the input file in the current directory.
     *
     * @throws java.io.FileNotFoundException if there is no input file.
     *
     * @param path The path of the input file, or {@code default} for def settings.
     */
    public void readInput(String path) throws FileNotFoundException {

        File inputFile;
        if(path.compareTo("default")!= 0){
            inputFile= new File(path);
        }
        else{
            inputFile = new File(System.getProperty("user.dir")+"/input.txt");
        }
        Scanner sc = new Scanner(inputFile);

        input = "";
        while(sc.hasNextLine()){
            input += sc.nextLine();
        }


    }

    /**
     * Reads input from the {@code input} string, and puts in the
     * {@code currentChar} string. If no such index in the string, prints "Out of input"
     * to the console.
     */
    private void readInput(){
        try {
            currentChar = input.charAt(no_char);

        }
        catch (IndexOutOfBoundsException e){
            //System.out.println("Out of input.");
            stop();
        }
    }

    /**
     * Prints the input and the syntactical structure of the input to the console.
     */
    private void printOutput(){
        System.out.println("Input: "+input+"\n");
        System.out.println("Syntactical structure: "+output+"\n");
    }

    /**
     * Prints the syntax errors to the console, if there is any.
     */
    private void printExceptions(){
        if(exceptions.size()> 0){
            System.out.println(exceptions);
        }
    }


    /**
     * 1. The start state of the lexical analyzer.
     */
    public void startState(){

        readInput();

        if(isAlphabetic(currentChar)){
            no_char++;
            identifier();   // state 2.
        }
        else if(isDigit(currentChar)){
            no_char++;
            number();       //state 4.
        }
        else if(currentChar == '{'){
            no_char++;
            bracketComment();   //state 6.
        }
        else if( currentChar == '('){
            no_char++;
            parenthesis();  //state 8.
        }
        else if(currentChar==':'){
            no_char++;
            colon();    //state 12.
        }
        else if(currentChar == '<'){
            no_char++;
            lessthan();     //state 14.
        }
        else if(currentChar == '>'){
            no_char++;
            greaterthan();  //state 17.
        }
        else if(currentChar == ' '){
            no_char++;
            startState();   //state 1.
        }
        else if(currentChar == '$'){
            output.add("<End>");
            stop();         //state 21.
        }
        else {
            exceptions();   //error handling
        }
    }

    /**
     * 2.
     */
    private void identifier(){

        readInput();

        if(isAlphabetic(currentChar)){
            no_char++;
            identifier();   // state 2.
        }
        else if(isDigit(currentChar)){
            no_char++;
            identifier();       //state 2.
        }
        else {
            identifierEnd();
        }
    }

    /**
     * 3.
     */
    private void identifierEnd(){
        output.add("<indentifier>");
        startState();
    }

    /**
     * 4
     */
    private void number(){
        readInput();

        if(isDigit(currentChar)){
            no_char++;
            number();       //state 4.
        }
        else {
            numberEnd();    //state 5.
        }
     }
    /**
     * 5.
     */
    private void numberEnd(){
        output.add("<number>");
        startState();   // state 1.
    }
    /**
     * 6.
     */
    private void bracketComment(){
        readInput();
        if( currentChar == '}'){
            no_char++;
            bracketCommentEnd();    //state 7.
        }
        else if( currentChar == '$'){
            exceptions();       //state 19.
        }
        else {
            no_char++;
            bracketComment();   //state 6.
        }
    }
    /**
     * 7.
     */
    private void bracketCommentEnd(){
        output.add("<Bracket-comment>");
        startState(); //state 1.
    }
    /**
     * 8.
     */
    private void parenthesis(){
        readInput();

        if(currentChar == '*'){
            no_char++;
            longComment();  //state 9.
        }
        else if( currentChar == '$'){
            exceptions();   //state 19.
        }
        else {
            something();    //state 20.
        }
    }
    /**
     * 9.
     */
    private void longComment(){
        readInput();

        if (currentChar == '*'){
            no_char++;
            astLongComment();   //state 10.
        }
        else if( currentChar == '$'){
            exceptions();   //state 19.
        }
        else{
            no_char++;
            longComment();  //state 9.
        }
    }
    /**
     * 10.
     */
    private void astLongComment(){
        readInput();

        if(currentChar == '*'){
            no_char++;
            astLongComment();   //state 10.
        }
        else if (currentChar == ')'){
            no_char++;
            longCommentEnd();   //state 11.
        }
        else if( currentChar == '$'){
            exceptions();   //state 19.
        }
        else {
            no_char++;
            longComment();  //state 9.
        }
    }
    /**
     * 11.
     */
    private void longCommentEnd(){
        output.add("<(**) comment>");
        startState();   //state 1.
    }
    /**
     * 12.
     */
    private void colon(){
        readInput();

        if (currentChar == '='){
            no_char++;
            coloneqv(); //state 13.
        }
        else if (currentChar == '$'){
            exceptions();   //state 19.
        }
        else {
            output.add("< : token >");
            something();    //state 20.
        }
    }
    /**
     * 13.
     */
    private void coloneqv(){
        output.add("<:= token>");
        startState();
    }
    /**
     * 14.
     */
    private void lessthan(){
        readInput();
        if(currentChar == '='){
            no_char++;
            lesseqv();  // state 15.
        }
        else if(currentChar == '>'){
            no_char++;
            noteqv();     //state 16.
        }
        else if(currentChar == '$'){
            exceptions();   //state 19.
        }
        else {
            output.add("< < token >");
            something(); //state 20.
        }
    }
    /**
     * 15.
     */
    private void lesseqv(){
        output.add("< <= token>");
        startState(); //state 1.
    }
    /**
     * 16.
     */
    private void noteqv(){
        output.add("< <> token >");
        startState(); //state 1.
    }
    /**
     * 17.
     */
    private void greaterthan(){
        readInput();

        if(currentChar == '='){
            no_char++;
            greatereqv();
        }
        else if(currentChar == '$'){
            exceptions();   //state 19.
        }
        else {
            output.add("< > token >");
            something();    //state 20.
        }
    }
    /**
     * 18.
     */
    private void greatereqv(){
        output.add("< >= token >");
        startState();     //state 1.
    }
    /**
     * 19.
     */
    private void exceptions(){
        int error = no_char+1;
        exceptions.add("SYNTAX ERROR at the "+ error +". character, '"+input.charAt(no_char)+"'.\n");
        output.add("<Error!>");
        no_char++;
        startState();   //state 1.
    }
    /**
     * 20
     */
    private void something(){
        //not implemented

        no_char++;
        startState(); //state 1.
    }
    /**
     * 21.
     */
    private void stop(){
        printOutput();
        printExceptions();
        System.exit(0);
    }
}
