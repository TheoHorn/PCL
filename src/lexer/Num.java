package lexer;

/**
 * Num class
 */
public class Num extends Token{

    /**
     * Num value
     */
    public final int value;

    /**
     * Constructor
     * @param v, Num value
     */
    public Num(int v) {
        super(Tag.NUM);
        this.value = v;
    }

    @Override
    public String toString() {
        return "Num{" +
                "tag=" + tag +
                ", value=" + value +
                '}';
    }
}
