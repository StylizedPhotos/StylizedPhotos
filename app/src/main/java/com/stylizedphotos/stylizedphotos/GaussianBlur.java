package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class GaussianBlur {
    public ArrayList<SeekBar> slider_array = new ArrayList<>();
    public ArrayList<TextView> names = new ArrayList<>();
    RenderScript rs;
    private FilterScreen filterScreenContext;

    GaussianBlur(final Bitmap bitmap, final FilterScreen filterScreen) {
        filterScreenContext = filterScreen;
        rs = RenderScript.create(filterScreen);
        SeekBar s1 = new SeekBar(filterScreen);
        s1.setMax(2);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                filterScreen.RefreshImage(FilterFunction(bitmap));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
        TextView n1 = new TextView(filterScreen);
        n1.setText("Strength");
        names.add(n1);
        slider_array.add(s1);
    }

    private static Bitmap FilterFunction(Bitmap image)
    {
        float arr1[][] = {{1},{76},{1992},{20199},{80576},{127641},{80576},{20199},{1992},{76},{1}};
        float arr2[][] = {{1,76,1992,20199,80576,127641,80576,20199,1992,76,1}};
        Matrix ker1 = new Matrix(11,1,arr1);
        Matrix ker2 = new Matrix(1,11,arr2);
        return Matrix.convolution(ker2, Matrix.convolution(ker1,image,true),true);
    }

    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<GaussianBlur.MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(GaussianBlur.MyTaskParams... params) {
            Bitmap loc_bitmap = params[0].bitmap.copy(params[0].bitmap.getConfig(), true);
            Allocation alloc = Allocation.createFromBitmap(rs, loc_bitmap);
            alloc.copyTo(loc_bitmap);
            return loc_bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            RefreshImage(bitmap);
        }

    }
    //this is a warper inorder to send the image with the seekbar value
    private static class MyTaskParams {
        Bitmap bitmap;
        int k;
        MyTaskParams(Bitmap bitmap, int k ) {
            this.bitmap = bitmap;
            this.k = k;
        }
    }

    public static Bitmap Preview(Bitmap image)
    {
        float arr1[][] = {{1},{76},{1992},{20199},{80576},{127641},{80576},{20199},{1992},{76},{1}};
        float arr2[][] = {{1,76,1992,20199,80576,127641,80576,20199,1992,76,1}};
        Matrix ker1 = new Matrix(11,1,arr1);
        Matrix ker2 = new Matrix(1,11,arr2);
        return Matrix.convolution(ker2, Matrix.convolution(ker1,image,true),true);
    }
}