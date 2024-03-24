package compiler;

public class Token
{
    TokenTypes type;
    double value;
    String valueString;

    public Token(TokenTypes type, String value)
    {
        this.type = type;
        this.valueString = value;
    }

    public Token(TokenTypes type, double valueD, String valueS)
    {
        this.type = type;
        this.value = valueD;
        this.valueString = valueS;
    }

    public Token copy()
    {
        return new Token(this.type, this.value, this.valueString);
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
        if (this.valueString != null) {return this.type + " : " + this.valueString;}
        return this.type.toString();
    }
}