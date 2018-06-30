package com.stylizedphotos.stylizedphotos;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilterScreen extends AppCompatActivity {

    public ImageView image_view;
    Bitmap orig_image;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    final int RESULT_RETURN_TO_FILTER_CHOOSER = 2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        int opcode = extras.getInt("opcode");
        final boolean perf = extras.getBoolean("perf");
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));
        Bitmap bitmap = null;  //convert the uri to a bitmap
        try {

            if (perf)
            {
                bitmap = HelpMethods.scaleBitmap( getBitmapFromUri(imageUri));
            }
            else
                bitmap = getBitmapFromUri(imageUri);
            orig_image = bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        RefreshImage(bitmap);
        Button save = findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RequestSaveImage();
            }
        });
        Button reset = findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ResetImage();
            }
        });
        TextView headline = findViewById(R.id.headline);
        switch (opcode){
            case 0:
                MeanBlur meanBlur = new MeanBlur(bitmap, this);
                for (int i = 0; i < meanBlur.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(meanBlur.names.get(i));
                    linearLayout.addView(meanBlur.slider_array.get(i));
                    headline.setText("Mean blur");
                }
                break;
            case 1:
                GaussianBlur gaussianBlur = new GaussianBlur(bitmap, this);
                for (int i = 0; i < gaussianBlur.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(gaussianBlur.names.get(i));
                    linearLayout.addView(gaussianBlur.slider_array.get(i));
                    headline.setText("Gaussian blur");
                }
                break;
            case 2:
                EdgeDetection EdgeDetection = new EdgeDetection(bitmap, this);
                for (int i = 0; i < EdgeDetection.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(EdgeDetection.names.get(i));
                    linearLayout.addView(EdgeDetection.slider_array.get(i));
                    headline.setText("Edge detection");
                }
                break;
            case 3:
                Sharpening Sharpening = new Sharpening(bitmap, this);
                for (int i = 0; i < Sharpening.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(Sharpening.names.get(i));
                    linearLayout.addView(Sharpening.slider_array.get(i));
                    headline.setText("Sharpening");
                }
                break;
            case 4:
                RGB RGB = new RGB(bitmap, this);
                for (int i = 0; i < RGB.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(RGB.names.get(i));
                    linearLayout.addView(RGB.slider_array.get(i));
                    headline.setText("RGB");
                }
                break;
            case 5:
                assert bitmap != null;
                HSV HSV = new HSV(bitmap, this);
                for (int i = 0; i < HSV.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(HSV.names.get(i));
                    linearLayout.addView(HSV.slider_array.get(i));
                    headline.setText("HSV");
                }
                break;
            case 6: {
                assert bitmap != null;
                ColorRemoval ColorRemoval = new ColorRemoval(bitmap, this);
                LinearLayout hue = findViewById(R.id.linearLayoutSeek);
                for (int i = 0; i < ColorRemoval.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(ColorRemoval.names.get(i));
                    linearLayout.addView(ColorRemoval.slider_array.get(i));
                    headline.setText("Color removal");
                }
            }
            break;
            case 7:
                assert bitmap != null;
                ColorHighlight ColorHighlight = new ColorHighlight(bitmap, this);
                LinearLayout hue = findViewById(R.id.linearLayoutSeek);
                for (int i = 0; i < ColorHighlight.slider_array.size(); i++) {
                    LinearLayout linearLayout = findViewById(R.id.linearLayoutSeek);
                    linearLayout.addView(ColorHighlight.names.get(i));
                    linearLayout.addView(ColorHighlight.slider_array.get(i));
                    headline.setText("Color highlight");
                }
                break;
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    public void RefreshImage (Bitmap bitmap)
    {
        image_view = findViewById(R.id.imageViewFilter);
        image_view.setImageBitmap(bitmap);
    }

    public void RequestSaveImage() {
        File file;
        String myPath;
        SharedPreferences pref = getSharedPreferences("save data", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("save data", MODE_PRIVATE).edit();
        int counter = pref.getInt("counter",0);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)//if no permission
        {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)//if no permission
            SaveImage();

    }

    public void SaveImage() {
        File file;
        String myPath;
        SharedPreferences pref = getSharedPreferences("save data", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("save data", MODE_PRIVATE).edit();
        int counter = pref.getInt("counter", 0);
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                File.separator + "Stylized Photos");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        if (success) {
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Stylized Photos" + File.separator + "StylizedPhotos_" + counter + ".png");
            myPath = "Stylized Photos"+ File.separator +"StylizedPhotos_" + counter + ".png";
        } else {
            file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "StylizedPhotos_" + counter + ".png");
            myPath = "Stylized Photos" + File.separator + "StylizedPhotos_" + counter + ".png";
        }
        try {
            OutputStream stream;
            stream = new FileOutputStream(file);
            Bitmap image = ((BitmapDrawable) image_view.getDrawable()).getBitmap();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            editor.putInt("counter", ++counter);
            editor.apply();
            Intent close = new Intent("finish_activity");
            sendBroadcast(close);

            Intent intent = new Intent(getBaseContext(), FilterChooser.class);
            intent.putExtra("imageUri", file.toURI().toString());
            intent.putExtra("shareUri", myPath);
            startActivityForResult(intent, RESULT_RETURN_TO_FILTER_CHOOSER);
            finish();
            Toast.makeText(getApplicationContext(), "Image Saved!",
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Image Cannot be Saved!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                SaveImage();
            } else
                Toast.makeText(getApplicationContext(), "Can't Save - No permission!", Toast.LENGTH_LONG).show();
        }
    }

    public void ResetImage()
    {
        image_view.setImageBitmap(orig_image);
    }
}
