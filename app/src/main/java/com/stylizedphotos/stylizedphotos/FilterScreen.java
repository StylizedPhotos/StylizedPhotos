package com.stylizedphotos.stylizedphotos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class FilterScreen extends AppCompatActivity {
    public ImageView image_view;
    Bitmap orig_image;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    final int RESULT_RETU_RETURN_TO_FILTER_CHOOSER = 2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);

        Bundle extras = getIntent().getExtras();
        int opcode = extras.getInt("opcode");
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        Bitmap bitmap = null;  //convert the uri to a bitmap
        try {
            bitmap = getBitmapFromUri(imageUri);
            orig_image = bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        RefreshImage(bitmap);
        Button save = (Button)findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SaveImage();
            }
        });
        Button reset = (Button)findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ResetImage();
            }
        });
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
                EdgeDetection EdgeDetection = new EdgeDetection(bitmap, this);
                for (int i = 0; i < EdgeDetection.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(EdgeDetection.names.get(i));
                    linearLayout.addView(EdgeDetection.slider_array.get(i));
                }
                break;
            case 3:
                Sharpening Sharpening = new Sharpening(bitmap, this);
                for (int i = 0; i < Sharpening.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(Sharpening.names.get(i));
                    linearLayout.addView(Sharpening.slider_array.get(i));
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
            case 5:
                HSV HSV = new HSV(bitmap, this);
                for (int i = 0; i < HSV.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(HSV.names.get(i));
                    linearLayout.addView(HSV.slider_array.get(i));
                }
                break;
            case 6: {
                DominantColorRemoval DominantColorRemoval = new DominantColorRemoval(bitmap, this);
                LinearLayout hue = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                hue.addView(DominantColorRemoval.huescale);
                for (int i = 0; i < DominantColorRemoval.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(DominantColorRemoval.names.get(i));
                    linearLayout.addView(DominantColorRemoval.slider_array.get(i));
                }
            }
                break;
            case 7:
                DominantColorHighlight DominantColorHighlight = new DominantColorHighlight(bitmap, this);
                LinearLayout hue = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                hue.addView(DominantColorHighlight.huescale);
                for (int i = 0; i < DominantColorHighlight.slider_array.size(); i++) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(DominantColorHighlight.names.get(i));
                    linearLayout.addView(DominantColorHighlight.slider_array.get(i));
                }
                break;
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

    public void SaveImage()
    {
        //File file;
        //String path = Environment.getExternalStorageDirectory().toString()+"StylizedPhotos";
        //file = new File(path, "image"+".jpg");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        else {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "StylizedPhotos.jpg");
            try {
                OutputStream stream = null;
                stream = new FileOutputStream(file);
                Bitmap image = ((BitmapDrawable) image_view.getDrawable()).getBitmap();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.flush();
                stream.close();
                Intent intent = new Intent(getBaseContext(), FilterChooser.class);
                intent.putExtra("imageUri", file.toURI().toString());
                startActivityForResult(intent, RESULT_RETU_RETURN_TO_FILTER_CHOOSER);
            } catch (IOException e) // Catch the exception
            {
                e.printStackTrace();
            }
        }
    }

    public void ResetImage()
    {
        image_view.setImageBitmap(orig_image);
    }
}
