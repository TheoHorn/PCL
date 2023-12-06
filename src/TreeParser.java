import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import lexer.*;



public class TreeParser {
    
    public ArrayList<lexer.Token> Axiome;
    

    TreeParser(ArrayList<lexer.Token> Axiome){
        this.Axiome=Axiome;
    }


    public void Axiome(ArrayList<lexer.Token> tokens) throws Exception {
        if(tokens.get(0).getTag()==Tag.WITH){Axiome.add(tokens.get(0)); tokens.remove(0);}
        if (tokens.get(0) instanceof lexer.Word & ((lexer.Word) tokens.get(0)).getValue()=="Text_IO"){Axiome.add(tokens.get(0)); tokens.remove(0);}
        if(tokens.get(0).getTag()==Tag.OP){Axiome.add(tokens.get(0)); tokens.remove(0);}
        if (tokens.get(0) instanceof lexer.Word || ((lexer.Word) Axiome.get(3)).getValue()!="Text_IO"){throw new Exception("Syntax Error");}
        if(tokens.get(0).getTag()!=Tag.SEPARATOR){Axiome.add(tokens.get(0)); tokens.remove(0);}
        if(tokens.get(0).getTag()!=Tag.USE){Axiome.add(tokens.get(0)); tokens.remove(0);}
        if(tokens.get(0).getTag()!=Tag.OP){Axiome.add(tokens.get(0)); tokens.remove(0);} //acs
        if(tokens.get(0) instanceof lexer.Word || ((lexer.Word) Axiome.get(0)).getValue()!="Text_IO"){Axiome.add(tokens.get(0)); tokens.remove(0);}
        if(tokens.get(0).getTag()!=Tag.SEPARATOR){Axiome.add(tokens.get(0)); tokens.remove(0);}
        PROC(tokens);
        }
    
    public void DECL(ArrayList<lexer.Token> tokens) throws Exception {
    
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
            if(tokens.get(0) instanceof lexer.Word || ((lexer.Word) Axiome.get(0)).getValue()!=":"){
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

    public void DEF_IDENT(ArrayList<lexer.Token> tokens) throws Exception{
        if(tokens.get(0).getTag()==Tag.SEPARATOR){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);}
        if(tokens.get(0).getTag()==Tag.IF){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            DEF_IDENT_FIN(tokens);}
        else{throw new Exception("Syntax Error");}
    }

    public void DEF_IDENT_FIN(ArrayList<lexer.Token> tokens) throws Exception {
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

    public void PROC(ArrayList<lexer.Token> tokens) throws Exception {
    
    }
    }

    
    







