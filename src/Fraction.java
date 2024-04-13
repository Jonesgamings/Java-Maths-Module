import java.util.ArrayList;
import java.util.Objects;

public class Fraction
{
    int numerator;
    int denominator;

    public Fraction(int numerator, int denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction convertRecursive(int recursiveDigits)
    {
        int lengthDigits = String.valueOf(recursiveDigits).length();
        if (lengthDigits >= 9) {return null;}
        return new Fraction(recursiveDigits, (int) (Math.pow(10, lengthDigits) - 1)).simplify();
    }

    public static Fraction convertDouble(double number)
    {
        String numberString = Double.toString(number);;
        int digitsAfterFullStop = 0;
        boolean fullstop = false;
        for (int i = 0; i < numberString.length(); i++)
        {
            if ((Character.toString(numberString.charAt(i))).equals(".")) {fullstop = true;}
            else if (fullstop) {digitsAfterFullStop += 1;}
        }
        if (digitsAfterFullStop >= 9)
        {
            digitsAfterFullStop = 9;
        }
        return new Fraction((int) (number * Math.pow(10, digitsAfterFullStop)), (int) Math.pow(10, digitsAfterFullStop)).simplify();
    }

    public double evaluate()
    {
        return (double) this.numerator/this.denominator;
    }

    public static ArrayList<Integer> primeFactors(int number)
    {
        ArrayList<Integer> factors = new ArrayList<>();
        while (number % 2 == 0)
        {
            factors.add(2);
            number /= 2;
        }

        for (int i = 3; i <= Math.sqrt(number); i += 2)
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
        ArrayList<Integer> numeratorFactors = primeFactors(numerator);
        ArrayList<Integer> denominatorFactors = primeFactors(denominator);

        if (numeratorFactors.size() == 1)
        {
            if (!denominatorFactors.contains(numerator)) {return this;}
        }

        if (denominatorFactors.size() == 1)
        {
            if (!numeratorFactors.contains(denominator)) {return this;}
        }

        for (int n = 0; n < numeratorFactors.size(); n++)
        {
            for (int d = 0; d < denominatorFactors.size(); d++)
            {
                if (Objects.equals(numeratorFactors.get(n), denominatorFactors.get(d))){
                    numeratorFactors.remove(n);
                    denominatorFactors.remove(d);
                }
            }
        }
        int newNumerator = 1;
        int newDenominator = 1;
        for (int i: numeratorFactors)
        {
            newNumerator *= i;
        }
        for (int j: denominatorFactors)
        {
            newDenominator *= j;
        }
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction power(int number)
    {
        return new Fraction((int) Math.pow(this.numerator, number), (int) Math.pow(this.denominator, number));
    }

    public Fraction inverse()
    {
        return new Fraction(this.denominator, this.numerator);
    }

    public Fraction multiply(Fraction fraction)
    {
        return new Fraction(fraction.numerator * this.numerator, fraction.denominator * this.denominator);
    }

    public Fraction multiply(int number)
    {
        return new Fraction(this.numerator * number, this.denominator);
    }

    public Fraction divide(Fraction fraction)
    {
        return this.multiply(fraction.inverse());
    }

    public Fraction divide(int number)
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

    public Fraction add(int number)
    {
        return new Fraction(this.denominator * number + this.numerator, this.denominator);
    }

    public Fraction subtract(int number)
    {
        return new Fraction(this.numerator - this.denominator * number, this.denominator);
    }

    public static void main(String[] args) {
        System.out.println(Fraction.convertRecursive(9));
    }
}
