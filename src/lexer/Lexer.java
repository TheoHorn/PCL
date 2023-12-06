package lexer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Lexer class
 */
public class Lexer {

    /**
     * line number
     */
    private int line_number;

    /**
     * Hashtable of words
     */
    private final Hashtable<String, Word> words;

    /**
     * BufferedReader to read the file
     */
    private BufferedReader br;

    /**
     * List of tokens found
     */
    private final ArrayList<Token> tokens;

    /**
     * Constructor
     * @param f, file to read
     */
    public Lexer(File f){
        this.line_number = 1;
        this.tokens = new ArrayList<>();
        this.words = new Hashtable<>();
        this.reservedKeywords();
        try {
            FileReader fr = new FileReader(f);
            this.br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé" + f.getName());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Reserved keywords in the language
     */
    private void reservedKeywords() {
        this.reserve(new Word(Tag.ACCESS,"access"));
        this.reserve(new Word(Tag.AND,"and"));
        this.reserve(new Word(Tag.BEGIN,"begin"));
        this.reserve(new Word(Tag.ELSE,"else"));
        this.reserve(new Word(Tag.ELSIF,"elsif"));
        this.reserve(new Word(Tag.END,"end"));
        this.reserve(new Word(Tag.FALSE,"false"));
        this.reserve(new Word(Tag.FOR,"for"));
        this.reserve(new Word(Tag.FUNCTION,"function"));
        this.reserve(new Word(Tag.IF,"if"));
        this.reserve(new Word(Tag.IN,"in"));
        this.reserve(new Word(Tag.IS,"is"));
        this.reserve(new Word(Tag.LOOP,"loop"));
        this.reserve(new Word(Tag.NEW,"new"));
        this.reserve(new Word(Tag.NOT,"not"));
        this.reserve(new Word(Tag.NULL,"null"));
        this.reserve(new Word(Tag.OR,"or"));
        this.reserve(new Word(Tag.OUT,"out"));
        this.reserve(new Word(Tag.PROCEDURE,"procedure"));
        this.reserve(new Word(Tag.RECORD,"record"));
        this.reserve(new Word(Tag.REM,"rem"));
        this.reserve(new Word(Tag.RETURN,"return"));
        this.reserve(new Word(Tag.REVERSE,"reverse"));
        this.reserve(new Word(Tag.THEN,"then"));
        this.reserve(new Word(Tag.TRUE,"true"));
        this.reserve(new Word(Tag.TYPE,"type"));
        this.reserve(new Word(Tag.USE,"use"));
        this.reserve(new Word(Tag.WHILE,"while"));
        this.reserve(new Word(Tag.WITH,"with"));
    }

    /**
     * Add a word to the hashtable
     * @param word, word to add
     */
    private void reserve(Word word) {
        this.words.put(word.value, word);
    }

    /**
     * Tokenize the file
     * @throws Exception, if a character is not valid
     */
    public void tokenizer() throws Exception {
        String line = this.br.readLine();
        // While there is a line to read
        while(line != null){
            int i = 0;
            // While there is a character to read
            while (i < line.length()){
                char c = line.charAt(i);
                // If the character is a space, a tab or a carriage return, we skip it
                 if(c == ' ' || c == '\t' || c == '\r'){
                    i++;
                    if (i < line.length()){
                        c = line.charAt(i);
                    }else{
                        break;
                    }
                }// If the character is an end of line return, we change line
                 else if(c == '\n'){
                    break;
                }// If the character is a digit, we read the whole number
                 else if (Character.isDigit(c)){
                    int v = 0;
                    while (Character.isDigit(c)){
                        v = 10 * v + Character.digit(c, 10);
                        i++;
                        if (i < line.length()){
                            c = line.charAt(i);
                        }else{
                            break;
                        }
                    }
                    // If the character is a letter or an underscore, we throw an exception
                    if (Character.isLetter(c)|| c == '_'){
                        throw new Exception("Error at line " + line_number + " : " + c + " is not a valid character");
                    }
                    this.tokens.add(new Num(v));
                }// If the character is a letter, we read the whole word
                 else if(Character.isLetter(c)){
                    StringBuilder s = new StringBuilder();
                    while (Character.isLetterOrDigit(c) || c == '_'){
                        s.append(c);
                        i++;
                        if (i < line.length()){
                            c = line.charAt(i);
                        }else{
                            break;
                        }
                    }
                    String str = s.toString();
                    // If the word is a reserved keyword, we add it to the tokens with the corresponding tag
                    if (this.words.containsKey(str)){
                        this.tokens.add(this.words.get(str));
                    }else{
                        Word w = new Word(Tag.ID, str);
                        this.words.put(str, w);
                        this.tokens.add(w);
                    }
                }// If the character is an operator that can be used alone or with an = , we add it to the tokens
                 else if (c == ':' || c == '<' || c == '>' || c == '/' || c == '*' || c == '+' || c == '(' || c == ')'){
                    i++;
                    char d = line.charAt(i);
                    // If the next character is an =, we add the operator with the corresponding tag
                    if (d == '=') {
                        this.tokens.add(new Operator(String.valueOf(c)+ d));
                        i++;
                    } else {
                        this.tokens.add(new Operator(String.valueOf(c)));
                    }
                 }// If the character is a -, we check if it is a comment or an operator
                 else if (c == '-'){
                    i++;
                    char d = line.charAt(i);

                    if (d == '-'){
                        break;
                    }else{
                        if(!this.tokens.isEmpty()){
                            Token last_token = this.tokens.get(this.tokens.size()-1);
                            if (last_token.getTag() == Tag.OP){
                                this.tokens.add(new Operator("-u"));
                            }else{
                                this.tokens.add(new Operator("-"));
                            }
                        }else{
                            this.tokens.add(new Operator("-u"));
                        }


                    }
                 }// If the character is an equal, we add it to the tokens
                 else if(c == '=' ){
                    this.tokens.add(new Operator(String.valueOf(c)));
                    i++;
                }// If the character is a dot, we check if it is a dot alone or a double dot
                 else if (c == '.') {
                    i++;
                    char d = line.charAt(i);
                    if (d == '.') {
                        this.tokens.add(new Operator(".."));
                        i++;
                    } else {
                        this.tokens.add(new Operator("."));
                    }
                } // If the character is an apostrophe, we add the char to the tokens, and we check if the next character is an apostrophe
                 else if (c == '\''){
                    i++;
                    this.tokens.add(new Char(line.charAt(i)));
                    i++;
                    c = line.charAt(i);
                    if (c != '\''){
                        throw new Exception("Error at line " + line_number + " : ' is used incorrectly");
                    }
                }else if(c == ';' || c == ','){
                    this.tokens.add(new Word(Tag.SEPARATOR,String.valueOf(c)));
                    i++;
                 }else{
                    throw new Exception("Error at line " + line_number + " : " + c + " is not a valid character");
                }
            }
        line_number++;
        line = this.br.readLine();
        }
    }


    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        for (Token t : this.tokens){
            s.append(t.toString()).append("\n");
        }
        return s.toString();
    }


}
