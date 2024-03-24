package compiler;

public class UnaryOpNode implements Node
{
    Token operator;
    Node exprNode;

    public UnaryOpNode(Token operator, Node exprNode)
    {
        this.operator = operator;
        this.exprNode = exprNode;
    }

    @Override
    public String toString() {
        return "(" + this.operator + ", " + this.exprNode + ")";
    }
}

