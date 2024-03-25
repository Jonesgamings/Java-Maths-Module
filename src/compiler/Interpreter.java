package compiler;

import java.util.Arrays;
import java.util.HashMap;

public class Interpreter
{
    Node ASTree;
    HashMap<String, Double> variables;

    public Interpreter(Node ASTree, HashMap<String, Double> variables)
    {
        this.ASTree = ASTree;
        this.variables = variables;
    }

    public double calculateBinOpNode(BinOpNode node)
    {
        Node left = node.left;
        Node right = node.right;
        return switch (node.operator.type) {
            case TokenTypes.PLUS -> this.calculateNode(left) + this.calculateNode(right);
            case TokenTypes.MINUS -> this.calculateNode(left) - this.calculateNode(right);
            case TokenTypes.DIVIDE -> this.calculateNode(left) / this.calculateNode(right);
            case TokenTypes.MULTIPLY -> this.calculateNode(left) * this.calculateNode(right);
            default -> 0;
        };
    }
    public double calculateUnaryOpNode(UnaryOpNode node)
    {
        Node innerNode = node.exprNode;
        return switch (node.operator.type) {
            case TokenTypes.SIN -> Math.sin(this.calculateNode(innerNode));
            case TokenTypes.COS -> Math.cos(this.calculateNode(innerNode));
            case TokenTypes.TAN -> Math.tan(this.calculateNode(innerNode));
            case TokenTypes.PLUS -> this.calculateNode(innerNode);
            case TokenTypes.MINUS -> -1 * this.calculateNode(innerNode);
            default -> 0;
        };
    }

    public double calculateVariableNode(VariableNode node)
    {
        String variableName = node.variableToken.valueString;
        return this.variables.get(variableName);
    }

    public double calculateNode(Node node)
    {
        return switch (node.getClass().getName()) {
            case "compiler.BinOpNode" -> {
                BinOpNode binOpNode = (BinOpNode) node;
                yield this.calculateBinOpNode(binOpNode);
            }
            case "compiler.NumberNode" -> {
                NumberNode numberNode = (NumberNode) node;
                yield numberNode.get();
            }
            case "compiler.UnaryOpNode" -> {
                UnaryOpNode unaryOpNode = (UnaryOpNode) node;
                yield this.calculateUnaryOpNode(unaryOpNode);
            }
            case "compiler.VariableNode" -> {
                VariableNode variableNode = (VariableNode) node;
                yield this.calculateVariableNode(variableNode);
            }
            default -> 0;
        };
    }

    public double calculateValue()
    {
        return calculateNode(this.ASTree);
    }

    public static void main(String[] args) {
        HashMap<String, Double> variables = new HashMap<String, Double>();
        variables.put("x", 3d);
        Lexer l = new Lexer("(7 + x) * 5");
        Parser p = new Parser(l.createTokens());
        Interpreter i = new Interpreter(p.parse(), variables);
        System.out.println(i.calculateValue());
    }
}
