package base;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class ComplexMatrix
{
    int rows;
    int columns;
    private ComplexNumber[][] matrix;

    public ComplexMatrix(int rows, int columns)
    {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new ComplexNumber[rows][columns];
        this.setZero();
    }

    public ComplexMatrix(ComplexNumber[][] inner)
    {
        this.matrix = inner;
        this.rows = inner.length;
        this.columns = inner[0].length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexMatrix that = (ComplexMatrix) o;
        return rows == that.rows && columns == that.columns && Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    public ComplexMatrix hadamardProduct(Matrix m)
    {
        if (m.rows != rows || m.columns != columns) {return null;}
        ComplexMatrix newMatrix = new ComplexMatrix(m.rows, m.columns);
        for (int i = 0; i < m.rows; i++)
        {
            for (int j = 0; j < m.columns; j++)
            {
                newMatrix.setAt(i, j, this.getAt(i, j).multiply(m.getAt(i, j)));
            }
        }
        return newMatrix;
    }

    public ComplexMatrix tensorProduct(Matrix m)
    {
        ComplexMatrix newMatrix = new ComplexMatrix(m.rows * this.rows, this.columns * m.columns);
        for (int i = 0; i < newMatrix.rows; i++)
        {
            for (int j = 0; j < newMatrix.columns; j++)
            {
                ComplexNumber newValue = this.getAt(i / m.rows, j / m.columns).multiply(m.getAt(i % m.rows,j % m.columns));
                newMatrix.setAt(i, j, newValue);
            }
        }
        return newMatrix;
    }

    public static ComplexMatrix zeroesMatrix(int size)
    {
        ComplexMatrix zeroesMatrix = new ComplexMatrix(size, size);
        for (int row = 0; row < zeroesMatrix.rows; row++) {
            for (int column = 0; column < zeroesMatrix.columns; column++) {
                zeroesMatrix.setAt(row, column, 0f);
            }
        }
        return zeroesMatrix;
    }

    public ComplexNumber[][] getMatrix() {
        return matrix;
    }

    public ComplexMatrix setZero()
    {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                this.setAt(row, column, 0);
            }
        }
        return this;
    }

    public ComplexMatrix multiply(ComplexNumber factor)
    {
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < newComplexMatrix.rows; row++) {
            for (int column = 0; column < newComplexMatrix.columns; column++) {
                newComplexMatrix.setAt(row, column, this.getAt(row, column).multiply( factor));
            }
        }
        return newComplexMatrix;
    }

    public ComplexMatrix divide(ComplexNumber factor)
    {
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < newComplexMatrix.rows; row++) {
            for (int column = 0; column < newComplexMatrix.columns; column++) {
                newComplexMatrix.setAt(row, column, this.getAt(row, column).divide(factor));
            }
        }
        return newComplexMatrix;
    }

    public ComplexMatrix multiply(double factor)
    {
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < newComplexMatrix.rows; row++) {
            for (int column = 0; column < newComplexMatrix.columns; column++) {
                newComplexMatrix.setAt(row, column, this.getAt(row, column).multiply( factor));
            }
        }
        return newComplexMatrix;
    }

    public ComplexMatrix divide(double factor)
    {
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < newComplexMatrix.rows; row++) {
            for (int column = 0; column < newComplexMatrix.columns; column++) {
                newComplexMatrix.setAt(row, column, this.getAt(row, column).divide(factor));
            }
        }
        return newComplexMatrix;
    }

    public ComplexMatrix add(ComplexMatrix complexMatrix)
    {
        if (this.rows != complexMatrix.rows || this.columns != complexMatrix.columns) {return null;}
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < newComplexMatrix.rows; row++) {
            for (int column = 0; column < newComplexMatrix.columns; column++) {
                newComplexMatrix.setAt(row, column, this.getAt(row, column).add(complexMatrix.getAt(row, column)));
            }
        }
        return newComplexMatrix;
    }

    public ComplexMatrix subtract(ComplexMatrix complexMatrix)
    {
        if (this.rows != complexMatrix.rows || this.columns != complexMatrix.columns) {return null;}
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < newComplexMatrix.rows; row++) {
            for (int column = 0; column < newComplexMatrix.columns; column++) {
                newComplexMatrix.setAt(row, column, this.getAt(row, column).subtract(complexMatrix.getAt(row, column)));
            }
        }
        return newComplexMatrix;
    }

    public ComplexMatrix multiply(ComplexMatrix complexMatrix)
    {
        if (this.columns != complexMatrix.rows) {return null;}
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, complexMatrix.columns);
        for (int row = 0; row < newComplexMatrix.rows; row++)
        {
            for (int column = 0; column < newComplexMatrix.columns; column++)
            {
                ComplexNumber newValue = new ComplexNumber(0 ,0);
                for (int i = 0; i < complexMatrix.rows; i++)
                {
                    newValue = newValue.add(this.getAt(row, i).multiply(complexMatrix.getAt(i, column)));
                }
                newComplexMatrix.setAt(row, column, newValue);
            }
        }
        return newComplexMatrix;
    }

    public ComplexMatrix setMatrix(ComplexNumber[][] matrix)
    {
        this.matrix = matrix;
        return this;
    }

    public ComplexMatrix setMatrix(double[][] matrix)
    {
        ComplexNumber[][] complexMatrix = new ComplexNumber[matrix.length][matrix[0].length];
        for (int i = 0; i<matrix.length; i++)
        {
            for (int j = 0; j<matrix[0].length; j++)
            {
                complexMatrix[i][j] = new ComplexNumber(matrix[i][j], 0);
            }
        }
        this.matrix = complexMatrix;
        return this;
    }

    public ComplexNumber getAt(int row, int column)
    {
        return this.matrix[row][column];
    }

    public void setAt(int row, int column, ComplexNumber value)
    {
        this.matrix[row][column] = value;
    }

    public void setAt(int row, int column, double value)
    {
        this.matrix[row][column] = new ComplexNumber(value, 0);
    }

    public static ComplexNumber determinate2x2(ComplexMatrix m)
    {
        return ((m.getAt(0, 0).multiply (m.getAt(1, 1))).subtract (m.getAt(0, 1).multiply( m.getAt(1, 0))));
    }

    public static ComplexMatrix identity(int size)
    {
        ComplexMatrix identityComplexMatrix = new ComplexMatrix(size, size);
        for (int row = 0; row < size; row++) {
            identityComplexMatrix.setAt(row, row, 1f);
        }
        return identityComplexMatrix;
    }

    public ComplexMatrix random_values(double min, double max)
    {
        ComplexMatrix newComplexMatrix = new ComplexMatrix(rows, columns);
        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                Random r = new Random();
                double randomR = Math.round(min + (max - min) * r.nextDouble());
                double randomI = Math.round(min + (max - min) * r.nextDouble());
                newComplexMatrix.setAt(row, column, new ComplexNumber(randomR, randomI));
            }
        }
        return newComplexMatrix;
    }
    public ComplexMatrix adjugate()
    {
        ComplexMatrix adjugate = new ComplexMatrix(rows, columns);
        if (adjugate.columns == 2) {
            return new ComplexMatrix(2, 2).setMatrix(new ComplexNumber[][] {{matrix[1][1], matrix[0][1].multiply(-1)}, {matrix[1][0].multiply(-1), matrix[0][0]}}).divide(this.determinate());
        }
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                ComplexNumber subDeterminate = ComplexMatrix.subMatrix(this, row, column).determinate();
                adjugate.setAt(row, column, subDeterminate.multiply(Math.pow(-1, (row + column)%2)));
            }
        }
        return adjugate.transpose();
    }
    public ComplexNumber determinate()
    {
        if (columns != rows){ return ComplexNumber.NaN;}
        if (columns == 2) {return ComplexMatrix.determinate2x2(this);}
        ComplexNumber determinate = new ComplexNumber(0 ,0 );
        int sign = -1;
        for (int index = 0; index < columns; index ++) {
            ComplexNumber value = this.matrix[0][index];
            ComplexMatrix newComplexMatrix = ComplexMatrix.subMatrix(this, 0, index);
            determinate = determinate.add(((newComplexMatrix.determinate()).multiply(value)).multiply(sign * -1));
            sign *= -1;
        }
        return determinate;
    }

    public ComplexMatrix transpose()
    {
        ComplexMatrix transpose = new ComplexMatrix(columns, rows);
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                transpose.setAt(column, row, this.getAt(row, column));
            }
        }
        return transpose;
    }

    public ComplexMatrix inverse()
    {
        ComplexNumber determinate = determinate();
        ComplexMatrix inverse = new ComplexMatrix(rows, columns);
        if (ComplexNumber.isNaN(determinate)) {return null;}
        if (inverse.columns != inverse.rows) {return null;}
        if (inverse.columns == 2) {
            return new ComplexMatrix(2, 2).setMatrix(new ComplexNumber[][] {{matrix[1][1], matrix[0][1].multiply(-1)}, {matrix[1][0].multiply(-1), matrix[0][0]}}).divide(this.determinate());
        }
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                ComplexNumber subDeterminate = ComplexMatrix.subMatrix(this, row, column).determinate();
                inverse.setAt(row, column, ((new ComplexNumber(1, 0).divide(determinate)).multiply(subDeterminate)).multiply(Math.pow(-1, (row + column)%2)));
            }
        }
        return inverse.transpose();
    }

    public ComplexNumber trace()
    {
        if (this.columns != this.rows) {return ComplexNumber.NaN;}
        ComplexNumber sum = new ComplexNumber(0 ,0);
        for (int i = 0; i < this.rows; i++)
        {
            sum = sum.add(this.getAt(i, i));
        }
        return sum;
    }

    public static ComplexMatrix subMatrix(ComplexMatrix complexMatrix, int ExRow, int ExColumn)
    {
        ComplexMatrix newComplexMatrix = new ComplexMatrix(complexMatrix.rows-1, complexMatrix.columns-1);
        for (int row = 0; row < complexMatrix.rows; row++)
        {
            for (int column = 0; column < complexMatrix.columns; column++)
            {
                if (row != ExRow && column != ExColumn)
                {
                    int newRow = row;
                    int newColumn = column;
                    if (row > ExRow){ newRow -= 1;}
                    if (column >ExColumn) {newColumn -= 1;}
                    newComplexMatrix.setAt(newRow, newColumn, complexMatrix.getAt(row, column));
                }
            }
        }
        return newComplexMatrix;
    }

    public double eigenvalues()
    {
        if (columns != rows) {return 0;}
        return 0;
    }

    public ComplexMatrix power(int pow)
    {
        if (rows != columns) {return null;}
        if (pow == 0) {return ComplexMatrix.identity(this.rows);}
        ComplexMatrix newComplexMatrix = this;
        for (int i =0; i<(pow-1); i++)
        {
            newComplexMatrix = newComplexMatrix.multiply(this);
        }
        return newComplexMatrix;
    }

    @Override
    public String toString() {
        StringBuilder matrixString = new StringBuilder("{{");
        for (int row=0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                matrixString.append(this.matrix[row][column]);
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
    public static void main(String[] args){
        ComplexMatrix m = new ComplexMatrix(5, 5).random_values(-10, 10);
        System.out.println(m);
        System.out.println(m.determinate());
    }
}
