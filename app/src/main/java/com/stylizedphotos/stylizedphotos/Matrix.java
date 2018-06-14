package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;


public class Matrix {

    private int rows;
    private int cols;
    //private float[] array1D;

    public float[][] getMatrix() {
        return matrix;
    }

    public float getVal(int i, int j) {
        return matrix[i][j];
    }

    public void setMatrix(float[][] arr)
    {
        int k=0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++,k++)
            {
                setVal(i, j, arr[i][j]);
                //array1D[k] = arr[i][j];
            }
        }
    }

    public void setVal(int i, int j, float val) {
        matrix[i][j] = val;
    }

    private float[][] matrix;

    public int getRows() {
        return rows;
    }
//    public float[] getArray1D() {
//        return array1D;
//    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    Matrix(int rows, int cols, float[][] arr) {
        matrix = new float[rows][cols];
        this.rows = rows;
        this.cols = cols;
        setMatrix(arr);
    }

    void MatrixByScalar(int k) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.setVal(i, j, this.getVal(i, j) * k);
            }
        }
    }
    //TODO add operations

    private static int[] find_center(Matrix mat) {
        int[] result= new int[2];
        result[1] = (mat.getCols() - 1) / 2;
        result[0] = (mat.getRows() - 1) / 2; //2,3
        return result;
    }


    public static Bitmap convolution(Matrix kernel, Bitmap image,boolean toSum )
    {
        int i, j, ii, jj, m, n, mm, nn,sum=0, width=image.getWidth(), height=image.getHeight(),ker_cols=kernel.getCols(),ker_rows=kernel.getRows();
        int[] center = new int [2];
        float [][] ker_vals = new float[ker_rows][ker_cols];
        int new_color;
        int temp_red = 0, temp_green = 0, temp_blue = 0,max=0;
        double fred=0,fgreen=0,fblue=0;
        int[] intArray = new int[image.getWidth()*image.getHeight()];// 1d array of ints to get image
        int[] outArray = new int[image.getWidth()*image.getHeight()];// 1d array of ints to get
        // image
        double[] floatArray = new double[image.getWidth()*image.getHeight()];// 1d array of ints to get image
        image.getPixels(intArray, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight()); // pixels to int array
        for(i=0;i<intArray.length;i++)
        {
            floatArray[i] = (float)intArray[i]/255;
        }
        for(i=0;i<ker_rows;i++)
            for(j=0;j<ker_cols;j++)
                ker_vals[i][j]=kernel.getVal(i,j);

        center[0]= find_center(kernel)[0];
        center[1]= find_center(kernel)[1];

        for (i = 0; i < height; ++i)              // rows
        {
            for (j = 0; j < width; ++j)          // columns
            {
                for (m = 0; m < ker_rows; ++m)     // kernel rows
                {
                    mm = ker_rows - 1 - m;      // row index of flipped kernel
                    ii = i + m - center[0];//here to save some actions
                    for (n = 0; n < ker_cols; ++n) // kernel columns
                    {
                        nn = ker_cols - 1 - n;  // column index of flipped kernel
                        // index of input signal, used for checking boundary
                        jj = j + n - center[1];
                        // ignore input samples which are out of bound
                        if (ii >= 0 && ii < height && jj >= 0 && jj <  width) {
                            //get all pixel value and calculate with them

                            temp_red += ((intArray[ii*width+jj] >> 16) & 0xff) * ker_vals[mm][nn];
                            if(temp_red>max)
                                max=temp_red;
                            temp_green += ((intArray[ii*width+jj] >> 8) & 0xff) * ker_vals[mm][nn];
                            if(temp_green>max)
                                max=temp_green;
                            temp_blue += (intArray[ii*width+jj] & 0xff) * ker_vals[mm][nn];
                            if(temp_blue>max)
                                max=temp_blue;
                            if(toSum==true)
                                sum+=ker_vals[m][n];
                        }
                    }
                }
                //set the new value
                //  if(sum==0)
                // sum=1;
                if(toSum == true) {
                    temp_red = temp_red / sum;//to prevent sliding
                    temp_green = temp_green / sum;//to prevent sliding
                    temp_blue = temp_blue / sum;//to prevent sliding
                }
                temp_red = HelpMethods.clamp((int)(temp_red + 0.5));
                temp_green = HelpMethods.clamp((int)(temp_green + 0.5));
                temp_blue = HelpMethods.clamp((int)(temp_blue + 0.5));
                new_color = ((255 & 0xff) <<24 )| ((temp_red & 0xff) << 16) | ((temp_green & 0xff) << 8) | (temp_blue & 0xff);
                outArray[i*width+j]=new_color;
                sum=0;
                temp_red = 0;
                temp_green = 0;
                temp_blue = 0;
            }
        }

        Bitmap out2 = Bitmap.createBitmap(outArray, width, height, Bitmap.Config.ARGB_8888);

        return out2;
    }
}


