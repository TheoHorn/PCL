package SyntaxerTest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import lexer.Operator;
import lexer.Tag;
import lexer.Word;
import syntaxer.Node;
import syntaxer.SyntaxException;
import syntaxer.Syntaxer;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

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

    @Test
    public void test_ident_existe_ok() throws SyntaxException {
        syntaxer.addToken(new Word(Tag.ID, "mot"));
        Node n = syntaxer.IDENT_EXISTE();
        Node n2 = new Node("IDENT_EXISTE");
        Node child = new Node("IDENT");
        child.addChild(new Node("mot"));
        n2.addChild(child);
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
        Node n2 = new Node("IDENT_EXISTE");
        assertTrue(n.equals(n2));
    }

    @Test
    public void test_ident_suite_ok()throws SyntaxException{
        syntaxer.addToken(new Operator(","));
        syntaxer.addToken(new Word(Tag.ID, "mot"));
        syntaxer.addToken(new Word(Tag.OP, ":"));
        ArrayList<Node> n = syntaxer.IDENT_SUITE();

        ArrayList<Node> n2 = new ArrayList<>();
        n2 = syntaxer.IDENT_PLUS();

        assertTrue(n.equals(n2));
        
    }


    @Test
    public void test_ident_plus_ok() throws SyntaxException{
        syntaxer.addToken(new Word(Tag.ID, "mot"));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Word(Tag.ID, "mot2"));
        ArrayList<Node> n = syntaxer.IDENT_PLUS();
        Node n2 = new Node("IDENT_PLUS");
        Node child = new Node("IDENT");
        child.addChild(new Node("mot"));
        Node child2 = new Node("IDENT");
        child2.addChild(new Node("mot2"));

        n2.addChild(child);
        System.out.println(n2);
        System.out.println(n);
        assertTrue(n.equals(n2));
    }

    
}
