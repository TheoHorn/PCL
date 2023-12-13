package syntaxer;
public class SyntaxException extends Exception {
    public SyntaxException(String message) {
        super("Erreur de syntaxe : "+message+"n'est pas un token valide");
    }
    
}
