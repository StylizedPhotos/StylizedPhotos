package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Sharpening {
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();
    RenderScript rs;
    FilterScreen filterScreenContext;

    Sharpening(final Bitmap bitmap, final FilterScreen filterScreen) {
        filterScreenContext = filterScreen;
        rs = RenderScript.create(filterScreen);
        SeekBar s1 = new SeekBar(filterScreen);
        s1.setMax(2);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //  MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
                // new Background().execute(params);

                filterScreen.RefreshImage(FilterFunction(bitmap));
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
        float arr[][]={ {0,-1,0},
                        {-1,5,-1},
                        {0,-1,0}};
        Matrix ker = new Matrix (3,3,arr);
      //  Bitmap temp_bitmap = HelpMethods.pad(image,1,1);
      //  temp_bitmap = Matrix.convolution(ker,temp_bitmap,false);
     //   temp_bitmap = Bitmap.createBitmap(temp_bitmap, 1, 1, image.getWidth(), image.getHeight());
        return Matrix.convolution(ker, image,false);
    }

    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<Sharpening.MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Sharpening.MyTaskParams... params) {
            Bitmap loc_bitmap = params[0].bitmap.copy(params[0].bitmap.getConfig(), true);
            Allocation alloc = Allocation.createFromBitmap(rs, loc_bitmap);
            // ScriptC_parallel parallel_script = new ScriptC_parallel(rs);
            //parallel_script.forEach_parallel(alloc);
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