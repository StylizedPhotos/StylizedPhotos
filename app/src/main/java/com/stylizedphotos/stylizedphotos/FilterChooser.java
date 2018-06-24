package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    ArrayList<Button> Buttons = new ArrayList<Button>();
    ImageView image;
    Bitmap bitmap;
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
        filters_names.add("Color Removal");
        filters_names.add("Color Highlight");
        Bundle extras = getIntent().getExtras();
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));
        bitmap = null;  //convert the uri to a bitmap
        try {
            bitmap = getBitmapFromUri(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = (ImageView) findViewById(R.id.imageViewFilter);
        image.setImageBitmap(bitmap);
        for(int i=0;i<filters_names.size();i++) {
            final int opcode = i;
            LinearLayout filters = (LinearLayout) findViewById(R.id.filter_layout);
           Button but = new Button(this);
           // ImageButton but = new ImageButton(this);


            but.setLayoutParams(new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT));
            but.setText(filters_names.get(i));

           // preview.setDensity(1000);
            but.setId(i);
            Buttons.add(but);
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
        Bitmap preview = bitmap.createScaledBitmap(bitmap, 200,200,true);
        Bitmap temp_preview = preview;
        temp_preview = MeanBlur.Preview(preview);
        Drawable d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(0).setBackground(d);

        temp_preview = GaussianBlur.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(1).setBackground(d);

        temp_preview = EdgeDetection.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(2).setBackground(d);

        temp_preview = Sharpening.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(3).setBackground(d);

        RGB rgb = new RGB(this);
        temp_preview = rgb.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(4).setBackground(d);

        HSV hsv = new HSV(this);
        temp_preview = hsv.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(5).setBackground(d);

        DominantColorRemoval remove = new DominantColorRemoval(this);
        temp_preview = remove.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(6).setBackground(d);

        DominantColorHighlight highlight = new DominantColorHighlight(this);
        temp_preview = highlight.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(7).setBackground(d);
        //but.setBackground(d);
        //Button blurbut = (Button)findViewById(R.1);
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

    public void RefreshImage (Bitmap bitmap)
    {
        image = (ImageView) findViewById(R.id.imageViewFilter);
        image.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }
}
