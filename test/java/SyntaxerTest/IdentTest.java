package SyntaxerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import lexer.Tag;
import lexer.Word;
import syntaxer.Node;
import syntaxer.Syntaxer;

public class IdentTest {

    private static Syntaxer syntaxer;

    @Before
    public void init() throws Exception {
        syntaxer = new Syntaxer();   
    }
    
    //to continue
    @Test
    public void test_declaration_procedure() throws Exception {
        syntaxer.addToken(new Word(Tag.ID, "mot"));
        Node n = syntaxer.IDENT();
        
        assertTrue(n.equals(new Node("IDENT")));
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
