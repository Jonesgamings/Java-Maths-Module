package compiler;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {

    ArrayList<Token> tokens;
    Token currentToken;
    int currentPosition = -1;

    final ArrayList<TokenTypes> terms = new ArrayList<TokenTypes>(Arrays.asList(TokenTypes.MULTIPLY, TokenTypes.DIVIDE));
    final ArrayList<TokenTypes> expressions = new ArrayList<TokenTypes>(Arrays.asList(TokenTypes.PLUS, TokenTypes.MINUS));

    public Parser(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
        this.advance();
    }

    public Node parse()
    {
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

    public NumberNode factor()
    {
        Token token = this.currentToken.copy();
        if (expressions.contains(this.currentToken.type))
        {

        }
        if (token.type == TokenTypes.NUMBER)
        {
            this.advance();
            return new NumberNode(token);
        }
        return null;
    }

    public Node term()
    {
        Node left = this.factor();
        while (terms.contains(this.currentToken.type))
        {
            Token operator = this.currentToken.copy();
            this.advance();
            NumberNode right = this.factor();
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

    public static void main(String[] args) {
        Lexer l = new Lexer("123 * 45 + 23");
        Parser p = new Parser(l.createTokens());
        System.out.println(p.parse());
    }
}
