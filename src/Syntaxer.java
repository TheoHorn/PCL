
import java.util.ArrayList;
import lexer.Lexer;
import lexer.Char;
import lexer.Num;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Syntaxer {
    private ArrayList<Token> tokens;
    private int currentTokenIndex = 0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private void nextToken() {
        currentTokenIndex++;
    }

    public ASTNode parse() {

        return parseProgram();
    }

    private ASTNode parseProgram() {

        return new ProgramNode();
    }

}



