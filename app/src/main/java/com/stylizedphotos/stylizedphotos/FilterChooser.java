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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class FilterChooser extends AppCompatActivity
{
    //int number_of_filters=8;// a constant that change when the user add an external filter
    //private ImageButton mImageButton;
    ArrayList<String> filters_names = new ArrayList<String>();
    private static final int RESULT_OPEN_FILTER_SCREEN = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_chooser);



        filters_names.add("Mean blur");
        filters_names.add("Gaussian blur");
        filters_names.add("Edge Detection");
        filters_names.add("Sharpening");
        filters_names.add("RGB");
        filters_names.add("HSV");
        filters_names.add("Dominant Color Removal");
        filters_names.add("Dominant Color Highlight");
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
        for(int i=0;i<filters_names.size();i++) {
            final int opcode = i;
            LinearLayout filters = (LinearLayout) findViewById(R.id.filter_layout);
           Button but = new Button(this);
           // ImageButton but = new ImageButton(this);


            but.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            but.setText(filters_names.get(i));
            but.setBackground((this.getResources().getDrawable(R.drawable.button)));
            but.setId(i);
            filters.addView(but);
            but.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), FilterScreen.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("opcode", opcode);
                    startActivityForResult(intent, RESULT_OPEN_FILTER_SCREEN);
                }
            });
        }
        //Button FilterButton = (Button) findViewById(R.id.button1);

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
