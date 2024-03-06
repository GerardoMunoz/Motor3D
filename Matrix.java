public class Matrix {
     final int rows;
     final int columns;
     final double[][] data;


    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    public Matrix(double[][] data) {
        this.rows = data.length;
        this.columns = data[0].length;
        this.data = data;
    }

    public Matrix(double[] data) {
        this.rows = data.length;
        this.columns = 1;
        this.data = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            this.data[i]=new double[]{data[i]};
        }
    }

    public static Matrix add(Matrix matrix1, Matrix matrix2) {
        if (matrix1.rows != matrix2.rows || matrix1.columns != matrix2.columns) {
            throw new IllegalArgumentException("Matrices must have the same dimensions for addition.");
        }
        double[][] resultData = new double[matrix1.rows][matrix1.columns];
        for (int i = 0; i < matrix1.rows; i++) {
            for (int j = 0; j < matrix1.columns; j++) {
                resultData[i][j] = matrix1.data[i][j] + matrix2.data[i][j];
            }
        }
        return new Matrix( resultData);
    }

	public static Matrix sub(Matrix matrix1, Matrix matrix2){
		return add(matrix1,multiply(-1,matrix2));
	}

    public static double dot(Matrix matrix1, Matrix matrix2) {
		return multiply(transpose(matrix1),matrix2).getElement(0,0);
	}

	public double getElement(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IllegalArgumentException("Invalid row or column index.");
        }

        return data[row][column];
    }

    public static Matrix crossProduct(Matrix vector1, Matrix vector2) {
        if (vector1.getRows() != 3 || vector1.getColumns() != 1 ||
            vector2.getRows() != 3 || vector2.getColumns() != 1) {
            throw new IllegalArgumentException("Cross product is defined for 3D vectors only.");
        }
        double resultDataX = vector1.getElement(1, 0) * vector2.getElement(2, 0) - vector1.getElement(2, 0) * vector2.getElement(1, 0);
        double resultDataY = vector1.getElement(2, 0) * vector2.getElement(0, 0) - vector1.getElement(0, 0) * vector2.getElement(2, 0);
        double resultDataZ = vector1.getElement(0, 0) * vector2.getElement(1, 0) - vector1.getElement(1, 0) * vector2.getElement(0, 0);
        return new Matrix(new double[][]{{resultDataX}, {resultDataY}, {resultDataZ}});
    }

    public static Matrix multiply(Matrix matrix1, Matrix matrix2) {
        if (matrix1.columns != matrix2.rows) {
            throw new IllegalArgumentException("Number of columns in the first matrix must be equal to the number of rows in the second matrix for multiplication.");
        }
        double[][] resultData = new double[matrix1.rows][matrix2.columns];
        for (int i = 0; i < matrix1.rows; i++) {
            for (int j = 0; j < matrix2.columns; j++) {
                for (int k = 0; k < matrix1.columns; k++) {
                    resultData[i][j] += matrix1.data[i][k] * matrix2.data[k][j];
                }
            }
        }

        return new Matrix(resultData);
    }

    public static Matrix multiply(double scalar, Matrix matrix) {
        double[][] resultData = new double[matrix.rows][matrix.columns];
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                resultData[i][j] = scalar * matrix.data[i][j];
            }
        }
        return new Matrix(resultData);
    }

    public static Matrix transpose(Matrix matrix) {
        double[][] transposedData = new double[matrix.columns][matrix.rows];

        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                transposedData[j][i] = matrix.data[i][j];
            }
        }

        return new Matrix( transposedData);
    }

    public Matrix getColumn(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= columns) {
            throw new IllegalArgumentException("Invalid column index.");
        }

        double[] column = new double[rows];
        for (int i = 0; i < rows; i++) {
            column[i] = data[i][columnIndex];
        }

        return new Matrix(column);
    }

    public Matrix getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= rows) {
            throw new IllegalArgumentException("Invalid row index.");
        }

        return new Matrix(new double[][] {data[rowIndex].clone()}); 
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

	public double[][] toArrays() {
 
        return data;
    }

}