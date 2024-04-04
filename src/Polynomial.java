import java.util.ArrayList;

public class Polynomial
{
    double[] coefficients;
    int degree;

    public Polynomial(double[] coefficients)
    {
        this.coefficients = coefficients;
        this.degree = this.coefficients.length - 1;
    }

    @Override
    public String toString()
    {
        StringBuilder polyString = new StringBuilder();
        for (int i=0; i <= this.degree; i++)
        {
            double coefficient = this.coefficients[i];
            polyString.append(coefficient);
            if (this.degree-i == 1) {polyString.append("x + ");}
            else{if (this.degree-i != 0) {polyString.append("x^").append(this.degree - i).append(" + ");}}}
        return polyString.toString();
    }

    public ComplexMatrix atX(ComplexMatrix x)
    {
        ComplexMatrix total = new ComplexMatrix(x.rows, x.columns);
        for (int i=0; i < this.degree+1; i++) {
            total = total.add(x.power(degree-i).multiply(this.coefficients[i]));
        }
        return total;
    }

    public Matrix atX(Matrix x)
    {
        Matrix total = new Matrix(x.rows, x.columns);
        for (int i=0; i < this.degree+1; i++) {
            total = total.add(x.power(degree-i).multiply(this.coefficients[i]));
        }
        return total;
    }

    public ComplexNumber atX(ComplexNumber x)
    {
        ComplexNumber total = new ComplexNumber(0, 0);
        for (int i=0; i < this.degree+1; i++) {
            total = total.add(x.power(degree-i).multiply(this.coefficients[i]));
        }
        return total;
    }

    public double atX(double x)
    {
        double total = 0;
        for (int i=0; i < this.degree+1; i++) {
            total += this.coefficients[i] * Math.pow(x, this.degree - i);
        }
        return total;
    }

    public static Polynomial straightLine(double gradient, double yIntercept)
    {
        return new Polynomial(new double[] {gradient, yIntercept});
    }

    public static Vector2D toVector(Polynomial straightLine)
    {
        if (straightLine.degree != 1) {return null;}
        return new Vector2D(1, straightLine.coefficients[0]);
    }

    public static Polynomial straightLine(double gradient, Vector2D point)
    {
        return new Polynomial(new double[] {gradient, point.y - gradient*point.x});
    }

    private static int Product(int n, int starting)
    {
        int total = 1;
        for (int current=0; current < n; current++)
        {
            if (starting - current == 0) {break;}
            total *= (starting - current);
        }
        return total;
    }

    public Polynomial differentiate(int n)
    {
        double[] newCoefficients = new double[this.degree-n+1];
        for (int i=0; i < this.degree-n+1; i++)
        {
            int power = this.degree-i;
            double coefficient = this.coefficients[i];
            newCoefficients[i] = Polynomial.Product(n, power) * coefficient;
        }
        return new Polynomial(newCoefficients);
    }

    public Polynomial tangent(double x)
    {
        double y = atX(x);
        double gradient = differentiate(1).atX(x);
        return Polynomial.straightLine(gradient, new Vector2D(x, y));
    }

    public Polynomial normal(double x)
    {
        double y = atX(x);
        double gradient = -1/differentiate(1).atX(x);
        return Polynomial.straightLine(gradient, new Vector2D(x, y));
    }

    public Polynomial indefiniteIntegral(double constant)
    {
        double[] newCoefficients = new double[this.degree+2];
        for (int i=0; i < this.degree+1; i++)
        {
            int power = this.degree-i+1;
            double coefficient = this.coefficients[i];
            newCoefficients[i] = coefficient/power;
        }
        newCoefficients[this.degree+1] = constant;
        return new Polynomial(newCoefficients);
    }

    public double definiteIntegral(double lower, double upper)
    {
        Polynomial integral = this.indefiniteIntegral(0);
        return  integral.atX(upper) - integral.atX(lower);
    }

    public double[] roots()
    {
        return new double[this.degree];
    }

    public static double Factorial(int n)
    {
        if (n < 0) {return Double.NaN;}
        if (n == 1 || n == 0) {return 1;}
        double total = 1;
        for (int i =1; i<n+1; i++)
        {
            total *= i;
        }
        return total;
    }

    public static Polynomial NtoX(double n, int accuracy)
    {
        if (accuracy > 171) {accuracy = 171;}
        ArrayList<Double> coefficients = new ArrayList<>();
        for (int i =0; i<accuracy; i++)
        {
            coefficients.add(Math.pow(Math.log(n), i) / Polynomial.Factorial(i));
        }
        return new Polynomial(coefficients.reversed().stream().mapToDouble(i -> i).toArray());
    }

    public static void main(String[] args) {
        Polynomial p = new Polynomial(new double[] {1, 2, 3, 4});
        System.out.println(p);
    }
}
