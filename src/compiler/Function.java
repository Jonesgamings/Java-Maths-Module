package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class Function {

    String mathString;
    ArrayList<Token> tokens;
    Node ASTree;
    Interpreter interpreter;
    String variableName;
    static double deltaX = Math.pow(10, -30);

    public Function(String mathString)
    {
        this.mathString = mathString;
        this.tokens = new Lexer(mathString).createTokens();
        this.ASTree = new Parser(this.tokens).parse();
        this.interpreter = new Interpreter(this.ASTree);
        this.getVariableName();
    }

    public void getVariableName()
    {
        for (Token token: this.tokens)
        {
            if (token.type == TokenTypes.VARIABLE)
            {
                if (token.valueString != "e" && token.valueString != "pi")
                {
                    this.variableName = token.valueString;
                }
            }
        }
    }

    public double getAt(double position)
    {
        this.interpreter.setVariable(this.variableName, position);
        return this.interpreter.calculateValue();
    }

    public double derivative(double position)
    {
        double startValue = this.getAt(position);
        double endValue = this.getAt(position + deltaX);
        return (endValue - startValue) / deltaX;
    }

    public double integral(double start, double end, int segments)
    {
        double startY = this.getAt(start);
        double endY = this.getAt(end);
        double h = (end - start) / segments;
        double sum=0;
        for (int i = 1; i < segments; i++)
        {
            sum += this.getAt(start + (i * h));
        }
        return (h/2) * (startY + endY + 2*sum);
    }

    public double integral(double start, double end)
    {
        double startY = this.getAt(start);
        double endY = this.getAt(end);
        int segments = 10000;
        double h = (end - start) / segments;
        double sum=0;
        for (int i = 1; i < segments; i++)
        {
            sum += this.getAt(start + (i * h));
        }
        return (h/2) * (startY + endY + 2*sum);
    }

    public static void main(String[] args) {
        Function function = new Function("sin(x)");
        System.out.println(function.integral(0, 1000, 100));
    }
}
