package SyntaxerTest;

import lexer.Operator;
import lexer.Tag;
import lexer.Word;
import org.junit.Before;
import org.junit.Test;
import syntaxer.Node;
import syntaxer.Syntaxer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileTest {

    private static Syntaxer syntaxer;

    @Before
    public void init() throws Exception {
        syntaxer = new Syntaxer();
    }
    
    @Test
    public void test_file() throws Exception{
        syntaxer.addToken(new Word(Tag.WITH, "with"));
        syntaxer.addToken(new Word(Tag.ID, "Ada"));
        syntaxer.addToken(new Operator( "."));
        syntaxer.addToken(new Word(Tag.ID, "Text_IO"));
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        syntaxer.addToken(new Word(Tag.USE, "use"));
        syntaxer.addToken(new Word(Tag.ID, "Ada"));
        syntaxer.addToken(new Operator( "."));
        syntaxer.addToken(new Word(Tag.ID, "Text_IO"));
        syntaxer.addToken(new Word(Tag.SEPARATOR, ";"));
        syntaxer.addToken(new Word(Tag.PROCEDURE, "procedure"));

        syntaxer.addToken((new Word(Tag.FUNCTION, "function")));

        Node result = syntaxer.File();

        assertNotNull(result);
    }



}
