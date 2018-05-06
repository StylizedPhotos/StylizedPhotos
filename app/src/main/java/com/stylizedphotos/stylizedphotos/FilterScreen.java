package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

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
    }
    /*FilterScreen(Bitmap image)
    {
        FilterDemo demo = new FilterDemo(image);
        demo.slider_array.size();
    }*/

    private Bitmap getBitmapFromUri(Uri uri)throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
