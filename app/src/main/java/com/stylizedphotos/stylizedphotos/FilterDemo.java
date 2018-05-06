package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class FilterDemo {
    private Bitmap image;
    public ArrayList<Slider> slider_array;

    FilterDemo(Bitmap image)
    {
        Slider s1 = new Slider("s1",2,0);
        slider_array.add(s1);
        Slider s2 = new Slider("s2",4,0);
        slider_array.add(s2);
        Slider s3 = new Slider("s3",6,0);
        slider_array.add(s3);
        this.image = image.copy(image.getConfig(),true);
        Bitmap Filtered_image = FilterFuncation(image);
    }

    Bitmap FilterFuncation(Bitmap image)
    {
        return image;
    }

}
