package com.stylizedphotos.stylizedphotos;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class ExternalFilterScreen extends AppCompatActivity implements Serializable {

    public ImageView image_view;
    Bitmap orig_image;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    final int RESULT_RETURN_TO_FILTER_CHOOSER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_filter_screen);

        Bundle extras = getIntent().getExtras();
        int opcode = extras.getInt("opcode");
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));
        final Matrix kernel = (Matrix)extras.getSerializable("matrix");
        final boolean devide = (boolean)extras.get("devide");
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
        RefreshImage(Matrix.convolution(kernel,bitmap,devide));

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
        File file;
        String myPath = "";
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
        {




            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                    File.separator + "Stylized Photos");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }

            if(success==true)
            {file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Stylized Photos"+ File.separator +"StylizedPhotos_" + counter + ".png");
                myPath = "Stylized Photos"+ File.separator +"StylizedPhotos_" + counter + ".png";
            }
            else
            {
                file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "StylizedPhotos_" + counter + ".png");
                myPath = "Stylized Photos"+ File.separator +"StylizedPhotos_" + counter + ".png";
            }
            try {
                OutputStream stream = null;
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
            }
            catch (IOException e) // Catch the exception
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Image Cannot be Saved!",
                        Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(getApplicationContext(), "Can't Save - No permissions!",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void ResetImage()
    {
        image_view.setImageBitmap(orig_image);
    }
}
