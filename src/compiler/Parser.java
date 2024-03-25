package compiler;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    ArrayList<Token> tokens;
    Token currentToken;
    int currentPosition = -1;

    final ArrayList<TokenTypes> terms = new ArrayList<TokenTypes>(Arrays.asList(TokenTypes.MULTIPLY, TokenTypes.DIVIDE));
    final ArrayList<TokenTypes> expressions = new ArrayList<TokenTypes>(Arrays.asList(TokenTypes.PLUS, TokenTypes.MINUS));
    final ArrayList<TokenTypes> functions = new ArrayList<TokenTypes>(Arrays.asList(TokenTypes.SIN, TokenTypes.COS, TokenTypes.TAN));

    public Parser(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
        this.advance();
    }

    public Node parse()
    {
        this.currentPosition = -1;
        this.advance();
        return this.expression();
    }

    public void advance()
    {
        this.currentPosition += 1;
        if (this.currentPosition < tokens.size())
        {
            this.currentToken = tokens.get(this.currentPosition);
        }
    }

    public Node function()
    {
        Token token = this.currentToken.copy();
        if (functions.contains(token.type))
        {
            this.advance();
            if (this.currentToken.type == TokenTypes.L_PAR) {
                this.advance();
                Node expression = this.expression();
                if (this.currentToken.type == TokenTypes.R_PAR) {
                    this.advance();
                    return new UnaryOpNode(token, expression);
                }
            }
        } else if (token.type == TokenTypes.NUMBER) {
            this.advance();
            return new NumberNode(token);
        } else if (token.type == TokenTypes.VARIABLE) {
            this.advance();
            return new VariableNode(token);
        }
        return null;
    }

    public Node atom()
    {
        Token token = this.currentToken.copy();
        if (token.type == TokenTypes.L_PAR) {
            this.advance();
            Node expression = this.expression();
            if (this.currentToken.type == TokenTypes.R_PAR)
            {
                this.advance();
                return expression;
            }
        }
        return this.function();
    }

    public Node power()
    {
        Node left = this.atom();
        while (this.currentToken.type == TokenTypes.POW)
        {
            Token operator = this.currentToken.copy();
            this.advance();
            Node right = this.factor();
            left = new BinOpNode(left, operator, right);
        }
        return left;
    }

    public Node factor()
    {
        Token token = this.currentToken.copy();
        if (expressions.contains(token.type))
        {
            this.advance();
            Node factor = factor();
            return new UnaryOpNode(token, factor);
        }
        return this.power();
    }

    public Node term()
    {
        Node left = this.factor();
        while (terms.contains(this.currentToken.type))
        {
            Token operator = this.currentToken.copy();
            this.advance();
            Node right = this.factor();
            left = new BinOpNode(left, operator, right);
        }
        return left;
    }

    public Node expression()
    {
        Node left = this.term();
        while (expressions.contains(this.currentToken.type))
        {
            Token operator = this.currentToken.copy();
            this.advance();
            Node right = this.term();
            left = new BinOpNode(left, operator, right);
        }
        return left;
    }
}
