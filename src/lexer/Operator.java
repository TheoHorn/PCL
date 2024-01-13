package lexer;

/**
 * Operator class
 */
public class Operator extends Token{

    /**
     * Operator value
     */
    public String value;

    /**
     * Constructor
     * @param s, type of operator
     */
    public Operator(String s){
        super(Tag.OP);
        value = defValue(s);
    }

    /**
     * Define the value of the operator
     * @param s, type of operator
     * @return the value of the operator
     */
    public String defValue(String s){
        return switch (s) {
            case "+" -> "add";
            case "-" -> "sub";
            case "-u" -> "neg";
            case "*" -> "mul";
            case "/" -> "div";
            case "/=" -> "res";
            case "=" -> "eq";
            case "<" -> "lt";
            case ">" -> "gt";
            case "<=" -> "le";
            case ">=" -> "ge";
            case ":" -> "def";
            case ":=" -> "afc";
            case "." -> "acs";
            case ".." -> "adr";
            case "(" -> "lpa";
            case ")" -> "rpa";
            default -> "unknown";
        };
    }

    public String getOp(){
        return switch (value) {
            case "add" -> "+";
            case "sub" -> "-";
            case "neg" -> "-u";
            case "mul" -> "*";
            case "div" -> "/";
            case "res" -> "/=";
            case "eq" -> "=";
            case "lt" -> "<";
            case "gt" -> ">";
            case "le" -> "<=";
            case "ge" -> ">=";
            case "def" -> ":";
            case "afc" -> ":=";
            case "acs" -> ".";
            case "adr" -> "..";
            case "lpa" -> "(";
            case "rpa" -> ")";
            default -> "unknown";
        };
    }

    @Override
    public String toString() {
        return "Operator{" +
                "tag=" + tag +
                ", value='" + value + '\'' +
                '}';
    }

    /**
     * Get operator value
     * @return operator value
     */
    public String getValue() {
        return value;
    }
    
}
