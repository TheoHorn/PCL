package syntaxer;
import java.util.ArrayDeque;
import java.util.ArrayList;

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

    public ArrayList<Node> PARAMS() throws SyntaxException {
        ArrayList<Node> params = new ArrayList<>();
        Token currentToken = tokens.poll(); // Consommer '('
        Tag currentTag = currentToken.getTag();
    
        if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa"))) {
            throw new SyntaxException("Expected '(', found: " + currentToken.toString());
        }
    
        params.addAll(PARAMS_PLUS());
    
        currentToken = tokens.poll(); // Consommer ')'
        currentTag = currentToken.getTag();
        if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
            throw new SyntaxException("Expected ')', found: " + currentToken.toString());
        }
    
        return params;
    }
    
    
    

    public Node PARAMS_EXISTE() throws SyntaxException {
        Node paramsExiste = new Node("PARAMS_EXISTE");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa")) {
            paramsExiste.addChildren(PARAMS());
        }
        return paramsExiste;
    }
    

    public ArrayList<Node> PARAMS_PLUS() throws SyntaxException {
        ArrayList<Node> paramsPlus = new ArrayList<>();
        paramsPlus.add(PARAM());
        paramsPlus.addAll(PARAMS_SUITE());
        return paramsPlus;
    }
    

    public ArrayList<Node> PARAMS_SUITE() throws SyntaxException {
        ArrayList<Node> paramsSuite = new ArrayList<>();
        while (tokens.peek().getTag() == Tag.OP && ((Operator) tokens.peek()).getValue().equals("def")) {
            tokens.poll(); // Consommer ';'
            paramsSuite.addAll(PARAMS_PLUS());
        }
        return paramsSuite;
    }


    public Node PARAM() throws SyntaxException {
        Node param = new Node("PARAM");
        ArrayList<Node> identPlus = IDENT_PLUS();
    
        Token currentToken = tokens.poll(); // Consommer ':'
        Tag currentTag = currentToken.getTag();
        if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("def"))) {
            throw new SyntaxException("Expected ':', found: " + currentToken.toString());
        }
    
        Node type = TYPE();
        param.addChildren(identPlus);
        param.addChild(type);
    
        return param;
    }
    

    public Node MODE_EXISTE() throws SyntaxException {
        Node modeExiste = new Node("MODE_EXISTE");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP) {
            String opValue = ((Operator) currentToken).getValue();
            if (opValue.equals("in")) {
                modeExiste.addChild(MODE());
            }
        }
        // Le cas epsilon est géré implicitement
        return modeExiste;
    }
    

    public Node MODE() throws SyntaxException {
        Node mode = new Node("MODE");
        Token currentToken = tokens.poll();
        Tag currentTag = currentToken.getTag();
    
        if (currentTag == Tag.OP) {
            String opValue = ((Operator) currentToken).getValue();
            if (opValue.equals("in")) {
                mode.addChild(new Node("in"));
                Node outExiste = OUT_EXISTE();
                if (outExiste != null) {
                    mode.addChild(outExiste);
                }
            } else {
                throw new SyntaxException("Expected 'in', found: " + currentToken.toString());
            }
        } else {
            throw new SyntaxException("Expected 'in', found: " + currentToken.toString());
        }
    
        return mode;
    }
    

    public Node OUT_EXISTE() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("out")) {
            tokens.poll(); // Consommer 'out'
            return new Node("out");
        }
        // Le cas epsilon est géré implicitement
        return null;
    }
    

    public Node AFFECT_EXISTE() throws SyntaxException {
        Node affectExiste = new Node("AFFECT_EXISTE");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("afc")) {
            affectExiste.addChild(AFFECT());
        }
        // Le cas epsilon est géré implicitement
        return affectExiste;
    }
    
    public Node AFFECT() throws SyntaxException {
        Node affect = new Node("AFFECT");
        Token currentToken = tokens.poll();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("afc")) {
            affect.addChild(new Node(":="));
            Node expr = EXPR();
            affect.addChild(expr);
        } else {
            throw new SyntaxException("Expected ':=', found: " + currentToken.toString());
        }
    
        return affect;
    }
    

    public Node EXPR() throws SyntaxException {
        // Supposons que PRIO_7 est le niveau de priorité le plus bas dans vos expressions
        return PRIO_7();
    }
    

    public ArrayList<Node> EXPR_PLUS() throws SyntaxException {
        ArrayList<Node> exprPlus = new ArrayList<>();
        exprPlus.add(EXPR());
        exprPlus.addAll(EXPR_SUITE());
        return exprPlus;
    }
    

    public ArrayList<Node> EXPR_SUITE() throws SyntaxException {
        ArrayList<Node> exprSuite = new ArrayList<>();
        while (tokens.peek().getTag() == Tag.OP && ((Operator) tokens.peek()).getValue().equals("comma")) {
            tokens.poll(); // Consommer ','
            exprSuite.add(EXPR());
        }
        return exprSuite;
    }
    

    public Node EXPR_EXISTE() throws SyntaxException {
        Node exprExiste = new Node("EXPR_EXISTE");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP || currentTag == Tag.TRUE || currentTag == Tag.FALSE || currentTag == Tag.NULL || currentTag == Tag.NEW || currentTag == Tag.CHARACTERVAL || currentTag == Tag.NOT || currentTag == Tag.ID || currentTag == Tag.NUM || currentTag == Tag.CHAR) {
            if (currentTag == Tag.OP) {
                String opValue = ((Operator) currentToken).getValue();
                if(opValue.equals("lpa") || opValue.equals("-u")){
                    exprExiste.addChild(EXPR());
                }
            } else {
                exprExiste.addChild(EXPR());
            }
        }else if (!(currentTag == Tag.END || currentTag == Tag.ELSIF || currentTag == Tag.ELSE)){
            //si le prochain token n'est pas dans le cas epsilon
            throw new SyntaxException("Expected an expression, found: " + currentToken.toString());
        }
        return exprExiste;
    }
    
    

    public ArrayList<Node> INSTR_PLUS() throws SyntaxException {
        ArrayList<Node> instrPlus = new ArrayList<>();
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.BEGIN || currentTag == Tag.IF || currentTag == Tag.FOR || currentTag ==Tag.WHILE || currentTag ==Tag.ID){
            instrPlus.add(INSTR());
            currentToken = tokens.poll(); // Consommer ';'
            currentTag = currentToken.getTag();
            if (currentTag == Tag.SEPARATOR && ((Operator) currentToken).getValue().equals(";")){
                instrPlus.addAll(INSTR_SUITE());
            }else{
                throw new SyntaxException("Expected ';', found: " + currentToken.toString());
            }
        }else if (currentTag == Tag.RETURN){
            tokens.poll(); // Consommer 'return'
            instrPlus.add(RETURN());
        }else{
            throw new SyntaxException("Expected an instruction, found: " + currentToken.toString());
        }
        return instrPlus;
    }
    

    public ArrayList<Node> INSTR_SUITE() throws SyntaxException {
        ArrayList<Node> instrSuite = new ArrayList<>();
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.BEGIN || currentTag == Tag.IF || currentTag == Tag.FOR || currentTag ==Tag.WHILE || currentTag ==Tag.ID){
            instrSuite.add(INSTR());
            currentToken = tokens.poll(); // Consommer ';'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.SEPARATOR && ((Operator) currentToken).getValue().equals(";"))){
                throw new SyntaxException("Expected ';', found: " + currentToken.toString());
            }
        }else if (currentTag == Tag.RETURN){
            tokens.poll(); // Consommer 'return'
            instrSuite.add(RETURN());
        }else if (!(currentTag == Tag.END || currentTag == Tag.ELSIF || currentTag == Tag.ELSE)){
            //si le prochain token n'est pas dans le cas epsilon
            throw new SyntaxException("Expected an instruction, found: " + currentToken.toString());
        }
        return instrSuite;
    }
    

    public Node INSTR() throws SyntaxException {
        Node instr = new Node("INSTR");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.BEGIN){
            tokens.poll(); // Consommer 'begin'
            instr.addChildren(INSTR_PLUS());
            currentToken = tokens.poll(); // Consommer 'end'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.END)){
                throw new SyntaxException("Expected 'end', found: " + currentToken.toString());
            }
        }else if (currentTag == Tag.IF){
            tokens.poll(); // Consommer 'if'
            instr.addChild(EXPR());
            THEN_EXISTE();
            instr.addChildren(INSTR_PLUS());
            instr.addChild(ELSEIF_MULT());
            ELSE_EXISTE();
            currentToken = tokens.poll(); // Consommer 'end'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.END)){
                throw new SyntaxException("Expected 'end', found: " + currentToken.toString());
            }
            currentToken = tokens.poll(); // Consommer 'if'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.IF)){
                throw new SyntaxException("Expected 'if', found: " + currentToken.toString());
            }
        }else if(currentTag == Tag.FOR){
            tokens.poll(); // Consommer 'for'
            instr.addChild(IDENT());
            currentToken = tokens.poll(); // Consommer 'in'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.IN)){
                throw new SyntaxException("Expected 'in', found: " + currentToken.toString());
            }
            REVERSE_EXISTE();
            instr.addChild(EXPR());
            currentToken = tokens.poll(); // Consommer '..'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("adr"))){
                throw new SyntaxException("Expected '..', found: " + currentToken.toString());
            }
            instr.addChild(LOOP());
        }else if(currentTag == Tag.WHILE) {
            tokens.poll(); // Consommer 'while'
            instr.addChild(LOOP());
        }else if(currentTag == Tag.ID) {
            instr.addChild(IDENT());
            instr.addChild(INSTR_FIN());
        }
        return instr;
    }

    public Node INSTR_FIN() throws SyntaxException {
        Node instrFin = new Node("INSTR_FIN");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if(currentTag == Tag.OP){
            if(((Operator) currentToken).getValue().equals("lpa")){
                tokens.poll(); // Consommer '('
                instrFin.addChildren(EXPR_PLUS());
                currentToken = tokens.poll(); // Consommer ')'
                currentTag = currentToken.getTag();
                if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
                    throw new SyntaxException("Expected ')', found: " + currentToken.toString());
                }
            }else if(((Operator) currentToken).getValue().equals("afc")) {
                tokens.poll(); // Consommer ':='
                instrFin.addChild(EXPR());
            }
        }else if (! (currentTag == Tag.SEPARATOR && ((Word) currentToken).getValue().equals(";"))){
            throw new SyntaxException("Expected ';', found: " + currentToken.toString());
        }
        return instrFin;
    }

    public Node RETURN(){
        return null;
    }

    public Node LOOP(){
        return null;
    }

    public Node ELSEIF_MULT() throws SyntaxException {
        Node node = new Node("ELSEIF_MULT");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.ELSIF) {
            node = ELSEIF();
        }else if (!(currentTag == Tag.END || currentTag == Tag.ELSE)) {
            throw new SyntaxException("Expected 'end' or 'else', found: " + currentToken.toString());
        }
        return node;
    }
    

    public Node ELSEIF() throws SyntaxException {
        Node elseif = new Node("ELSEIF");
        Token currentToken = tokens.poll(); // Consommer 'elsif'
        Tag currentTag = currentToken.getTag();
        if (!(currentTag == Tag.ELSIF)) {
            throw new SyntaxException("Expected 'elif', found: " + currentToken.toString());
        }
        elseif.addChild(EXPR()); // Ajouter la condition
        THEN_EXISTE();
        elseif.addChildren(INSTR_PLUS());
        return elseif;
    }
    


    

    public Node ELSE_EXISTE() throws SyntaxException {
        Node elseExiste = new Node("ELSE_EXISTE");
        if (tokens.peek().getTag() == Tag.ID && ((Word) tokens.peek()).getValue().equals("else")) {
            elseExiste.addChild(ELSE());
        }
        return elseExiste;
    }
    

    public Node ELSE() throws SyntaxException {
        Node elseNode = new Node("ELSE");
        tokens.poll(); // Consommer 'else'
        elseNode.addChildren(INSTR_PLUS()); // Ajouter le corps de la branche
        return elseNode;
    }
    
    

    public Node THEN_EXISTE() throws SyntaxException {
        Node thenExiste = new Node("THEN_EXISTE");
        Token currentToken = tokens.peek();
    
        if (currentToken.getTag() == Tag.ID && ((Word) currentToken).getValue().equals("then")) {
            tokens.poll(); // Consommer 'then'
            thenExiste.addChild(new Node("then")); // Ajouter le nœud 'then'
        }
        return thenExiste;
    }
    
/////////////////////////////////////////////////////////////

    public Node VAL() throws SyntaxException {
            Node val = new Node("VAL");
            Token currentToken = tokens.peek();

            switch (currentToken.getTag()) {
                case OP:
                    Operator opToken = (Operator) currentToken;
                    switch (opToken.getValue()) {
                        case "lpa": // '('
                            tokens.poll(); // Consommer '('
                            val.addChild(PRIO_7());
                            currentToken = tokens.poll();
                            Tag currentTag = currentToken.getTag();
                            if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
                                throw new SyntaxException("Expected ')', found: " + currentToken.toString());
                            }
                            break;
                        case "afc": // ':='
                            // Gérer l'opérateur ':=' si nécessaire dans le contexte de VAL
                            break;
                    }
                    break;
                case ID:
                    val.addChild(IDENT());
                    val.addChild(IDENT_FIN());
                    break;
                case NUM:
                    tokens.poll(); // Consommer le token
                    val.addChild(new Node(((Word) currentToken).getValue()));
                    break;
                case CHAR:
                    tokens.poll(); // Consommer le token
                    val.addChild(new Node(((Word) currentToken).getValue()));
                    break;
                case TRUE:
                case FALSE:
                case NULL:
                    tokens.poll(); // Consommer le token
                    val.addChild(new Node(currentToken.getTag().toString()));
                    break;
                case NEW:
                    tokens.poll(); // Consommer 'new'
                    val.addChild(new Node("new"));
                    val.addChild(IDENT());
                    break;
                default:
                    throw new SyntaxException("Unexpected token for VAL: " + currentToken.toString());
            }

            return val;
    }

        public Node PRIO_1() throws SyntaxException {
            Node prio1 = new Node("PRIO_1");
            Node valNode = VAL();  // Gérer les valeurs basiques
            prio1.addChild(valNode);
        
            while (tokens.peek().getTag() == Tag.OP) {
                Operator op = (Operator) tokens.peek();
                if (op.getValue().equals("acs")) { // Par exemple, '.' pour l'accès à un champ
                    tokens.poll(); // Consommer l'opérateur
                    Node suivant = VAL(); // Supposons que l'accès à un champ est suivi par une autre valeur
                    prio1.addChild(suivant);
                } else {
                    break; // Sortir de la boucle si l'opérateur n'est pas de priorité 1
                }
            }
        
            return prio1;
        }
        

    public Node PRIO_1_SUITE(Node leftOperand) throws SyntaxException {
        Node prio1Suite = new Node("PRIO_1_SUITE");
        prio1Suite.addChild(leftOperand);

        while (tokens.peek().getTag() == Tag.OP) {
            Operator op = (Operator) tokens.peek();
            if (op.getValue().equals("acs")) { // Vérifier si l'opérateur est '.'
                tokens.poll(); // Consommer l'opérateur '.'
                Node rightOperand = PRIO_1(); // Récupérer l'opérande de droite
                Node operationNode = new Node("Operation");
                operationNode.addChild(prio1Suite);
                operationNode.addChild(rightOperand);
                prio1Suite = operationNode;
            } else {
                break; // Sortir de la boucle si l'opérateur n'est pas un opérateur de PRIO_1_SUITE
            }
        }

        return prio1Suite;
    }


    public Node PRIO_1_OP() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP) {
            Operator op = (Operator) currentToken;
            if (op.getValue().equals("acs")) { // Vérifier si l'opérateur est '.'
                tokens.poll(); // Consommer l'opérateur
                return new Node(".");
            }
            // Ajoutez des cas pour d'autres opérateurs de priorité 1 si nécessaire
        }
        throw new SyntaxException("Expected a PRIO_1 operator, found: " + currentToken.toString());
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
    

    public Node INT() throws SyntaxException {
        Token token = tokens.poll();
        Tag tag = token.getTag();
        if (tag == Tag.NUM) {
            return new Node(String.valueOf(((Num) token).getValue()));
        }else{
            throw new SyntaxException("Expected an INT, found: " + token.toString());
        }
    }

    public Node CARAC() throws SyntaxException {
        Token token = tokens.poll();
        Tag tag = token.getTag();
        if (tag == Tag.CHAR) {
            return new Node(String.valueOf(((Char) token).getValue()));
        }else{
            throw new SyntaxException("Expected a CHAR, found: " + token.toString());
        }
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



