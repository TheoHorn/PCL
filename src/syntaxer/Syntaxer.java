package syntaxer;
import java.util.*;

import javax.swing.JPopupMenu.Separator;

import lexer.*;



public class Syntaxer {
    
    /**
     * @param arbre : l'arbre syntaxique
     * @param tokens : la liste des tokens
     */
    private Node arbre;
    private ArrayDeque<Token> tokens;

    // a utiliser pour les tests
    public Syntaxer() throws Exception {
        this.tokens = new ArrayDeque<>();
        this.arbre = null;
    }
    
    /**
     * Constructeur de la classe Syntaxer
     * @param tokens, la liste des tokens
     * @throws Exception
     */
    public Syntaxer(ArrayDeque<Token> tokens) throws Exception {
        this.tokens = tokens;
        this.arbre = null;
    }


    /**
     * Fonction qui lance la syntaxe
     * @return
     * @throws Exception
     */
    public Node launch() throws Exception {
        return this.File();
    }

    public Node File() throws Exception {
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
        return proc;
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
            ArrayList<Node> ident_plus = IDENT_PLUS();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.OP && ((Operator) current_token).getValue().equals("def")){
                Node type = TYPE();
                Node affect_exist = AFFECT_EXISTE();
                decl.addChildren(ident_plus);
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
        return def_ident;
    }

    public Node DEF_IDENT_FIN() throws Exception {
        Node def_ident_fin = new Node("DEF_IDENT_FIN");
        Token current_token = tokens.poll();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.ACCESS){
            Node ident = IDENT();
            def_ident_fin.addChild(ident);
        }else if (current_tag == Tag.RECORD){
            ArrayList<Node> champs_plus = CHAMPS_PLUS();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.END){
                current_token = tokens.poll();
                current_tag = current_token.getTag();
                if(current_tag == Tag.RECORD){
                    current_token = tokens.poll();
                    current_tag = current_token.getTag();
                    if(current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
                        def_ident_fin.addChildren(champs_plus);
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

    public Node FUNC() throws Exception{
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
                    ArrayList<Node> decl = DECL_MULT();
                    current_token = tokens.poll();
                    current_tag = current_token.getTag();
                    if(current_tag == Tag.BEGIN){
                        ArrayList<Node> inst = INSTR_PLUS();
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
                                func.addChildren(decl);
                                func.addChildren(inst);
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

    public Node PROC() throws Exception{
        Node proc = new Node("PROC");
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.ID){
            Node ident = IDENT();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if (current_tag == Tag.IS){
                ArrayList<Node> decl = DECL_MULT();
                current_token = tokens.poll();
                current_tag = current_token.getTag();
                if(current_tag == Tag.BEGIN){
                    ArrayList<Node> inst = INSTR_PLUS();
                    current_token = tokens.poll();
                    current_tag = current_token.getTag();
                    if(current_tag == Tag.END){
                        Node ident2 = IDENT_EXISTE();
                        current_token = tokens.poll();
                        current_tag = current_token.getTag();
                        if(current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
                            proc.addChild(ident);
                            proc.addChildren(decl);
                            proc.addChildren(inst);
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


    public ArrayList<Node> DECL_MULT() throws Exception{
        ArrayList<Node> decl_mult = new ArrayList<>();
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.TYPE || current_tag == Tag.PROCEDURE || current_tag == Tag.FUNCTION || current_tag == Tag.ID){
            Node decl = DECL();
            ArrayList<Node> decl_mult2 = DECL_MULT();
            decl_mult.add(decl);
            decl_mult.addAll(decl_mult2);
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
            ArrayList<Node> ident = IDENT_PLUS();
            current_token = tokens.poll();
            current_tag = current_token.getTag();
            if(current_tag == Tag.OP && ((Operator) current_token).getValue().equals("def")){
                Node type = TYPE();
                current_token = tokens.poll();
                current_tag = current_token.getTag();
                if(current_tag == Tag.SEPARATOR && ((Word) current_token).getValue().equals(";")){
                    champs.addChildren(ident);
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

    public ArrayList<Node> CHAMPS_PLUS() throws SyntaxException{
        ArrayList<Node> champs_plus = new ArrayList<>();
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();
        if (current_tag == Tag.ID){
            Node champs = CHAMPS();
            ArrayList<Node> champs_suite = CHAMPS_SUITE();
            champs_plus.add(champs);
            champs_plus.addAll(champs_suite);
        }else{
            throw new SyntaxException(current_token.toString());
        }
        return champs_plus;
    }

    public ArrayList<Node> CHAMPS_SUITE() throws SyntaxException{
        ArrayList<Node> champ_suite = new ArrayList<>();
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();

        if (currentTag == Tag.ID){
            ArrayList<Node> champs = CHAMPS_PLUS();
            champ_suite.addAll(champs);
        }else if (currentTag == Tag.END){
            // on ne fait rien car on est dans le cas epsilon
        }else{
            throw new SyntaxException(currentToken.toString());
        }
        return champ_suite;
    }

    public Node TYPE() throws SyntaxException {
        Node type = new Node("TYPE");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.ID){
            Node ident = IDENT();
            type.addChild(ident);
            }
        else if (currentTag == Tag.ACCESS){
            currentToken = tokens.poll();
            currentTag = currentToken.getTag();
            if (currentTag == Tag.ID){
                Node ident = IDENT();
                type.addChild(ident);
                }
            else{
                throw new SyntaxException(currentToken.toString());
                }
            }
        else{ 
            throw new SyntaxException(currentToken.toString());
            }
        return type;
    }

    public ArrayList<Node> PARAMS() {
        return null;
    }

    public Node PARAMS_EXISTE() {
        return null;
    }

    public ArrayList<Node> PARAMS_PLUS() {
        return null;
    }

    public ArrayList<Node> PARAMS_SUITE(){
        return null;
    }

    public Node PARAM(){
        return null;
    }

    public Node MODE_EXISTE(){
        return null;
    }

    public Node MODE(){
        return null;
    }

    public Node OUT_EXISTE(){
        return null;
    }

    public Node AFFECT_EXISTE(){
        return null;
    }

    public Node AFFECT(){
        return null;
    }

    public Node EXPR(){
        return null;
    }

    public ArrayList<Node> EXPR_PLUS(){
        return null;
    }

    public ArrayList<Node> EXPR_SUITE(){
        return null;
    }

    public Node EXPR_EXISTE(){
        return null;
    }

    public ArrayList<Node> INSTR_PLUS(){
        return null;
    }

    public ArrayList<Node> INSTR_SUITE(){
        return null;
    }

    public Node INSTR(){
        return null;
    }

    public Node INSTR_FIN(){
        return null;
    }

    public Node RETURN(){
        return null;
    }

    public Node LOOP(){
        return null;
    }

    public ArrayList<Node> ELSEIF_MULT(){
        return null;
    }

    public Node ELSEIF(){
        return null;
    }

    public Node ELSE_EXISTE(){
        return null;
    }

    public Node ELSE(){
        return null;
    }

    public Node THEN_EXISTE(){
        return null;
    }

    public Node VAL(){
        return null;
    }

    public Node PRIO_1(){
        return null;
    }

    public Node PRIO_1_SUITE(){
        return null;
    }

    public Node PRIO_1_OP(){
        return null;
    }

    public Node PRIO_2(){
        return null;
    }
    
    public Node PRIO_2_SUITE(){
        return null;
    }
    
    public Node PRIO_2_OP(){
        return null;
    }
    
    public Node PRIO_3(){
        return null;
    }
    
    public Node PRIO_3_SUITE(){
        return null;
    }
    
    public Node PRIO_3_OP(){
        return null;
    }
    
    public Node PRIO_4(){
        return null;
    }
    
    public Node PRIO_4_SUITE(){
        return null;
    }
    
    public Node PRIO_4_OP(){
        return null;
    }
    
    public Node PRIO_5(){
        return null;
    }
    
    public Node PRIO_5_SUITE(){
        return null;
    }
    
    public Node PRIO_5_OP(){
        return null;
    }
    
    public Node PRIO_6(){
        return null;
    }
    
    public Node PRIO_6_SUITE(){
        return null;
    }
    
    public Node PRIO_6_OP(){
        return null;
    }

    public Node PRIO_6_OP_SUITE(){
        return null;
    }
    
    public Node PRIO_7(){
        return null;
    }
    
    public Node PRIO_7_SUITE(){
        return null;
    }
    
    public Node PRIO_7_OP(){
        return null;
    }

    public Node PRIO_7_OP_SUITE(){
        return null;
    }

    public Node IDENT() throws SyntaxException {
        Node ident = new Node("IDENT");
        Token current_token = tokens.poll();
        Tag current_tag = current_token.getTag();

        if (current_tag == Tag.ID){
            ident.addChild(new Node(((Word) current_token).getValue()));
        }else{
            throw new SyntaxException(current_token.toString());
        }
        return ident;
    }



    public ArrayList<Node> IDENT_PLUS() throws SyntaxException {
        if (tokens.peek().getTag() == Tag.ID) {
            ArrayList<Node> identPlus = new ArrayList<>();
            Node ident = IDENT();
            ArrayList<Node> identSuite = IDENT_SUITE();
            identPlus.add(ident);
            identPlus.addAll(identSuite);
            return identPlus;
        }else{
            throw new SyntaxException("Expected IDENT, found: " + tokens.peek().toString());
        }
        
    }
    
    public Node IDENT_EXISTE() throws SyntaxException {
        Node identExiste = new Node("IDENT_EXISTE");
        if (tokens.peek().getTag() == Tag.ID) {
            identExiste.addChild(IDENT());
        }else if (tokens.peek().getTag() == Tag.SEPARATOR && ((Word) tokens.peek()).getValue().equals(";")) {
            // on ne fait rien car on est dans le cas epsilon
        }else{
            throw new SyntaxException("Expected IDENT or ';', found: " + tokens.peek().toString());
        }
        return identExiste;
    }
    

    public ArrayList<Node> IDENT_SUITE() throws SyntaxException {
        ArrayList<Node> identSuite = new ArrayList<>();
        Token currentToken = tokens.peek();
        Tag currentTag = tokens.peek().getTag();
        if (currentTag == Tag.SEPARATOR && ((Word) currentToken).getValue().equals(",")) {
            tokens.poll(); 
            identSuite.addAll(IDENT_PLUS());
        }else if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("def")) {
            // on ne fait rien car on est dans le cas epsilon
        }else{
            throw new SyntaxException("Expected ',' or ':', found: " + currentToken.toString());
        }
        return identSuite;
    }
    
    public Node IDENT_FIN() throws SyntaxException {
        Node identFin = new Node("IDENT_FIN");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();

        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa")) {
            tokens.poll(); 
            ArrayList<Node> exprPlus = EXPR_PLUS();
            identFin.addChildren(exprPlus);
            currentToken = tokens.poll();
            currentTag = currentToken.getTag();
            if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa")) {
                // on ne fait rien car on est dans le cas epsilon
            }else{
                throw new SyntaxException("Expected ')', found: " + currentToken.toString());
            }
        }else if (currentTag == Tag.OP){
            String val = ((Operator) currentToken).getValue();
            if (val.equals("-u") || val.equals("(") || val.equals("not") || val.equals("afc") || val.equals("def")){
                throw new SyntaxException("Expected IDENT, found: " + currentToken.toString());
            }else{
                // on ne fait rien car on est dans le cas epsilon
            }
        }else if (currentTag == Tag.SEPARATOR && ((Word) currentToken).getValue().equals(";")) {
            // on ne fait rien car on est dans le cas epsilon
        }else if (currentTag == Tag.SEPARATOR && ((Word) currentToken).getValue().equals(",")) {
            // on ne fait rien car on est dans le cas epsilon
        }else if (currentTag == Tag.END || currentTag == Tag.RETURN || currentTag == Tag.ELSE || currentTag == Tag.ELSIF || currentTag == Tag.THEN || currentTag == Tag.LOOP || currentTag == Tag.BEGIN || currentTag == Tag.IF || currentTag == Tag.WHILE || currentTag == Tag.FOR || currentTag == Tag.ID) {
            // on ne fait rien car on est dans le cas epsilon
        }else{
            throw new SyntaxException("Expected an other token, found: " + currentToken.toString());
        }
        return identFin;
    }
    

    public Node INT() {
        return null;
    }

    public Node CARAC() {
        return null;
    }

    public Node REVERSE_EXISTE() {
        return null;
    }

    //pour les tests
    public void setTokens(ArrayDeque<Token> tokens) {
        this.tokens = tokens;
    }
    public void addToken(Token token) {
        this.tokens.addLast(token);
    }

}



