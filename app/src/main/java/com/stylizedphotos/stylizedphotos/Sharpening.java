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
        s1.setMax(3);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //  MyTaskParams params = new MyTaskParams(bitmap,seekBar.getProgress());
                // new Background().execute(params);

                filterScreen.RefreshImage(FilterFunction(bitmap,seekBar.getProgress()));
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
    }

    private static Bitmap FilterFunction(Bitmap image,int strength)
    {
        float arr1[][]={ {0,-1,0},
                        {-1,5,-1},
                        {0,-1,0}};

        float arr2[][]={{0,0,-1,0,0},
                {0,0,-1,0,0},
                {-1,-1,9,-1,-1},
                {0,0,-1,0,0},
                {0,0,-1,0,0}};

        float arr3[][]={{0,0,0,-1,0,0,0},
                {0,0,0,-1,0,0,0},
                {0,0,0,-1,0,0,0},
                {-1,-1,-1,13,-1,-1,-1},
                {0,0,0,-1,0,0,0},
                {0,0,0,-1,0,0,0},
                {0,0,0,-1,0,0,0}};
        Matrix ker = new Matrix(0, 0,null );
        switch (strength){
            case(0):{
            }
            case(1): {
                ker = new Matrix(3, 3, arr1);
                break;
            }
            case(2): {
                ker = new Matrix(5, 5, arr2);
                break;
            }
            case(3): {
                ker = new Matrix(7, 7, arr3);
                break;
            }
        }
        if(strength!=0)
            return Matrix.convolution(ker,image,false);
        else
            return image;
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

    public static Bitmap Preview(Bitmap image)
    {
        float arr[][]={ {0,-1,0},
                {-1,5,-1},
                {0,-1,0}};
        Matrix ker1 = new Matrix(3,3,arr);
        return Matrix.convolution(ker1,image,false);
    }
}