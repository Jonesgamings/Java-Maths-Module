import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
            if (coefficient != 0) {
                polyString.append(coefficient);
                if (this.degree - i == 1) {
                    polyString.append("x + ");
                } else {
                    if (this.degree - i != 0) {
                        polyString.append("x^").append(this.degree - i).append(" + ");
                    }
                }
            }
        }
        return polyString.toString();
    }

    public ComplexMatrix getAt(ComplexMatrix x)
    {
        ComplexMatrix total = new ComplexMatrix(x.rows, x.columns);
        for (int i=0; i < this.degree+1; i++) {
            total = total.add(x.power(degree-i).multiply(this.coefficients[i]));
        }
        return total;
    }

    public Matrix getAt(Matrix x)
    {
        Matrix total = new Matrix(x.rows, x.columns);
        for (int i=0; i < this.degree+1; i++) {
            total = total.add(x.power(degree-i).multiply(this.coefficients[i]));
        }
        return total;
    }

    public ComplexNumber getAt(ComplexNumber x)
    {
        ComplexNumber total = new ComplexNumber(0, 0);
        for (int i=0; i < this.degree+1; i++) {
            total = total.add(x.power(degree-i).multiply(this.coefficients[i]));
        }
        return total;
    }

    public double getAt(double x)
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

    public Polynomial derivative(int n)
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
        double y = getAt(x);
        double gradient = derivative(1).getAt(x);
        return Polynomial.straightLine(gradient, new Vector2D(x, y));
    }

    public Polynomial normal(double x)
    {
        double y = getAt(x);
        double gradient = -1/derivative(1).getAt(x);
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
        return  integral.getAt(upper) - integral.getAt(lower);
    }

    public double newtonRaphson(double start, int iterations)
    {
        double lastX = start;
        for (int i = 0; i < iterations; i++)
        {
            lastX = lastX - (this.getAt(lastX) / this.derivative(1).getAt(lastX));
        }
        return lastX;
    }

    public double newtonRaphson(double start)
    {
        double lastX = start;
        for (int i = 0; i < Function.iterations; i++)
        {
            lastX = lastX - (this.getAt(lastX) / this.derivative(1).getAt(lastX));
        }
        return lastX;
    }

    public ComplexNumber newtonRaphson(ComplexNumber start, int iterations)
    {
        ComplexNumber lastX = start;
        for (int i = 0; i < iterations; i++)
        {
            lastX = lastX.subtract(this.getAt(lastX).divide(this.derivative(1).getAt(lastX)));
        }
        return lastX;
    }

    public ComplexNumber newtonRaphson(ComplexNumber start)
    {
        ComplexNumber lastX = start;
        for (int i = 0; i < Function.iterations; i++)
        {
            lastX = lastX.subtract(this.getAt(lastX).divide(this.derivative(1).getAt(lastX)));
        }
        return lastX;
    }

    public ComplexNumber[] findComplexRoot(ComplexNumber min, ComplexNumber max)
    {
        ComplexNumber last;
        ComplexNumber[] roots = new ComplexNumber[this.degree];
        int lastIndex = -1;
        for (double r = min.real; r < max.real+1; r++) {
            last = new ComplexNumber(0 ,0);
            for (double i = min.imaginary; i < max.imaginary+1; i++) {
                System.out.println(this.getAt(new ComplexNumber(r, i)) + " <- " + new ComplexNumber(r, i));
                if (lastIndex + 1 >= this.degree) {
                    break;
                }
                if (this.getAt(new ComplexNumber(r, i)).real == 0) {
                    roots[lastIndex + 1] = new ComplexNumber(r, i);
                    lastIndex++;
                }
                else if (((last.real < 0 && this.getAt(new ComplexNumber(r, i).real) > 0) || (last.real > 0 && this.getAt(new ComplexNumber(r, i)).real < 0)) && last.imaginary/this.getAt(new ComplexNumber(r, i)).imaginary >= 0) {
                    roots[lastIndex + 1] = newtonRaphson(new ComplexNumber(r, i));
                    lastIndex++;

                }
                last = getAt(new ComplexNumber(r, i));
            }
        }
        return roots;
    }

    public double[] findRealRoot(double min, double max)
    {
        double last = 0;
        double[] roots = new double[this.degree];
        int lastIndex = -1;
        for (double i = min; i < max; i++)
        {
            if (lastIndex+1 >= this.degree) {break;}
            if ((last < 0 && this.getAt(i) > 0) || (last > 0 && this.getAt(i) < 0))
            {
                roots[lastIndex+1] = newtonRaphson(i);
            }
            else if (this.getAt(i) == 0)
            {
                roots[lastIndex + 1] = i;
            }
            lastIndex++;
            last = getAt(i);
        }
        return roots;
    }

    public ComplexNumber[] roots()
    {
        if (degree == 2)
        {
            return Polynomial.quadraticFormula(this);
        }
        return null;
    }

    public Polynomial divide(Polynomial p)
    {
        if (p.degree != 1) {return null;}
        double lastSum = this.coefficients[0];
        double root = -p.coefficients[1] / p.coefficients[0];
        ArrayList<Double> newCoefficients = new ArrayList<>();
        for (int i = 1; i < this.coefficients.length; i++)
        {
            newCoefficients.add(lastSum);
            lastSum = (lastSum * root) + this.coefficients[i];
        }
        newCoefficients.add(lastSum);
        if (lastSum == 0)
        {
            newCoefficients.removeLast();
            return new Polynomial(newCoefficients.stream().mapToDouble(i -> i).toArray());
        }
        return null;
    }

    public Polynomial divide(double root)
    {
        double lastSum = this.coefficients[0];
        ArrayList<Double> newCoefficients = new ArrayList<>();
        for (int i = 1; i < this.coefficients.length; i++)
        {
            newCoefficients.add(lastSum);
            lastSum = (lastSum * root) + this.coefficients[i];
        }
        newCoefficients.add(lastSum);
        if (lastSum == 0)
        {
            newCoefficients.removeLast();
            return new Polynomial(newCoefficients.stream().mapToDouble(i -> i).toArray());
        }
        return null;
    }

    public Polynomial divide(ComplexNumber root)
    {
        ComplexNumber lastSum = new ComplexNumber(this.coefficients[0], 0);
        ArrayList<ComplexNumber> newCoefficients = new ArrayList<>();
        for (int i = 1; i < this.coefficients.length; i++)
        {
            newCoefficients.add(lastSum);
            lastSum = (root.multiply(lastSum)).add(this.coefficients[i]);
        }
        newCoefficients.add(lastSum);
        if (Objects.equals(lastSum, new ComplexNumber(0, 0)))
        {
            newCoefficients.removeLast();
            double[] newCoefficientsArray = new double[newCoefficients.size()];
            for (int j = 0; j < newCoefficients.size(); j++)
            {
                newCoefficientsArray[j] = newCoefficients.get(j).real;
            }
            return new Polynomial(newCoefficientsArray);
        }
        return null;
    }

    public boolean checkRoot(double root)
    {
        double lastSum = this.coefficients[0];
        for (int i = 1; i < this.coefficients.length; i++)
        {
            lastSum = (lastSum * root) + this.coefficients[i];
        }
        return lastSum == 0;
    }

    public boolean checkRoot(ComplexNumber root)
    {
        {
            ComplexNumber lastSum = new ComplexNumber(this.coefficients[0], 0);
            for (int i = 1; i < this.coefficients.length; i++)
            {
                lastSum = (root.multiply(lastSum)).add(this.coefficients[i]);
            }
            return Objects.equals(lastSum, new ComplexNumber(0, 0));
        }
    }

    public static ComplexNumber[] quadraticFormula(Polynomial polynomial)
    {
        if (polynomial.degree != 2) {return null;}
        double a = polynomial.coefficients[0];
        double b = polynomial.coefficients[1];
        double c = polynomial.coefficients[2];
        double discriminant = Math.sqrt(Math.pow(b, 2) - 4 * a * c);
        if (discriminant >= 0) {
            return new ComplexNumber[]{new ComplexNumber((-b + discriminant) / (2 * a), 0), new ComplexNumber((-b - discriminant) / (2 * a), 0)};
        }
        else
        {
            return new ComplexNumber[]{new ComplexNumber(-b/(2*a),Math.sqrt(Math.abs(discriminant))/(2*a)), new ComplexNumber(-b/(2*a),-Math.sqrt(Math.abs(discriminant))/(2*a))};
        }
    }

    public Polynomial multiply(Polynomial polynomial)
    {
        return new Polynomial(Polynomial.Convolution(this.coefficients, polynomial.coefficients));
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

    public static double[] Convolution(double array1[], double[] array2)
    {
        double[] newArray = new double[array1.length + array2.length-1];
        for (int i =0; i < newArray.length; i++)
        {
            double total = 0;
            for (int j =0;j < Math.max(array1.length, array2.length); j++)
            {
                double first;
                double second;
                if (i - j < 0 || i - j >= array1.length) {first = 0;}
                else {first  = array1[i - j];}

                if (j >= array2.length) {second = 0;}
                else {second = array2[j];}

                total += first * second;
            }
            newArray[i] = total;
        }
        return newArray;
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
        Polynomial p = new Polynomial(new double[] {1, -1, -3, 5});
        System.out.println(p);
        System.out.println(Arrays.toString(p.findComplexRoot(new ComplexNumber(-1, -1), new ComplexNumber(1, 1))));
    }
}
