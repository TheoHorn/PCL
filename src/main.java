import lexer.Lexer;

import java.io.File;

public class main {
    public static void main(String[] args) {
        File f = new File("src\\test.txt");
    
        Lexer l = new Lexer(f);
        try {
            l.tokenizer();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        //System.out.println(l.getTokens().size());
        l.getTokens().forEach(System.out::println);
    }
}

