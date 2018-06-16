package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class DominantColorRemoval {
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();
    ImageView huescale;
    RenderScript rs;
    FilterScreen filterScreenContext;
    private float seek_hue = 0;
    private float seek_range = 0;
    private float[][] floatArrayOrigin;
    private float[][] floatArrayOut;
    private float [] float1dArray;
    private float [] float1dArrayout;



    public DominantColorRemoval (final Bitmap bitmap, final FilterScreen filterScreen){
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
                DominantColorRemoval.MyTaskParams params = new DominantColorRemoval.MyTaskParams(bitmap);
                seek_hue = seekBar.getProgress();
                params.setHue(seek_hue);
                new DominantColorRemoval.Background().execute(params);
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
        huescale = new ImageView(filterScreen);
        huescale.setImageBitmap(BitmapFactory.decodeResource(filterScreen.getResources(), R.drawable.hue));
        SeekBar saturation = new SeekBar(filterScreen);
        saturation.setMax(41);
        saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                DominantColorRemoval.MyTaskParams params = new DominantColorRemoval.MyTaskParams(bitmap);
                seek_range = seekBar.getProgress();
                params.setHue(seek_hue);
                params.setRange(seek_range);
                new DominantColorRemoval.Background().execute(params);
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
        n2.setText("Range");
        names.add(n2);
        slider_array.add(saturation);
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

    class Background extends AsyncTask<DominantColorRemoval.MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(DominantColorRemoval.MyTaskParams... params) {
            ScriptC_DominantColorRemoval parallel_script = new ScriptC_DominantColorRemoval(rs);
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
            parallel_script.set_range(params[0].seek_range);
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
        private float seek_range;
        MyTaskParams(Bitmap bitmap ) {
            this.bitmap = bitmap;
        }
        void setHue (float k){ seek_hue = k; }
        void setRange (float k){ seek_range = k; }

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
