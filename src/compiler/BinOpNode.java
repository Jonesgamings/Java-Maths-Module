package compiler;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinOpNode binOpNode = (BinOpNode) o;
        return Objects.equals(left, binOpNode.left) && Objects.equals(operator, binOpNode.operator) && Objects.equals(right, binOpNode.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, operator, right);
    }
}
