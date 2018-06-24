package com.stylizedphotos.stylizedphotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ColorHighlight {
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();
    RenderScript rs;
    FilterScreen filterScreenContext = null;
    Context context = null;
    private float seek_hue = 0;
    private float seek_range = 0;
    private float[][] floatArrayOrigin;
    private float[][] floatArrayOut;
    private float [] float1dArray;
    private float [] float1dArrayout;



    public ColorHighlight (final Bitmap bitmap, final FilterScreen filterScreen){
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
                ColorHighlight.MyTaskParams params = new ColorHighlight.MyTaskParams(bitmap);
                seek_hue = seekBar.getProgress();
                params.setHue(seek_hue);
                params.setRange(seek_range);
                new ColorHighlight.Background().execute(params);
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
        Bitmap hueScale = BitmapFactory.decodeResource(filterScreen.getResources(), R.drawable.hue);
        Drawable d = new BitmapDrawable(filterScreen.getResources(), hueScale);
        hue.setBackground(d);
        SeekBar range = new SeekBar(filterScreen);
        range.setMax(41);
        range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                ColorHighlight.MyTaskParams params = new ColorHighlight.MyTaskParams(bitmap);
                seek_range = seekBar.getProgress();
                params.setHue(seek_hue);
                params.setRange(seek_range);
                new ColorHighlight.Background().execute(params);
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
        slider_array.add(range);
    }

    public ColorHighlight(Context context){
        this.context = context;
    }

    private void RefreshImage(Bitmap bitmap) {
        filterScreenContext.RefreshImage(bitmap);
    }

    class Background extends AsyncTask<ColorHighlight.MyTaskParams, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(ColorHighlight.MyTaskParams... params) {
            ScriptC_ColorHighlight parallel_script = new ScriptC_ColorHighlight(rs);
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

            loc_bitmap = toRGB(floatArrayOut,loc_bitmap.getWidth(),loc_bitmap.getHeight());
            return loc_bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(filterScreenContext!=null)
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

    public Bitmap Preview(Bitmap image) {
        rs = RenderScript.create(context);
        MyTaskParams params = new MyTaskParams(image);
        params.setHue(0);
        params.setRange(10);
        floatArrayOrigin = new float[image.getWidth()*image.getHeight()][3];// 1d array of ints to get image
        floatArrayOut = new float[image.getWidth()*image.getHeight()][3];
        float1dArray = new float[image.getWidth()*image.getHeight()*3];
        float1dArrayout = new float[image.getWidth()*image.getHeight()*3];
        toHSV(image);
        for(int i=0,j=0; j<float1dArray.length;j+=3,i++)
        {
            float1dArray[j] = floatArrayOrigin[i][0];
            float1dArray[j+1] = floatArrayOrigin[i][1];
            float1dArray[j+2] = floatArrayOrigin[i][2];
        }
        Bitmap preview1 = null;
        try {
            preview1= new Background().execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        while (preview1 == null) {}
        return preview1;
    }
}
