package SyntaxerTest;


import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import lexer.Tag;
import lexer.Word;
import syntaxer.Syntaxer;

public class DeclTest {

    private static Syntaxer syntaxer;

    @BeforeAll
    public static void init() throws Exception {
        syntaxer = new Syntaxer();   
    }
    
    //to continue
    @Test
    public void test_declaration_procedure() {
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));
        syntaxer.addToken(new Word(Tag.ID, "test"));
        syntaxer.addToken(new Word(Tag.IS, "is"));
    }

    //to continue
    @Test
    public void test_declaration_procedure_error() {
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));
        syntaxer.addToken(new Word(Tag.ID, "test"));
        syntaxer.addToken(new Word(Tag.IS, "is"));
        syntaxer.addToken(new Word(Tag.ID, "test"));
    }
}
