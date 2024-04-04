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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}

        if (obj.getClass() != this.getClass()) {return false;}
        NumberNode node = (NumberNode) obj;
        return this.numberToken.equals(node.numberToken);
    }

    public double get()
    {
        return numberToken.value;
    }
}
