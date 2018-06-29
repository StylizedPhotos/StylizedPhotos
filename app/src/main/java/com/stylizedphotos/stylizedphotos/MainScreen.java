package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainScreen extends AppCompatActivity
{
    private static final int RESULT_LOAD_IMAGE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
    Uri photoUri = null;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);//set toolbar to variable
        setSupportActionBar(toolbar);//enable toolbar
        ImageButton GalleryButton = findViewById(R.id.imageButton2); //set gallery button to a variable
        GalleryButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CallGallery();
            }
        });
        ImageButton CameraButton = findViewById(R.id.imageButton3);//set camera button to a variable
        CameraButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                CallCamera();
            }
        });
    }

    public void CallGallery()
    {
    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // Show only images, no videos or anything else
    startActivityForResult(intent, RESULT_LOAD_IMAGE);  //start the activity of the phone gallery
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
        //checking if its the gallery intent
            Uri uri = data.getData();   //get the data into a local variable
            CheckBox perf = findViewById(R.id.low);
            Intent intent = new Intent(this, FilterChooser.class); //creating the intent to switch to the FilterChooser activity
            intent.putExtra("imageUri", uri.toString());  //add the image to the intent
            intent.putExtra("shareUri", "");
            intent.putExtra("perf", perf.isChecked());
            startActivityForResult(intent, RESULT_LOAD_IMAGE);  //starting the intent
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        //checking if its the camera intent
            CheckBox perf = findViewById(R.id.low);
            Intent intent = new Intent(this, FilterChooser.class);  //creating the intent to switch to the FilterChooser activity
            intent.putExtra("imageUri", photoUri.toString());  //add the image to the intent
            intent.putExtra("shareUri", "");
            intent.putExtra("perf", perf.isChecked());
            startActivityForResult(intent, RESULT_LOAD_IMAGE);  //starting the intent
        }
    }

    public void CallCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) // Ensure that there's a camera activity to handle the intent
        {

            File photoFile = null;
            try {
                photoFile = createImageFile();
                photoUri = FileProvider.getUriForFile(MainScreen.this, "com.stylizedphotos.stylizedphotos.provider", photoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);   //adding to the intent the request for full image and the uri for where to store it
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);   //starting the intent
            }
        }
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
