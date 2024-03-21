package compiler;

import java.util.ArrayList;
import java.util.Arrays;

public class Lexer
{
    ArrayList<Token> tokens;
    String text;
    char currentCharacter;
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
            this.currentCharacter = this.text.charAt(this.currentPosition);
        }
    }

    public ArrayList<Token> create_tokens()
    {
        while (this.currentPosition < text.length())
        {
            String currentCharacterString = Character.toString(this.currentCharacter);
            if (this.whitespaces.contains(currentCharacterString)) {
                this.advance();
            }
            else if (this.numbers.contains(currentCharacterString)) {

            }
            else {
                switch (currentCharacterString) {
                    case "+":
                        this.tokens.add(new Token(TokenTypes.PLUS));
                        break;

                    case "-":
                        this.tokens.add(new Token(TokenTypes.MINUS));
                        break;

                    case "/":
                        this.tokens.add(new Token(TokenTypes.DIVIDE));
                        break;

                    case "*":
                        this.tokens.add(new Token(TokenTypes.MULTIPLY));
                        break;

                    case "(":
                        this.tokens.add(new Token(TokenTypes.L_PAR));
                        break;

                    case ")":
                        this.tokens.add(new Token(TokenTypes.R_PAR));
                        break;
                }
            }
        }
        return this.tokens;
    }
}
