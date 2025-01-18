package base;

public abstract class Statistics
{
    public static double mean(VectorND vector)
    {
        double total = 0;
        for (double val: vector.values)
        {
            total += val;
        }
        return total / vector.dimensions;
    }

    public static double mean(double[] array)
    {
        double total = 0;
        for (double val: array)
        {
            total += val;
        }
        return total / array.length;
    }

    public static double mean(Matrix matrix)
    {
        double total = 0;
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                total += matrix.getAt(i, j);
            }
        }
        return total / (matrix.rows * matrix.columns);
    }

    public static double mean(double[][] array2d)
    {
        double total = 0;
        for (int i = 0; i < array2d.length; i++) {
            for (int j = 0; j < array2d[i].length; j++) {
                total += array2d[i][j];
            }
        }
        return total / (array2d.length * array2d[0].length);
    }

    public static double median(VectorND vector)
    {
        double[] ascendingOrder = SortAlgorithm.QuickSortAscending(vector.values);
        if (ascendingOrder.length % 2 == 1)
        {
            return ascendingOrder[(int) ((double) ascendingOrder.length / 2 - 0.5)];
        }
        else
        {
            return (ascendingOrder[(ascendingOrder.length / 2)] + ascendingOrder[(ascendingOrder.length / 2 - 1)])/2;
        }
    }

    public static double median(double[] array)
    {
        double[] ascendingOrder = SortAlgorithm.QuickSortAscending(array);
        if (ascendingOrder.length % 2 == 1)
        {
            return ascendingOrder[(int) ((double) ascendingOrder.length / 2 - 0.5)];
        }
        else
        {
            return (ascendingOrder[(ascendingOrder.length / 2)] + ascendingOrder[(ascendingOrder.length / 2 - 1)])/2;
        }
    }

    public static double variance(VectorND vector)
    {
        double total = 0;
        double mean = Statistics.mean(vector);
        for (double val: vector.values)
        {
            total += Math.pow(val-mean, 2);
        }
        return total/vector.dimensions;
    }

    public static double variance(double[] array)
    {
        double total = 0;
        double mean = Statistics.mean(array);
        for (double val: array)
        {
            total += Math.pow(val-mean, 2);
        }
        return total/array.length;
    }

    public static double standardDeviation(VectorND vector)
    {
        double variance = Statistics.variance(vector);
        return Math.sqrt(variance);
    }

    public static double standardDeviation(double[] array)
    {
        double variance = Statistics.variance(array);
        return Math.sqrt(variance);
    }
}
