package compiler;

import java.util.HashMap;
import java.util.Objects;

public class Interpreter
{
    Node ASTree;
    HashMap<String, Double> variables;

    public Interpreter(Node ASTree)
    {
        this.ASTree = ASTree;
        this.variables = new HashMap<String, Double>();
        this.variables.put("e", Math.E);
        this.variables.put("pi", Math.PI);
    }

    public boolean setVariable(String name, double value)
    {
        if (Objects.equals(name, "e") || Objects.equals(name, "pi")) {return false;}
        this.variables.put(name, value);
        return true;
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
            case TokenTypes.POW -> Math.pow(this.calculateNode(left), this.calculateNode(right));
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
            case TokenTypes.SINH -> Math.sinh(this.calculateNode(innerNode));
            case TokenTypes.COSH -> Math.cosh(this.calculateNode(innerNode));
            case TokenTypes.TANH -> Math.tanh(this.calculateNode(innerNode));
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

    public double calculateValue(HashMap<String, Double> variables)
    {
        this.variables = variables;
        return calculateNode(this.ASTree);
    }

    public double calculateValue()
    {
        return calculateNode(this.ASTree);
    }

    public static void main(String[] args) {
        Lexer l = new Lexer("SIN(e^x)");
        Parser p = new Parser(l.createTokens());
        Interpreter i = new Interpreter(p.parse());
        for (int index = -100; index<201; index++) {
            i.setVariable("x", index);
            System.out.println(i.calculateValue());
        }
    }
}
