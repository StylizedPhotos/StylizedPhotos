package com.stylizedphotos.stylizedphotos;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Allocation;

import java.util.ArrayList;

public class MeanBlur {
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();
    FilterScreen filterScreenContext;
    Bitmap tryMe ;

    MeanBlur(final Bitmap bitmap, final FilterScreen filterScreen) {
        filterScreenContext = filterScreen;
        SeekBar s1 = new SeekBar(filterScreen);
        s1.setMax(6);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                filterScreen.RefreshImage(FilterFunction(bitmap,seekBar.getProgress()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
        });
        TextView n1 = new TextView(filterScreen);
        n1.setText("Strength");
        names.add(n1);
        slider_array.add(s1);
    }
    MeanBlur(Bitmap bitmap)
    {
        tryMe = bitmap;
    }

    private Bitmap FilterFunction(Bitmap image, int strength)
    {
        float arr1[][] = {{1},{1},{1},{1},{1},{1},{1},{1},{1},{1},{1},{1},{1}};
        float arr2[][] = {{1,1,1,1,1,1,1,1,1,1,1,1,1}};
        float arr3[][] = {{1},{1},{1},{1},{1},{1},{1},{1},{1},{1},{1}};
        float arr4[][] = {{1,1,1,1,1,1,1,1,1,1,1}};
        float arr5[][] = {{1},{1},{1},{1},{1},{1},{1},{1},{1}};
        float arr6[][] = {{1,1,1,1,1,1,1,1,1}};
        float arr7[][] = {{1},{1},{1},{1},{1},{1},{1}};
        float arr8[][] = {{1,1,1,1,1,1,1}};
        float arr9[][] = {{1},{1},{1},{1},{1}};
        float arr10[][] = {{1,1,1,1,1}};
        float arr11[][] = {{1},{1},{1}};
        float arr12[][] = {{1,1,1}};
        Matrix ker1 = new Matrix(1,1,arr1);
        Matrix ker2 = new Matrix(1,1,arr2);
        switch (strength){
            case(0):{
            }
            case(1): {
                ker1 = new Matrix(3,1,arr1);
                ker2 = new Matrix(1,3,arr2);
                break;
            }
            case(2): {
                ker1 = new Matrix(5,1,arr1);
                ker2 = new Matrix(1,5,arr2);
                break;
            }
            case(3): {
                ker1 = new Matrix(7,1,arr1);
                ker2 = new Matrix(1,7,arr2);
                break;
            }
            case(4): {
                ker1 = new Matrix(9,1,arr1);
                ker2 = new Matrix(1,9,arr2);
                break;
            }
            case(5): {
                ker1 = new Matrix(11,1,arr1);
                ker2 = new Matrix(1,11,arr2);
                break;
            }
            case(6): {
                ker1 = new Matrix(13,1,arr1);
                ker2 = new Matrix(1,13,arr2);
                break;
            }
        }
        if(strength!=0)
            return Matrix.convolution(ker2, Matrix.convolution(ker1,image,true),true);
        else
            return image;
    }

    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    public static Bitmap Preview(Bitmap image)
    {
        float arr1[][] = {{1},{1},{1}};
        float arr2[][] = {{1,1,1}};
        Matrix ker1 = new Matrix(3,1,arr1);
        Matrix ker2 = new Matrix(1,3,arr2);
        return Matrix.convolution(ker2, Matrix.convolution(ker1,image,true),true);
    }
}

