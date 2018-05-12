package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.widget.SeekBar;
import android.widget.TextView;

public class MeanBlur extends InternalFilter {
    Matrix matrix;
    float arr [][] = {{1,1,1},{1,1,1},{1,1,1}};
    MeanBlur(final Bitmap bitmap, final FilterScreen filterScreen)
    {
        matrix = new Matrix(3,3,arr);

        SeekBar s1 = new SeekBar(filterScreen);
        s1.setMax(2);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                filterScreen.RefreshImage(FilterFunction(bitmap));            }

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
        this.addName(n1);
        this.addSeekbar(s1);
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
}
