package com.stylizedphotos.stylizedphotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class FilterDemo {
    private Bitmap imageloc;
    public ArrayList<SeekBar> slider_array = new ArrayList<SeekBar>();
    public ArrayList<TextView> names = new ArrayList<TextView>();

    FilterDemo(final Bitmap image, Context context) {
        SeekBar s1 = new SeekBar(context);
        s1.setMax(2);
        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                imageloc = FilterFuncation(image);
            }
        });
        TextView n1 = new TextView(context);
        n1.setText("s1");
        names.add(n1);
        slider_array.add(s1);
        SeekBar s2 = new SeekBar(context);
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
                imageloc = FilterFuncation(image);
            }
        });
        TextView n2 = new TextView(context);
        n2.setText("s2");
        names.add(n2);
        slider_array.add(s2);
        SeekBar s3 = new SeekBar(context);
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
                imageloc = FilterFuncation(image);
            }
        });
        TextView n3 = new TextView(context);
        n3.setText("s3");
        names.add(n3);
        slider_array.add(s3);
        imageloc = image.copy(image.getConfig(), true);
        Bitmap Filtered_image = FilterFuncation(image);
    }

    private static Bitmap FilterFuncation(Bitmap image)
    {
        return image;
    }
}

