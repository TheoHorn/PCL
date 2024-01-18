package SyntaxerTest;


import lexer.*;
import org.junit.Test;
import org.junit.Before;

import syntaxer.Node;
import syntaxer.SyntaxException;
import syntaxer.Syntaxer;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeclTest {

    private static Syntaxer syntaxer;

    @Before
    public void init() throws Exception {
        syntaxer = new Syntaxer();   
    }

    @Test
    public void test_declaration_procedure() throws Exception {
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));
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
        Node node = syntaxer.DECL();
        Node node2 = new Node("test");
        //assertEquals(syntaxer.getTokens().getFirst(),node2);
    }

    //DEF IDENT FIN ajout
    @Test
    public void test_declaration_id() throws Exception {
        syntaxer.addToken(new Word(Tag.ID, "example"));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Word(Tag.ID, "integer"));
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        syntaxer.addToken(new Word(Tag.TYPE,"integer"));
        syntaxer.addToken(new Operator("("));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Operator("="));
        syntaxer.addToken(new Num(4));
        syntaxer.addToken(new Operator(")"));
        syntaxer.addToken(new Word(Tag.SEPARATOR,";"));
        Node node = syntaxer.DECL();
        Node node2 = new Node("example");
        assertEquals(node.getChildren().get(0),node2);

    }

    @Test
    public void test_declaration_function() throws Exception {
        syntaxer.addToken(new Word(Tag.TYPE, "function"));
        syntaxer.addToken(new Word(Tag.ID,"example"));

        syntaxer.addToken(new Operator("("));
        syntaxer.addToken(new Word(Tag.ID,"a"));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Word(Tag.TYPE,"int"));
        syntaxer.addToken(new Word(Tag.SEPARATOR,";"));
        syntaxer.addToken(new Word(Tag.ID,"b"));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Word(Tag.TYPE, "int"));
        syntaxer.addToken(new Operator(")"));

        syntaxer.addToken(new Word(Tag.RETURN,"return"));
        syntaxer.addToken(new Word(Tag.TYPE,"integer"));
        syntaxer.addToken(new Word(Tag.IS,"is"));
        //DECL_MULT
        syntaxer.addToken(new Word(Tag.BEGIN,"begin"));
        syntaxer.addToken(new Word(Tag.ID,"idetn"));
        syntaxer.addToken(new Word(Tag.SEPARATOR,";"));

        syntaxer.addToken(new Word(Tag.END,"end"));
        syntaxer.addToken(new Word(Tag.ID,"example"));
        //Node node = syntaxer.DECL();
        //Node node2 = new Node("example");
        //assertEquals(node.getChildren().getFirst(),node2);
    }

/*    @Test
    public void test_declaration_type() throws Exception {
        syntaxer.addToken(new Word(Tag.TYPE, "integer"));
        syntaxer.addToken(new Word(Tag.ID, "example"));
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        Node node = syntaxer.DECL();

    }*/

    @Test
    public void test_declaration_id_error() throws Exception {
        syntaxer.addToken(new Word(Tag.TYPE, "example"));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Word(Tag.ID, "integer"));
        assertThrows(SyntaxException.class, () -> syntaxer.DECL());
    }

    @Test
    public void test_declaration_errortype() throws Exception {
        syntaxer.addToken(new Word(Tag.THEN,"then"));
        assertThrows(SyntaxException.class, () -> syntaxer.DECL());
    }

    //to continue
    @Test
    public void test_declaration_procedure_error() {
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));
        syntaxer.addToken(new Word(Tag.ID, "test"));
        syntaxer.addToken(new Word(Tag.IS, "is"));
        syntaxer.addToken(new Word(Tag.ID, "test"));
    }


//PROC()
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
        syntaxer.addToken(new Word(Tag.OR,"or"));
        //assertThrows(SyntaxException.class, () -> syntaxer.PROC());
    }

     @Test
     public void test_ajout_instruction(){

     }

    @Test
    public void test_identificateur_exist_error() throws Exception {

        syntaxer.addToken(new Word(Tag.END, "end"));
        assertThrows(SyntaxException.class, () -> syntaxer.IDENT_EXISTE());
    }

    @Test
    public void test_declaration_fonctions_multiples_error() throws Exception {
        //mettre un autre Tag que celui attendu pour vérifier la détection d'erreur
        syntaxer.addToken(new Word(Tag.THEN,"then"));
        assertThrows(SyntaxException.class, () -> syntaxer.DECL_MULT());
    }

    @Test
    //La méthode retourne bien la liste de node traités
    public void test_declaration_fonctions_multiples(){
        syntaxer.addToken(new Word(Tag.FUNCTION, "function"));
        syntaxer.addToken(new Word(Tag.ID, "max"));
        syntaxer.addToken(new Operator("("));
        syntaxer.addToken(new Word(Tag.ID,"a"));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Word(Tag.TYPE,"int"));
        syntaxer.addToken(new Word(Tag.SEPARATOR,";"));
        syntaxer.addToken(new Word(Tag.ID,"b"));
        syntaxer.addToken(new Operator(":"));
        syntaxer.addToken(new Word(Tag.TYPE, "int"));
        syntaxer.addToken(new Operator(")"));
    }

}
