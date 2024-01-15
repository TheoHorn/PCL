import lexer.Lexer;
import syntaxer.Node;
import syntaxer.Syntaxer;

import java.io.File;

public class main {
    public static void main(String[] args) throws Exception {
        File inputFile = new File("src/test.txt");

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

            syntaxer.writeJSONToFile(ast, "arbre.json");

            System.out.println("ast done");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

