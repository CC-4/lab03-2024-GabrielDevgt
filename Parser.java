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
        // System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            // Codigo para el Shunting Yard Algorithm
            /*
            if (id == Token.NUMBER) {
				// Encontramos un numero
				// Debemos guardarlo en el stack de operandos
				operandos.push( this.tokens.get(this.next).getVal() );

			} else if (id == Token.SEMI) {
				// Encontramos un punto y coma
				// Debemos operar todo lo que quedo pendiente
				while (!this.operadores.empty()) {
					popOp();
				}
				
			} else {
				// Encontramos algun otro token, es decir un operador
				// Lo guardamos en el stack de operadores
				// Que pushOp haga el trabajo, no quiero hacerlo yo aqui
				pushOp( this.tokens.get(this.next) );
			}
			*/

            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch(op.getId()) {
        	case Token.PLUS:
        		return 1;
        	case Token.MULT:
        		return 2;
        	default:
        		return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        if (op.equals(Token.PLUS)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("suma " + a + " + " + b);
        	this.operandos.push(a + b);
        } else if (op.equals(Token.MULT)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("mult " + a + " * " + b);
        	this.operandos.push(a * b);
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
