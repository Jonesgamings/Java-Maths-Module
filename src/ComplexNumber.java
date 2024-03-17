import java.util.Map;

public class ComplexNumber
{
    double real;
    double imaginary;

    public ComplexNumber(double real, double imaginary)
    {
        this.real = real;
        this.imaginary = imaginary;
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
    public ComplexNumber multiply(ComplexNumber number)
    {
        return new ComplexNumber(this.real * number.real - this.imaginary * number.imaginary, this.real * number.imaginary + this.imaginary * number.real);
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
        double angle = Math.atan2(this.imaginary, this.real);
        return angle;
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
}
