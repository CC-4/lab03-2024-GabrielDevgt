/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        System.out.println("Resultado: " + this.operandos.peek());

        while (!operadores.empty()) {
            popOp();
        }

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if (this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {

            Token curentToken = this.tokens.get(this.next);

            // Codigo para el Shunting Yard Algorithm

            if (id == Token.NUMBER) {
                // Encontramos un numero
                // Debemos guardarlo en el stack de operandos
                operandos.push(this.tokens.get(this.next).getVal());

            } else if (id == Token.SEMI) {
                // Encontramos un punto y coma
                // Debemos operar todo lo que quedo pendiente
                while (!this.operadores.empty()) {
                    popOp();
                }
            } else if (isOperator(curentToken)) {
                // Encontramos algun otro token, es decir un operador
                // Lo guardamos en el stack de operadores
                // Que pushOp haga el trabajo, no quiero hacerlo yo aqui
                pushOp(curentToken);
            } else if (id == Token.LPAREN) {
                operadores.push(curentToken);
            } else if (id == Token.RPAREN) {
                while (!operadores.empty() && operadores.peek().getId() != Token.LPAREN) {
                    popOp();
                }

                if (!operadores.empty() && operadores.peek().getId() == Token.LPAREN) {
                    operadores.pop();
                } else {
                    throw new IllegalArgumentException("Missing parenthesis");
                }
            }

            this.next++;

            return true;
        }

        // Next line for debuggin
        // System.out
        // .println(
        // "Token unsuccesful, Expecte ID: " + id + ", Found ID: " +
        // this.tokens.get(this.next).getId());

        return false;
    }

    private boolean isOperator(Token token) {
        return token.getId() == Token.PLUS
                || token.getId() == Token.MINUS
                || token.getId() == Token.MULT
                || token.getId() == Token.DIV
                || token.getId() == Token.MOD
                || token.getId() == Token.EXP;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch (op.getId()) {
            case Token.PLUS:
            case Token.MINUS:
                return 1;
            case Token.MULT:
            case Token.DIV:
            case Token.MOD:
                return 2;
            case Token.EXP:
                return 3;
            default:
                return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        /* TODO: Su codigo aqui */

        // Pop b first to preserver entry order
        double b = this.operandos.pop();
        double a = this.operandos.pop();

        /* El codigo de esta seccion se explicara en clase */

        System.out.println(op.getId() + ": " + a + op + b);

        switch (op.getId()) {
            case Token.PLUS:
                operandos.push(a + b);
                break;
            case Token.MINUS:
                operandos.push(a - b);
                break;
            case Token.MULT:
                operandos.push(a * b);
                break;
            case Token.DIV:
                operandos.push(a / b);
                break;
            case Token.MOD:
                operandos.push(a % b);
                break;
            case Token.EXP:
                operandos.push(Math.pow(a, b));
                break;
            default:
                throw new IllegalArgumentException("Uknonw operator: " + op);
        }
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */

        /* Casi todo el codigo para esta seccion se vera en clase */
    	
    	// Si no hay operandos automaticamente ingresamos op al stack

    	// Si si hay operandos:
    		// Obtenemos la precedencia de op
        	// Obtenemos la precedencia de quien ya estaba en el stack
        	// Comparamos las precedencias y decidimos si hay que operar
        	// Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
        	// Al terminar operaciones pendientes, guardamos op en stack

            while (!operadores.empty() && pre(operadores.peek()) >= pre(op)) {
                popOp();
            }
    
            // Si no hay operandos automaticamente ingresamos op al stack
            operadores.push(op);

    }

    /*correct gramatic:
            S ::= E;
            E ::= T A
            A ::= T + A
                | T - A
                | 
            T ::= F B
            B ::= F * B
                | F / B
                | F % B
                | 
            F ::= P C
            C ::= F ^
                | 
            P ::= (E)
                | P ~
                | N
            N ::= number
                 */

                 private boolean S() {
                    return E() && term(Token.SEMI);
                }
                
                private boolean E() {
                    return E1();
                }
                
                private boolean E1() {
                    return T() && A();
                }
                
                private boolean T() {
                    return T1();
                }
                
                private boolean T1() {
                    return F() && B();
                }
                
                private boolean A() {
                    int save = next;
                    if (A1()) {
                        return true;
                    }
                    next = save;
                    if (A2()) {
                        return true;
                    }
                    return true; // A puede ser vacío
                }
                
                private boolean A1() {
                    return term(Token.PLUS) && T() && A();
                }
                
                private boolean A2() {
                    return term(Token.MINUS) && T() && A();
                }
                
                private boolean B() {
                    int save = next;
                    if (B1()) {
                        return true;
                    }
                    next = save;
                    if (B2()) {
                        return true;
                    }
                    next = save;
                    if (B3()) {
                        return true;
                    }
                    return true; // B puede ser vacío
                }
                
                private boolean B1() {
                    return term(Token.MULT) && F() && B();
                }
                
                private boolean B2() {
                    return term(Token.DIV) && F() && B();
                }
                
                private boolean B3() {
                    return term(Token.MOD) && F() && B();
                }
                
                private boolean F() {
                    return F1();
                }
                
                private boolean F1() {
                    return P() && C();
                }
                
                private boolean C() {
                    int save = next;
                    if (C1()) {
                        return true;
                    }
                    next = save;
                    return true; // C puede ser vacío
                }
                
                private boolean C1() {
                    return term(Token.EXP) && F();
                }
                
                private boolean P() {
                    int save = next;
                    if (P1()) {
                        return true;
                    }
                    next = save;
                    if (P2()) {
                        return true;
                    }
                    next = save;
                    return P3();
                }
                
                private boolean P1() {
                    return term(Token.LPAREN) && E() && term(Token.RPAREN);
                }
                
                private boolean P2() {
                    return term(Token.UNARY) && P();
                }
                
                private boolean P3() {
                    return N();
                }
                
                private boolean N() {
                    return term(Token.NUMBER);
                }

    /* TODO: sus otras funciones aqui */
}
