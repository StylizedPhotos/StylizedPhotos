package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import static android.graphics.Color.red;

public class Matrix {

    private int rows;
    private int cols;

    public float[][] getMatrix() {
        return matrix;
    }

    public float getVal(int i, int j) {
        return matrix[i][j];
    }

    public void setMatrix(float[][] arr) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                setVal(i, j, arr[i][j]);
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

    private static int find_center(Matrix mat) {
        int result;
        result = (mat.getCols() - 1) / 2; //one of them not needed thanks to n x n
        return result;
    }
    /*
//just to compile
    private float convolve (Matrix kernel, Bitmap source) //inside convolution operation
    {
        int[] center = new int[2];
        int red=0,green=0,blue=0;
        Color result = new Color();
        center = find_center(kernel);//by ref
        int vertical,horizontal;

        for(int i=0; i < kernel.getRows();i++)
        {
            horizontal=i-center[0];
            if(horizontal >= 0 && horizontal < source.getHeight())
            {
                for (int j = 0; j < kernel.getCols(); j++)
                {
                    vertical = j - center[1];
                    if (vertical >= 0 && vertical < source.getWidth())
                    {
                       red+=  red(source.getPixel(i, j)) * kernel.getVal(i, j);
                       green+=  red(source.getPixel(i, j)) * kernel.getVal(i, j);
                       blue+=  red(source.getPixel(i, j)) * kernel.getVal(i, j);
                    }
                }
            }
        }
        result /= (source.getHeight()*source.getHeight() +source.getCols()*source.getCols() + source.getRows()*source.getCols());
        return result;
    }

    public float convolution (Matrix kernel, Bitmap image) // the convolution itself - can be called from outside of matrix
    {
        int result;
        float[][] temp_arr = new float[kernel.getCols()][kernel.getRows()];
        for(int i=0;i<kernel.getCols();i++)
            for (int j=0;j<kernel.getRows();j++)
            {
                temp_arr[i][j] = image.getPixel()
            }
        Matrix temp_matrix = new Matrix(kernel.getRows(),kernel.getCols(),temp_arr);
        for(int i=0;i<image.getWidth();i++)
            for (int j=0;j<image.getHeight();j++)
            {

                convolve(kernel, )
            }
        return result;
    }
    */


    public static Bitmap convolution(Matrix kernel, Bitmap image)
    {
        int i, j, ii, jj, m, n, mm, nn,sum=0;
        //Bitmap out = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());// check if mutable
        Bitmap out = image.copy(image.getConfig(), true);// mutable copy of the source
        int new_color = 0;
        int temp_red = 0, temp_green = 0, temp_blue = 0, temp_alpha = 0;





        for (i = 0; i < image.getHeight(); ++i)              // rows
        {
            for (j = 0; j < image.getWidth(); ++j)          // columns
            {




                for (m = 0; m < kernel.getRows(); ++m)     // kernel rows
                {
                    mm = kernel.getRows() - 1 - m;      // row index of flipped kernel

                    for (n = 0; n < kernel.getCols(); ++n) // kernel columns
                    {
                        nn = kernel.getCols() - 1 - n;  // column index of flipped kernel

                        // index of input signal, used for checking boundary
                        ii = i + m - find_center(kernel);
                        jj = j + n - find_center(kernel);

                        // ignore input samples which are out of bound
                        if (ii >= 0 && ii < image.getHeight() && jj >= 0 && jj <  image.getWidth()) {
                            //get all pixel value and calculate with them
                           // temp_alpha += (image.getPixel(ii, jj) >> 24 & 0xff) * kernel.getVal(mm, nn);
                            temp_red += (image.getPixel(jj, ii) >> 16 & 0xff) * kernel.getVal(mm, nn);
                            temp_green += (image.getPixel(jj, ii) >> 8 & 0xff) * kernel.getVal(mm, nn);
                            temp_blue += (image.getPixel(jj, ii) & 0xff) * kernel.getVal(mm, nn);
                            sum+=kernel.getVal(m,n);
                        }
                    }

                }
                //set the new value
                new_color = 0;
               // temp_alpha /= kernel.getRows()*kernel.getCols();

                temp_red /= sum;
                temp_green /= sum;
                temp_blue /= sum;
                new_color = new_color/* | (temp_alpha << 24)*/ | (temp_red << 16) | (temp_green << 8) | temp_blue;
                out.setPixel(j, i, new_color);
               // temp_alpha = 0;
                sum=0;
                temp_red = 0;
                temp_green = 0;
                temp_blue = 0;
            }
        }
        return out;
    }



}
