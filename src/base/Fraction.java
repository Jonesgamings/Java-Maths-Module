package base;

import java.util.ArrayList;
import java.util.Objects;

public class Fraction
{
    long numerator;
    long denominator;

    public Fraction(int numerator, int denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction(long numerator, long denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction convertRecursive(long recursiveDigits)
    {
        long lengthDigits = String.valueOf(recursiveDigits).length();
        if (lengthDigits >= 15) {return null;}
        return new Fraction(recursiveDigits, (long) (Math.pow(10, lengthDigits) - 1)).simplify();
    }

    public static Fraction convertDouble(double number)
    {
        String numberString = Double.toString(number);;
        long digitsAfterFullStop = 0;
        boolean fullstop = false;
        for (long i = 0; i < numberString.length(); i++)
        {
            if ((Character.toString(numberString.charAt((int) i))).equals(".")) {fullstop = true;}
            else if (fullstop) {digitsAfterFullStop += 1;}
        }
        if (digitsAfterFullStop >= 15)
        {
            digitsAfterFullStop = 15;
        }
        return new Fraction((long) (number * Math.pow(10, digitsAfterFullStop)), (long) Math.pow(10, digitsAfterFullStop)).simplify();
    }

    public double evaluate()
    {
        return (double) this.numerator/this.denominator;
    }

    public static ArrayList<Long> primeFactors(long number)
    {
        ArrayList<Long> factors = new ArrayList<>();
        while (number % 2 == 0)
        {
            factors.add(2l);
            number /= 2;
        }

        for (long i = 3; i <= Math.sqrt(number); i += 2)
        {
            while (number % i == 0)
            {
                factors.add(i);
                number /= i;
            }
        }

        if (number > 2)
        {
            factors.add(number);
        }
        return factors;
    }

    @Override
    public String toString() {
        if (denominator == 1) {return this.numerator + "";}
        return this.numerator + "/" + this.denominator;
    }

    public Fraction simplify()
    {
        if (numerator % denominator == 0) {return new Fraction((numerator / denominator), 1);}
        ArrayList<Long> numeratorFactors = primeFactors(numerator);
        ArrayList<Long> denominatorFactors = primeFactors(denominator);

        if (numeratorFactors.size() == 1)
        {
            if (!denominatorFactors.contains(numerator)) {return this;}
        }

        if (denominatorFactors.size() == 1)
        {
            if (!numeratorFactors.contains(denominator)) {return this;}
        }

        for (long n = 0; n < numeratorFactors.size(); n++)
        {
            for (long d = 0; d < denominatorFactors.size(); d++)
            {
                if (Objects.equals(numeratorFactors.get((int) n), denominatorFactors.get((int) d))){
                    numeratorFactors.remove(n);
                    denominatorFactors.remove(d);
                }
            }
        }
        long newNumerator = 1;
        long newDenominator = 1;
        for (long i: numeratorFactors)
        {
            newNumerator *= i;
        }
        for (long j: denominatorFactors)
        {
            newDenominator *= j;
        }
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction power(long number)
    {
        return new Fraction((long) Math.pow(this.numerator, number), (long) Math.pow(this.denominator, number));
    }

    public Fraction inverse()
    {
        return new Fraction(this.denominator, this.numerator);
    }

    public Fraction multiply(Fraction fraction)
    {
        return new Fraction(fraction.numerator * this.numerator, fraction.denominator * this.denominator);
    }

    public Fraction multiply(long number)
    {
        return new Fraction(this.numerator * number, this.denominator);
    }

    public Fraction divide(Fraction fraction)
    {
        return this.multiply(fraction.inverse());
    }

    public Fraction divide(long number)
    {
        return new Fraction(this.numerator, this.denominator * number);
    }

    public Fraction add(Fraction fraction)
    {
        return new Fraction(this.denominator * fraction.numerator + fraction.denominator * this.numerator, this.denominator * fraction.denominator);
    }

    public Fraction subtract(Fraction fraction)
    {
        return new Fraction( fraction.denominator * this.numerator - this.denominator * fraction.numerator, this.denominator * fraction.denominator);
    }

    public Fraction add(long number)
    {
        return new Fraction(this.denominator * number + this.numerator, this.denominator);
    }

    public Fraction subtract(long number)
    {
        return new Fraction(this.numerator - this.denominator * number, this.denominator);
    }

    public static void main(String[] args) {
        System.out.println(Fraction.convertDouble(Math.log(2)));
    }
}
