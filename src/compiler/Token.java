package compiler;

public class Token
{
    TokenTypes type;
    double value;

    public Token(TokenTypes type)
    {
        this.type=type;
    }

    public Token(TokenTypes type, double value)
    {
        this.type = type;
        this.value = value;
    }
}