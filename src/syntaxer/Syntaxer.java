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
        if (current_tag == Tag.ID && ((Word) current_token).getValue().equals("Text_IO")){
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

    public Node FUNC() throws Exception{
        Node func = new Node("FUNC");
        //peek car on veut garder le token pour le prochain appel de fonction
        Token current_token = tokens.peek();
        Tag current_tag = current_token.getTag();

        if(current_tag == Tag.ID){
            Node ident = IDENT();
            ArrayList<Node> param = PARAMS_EXISTE();
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
            type.addChild(IDENT());
            }
        else if (currentTag == Tag.ACCESS){
            tokens.poll(); // Consommer 'access'
            type.addChild(IDENT());
        }else{
            throw new SyntaxException(currentToken.toString());
        }
        return type;
    }

    public ArrayList<Node> PARAMS() throws SyntaxException {
        Token currentToken = tokens.poll(); // Consommer '('
        Tag currentTag = currentToken.getTag();
    
        if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa"))) {
            throw new SyntaxException("Expected '(', found: " + currentToken.toString());
        }

        ArrayList<Node> params = new ArrayList<>(PARAMS_PLUS());
    
        currentToken = tokens.poll(); // Consommer ')'
        currentTag = currentToken.getTag();
        if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
            throw new SyntaxException("Expected ')', found: " + currentToken.toString());
        }
    
        return params;
    }
    
    
    

    public ArrayList<Node> PARAMS_EXISTE() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa")) {
             return PARAMS();
        }else{
            throw new SyntaxException("Expected '(', found: " + currentToken.toString());
        }
    }
    

    public ArrayList<Node> PARAMS_PLUS() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        ArrayList<Node> paramsPlus;
        if (currentTag == Tag.ID) {
            paramsPlus = new ArrayList<>();
            paramsPlus.add(PARAM());
            paramsPlus.addAll(PARAMS_SUITE());
        } else {
            throw new SyntaxException("Expected a parameter, found: " + currentToken.toString());
        }
        return paramsPlus;
    }
    

    public ArrayList<Node> PARAMS_SUITE() throws SyntaxException {
        ArrayList<Node> paramsSuite = new ArrayList<>();
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.SEPARATOR && ((Word) currentToken).getValue().equals(";")) {
            tokens.poll(); // Consommer ';'
            paramsSuite.addAll(PARAMS_PLUS());
        }else if(!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
            // on ne fait rien si ) car on est dans le cas epsilon sinon
            throw new SyntaxException("Expected ')', found: " + currentToken.toString());
        }
        return paramsSuite;
    }


    public Node PARAM() throws SyntaxException {
        Node param = new Node("PARAM");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.ID){
            param.addChild(IDENT_PLUS());
            currentToken = tokens.poll(); // Consommer ':'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("def"))) {
                throw new SyntaxException("Expected ':', found: " + currentToken.toString());
            }
            param.addChild((MODE_EXISTE()));
            param.addChild(TYPE());
        }else{
            throw new SyntaxException("Expected an identifier, found: " + currentToken.toString());
        }
        return param;
    }
    

    public ArrayList<Node> MODE_EXISTE() throws SyntaxException {
        ArrayList<Node> modeExiste = null;
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP) {
            String opValue = ((Operator) currentToken).getValue();
            if (opValue.equals("in")) {
                modeExiste = MODE();
            }
        }else if (!(currentTag == Tag.ACCESS || currentTag == Tag.ID)){
            // si on est pas dans le cas epsilon on leve une exception
           throw new SyntaxException("Expected 'access' or an identifier, found: " + currentToken.toString());
        }
        return modeExiste;
    }
    

    public ArrayList<Node> MODE() throws SyntaxException {
        Token currentToken = tokens.poll();
        Tag currentTag = currentToken.getTag();

        ArrayList<Node> mode = null;
        if (currentTag == Tag.IN) {
            mode = new ArrayList<>();
            mode.add(new Node("in"));
            mode.add(OUT_EXISTE());
        } else {
            throw new SyntaxException("Expected 'in', found: " + currentToken.toString());
        }
        return mode;
    }
    

    public Node OUT_EXISTE() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OUT){
            tokens.poll(); // Consommer 'out'
            return new Node("out");
        }else if (!(currentTag == Tag.ACCESS || currentTag == Tag.ID)){
            // si on est pas dans le cas epsilon on leve une exception
            throw new SyntaxException("Expected 'access' or an identifier, found: " + currentToken.toString());
        }
        return null;
    }
    

    public Node AFFECT_EXISTE() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa")) {
            return AFFECT();
        }else if(!(currentTag == Tag.SEPARATOR && ((Word) currentToken).getValue().equals(";"))){
            // si on est pas dans le cas epsilon on leve une exception
            throw new SyntaxException("Expected '(' or ';', found: " + currentToken.toString());
        }
        // Le cas epsilon est géré implicitement
        return null;
    }
    
    public Node AFFECT() throws SyntaxException {
        Node affect = new Node("AFFECT");
        Token currentToken = tokens.poll(); // Consommer '('
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa")) {
            currentToken = tokens.poll(); // Consommer ':='
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("afc"))) {
                throw new SyntaxException("Expected ':=', found: " + currentToken.toString());
            }
            affect.addChild(new Node(":="));
            affect.addChild(EXPR());
            currentToken = tokens.poll(); // Consommer ')'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
                throw new SyntaxException("Expected ')', found: " + currentToken.toString());
            }

        } else {
            throw new SyntaxException("Expected '(', found: " + currentToken.toString());
        }
        return affect;
    }
    

    public Node EXPR() throws SyntaxException {
        //not, (, true, false, null, new, character'val, 1, idf, int, carac | ), ,, ;, return, begin, if, for, while, idf, then, .., loop, end, else, elif
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        Node expr = new Node("EXPR");
        if (currentTag == Tag.OP || currentTag == Tag.TRUE || currentTag == Tag.FALSE || currentTag == Tag.NULL || currentTag == Tag.NEW || currentTag == Tag.CHARACTERVAL || currentTag == Tag.NOT || currentTag == Tag.ID || currentTag == Tag.NUM || currentTag == Tag.CHAR){
            if (currentTag == Tag.OP) {
                String opValue = ((Operator) currentToken).getValue();
                if (opValue.equals("lpa") || opValue.equals("-u")) {
                    expr.addChild(PRIO_7());
                }else{
                    throw new SyntaxException("Expected '(' or '-', found: " + currentToken.toString());
                }
            }else{
                expr.addChild(PRIO_7());
            }
        }else{
            throw new SyntaxException("Expected an expression, found: " + currentToken.toString());
        }
        return expr;
    }
    

    public ArrayList<Node> EXPR_PLUS() throws SyntaxException {
        ArrayList<Node> exprPlus = new ArrayList<>();
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP || currentTag == Tag.TRUE || currentTag == Tag.FALSE || currentTag == Tag.NULL || currentTag == Tag.NEW || currentTag == Tag.CHARACTERVAL || currentTag == Tag.NOT || currentTag == Tag.ID || currentTag == Tag.NUM || currentTag == Tag.CHAR){
            if (currentTag == Tag.OP) {
                String opValue = ((Operator) currentToken).getValue();
                if (opValue.equals("lpa") || opValue.equals("-u")) {
                    exprPlus.add(EXPR());
                    exprPlus.add(EXPR_SUITE());
                }else{
                    throw new SyntaxException("Expected '(' or '-', found: " + currentToken.toString());
                }
            }else {
                exprPlus.add(EXPR());
                exprPlus.add(EXPR_SUITE());
            }
        }
        return exprPlus;
    }
    

    public Node EXPR_SUITE() throws SyntaxException {
        Node exprSuite = new Node("EXPR_SUITE");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP){
            String opValue = ((Operator) currentToken).getValue();
            if (!opValue.equals("rpa")){
                throw new SyntaxException("Expected ')', found: " + currentToken.toString());
            }
        }else if(currentTag == Tag.SEPARATOR){
            String opValue = ((Word) currentToken).getValue();
            tokens.poll(); // Consommer ','
            if (!opValue.equals(",")){
                throw new SyntaxException("Expected ';', found: " + currentToken.toString());
            }else{
                return EXPR();
            }
        }
        return exprSuite;
    }
    

    public Node EXPR_EXISTE() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.OP || currentTag == Tag.TRUE || currentTag == Tag.FALSE || currentTag == Tag.NULL || currentTag == Tag.NEW || currentTag == Tag.CHARACTERVAL || currentTag == Tag.NOT || currentTag == Tag.ID || currentTag == Tag.NUM || currentTag == Tag.CHAR) {
            if (currentTag == Tag.OP) {
                String opValue = ((Operator) currentToken).getValue();
                if(opValue.equals("lpa") || opValue.equals("-u")){
                    return EXPR();
                }
            } else {
                return EXPR();
            }
        }else if (!(currentTag == Tag.END || currentTag == Tag.ELSIF || currentTag == Tag.ELSE)){
            //si le prochain token n'est pas dans le cas epsilon
            throw new SyntaxException("Expected an expression, found: " + currentToken.toString());
        }
        return null;
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
            instr.addChild(INSTR_PLUS());
            currentToken = tokens.poll(); // Consommer 'end'
            currentTag = currentToken.getTag();
            if (!(currentTag == Tag.END)){
                throw new SyntaxException("Expected 'end', found: " + currentToken.toString());
            }
        }else if (currentTag == Tag.IF){
            tokens.poll(); // Consommer 'if'
            instr.addChild(EXPR());
            THEN_EXISTE();
            instr.addChild(INSTR_PLUS());
            instr.addChild(ELSEIF_MULT());
            instr.addChild(ELSE_EXISTE());
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
            instr.addChild(REVERSE_EXISTE());
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

    public ArrayList<Node> INSTR_FIN() throws SyntaxException {
        ArrayList<Node> instrFin = new ArrayList<>();
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if(currentTag == Tag.OP){
            if(((Operator) currentToken).getValue().equals("lpa")){
                tokens.poll(); // Consommer '('
                instrFin.addAll(EXPR_PLUS());
                currentToken = tokens.poll(); // Consommer ')'
                currentTag = currentToken.getTag();
                if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
                    throw new SyntaxException("Expected ')', found: " + currentToken.toString());
                }
            }else if(((Operator) currentToken).getValue().equals("afc")) {
                tokens.poll(); // Consommer ':='
                instrFin.add(EXPR());
            }
        }else if (! (currentTag == Tag.SEPARATOR && ((Word) currentToken).getValue().equals(";"))){
            throw new SyntaxException("Expected ';', found: " + currentToken.toString());
        }
        return instrFin;
    }

    public Node RETURN() throws SyntaxException {
        Node returnNode = new Node("RETURN");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.END || currentTag == Tag.ELSIF || currentTag == Tag.ELSE || currentTag == Tag.TRUE || currentTag == Tag.FALSE || currentTag == Tag.NULL || currentTag == Tag.NEW || currentTag == Tag.CHARACTERVAL || currentTag == Tag.NOT || currentTag == Tag.ID || currentTag == Tag.NUM || currentTag == Tag.CHAR || currentTag == Tag.OP){
            String opValue = ((Operator) currentToken).getValue();
            if(opValue.equals("lpa") || opValue.equals("-u")){
                returnNode.addChild(EXPR_EXISTE());
            }else{
                throw new SyntaxException("Expected an expression, found: " + currentToken.toString());

            }
        }
        return returnNode;
    }

    public Node LOOP() throws SyntaxException {
        Node loop = new Node("LOOP");
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.TRUE || currentTag == Tag.FALSE || currentTag == Tag.NULL || currentTag == Tag.NEW || currentTag == Tag.CHARACTERVAL || currentTag == Tag.NOT || currentTag == Tag.ID || currentTag == Tag.NUM || currentTag == Tag.CHAR || currentTag == Tag.OP){
            String opValue = ((Operator) currentToken).getValue();
            if(opValue.equals("lpa") || opValue.equals("-u")){
                loop.addChild(EXPR());
                currentToken = tokens.poll(); // Consommer 'loop'
                currentTag = currentToken.getTag();
                if (!(currentTag == Tag.LOOP)){
                    throw new SyntaxException("Expected 'loop', found: " + currentToken.toString());
                }
                loop.addChild(INSTR_PLUS());
                currentToken = tokens.poll(); // Consommer 'end'
                currentTag = currentToken.getTag();
                if (!(currentTag == Tag.END)){
                    throw new SyntaxException("Expected 'end', found: " + currentToken.toString());
                }
                currentToken = tokens.poll(); // Consommer 'loop'
                currentTag = currentToken.getTag();
                if (!(currentTag == Tag.LOOP)){
                    throw new SyntaxException("Expected 'loop', found: " + currentToken.toString());
                }
            }else{
                throw new SyntaxException("Expected an expression, found: " + currentToken.toString());
            }
        }
        return loop;
    }

    public Node ELSEIF_MULT() throws SyntaxException {
        Node node = null;
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
        elseif.addChild(INSTR_PLUS());
        return elseif;
    }

    public Node ELSE_EXISTE() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.ELSE) {
           return ELSE();
        }else if (!(currentTag == Tag.END)) {
            throw new SyntaxException("Expected 'end', found: " + tokens.peek().toString());
        }
        return null;
    }
    

    public Node ELSE() throws SyntaxException {
        Node elseNode = new Node("ELSE");
        tokens.poll(); // Consommer 'else'
        elseNode.addChild(INSTR_PLUS()); // Ajouter le corps de la branche
        return elseNode;
    }
    
    

    public void THEN_EXISTE() throws SyntaxException {
        Token currentToken = tokens.peek();
        Tag currentTag = currentToken.getTag();
        if (currentTag == Tag.THEN) {
            tokens.poll(); // Consommer 'then'
        }else if(!(currentTag == Tag.RETURN || currentTag == Tag.BEGIN || currentTag == Tag.IF || currentTag == Tag.FOR || currentTag ==Tag.WHILE || currentTag ==Tag.ID)){
            throw new SyntaxException("Expected 'return', 'begin', 'if', 'for', 'while' or an identifier, found: " + currentToken.toString());
        }
    }

    public ArrayList<Node> VAL() throws SyntaxException {
            ArrayList<Node> val = new ArrayList<>();
            Token currentToken = tokens.peek();
            Tag currentTag = currentToken.getTag();
            switch (currentTag) {
                case OP:
                    Operator opToken = (Operator) currentToken;
                    if (opToken.getValue().equals("lpa")){
                        tokens.poll(); // Consommer '('
                        val.add(PRIO_7());
                        currentToken = tokens.poll(); // Consommer ')'
                        currentTag = currentToken.getTag();
                        if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
                            throw new SyntaxException("Expected ')', found: " + currentToken);
                        }
                    }else if(opToken.getValue().equals("-u")) {
                        tokens.poll(); // Consommer '-'
                        Node node = new Node("-");
                        node.addChild(VAL());
                        val.add(node);
                    }else{
                        throw new SyntaxException("Expected '(' or '-', found: " + currentToken);
                    }
                    break;
                case ID:
                    val.add(IDENT());
                    val.add(IDENT_FIN());
                    break;
                case NUM:
                    val.add(INT());
                    break;
                case CHAR:
                    val.add(CARAC());
                    break;
                case TRUE:
                    tokens.poll(); // Consommer true
                    val.add(new Node("true"));
                    break;
                case FALSE:
                    tokens.poll(); // Consommer false
                    val.add(new Node("false"));
                    break;
                case NULL:
                    tokens.poll(); // Consommer le token
                    val.add(new Node("null"));
                    break;
                case NEW:
                    tokens.poll(); // Consommer 'new'
                    Node node = new Node("new");
                    node.addChild(IDENT());
                    val.add(node);
                    break;
                case CHARACTERVAL:
                    tokens.poll(); // Consommer le token
                    node = new Node("characterVal");
                    currentToken = tokens.poll(); // Consommer '('
                    currentTag = currentToken.getTag();
                    if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("lpa"))) {
                        throw new SyntaxException("Expected '(', found: " + currentToken);
                    }
                    node.addChild(EXPR());
                    currentToken = tokens.poll(); // Consommer ')'
                    currentTag = currentToken.getTag();
                    if (!(currentTag == Tag.OP && ((Operator) currentToken).getValue().equals("rpa"))) {
                        throw new SyntaxException("Expected ')', found: " + currentToken);
                    }
                    val.add(node);
                    break;
                default:
                    throw new SyntaxException("Unexpected token for VAL: " + currentToken.toString());
            }
            return val;
    }

        public ArrayList<Node> PRIO_1() throws SyntaxException {
            ArrayList<Node> prio1 = new ArrayList<>();
            Token currentToken = tokens.peek();
            Tag currentTag = currentToken.getTag();
            if (currentTag == Tag.TRUE || currentTag == Tag.FALSE || currentTag == Tag.NULL || currentTag == Tag.NEW || currentTag == Tag.CHARACTERVAL || currentTag == Tag.ID || currentTag == Tag.NUM || currentTag == Tag.CHAR || currentTag == Tag.OP){
                if (currentTag == Tag.OP) {
                    String opValue = ((Operator) currentToken).getValue();
                    if (opValue.equals("lpa") || opValue.equals("-u")) {
                        prio1.addAll(VAL());
                        prio1.add(PRIO_1_SUITE());
                    } else {
                        throw new SyntaxException("Expected '(' or '-', found: " + currentToken.toString());
                    }
                }else{
                    prio1.addAll(VAL());
                    prio1.add(PRIO_1_SUITE());
                }
            }else{
                throw new SyntaxException("Expected an operand, or an operand with a more important priority, found: " + currentToken);
            }
            return prio1;
        }
        

    public Node PRIO_1_SUITE() throws SyntaxException {
        // TO DO
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
            identFin.addChild(exprPlus);
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



