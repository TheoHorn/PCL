import lexer.Lexer;
import syntaxer.Node;
import syntaxer.Syntaxer;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File inputFile = new File("src/test2.txt");

        Lexer lexer = new Lexer(inputFile);
        try {
            lexer.tokenizer();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Syntaxer syntaxer = new Syntaxer(lexer.getTokens());
        try {

            Node ast = syntaxer.launch();

            ast.writeJSONToFile("src/arbre.json");

            System.out.println("json file written");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

