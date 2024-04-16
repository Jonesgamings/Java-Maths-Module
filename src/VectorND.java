import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class VectorND
{
    public double[] values;
    public int dimensions;

    public VectorND(double[] values)
    {
        this.values = values.clone();
        this.dimensions = this.values.length;
    }

    public VectorND(int dimensions)
    {
        this.values = new double[dimensions];
        this.dimensions = dimensions;
    }

    public VectorND(ArrayList<Double> valuesList)
    {
        this.values = valuesList.stream().mapToDouble(i -> i).toArray();
        this.dimensions = valuesList.size();
    }

    public VectorND project(VectorND a)
    {
        return this.multiply((this.dot(a) / this.dot(this)));
    }

    public VectorND reject(VectorND a)
    {
        return a.subtract(this.project(a));
    }

    public static VectorND projection(VectorND a, VectorND b)
    {
        return b.multiply((b.dot(a) / b.dot(b)));
    }

    public static VectorND rejection(VectorND a, VectorND b)
    {
        return a.subtract(VectorND.projection(a, b));
    }

    public VectorND inverse()
    {
        return this.divide(Math.pow(this.magnitude(),2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VectorND vectorND = (VectorND) o;
        return dimensions == vectorND.dimensions && Arrays.equals(values, vectorND.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    public double distance(VectorND vector)
    {
        if (vector.dimensions != this.dimensions) {return Double.NaN;}
        double squaredSum = 0;
        for (int i = 0; i < dimensions; i++)
        {
            squaredSum += Math.pow(this.getValue(i) - vector.getValue(i), 2);
        }
        return Math.sqrt(squaredSum);
    }

    public VectorND midpoint(VectorND vector)
    {
        if (vector.dimensions != this.dimensions) {return null;}
        VectorND newVector = new VectorND(dimensions);
        for (int i = 0; i < dimensions; i++)
        {
            newVector.setValue(i, (this.getValue(i) + vector.getValue(i))/2);
        }
        return newVector;
    }

    public boolean setValue(int position, double value)
    {
        if (position >= this.dimensions) {return false;}
        this.values[position] = value;
        return true;
    }

    public double getValue(int position)
    {
        if (position >= this.dimensions) {return Double.NaN;}
        return this.values[position];
    }

    public VectorND add(VectorND vector)
    {
        if (vector.dimensions != this.dimensions) {return null;}
        double[] newValues = new double[dimensions];
        for (int i = 0; i < dimensions; i++)
        {
            newValues[i] = values[i] + vector.getValue(i);
        }
        return new VectorND(newValues);
    }

    public VectorND subtract(VectorND vector)
    {
        if (vector.dimensions != this.dimensions) {return null;}
        double[] newValues = new double[dimensions];
        for (int i = 0; i < dimensions; i++)
        {
            newValues[i] = values[i] - vector.getValue(i);
        }
        return new VectorND(newValues);
    }

    public VectorND multiply(double factor)
    {
        double[] newValues = new double[dimensions];
        for (int i = 0; i < dimensions; i++)
        {
            newValues[i] = values[i] * factor;
        }
        return new VectorND(newValues);
    }
    public VectorND divide(double factor)
    {
        double[] newValues = new double[dimensions];
        for (int i = 0; i < dimensions; i++)
        {
            newValues[i] = values[i] / factor;
        }
        return new VectorND(newValues);
    }

    public Matrix toMatrix()
    {
        Matrix m = new Matrix(this.dimensions, 1);
        for (int i = 0; i < dimensions; i++)
        {
            m.setAt(i, 0, this.getValue(i));
        }
        return m;
    }

    public double magnitude()
    {
        double squaredSum = 0;
        for (double value: values)
        {
            squaredSum += Math.pow(value, 2);
        }
        return Math.sqrt(squaredSum);
    }

    public VectorND normalise()
    {
        double magnitude = this.magnitude();
        ArrayList<Double> valuesList = new ArrayList<>();
        for (double value: this.values)
        {
            valuesList.add(value / magnitude);
        }
        return new VectorND(valuesList);
    }

    public double dot(VectorND vector)
    {
        if (vector.dimensions != this.dimensions) {return Double.NaN;}
        double dotProduct = 0;
        for (int i = 0; i < dimensions; i++)
        {
            dotProduct += values[i] + vector.getValue(i);
        }
        return dotProduct;
    }

    public double innerProduct(VectorND vector)
    {
        if (vector.dimensions != this.dimensions) {return Double.NaN;}
        double dotProduct = 0;
        for (int i = 0; i < dimensions; i++)
        {
            dotProduct += values[i] + vector.getValue(i);
        }
        return dotProduct;
    }

    public Vector3D toVector3D()
    {
        if (this.dimensions == 3)
        {
            return new Vector3D(this.getValue(0), this.getValue(1), this.getValue(2));
        }
        return null;
    }

    public Vector2D toVector2D()
    {
        if (this.dimensions == 2)
        {
            return new Vector2D(this.getValue(0), this.getValue(1));
        }
        return null;
    }

    public Quaternion toQuaternion()
    {
        if (this.dimensions == 4)
        {
            return new Quaternion(this.getValue(0), this.getValue(1), this.getValue(2), this.getValue(3));
        }
        return null;
    }

    public VectorND cross(VectorND[] vectors)
    {
        ArrayList<VectorND> vectorsList = new ArrayList<>(Arrays.asList(vectors));
        vectorsList.addFirst(this);
        if (vectorsList.size() != this.dimensions-1) {return null;}
        Matrix m = new Matrix(dimensions, dimensions);
        for (int i = 0; i < vectorsList.size(); i++)
        {
            for (int j = 0; j < dimensions; j++)
            {
                VectorND vector = vectorsList.get(i);
                m.setAt(i+1, j, vector.getValue(j));
            }
        }
        VectorND newVector = new VectorND(dimensions);
        for (int k = 0; k < dimensions; k++)
        {
            newVector.setValue(k, Matrix.submatrix(m, 0, k).determinate());
        }
        return newVector;
    }

    @Override
    public String toString() {
        StringBuilder vectorString = new StringBuilder();
        for (int i = 0; i < dimensions; i++)
        {

            vectorString.append(this.getValue(i));
            if (i != dimensions-1) {vectorString.append(", ");}
        }
        return  vectorString.toString();
    }

    public static void main(String[] args) {
    }
}
