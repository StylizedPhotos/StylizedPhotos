package com.stylizedphotos.stylizedphotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptC;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import java.io.IOException;
import java.util.ArrayList;

public class FilterDemo {
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();
    RenderScript rs;
    FilterScreen filterScreenContext;

    FilterDemo(final Bitmap bitmap, final FilterScreen filterScreen) {
        filterScreenContext = filterScreen;
        rs = RenderScript.create(filterScreen);
        SeekBar s1 = new SeekBar(filterScreen);
        s1.setMax(2);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
                new Background().execute(params);

                /*filterScreen.RefreshImage(FilterFunction(bitmap));      */      }

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
                MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
                new Background().execute(params);
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
                MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
                new Background().execute(params);            }
        });
        TextView n3 = new TextView(filterScreen);
        n3.setText("s3");
        names.add(n3);
        slider_array.add(s3);
    }

    private static Bitmap FilterFunction(Bitmap image)
    {
        Bitmap loc_bitmap = image.copy(image.getConfig(), true);
        for (int i=0;i<loc_bitmap.getHeight();i++)
        {
            for (int j=0;j<loc_bitmap.getWidth();j++)
            {
                loc_bitmap.setPixel(j,i,0xffffff00);
            }
        }
        return loc_bitmap;
    }

    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(MyTaskParams... params) {
            Bitmap loc_bitmap = params[0].bitmap.copy(params[0].bitmap.getConfig(), true);
            Allocation alloc = Allocation.createFromBitmap(rs, loc_bitmap);
            ScriptC_parallel parallel_script = new ScriptC_parallel(rs);
            parallel_script.forEach_parallel(alloc);
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
}

