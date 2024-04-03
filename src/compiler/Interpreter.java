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
        this.variables.put("h", Function.deltaX);
    }

    public Node derivative()
    {
        return this.derivativeNode(this.ASTree);
    }

    public Node derivativeBinOpNode(BinOpNode node)
    {
        Node left = node.left;
        Node right = node.right;
        switch (node.operator.type) {
            case TokenTypes.PLUS: {return new BinOpNode(this.derivativeNode(left), new Token(TokenTypes.PLUS), this.derivativeNode(right));}
            case TokenTypes.MINUS: {return new BinOpNode(this.derivativeNode(left), new Token(TokenTypes.MINUS), this.derivativeNode(right));}
            //case TokenTypes.DIVIDE : {this.calculateNode(left) / this.calculateNode(right);}
            case TokenTypes.MULTIPLY: {return new BinOpNode(new BinOpNode(left, new Token(TokenTypes.MULTIPLY), this.derivativeNode(right)) , new Token(TokenTypes.PLUS), new BinOpNode(right, new Token(TokenTypes.MULTIPLY), this.derivativeNode(left)));}
            case TokenTypes.POW:
            {
                Node a = new BinOpNode(left, new Token(TokenTypes.POW), new BinOpNode(right, new Token(TokenTypes.MINUS), new NumberNode(new Token(TokenTypes.NUMBER, -1))));
                Node LEFT = new BinOpNode(right, new Token(TokenTypes.MULTIPLY), new BinOpNode(a, new Token(TokenTypes.MULTIPLY), this.derivativeNode(left)));
                Node c = new BinOpNode(left, new Token(TokenTypes.POW), right);
                Node d = new LogNode(new NumberNode(new Token(TokenTypes.NUMBER, Math.E)), left);
                Node RIGHT = new BinOpNode(c, new Token(TokenTypes.MULTIPLY), new BinOpNode(d, new Token(TokenTypes.MULTIPLY), this.derivativeNode(right)));
                return new BinOpNode(LEFT, new Token(TokenTypes.PLUS), RIGHT);
            }
        };
        return null;
    }

    public Node derivativeNode(Node node)
    {
        return switch (node.getClass().getName()) {
            case "compiler.BinOpNode" -> {
                BinOpNode binOpNode = (BinOpNode) node;
                yield this.derivativeBinOpNode(binOpNode);
            }
            case "compiler.NumberNode" -> {
                yield new NumberNode(new Token(TokenTypes.NUMBER, 0));
            }
            case "compiler.VariableNode" ->
            {
                VariableNode variableNode = (VariableNode) node;
                yield this.derivativeVariableNode(variableNode);
            }
            case "compiler.UnaryOpNode" -> {
                UnaryOpNode unaryOpNode = (UnaryOpNode) node;
                yield this.derivativeUnaryOpNode(unaryOpNode);
            }
            case "compiler.LogNode" -> {
                LogNode logNode = (LogNode) node;
                yield  this.derivativeLogNode(logNode);
            }
            default -> 0;
        };
    }

    public boolean setVariable(String name, double value)
    {
        if (Objects.equals(name, "e") || Objects.equals(name, "pi")) {return false;}
        this.variables.put(name, value);
        return true;
    }

    public double calculateLogNode(LogNode logNode)
    {
        Node base = logNode.base;
        Node insideLog = logNode.insideLog;
        return Math.log(this.calculateNode(insideLog)) / Math.log(this.calculateNode(base));
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

    public static double asinh(double value)
    {
        return Math.log(value + Math.sqrt(value*value + 1.0));
    }

    public static double acosh(double value)
    {
        return Math.log(value + Math.sqrt(value*value - 1.0));
    }

    public static double atanh(double value)
    {
        return 0.5*Math.log( (value + 1.0) / (value - 1.0) );
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
            case TokenTypes.ASIN -> Math.asin(this.calculateNode(innerNode));
            case TokenTypes.ACOS -> Math.acos(this.calculateNode(innerNode));
            case TokenTypes.ATAN -> Math.atan(this.calculateNode(innerNode));
            case TokenTypes.ASINH -> Interpreter.asinh(this.calculateNode(innerNode));
            case TokenTypes.ACOSH -> Interpreter.acosh(this.calculateNode(innerNode));
            case TokenTypes.ATANH -> Interpreter.atanh(this.calculateNode(innerNode));
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
            case "compiler.LogNode" -> {
                LogNode logNode = (LogNode) node;
                yield  this.calculateLogNode(logNode);
            }
            default -> 0;
        };
    }

    public double calculateValue()
    {
        return calculateNode(this.ASTree);
    }

    public static void main(String[] args) {
        double startTime = System.nanoTime();
        Lexer l = new Lexer("log(e^x, x)");
        Parser p = new Parser(l.createTokens());
        Interpreter i = new Interpreter(p.parse());
        i.setVariable("x", 1.01);
        System.out.println(i.calculateValue());
    }
}
