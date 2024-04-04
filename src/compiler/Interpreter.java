package compiler;

import java.util.HashMap;
import java.util.Objects;

public class Interpreter
{
    Node ASTree;
    HashMap<String, Double> variables;
    private boolean simplified = false;

    public void removeVariable(String variable)
    {
        this.variables.remove(variable);
    }

    public String stringLogNode(LogNode node)
    {
        return "log(" + this.nodeToString(node.base) + ", " + this.nodeToString(node.insideLog) + ")";
    }

    public String stringUnaryOpNode(UnaryOpNode node)
    {
        return node.operator.toText() + "(" + this.nodeToString(node.exprNode) + ")";
    }

    public String stringBinOpNode(BinOpNode node)
    {
        Node left = node.left;
        Node right = node.right;
        return switch (node.operator.type) {
            case TokenTypes.PLUS -> "(" + this.nodeToString(left) + " + " + this.nodeToString(right) + ")";
            case TokenTypes.MINUS -> "(" + this.nodeToString(left) + " - " + this.nodeToString(right) + ")";
            case TokenTypes.DIVIDE -> "(" + this.nodeToString(left) + " / " + this.nodeToString(right) + ")";
            case TokenTypes.MULTIPLY -> "(" + this.nodeToString(left) + " * " + this.nodeToString(right) + ")";
            case TokenTypes.POW -> "(" + this.nodeToString(left) + " ^ " + this.nodeToString(right) + ")";
            default -> "";
        };
    }

    public String nodeToString(Node node)
    {
        return switch (node.getClass().getName()) {
            case "compiler.BinOpNode" -> {
                BinOpNode binOpNode = (BinOpNode) node;
                yield this.stringBinOpNode(binOpNode);
            }
            case "compiler.NumberNode" -> {
                NumberNode numberNode = (NumberNode) node;
                yield Double.toString(numberNode.numberToken.value);
            }
            case "compiler.VariableNode" -> {
                VariableNode variableNode = (VariableNode) node;
                yield variableNode.variableToken.valueString;
            }
            case "compiler.UnaryOpNode" -> {
                UnaryOpNode unaryOpNode = (UnaryOpNode) node;
                yield this.stringUnaryOpNode(unaryOpNode);
            }
            case "compiler.LogNode" -> {
                LogNode logNode = (LogNode) node;
                yield this.stringLogNode(logNode);
            }
            default -> null;
        };
    }

    @Override
    public String toString()
    {
        return this.nodeToString(ASTree);
    }

    public Interpreter(Node ASTree)
    {
        this.ASTree = ASTree;
        this.variables = new HashMap<String, Double>();
        this.variables.put("e", Math.E);
        this.variables.put("pi", Math.PI);
    }

    public Node derivative()
    {
        Node tree = this.simplifyNode(this.derivativeNode(this.ASTree));
        while (simplified) {
            this.simplified = false;
            tree = this.simplifyNode(tree);
        }
        return tree;
    }

    public Node simplifyBinOpNode(BinOpNode node)
    {
        Node left = this.simplifyNode(node.left);
        Node right = this.simplifyNode(node.right);
        BinOpNode newBinOp = new BinOpNode(left, node.operator, right);
        if (left.getClass() == NumberNode.class && right.getClass() != NumberNode.class)
        {
            NumberNode newL = (NumberNode) left;
            switch (node.operator.type) {
                case TokenTypes.PLUS ->
                {
                    if (newL.numberToken.value == 0) {
                        this.simplified = true;
                        return right;}
                    if (newL.numberToken.value == 1) {return newBinOp;}
                }
                case TokenTypes.MINUS ->
                {
                    if (newL.numberToken.value == 0) {
                        this.simplified = true;
                        return new UnaryOpNode(new Token(TokenTypes.MINUS), right);}
                    if (newL.numberToken.value == 1) {return newBinOp;}
                }
                case TokenTypes.DIVIDE -> {
                    if (newL.numberToken.value == 0) {
                        this.simplified = true;
                        return new NumberNode(new Token(TokenTypes.NUMBER, 0));}
                    if (newL.numberToken.value == 1) {return newBinOp;}
                }
                case TokenTypes.MULTIPLY -> {
                    if (newL.numberToken.value == 0) {
                        this.simplified = true;
                        return new NumberNode(new Token(TokenTypes.NUMBER, 0));}
                    if (newL.numberToken.value == 1) {
                        this.simplified = true;
                        return right;}
                }
                case TokenTypes.POW -> {
                    if (newL.numberToken.value == 0) {
                        this.simplified = true;
                        return new NumberNode(new Token(TokenTypes.NUMBER, 0));}
                    if (newL.numberToken.value == 1) {
                        this.simplified = true;
                        return new NumberNode(new Token(TokenTypes.NUMBER, 1));}
                }
                default -> {
                    return null;
                }
            };
        }
        else if (left.getClass() != NumberNode.class && right.getClass() == NumberNode.class)
        {
            NumberNode newR = (NumberNode) right;
            switch (node.operator.type) {
                case TokenTypes.PLUS ->
                {
                    if (newR.numberToken.value == 0) {
                        this.simplified = true;
                        return left;}
                    if (newR.numberToken.value == 1) {return newBinOp;}
                }
                case TokenTypes.MINUS ->
                {
                    if (newR.numberToken.value == 0) {
                        this.simplified = true;
                        return new UnaryOpNode(new Token(TokenTypes.MINUS), left);}
                    if (newR.numberToken.value == 1) {return newBinOp;}
                }
                case TokenTypes.DIVIDE -> {
                    if (newR.numberToken.value == 0) {
                        this.simplified = true;
                        return null;}
                    if (newR.numberToken.value == 1) {
                        this.simplified = true;
                        return left;}
                }
                case TokenTypes.MULTIPLY -> {
                    if (newR.numberToken.value == 0) {
                        this.simplified = true;
                        return new NumberNode(new Token(TokenTypes.NUMBER, 0));}
                    if (newR.numberToken.value == 1) {
                        this.simplified = true;
                        return left;}
                }
                case TokenTypes.POW -> {
                    if (newR.numberToken.value == 0){
                        this.simplified = true;
                        return new NumberNode(new Token(TokenTypes.NUMBER, 1));}
                    if (newR.numberToken.value == 1) {
                        this.simplified = true;
                        return left;}
                }
                default -> {
                    return null;
                }
            };
        }

        else if (left.getClass() == NumberNode.class && right.getClass() == NumberNode.class) {
            this.simplified = true;
            return new NumberNode(new Token(TokenTypes.NUMBER, this.calculateBinOpNode(newBinOp)));
        }
        return newBinOp;
    }

    public Node simplifyUnaryOpNode(UnaryOpNode node)
    {
        Node expr = this.simplifyNode(node.exprNode);
        if (expr.getClass() == NumberNode.class)
        {
            this.simplified = true;
            return new NumberNode(new Token(TokenTypes.NUMBER, this.calculateUnaryOpNode(node)));
        };
        if (expr.getClass() == UnaryOpNode.class)
        {
            UnaryOpNode exprU = (UnaryOpNode) expr;
            if (exprU.operator.type == TokenTypes.MINUS && node.operator.type == TokenTypes.MINUS)
            {
                return exprU.exprNode;
            }
            if (exprU.operator.type == TokenTypes.PLUS && node.operator.type == TokenTypes.MINUS)
            {
                return new UnaryOpNode(new Token(TokenTypes.MINUS), exprU.exprNode);
            }
            if (exprU.operator.type == TokenTypes.MINUS && node.operator.type == TokenTypes.PLUS)
            {
                return new UnaryOpNode(new Token(TokenTypes.MINUS), exprU.exprNode);
            }
        }
        return new UnaryOpNode(node.operator, expr);
    }

    public Node simplifyLogNode(LogNode node)
    {
        Node base = this.simplifyNode(node.base);
        Node inside = this.simplifyNode(node.insideLog);
        if (base.getClass() == NumberNode.class && inside.getClass() == NumberNode.class)
        {
            this.simplified = true;
            return new NumberNode(new Token(TokenTypes.NUMBER, this.calculateLogNode(node)));
        }
        return new LogNode(base, inside);
    }

    public Node simplifyNode(Node node)
    {
        switch (node.getClass().getName()) {
            case "compiler.BinOpNode" -> {
                BinOpNode binOpNode = (BinOpNode) node;
                return this.simplifyBinOpNode(binOpNode);
            }
            case "compiler.UnaryOpNode" -> {
                UnaryOpNode unaryOpNode = (UnaryOpNode) node;
                return this.simplifyUnaryOpNode(unaryOpNode);
            }
            case "compiler.LogNode" -> {
                LogNode logNode = (LogNode) node;
                return this.simplifyLogNode(logNode);
            }
            case "compiler.VariableNode" ->
            {
                VariableNode variableNode = (VariableNode) node;
                if (this.variables.containsKey(variableNode.variableToken.valueString)) {return new NumberNode(new Token(TokenTypes.NUMBER, this.calculateVariableNode(variableNode)));}
                return node;
            }
        };
        return node;
    }

    public Node derivativeVariableNode(VariableNode node)
    {
        if (this.variables.containsKey(node.variableToken.valueString))
        {
            return new NumberNode(new Token(TokenTypes.NUMBER, 0));
        }
        return new NumberNode(new Token(TokenTypes.NUMBER, 1));
    }

    public Node derivativeLogNode(LogNode node)
    {
        Node base = node.base;
        Node inside = node.insideLog;
        Node a = new BinOpNode(new BinOpNode(new LogNode(new NumberNode(new Token(TokenTypes.NUMBER, Math.E)), base), new Token(TokenTypes.MULTIPLY), this.derivativeNode(inside)), new Token(TokenTypes.DIVIDE), inside);
        Node b = new BinOpNode(new BinOpNode(new LogNode(new NumberNode(new Token(TokenTypes.NUMBER, Math.E)), inside), new Token(TokenTypes.MULTIPLY), this.derivativeNode(base)), new Token(TokenTypes.DIVIDE), base);
        Node c = new BinOpNode(new LogNode(new NumberNode(new Token(TokenTypes.NUMBER, Math.E)), base), new Token(TokenTypes.POW), new NumberNode(new Token(TokenTypes.NUMBER, 2)));
        return new BinOpNode(new BinOpNode(a, new Token(TokenTypes.MINUS), b), new Token(TokenTypes.DIVIDE), c);
    }

    public Node derivativeUnaryOpNode(UnaryOpNode node)
    {
        Node innerNode = node.exprNode;
        switch (node.operator.type) {
            case TokenTypes.SIN -> {
                return new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.MULTIPLY), new UnaryOpNode(new Token(TokenTypes.COS), innerNode));
            }
            case TokenTypes.COS -> {
                return new UnaryOpNode(new Token(TokenTypes.MINUS), new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.MULTIPLY), new UnaryOpNode(new Token(TokenTypes.SIN), innerNode)));
            }
            case TokenTypes.TAN -> {
                return new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.DIVIDE), new BinOpNode(new UnaryOpNode(new Token(TokenTypes.COS), innerNode), new Token(TokenTypes.POW), new NumberNode(new Token(TokenTypes.NUMBER, 2))));
            }

            case TokenTypes.SINH -> {
                return new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.MULTIPLY), new UnaryOpNode(new Token(TokenTypes.COSH), innerNode));
            }
            case TokenTypes.COSH -> {
                return new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.MULTIPLY), new UnaryOpNode(new Token(TokenTypes.SINH), innerNode));
            }
            case TokenTypes.TANH ->
            {
                return new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.DIVIDE), new BinOpNode(new UnaryOpNode(new Token(TokenTypes.COSH), innerNode), new Token(TokenTypes.POW), new NumberNode(new Token(TokenTypes.NUMBER, 2))));
            }
            //case TokenTypes.ASIN -> Math.asin(this.calculateNode(innerNode));
            //case TokenTypes.ACOS -> Math.acos(this.calculateNode(innerNode));
            //case TokenTypes.ATAN -> Math.atan(this.calculateNode(innerNode));
            //case TokenTypes.ASINH -> Interpreter.asinh(this.calculateNode(innerNode));
            //case TokenTypes.ACOSH -> Interpreter.acosh(this.calculateNode(innerNode));
            //case TokenTypes.ATANH -> Interpreter.atanh(this.calculateNode(innerNode));
            case TokenTypes.PLUS -> {
                return this.derivativeNode(innerNode);
            }
            case TokenTypes.MINUS -> {
                return new UnaryOpNode(new Token(TokenTypes.MINUS), this.derivativeNode(innerNode));
            }
        }
        return null;
    }

    public Node derivativeBinOpNode(BinOpNode node)
    {
        Node left = node.left;
        Node right = node.right;
        switch (node.operator.type) {
            case TokenTypes.PLUS: {return new BinOpNode(this.derivativeNode(left), new Token(TokenTypes.PLUS), this.derivativeNode(right));}
            case TokenTypes.MINUS: {return new BinOpNode(this.derivativeNode(left), new Token(TokenTypes.MINUS), this.derivativeNode(right));}
            case TokenTypes.DIVIDE : {
                Node a = new BinOpNode(right, new Token(TokenTypes.MULTIPLY), this.derivativeNode(left));
                Node b = new BinOpNode(left, new Token(TokenTypes.MULTIPLY), this.derivativeNode(right));
                Node c = new BinOpNode(right, new Token(TokenTypes.POW), new NumberNode(new Token(TokenTypes.NUMBER, 2)));
                return new BinOpNode(new BinOpNode(a, new Token(TokenTypes.MINUS), b), new Token(TokenTypes.DIVIDE), c);
            }
            case TokenTypes.MULTIPLY: {return new BinOpNode(new BinOpNode(left, new Token(TokenTypes.MULTIPLY), this.derivativeNode(right)) , new Token(TokenTypes.PLUS), new BinOpNode(right, new Token(TokenTypes.MULTIPLY), this.derivativeNode(left)));}
            case TokenTypes.POW:
            {
                Node a = new BinOpNode(left, new Token(TokenTypes.POW), new BinOpNode(right, new Token(TokenTypes.MINUS), new NumberNode(new Token(TokenTypes.NUMBER, 1))));
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
            default -> null;
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
        Lexer l = new Lexer("sin(x)");
        Parser p = new Parser(l.createTokens());
        Interpreter i = new Interpreter(p.parse());
        Interpreter i2 = new Interpreter(i.derivative());
        i2.setVariable("x", 1);
        System.out.println(i2);
    }
}
