import lexer.Lexer;
import syntaxer.Node;
import syntaxer.Syntaxer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

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

            Node parseTree = syntaxer.launch();

            parseTree.writeJSONToFile("src/arbre.json");

            String pythonCommand = "python3";  
            String pythonScript = "src/arbre.py";  

            ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand, pythonScript);

            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
            }

            int exitCode = process.waitFor();
            
            System.out.println("Code de sortie : " + exitCode);

            System.out.println("json file written");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

