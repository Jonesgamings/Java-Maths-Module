package compiler;

import java.util.ArrayList;
import java.util.Arrays;
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
            case "compiler.ChainBinOpNode" -> {
                ChainBinOpNode chainBinOpNode = (ChainBinOpNode) node;
                yield this.stringChainBinOpNode(chainBinOpNode);
            }
            default -> null;
        };
    }

    public String stringChainBinOpNode(ChainBinOpNode node)
    {
        StringBuilder toReturn = new StringBuilder();
        toReturn.append("(");
        for (int i = 0; i < node.nodes.size(); i++)
        {
            toReturn.append(this.nodeToString(node.nodes.get(i)));
            if (i != node.nodes.size()-1)
            {
                if (node.operator.type == TokenTypes.PLUS) {toReturn.append(" + ");}
                if (node.operator.type == TokenTypes.MULTIPLY) {toReturn.append(" * ");}
            }
        }
        toReturn.append(")");
        return toReturn.toString();
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
        if (node.operator.type == TokenTypes.MULTIPLY || node.operator.type == TokenTypes.PLUS) {

            if (left.getClass() == BinOpNode.class && right.getClass() == ChainBinOpNode.class) {
                BinOpNode binOpLeft = (BinOpNode) left;
                ChainBinOpNode binOpRight = (ChainBinOpNode) right;
                if (binOpLeft.operator.type == node.operator.type && binOpRight.operator.type == node.operator.type) {
                    ChainBinOpNode newChained = new ChainBinOpNode(node.operator.copy());
                    newChained.nodes.addAll(binOpRight.nodes);
                    newChained.addNode(binOpLeft.left);
                    newChained.addNode(binOpLeft.right);
                    this.simplified = true;
                    return newChained;
                }
            }

            if (left.getClass() == ChainBinOpNode.class && right.getClass() == BinOpNode.class) {
                ChainBinOpNode binOpLeft = (ChainBinOpNode) left;
                BinOpNode binOpRight = (BinOpNode) right;
                if (binOpLeft.operator.type == node.operator.type && binOpRight.operator.type == node.operator.type) {
                    ChainBinOpNode newChained = new ChainBinOpNode(node.operator.copy());
                    newChained.nodes.addAll(binOpLeft.nodes);
                    newChained.addNode(binOpRight.left);
                    newChained.addNode(binOpRight.right);
                    this.simplified = true;
                    return newChained;
                }
            }

            // Checks if left or right of BinOpNode is also a binopnode of same operator
            if (left.getClass() == BinOpNode.class && right.getClass() != BinOpNode.class) {
                BinOpNode binOpLeft = (BinOpNode) left;
                if (binOpLeft.operator.type == node.operator.type) {
                    this.simplified = true;
                    return new ChainBinOpNode(new ArrayList<Node>(Arrays.asList(binOpLeft.left, binOpLeft.right, right)), node.operator.copy());
                }
            }
            if (left.getClass() == BinOpNode.class && right.getClass() == BinOpNode.class) {
                BinOpNode binOpLeft = (BinOpNode) left;
                BinOpNode binOpRight = (BinOpNode) right;
                if (binOpRight.operator.type == node.operator.type && binOpLeft.operator.type == node.operator.type) {
                    this.simplified = true;
                    return new ChainBinOpNode(new ArrayList<Node>(Arrays.asList(binOpLeft.left, binOpLeft.right, binOpRight.left, binOpRight.right)), node.operator.copy());
                }
            }
            if (left.getClass() != BinOpNode.class && right.getClass() == BinOpNode.class) {
                BinOpNode binOpRight = (BinOpNode) right;
                if (binOpRight.operator.type == node.operator.type) {
                    this.simplified = true;
                    return new ChainBinOpNode(new ArrayList<Node>(Arrays.asList(left, binOpRight.left, binOpRight.right)), node.operator.copy());
                }
            }

            // For Chained bin op nodes

            if (left.getClass() == ChainBinOpNode.class && right.getClass() != ChainBinOpNode.class) {
                ChainBinOpNode binOpLeft = (ChainBinOpNode) left;
                if (binOpLeft.operator.type == node.operator.type) {
                    ChainBinOpNode newChained = new ChainBinOpNode(node.operator.copy());
                    newChained.nodes.addAll(binOpLeft.nodes);
                    newChained.addNode(right);
                    this.simplified = true;
                    return newChained;
                }
            }
            if (left.getClass() == ChainBinOpNode.class && right.getClass() == ChainBinOpNode.class) {
                ChainBinOpNode binOpLeft = (ChainBinOpNode) left;
                ChainBinOpNode binOpRight = (ChainBinOpNode) right;
                if (binOpLeft.operator.type == node.operator.type && binOpRight.operator.type == node.operator.type) {
                    ChainBinOpNode newChained = new ChainBinOpNode(node.operator.copy());
                    newChained.nodes.addAll(binOpLeft.nodes);
                    newChained.nodes.addAll(binOpRight.nodes);
                    this.simplified = true;
                    return newChained;
                }
            }
            if (left.getClass() != ChainBinOpNode.class && right.getClass() == ChainBinOpNode.class) {
                ChainBinOpNode binOpRight = (ChainBinOpNode) right;
                if (binOpRight.operator.type == node.operator.type) {
                    ChainBinOpNode newChained = new ChainBinOpNode(node.operator.copy());
                    newChained.nodes.addAll(binOpRight.nodes);
                    newChained.addNode(left);
                    this.simplified = true;
                    return newChained;
                }
            }
        }
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
        /*
        if (expr.getClass() == NumberNode.class)
        {
            this.simplified = true;
            return new NumberNode(new Token(TokenTypes.NUMBER, this.calculateUnaryOpNode(node)));
        };
        */
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
        if (expr.getClass() == ChainBinOpNode.class)
        {
            if (node.operator.type == TokenTypes.MINUS || node.operator.type == TokenTypes.PLUS)
            {
                ChainBinOpNode exprChainBinOp = (ChainBinOpNode) expr;
                Node first = exprChainBinOp.nodes.getFirst();
                Node newFirst = this.simplifyUnaryOpNode(new UnaryOpNode(new Token(TokenTypes.MINUS), first));
                exprChainBinOp.removeNode(0);
                exprChainBinOp.addNode(newFirst);
                this.simplified = true;
                return exprChainBinOp;
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
        if (inside.getClass() == BinOpNode.class) {
            BinOpNode binopnode = (BinOpNode) inside;
            if (binopnode.operator.type == TokenTypes.POW)
            {
                if (binopnode.left.equals(base))
                {
                    return binopnode.right;
                }
            }
        }
        return new LogNode(base, inside);
    }

    public Node simplifyChainBinOpNode(ChainBinOpNode node)
    {
        if (node.operator.type == TokenTypes.MULTIPLY)
        {
            ChainBinOpNode newChained = new ChainBinOpNode(node.operator.copy());
            boolean allNumber = true;
            for (Node insideNode: node.nodes)
            {
                Node newInside = this.simplifyNode(insideNode);
                if (newInside.getClass() == NumberNode.class)
                {
                    NumberNode insideNumber = (NumberNode) newInside;
                    if (insideNumber.numberToken.value == 0)
                    {
                        this.simplified = true;
                        return new NumberNode(new Token(TokenTypes.NUMBER, 0));
                    }
                    if (insideNumber.numberToken.value != 1)
                    {
                        newChained.addNode(insideNumber);
                    }
                }
                else if (newInside.getClass() == ChainBinOpNode.class)
                {
                    ChainBinOpNode newChainInside = (ChainBinOpNode) newInside;
                    allNumber = false;
                    if (newChainInside.operator.type == TokenTypes.MULTIPLY) {
                        this.simplified = true;
                        for (Node innerNewChainInside : newChainInside.nodes) {
                            newChained.addNode(innerNewChainInside);
                        }
                    }
                    else {newChained.addNode(newInside);}
                }
                else if (newInside.getClass() == BinOpNode.class)
                {
                    BinOpNode newBinOpInside = (BinOpNode) newInside;
                    allNumber = false;
                    if (newBinOpInside.operator.type == TokenTypes.MULTIPLY) {
                        this.simplified = true;
                        newChained.addNode(newBinOpInside.left);
                        newChained.addNode(newBinOpInside.right);
                    }
                    else {newChained.addNode(newInside);}
                }
                else {
                    allNumber = false;
                    newChained.addNode(newInside);
                }
            }
            if (allNumber)
            {
                this.simplified = true;
                System.out.println(newChained);
                return new NumberNode(new Token(TokenTypes.NUMBER, this.calculateNode(newChained)));
            }

            if (newChained.nodes.size() == 2)
            {
                return new BinOpNode(newChained.nodes.get(0), newChained.operator.copy(), newChained.nodes.get(1));
            }
            return newChained;
        }

        if (node.operator.type == TokenTypes.PLUS)
        {
            ChainBinOpNode newChained = new ChainBinOpNode(node.operator.copy());
            boolean allNumber = true;
            for (Node insideNode: node.nodes)
            {
                Node newInside = this.simplifyNode(insideNode);
                if (newInside.getClass() == NumberNode.class)
                {
                    NumberNode insideNumber = (NumberNode) newInside;
                    if (insideNumber.numberToken.value != 0)
                    {
                        newChained.addNode(insideNumber);
                    }
                }
                else if (newInside.getClass() == ChainBinOpNode.class)
                {
                    ChainBinOpNode newChainInside = (ChainBinOpNode) newInside;
                    allNumber = false;
                    if (newChainInside.operator.type == TokenTypes.PLUS) {
                        this.simplified = true;
                        for (Node innerNewChainInside : newChainInside.nodes) {
                            newChained.addNode(innerNewChainInside);
                        }
                    }
                    else {newChained.addNode(newInside);}
                }
                else if (newInside.getClass() == BinOpNode.class)
                {
                    BinOpNode newBinOpInside = (BinOpNode) newInside;
                    allNumber = false;
                    if (newBinOpInside.operator.type == TokenTypes.PLUS) {
                        this.simplified = true;
                        newChained.addNode(newBinOpInside.left);
                        newChained.addNode(newBinOpInside.right);
                    }
                    else {newChained.addNode(newInside);}
                }
                else {
                    allNumber = false;
                    newChained.addNode(newInside);
                }
            }
            if (allNumber)
            {
                this.simplified = true;
                System.out.println(newChained);
                return new NumberNode(new Token(TokenTypes.NUMBER, this.calculateNode(newChained)));
            }

            if (newChained.nodes.size() == 2)
            {
                return new BinOpNode(newChained.nodes.get(0), newChained.operator.copy(), newChained.nodes.get(1));
            }
            return newChained;
        }
        return null;
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
            case "compiler.ChainBinOpNode" ->
            {
                ChainBinOpNode chainBinOpNode = (ChainBinOpNode) node;
                return this.simplifyChainBinOpNode(chainBinOpNode);
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
            case TokenTypes.LOG ->
            {
                return new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.DIVIDE), new BinOpNode(new UnaryOpNode(new Token(TokenTypes.LN), new NumberNode(new Token(TokenTypes.NUMBER, 10))), new Token(TokenTypes.MULTIPLY), innerNode));
            }
            case TokenTypes.LN ->
            {
                return new BinOpNode(this.derivativeNode(innerNode), new Token(TokenTypes.DIVIDE), innerNode);
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
                if (left.getClass() == NumberNode.class) {
                    NumberNode leftN = (NumberNode) left;
                    if (leftN.numberToken.value == Math.E)
                    {
                        return new BinOpNode(this.derivativeNode(right), new Token(TokenTypes.MULTIPLY), node);
                    }
                }
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

    public Node derivativeChainBinOpNode(ChainBinOpNode node)
    {
        if (node.operator.type == TokenTypes.MULTIPLY) {
            ChainBinOpNode derivative = new ChainBinOpNode(new Token(TokenTypes.PLUS));
            for (int i = 0; i < node.nodes.size(); i++) {
                ChainBinOpNode nodeCopy = node.copy();
                nodeCopy.removeNode(i);
                nodeCopy.addNode(this.derivativeNode(node.nodes.get(i)));
                derivative.addNode(nodeCopy);
            }
            return derivative;
        }
        if (node.operator.type == TokenTypes.PLUS)
        {
            ChainBinOpNode derivative = new ChainBinOpNode(new Token(TokenTypes.PLUS));
            for (Node insideNode: node.nodes)
            {
                derivative.addNode(this.derivativeNode(insideNode));
            }
            return derivative;
        }
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
            case "compiler.ChainBinOpNode" -> {
                ChainBinOpNode chainBinOpNode = (ChainBinOpNode) node;
                yield this.derivativeChainBinOpNode(chainBinOpNode);
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
            case TokenTypes.LN -> Math.log(this.calculateNode(innerNode));
            case TokenTypes.LOG -> Math.log10(this.calculateNode(innerNode));
            default -> 0;
        };
    }

    public double calculateVariableNode(VariableNode node)
    {
        String variableName = node.variableToken.valueString;
        return this.variables.get(variableName);
    }

    public double calculateChainBinOpNode(ChainBinOpNode node)
    {
        if (node.operator.type == TokenTypes.MULTIPLY) {
            double total = 1;
            for (Node insideNode: node.nodes)
            {
                total *= this.calculateNode(insideNode);
            }
            return total;
        }
        else if (node.operator.type == TokenTypes.PLUS) {
            double total = 0;
            for (Node insideNode: node.nodes)
            {
                total += this.calculateNode(insideNode);
            }
            return total;
        }
        return 0;
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
                yield this.calculateLogNode(logNode);
            }
            case "compiler.ChainBinOpNode" -> {
                ChainBinOpNode chainBinOpNode = (ChainBinOpNode) node;
                yield this.calculateChainBinOpNode(chainBinOpNode);
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
        Lexer l = new Lexer("LOG(sin(x))");
        Parser p = new Parser(l.createTokens());
        Interpreter i = new Interpreter(p.parse());
        Interpreter i2 =  new Interpreter(i.derivative());
        System.out.println(i2);
        i2.setVariable("x", 3);
        System.out.println(i2.calculateValue());
    }
}
