package compiler;

import java.util.Objects;

public class Token
{
    public TokenTypes type;
    double value;
    public String valueString;

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
    public boolean equals(Object obj) {
        if (obj == null) {return false;}

        if (obj.getClass() != this.getClass()) {return false;}

        Token token = (Token) obj;
        if (type != token.type) {return false;}
        if (value != token.value) {return false;}
        if (!Objects.equals(valueString, token.valueString)) {return false;}
        return true;
    }

    @Override
    public String toString()
    {
        if (this.type == TokenTypes.NUMBER) {return this.type + ":" + this.value;}
        if (this.valueString != null) {return this.type + ":" + this.valueString;}
        return this.type.toString();
    }

    public String toText()
    {
        if (this.type == TokenTypes.MINUS || this.type == TokenTypes.PLUS)
        {
            return "-";
        }
        return this.type.toString();
    }
}