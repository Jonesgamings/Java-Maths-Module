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

    public Polynomial indefiniteIntergral(double constant)
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

    public double definiteIntergral(double lower, double upper)
    {
        Polynomial intergral = this.indefiniteIntergral(0);
        return  intergral.atX(upper) - intergral.atX(lower);
    }
}
