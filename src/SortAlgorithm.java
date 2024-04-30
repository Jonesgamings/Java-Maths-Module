import java.util.ArrayList;
import java.util.Arrays;

public class SortAlgorithm
{
    public static double[] BubbleSortAscending(double [] array)
    {
        double[] newArray = array.clone();
        for (int i = 0; i < array.length; i++)
        {
            boolean swapped = false;
            for (int j = 0; j < array.length-i; j++)
            {
                if (j+1 == array.length) {continue;}
                double first = newArray[j];
                double second = newArray[j+1];
                if (second < first)
                {
                    newArray[j] = second;
                    newArray[j+1] = first;
                    swapped = true;
                }
            }
            if (!swapped) {return newArray;}
        }
        return newArray;
    }

    public static ArrayList<Double> BubbleSortAscending(ArrayList<Double> array)
    {
        ArrayList<Double> newArray = (ArrayList<Double>) array.clone();
        for (int i = 0; i < array.size(); i++)
        {
            boolean swapped = false;
            for (int j = 0; j < array.size()-i; j++)
            {
                if (j+1 == array.size()) {continue;}
                double first = newArray.get(j);
                double second = newArray.get(j + 1);
                if (second < first)
                {
                    newArray.set(j, second);
                    newArray.set(j + 1, first);
                    swapped = true;
                }
            }
            if (!swapped) {return newArray;}
        }
        return newArray;
    }

    public static double[] ShuttleSortAscending(double [] array)
    {
        double[] newArray = array.clone();
        for (int i = 1; i < array.length; i++)
        {
            for (int j = i; j > 0; j--)
            {
                double first = newArray[j];
                double second = newArray[j-1];
                boolean swapped = false;
                if (first < second)
                {
                    newArray[j-1] = first;
                    newArray[j] = second;
                    swapped = true;
                }
                if (!swapped) {break;}
            }
        }
        return newArray;
    }

    public static ArrayList<Double> ShuttleSortAscending(ArrayList<Double> array)
    {
        ArrayList<Double> newArray = (ArrayList<Double>) array.clone();
        for (int i = 1; i < array.size(); i++)
        {
            for (int j = i; j > 0; j--)
            {
                double first = newArray.get(j);
                double second = newArray.get(j - 1);
                boolean swapped = false;
                if (first < second)
                {
                    newArray.set(j - 1, first);
                    newArray.set(j, second);
                    swapped = true;
                }
                if (!swapped) {break;}
            }
        }
        return newArray;
    }

    public static ArrayList<Double> QuickSortAscending(ArrayList<Double> array)
    {
        if (array.size() == 1 || array.isEmpty()) {return array;}
        ArrayList<Double> left = new ArrayList<>();
        ArrayList<Double> right = new ArrayList<>();
        double pivot = array.getFirst();
        for (int i = 1; i < array.size(); i++)
        {
            if (array.get(i) < pivot)
            {
                left.add(array.get(i));
            }
            else {right.add(array.get(i));}
        }
        ArrayList<Double> sorted = QuickSortAscending(left);
        sorted.add(pivot);
        sorted.addAll(QuickSortAscending(right));
        return sorted;
    }

    public static double[] QuickSortAscending(double [] array)
    {
        if (array.length == 1 || array.length == 0) {return array;}
        ArrayList<Double> left = new ArrayList<>();
        ArrayList<Double> right = new ArrayList<>();
        double pivot = array[0];
        for (int i = 1; i < array.length; i++)
        {
            if (array[i] < pivot)
            {
                left.add(array[i]);
            }
            else {right.add(array[i]);}
        }
        ArrayList<Double> sorted = QuickSortAscending(left);
        sorted.add(pivot);
        sorted.addAll(QuickSortAscending(right));
        return sorted.stream().mapToDouble(i -> i).toArray();
    }

    public static double[] BubbleSortDescending(double [] array)
    {
        double[] newArray = array.clone();
        for (int i = 0; i < array.length; i++)
        {
            boolean swapped = false;
            for (int j = 0; j < array.length-i; j++)
            {
                if (j+1 == array.length) {continue;}
                double first = newArray[j];
                double second = newArray[j+1];
                if (second > first)
                {
                    newArray[j] = second;
                    newArray[j+1] = first;
                    swapped = true;
                }
            }
            if (!swapped) {return newArray;}
        }
        return newArray;
    }

    public static ArrayList<Double> BubbleSortDescending(ArrayList<Double> array)
    {
        ArrayList<Double> newArray = (ArrayList<Double>) array.clone();
        for (int i = 0; i < array.size(); i++)
        {
            boolean swapped = false;
            for (int j = 0; j < array.size()-i; j++)
            {
                if (j+1 == array.size()) {continue;}
                double first = newArray.get(j);
                double second = newArray.get(j + 1);
                if (second > first)
                {
                    newArray.set(j, second);
                    newArray.set(j + 1, first);
                    swapped = true;
                }
            }
            if (!swapped) {return newArray;}
        }
        return newArray;
    }

    public static double[] ShuttleSortDescending(double [] array)
    {
        double[] newArray = array.clone();
        for (int i = 1; i < array.length; i++)
        {
            for (int j = i; j > 0; j--)
            {
                double first = newArray[j];
                double second = newArray[j-1];
                boolean swapped = false;
                if (first > second)
                {
                    newArray[j-1] = first;
                    newArray[j] = second;
                    swapped = true;
                }
                if (!swapped) {break;}
            }
        }
        return newArray;
    }

    public static ArrayList<Double> ShuttleSortDescending(ArrayList<Double> array)
    {
        ArrayList<Double> newArray = (ArrayList<Double>) array.clone();
        for (int i = 1; i < array.size(); i++)
        {
            for (int j = i; j > 0; j--)
            {
                double first = newArray.get(j);
                double second = newArray.get(j - 1);
                boolean swapped = false;
                if (first > second)
                {
                    newArray.set(j - 1, first);
                    newArray.set(j, second);
                    swapped = true;
                }
                if (!swapped) {break;}
            }
        }
        return newArray;
    }

    public static ArrayList<Double> QuickSortDescending(ArrayList<Double> array)
    {
        if (array.size() == 1 || array.isEmpty()) {return array;}
        ArrayList<Double> left = new ArrayList<>();
        ArrayList<Double> right = new ArrayList<>();
        double pivot = array.getFirst();
        for (int i = 1; i < array.size(); i++)
        {
            if (array.get(i) > pivot)
            {
                left.add(array.get(i));
            }
            else {right.add(array.get(i));}
        }
        ArrayList<Double> sorted = QuickSortDescending(left);
        sorted.add(pivot);
        sorted.addAll(QuickSortDescending(right));
        return sorted;
    }

    public static double[] QuickSortDescending(double [] array)
    {
        if (array.length == 1 || array.length == 0) {return array;}
        ArrayList<Double> left = new ArrayList<>();
        ArrayList<Double> right = new ArrayList<>();
        double pivot = array[0];
        for (int i = 1; i < array.length; i++)
        {
            if (array[i] > pivot)
            {
                left.add(array[i]);
            }
            else {right.add(array[i]);}
        }
        ArrayList<Double> sorted = QuickSortDescending(left);
        sorted.add(pivot);
        sorted.addAll(QuickSortDescending(right));
        return sorted.stream().mapToDouble(i -> i).toArray();
    }

    public static void main(String[] args) {
        double[] array = new double[] {34, 15, 3, 75, 10, 4};
        System.out.println(Arrays.toString(SortAlgorithm.QuickSortDescending(array)));
    }
}
