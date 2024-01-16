package SyntaxerTest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import lexer.Operator;
import lexer.Tag;
import lexer.Word;
import syntaxer.Node;
import syntaxer.SyntaxException;
import syntaxer.Syntaxer;

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
        Node n2 = new Node("mot");
        assertTrue(n.equals(n2));
    }

    @Test
    public void test_ident_error() throws SyntaxException {
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));
        assertThrows(SyntaxException.class, () -> syntaxer.IDENT());
    }

    @Test
    public void test_ident_existe_ok() throws SyntaxException {
        syntaxer.addToken(new Word(Tag.ID, "mot"));
        Node n = syntaxer.IDENT_EXISTE();
        Node n2 = new Node("mot");
        assertTrue(n.equals(n2));
    }

    @Test
    public void test_ident_existe_pasIdf() throws SyntaxException {
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));
        assertThrows(SyntaxException.class, () -> syntaxer.IDENT_EXISTE());
    }

    @Test
    public void test_ident_existe_idf_non_present() throws SyntaxException {
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        Node n = syntaxer.IDENT_EXISTE();
        Node n2 = null;
        assertTrue(n == n2);
    }

}
