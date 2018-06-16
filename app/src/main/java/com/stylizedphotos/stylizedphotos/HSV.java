package com.stylizedphotos.stylizedphotos;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v8.renderscript.Element;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Allocation;

import java.util.ArrayList;

public class HSV {
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();
    RenderScript rs;
    FilterScreen filterScreenContext;
    private float seek_hue = 0;
    private float seek_saturation = 0;
    private float seek_value = 0;
    private float[][] floatArrayOrigin;
    private float[][] floatArrayOut;
    private float [] float1dArray;
    private float [] float1dArrayout;

    public HSV (final Bitmap bitmap, final FilterScreen filterScreen){
        filterScreenContext = filterScreen;
        rs = RenderScript.create(filterScreen);
        floatArrayOrigin = new float[bitmap.getWidth()*bitmap.getHeight()][3];// 1d array of ints to get image
        floatArrayOut = new float[bitmap.getWidth()*bitmap.getHeight()][3];
        float1dArray = new float[bitmap.getWidth()*bitmap.getHeight()*3];
        float1dArrayout = new float[bitmap.getWidth()*bitmap.getHeight()*3];
        toHSV(bitmap);
        for(int i=0,j=0; j<float1dArray.length;j+=3,i++)
        {
            float1dArray[j] = floatArrayOrigin[i][0];
            float1dArray[j+1] = floatArrayOrigin[i][1];
            float1dArray[j+2] = floatArrayOrigin[i][2];
        }
        SeekBar hue= new SeekBar(filterScreen);
        hue.setMax(359);
        hue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyTaskParams params = new MyTaskParams(bitmap);
                seek_hue = seekBar.getProgress();
                params.setHue(seek_hue);
                params.setSaturation(seek_saturation);
                params.setValue(seek_value);
                new Background().execute(params);
                //Bitmap loc_bitmap = Bitmap.createBitmap(FilterFunction(bitmap.getWidth()*bitmap.getHeight()),bitmap.getWidth(),bitmap.getHeight())
                //filterScreen.RefreshImage();
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
        n1.setText("Hue");
        names.add(n1);
        slider_array.add(hue);
        SeekBar saturation = new SeekBar(filterScreen);
        saturation.setMax(201);
        saturation.setProgress(100);
        saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                MyTaskParams params = new MyTaskParams(bitmap);
                seek_saturation = seekBar.getProgress()-100;
                seek_saturation/=100;
                params.setHue(seek_hue);
                params.setSaturation(seek_saturation);
                params.setValue(seek_value);
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
        n2.setText("Saturation");
        names.add(n2);
        slider_array.add(saturation);
        SeekBar value = new SeekBar(filterScreen);
        value.setMax(100);
        value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                MyTaskParams params = new MyTaskParams(bitmap);
                seek_value = seekBar.getProgress();
                params.setHue(seek_hue);
                params.setSaturation(seek_saturation);
                params.setValue(seek_value);
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
        n3.setText("Value");
        names.add(n3);
        slider_array.add(value);
    }
    private void FilterFunction(int size)
    {
        int i;
        for(i=0;i<size;i++)
        {
            floatArrayOrigin[i][0]+= 50;
        }

    }
    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<HSV.MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(HSV.MyTaskParams... params) {
            ScriptC_HSV parallel_script = new ScriptC_HSV(rs);
            Bitmap loc_bitmap = params[0].bitmap.copy(params[0].bitmap.getConfig(), true);
            Allocation inalloc = Allocation.createFromBitmap(rs, params[0].bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation outalloc = Allocation.createFromBitmap(rs, loc_bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation inarray = Allocation.createSized(rs, Element.F32(rs), loc_bitmap.getWidth()*loc_bitmap.getHeight()*3);
            Allocation outarray = Allocation.createSized(rs, Element.F32(rs), loc_bitmap.getWidth()*loc_bitmap.getHeight()*3);
            inarray.copy1DRangeFrom(0,loc_bitmap.getWidth()*loc_bitmap.getHeight()*3,float1dArray);
            parallel_script.set_inarray(inarray);
            parallel_script.set_outarray(outarray);
            parallel_script.set_width(loc_bitmap.getWidth());
            parallel_script.set_hue(params[0].seek_hue);
            parallel_script.set_saturation(params[0].seek_saturation);
            parallel_script.set_value(params[0].seek_value);
            parallel_script.forEach_root(inalloc,outalloc);
            outarray.copy1DRangeTo(0,loc_bitmap.getWidth()*loc_bitmap.getHeight()*3,float1dArrayout);
            for(int i=0,j=0; j<float1dArrayout.length;j+=3,i++)
            {
                floatArrayOut[i][0]=float1dArrayout[j];
                floatArrayOut[i][1]=float1dArrayout[j+1];
                floatArrayOut[i][2]=float1dArrayout[j+2];
            }
            //outalloc.copyTo(floatArrayOrigin);
            //outalloc.copyTo(loc_bitmap);
            loc_bitmap = toRGB(floatArrayOut,loc_bitmap.getWidth(),loc_bitmap.getHeight());
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
        private float seek_hue;
        private float seek_saturation;
        private float seek_value;
        MyTaskParams(Bitmap bitmap ) {
            this.bitmap = bitmap;
        }
        void setHue (float k){ seek_hue = k; }
        void setSaturation (float k){ seek_saturation = k; }
        void setValue (float k){ seek_value = k; }
    }

    private void toHSV(Bitmap image)
    {
        int i;
        int[] intArray = new int[image.getWidth()*image.getHeight()];// 1d array of ints to get image
        image.getPixels(intArray, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight()); // pixels to int array
        for (i=0;i<image.getHeight()*image.getWidth();i++)
        {
            Color.RGBToHSV((Color.red(intArray[i])),(Color.green(intArray[i])) ,(Color.blue(intArray[i])), floatArrayOrigin[i]);
            floatArrayOrigin[i][2]*=100;
        }
    }

    private Bitmap toRGB(float [][] arr,int width, int hight)
    {
        int i;
        int[] intArray = new int[arr.length];// 1d array of ints to get image
         // pixels to int array
        for (i=0;i<arr.length;i++)
        {
            intArray[i] = Color.HSVToColor(floatArrayOut[i]);
        }
        Bitmap temp_image = Bitmap.createBitmap(intArray, width, hight, Bitmap.Config.ARGB_8888);
        return temp_image;
    }
}