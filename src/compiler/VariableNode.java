package compiler;

public class VariableNode implements Node
{
    Token variableToken;

    public VariableNode(Token variableToken)
    {
        this.variableToken = variableToken;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}

        if (obj.getClass() != this.getClass()) {return false;}
        VariableNode node = (VariableNode) obj;
        return this.variableToken.equals(node.variableToken);
    }

    @Override
    public String toString() {
        return variableToken.toString();
    }
}
