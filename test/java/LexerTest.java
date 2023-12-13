
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void test_space(){
        File f = new File("test/ressource/space.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Word{tag=ID, value='abc'}
                Word{tag=ID, value='def'}
                Word{tag=ID, value='ghi'}
                """);
    }

    @Test
    public void test_carriage_return(){
        File f = new File("test/ressource/carriage_return.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Word{tag=ID, value='abc'}
                Word{tag=ID, value='def'}
                Word{tag=ID, value='ghi'}
                """);
    }

    @Test
    public void test_tabulation(){
        File f = new File("test/ressource/tabulation.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Word{tag=ID, value='abc'}
                Word{tag=ID, value='def'}
                Word{tag=ID, value='ghi'}
                """);
    }

    @Test
    public void test_alpha_normal(){
        File f = new File("test/ressource/alpha.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Word{tag=ID, value='alph0'}
                Word{tag=ID, value='b8ta'}
                Word{tag=ID, value='d3758'}
                Word{tag=ID, value='eps1lon'}
                Word{tag=ID, value='z3ta'}
                Word{tag=ID, value='e'}
                """);
    }

    @Test
    public void test_alpha_underscore(){
        File f = new File("test/ressource/alpha2.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Word{tag=ID, value='al_ph0'}
                Word{tag=ID, value='b8ta'}
                Word{tag=ID, value='d_3758__'}
                Word{tag=ID, value='eps1__lon'}
                Word{tag=ID, value='z3ta'}
                Word{tag=ID, value='eta'}
                """);
    }

    @Test
    public void test_alpha_error_number_first(){
        File f = new File("test/ressource/alpha3.txt");
        Lexer lex = new Lexer(f);
        Exception exception = assertThrows(Exception.class, lex::tokenizer);
        assertEquals(exception.getMessage(), "Error at line 1 : _ is not a valid character");
    }

    @Test
    public void test_alpha_error_underscore_first(){
        File f = new File("test/ressource/alpha4.txt");
        Lexer lex = new Lexer(f);
        Exception exception = assertThrows(Exception.class, lex::tokenizer);
        assertEquals(exception.getMessage(), "Error at line 1 : _ is not a valid character");
    }

    @Test void test_alpha_symbols(){
        File f = new File("test/ressource/alpha_symbols.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
              Operator{tag=OP, value='lpa'}
              Word{tag=ID, value='abc'}
              Operator{tag=OP, value='rpa'}
              Word{tag=SEPARATOR, value=','}
              Word{tag=SEPARATOR, value=';'}
              Word{tag=SEPARATOR, value=','}
              Word{tag=SEPARATOR, value=','}
              Word{tag=SEPARATOR, value=';'}
              Word{tag=SEPARATOR, value=';'}
              Word{tag=SEPARATOR, value=';'}
              Operator{tag=OP, value='def'}
              Word{tag=ID, value='mot34'}
              Operator{tag=OP, value='def'}
              Word{tag=ID, value='avb'}
              Operator{tag=OP, value='afc'}
              Num{tag=NUM, value=35}
              """);
    }

    @Test
    public void test_only_unique_operator(){
        File f = new File("test/ressource/only_unique_operator.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Num{tag=NUM, value=25}
                Operator{tag=OP, value='add'}
                Num{tag=NUM, value=24}
                Word{tag=ID, value='petit'}
                Operator{tag=OP, value='add'}
                Word{tag=ID, value='grand'}
                Num{tag=NUM, value=1}
                Operator{tag=OP, value='sub'}
                Num{tag=NUM, value=2}
                Num{tag=NUM, value=42}
                Operator{tag=OP, value='sub'}
                Num{tag=NUM, value=35}
                Word{tag=ID, value='x'}
                Operator{tag=OP, value='mul'}
                Word{tag=ID, value='y'}
                Word{tag=ID, value='talent'}
                Operator{tag=OP, value='mul'}
                Word{tag=ID, value='bg'}
                Word{tag=ID, value='moi'}
                Operator{tag=OP, value='eq'}
                Word{tag=ID, value='toi'}
                Word{tag=ID, value='truc34'}
                Operator{tag=OP, value='eq'}
                Word{tag=ID, value='chose43'}
                """);
    }

    @Test
    public void test_double_operator(){
        File f = new File("test/ressource/double_operator.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Num{tag=NUM, value=24}
                Operator{tag=OP, value='res'}
                Num{tag=NUM, value=5}
                Num{tag=NUM, value=24}
                Operator{tag=OP, value='res'}
                Num{tag=NUM, value=6}
                Num{tag=NUM, value=32}
                Operator{tag=OP, value='ge'}
                Num{tag=NUM, value=24}
                Operator{tag=OP, value='le'}
                Num{tag=NUM, value=10}
                """);
    }

    @Test
    public void test_comment(){
        File f = new File("test/ressource/double_operator_commented.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
                Num{tag=NUM, value=24}
                Operator{tag=OP, value='res'}
                Num{tag=NUM, value=5}
                Num{tag=NUM, value=24}
                Operator{tag=OP, value='res'}
                Num{tag=NUM, value=6}
                Num{tag=NUM, value=32}
                Operator{tag=OP, value='ge'}
                Num{tag=NUM, value=24}
                Operator{tag=OP, value='le'}
                Num{tag=NUM, value=10}
                """);
    }

    @Test
    public void test_principal_if(){
        File f = new File("test/ressource/if.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
               Word{tag=IF, value='if'}
               Operator{tag=OP, value='lpa'}
               Word{tag=NOT, value='not'}
               Word{tag=ID, value='x'}
               Operator{tag=OP, value='eq'}
               Num{tag=NUM, value=24}
               Operator{tag=OP, value='rpa'}
               Word{tag=THEN, value='then'}
               Word{tag=ID, value='x'}
               Operator{tag=OP, value='afc'}
               Num{tag=NUM, value=24}
               Word{tag=SEPARATOR, value=';'}
               Word{tag=ID, value='y'}
               Operator{tag=OP, value='afc'}
               Num{tag=NUM, value=20}
               Word{tag=SEPARATOR, value=';'}
               Word{tag=ID, value='res'}
               Operator{tag=OP, value='acs'}
               Word{tag=ID, value='distance'}
               Operator{tag=OP, value='afc'}
               Num{tag=NUM, value=20}
               Operator{tag=OP, value='mul'}
               Num{tag=NUM, value=24}
               Word{tag=SEPARATOR, value=';'}
               Word{tag=ELSE, value='else'}
               Word{tag=ID, value='var'}
               Operator{tag=OP, value='afc'}
               Word{tag=TRUE, value='true'}
               Word{tag=SEPARATOR, value=';'}
               Word{tag=END, value='end'}
               Word{tag=IF, value='if'}
               Word{tag=SEPARATOR, value=';'}
                """);
    }

    @Test
    public void test_principal_loop(){
        File f = new File("test/ressource/loop.txt");
        Lexer lex = new Lexer(f);
        try {
            lex.tokenizer();
        }catch (Exception ignored){}
        assertEquals(lex.toString(), """
            Word{tag=FOR, value='for'}
            Word{tag=ID, value='x'}
            Word{tag=IN, value='in'}
            Num{tag=NUM, value=1}
            Operator{tag=OP, value='adr'}
            Num{tag=NUM, value=10}
            Word{tag=LOOP, value='loop'}
            Word{tag=ID, value='y'}
            Operator{tag=OP, value='afc'}
            Word{tag=ID, value='x'}
            Operator{tag=OP, value='add'}
            Word{tag=ID, value='y'}
            Word{tag=SEPARATOR, value=';'}
            Word{tag=END, value='end'}
            Word{tag=LOOP, value='loop'}
            Word{tag=SEPARATOR, value=';'}
            Word{tag=ID, value='x'}
            Operator{tag=OP, value='afc'}
            Word{tag=ID, value='y'}
            Operator{tag=OP, value='div'}
            Word{tag=ID, value='x'}
            Word{tag=SEPARATOR, value=';'}
            Word{tag=WHILE, value='while'}
            Word{tag=ID, value='y'}
            Operator{tag=OP, value='lt'}
            Num{tag=NUM, value=10}
            Word{tag=LOOP, value='loop'}
            Word{tag=ID, value='y'}
            Operator{tag=OP, value='afc'}
            Word{tag=ID, value='y'}
            Operator{tag=OP, value='add'}
            Num{tag=NUM, value=1}
            Word{tag=SEPARATOR, value=';'}
            Word{tag=END, value='end'}
            Word{tag=LOOP, value='loop'}
            Word{tag=SEPARATOR, value=';'}
            """);
    }
}
