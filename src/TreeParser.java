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
        // on recupere le premier token en le supprimant de la tete de file
        Token current_token = tokens.poll();
        // on recupere le tag du token
        Tag current_tag = current_token.getTag();

        //first token : with
        if(current_tag == Tag.WITH){
            // le token est bon, on recupere le suivant en le supprimant de la tete de file
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //second token : Ada
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Ada"){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //third token : .
        if(current_tag==Tag.OP && ((Operator) current_token).getValue().equals("acs")){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new SyntaxException(current_token.toString());
        }

        //fourth token : Text_IO
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Text_IO"){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //fifth token : ;
        if(current_tag==Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //sixth token : use
         if(current_tag!=Tag.ID && ((Word) current_token).getValue().equals("use")){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //seventh token : Ada
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Ada"){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //eighth token : .
        if(current_tag==Tag.OP && ((Operator) current_token).getValue().equals("acs")){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new SyntaxException(current_token.toString());
        }

        //ninth token : Text_IO
        if (current_tag == Tag.ID && ((Word) current_token).getValue()=="Text_IO"){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //tenth token : ;
        if(current_tag==Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
            current_token = this.tokens.poll();
            current_tag = current_token.getTag();
        }else{
            throw new  SyntaxException(current_token.toString());
        }

        //eleventh token : procedure
        if(!(current_tag==Tag.PROCEDURE)){
            //pas de poll car on veut garder le token pour le prochain appel de fonction
             throw new  SyntaxException(current_token.toString());
        }

        Node proc = PROC();
        // on ajoute le noeud de la procedure a l'arbre
        this.arbre.addChild(proc);
        }
    
    public Node DECL() throws Exception {
        Node decl = new Node("DECL");
        // on recupere le premier token sans le supprimer car on veut le garder pour le prochain appel de fonction (ici IDENT_PLUS)
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        // on regarde les premiers et les règles associées dans notre table puis on les applique

        //first rule : type IDENT DEF_IDENT
        if(current_tag == Tag.TYPE){
            // on le supprime car on vient de l'utiliser
            tokens.poll();
            Node ident = IDENT(tokens);
            Node def_ident = DEF_IDENT(tokens);
            decl.addChild(ident);
            decl.addChild(def_ident);
        }
        // second rule : procedure PROC
        else if (current_tag == Tag.PROCEDURE){
            tokens.poll();
            Node proc = PROC(tokens);
            decl.addChild(proc);
        }
        // third rule : function FUNC
        else if (current_tag == Tag.FUNCTION){
            tokens.poll();
            Node func = FUNC(tokens);
            decl.addChild(func);
        }
        // fourth rule : IDENT_PLUS : TYPE AFFECT_EXIST
        else if (current_tag == Tag.ID){
            Node ident_plus = IDENT_PLUS(tokens);
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.OP && ((Operator) current_token).getValue().equals(":")){
                Node type = TYPE(tokens);
                Node affect_exist = AFFECT_EXIST(tokens);
                decl.addChild(ident_plus);
                decl.addChild(type);
                decl.addChild(affect_exist);
            }else{
                throw new SyntaxException(current_token.toString());
            }
        }
        else{
            throw new SyntaxException(current_token.toString());
        }
        return decl;
    }

    private Node IDENT(ArrayDeque<Token> tokens2) {
        return null;
    }

    public Node DEF_IDENT() throws Exception{
        if(tokens.get(0).getTag()==Tag.SEPARATOR){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);}
        if(tokens.get(0).getTag()==Tag.IF){
            Axiome.add(tokens.get(0)); 
            tokens.remove(0);
            DEF_IDENT_FIN(tokens);}
        else{throw new Exception("Syntax Error");}
    }

    public Node DEF_IDENT_FIN() throws Exception {
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

    public Node PROC() throws Exception {
    
    }
    }

    
    







