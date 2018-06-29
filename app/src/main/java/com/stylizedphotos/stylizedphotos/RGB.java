package com.stylizedphotos.stylizedphotos;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RGB {
    public ArrayList<SeekBar> slider_array = new ArrayList<>();
    public ArrayList<TextView> names = new ArrayList<>();
    RenderScript rs;
    private FilterScreen filterScreenContext = null;
    private Context context = null;
    private int seek_red = 0;
    private int seek_green = 0;
    private int seek_blue = 0;

    public RGB (final Bitmap bitmap, final FilterScreen filterScreen){
        filterScreenContext = filterScreen;
        rs = RenderScript.create(filterScreen);
        SeekBar red= new SeekBar(filterScreen);
        red.setMax(100);
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyTaskParams params = new MyTaskParams(bitmap);
                seek_red = seekBar.getProgress();
                params.setRed(seek_red);
                params.setGreen(seek_green);
                params.setBlue(seek_blue);
                new Background().execute(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
        TextView n1 = new TextView(filterScreen);
        n1.setText("Red");
        names.add(n1);
        slider_array.add(red);
        SeekBar green = new SeekBar(filterScreen);
        green.setMax(100);
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyTaskParams params = new MyTaskParams(bitmap);
                seek_green = seekBar.getProgress();
                params.setRed(seek_red);
                params.setGreen(seek_green);
                params.setBlue(seek_blue);
                new Background().execute(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
        TextView n2 = new TextView(filterScreen);
        n2.setText("Green");
        names.add(n2);
        slider_array.add(green);
        SeekBar blue = new SeekBar(filterScreen);
        blue.setMax(100);
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyTaskParams params = new MyTaskParams(bitmap);
                seek_blue = seekBar.getProgress();
                params.setRed(seek_red);
                params.setGreen(seek_green);
                params.setBlue(seek_blue);
                new Background().execute(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
        TextView n3 = new TextView(filterScreen);
        n3.setText("Blue");
        names.add(n3);
        slider_array.add(blue);
    }

    public RGB(Context context){
        this.context = context;
    }

    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<RGB.MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(RGB.MyTaskParams... params) {
            ScriptC_RGB parallel_script = new ScriptC_RGB(rs);
            Bitmap loc_bitmap = params[0].bitmap.copy(params[0].bitmap.getConfig(), true);
            Allocation inalloc = Allocation.createFromBitmap(rs, params[0].bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation outalloc = Allocation.createFromBitmap(rs, loc_bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);

            parallel_script.invoke_setRed(params[0].k[0]);
            parallel_script.invoke_setGreen(params[0].k[1]);
            parallel_script.invoke_setBlue(params[0].k[2]);
            parallel_script.forEach_root(inalloc,outalloc);
            outalloc.copyTo(loc_bitmap);
            return loc_bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (filterScreenContext!=null)
                RefreshImage(bitmap);
        }

    }
    //this is a warper inorder to send the image with the seekbar value
    private static class MyTaskParams {
        Bitmap bitmap;
        int [] k = new int[3];
        MyTaskParams(Bitmap bitmap ) {
            this.bitmap = bitmap;
        }
        void setRed (int k){ this.k[0] = k; }
        void setGreen (int k){
            this.k[1] = k;
        }
        void setBlue (int k){
            this.k[2] = k;
        }
    }

    public Bitmap Preview(Bitmap image) {
        rs = RenderScript.create(context);
        MyTaskParams params = new MyTaskParams(image);
        params.setRed(50);
        params.setGreen(30);
        params.setBlue(100);
        Bitmap preview1 = null;
        try {
             preview1= new Background().execute(params).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        while (preview1 == null) {}
        return preview1;
    }
}