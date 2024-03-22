package compiler;

public class Token
{
    TokenTypes type;
    double value;

    public Token copy()
    {
        return new Token(this.type, this.value);
    }

    public Token(TokenTypes type)
    {
        this.type=type;
    }

    public Token(TokenTypes type, double value)
    {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString()
    {
        if (this.value != 0) {return this.type + " : " + this.value;}
        return this.type.toString();
    }
}