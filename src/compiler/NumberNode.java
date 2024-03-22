package compiler;

public class NumberNode implements Node
{
    Token numberToken;

    public NumberNode(Token numberToken)
    {
        this.numberToken = numberToken;
    }

    @Override
    public String toString() {
        return numberToken.toString();
    }
}
