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
            //pas de poll() car on veut garder le token pour le prochain appel de fonction
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
            Node ident = IDENT();
            Node def_ident = DEF_IDENT();
            decl.addChild(ident);
            decl.addChild(def_ident);
        }
        // second rule : procedure PROC
        else if (current_tag == Tag.PROCEDURE){
            tokens.poll();
            Node proc = PROC();
            decl.addChild(proc);
        }
        // third rule : function FUNC
        else if (current_tag == Tag.FUNCTION){
            tokens.poll();
            Node func = FUNC();
            decl.addChild(func);
        }
        // fourth rule : IDENT_PLUS : TYPE AFFECT_EXIST
        else if (current_tag == Tag.ID){
            Node ident_plus = IDENT_PLUS();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.OP && ((Operator) current_token).getValue().equals("def")){
                Node type = TYPE();
                Node affect_exist = AFFECT_EXIST();
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

    public Node DEF_IDENT() throws Exception{
        Node def_ident = new Node("DEF_IDENT");
        Token current_token = tokens.poll();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.IS){
            Node def_ident_fin = DEF_IDENT_FIN();
            def_ident.addChild(def_ident_fin);
        }else if (current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
            Node def_ident_fin = DEF_IDENT_FIN();
            def_ident.addChild(def_ident_fin);
        }else{
            throw new SyntaxException(current_token.toString());
        }
    }

    public Node DEF_IDENT_FIN() throws Exception {
        Node def_ident_fin = new Node("DEF_IDENT_FIN");
        Token current_token = tokens.poll();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.ACCESS){
            Node ident = IDENT();
            def_ident_fin.addChild(ident);
        }else if (current_tag == Tag.RECORD){
            Node champs_plus = CHAMPS_PLUS();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.END){
                current_token = tokens.poll();
                current_tag = current_token.getTag();
                if(current_tag == Tag.RECORD){
                    current_token = tokens.poll();
                    current_tag = current_token.getTag();
                    if(current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
                        def_ident_fin.addChild(champs_plus);
                    }else{
                        throw new SyntaxException(current_token.toString());
                    }
                }else{
                    throw new SyntaxException(current_token.toString());
                }
            }else{
                throw new SyntaxException(current_token.toString());
            }
        }else{
            throw new SyntaxException(current_token.toString());
        }
        return def_ident_fin;
    }

    public Node FUNC(){
        Node func = new Node("FUNC");
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.ID){
            Node ident = IDENT();
            Node param = PARAMS_EXISTE();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.RETURN){
                Node type = TYPE();
                current_token = tokens.poll();
                current_tag = current_token.getTag();
                if(current_tag == Tag.IS){
                    Node decl = DECL_MULT();
                    current_token = tokens.poll();
                    current_tag = current_token.getTag();
                    if(current_tag == Tag.BEGIN){
                        Node inst = INSTR_PLUS();
                        current_token = tokens.poll();
                        current_tag = current_token.getTag();
                        if(current_tag == Tag.END){
                            Node ident2 = IDENT_EXISTE();
                            current_token = tokens.poll();
                            current_tag = current_token.getTag();
                            if(current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
                                func.addChild(ident);
                                func.addChild(param);
                                func.addChild(type);
                                func.addChild(decl);
                                func.addChild(inst);
                                func.addChild(ident2);
                            }else{
                                throw new SyntaxException(current_token.toString());
                            }
                        }else{
                            throw new SyntaxException(current_token.toString());
                        }
                    }else{
                        throw new SyntaxException(current_token.toString());
                    }
                }else{
                    throw new SyntaxException(current_token.toString());
                }
            }else{
                throw new SyntaxException(current_token.toString());
            }
        }else{
            throw new SyntaxException(current_token.toString());
        }
        return func;
    }


    public Node PROC(){
        Node proc = new Node("PROC");
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.ID){
            Node ident = IDENT();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if (current_tag == Tag.IS){
                Node decl = DECL_MULT();
                current_token = tokens.poll();
                current_tag = current_token.getTag();
                if(current_tag == Tag.BEGIN){
                    Node inst = INSTR_PLUS();
                    current_token = tokens.poll();
                    current_tag = current_token.getTag();
                    if(current_tag == Tag.END){
                        Node ident2 = IDENT_EXISTE();
                        current_token = tokens.poll();
                        current_tag = current_token.getTag();
                        if(current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
                            proc.addChild(ident);
                            proc.addChild(decl);
                            proc.addChild(inst);
                            proc.addChild(ident2);
                        }else{
                            throw new SyntaxException(current_token.toString());
                        }
                    }else{
                        throw new SyntaxException(current_token.toString());
                    }
                }else{
                    throw new SyntaxException(current_token.toString());
                }
            }
        }
        return proc;
    }

    public Node DECL_MULT() throws SyntaxException{
        Node decl_mult = new Node("DECL_MULT");
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.TYPE || current_tag == Tag.PROCEDURE || current_tag == Tag.FUNCTION || current_tag == Tag.ID){
            Node decl = DECL();
            Node decl_mult2 = DECL_MULT();
            decl_mult.addChild(decl);
            decl_mult.addChild(decl_mult2);
        }else if (current_tag == Tag.BEGIN){
            // on ne fait rien car on est dans le cas epsilon
        }else{
            throw new SyntaxException(current_token.toString());
        }
        return decl_mult;
    }

    public Node CHAMPS() throws SyntaxException{
        Node champs = new Node("CHAMPS");
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.ID){
            Node ident = IDENT_PLUS();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.OP && ((Operator) current_token).getValue().equals("def")){
                Node type = TYPE();
                current_token = tokens.poll();
                current_tag = current_token.getTag();
                if(current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
                    champs.addChild(ident);
                    champs.addChild(type);
                }else{
                    throw new SyntaxException(current_token.toString());
                }
            }else{
                throw new SyntaxException(current_token.toString());
            }
        }
        return champs;
    }

    public Node CHAMPS_PLUS() throws SyntaxException{
        Node champs_plus = new Node("CHAMPS_PLUS");
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();
        if (current_tag == Tag.ID){
            Node champs = CHAMPS();
            Node champs_suite = CHAMPS_SUITE();
            champs_plus.addChild(champs);
            champs_plus.addChild(champs_suite);
    }

}

    
    







