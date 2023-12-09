import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import lexer.*;



public class TreeParser {
    
    private Node arbre;
    private ArrayDeque<Token> tokens;
    
    public TreeParser(ArrayDeque<Token> tokens) throws Exception {
        this.tokens = tokens;
        this.arbre = null;
        this.File();
    }

    public void File() throws Exception {
        this.arbre = new Node("File");
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        //first token : with
        if(current_tag == Tag.WITH){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //second token : Ada
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Ada"){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //third token : .
        if(current_tag==Tag.OP && ((Operator) current_token).getValue().equals("acs")){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new SyntaxException(current_token.toString());
        }

        //fourth token : Text_IO
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Text_IO"){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //fifth token : ;
        if(current_tag==Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //sixth token : use
         if(current_tag!=Tag.ID && ((Word) current_token).getValue().equals("use")){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }

        //seventh token : Ada
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Ada"){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //eighth token : .
        if(current_tag==Tag.OP && ((Operator) current_token).getValue().equals("acs")){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new SyntaxException(current_token.toString());
        }

        //ninth token : Text_IO
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Text_IO"){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //tenth token : ;
        if(current_tag==Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //eleventh token : procedure
        if(current_tag==Tag.PROCEDURE){
            this.tokens.poll();
            current_token = tokens.peek();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        PROC();
        
        
        }
    
    public void DECL() throws Exception {
    
        if(tokens.get(0).getTag()==Tag.TYPE){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            IDENT(tokens);
            DEF_IDENT(tokens);
        }
        if(tokens.get(0).getTag()==Tag.PROCEDURE){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            PROC(tokens);
        }
        if(tokens.get(0).getTag()==Tag.FUNCTION){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            FUNC(tokens);
        }
        else{
            
            IDENT_PLUS(tokens);
            if(tokens.get(0) instanceof Word || ((Word) Axiome.get(0)).getValue()!=":"){
                Axiome.add(tokens.get(0)); 
                tokens.remove(0);
                if(tokens.get(0).getTag()==Tag.TYPE){
                    Axiome.add(tokens.get(0)); 
                    tokens.remove(0);
                    AFFECT_EXIST(tokens);
                    if(tokens.get(0).getTag()==Tag.SEPARATOR){
                        Axiome.add(tokens.get(0)); 
                        tokens.remove(0);}
                }
            }
            

        }
    }

    public void DEF_IDENT() throws Exception{
        if(tokens.get(0).getTag()==Tag.SEPARATOR){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);}
        if(tokens.get(0).getTag()==Tag.IF){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            DEF_IDENT_FIN(tokens);}
        else{throw new Exception("Syntax Error");}
    }

    public void DEF_IDENT_FIN() throws Exception {
        if(tokens.get(0).getTag()==Tag.ACCESS){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            IDENT(tokens);
            if(tokens.get(0).getTag()==Tag.SEPARATOR){
                Axiome.add(tokens.get(0)); 
                tokens.remove(0);}
        }
        if(tokens.get(0).getTag()==Tag.RECORD){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            CHAMPS_PLUS(tokens);
            if(tokens.get(0).getTag()==Tag.END){
                Axiome.add(tokens.get(0)); 
                tokens.remove(0);
                if(tokens.get(0).getTag()==Tag.RECORD){
                    Axiome.add(tokens.get(0)); 
                    tokens.remove(0);
                    if(tokens.get(0).getTag()==Tag.SEPARATOR){
                        Axiome.add(tokens.get(0)); 
                        tokens.remove(0);
                    }
                }
            }
        }

    }

    public void PROC() throws Exception {
    
    }
    }

    
    







