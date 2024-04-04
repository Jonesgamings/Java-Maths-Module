import compiler.*;

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

    public Function copy()
    {
        return new Function(this.ASTree, this.variableName);
    }

    public Polynomial taylorSeries(int degree)
    {
        ArrayList<Double> coefficients = new ArrayList<>();
        Function f = this;
        for (int i = 0; i < degree; i++)
        {
            coefficients.add(f.getAt(0)/Polynomial.Factorial(i));
            f.removeVariable(variableName);
            f = f.derivative(variableName);
        }
        return new Polynomial(coefficients.reversed().stream().mapToDouble(i -> i).toArray());
    }

    @Override
    public String toString()
    {
        return this.interpreter.toString();
    }

    public Function(Node ASTree, String variableName)
    {
        this.ASTree = ASTree;
        this.interpreter = new Interpreter(this.ASTree);
        this.variableName = variableName;
    }

    public Function setVariableName(String variableName)
    {
        this.variableName = variableName;
        return this;
    }

    public void getVariableName()
    {
        if(!tokens.isEmpty()) {
            for (Token token : this.tokens) {
                if (token.type == TokenTypes.VARIABLE) {
                    if (!Objects.equals(token.valueString, "e") && !Objects.equals(token.valueString, "pi") && !Objects.equals(token.valueString, "h")) {
                        this.variableName = token.valueString;
                        return;
                    }
                }
            }
        }
        this.variableName = "x";
    }

    public void removeVariable(String variableName)
    {
        this.interpreter.removeVariable(variableName);
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

    public Function derivative(String variableName)
    {
        this.variableName = variableName;
        return new Function(this.interpreter.derivative(), this.variableName);
    }

    public Function derivative()
    {
        return new Function(this.interpreter.derivative(), variableName);
    }

    public Function derivative(int n, String variableName)
    {
        this.variableName = variableName;
        Function function = this;
        for (int i = 0; i < n; i++)
        {
            function = function.derivative(variableName);
        }
        return function;
    }
    public Function derivative(int n)
    {
        Function function = this;
        for (int i = 0; i < n; i++)
        {
            function = function.derivative(variableName);
        }
        return function;
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
        Function function = new Function("x^2").setVariableName("x");
        Polynomial p = function.taylorSeries(100);
        System.out.println(p.atX(new Matrix(2, 2).setMatrix(new double[][] {{1 , 0}, {0, 1}})));
    }
}
