package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class RGB {
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();
    RenderScript rs;
    FilterScreen filterScreenContext;

    public RGB (final Bitmap bitmap, final FilterScreen filterScreen){
        filterScreenContext = filterScreen;
        rs = RenderScript.create(filterScreen);
        SeekBar red= new SeekBar(filterScreen);
        red.setMax(255);
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyTaskParams params = new MyTaskParams(bitmap);
                params.setRed(seekBar.getProgress());
                new Background().execute(params);

                //filterScreen.RefreshImage(FilterFunction(bitmap));
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
        n1.setText("Red");
        names.add(n1);
        slider_array.add(red);
        SeekBar green = new SeekBar(filterScreen);
        green.setMax(255);
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                MyTaskParams params = new MyTaskParams(bitmap);
                params.setGreen(seekBar.getProgress());
                new Background().execute(params);
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
        n2.setText("Green");
        names.add(n2);
        slider_array.add(green);
        SeekBar blue = new SeekBar(filterScreen);
        blue.setMax(255);
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                MyTaskParams params = new MyTaskParams(bitmap);
                params.setBlue(seekBar.getProgress());
                new Background().execute(params);
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
        n3.setText("Blue");
        names.add(n3);
        slider_array.add(blue);
    }
    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<RGB.MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(RGB.MyTaskParams... params) {
            Bitmap loc_bitmap = params[0].bitmap.copy(params[0].bitmap.getConfig(), true);
            Allocation inalloc = Allocation.createFromBitmap(rs, params[0].bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation outalloc = Allocation.createFromBitmap(rs, loc_bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            ScriptC_parallel parallel_script = new ScriptC_parallel(rs);
            parallel_script.set_red(params[0].k[0]);
            parallel_script.set_green(params[0].k[1]);
            parallel_script.set_blue(params[0].k[2]);
            //parallel_script.forEach_parallel(outalloc);
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
        int [] k = new int[3];
        MyTaskParams(Bitmap bitmap ) {
            this.bitmap = bitmap;
            /*this.k[0] = k[0];
            this.k[1] = k[1];
            this.k[2] = k[2];*/
        }
        void setRed (int k){
            this.k[0] = k;
        }
        void setGreen (int k){
            this.k[1] = k;
        }
        void setBlue (int k){
            this.k[2] = k;
        }
    }
}
