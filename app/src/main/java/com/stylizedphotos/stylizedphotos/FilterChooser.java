package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class FilterChooser extends AppCompatActivity
{
    private static final int RESULT_OPEN_FILTER_SCREEN = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_chooser);
        Bundle extras = getIntent().getExtras();
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Bitmap bitmap = null;  //convert the uri to a bitmap
        try {
            bitmap = getBitmapFromUri(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView image = (ImageView) findViewById(R.id.imageViewFilter);

        image.setImageBitmap(bitmap);

        Button FilterButton = (Button) findViewById(R.id.button1);
        FilterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FilterScreen.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivityForResult(intent, RESULT_OPEN_FILTER_SCREEN);
            }
        });
    }

    private Bitmap getBitmapFromUri(Uri uri)throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}
