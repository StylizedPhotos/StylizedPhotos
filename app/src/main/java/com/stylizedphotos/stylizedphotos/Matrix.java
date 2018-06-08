package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;

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

    private static int[] find_center(Matrix mat) {
        int[] result= new int[2];
        result[1] = (mat.getCols() - 1) / 2;
        result[0] = (mat.getRows() - 1) / 2; //2,3
        return result;
    }



    public static Bitmap convolution(Matrix kernel, Bitmap image)
    {
        int i, j, ii, jj, m, n, mm, nn,sum=0, width=image.getWidth(), height=image.getHeight(),ker_cols=kernel.getCols(),ker_rows=kernel.getRows();
        int[] center = new int [2];
        int[][] arr = new int[image.getHeight()+1][image.getWidth()+1];
        //Bitmap out = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());// check if mutable
        Bitmap out = image.copy(image.getConfig(), true);// mutable copy of the source
        int new_color = 0;
        int temp_red = 0, temp_green = 0, temp_blue = 0, temp_alpha = 0;
//        for (i = 1; i < image.getHeight()+1; ++i)              // rows
//        {
//            for (j = 1; j < image.getWidth()+1; ++j)          // columns
//            {
//                arr[i][j] = image.getPixel(j,i);
//            }
//        }

// trying new format - bmp to png via output stream-------------------------------------------------//
        int[] intArray = new int[out.getWidth()*out.getHeight()];// 1d array of ints to get image
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        out.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
        out.getPixels(intArray, 0, out.getWidth(), 0, 0, out.getWidth(), out.getHeight()); // pixels to int array

//        int [] rowData= new int [out.getWidth()];
//        for (int row = 0; row < out.getHeight(); row ++) {
//            // Load row of pixels
//            out.getPixels(rowData, 0, out.getWidth(), 0, row, out.getWidth(), 1);
//
//            for (int column = 0; column < out.getWidth(); column ++) {
//                byteArray[column+row*out.getWidth()] = rowData[column];
//            }
//        }

//        for (i=0; i < intArray.length; i++)
//        {
//                intArray[i] =  0xFFFFFF00;
//        }



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
                            temp_red += (intArray[ii*width+jj] >> 16 & 0xff) * kernel.getVal(mm, nn);
                            temp_green += (intArray[ii*width+jj] >> 8 & 0xff) * kernel.getVal(mm, nn);
                            temp_blue += (intArray[ii*width+jj] & 0xff) * kernel.getVal(mm, nn);
                            sum+=kernel.getVal(m,n);
                        }
                    }
                }
                //set the new value
                new_color = 0;
                // temp_alpha /= kernel.getRows()*kernel.getCols();
                temp_red = (temp_red/sum)%256;//to prevent sliding
                temp_green = (temp_green/sum)%256;//to prevent sliding
                temp_blue = (temp_blue/sum)%256;//to prevent sliding
                new_color = new_color | (temp_red << 16) | (temp_green << 8) | temp_blue;
                intArray[i*width+j]=new_color;
                //out.setPixel(j, i, new_color);
                // arr[j][i] = new_color;
                // temp_alpha = 0;
                sum=0;
                temp_red = 0;
                temp_green = 0;
                temp_blue = 0;
            }
        }




        Bitmap out2 = Bitmap.createBitmap(intArray, width, height, Bitmap.Config.RGB_565);














//--------------------------------------------------------------------------------------------------//
//        for (i = 0; i < image.getHeight(); ++i)              // rows
//        {
//            for (j = 0; j < image.getWidth(); ++j)          // columns
//            {
//
//
//
//
//                for (m = 0; m < kernel.getRows(); ++m)     // kernel rows
//                {
//                    mm = kernel.getRows() - 1 - m;      // row index of flipped kernel
//
//                    for (n = 0; n < kernel.getCols(); ++n) // kernel columns
//                    {
//                        nn = kernel.getCols() - 1 - n;  // column index of flipped kernel
//
//                        // index of input signal, used for checking boundary
//                        ii = i + m - find_center(kernel);
//                        jj = j + n - find_center(kernel);
//
//                        // ignore input samples which are out of bound
//                        if (ii >= 0 && ii < image.getHeight() && jj >= 0 && jj <  image.getWidth()) {
//                            //get all pixel value and calculate with them
//                           // temp_alpha += (image.getPixel(ii, jj) >> 24 & 0xff) * kernel.getVal(mm, nn);
//                             temp_red += (image.getPixel(jj, ii) >> 16 & 0xff) * kernel.getVal(mm, nn);
//                            temp_green += (image.getPixel(jj, ii) >> 8 & 0xff) * kernel.getVal(mm, nn);
//                            temp_blue += (image.getPixel(jj, ii) & 0xff) * kernel.getVal(mm, nn);
////                            temp_red += (arr[jj][ii] >> 16 & 0xff) * kernel.getVal(mm, nn);
////                            temp_green += (arr[jj][ii] >> 8 & 0xff) * kernel.getVal(mm, nn);
////                            temp_blue += (arr[jj][ii] & 0xff) * kernel.getVal(mm, nn);
//                            sum+=kernel.getVal(m,n);
//                        }
//                    }
//
//                }
//                //set the new value
//                new_color = 0;
//               // temp_alpha /= kernel.getRows()*kernel.getCols();
//
//                temp_red /= sum;
//                temp_green /= sum;
//                temp_blue /= sum;
//                new_color = new_color/* | (temp_alpha << 24)*/ | (temp_red << 16) | (temp_green << 8) | temp_blue;
//                out.setPixel(j, i, new_color);
//               // arr[j][i] = new_color;
//                // temp_alpha = 0;
//                sum=0;
//                temp_red = 0;
//                temp_green = 0;
//                temp_blue = 0;
//            }
//        }

//        for (i = 0; i < image.getHeight(); ++i)              // rows
//        {
//            for (j = 0; j < image.getWidth(); ++j)          // columns
//            {
//                out.setPixel(j,i,arr[j][i]);
//            }
//        }
        return out2;
    }



}
