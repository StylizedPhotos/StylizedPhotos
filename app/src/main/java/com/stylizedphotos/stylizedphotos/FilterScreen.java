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
    public ImageView image_view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);

        Bundle extras = getIntent().getExtras();
        int opcode = extras.getInt("opcode");
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        Bitmap bitmap = null;  //convert the uri to a bitmap
        try {
            bitmap = getBitmapFromUri(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RefreshImage(bitmap);

       /* Intent intent = new Intent(this, FilterDemo.class);  //creating the intent to switch to the FilterChooser activity
        startActivity(intent);  //starting the intent*/
        switch (opcode){
            case 0:
                MeanBlur meanBlur = new MeanBlur(bitmap, this);
                for (int i = 0; i < meanBlur.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(meanBlur.names.get(i));
                    linearLayout.addView(meanBlur.slider_array.get(i));
                }
                break;
            case 1:
                GaussianBlur gaussianBlur = new GaussianBlur(bitmap, this);
                for (int i = 0; i < gaussianBlur.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(gaussianBlur.names.get(i));
                    linearLayout.addView(gaussianBlur.slider_array.get(i));
                }
                break;
            case 2:
                Sharpening Sharpening = new Sharpening(bitmap, this);
                for (int i = 0; i < Sharpening.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(Sharpening.names.get(i));
                    linearLayout.addView(Sharpening.slider_array.get(i));
                }
                break;
            case 3:
                EdgeDetection EdgeDetection = new EdgeDetection(bitmap, this);
                for (int i = 0; i < EdgeDetection.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(EdgeDetection.names.get(i));
                    linearLayout.addView(EdgeDetection.slider_array.get(i));
                }
                break;
            case 4:
                RGB RGB = new RGB(bitmap, this);
                for (int i = 0; i < RGB.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(RGB.names.get(i));
                    linearLayout.addView(RGB.slider_array.get(i));
                }
                break;
            /*case 5:
                MeanBlur filter = new MeanBlur(bitmap, this);
                for (int i = 0; i < filter.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(filter.names.get(i));
                    linearLayout.addView(filter.slider_array.get(i));
                }
                break;
            case 6:
                MeanBlur filter = new MeanBlur(bitmap, this);
                for (int i = 0; i < filter.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(filter.names.get(i));
                    linearLayout.addView(filter.slider_array.get(i));
                }
                break;
            case 7:
                MeanBlur filter = new MeanBlur(bitmap, this);
                for (int i = 0; i < filter.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(filter.names.get(i));
                    linearLayout.addView(filter.slider_array.get(i));
                }
                break;*/
        }
        //FilterDemo demo = new FilterDemo(bitmap, this);
        //ScrollView scroll = (ScrollView) findViewById(R.id.scrollView2);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    public void RefreshImage (Bitmap bitmap)
    {
        image_view = (ImageView) findViewById(R.id.imageViewFilter);
        image_view.setImageBitmap(bitmap);
    }
}
