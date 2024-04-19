import java.util.Map;
import java.util.Objects;

public class ComplexNumber
{
    double real;
    double imaginary;
    final static ComplexNumber NaN = new ComplexNumber(Double.NaN, Double.NaN);

    public static boolean isNaN(ComplexNumber number)
    {
        return number.equals(NaN);
    }

    public ComplexNumber(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(real, that.real) == 0 && Double.compare(imaginary, that.imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    public boolean isReal()
    {
        return this.imaginary == 0;
    }

    @Override
    public String toString() {
        if (this.imaginary == 0) {return this.real + "";}
        else if (this.real == 0) {return this.imaginary + "i";}
        else if (this.imaginary > 0) {return this.real + " + " + this.imaginary + "i";}
        else {return this.real + " - " + Math.abs(this.imaginary) + "i";}
    }

    public ComplexNumber conjugate()
    {
        return new ComplexNumber(this.real, -this.imaginary);
    }

    public ComplexNumber add(ComplexNumber number)
    {
        return new ComplexNumber(this.real + number.real, this.imaginary + number.imaginary);
    }

    public ComplexNumber subtract(ComplexNumber number)
    {
        return new ComplexNumber(this.real - number.real, this.imaginary - number.imaginary);
    }

    public ComplexNumber normalise()
    {
        return this.divide(this.magnitude());
    }
    public ComplexNumber multiply(ComplexNumber number)
    {
        return new ComplexNumber(this.real * number.real - this.imaginary * number.imaginary, this.real * number.imaginary + this.imaginary * number.real);
    }

    public static Vector2D rotate(Vector2D v, double angle)
    {
        return v.toComplex().multiply(new ComplexNumber(Math.cos(angle), Math.sin(angle))).toVector();
    }

    public static ComplexNumber rotate(ComplexNumber c, double angle)
    {
        return c.multiply(new ComplexNumber(Math.cos(angle), Math.sin(angle)));
    }

    public Vector2D transform(Vector2D vector)
    {
        return this.multiply(vector.toComplex()).toVector();
    }

    public ComplexNumber transform(ComplexNumber complexNumber)
    {
        return this.multiply(complexNumber);
    }

    public ComplexNumber inverse()
    {
        return this.conjugate().divide(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2));
    }
    public ComplexNumber divide(ComplexNumber number)
    {
        ComplexNumber conjugate = number.conjugate();
        double divideFactor = this.real * conjugate.real - this.imaginary * conjugate.imaginary;
        return this.multiply(conjugate).divide(divideFactor);
    }

    public ComplexNumber multiply(double factor)
    {
        return new ComplexNumber(this.real * factor, this.imaginary * factor);
    }
    public ComplexNumber divide(double factor)
    {
        return new ComplexNumber(this.real / factor, this.imaginary / factor);
    }
    public ComplexNumber add(double number)
    {
        return new ComplexNumber(this.real + number, this.imaginary);
    }

    public ComplexNumber subtract(double number)
    {
        return new ComplexNumber(this.real - number, this.imaginary);
    }

    public double angle()
    {
        return Math.atan2(this.imaginary, this.real);
    }

    public double magnitude()
    {
        return Math.pow(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2), 0.5);
    }

    public ComplexNumber power(double power)
    {
        double angle = angle();
        double magnitude = magnitude();
        return new ComplexNumber(Math.pow(magnitude, power) * Math.cos(power * angle), Math.pow(magnitude, power) * Math.sin(power * angle));
    }

    public ComplexNumber power(ComplexNumber power)
    {
        Polynomial eX = Polynomial.NtoX(Math.E, 200);
        ComplexNumber newComplex = power.multiply(new ComplexNumber(Math.log(this.magnitude()), this.angle()));
        return eX.getAt(newComplex);
    }

    public ComplexNumber ln()
    {
        return new ComplexNumber(Math.log(Math.sqrt(real*real + imaginary*imaginary)), Math.atan(imaginary/real));
    }

    public ComplexNumber log(double base)
    {
        return this.ln().divide(Math.log(base));
    }

    public ComplexNumber sinh()
    {
        return new ComplexNumber(Math.sinh(this.real) * Math.cos(this.imaginary), Math.cosh(this.real) * Math.sin(this.imaginary));
    }

    public ComplexNumber cosh()
    {
        return new ComplexNumber(Math.cosh(this.real) * Math.cos(this.imaginary), -1 * Math.sinh(this.real) * Math.sin(this.imaginary));
    }

    public ComplexNumber tanh()
    {
        return this.sinh().divide(this.cosh());
    }

    public ComplexNumber sin()
    {
        return new ComplexNumber(Math.sin(this.real) * Math.cosh(this.imaginary), Math.cos(this.real) * Math.sinh(this.imaginary));
    }

    public ComplexNumber cos()
    {
        return new ComplexNumber(Math.cos(this.real) * Math.cosh(this.imaginary), -1 * Math.sin(this.real) * Math.sinh(this.imaginary));
    }

    public ComplexNumber tan()
    {
        return this.sin().divide(this.cos());
    }

    public Vector2D toVector()
    {
        return new Vector2D(this.real, this.imaginary);
    }

    public Matrix toMatrix()
    {
        return Matrix.rotation2D(this.normalise().angle());
    }

    public static void main(String[] args) {
        ComplexNumber c1 = new ComplexNumber(1 , 1);
        ComplexNumber c2 = new ComplexNumber(3, 4);
        System.out.println(c1.sin());
    }
}
