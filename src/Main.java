import lexer.Lexer;
import syntaxer.Node;
import syntaxer.Syntaxer;

import java.io.File;

public class Main {
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
        lexer.getTokens().forEach(System.out::println);
        try {

            Node ast = syntaxer.launch();

            //syntaxer.writeJSONToFile(ast, "src/arbre.json");

            //System.out.println("json file written");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

