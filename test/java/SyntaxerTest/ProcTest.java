package SyntaxerTest;

import lexer.*;
import org.junit.Test;
import org.junit.Before;

import syntaxer.Node;
import syntaxer.SyntaxException;
import syntaxer.Syntaxer;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProcTest {

    private static Syntaxer syntaxer;

    @Before
    public void init() throws Exception {
        syntaxer = new Syntaxer();
    }


    @Test
    public void procedure() throws Exception {
        syntaxer.addToken(new Word(Tag.ID, "test"));
        syntaxer.addToken(new Word(Tag.IS, "is"));
        //DECL_MULT
        syntaxer.addToken(new Word(Tag.BEGIN, "begin"));
        //INSTR_PLUS
        syntaxer.addToken(new Word(Tag.ID,"ident"));
        syntaxer.addToken(new Word(Tag.SEPARATOR,";"));
        syntaxer.addToken(new Word(Tag.END, "end"));
        //IDENT_EXIST
        syntaxer.addToken(new Word(Tag.ID, "test"));  // Example for IDENT_EXISTE, adjust based on your grammar
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        Node node = syntaxer.PROC();
        Node node2 = new Node("test");
        assertEquals(node2, node.getChildren().get(0));
    }

    @Test
    public void test_procedure_error_begin() throws Exception {
        syntaxer.addToken(new Word(Tag.ID, "test"));
        syntaxer.addToken(new Word(Tag.IS, "is"));
        //DECL_MULT
        //INSTR_PLUS
        syntaxer.addToken(new Word(Tag.END, "end"));
        //IDENT_EXIST
        syntaxer.addToken(new Word(Tag.ID, "test"));  // Example for IDENT_EXISTE, adjust based on your grammar
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        assertThrows(SyntaxException.class, () -> syntaxer.PROC());
    }

    @Test
    public void test_procedure_error_end() throws SyntaxException {
        syntaxer.addToken(new Word(Tag.ID, "test"));
        syntaxer.addToken(new Word(Tag.IS, "is"));
        //DECL_MULT
        syntaxer.addToken(new Word(Tag.BEGIN, "begin"));
        //INSTR_PLUS
        //IDENT_EXIST
        syntaxer.addToken(new Word(Tag.ID, "test"));  // Example for IDENT_EXISTE, adjust based on your grammar
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        assertThrows(NullPointerException.class, () -> syntaxer.PROC());
    }

    @Test
    public void test_procedure_error_separator() throws Exception {
        syntaxer.addToken(new Word(Tag.ID, "test"));
        syntaxer.addToken(new Word(Tag.IS, "is"));
        //DECL_MULT
        syntaxer.addToken(new Word(Tag.BEGIN, "begin"));
        //INSTR_PLUS

        syntaxer.addToken(new Word(Tag.END, "end"));
        //IDENT_EXIST
        syntaxer.addToken(new Word(Tag.ID, "test"));  // Example for IDENT_EXISTE, adjust based on your grammar

        assertThrows(SyntaxException.class, () -> syntaxer.PROC());
    }


    @Test
    public void test_procedure_error() throws Exception {
        //mettre un autre Tag que celui attendu pour vérifier la détection d'erreur
        syntaxer.addToken(new Word(Tag.ID,"test"));
        syntaxer.addToken(new Word(Tag.IS,"is"));
        syntaxer.addToken(new Word(Tag.THEN,"then"));
        assertThrows(SyntaxException.class, () -> syntaxer.PROC());
    }

}
