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
        if (this.rows != matrix.columns) {return null;}
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
        return m.getAt(0, 0) * m.getAt(1, 1) - m.getAt(0, 1) * m.getAt(1, 0);
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

    public double determinate()
    {
        if (columns != rows){ return 0f;}
        if (columns == 2) {return Matrix.determinate2x2(this);}
        double determinate = 0f;
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

    private static Matrix submatrix(Matrix matrix, int ExRow, int ExColumn)
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
}
