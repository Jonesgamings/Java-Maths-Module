package base;

public class SimplexAlgorithm
{
    Matrix matrix;

    public SimplexAlgorithm(Matrix matrix)
    {
        this.matrix = matrix;
    }

    public int getPivotColumnIndex()
    {
        double[] objectRow = this.matrix.getRow(0);
        int pivotColumn = 0;
        double pivotColumnValue = Double.MAX_VALUE;
        for (int i = 1; i < objectRow.length-1; i++)
        {
            if (objectRow[i] < pivotColumnValue && objectRow[i] <= 0)
            {
                double[] column = this.matrix.getColumn(i);
                int numbers = 0;
                for (double num: column)
                {
                    if (num != 0) {numbers +=1;}
                }
                if (numbers == 1) {continue;}
                pivotColumn = i;
                pivotColumnValue = objectRow[i];
            }
        }
        return pivotColumn;
    }

    public int getPivotRowIndex(int pivotColumn)
    {
        double[] column = this.matrix.getColumn(pivotColumn);
        double[] RHS = this.matrix.getColumn(this.matrix.columns - 1);
        int pivotRow = 0;
        double pivotRowValue = Double.MAX_VALUE;
        for (int i = 1; i < column.length; i++)
        {
            if (column[i] == 0) {continue;}
            if (RHS[i]/column[i] < pivotRowValue && RHS[i]/column[i] >= 0)
            {
                pivotRowValue = RHS[i]/column[i];
                pivotRow = i;
            }
        }
        return pivotRow;
    }

    public Matrix maximiseIteration()
    {
        int pivotColumnIndex = this.getPivotColumnIndex();
        int pivotRowIndex = this.getPivotRowIndex(pivotColumnIndex);
        double pivotValue = this.matrix.getAt(pivotRowIndex, pivotColumnIndex);
        Matrix newMatrix = new Matrix(this.matrix.rows, this.matrix.columns);
        for (int i = 0; i < this.matrix.rows; i++)
        {
            for (int j = 0; j < this.matrix.columns; j++)
            {
                if (i == pivotRowIndex)
                {
                    newMatrix.setAt(i, j, this.matrix.getAt(i, j) / pivotValue);
                }
                else
                {
                    double scalar = this.matrix.getAt(i, pivotColumnIndex);
                    double onPivot = this.matrix.getAt(pivotRowIndex, j);
                    newMatrix.setAt(i, j, this.matrix.getAt(i, j) - (scalar * onPivot / pivotValue));
                }
            }
        }
        this.matrix = newMatrix;
        return newMatrix;
    }

    public Matrix maximise(int additional)
    {
        Matrix lastMatrix = this.matrix;
        double lastBest = this.matrix.getAt(0, this.matrix.columns -1);
        for (int i = 0; i < this.matrix.columns-2+additional; i++)
        {
            Matrix m = this.maximiseIteration();
            if (m.getAt(0, this.matrix.columns-1) < lastBest){
                this.matrix = lastMatrix;
                return this.matrix;
            }
            lastMatrix = this.matrix;
            lastBest = m.getAt(0, this.matrix.columns-1);
        }
        return this.matrix;
    }

    public static void main(String[] args) {
        SimplexAlgorithm sa = new SimplexAlgorithm(new Matrix(3, 5).setMatrix(new double[][] {
                {1, 1, -3, -1, 0},
                {0, 14, 2, -4, 12},
                {0, -6, 0, 3, 5},
        }));
        System.out.println(sa.maximiseIteration());
        System.out.println(sa.maximiseIteration());
        System.out.println(sa.maximiseIteration());
        System.out.println(sa.maximise(10));
    }
}