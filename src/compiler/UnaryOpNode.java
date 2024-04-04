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
    public boolean equals(Object obj) {
        if (obj == null) {return false;}

        if (obj.getClass() != this.getClass()) {return false;}

        UnaryOpNode node = (UnaryOpNode) obj;
        if (!operator.equals(node.operator)) {return false;}
        if (!exprNode.equals(node.exprNode)) {return false;}
        return true;
    }

    @Override
    public String toString() {
        return "(" + this.operator + ", " + this.exprNode + ")";
    }
}

