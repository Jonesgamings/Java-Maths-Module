package compiler;

public class BinOpNode implements Node {
    Node left;
    Token operator;
    Node right;

    public BinOpNode(Node left, Token operator, Node right)
    {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left + ", " + operator + ", " + right + ")";
    }
}
