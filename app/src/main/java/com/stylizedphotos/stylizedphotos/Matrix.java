package com.stylizedphotos.stylizedphotos;

public class Matrix {
    public float[][] getMatrix() {
        return matrix;
    }

    public float getVal(int i,int j) {
        return matrix[i][j];
    }

    public void setMatrix(float[][] arr) {
        for (int i=0;i<rows;i++)
        {
            for (int j=0;j<cols;j++)
            {
                setVal(i, j, arr[i][j]);
            }
        }
    }

    public void setVal(int i,int j, float val) {
        matrix[i][j]=val;
    }

    private float [][] matrix;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    private int rows;
    private int cols;

    Matrix(int rows, int cols, float [][] arr)
    {
        matrix = new float[rows][cols];
        setMatrix(arr);
    }

    void MatrixByScalar(int k)
    {
        for (int i=0;i<rows;i++)
        {
            for (int j=0;j<cols;j++)
            {
                this.setVal(i, j, this.getVal(i,j)*k);
            }
        }
    }
    //TODO add operations
}
