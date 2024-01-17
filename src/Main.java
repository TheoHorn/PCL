import lexer.Lexer;
import syntaxer.Node;
import syntaxer.Syntaxer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) throws Exception {
        File inputFile = new File("src/test.txt");

        Lexer lexer = new Lexer(inputFile);
        try {
            lexer.tokenizer();
            System.out.println(lexer.print());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Syntaxer syntaxer = new Syntaxer(lexer.getTokens());
        try {

            Node parseTree = syntaxer.launch();

            parseTree.writeJSONToFile("src/arbre.json");

            String pythonCommand = "python3";  
            String pythonScript = "src/arbre.py";  

            ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand, pythonScript);

            processBuilder.start();

            Files.copy(Paths.get("src/arbre.png"), Paths.get("arbre.png"), StandardCopyOption.REPLACE_EXISTING);
            

            System.out.println("json file written");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

