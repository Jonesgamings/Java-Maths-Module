package compiler;

import java.util.ArrayList;

public class ChainBinOpNode implements Node
{
    ArrayList<Node> nodes;
    Token operator;

    public ChainBinOpNode(ArrayList<Node> nodes, Token operator)
    {
        this.nodes = nodes;
        this.operator = operator;
    }

    public ChainBinOpNode(Token operator)
    {
        this.nodes = new ArrayList<Node>();
        this.operator = operator;
    }

    public ChainBinOpNode copy()
    {
        return new ChainBinOpNode(new ArrayList<Node>(this.nodes), operator);
    }

    public void addNode(Node node)
    {
        this.nodes.add(node);
    }

    public void removeNode(int index)
    {
        this.nodes.remove(index);
    }

    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("(");
        for (int i = 0; i < this.nodes.size(); i++)
        {
            toReturn.append(this.nodes.get(i).toString());
            if (i != this.nodes.size()-1) {
                toReturn.append(" " + this.operator.toString() + " ");
            }
        }
        toReturn.append(")");
        return toReturn.toString();
    }
}
