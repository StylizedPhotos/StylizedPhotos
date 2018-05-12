package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;

public class InternalFilter extends Filter{

    InternalFilter(){
        super();
    }

    InternalFilter(Bitmap bitmap, final FilterScreen filterScreen){
        super(bitmap, filterScreen);
    }

}
