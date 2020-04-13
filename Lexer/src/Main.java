

import java.io.FileNotFoundException;

public class Main {

    public static void main( String[] args){

        Lexer lexer = new Lexer();

        System.out.println("\n+--------------------------+\n| Lexical analyzer program |\n+--------------------------+\n");

        if(args.length == 0){
            try{
                lexer.readInput("default");
            }
            catch (FileNotFoundException fn){
                System.out.println("\nError:No input.txt file in "+System.getProperty("user.dir")+" !\n");
                System.exit(-1);
            }
        }
        else {
            try{
                lexer.readInput(args[0]);
            }
            catch (FileNotFoundException fn){
                System.out.println("\nError:No file, wrong path!\n");
                System.exit(-1);
            }
        }
        lexer.startState();

    }
}
