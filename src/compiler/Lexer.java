package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer
{
    ArrayList<Token> tokens;
    String text;
    String currentCharacter;
    int currentPosition = -1;
    final String whitespaces = "\t\n ";
    final String numbers = "1234567890";
    final String characters = "QWERTYUIOPLKJHGFDSAZXCVBNMqwertyuioplkjhgfdsazxcvbnm";

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
        return new Token(TokenTypes.NUMBER, Double.parseDouble(numberString.toString()));
    }

    public Token createString()
    {
        StringBuilder functionString = new StringBuilder();
        while (this.currentPosition < text.length() && this.characters.contains(this.currentCharacter))
        {
            functionString.append(this.currentCharacter);
            this.advance();
        }
        return switch (functionString.toString().toLowerCase()) {
            case "sin" -> new Token(TokenTypes.SIN);
            case "cos" -> new Token(TokenTypes.COS);
            case "tan" -> new Token(TokenTypes.TAN);
            case "sinh" -> new Token(TokenTypes.SINH);
            case "cosh" -> new Token(TokenTypes.COSH);
            case "tanh" -> new Token(TokenTypes.TANH);
            case "asin" -> new Token(TokenTypes.ASIN);
            case "acos" -> new Token(TokenTypes.ACOS);
            case "atan" -> new Token(TokenTypes.ATAN);
            case "asinh" -> new Token(TokenTypes.ASINH);
            case "acosh" -> new Token(TokenTypes.ACOSH);
            case "atanh" -> new Token(TokenTypes.ATANH);
            case "log" -> new Token(TokenTypes.LOG);
            default -> new Token(TokenTypes.VARIABLE, functionString.toString());
        };
    }

    public ArrayList<Token> createTokens()
    {
        this.currentPosition = -1;
        this.advance();
        while (this.currentPosition < text.length())
        {
            if (this.whitespaces.contains(this.currentCharacter)) {
                this.advance();
            }
            else if (this.numbers.contains(this.currentCharacter)) {
                this.tokens.add(this.createNumber());
            }
            else if (this.characters.contains(this.currentCharacter))
            {
                Token varString = this.createString();
                if (varString != null)
                {
                    this.tokens.add(varString);
                }
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

                    case "^":
                        this.tokens.add(new Token(TokenTypes.POW));
                        this.advance();
                        break;

                    case ",":
                        this.tokens.add(new Token(TokenTypes.COMMA));
                        this.advance();
                        break;

                    default:
                        return null;
                }
            }
        }
        return this.tokens;
    }
}
