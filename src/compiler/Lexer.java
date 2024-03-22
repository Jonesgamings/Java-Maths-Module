package compiler;

import java.util.ArrayList;

public class Lexer
{
    ArrayList<Token> tokens;
    String text;
    String currentCharacter;
    int currentPosition = -1;
    final String whitespaces = "\t\n ";
    final String numbers = "1234567890";

    public Lexer(String text)
    {
        this.text = text;
        this.tokens = new ArrayList<Token>();
        this.advance();
    }

    public void advance(){
        this.currentPosition += 1;
        if (this.currentPosition < text.length())
        {
            this.currentCharacter = String.valueOf(this.text.charAt(this.currentPosition));
        }
    }

    public Token createNumber()
    {
        int dotCount = 0;
        StringBuilder numberString = new StringBuilder();
        while (this.currentPosition < text.length() && (this.numbers + ".").contains(this.currentCharacter))
        {
            if (this.currentCharacter.equals("."))
            {
                if (dotCount == 1)
                {
                    break;
                }
                dotCount += 1;
                numberString.append(this.currentCharacter);
            }
            else {
                numberString.append(this.currentCharacter);
            }
            this.advance();
        }
        return new Token(TokenTypes.NUMBER, Double.valueOf(numberString.toString()));
    }

    public ArrayList<Token> createTokens()
    {
        while (this.currentPosition < text.length())
        {
            if (this.whitespaces.contains(this.currentCharacter)) {
                this.advance();
            }
            else if (this.numbers.contains(this.currentCharacter)) {
                this.tokens.add(this.createNumber());
            }
            else {
                switch (this.currentCharacter) {
                    case "+":
                        this.tokens.add(new Token(TokenTypes.PLUS));
                        this.advance();
                        break;

                    case "-":
                        this.tokens.add(new Token(TokenTypes.MINUS));
                        this.advance();
                        break;

                    case "/":
                        this.tokens.add(new Token(TokenTypes.DIVIDE));
                        this.advance();
                        break;

                    case "*":
                        this.tokens.add(new Token(TokenTypes.MULTIPLY));
                        this.advance();
                        break;

                    case "(":
                        this.tokens.add(new Token(TokenTypes.L_PAR));
                        this.advance();
                        break;

                    case ")":
                        this.tokens.add(new Token(TokenTypes.R_PAR));
                        this.advance();
                        break;

                    default:
                        return null;
                }
            }
        }
        return this.tokens;
    }

    public static void main(String[] args) {
        Lexer l = new Lexer("123.453485*123");
        System.out.println( l.createTokens());
    }
}
