package lexer;

/**
 * Word class
 */
public class Word extends Token{

    /**
     * Word value
     */
    public final String value;

    /**
     * Constructor
     * @param t, tag corresponding to the appropriate word
     * @param s, word value
     */
    public Word(Tag t,String s){
        super(t);
        value = s;
    }

    @Override
    public String toString() {
        return "Word{" +
                "tag=" + tag +
                ", value='" + value + '\'' +
                '}';
    }
}
