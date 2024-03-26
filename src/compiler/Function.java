package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class Function {

    String mathString;
    ArrayList<Token> tokens;
    Node ASTree;
    Interpreter interpreter;

    public Function(String mathString)
    {
        this.mathString = mathString;
        this.tokens = new Lexer(mathString).createTokens();
        this.ASTree = new Parser(this.tokens).parse();
        this.interpreter = new Interpreter(this.ASTree);
    }
}
