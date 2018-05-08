package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class FilterScreen extends AppCompatActivity {
    private Bitmap image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);

        Bundle extras = getIntent().getExtras();
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        Bitmap bitmap = null;  //convert the uri to a bitmap
        try {
            bitmap = getBitmapFromUri(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView image = (ImageView) findViewById(R.id.imageViewFilter);
        image.setImageBitmap(bitmap);

       /* Intent intent = new Intent(this, FilterDemo.class);  //creating the intent to switch to the FilterChooser activity
        startActivity(intent);  //starting the intent*/

        FilterDemo demo = new FilterDemo(bitmap, this);
        //ScrollView scroll = (ScrollView) findViewById(R.id.scrollView2);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);

        for (int i = 0; i < demo.slider_array.size(); i++) {
            linearLayout.addView(demo.names.get(i));
            linearLayout.addView(demo.slider_array.get(i));
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
