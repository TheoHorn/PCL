package SyntaxerTest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import lexer.Tag;
import lexer.Word;
import syntaxer.Node;
import syntaxer.SyntaxException;
import syntaxer.Syntaxer;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IdentTest {

    private static Syntaxer syntaxer;

    @Before
    public void init() throws Exception {
        syntaxer = new Syntaxer();   
    }
    
    @Test
    public void test_ident() throws Exception {
        syntaxer.addToken(new Word(Tag.ID, "mot"));
        Node n = syntaxer.IDENT();
        Node n2 = new Node("IDENT");
        n2.addChild(new Node("mot"));
        assertTrue(n.equals(n2));
    }

    @Test
    public void test_ident_error() throws SyntaxException {
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));
        assertThrows(SyntaxException.class, () -> syntaxer.IDENT());
    }
    
}
