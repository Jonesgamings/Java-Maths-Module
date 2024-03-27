package compiler;

import java.util.ArrayList;
import java.util.Objects;

public class Function {

    String mathString;
    ArrayList<Token> tokens;
    Node ASTree;
    Interpreter interpreter;
    String variableName;
    static final double deltaX = Math.pow(10, -10);
    static final double segments = Math.pow(10, 4);
    static final double iterations = Math.pow(10, 4);

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
                if (!Objects.equals(token.valueString, "e") && !Objects.equals(token.valueString, "pi") && !Objects.equals(token.valueString, "h"))
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
        double h = (end - start) / segments;
        double sum=0;
        for (int i = 1; i < segments; i++)
        {
            sum += this.getAt(start + (i * h));
        }
        return (h/2) * (startY + endY + 2*sum);
    }

    public double volumeRevolution(double start, double end)
    {
        double startY = Math.pow(this.getAt(start), 2);
        double endY = Math.pow(this.getAt(end), 2);
        double h = (end - start) / segments;
        double sum=0;
        for (int i = 1; i < segments; i++)
        {
            sum += Math.pow(this.getAt(start + (i * h)), 2);
        }
        return Math.PI * (h/2) * (startY + endY + 2*sum);
    }

    public double volumeRevolution(double start, double end, double segments)
    {
        double startY = Math.pow(this.getAt(start), 2);
        double endY = Math.pow(this.getAt(end), 2);
        double h = (end - start) / segments;
        double sum=0;
        for (int i = 1; i < segments; i++)
        {
            sum += Math.pow(this.getAt(start + (i * h)), 2);
        }
        return Math.PI * (h/2) * (startY + endY + 2*sum);
    }

    public double meanValue(double start, double end, int segments)
    {
        return 1/(end-start) * this.integral(start, end, segments);
    }

    public double meanValue(double start, double end)
    {
        return 1/(end-start) * this.integral(start, end);
    }

    public double root(double start, int iterations)
    {
        double lastX = start;
        for (int i = 0; i < iterations; i++)
        {
            lastX = lastX - (this.getAt(lastX) / this.derivative(lastX));
        }
        return lastX;
    }

    public double root(double start)
    {
        double lastX = start;
        for (int i = 0; i < iterations; i++)
        {
            lastX = lastX - (this.getAt(lastX) / this.derivative(lastX));
        }
        return lastX;
    }

    public static void main(String[] args) {
        Function function = new Function("e-e^(sin(x))");
        System.out.println(function.getAt(1));
    }
}
