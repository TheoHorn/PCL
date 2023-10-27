package lexer;

/**
 * Char class
 */
public class Char extends Token{

    /**
     * char's value
     */
    public final char value;

    /**
     * Constructor
     * @param value, char's value
     */
    public Char(char value) {
        super(Tag.CHAR);
        this.value = value;
    }

    @Override
    public String toString() {
        return "Char{" +
                "tag=" + tag +
                ", value=" + value +
                '}';
    }
}
