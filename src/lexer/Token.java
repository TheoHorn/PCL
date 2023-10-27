package lexer;

/**
 * Token class
 */
public class Token {

    /**
     * token's tag
     */
    public final Tag tag;

    /**
     * Constructor
     * @param t, token's tag
     */
    public Token(Tag t){
        this.tag = t;
    }

    public Tag getTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tag=" + tag +
                '}';
    }
}
