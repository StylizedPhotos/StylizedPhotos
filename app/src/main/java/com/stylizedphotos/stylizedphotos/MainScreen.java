package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class MainScreen extends AppCompatActivity
{
    private static final int RESULT_LOAD_IMAGE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_popup, menu);
        return true;
    }

    public void CallGallery()
    {
    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    // Show only images, no videos or anything else
     /*   intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
    startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data.getData() != null) {

            Uri uri = data.getData();
           /* String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();*/
            try {
                Bitmap bitmap = getBitmapFromUri(uri);
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                // Log.d(TAG, String.valueOf(bitmap));

         /*   ImageView imageView = (ImageView) findViewById(R.id.imageViewFilter);
           // Picasso.get().load(uri).into(imageView);
            imageView.setImageBitmap(bitmap);
            Intent intent = new Intent(this, FilterChooser.class);
            startActivity(intent);*/
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(this, FilterChooser.class);
                intent.putExtra("picture", byteArray);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //ImageView imageView = (ImageView) findViewById(R.id.imageViewFilter);
            //imageView.setImageBitmap(imageBitmap);

            //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(this, FilterChooser.class);
            intent.putExtra("picture", byteArray);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri)throws IOException{
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void CallCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
}
