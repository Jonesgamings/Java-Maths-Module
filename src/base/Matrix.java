package base;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Matrix
{
    int rows;
    int columns;
    private double[][] matrix;

    public Matrix(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new double[rows][columns];
    }

    public double[] getRow(int index)
    {
        return this.matrix[index];
    }

    public double[] getColumn(int index)
    {
        return this.transpose().getRow(index);
    }

    public void setRow(int index, double[] row)
    {
        this.matrix[index] = row;
    }

    public void setColumn(int index, double[] column)
    {
        Matrix transpose = this.transpose();
        transpose.setRow(index, column);
        this.matrix = transpose.matrix;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public Matrix multiply(double factor)
    {
        Matrix newMatrix = new Matrix(rows, columns);
        for (int row = 0; row < newMatrix.rows; row++) {
            for (int column = 0; column < newMatrix.columns; column++) {
                newMatrix.setAt(row, column, this.getAt(row, column) * factor);
            }
        }
        return newMatrix;
    }

    public Matrix divide(double factor)
    {
        Matrix newMatrix = new Matrix(rows, columns);
        for (int row = 0; row < newMatrix.rows; row++) {
            for (int column = 0; column < newMatrix.columns; column++) {
                newMatrix.setAt(row, column, this.getAt(row, column) / factor);
            }
        }
        return newMatrix;
    }

    public Matrix add(Matrix matrix)
    {
        if (this.rows != matrix.rows || this.columns != matrix.columns) {return null;}
        Matrix newMatrix = new Matrix(rows, columns);
        for (int row = 0; row < newMatrix.rows; row++) {
            for (int column = 0; column < newMatrix.columns; column++) {
                newMatrix.setAt(row, column, this.getAt(row, column) + matrix.getAt(row, column));
            }
        }
        return newMatrix;
    }

    public Matrix subtract(Matrix matrix)
    {
        if (this.rows != matrix.rows || this.columns != matrix.columns) {return null;}
        Matrix newMatrix = new Matrix(rows, columns);
        for (int row = 0; row < newMatrix.rows; row++) {
            for (int column = 0; column < newMatrix.columns; column++) {
                newMatrix.setAt(row, column, this.getAt(row, column) - matrix.getAt(row, column));
            }
        }
        return newMatrix;
    }

    public Matrix multiply(Matrix matrix)
    {
        if (this.columns != matrix.rows) {return null;}
        Matrix newMatrix = new Matrix(rows, matrix.columns);
        for (int row = 0; row < newMatrix.rows; row++)
        {
            for (int column = 0; column < newMatrix.columns; column++)
            {
                double newValue = 0f;
                for (int i = 0; i < matrix.rows; i++)
                {
                    newValue += this.getAt(row, i) * matrix.getAt(i, column);
                }
                newMatrix.setAt(row, column, newValue);
            }
        }
        return newMatrix;
    }

    public Matrix setMatrix(double[][] matrix)
    {
        this.matrix = matrix;
        return this;
    }

    public double getAt(int row, int column)
    {
        return this.matrix[row][column];
    }

    public void setAt(int row, int column, double value)
    {
        this.matrix[row][column] = value;
    }


    public static double determinate2x2(Matrix m)
    {
        return (m.getAt(0, 0) * (m.getAt(1, 1)) - (m.getAt(0, 1) * m.getAt(1, 0)));
    }

    public static Matrix identity(int size)
    {
        Matrix identityMatrix = new Matrix(size, size);
        for (int row = 0; row < identityMatrix.rows; row++) {
            for (int column = 0; column < identityMatrix.columns; column++) {
                if (row != column) {continue;}
                identityMatrix.setAt(row, column, 1f);
            }
        }
        return identityMatrix;
    }

    public static Matrix zeroesMatrix(int size)
    {
        Matrix zeroesMatrix = new Matrix(size, size);
        for (int row = 0; row < zeroesMatrix.rows; row++) {
            for (int column = 0; column < zeroesMatrix.columns; column++) {
                zeroesMatrix.setAt(row, column, 0f);
            }
        }
        return zeroesMatrix;
    }

    public Matrix random_values(double min, double max)
    {
        Matrix newMatrix = new Matrix(rows, columns);
        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                Random r = new Random();
                double random = Math.round(min + (max - min) * r.nextDouble());
                newMatrix.setAt(row, column, random);
            }
        }
        return newMatrix;
    }

    public Matrix hadamardProduct(Matrix m)
    {
        if (m.rows != rows || m.columns != columns) {return null;}
        Matrix newMatrix = new Matrix(m.rows, m.columns);
        for (int i = 0; i < m.rows; i++)
        {
            for (int j = 0; j < m.columns; j++)
            {
                newMatrix.setAt(i, j, this.getAt(i, j) * m.getAt(i, j));
            }
        }
        return newMatrix;
    }

    public Matrix tensorProduct(Matrix m)
    {
        Matrix newMatrix = new Matrix(m.rows * this.rows, this.columns * m.columns);
        for (int i = 0; i < newMatrix.rows; i++)
        {
            for (int j = 0; j < newMatrix.columns; j++)
            {
                double newValue = this.getAt(i / m.rows, j / m.columns) * m.getAt(i % m.rows,j % m.columns);
                newMatrix.setAt(i, j, newValue);
            }
        }
        return newMatrix;
    }

    public VectorND reshape()
    {
        VectorND newV = new VectorND(this.rows * this.columns);
        int index = 0;
        for (double[] values: this.matrix)
        {
            for (double value: values)
            {
                newV.setValue(index, value);
                index++;
            }
        }
        return newV;
    }

    public VectorND toVector()
    {
        if (this.rows == 1)
        {
            return new VectorND(this.matrix[0]);
        }
        else if (columns == 1) {
            double[] values = new double[this.rows];
            for (int i = 0; i < this.rows; i++)
            {
                values[i] = this.getAt(i, 0);
            }
            return new VectorND(values);
        }
        return null;
    }

    public Quaternion toQuaternion()
    {
        if (this.columns != this.rows) {return null;}
        if (this.columns != 3) {return null;}
        double trace = this.trace();
        if (trace > 0)
        {
            double k = 0.f / Math.sqrt(1 + trace);
            return new Quaternion(k * (getAt(1, 2) - getAt(2, 1)), k * (getAt(2, 0) - getAt(0, 2)), k * getAt(0, 1) - getAt(1, 0), 0.25 / k);
        }
        else if (getAt(0,0) > getAt(1, 1) && getAt(0 , 0) > getAt(2, 2))
        {
            double k = 0.5 / Math.sqrt(1 + getAt(0, 0) - getAt(1,1) - getAt(2,2));
            return new Quaternion(0.25 / k, k * (getAt(1,0) + getAt(0, 1)), k * (getAt(2, 0) + getAt(0, 2)), k * (getAt(1, 2) - getAt(2, 1)));
        }
        else if (getAt(1, 1) > getAt(2, 2))
        {
            double k = 0.5 / Math.sqrt(1 + getAt(1, 1) - getAt(0, 0) - getAt(2, 2));
            return new Quaternion(k * (getAt(1, 0) + getAt(0, 1)), 0.25 / k, k * (getAt(2, 1) + getAt(1, 2)), k * (getAt(2, 0) - getAt(0, 2)));
        }
        else
        {
           double k = 0.5 / Math.sqrt(1 + getAt(2, 2) - getAt(0,0) - getAt(1, 1));
           return new Quaternion(k * (getAt(2, 0) + getAt(0, 2)), k * (getAt(2, 1) + getAt(1, 2)), 0.25 / k, k * (getAt(0, 1) - getAt(1, 0)));
        }
    }

    public static Matrix rotationX(double angle)
    {
        return new Matrix(3, 3).setMatrix(new double[][] {
                {1, 0, 0},
                {0, Math.cos(angle), -Math.sin(angle)},
                {0, Math.sin(angle), Math.cos(angle)}
        });
    }

    public static Matrix rotationY(double angle)
    {
        return new Matrix(3, 3).setMatrix(new double[][] {
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)}
        });
    }

    public static Matrix rotationZ(double angle)
    {
        return new Matrix(3, 3).setMatrix(new double[][] {
                {Math.cos(angle), -Math.sin(angle), 0},
                {Math.sin(angle), Math.cos(angle),0},
                {0, 0, 1}
        });
    }

    public static Matrix rotation2D(double angle)
    {
        return new Matrix(2, 2).setMatrix(new double[][] {{Math.cos(angle), -Math.sin(angle)}, {Math.sin(angle), Math.cos(angle)}});
    }

    public Matrix adjugate()
    {
        Matrix adjugate = new Matrix(rows, columns);
        if (adjugate.columns == 2) {
            return new Matrix(2, 2).setMatrix(new double[][] {{matrix[1][1], -matrix[0][1]}, {-matrix[1][0], matrix[0][0]}}).divide(this.determinate());
        }
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                double subDeterminate = Matrix.submatrix(this, row, column).determinate();
                adjugate.setAt(row, column, subDeterminate * Math.pow(-1, (row + column)%2));
            }
        }
        return adjugate.transpose();
    }

    public double determinate()
    {
        if (columns != rows){ return Double.NaN;}
        if (columns == 2) {return Matrix.determinate2x2(this);}
        double determinate = 0;
        int sign = -1;
        for (int index = 0; index < columns; index ++) {
            double value = this.matrix[0][index];
            Matrix newMatrix = Matrix.submatrix(this, 0, index);
            determinate += newMatrix.determinate() * value * sign * -1;
            sign *= -1;
        }
        return determinate;
    }

    public Matrix transpose()
    {
        Matrix transpose = new Matrix(columns, rows);
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                transpose.setAt(column, row, this.getAt(row, column));
            }
        }
        return transpose;
    }

    public Matrix inverse()
    {
        double determinate = determinate();
        Matrix inverse = new Matrix(rows, columns);
        if (Double.isNaN(determinate)) {return null;}
        if (inverse.columns != inverse.rows) {return null;}
        if (inverse.columns == 2) {
            return new Matrix(2, 2).setMatrix(new double[][] {{matrix[1][1], -matrix[0][1]}, {-matrix[1][0], matrix[0][0]}}).divide(this.determinate());
        }
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                double subDeterminate = Matrix.submatrix(this, row, column).determinate();
                inverse.setAt(row, column, 1/determinate * subDeterminate * Math.pow(-1, (row + column)%2));
            }
        }
        return inverse.transpose();
    }

    public double trace()
    {
        if (this.columns != this.rows) {return Double.NaN;}
        double sum = 0;
        for (int i = 0; i < this.rows; i++)
        {
            sum += this.getAt(i, i);
        }
        return sum;
    }

    public static Matrix submatrix(Matrix matrix, int ExRow, int ExColumn)
    {
        Matrix newMatrix = new Matrix(matrix.rows-1, matrix.columns-1);
        for (int row = 0; row < matrix.rows; row++)
        {
            for (int column = 0; column < matrix.columns; column++)
            {
                if (row != ExRow && column != ExColumn)
                {
                    int newRow = row;
                    int newColumn = column;
                    if (row > ExRow){ newRow -= 1;}
                    if (column >ExColumn) {newColumn -= 1;}
                    newMatrix.setAt(newRow, newColumn, matrix.getAt(row, column));
                }
            }
        }
        return newMatrix;
    }

    public ComplexNumber[] eigenValues()
    {
        if (columns != rows) {return null;}
        if (columns == 2)
        {
            return new Polynomial(new double[] {1, -this.trace(), this.determinate()}).roots();
        }
        return null;
    }

    public Vector2D[] eigenVectors()
    {
        if (columns != rows) {return null;}
        if (columns == 2) {
            ComplexNumber[] eigenvalues = this.eigenValues();
            ComplexNumber matrixX1 = new ComplexNumber(this.getAt(0, 0), 0).subtract(eigenvalues[0]);
            ComplexNumber matrixY = new ComplexNumber(this.getAt(0, 1), 0);
            ComplexNumber matrixX2 = new ComplexNumber(this.getAt(0, 0), 0).subtract(eigenvalues[1]);
            return new Vector2D[] { new Vector2D(1, -matrixX1.divide(matrixY).real), new Vector2D(1, -matrixX2.divide(matrixY).real)};
        }
        return null;
    }

    public Matrix power(int pow)
    {
        Matrix newMatrix = this;
        for (int i =0; i<(pow-1); i++)
        {
            newMatrix = newMatrix.multiply(this);
        }
        return newMatrix;
    }

    @Override
    public String toString() {
        StringBuilder matrixString = new StringBuilder("{{");
        for (int row=0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                matrixString.append(Double.toString(this.matrix[row][column]));
                if (column != columns-1) {matrixString.append(", ");}
            }
            matrixString.append(("},"));
            if (row != rows-1) {matrixString.append("\n");}
        }
        int length = matrixString.length();
        matrixString.deleteCharAt(length-1);
        matrixString.append("}");
        return  matrixString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix1 = (Matrix) o;
        return rows == matrix1.rows && columns == matrix1.columns && Arrays.deepEquals(matrix, matrix1.matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    public ComplexMatrix toComplex()
    {
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                newComplexMatrix.setAt(row, column, this.getAt(row, column));
            }
        }
        return newComplexMatrix;
    }

    public Matrix copy()
    {
        Matrix m = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                m.setAt(i, j, this.getAt(i, j));
            }
        }
        return m;
    }

    private Matrix multiplyRow(int index, double val)
    {
        Matrix m = this.copy();
        double[] currentRow = this.getRow(index);
        double[] newRow = new double[currentRow.length];
        for (int i = 0; i < currentRow.length; i++) {
            newRow[i] = currentRow[i] * val;
        }
        m.setRow(index, newRow);
        return m;
    }

    private Matrix addRows(int index1, int index2, double scalar)
    {
        Matrix m = this.copy();
        double[] addTo = this.getRow(index1);
        double[] toAdd = this.getRow(index2);
        for (int i = 0; i < addTo.length; i++) {
            addTo[i] += toAdd[i] * scalar;
        }
        m.setRow(index1, addTo);
        return m;
    }

    public Matrix gaussianElimination()
    {
        Matrix m = this.copy();
        for (int i = 0; i < m.rows; i++) {
            for (int j = i+1; j < m.rows; j++) {
                m = m.addRows(j, i, -m.getAt(j, i)/m.getAt(i, i));
            }
        }
        return m;
    }

    public int rank() {
        Matrix reduced = this.gaussianElimination();
        int rank = 0;

        for (int i = 0; i < reduced.rows; i++) {
            for (int j = 0; j < reduced.columns; j++) {
                if (Math.abs(reduced.getAt(i, j)) > 1e-10) { // Tolerance for numerical errors
                    rank++;
                    break;
                }
            }
        }
        return rank;
    }

    public Matrix eliminate()
    {
        Matrix m = this.copy();
        for (int i = 0; i < m.rows; i++) {
            m = m.multiplyRow(i, 1/m.getAt(i, i));
            for (int j = 0; j < m.rows; j++) {
                if (j != i) {
                    m = m.addRows(j, i, -m.getAt(j, i));
                }
            }
        }
        return m;
    }

    public static VectorND simultaneousEquation(Matrix right, VectorND equals)
    {
        if (right.rows != equals.dimensions)
        {
            return null;
        }
        if (right.determinate() != 0)
        {
            return right.inverse().multiply(equals.toMatrix()).toVector();
        }
        else
        {
            Matrix rightEliminated = right.gaussianElimination();
            VectorND answer = new VectorND(equals.dimensions);
            for (int i = answer.dimensions-1; i > -1; i++) {
                double sum = 0;
                for (int j = answer.dimensions; j > i; j++) {
                    sum -= rightEliminated.getAt(i, j) * answer.getValue(j);
                }
                answer.setValue(i, (equals.getValue(i) - sum)/rightEliminated.getAt(i, i));
                System.out.println(answer);
            }
            return answer;
        }
    }

    static double[] combineArrays(double[] first, double[] second)
    {
        double[] newDouble = new double[first.length + second.length];
        for (int i = 0; i < first.length; i++) {
            newDouble[i] = first[i];
        }
        for (int j = 0; j < second.length; j++) {
            newDouble[j + first.length] = second[j];
        }
        return newDouble;
    }

    public Matrix combine(Matrix other)
    {
        Matrix newMatrix = new Matrix(this.rows, this.columns + other.columns);
        for (int i = 0; i < this.rows; i++) {
            double[] newRow = Matrix.combineArrays(this.getRow(i), other.getRow(i));
            newMatrix.setRow(i, newRow);
        }
        return newMatrix;
    }

    public Matrix splitAfter(int index)
    {
        Matrix newMatrix = new Matrix(this.rows, this.columns - (index+1));
        for (int i = 0; i < newMatrix.rows; i++) {
            for (int j = 0; j < newMatrix.columns; j++) {
                newMatrix.setAt(i, j, this.getAt(i, j + index + 1));
            }
        }
        return newMatrix;
    }

    public Matrix splitBefore(int index)
    {
        Matrix newMatrix = new Matrix(this.rows, index);
        for (int i = 0; i < newMatrix.rows; i++) {
            for (int j = 0; j < newMatrix.columns; j++) {
                newMatrix.setAt(i, j, this.getAt(i, j));
            }
        }
        return newMatrix;
    }

    public double determinateREF()
    {
        double total = 1;
        for (int i = 0; i < Math.min(this.rows, this.columns); i++) {
            total *= this.getAt(i, i);
        }
        return total;
    }

    public static Matrix reshape(double[] array, int rows)
    {
        if (array.length % rows != 0) {return null;}
        int columns = array.length / rows;
        Matrix matrix = new Matrix(rows, columns);
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            matrix.setAt(j, i % rows, array[i]);
            if (i % rows == rows-1) {j++;}
        }
        return matrix;
    }

    public static void main(String[] args){
        Matrix m = new Matrix(3, 3).setMatrix(new double[][] {{2, 1, -1}, {-3, -1, 2}, {-2, 1, 2}});
        System.out.println(m.gaussianElimination());
        System.out.println(m.gaussianElimination().determinateREF());
        System.out.println(m.determinate());
    }
}
