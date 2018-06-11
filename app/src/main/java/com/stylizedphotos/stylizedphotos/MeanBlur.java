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
    RenderScript rs;
    FilterScreen filterScreenContext;

    MeanBlur(final Bitmap bitmap, final FilterScreen filterScreen) {
        filterScreenContext = filterScreen;
        rs = RenderScript.create(filterScreen);
        SeekBar s1 = new SeekBar(filterScreen);
        s1.setMax(2);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
              //  MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
               // new Background().execute(params);
              //  filterScreen.RefreshImage(FilterFunction(bitmap));
                MeanBlur.MyTaskParams params = new MeanBlur.MyTaskParams(bitmap);
                new MeanBlur.Background().execute(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // filterScreen.RefreshImage(FilterFunction(bitmap));
            }
        });
        TextView n1 = new TextView(filterScreen);
        n1.setText("s1");
        names.add(n1);
        slider_array.add(s1);
        SeekBar s2 = new SeekBar(filterScreen);
        s2.setMax(4);
        s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
               // new Background().execute(params);
                //filterScreen.RefreshImage(FilterFunction(bitmap));
            }
        });
        TextView n2 = new TextView(filterScreen);
        n2.setText("s2");
        names.add(n2);
        slider_array.add(s2);
        SeekBar s3 = new SeekBar(filterScreen);
        s3.setMax(6);
        s3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
               // new Background().execute(params);
                }
        });
        TextView n3 = new TextView(filterScreen);
        n3.setText("s3");
        names.add(n3);
        slider_array.add(s3);
    }

    private static Bitmap FilterFunction(Bitmap image)
    {
        float arr1[][] = {{1},{1},{1},{1},{1},{1},{1},{1},{1},{1},{1}};
        float arr2[][] = {{1,1,1,1,1,1,1,1,1,1,1}};
        Matrix ker1 = new Matrix(11,1,arr1);
        Matrix ker2 = new Matrix(1,11,arr2);


        return Matrix.convolution(ker2, Matrix.convolution(ker1,image));
        /*
        Bitmap loc_bitmap = image.copy(image.getConfig(), true);
        for (int i=0;i<loc_bitmap.getHeight();i++)
        {
            for (int j=0;j<loc_bitmap.getWidth();j++)
            {
                loc_bitmap.setPixel(j,i,0xffffff00);
            }
        }
        return loc_bitmap;
        */
    }

    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(MyTaskParams... params) {
            ScriptC_MeanBlur parallel_script = new ScriptC_MeanBlur(rs);
            Bitmap loc_bitmap = params[0].bitmap.copy(params[0].bitmap.getConfig(), true);
            Allocation inalloc = Allocation.createFromBitmap(rs, params[0].bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation outalloc = Allocation.createFromBitmap(rs, loc_bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);

            parallel_script.set_gIn(inalloc);
            parallel_script.set_gOut(outalloc);
            parallel_script.set_height(loc_bitmap.getHeight());
            parallel_script.set_width(loc_bitmap.getWidth());
            parallel_script.forEach_root(inalloc,outalloc);
            outalloc.copyTo(loc_bitmap);
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
        float [][] kernel;
        MyTaskParams(Bitmap bitmap)
        {
            this.bitmap = bitmap;
        }
    }
}

